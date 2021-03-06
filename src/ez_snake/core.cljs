(ns ez-snake.core
  (:require [goog.dom :as dom]
            [goog.style :as style]
            [goog.events :as events]
            [cljs.core.async :refer [put! chan <! close!]]
            [ez-snake.game :refer [new-game! crawl! turn! game]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:import goog.Timer
           goog.events.KeyHandler))

(defn listen [ch el type]
  (events/listen el type #(put! ch %))
  ch)

(defn beat [timer timeout]
  (doto timer
    (.stop)
    (.setInterval timeout)
    (.start)))

(defn key-handler [target]
  (KeyHandler. target))

(defn level []
  (.-value (dom/getElement "level")))

(def *ctx* (.getContext (dom/getElement "canvas") "2d"))

(defn render-background [{:keys [w h]}]
  (set! (.-fillStyle *ctx*) "white")
  (.fillRect *ctx* 0 0 (* w 10) (* h 10)))

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

(defn render-game-over [{:keys [lost]}]
  (style/showElement (dom/getElement "game-over") lost)
  (style/showElement (dom/getElement "controls") (not lost)))

(defn handle-buttons []
  (let [ch (chan)]
    (doseq [id ["east" "west" "north" "south"]
            evt ["click"]]
      (listen ch (dom/getElement id) evt))
    (go-loop []
          (when-let [event (<! ch)]
            ;(.log js/console event)
            (turn! (keyword (.. event -target -id)))
            (recur)))))

(defn handle-timer [timer]
  (let [heartbeats (listen (chan) timer goog.Timer/TICK)]
    (go-loop []
             (when (<! heartbeats)
               (crawl!)
               ((juxt
                 render-background
                 render-snake
                 render-bunny
                 render-state
                 render-score
                 render-game-over) @game)
               (when (:lost @game)
                 (.stop timer))
               (recur)))))

(defn handle-keystrokes []
  (let [key-codes { 37 :west 38 :north 39 :east 40 :south }
        keystrokes (listen (chan) (key-handler js/document) "key")]
    (go-loop []
          (when-let [event (<! keystrokes)]
            ;(.log js/console event)
            (when-let [new-dir (key-codes (.-keyCode event))]
              (turn! new-dir))
            (recur)))))

(defn new-round [timer level]
  (new-game! level)
  (beat timer level))

(let [timer (Timer.)
      rounds (listen (chan) (dom/getElement "new-game") "click")]
  ((juxt handle-buttons handle-keystrokes #(handle-timer timer)))
  (go (while (<! rounds)
        (new-round timer (level)))))

(. js/console (log "Hello world!"))
