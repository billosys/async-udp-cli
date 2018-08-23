(defproject systems.billo/async-udp-cli "0.1.0-SNAPSHOT"
  :description "A Clojure utility library for faster CLIs with async UDP clients/servers"
  :url "https://github.com/billosys/async-udp-cli"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :min-lein-version "2.8.1"
  :dependencies [
    [clojusc/trifl "0.3.0"]
    [org.clojure/clojure "1.9.0"]
    [org.clojure/core.async "0.4.474"]
    [systems.billo/inet-address "0.1.1"]
    [systems.billo/sockets "0.1.1"]]
  :profiles {
    :ubercompile {
      :aot :all}
    :lint {
      :plugins [
        [jonase/eastwood "0.2.8"]
        [lein-ancient "0.6.15"]]}
    :server-example {
      :dependencies [
        [clojusc/trifl "0.3.0"]
        [clojusc/twig "0.3.2"]
        [com.stuartsierra/component "0.3.2"]]
        :main billo.example.cli-server}
    :clojure {
      :source-paths ["src/clj"]}
    :client-example {
      :dependencies [
        [com.taoensso/timbre "4.10.0"]]
      :plugins [
        [lein-shell "0.5.0"]]}
    :clojurescript {
      :source-paths ["src/cljs"]
      :dependencies [
        [org.clojure/clojurescript "1.10.339"]]
      :plugins [
        [lein-cljsbuild "1.1.7"]]
      :cljsbuild {
        :builds
          [{:id "cli"
            :source-paths ["src/cljs"]
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
    "jar" ["with-profile" "+clojure" "jar"]
    "check-vers" ["with-profile" "+lint" "ancient" "check" ":all"]
    "check-jars" ["with-profile" "+lint" "do"
      ["deps" ":tree"]
      ["deps" ":plugin-tree"]]
    "check-deps" ["do"
      ["check-jars"]
      ["check-vers"]]
    "clean-clj" ["with-profile" "+clojure,+server-example" "clean"]
    "clean-cljs" ["with-profile" "+clojurescript,+client-example" "do"
      ["clean"]
      ["shell" "rm" "-f" "bin/example"]]
    "clean-all" ["do"
      ["clean-clj"]
      ["clean-cljs"]]
    "example-server" ["with-profile" "+clojure,+server-example" "trampoline" "run"]
    "build-cli" ["with-profile" "+clojurescript,+client-example" "do"
      ["cljsbuild" "once" "cli"]
      ["shell" "chmod" "755" "bin/example"]]
    "clean-build-cli" ["with-profile" "+clojurescript,+client-example" "do"
      ["clean-cljs"]
      ["build-cli"]]
    "deploy" ["with-profile" "+clojure,+clojurescript" "deploy"]})
