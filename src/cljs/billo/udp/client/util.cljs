(ns billo.udp.client.util
  (:require
    [clojure.string :as string]))

(defn args->str
  [args]
  (str (string/join " "args) "\n"))

(defn wait
  ([]
    (wait 3000))
  ([timeout]
    (js/setTimeout #(println "UDP client timed out.")
                   timeout)))

(defn get-env
  ([]
    (js->clj (.env node/process)))
  ([var-name]
    (or (get (get-env var-name))
        (throw (str "ENV has no variabel named '" k "'")))))
