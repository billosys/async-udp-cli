(ns billo.example.cli
  (:require
    [billo.udp.client.core :as udp]
    [billo.udp.client.util :as util]
    [cljs.nodejs :as node]
    [clojusc.twig :as logger]
    [taoensso.timbre :as log]))

;;; CLI setup and functions

(node/enable-util-print!)

(logger/set-level! [billo] :debug)

;;; UDP Callback

(defn handle-receive
  [client data]
  (let [buffer (js/Buffer. data)]
    (log/debug "Received data:" data)
    (udp/close client)
    (log/debug "Disconnected.")
    (println (str data))
    (.exit node/process)))

;;; Main

(defn -main
  [& args]
  (log/debug "Got args:" args)
  (let [client (udp/client)
        port (js/parseInt (util/get-env "UDP_PORT"))
        data (util/args->str args)]
    (udp/on-receive client #(handle-receive client %))
    (udp/send client port data)
    (util/wait)))

(set! *main-cli-fn* -main)
