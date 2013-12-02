(ns ez-snake.game
  (:require [ez-snake.snake :refer [crawl new-dir]]))

(def initial-state { :w 20,
                     :h 10
                     :snake '([5 5] [4 5] [3 5])
                     :dir :east
                     :bunny [7 5]
                     :state :init
                     :score 0 })

(def game (atom initial-state))

(defn new-game! [level]
  (reset! game initial-state))

(defn crawl! []
  (swap! game crawl))

(defn turn! [key]
  (swap! game update-in [:dir] new-dir key))


