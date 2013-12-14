(defproject ez-snake "0.2.0-SNAPSHOT"
  :description "Snake game - experiments with ClojureScript"
  :url "https://bitbucket.org/ezaretsky/ez-snake"
  
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2080"]
                 [org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]]
  
  :plugins [[lein-cljsbuild "1.0.0"]]
  
  :hooks [leiningen.cljsbuild]
  
  :source-paths ["jvm-src"]
  
  :jar-exclusions [#"^public/dev-js/", #".gitignore$"]
  
  :cljsbuild { :builds 
               { :dev 
                 { :source-paths ["src"]
                   :compiler 
                   { :output-to "resources/public/dev-js/ez_snake.js"
                     :output-dir "resources/public/dev-js/"
                     :optimizations :none
                     :source-map true }},
                 :release
                 { :source-paths ["src"]
                   :jar true
                   :compiler 
                   { :output-to "resources/public/js/ez_snake.js"
                     :pretty-print false
                     :optimizations :advanced }}}})

