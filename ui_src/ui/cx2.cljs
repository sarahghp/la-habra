(ns ui.cx2
  (:require [reagent.core :as reagent :refer [atom]]
            [ui.text :as t :refer [cd1 cd2 cd3 cd4]]
            [ui.helpers :refer [cos sin style url val-cyc deform]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                          b1 b2 b3 b4
                                          l1 l2 l3 l4 l5 l6]]
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
             [gen-color-noise pattern pattern-def
              blue-dots blue-dots-1 blue-dots-2 blue-dots-3 blue-dots-4 blue-dots-5
              blue-lines blue-lines-1 blue-lines-2 blue-lines-3 blue-lines-4 blue-lines-5
              pink-lines pink-lines-1 pink-lines-2 pink-lines-3 pink-lines-4 pink-lines-5
              pink-dots pink-dots-1 pink-dots-2 pink-dots-3 pink-dots-4 pink-dots-5
              gray-dots gray-dots-lg gray-dots-1 gray-dots-2 gray-dots-3 gray-dots-4 gray-dots-5
              gray-lines gray-lines-1 gray-lines-2 gray-lines-3 gray-lines-4 gray-lines-5
              gray-patch
              mint-dots mint-dots-1 mint-dots-2 mint-dots-3 mint-dots-4 mint-dots-5
              mint-lines mint-lines-1 mint-lines-2 mint-lines-3 mint-lines-4 mint-lines-5
              navy-dots navy-dots-1 navy-dots-2 navy-dots-3 navy-dots-4 navy-dots-5
              navy-lines navy-lines-1 navy-lines-2 navy-lines-3 navy-lines-4 navy-lines-5
              orange-dots orange-dots-1 orange-dots-2 orange-dots-3 orange-dots-4 orange-dots-5 orange-lines orange-lines-1 orange-lines-2 orange-lines-3 orange-lines-4 orange-lines-5
              br-orange-dots br-orange-dots-1 br-orange-dots-2 br-orange-dots-3 br-orange-dots-4 br-orange-dots-5  br-orange-lines br-orange-lines-1 br-orange-lines-2 br-orange-lines-3 br-orange-lines-4 br-orange-lines-5
              yellow-dots yellow-dots-1 yellow-dots-2 yellow-dots-3 yellow-dots-4 yellow-dots-5
              yellow-lines yellow-lines-1 yellow-lines-2 yellow-lines-3 yellow-lines-4 yellow-lines-5
              white-dots white-dots-1 white-dots-2 white-dots-3 white-dots-4 white-dots-5
              white-dots-lg
              white-lines white-lines-1 white-lines-2 white-lines-3 white-lines-4 white-lines-5
              midnight-dots-1 midnight-dots-2 midnight-dots-3 midnight-dots-4 midnight-dots-5  midnight-lines-1 midnight-lines-2 midnight-lines-3 midnight-lines-4 midnight-lines-5
              shadow noise]]
            [ui.animations :as animations :refer
              [ nth-frame anim anim-and-hold
                ]]))

;; --------------- SETTINGS --------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

;; --------------- ATOMS STORAGE --------------------

(def open
  (->>
   (gen-circ (pattern  (:id pink-lines-3)) (* 0.5 @width) (* 0.5 @height) 40)
   (anim "expand" "20s" "infinite")
   (draw)
   (atom)))
   
   (def open-2
     (->>
      (gen-circ (pattern  (:id white-dots-3)) (* 0.5 @width) (* 0.5 @height) 40)
      (anim "expand-sm" "10s" "infinite" {:delay "1.6s"})
      (draw)
      (atom)))
      
         (def open-3
           (->>
            (gen-circ (pattern  (str "noise-pink")) (* 0.5 @width) (* 0.5 @height) 40)
            (anim "expand-sm" "8s" "infinite" {:delay "2s"})
            (draw)
            (atom)))


;; ------------------- DRAWING HELPERS ------------------------

;; use with (doall (map fn range))
(defn thin
  [color frame flicker? n]
  (let [op (if (and (nth-frame 4 frame) flicker?) (rand) 1)]
    (->>
     (gen-rect color (* 0.15 @width) (* 0.15 @height) (* 0.7 @width) 3)
     (style {:transform (str "translateY(" (* n 10) "px)") :opacity op})
     (draw))))

(defn flicker-test [n frame]
  (or (and (= n 10) (nth-frame 12 frame))
      (and (= n 12) (nth-frame 8 frame))
      (= n 44) (= n 45)
      (and (= n 46) (nth-frame 8 frame))))



 ;; ----------- COLLECTION SETUP AND CHANGE ----------------



(defn cx2 [frame fast-frame slow-frame]
  (list

        (->>
         (gen-circ (pattern (str "noise-" white)) (* 0.5 @height) (* 0.5 @height) 3000)
         ;(style {:opacity .5})
         (draw))
         
         ;@open
         ;@open-2
         ;@open-3
         
                #_(->>
                 (gen-circ (pattern (:id white-lines-4)) (* 0.5 @width) (* 0.5 @height) (val-cyc frame [400 100 260 29]) "#grad-mask")
                 (draw)
                 (when (nth-frame 1 frame)))
         
                 #_(->>
                  (gen-circ br-orange (* 0.5 @height) (* 0.5 @height) 3000)
                  (style {:opacity .5})
                  (draw))

  ;(new-freakout @width @height 30 600 "testCirc")
  

))

