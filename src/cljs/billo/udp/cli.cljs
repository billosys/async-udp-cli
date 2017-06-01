(ns timi.client.cli
  (:require
    [cljs.nodejs :as node]
    [clojure.string :as string]
    [clojusc.twig :as logger]
    [taoensso.timbre :as log]
    [timi.client.config :as config]
    [timi.client.udp :as udp]))

;;; CLI setup and functions

(node/enable-util-print!)

(logger/set-level! (get-in config/data [:cli :client :log :ns])
                   (get-in config/data [:cli :client :log :level]))

(defn args->str
  [args]
  (str (string/join " "args) "\n"))

(defn wait
  []
  (js/setTimeout #(log/info "UDP client timed out.")
                 3000))

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
        data (args->str args)]
    (udp/on-receive client #(handle-receive client %))
    (udp/send client config/data data)
    (wait)))

(set! *main-cli-fn* -main)
