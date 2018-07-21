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
    (println "ENV: " (.-env js/process))
    (println "env: " (obj->clj (.-env js/process)))
    (obj->clj (.-env js/process)))
  ([var-name]
    (or (get (get-env) var-name)
        (throw (str "ENV has no variabel named '" k "'")))))
