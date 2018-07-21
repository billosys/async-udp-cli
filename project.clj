(defproject systems.billo/async-udp "0.1.0-SNAPSHOT"
  :description "A Clojure utility library for async UDP clients/servers"
  :url "https://github.com/billosys/async-udp"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :min-lein-version "2.8.1"
  :dependencies [
    [org.clojure/clojure "1.9.0"]]
  :profiles {
    :ubercompile {
      :aot :all}
    :lint {
      :plugins [
        [jonase/eastwood "0.2.8"]
        [lein-ancient "0.6.15"]]}
    :clojure {
      :source-paths ["src/clj"]
      :dependencies [
        [clojusc/trifl "0.3.0"]
        [org.clojure/core.async "0.4.474"]
        [systems.billo/inet-address "0.1.1"]
        [systems.billo/sockets "0.1.1"]]}
    :client-example {
      :dependencies [
        [clojusc/twig "0.3.3"]]}
    :clojurescript {
      :dependencies [
        [org.clojure/clojurescript "1.10.339"]]
      :plugins [
        [lein-cljsbuild "1.1.7"]]
      :cljsbuild {
        :builds
          [{:id "cli"
            :source-paths ["src/cljs/billo"]
            :compiler {
              :output-to "bin/example"
              :output-dir "target/cljs/billo"
              :optimizations :simple
              :pretty-print true
              :main billo.example.cli
              :target :nodejs
              :verbose true}}]}}}
  :aliases {
    "ubercompile" ["with-profile" "+ubercompile,+clojure" "uberjar"]
    "uberjar" ["with-profile" "+clojure" "uberjar"]
    "deploy" ["with-profile" "+clojure" "deploy"]
    "build-bin" ["with-profile" "+clojurescript,+client-example"
                 "cljsbuild" "once" "cli"]
    "check-vers" ["with-profile" "+lint" "ancient" "check" ":all"]
    "check-jars" ["with-profile" "+lint" "do"
      ["deps" ":tree"]
      ["deps" ":plugin-tree"]]
    "check-deps" ["do"
      ["check-jars"]
      ["check-vers"]]})
