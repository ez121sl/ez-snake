(defproject ez-snake "0.1.0-SNAPSHOT"
  :description "Snake game"
  :url "https://bitbucket.org/ezaretsky/ez-snake"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2080"]
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
                :source-map "ez_snake.js.map"}}]})
