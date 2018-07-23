(ns billo.example.cli-server
  (:require
    [billo.udp.server.core :as server]
    [com.stuartsierra.component :as component]
    [taoensso.timbre :as log]
    [trifl.java :as java]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn cli-parser
  [data options]
  (log/info "Simple CLI parser got data: " data)
  (log/info "And options: " options)
  (str "Simple cli-parser echoing back data: " data))

(defrecord CLIServer []
  component/Lifecycle

  (start [component]
    (log/info "Starting CLI server ...")
    (let [options {:port (Integer/parseInt (System/getenv "UDP_PORT"))
                   :parser-fn cli-parser
                   :parser-opts {:thing1 "cat" :thing2 "hat"}}
          server (server/run options)]
      (log/trace "Using server options:" options)
      (log/trace "Component keys:" (keys component))
      (log/debug "Successfully created server:" server)
      (assoc component :cli server)))

  (stop [component]
    (log/info "Stopping CLI server ...")
    (log/trace "Component keys" (keys component))
    (when-let [server (:cli component)]
      (log/debug "Using server object:" server)
      (server))
    (assoc component :cli nil)))

(defn new-server []
  (->CLIServer))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   System   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn init
  []
  (component/system-map
    :cli (component/using (new-server) [])))

(defn -main
  [& args]
  (let [system (init)]
    (component/start system)
    ;; Setup interrupt/terminate handling
    (java/add-shutdown-handler #(component/stop system))
    (java/join-current-thread)))
