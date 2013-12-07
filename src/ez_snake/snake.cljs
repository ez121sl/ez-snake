(ns ez-snake.snake)

(def head first)

(defn advance [body new-head]
  (list* new-head (drop-last body)))

(def grow conj)

(defn move [[x y] dir]
  (condp = dir
    :north [x (dec y)]
    :south [x (inc y)]
    :east  [(inc x) y]
    :west  [(dec x) y]))


(def dir-map { :north :v, :south :v, :east :h, :west :h })

(defn new-dir [{:keys [dir new-dir] :as game} requested-dir]
  (if-not (= (dir-map dir) (dir-map requested-dir))
    (assoc game :new-dir requested-dir)
    game))

(defn place-bunny [{:keys [snake w h]}]
  (let [snake (apply hash-set snake)
        rand-bunny #(vector (rand-int w) (rand-int h))]
    (some #(when-not (snake %) %) (repeatedly rand-bunny))))

(defn ran-into-itself [new-head snake]
  (some #{new-head} (rest snake)))

(defn out-of-bounds [[x y] w h]
  (or (neg? x) (neg? y) (>= x w) (>= y h)))

(defn crawl [{:keys [snake bunny new-dir w h] :as game}]
  (let [new-head (move (head snake) new-dir)]
    (cond
     (= new-head bunny)
       (-> game
           (assoc :snake (grow snake new-head))
           (assoc :dir new-dir)
           (as-> g (assoc g :bunny (place-bunny g)))
           (update-in [:score] + 10)
           (update-in [:bunnies-eaten] inc))
     (or (ran-into-itself new-head snake) (out-of-bounds new-head w h))
       (assoc game :lost true)
     :else
       (-> game         
           (assoc :snake (advance snake new-head))
           (assoc :dir new-dir)
           (update-in [:score] inc)))))

