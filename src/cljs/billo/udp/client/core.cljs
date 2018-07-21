(ns billo.udp.client.core
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
  (log/debug "Listening ...")
  (.on client "listening" callback))

(defn send
  [client port data]
  (let [buf (js/Buffer. data)]
    (log/debug "Sending message ...")
    (log/trace (str "Data:" data))
    (.send client
           buf
           0
           buf.length
           port)))

(defn close
  [client]
  (log/debug "Disconnecting ...")
  (.close client))

(defn on-receive
  [client callback]
  (log/debug "Receiving data ...")
  (.on client "message" callback))
