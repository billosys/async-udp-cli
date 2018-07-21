(ns billo.udp.server
  (:require
    [billo.udp.const :as const]
    [billo.udp.util :as util]
    [clojure.core.async :as async]
    [inet.address :as inet]
    [sockets.datagram.packet :as packet]
    [sockets.datagram.socket :as socket])
  (:import
    (clojure.lang Keyword)
    (java.net SocketException)))

(defn receive
  [sock]
  (try
    (socket/receive sock const/max-packet-size)
    (catch SocketException e nil)))

(defn packet-reader
  [sock]
  (let [in (async/chan)]
    (async/go-loop []
      (when-let [pkt (receive sock)]
        (async/>! in {:remote-addr (packet/address pkt)
                      :remote-port (packet/port pkt)
                      :data (util/bytes->str (packet/data pkt))})
        (recur)))
    in))

(defn packet-writer
  [sock opts]
  (let [out (async/chan)]
    (async/go-loop []
      (let [msg (async/<! out)
            parser-fn (:parser-fn opts)
            parser-opts (:parser-opts opts)
            data (:data msg)
            pkt-text (parser-fn data parser-opts)
            pkt-data (util/->bytes pkt-text)
            pkt (packet/create pkt-data
                               (count pkt-data)
                               (:remote-addr msg)
                               (:remote-port msg))]
        (socket/send sock pkt))
      (recur))
    out))

(defn io-service
  [in out]
  (async/go-loop []
    (let [dest (async/<! in)]
      (async/>! out dest)
      (recur))))

(defn run
  "Required options:
  * `:port` - UDP port
  * `:parser-fn` - the function that will parse the data sent over UDP; this
                   function expects two args: the data and a map of options
                   that may be used by the parser function.
  * `:parser-opts` - the options to pass as the second argument to the parser
                     function."
  [opts]
  (let [sock (socket/create (:port opts))
        in (packet-reader sock)
        out (packet-writer sock opts)]
    (async/go
      (io-service in out))
    (fn [] (socket/close sock))))
