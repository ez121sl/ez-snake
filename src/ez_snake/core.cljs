(ns ez-snake.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [goog.Timer :as timer]
            [goog.events.KeyHandler :as keyh]
            [cljs.core.async :refer [put! chan <!]]
            [ez-snake.game :refer [new-game! crawl! turn! game]]))

(defn listen [ch el type]
  (events/listen el type #(put! ch %))
  ch)

(defn beat [timeout]
  (let [timer (goog.Timer. timeout)]
    (.start timer)
    timer))

(defn key-handler [target]
  (let [kh (goog.events.KeyHandler. target)]
    kh))

(defn level []
  (.-value (dom/getElement "level")))

(def *ctx* (.getContext (dom/getElement "canvas") "2d"))

(defn render-background [_]
  (set! (.-fillStyle *ctx*) "white")
  (.fillRect *ctx* 0 0 200 100))

(defn render-snake [{:keys [snake] :as game}]
  (set! (.-fillStyle *ctx*) "green")
  (doseq [[x y] snake]
    (.fillRect *ctx* (* x 10) (* y 10) 10 10)))

(defn render-bunny [{:keys [bunny] :as game}]
  (set! (.-fillStyle *ctx*) "brown")
  (let [[x y] bunny]
    (.fillRect *ctx* (* x 10) (* y 10) 10 10)))

(defn render-state [game]
  (set! (.-innerHTML (dom/getElement "snake")) (str game)))

(defn render-score [game]
  (set! (.-innerHTML (dom/getElement "score")) (:score game)))

(let [ch (chan)]
  (doseq [id ["east" "west" "north" "south"]]
    (listen ch (dom/getElement id) "click"))
  (go (while true
        (let [event  (<! ch)]
          ;(.log js/console event)
          (turn! (keyword (.. event -target -id)))))))

(let [heartbeats (listen (chan) (beat (level)) goog.Timer/TICK)]
  (go (while (<! heartbeats)
        (crawl!)
        ((juxt
          render-background
          render-snake
          render-bunny
          render-state
          render-score) @game))))

(let [key-codes { 37 :west 38 :north 39 :east 40 :south }
      keystrokes (listen (chan) (key-handler js/document) "key")]
  (go (while true
        (let [event (<! keystrokes)]
          ;(.log js/console event)
          (when-let [new-dir (key-codes (.-keyCode event))]
            (turn! new-dir))))))

(. js/console (log "Hello world!"))
