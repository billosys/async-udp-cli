(ns billo.udp.client.util
  (:require
    [clojure.string :as string]
    [taoensso.timbre :as log]))

(defn args->str
  [args]
  (str (string/join " "args) "\n"))

(defn wait
  ([]
    (wait 3000))
  ([timeout]
    (js/setTimeout #(log/warn "UDP client timed out.")
                   timeout)))

(defn obj->clj
  [obj]
  (-> (fn [result key]
        (let [v (goog.object/get obj key)]
          (if (= "function" (goog/typeOf v))
            result
            (assoc result key v))))
      (reduce {} (.getKeys goog/object obj))))

(defn get-env
  ([]
    (obj->clj (.-env js/process)))
  ([var-name]
    (or (get (get-env) var-name)
        (log/error (str "ENV has no variabel named '" k "'")))))

(defn get-log-level
  [default]
  (if-let [level (get-env "LOG_LEVEL")]
    (if (empty? level)
      default
      (keyword level))
    default))

(defn set-log-level
  []
  (log/set-level! (get-log-level :info)))
