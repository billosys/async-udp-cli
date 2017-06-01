(ns timi.server.cli.udp
  (:require
    [clojure.core.async :as async]
    [inet.address :as inet]
    [sockets.datagram.packet :as packet]
    [sockets.datagram.socket :as socket]
    [taoensso.timbre :as log]
    [timi.server.cli.core :as cli])
  (:import
    (clojure.lang Keyword)
    (java.net SocketException)))

(def max-packet-size 4096)

(defmulti ->bytes type)

(defmethod ->bytes String
  [text]
  (let [bytes (.getBytes text "UTF-8")]
    (byte-array (count bytes) bytes)))

(defmethod ->bytes Keyword
  [data]
  (->bytes (str data)))

(defn bytes->str
  [data]
  (new String data "UTF-8"))

(defn cli-service
  [in out]
  (async/go-loop []
    (let [dest (async/<! in)]
      (async/>! out dest)
      (recur))))

(defn receive
  [sock]
  (try
    (socket/receive sock max-packet-size)
    (catch SocketException e nil)))

(defn packet-reader
  [sock]
  (let [in (async/chan)]
    (async/go-loop []
      (when-let [pkt (receive sock)]
        (async/>! in {:remote-addr (packet/address pkt)
                      :remote-port (packet/port pkt)
                      :command (bytes->str (packet/data pkt))})
        (recur)))
    in))

(defn cli-writer
  [config sock]
  (let [out (async/chan)]
    (async/go-loop []
      (let [msg (async/<! out)
            pkt-text (cli/run config (:command msg))
            pkt-data (->bytes pkt-text)
            pkt (packet/create pkt-data
                               (count pkt-data)
                               (:remote-addr msg)
                               (:remote-port msg))]
        (socket/send sock pkt))
      (recur))
    out))

(defn serve
  [config]
  (let [sock (-> config
                 (get-in [:cli :server :port])
                 (socket/create))
        in (packet-reader sock)
        out (cli-writer config sock)]
    (async/go
      (cli-service in out))
    (fn [] (socket/close sock))))
