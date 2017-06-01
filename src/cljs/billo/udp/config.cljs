(ns timi.client.config
  (:require
    [clojure.string :as string]
    [clojusc.twig :as logger]
    [taoensso.timbre :as log]
    [timi.client.tcp :as tcp]))

(def fs (js/require "fs"))

(defn read-config
  ([]
    ;; XXX add logic here for different environments/profiles
    (read-config "config/local/config.edn"))
  ([filename]
    (->> "utf-8"
         (.readFileSync fs filename)
         (cljs.reader/read-string))))

(def data (read-config))
