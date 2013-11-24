(defproject ez-snake "0.1.0-SNAPSHOT"
  :description "Snake game"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2030"]
                 [org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]]

  :plugins [[lein-cljsbuild "1.0.0"]]

  :source-paths ["src"]

  :cljsbuild { 
    :builds [{:id "ez-snake"
              :source-paths ["src"]
              :compiler {
                :output-to "ez_snake.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})