(ns ez-snake.game
  (:require [ez-snake.snake :refer [crawl new-dir]]))

(def initial-state { :w 34,
                     :h 17
                     :snake '([5 5] [4 5] [3 5])
                     :dir :east
                     :new-dir :east
                     :bunny [7 5]
                     :lost false
                     :score 0 
                     :bunnies-eaten 0 })

(def game (atom initial-state))

(defn new-game! [level]
  (reset! game initial-state))

(defn crawl! []
  (swap! game crawl))

(defn turn! [key]
  (swap! game new-dir key))

