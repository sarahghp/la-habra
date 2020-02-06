(ns ui.helpers
  (:require [clojure.string :as string :refer [split-lines split join trim]]))

(defn cos [x] (.cos js/Math x))
(defn sin [x] (.sin js/Math x))

(defn style
  [changes shape]
  (update-in shape [:style] #(merge % changes)))

(defn url
  ([ fill-id ]
    (str "url(#" fill-id ")")))

(defn val-cyc
  [frame vals]
  (let [n (count vals)]
    (nth vals (mod frame n))))

(defn deform [shape num-points amount]
  (let
    [shape-vec (split shape #"[Mz ]")
     to-change (set (take num-points (repeatedly #(rand-nth shape-vec))))
     ; idx-to-change (take num-points (repeatedly (rand-int (count shape-vec))))
     new-vec (map-indexed
      (fn [idx item]
        (if (contains? to-change item)
          (+ (js/Number item) (rand-int amount))
          item))
      shape-vec)
     ]
        
    (str "M" (join " " (rest new-vec)) "z")
     
    ))