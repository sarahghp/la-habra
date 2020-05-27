(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join trim]]
            [ui.text :as t :refer [cd1 cd2 cd3 cd4]]
            [ui.helpers :refer [cos sin style url val-cyc]]
            [ui.masks :as m :refer [masks]]
            [ui.cx1 :refer [cx]]
            [ui.cx2 :refer [cx2]]
            [ui.fills :as fills :refer
              [gray charcoal mint midnight navy blue orange
                br-orange pink white yellow]]
            [ui.generators :refer
             [freak-out new-freakout scatter lerp
              gen-circ gen-line gen-poly gen-rect gen-shape draw
              gen-group gen-offset-lines gen-bg-lines gen-mask
              gen-grid gen-line-grid gen-cols gen-rows]]
            [ui.filters :as filters :refer [turb noiz soft-noiz disappearing splotchy blur]]
            [ui.patterns :as patterns :refer
             [ gen-color-noise pattern pattern-def
               blue-dots blue-lines
               pink-dots pink-lines pink-dots-1 pink-dots-2 pink-dots-3 pink-dots-4 pink-dots-5
               gray-dots gray-dots-lg gray-lines gray-patch
               mint-dots mint-lines
               navy-dots navy-lines
               orange-dots orange-lines
               br-orange-dots br-orange-lines
               yellow-dots yellow-lines
               white-dots white-dots-lg white-lines
               shadow noise]]
            [ui.animations :as animations :refer
              [ make-body splice-bodies make-frames!
                nth-frame even-frame odd-frame
                seconds-to-frames frames-to-seconds
                anim anim-and-hold
                ]]))

(enable-console-print!)

(println "Loaded.")



;; ------------------------ SETTINGS  ---------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

(def settings {:width @width
               :height @height })

(def DEBUG false)

(when DEBUG
  (defonce collection (atom (cx 1 1 1))))

(when DEBUG
  (defonce collection2 (atom (cx2 1 1 1))))

(when-not DEBUG
  (defonce collection (atom (list))))

(when-not DEBUG
  (defonce collection2 (atom (list))))

;; ----------- LOOP TIMERS ------------------------------

(defonce frame (atom 0))
(defonce fast-frame (atom 0))
(defonce slow-frame (atom 0))

(when-not DEBUG
  (defonce start-cx-timer
    (js/setInterval
      #(reset! collection (cx @frame @fast-frame @slow-frame)) 50)) 

  (defonce start-cx-timer-2
    (js/setInterval
      #(reset! collection2 (cx2 @frame @fast-frame @slow-frame)) 50))

  (defonce start-frame-timer
    (js/setInterval
      #(swap! frame inc) 500))

  (defonce start-fast-frame-timer
    (js/setInterval
      #(swap! fast-frame inc) 250))

  (defonce start-slow-frame-timer
    (js/setInterval
      #(swap! slow-frame inc) 1000)))




;; ----------- DEFS AND DRAW ------------------------------

(def gradients [[:linearGradient { :id "grad" :key (random-uuid)}
                 [:stop { :offset "0" :stop-color "white" :stop-opacity "0" }]
                 [:stop { :offset "1" :stop-color "white" :stop-opacity "1" }]]])


(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [gray mint navy blue orange br-orange pink white yellow midnight])

(defn add-svg [collection blend-mode]
  [:svg {
    :style  {:mix-blend-mode blend-mode }
    :width  (:width settings)
    :height (:height settings)}
     ;; filters
    (map #(:def %) all-filters)
    ;; masks and patterns
    [:defs
     [:filter { :id "d-shadow" }
      [:feDropShadow { :dx "2.2" :dy "2.4" :stdDeviation "0.2"  :flood-color "cyan"}]]
     noise
     [:circle {:id "weeCirc" :cx 0 :cy 0 :r 4
               :style {:animation "colorcolor 100s infinite"
                       :opacity .6}}]
     [:circle {:id "testCirc" :cx 0 :cy 0 :r 100 :fill (pattern (str "noise-" white))}]
     [:circle {:id "testCirc3"
               :cx 0 :cy 0 :r 100
               :style {:animation "colorcolorcolor 100s infinite"}
               :fill (pattern (str "noise-" yellow))}]
          [:circle {:id "testCirc2" :cx 0 :cy 0 :r 100 :fill (pattern (str "noise-" navy))}]
         [:circle {:id "testCirc4" :cx 0 :cy 0 :r 100 :fill (pattern (:id pink-lines))}]

     (map identity gradients)
     (map identity masks)
     (map gen-color-noise all-fills)
     (map t/pattern-def-text [cd1 cd2 cd3 cd4])
     (map pattern-def [ blue-dots
                        blue-lines
                        pink-dots pink-dots-1 pink-dots-2 pink-dots-3 pink-dots-4 pink-dots-5
                        pink-lines
                        gray-dots
                        gray-dots-lg
                        gray-lines
                        gray-patch
                        mint-dots
                        mint-lines
                        navy-dots
                        navy-lines
                        orange-dots
                        orange-lines
                        br-orange-dots
                        br-orange-lines
                        yellow-dots
                        yellow-lines
                        white-dots
                        white-dots-lg
                        white-lines
                        shadow])]
                      collection
  ])

(defn drawing []
    (add-svg @collection (val-cyc @frame
             [
             "luminosity" "luminosity"
             ;"multiply" "multiply" "multiply" "multiply"
             ;"difference" "difference" 
              ])))

(defn drawing2 []
    (add-svg @collection2 (val-cyc @frame
             [
              "difference" "difference" "difference" 
             ;"multiply" "multiply" "multiply" "multiply"
              ;"luminosity"
              ])))

(reagent/render [:div
                 [:div {:style {:position "absolute" :top 0 :left 0}} [drawing]] [:div {:style {:position "absolute" :top 0 :left 0}} [drawing2]]]
                          (js/document.getElementById "app-container"))

#_(reagent/render [drawing]
                          (js/document.getElementById "app-container"))

