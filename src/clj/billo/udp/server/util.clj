(ns billo.udp.util
  (:import
    (clojure.lang Keyword)))

(def max-packet-size 4096)

(defmulti ->bytes type)

(defmethod ->bytes String
  [text]
  (let [bytes (.getBytes text "UTF-8")]
    (byte-array (count bytes) bytes)))

(defmethod ->bytes Keyword
  [data]
  (->bytes (str data)))

(defn bytes->str
  [data]
  (new String data "UTF-8"))

