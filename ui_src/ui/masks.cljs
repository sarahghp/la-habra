(ns ui.masks
  (:require 
   [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                 b1 b2 b3 b4]]
  [ui.helpers :refer [cos sin style url val-cyc]]
  [ui.generators :refer
      [freak-out new-freakout scatter lerp
       gen-circ gen-line gen-poly gen-rect gen-shape draw
       gen-group gen-offset-lines gen-bg-lines gen-mask
       gen-grid gen-line-grid gen-cols gen-rows]]
   [ui.fills :as fills :refer
     [gray charcoal mint midnight navy blue orange
       br-orange pink white yellow]]
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
      shadow noise]]))


;; --------------- SETTINGS --------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))


(def mask-list [
            [ "poly-mask"
              [:path {:d b2 :fill "#fff" :style { :transform-origin "center" :animation "woosh 2s infinite"}}]]
            [ "poly-mask-2"
                          [:path {:d b3 :fill "#fff" :style { :transform-origin "center" :animation "woosh-3 3s infinite"}}]]
            ["bitey"
               (->>
                (gen-circ (pattern (str "noise-" white)) (* 0.5 @width) (* 0.5 @height) 1000)
                (style {:opacity 1 :transform "scale(.3)"})
                (draw))]
            [ "grad-mask"
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" }]]
            [ "grad-mask-an"
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" :animation "woosh 3s infinite" }]]
            [ "cutout"
             (->>
               (gen-rect white 10 12 (* 0.94 @width) (* 0.88 @height))
               (draw))
             (->>
               (gen-circ "#000" (* 0.7 @width) (* 0.7 @height) 100)
                (draw))]
              ["rect-buds"
               (->>
                 (gen-rect white 10 12 (* 0.3 @width) (* 0.5 @height))
                 (draw))
                 ]
                ["na" [ :image {:key (random-uuid)
                                :x "0"
                                :y "0"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/blop.png"
                                :style {:transform-origin "center"
                                        :transform "scale(2)"} }]]
                ["nt1" [ :image {:key (random-uuid)
                                :x "0"
                                :y "0"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/001.png"
                                :style {:transform-origin "center"
                                        :transform "scale(2)"} }]]
                ["nt2" [ :image {:key (random-uuid)
                                :x "0"
                                :y "0"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/002.png"
                                :style {:transform-origin "center"
                                        :transform "scale(2)"} }]]
                ["nt3" [ :image {:key (random-uuid)
                                :x "0"
                                :y "0"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/003.png"
                                :style {:transform-origin "center"
                                        :transform "scale(2)"} }]]
                ["nt4" [ :image {:key (random-uuid)
                                :x "0"
                                :y "0"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/004.png"
                                :style {:transform-origin "center"
                                        :transform "scale(2)"} }]]
                ["nn" [ :image {:key (random-uuid)
                                :x "100"
                                :y "200"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/blop.png"
                                :style {:transform-origin "center"
                                        :transform "scale(10)"
                                        } }]]
            ])


(def masks (map (fn [[id & rest]] (apply gen-mask id rest)) mask-list))