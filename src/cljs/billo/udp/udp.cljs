(ns timi.client.udp
  (:require
    [cljs.nodejs :as node]
    [taoensso.timbre :as log]))

(def dgram (node/require "dgram"))

(defn client
  []
  (log/debug "Creating datagram socket ...")
  (.createSocket dgram "udp4"))

(defn on-listen
  [client callback]
  (.on client "listening" callback))

(defn send
  [client config data]
  (let [buf (js/Buffer. data)
        cfg (get-in config [:cli :server])]
    (log/debug cfg)
    (log/debug "Sending data:" data)
    (.send client
           buf
           0
           buf.length
           (get-in config [:cli :server :port]))))

(defn close
  [client]
  (log/debug "Disconnecting ...")
  (.close client))

(defn on-receive
  [client callback]
  (log/debug "Receiving data ...")
  (.on client "message" callback))
