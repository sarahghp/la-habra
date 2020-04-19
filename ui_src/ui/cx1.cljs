(ns ui.cx1
  (:require [reagent.core :as reagent :refer [atom]]
            [ui.text :as t :refer [cd1 cd2 cd3 cd4]]
            [ui.helpers :refer [cos sin style url val-cyc deform]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                          b1 b2 b3 b4 
                                          l1 l2 l3 l4 l5 l6
                                          ul2 ul3]]
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
              [ nth-frame anim anim-and-hold
                ]]))

;; --------------- SETTINGS --------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

;; --------------- ATOMS STORAGE --------------------

(def drops
  (atom  (map
     #(->>
       (gen-rect white (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "2.2s" "infinite" {:delay (str (* .5 %) "s")})
       (style {:mix-blend-mode "color-dodge"})
       (draw))
     (range 10))))


(def drops2
  (atom  (map
     #(->>
       (gen-rect white (+ 30 (* % 160)) 10 200 36)
       (anim "slide-up" "2.2s" "infinite" {:delay (str (* .5 %) "s")})
       (style {:mix-blend-mode "color-dodge"})
       (draw))
     (range 10))))

(def lm (->>
 (gen-shape pink l1)
 (style {:transform "translate(30vw, 30vh) scale(1.4)"})
 (anim "lump-morph" "20s" "infinite")
 (draw)
 (atom)))

(def lm2 (->>
 (gen-shape pink l1)
 (style {:transform "translate(10vw, 10vh) scale(1)"})
 (anim "l1l6" "2s" "infinite")
 (draw)
 (atom)))

(def lm3 (->>
 (gen-shape pink l1)
 (style {:transform "translate(20vw, 20vh) scale(1)" :mix-blend-mode "color-dodge"})
 (anim "l2l4" "3s" "infinite")
 (draw)
 (atom)))

(def babrect1 
  (->>
   (gen-rect white 0 0 @width (* .05 @height))
   (style {:mix-blend-mode "luminosity"})
   (anim "small-scale" "3.2s" "infinite")
   (draw)
   (atom)))

(def babrect2 
  (->>
   (gen-rect white 0 (* .9 @height) @width (* .05 @height))
   (style {:mix-blend-mode "luminosity"})
   (anim "small-scale" "3.2s" "infinite")
   (draw)
   (atom)))

(def move-me
  (->>
   (gen-shape (pattern (:id mint-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "luminosity"})
   (anim "woosh" "3s" "infinite")
   (draw)
   (atom)))

(def bnz
  (->>
   (gen-shape pink b1)
   (style {:opacity 1 :transform-origin "center" :transform "scale(1.4)"})
   (style {:mix-blend-mode "difference"})
   (anim "loopy-left" "8s" "infinite")
   (draw)
   (atom)))

(def bb6
  (->>
    (gen-shape mint oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "color-dodge"} )

      (anim "woosh" "6s" "infinite")
    (draw)
    (atom)))

(def bb6a
  (->>
    (gen-shape mint oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "color-dodge"} )

      (anim "woosh" "6s" "infinite" {:delay ".4s"})
    (draw)
    (atom)))

(def scale-me
        (->>
          (gen-rect (pattern (str "noise-" midnight)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" "3s" "infinite")
          (draw)
          (atom)))


(def sc-circ
  (->>
   (gen-circ (pattern (:id orange-lines)) (* 0.5 @width) (* 0.4 @height) 100)
   (anim "scaley" "5s" "infinite")
   (draw)
   (atom)))

(def mf
  (->>
   (gen-shape "hsla(100, 100%, 100%, 0)" l1)
   (style {:stroke mint
           :stroke-width 14
           :stroke-dasharray 100
           :stroke-dashoffset 100
           :stroke-linecap "round"
           :stroke-join "round"})
   (anim "lump-morph" "14s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(1)"}})
   (atom)))




;; ------------------- DRAWING HELPERS ------------------------

;(doall (map deref levels))
(def levels
  (map-indexed
    (fn [idx color]
          (->>
            (gen-rect color -100 -100 "120%" "120%" (url "cutout"))
            (style {:opacity .4
                    :transform-origin "center"
                    :transform (str
                                "translate(" (- (rand-int 200) 100) "px, " (- (rand-int 300) 100) "px)"
                                "rotate(" (- 360 (rand-int 720)) "deg)")})
            (anim "fade-in-out" "10s" "infinite" {:delay (str (* .1 idx) "s")})
            (draw)
            (atom)))
    (take 10 (repeatedly #(nth [pink orange yellow pink] (rand-int 6))))))

(def worms
  (map-indexed
   (fn [idx [color scale]]
     (->>
      (gen-shape "hsla(100, 100%, 100%, 0)" tri)
      (style {:stroke color
              :stroke-width 8
              :stroke-dasharray (+ (rand-int 20) 80)
              :stroke-dashoffset (+ (rand-int 20) 80)
              :stroke-linejoin "round"
              :stroke-linecap "round"})
      (anim "morph" "4s" "infinite" {:delay (str (* 4 (rand)) "s")})
      (draw)
      (gen-group {:style {
                          :transform-origin "center" 
                          :transform (str "translate(" (+ (rand-int 20) (rand-int 20)) 
                                          "vw, "(+ (rand-int 20) (rand-int 20)) "vh) scale(" scale ")")}})
      (atom)))
   (take 10
         (repeatedly #(rand-nth [[midnight 4] [navy 4] [navy 3] [pink 4.4] [mint 2.4]])))))

(def bb
  (->>
   (->>
    (gen-grid
      4 4
      {:col 100 :row 100}
      (->>
      (gen-shape white oct)))
      (map #(style {:mix-blend-mode "overlay"}  %))
      (map #(anim "colorcolorcolorcolor" (str (rand-int 60) "s") "infinite" %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center"
                                :transform (str
                                            "rotate(" (rand-int 360) "deg)"
                                            "scale(4) translate(-20vh, -20vh)")}} %)))
   (atom)))

(defonce streaks
  (scatter 40 
           (->>
            (gen-line [10 10] [200 100] white 4)
            (draw))))

(def trio (atom 
      (gen-group
       {:style {:transform-origin "center" 
                :animation "rot 3s infinite"
                }}
       (->>
         (gen-grid
           20 20
           {:col 300 :row 300}
           (->>
            (gen-shape yellow tri)))
           (map draw)
           (map #(gen-group {:style 
                             {:transform-origin "center" 
                              :transform "translate(-10vw, -10vh) scale(.3)"}} %))
           (map #(gen-group {:style {:transform-origin "center" :opacity ".7" :animation "rot 1s infinite"}} %))))))

(def line-growth 
   (map-indexed
    (fn [idx item]
      (->>
       (gen-rect white 
                 0 
                 (* (+ 1 idx) 0.07 @height) 
                 @width 
                 (* 4 (+ 1 idx) 0.001 @height))
       ;(style {:mix-blend-mode "color-dodge"})
       (anim "small-scale-y" "1s" "infinite" 
             {:delay (str (* idx .01) "s")})
       (draw)
       atom))
    (range 20)))


;; start end dur frame no-repeat
(def lerp1 (lerp))
(def lerp2 (lerp))
(def lerp3 (lerp))

 ;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defn cx [frame fast-frame slow-frame]
  (list

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (when (nth-frame 1 frame)
    (freak-out @width
               @height
               4
               400
               white))

  (let
    [colors [
             ;midnight midnight midnight midnight
             mint mint
             ;yellow yellow
             ;
             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:opacity .95})
        (draw)))
      

  
#_(gen-group {:mask (url "")} 
           (gen-group {:mask (url "nn")}
             ;(new-freakout @width @height 100 100 "testCirc")
             ;(new-freakout @width @height 10 100 "testCirc4")
             ;(new-freakout @width @height 4 100 "testCirc2")
             (->>
              (gen-circ white (* 0.5 @width) (* 0.5 @height) 400)
              (draw)
              (when (nth-frame 1 frame)))
             (when (nth-frame 4 frame)(gen-line-grid midnight 3
               80 80
               {:col 20 :row 20}))))


#_(->>
 (gen-shape "hsla(0, 0%, 0%, 0)" tri)
 (style {:stroke white
         :stroke-width 4
         :stroke-dasharray 10
         :stroke-dashoffset 10})
 (style {:transform-origin "center" :transform (str "translate(60vw, 60vh) rotate("
         (val-cyc frame [120 120 120 120 -60 -60 -60 -60 80 80 80 80 245 245 245 245])
         "deg) scale(6.2)")})
 (style {:mix-blend-mode "color-dodge"})
 (draw)
 (when (nth-frame 3 frame)))
  
#_(->>
 (gen-shape (pattern (:id blue-dots)) oct)
 (style {:opacity (val-cyc frame (concat 
                                  (repeat 4 .6)
                                  (repeat 4 .8)
                                  (repeat 4 .3)
                                  (repeat 4 .5)))})
 ;(style {:mix-blend-mode "color-dodge"})

 (style {:transform "translate(40vw, 40vh) scale(2)"})
 #_(style {:transform "translate(40vw, 40vh) scale(20)"})
 (style {:transform (str "translate(40vw, 40vh) scale("
                         (val-cyc frame (concat
                                         (repeat 3 3)
                                         (repeat 3 2)
                                         (repeat 3 4)
                                         (repeat 3 6)
                                         (repeat 3 4)
                                         (repeat 3 2)))
                         ")")})
 (draw)
 (when (nth-frame 3 frame)))


)) ; cx end