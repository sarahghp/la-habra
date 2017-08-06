(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines]]))

(enable-console-print!)

(println "Loaded.")

(def settings {:width 300
               :height 300
               :variation 12
               :stroke "#ff7d66"
               :stroke-width 2
               })

(def points-list (atom {:points
                        [{:x (/ (:width settings) 2)
                          :y (/ (:height settings) 2) }]}))

(defn plusOrMinus [span]
  (let [n (rand (* span 2))]
    (- n span)))

(defn randomize-point [{:keys [x y]}]
  {:x (+ x (plusOrMinus (:variation settings)))
   :y (+ y (plusOrMinus (:variation settings)))})

(defn new-points [points]
  (->> points
       last
       randomize-point
       (conj points)))

(defonce points-updater
  (js/setInterval
    #(swap! points-list update :points new-points)
    0))

(defn convert-points [points]
  (apply str
         (drop-last
           (interleave
             (map #(str (:x %) "," (:y %)) points)
             (repeat ", ")))))

(defn brownian []
  [:svg { :height (:width settings), :width (:height settings) }
   [:polyline {:points (convert-points (:points @points-list))
               :fill "none"
               :stroke (:stroke settings)
               :stroke-width (:stroke-width settings)}]])

(reagent/render-component [brownian]
                          (js/document.getElementById "app-container"))
