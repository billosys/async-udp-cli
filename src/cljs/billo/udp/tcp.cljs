(ns timi.client.tcp
  (:require
    [cljs.nodejs :as node]
    [taoensso.timbre :as log]))

(def net (node/require "net"))

(defn connect
  [config]
  (log/debug "Connecting ...")
  (.createConnection net (get-in config [:cli :server :port])))

(defn on-connect
  [client callback]
  (.on client "connect" callback))

(defn on-receive
  [client callback]
  (log/debug "Receiving data ...")
  (.on client "data" callback))

(defn send
  [client data]
  (log/debug "Sending data:" data)
  (.write client data))

(defn disconnect
  [client]
  (log/debug "Disconnecting ...")
  (.end client))
