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

(def lm4 (->>
 (gen-shape yellow l1)
 (style {:transform "translate(10vw, 40vh) scale(1)"})
 (anim "l2l4" "2s" "infinite" {:delay '.3s'})
 (draw)
 (atom)))


(def babrect1 
  (->>
   (gen-rect white 0 0 @width (* .05 @height))
   (style {:mix-blend-mode "luminosity"})
   (anim "small-scale" "3s" "infinite")
   (draw)
   (atom)))

(def babrect2 
  (->>
   (gen-rect white 0 (* .9 @height) @width (* .05 @height))
   (style {:mix-blend-mode "luminosity"})
   (anim "small-scale" "3s" "infinite")
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

(def bb6s
  (->>
    (gen-shape (pattern (:id white-lines))  oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "difference"} )

      (anim "loopy-left" "3s" "infinite")
    (draw)
    (atom)))


(def bb6s2
  (->>
    (gen-shape (pattern (:id yellow-lines))  hept)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "difference"} )

      (anim "loopy-right" "3s" "infinite")
    (draw)
    (atom)))


(def bb7
  (->>
    (gen-shape mint oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "color-dodge"} )

      (anim "loopy-right" "4s" "infinite")
    (draw)
    (atom)))

(def bb7s
  (->>
    (gen-shape (pattern (:id mint-lines))  oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "color-dodge"} )

      (anim "loopy-left" "1s" "infinite")
    (draw)
    (atom)))



(def bb3
  (->>
    (gen-shape orange hept)
      (style {:transform "translate(30vw, 44vh) scale(2.4)"})
      (style {:mix-blend-mode "difference"} )
      (anim "woosh-3" "3s" "infinite")
    (draw)
    (atom)))

(def scale-me
        (->>
          (gen-rect (pattern (str "noise-" midnight)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" "3s" "infinite")
          (draw)
          (atom)))


(def scale-me-2
        (->>
          (gen-rect (pattern (:id white-dots)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" "1s" "infinite")
          (draw)
          (atom)))

(def scale-me-4
        (->>
          (gen-rect (pattern (:id pink-dots)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" ".5s" "infinite")
          (draw)
          (atom)))


(def scale-me-3
        (->>
          (gen-rect (pattern (str "noise-" navy)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" ".5s" "infinite" {:delay ".2s"})
          (draw)
          (atom)))

(def sc-circ
  (->>
   (gen-circ (pattern (:id orange-lines)) (* 0.5 @width) (* 0.4 @height) 100)
   (anim "scaley" "5s" "infinite")
   (draw)
   (atom)))

(def sc-circ-2
  (->>
   (gen-circ white (* 0.5 @width) (* 0.4 @height) 100)
   (style {:opacity .4 :mix-blend-mode "overlay"})
   (anim "scaley" "3s" "infinite")
   (draw)
   (atom)))

(def lr1
  (->>
   (gen-line [(* 0.5 @width) (* 0.2 @height)] [(* 0.5 @width) (* 0.8 @height)] white 20)
   (anim "rot" "2s" "infinite" {:timing "linear"} )
   (draw)
   (atom)))


(def mf
  (->>
   (gen-shape "hsla(100, 100%, 100%, 0)" tri)
   (style {:stroke mint
           :stroke-width 4
           :stroke-dasharray 100
           :stroke-dashoffset 100})
   (anim "morph" "4s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(3)"}})
   (atom)))

(def mf2
  (->>
   (gen-shape "hsla(100, 100%, 100%, 0)" tri)
   (style {:stroke mint
           :stroke-width 4
           :stroke-dasharray 80
           :stroke-dashoffset 80})
   (anim "morph" "4s" "infinite" {:delay "1.2s"})
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(30vw, 20vh) scale(4)"}})
   (atom)))

(def mf3
  (->>
   (gen-shape "hsla(100, 100%, 100%, 0)" tri)
   (style {:stroke pink
           :stroke-width 4
           :stroke-dasharray 120
           :stroke-dashoffset 120})
   (anim "morph" "4s" "infinite" {:delay "2.2s"})
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(10vw, 10vh) scale(4.4)"}})
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
    (take 10 (repeatedly #(nth [mint navy navy mint] (rand-int 6))))))


(def all-the-moves
  (map-indexed
   (fn [idx [time patt]]
     (->>
      (gen-shape (pattern (:id patt)) hept)
      (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
      (style {:mix-blend-mode "difference"})
      (anim "woosh-3" (str time "s") "infinite" {:delay (str (rand 4) "s")})
      (draw)
      (gen-group {:mask (url "dmask")})
      (atom)))
   (take 4
         (repeatedly #(rand-nth [[10 gray-dots-lg] [9 pink-lines] [12 white-dots]])))))


(def worms
  (map-indexed
   (fn [idx [color scale]]
     (->>
      (gen-shape "hsla(100, 100%, 100%, 0)" tri)
      (style {:stroke color
              :stroke-width 4
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

(defonce b
  (scatter 100 (->>
   (gen-circ navy 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))


(defonce k
  (scatter 40 
           (->>
            (gen-line [10 10] [200 100] white 4)
            (draw))))



(def bbb (atom (gen-group {:style {:animation "rot 2s infinite"}} @b)))



 ;; ----------- COLLECTION SETUP AND CHANGE ----------------


(def rr (atom
         (->>
          (gen-grid
            20 30
            {:col 100 :row 150}
            (->>
             (gen-shape navy tri)))
            ;(map #(style styles %))
            ;(map #(anim "rot" "10s" "infinte" %))
            (map draw)
          (map #(gen-group {:style {:transform-origin "center" :transform "scale(2)"}} %))
          (map #(gen-group {:mask (url "bitey") :style {:transform-origin "center" :animation "rot 10s infinite" }} %)))))

(def rr2 (atom
         (->>
          (gen-grid
            20 30
            {:col 100 :row 150}
            (->>
             (gen-shape pink tri)))
              (map #(style {:opacity 1 :mix-blend-mode "overlay"} %))

            (map #(anim "morph" "5s" "infinite" %))
            (map draw)
                  (map #(gen-group {:style {:transform-origin "center" :transform "scale(2)"}} %))
          (map #(gen-group {:style {:transform-origin "center" :transform "translate(-400px, 1px)"}} %))
            (map #(gen-group { :style {:transform-origin "center"  :animation "rot 5s infinite" }} %)))))

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



(def lerp1 (lerp))


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
             midnight midnight midnight midnight
             mint mint
             yellow yellow
             
             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:opacity .95})
        (draw)))
  
  

  
(gen-group {:mask (url "nn")}
             (new-freakout @width @height 100 100 "testCirc")
             (new-freakout @width @height 10 100 "testCirc4")
             (new-freakout @width @height 4 100 "testCirc2")
             (->>
              (gen-circ white (* 0.5 @width) (* 0.5 @height) 400)
              (style {:mix-blend-mode "overlay"})
              (draw)
              (when (nth-frame 1 frame)))
             (when (nth-frame 4 frame)(gen-line-grid midnight 3
               80 80
               {:col 20 :row 20})))
  



  
  #_(->>
   (gen-shape white oct)
   (style {:opacity (val-cyc frame (concat 
                                    (repeat 4 .3)
                                    (repeat 4 .7)
                                    (repeat 4 .3)
                                    (repeat 4 .5)))})
   (style {:mix-blend-mode "color-dodge"})
   #_(style {:filter (url (:id noiz))} )
   (style {:transform "translate(40vw, 40vh) scale(2)"})
   #_(style {:transform "translate(40vw, 40vh) scale(5)"})
   #_(style {:transform "translate(40vw, 40vh) scale(10)"})
  #_(style {:transform (str "translate(40vw, 40vh) scale("
                           (val-cyc frame (concat
                                           (repeat 4 2)
                                           (repeat 4 3)
                                           (repeat 4 5)
                                           (repeat 4 8)
                                           (repeat 4 5)
                                           (repeat 4 3)))
                           ")")})
   (draw)
   (when (nth-frame 3 frame)))
  
  

  (list 
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
     (gen-shape "hsla(0, 0%, 0%, 0)" tri)
     (style {:stroke white
             :stroke-width 4
             :stroke-dasharray 10
             :stroke-dashoffset 10})
     (style {:transform-origin "center" :transform (str "translate(20vw, 20vh) rotate("
             (val-cyc frame [80 80 80 80 120 120 120 120 -60 -60 -60 -60 245 245 245 245])
             "deg) scale(4)")})
     (style {:mix-blend-mode "color-dodge"})
     (draw)
     (when (nth-frame 2 frame)))

  #_(->>
   (gen-shape pink tri)
   (style {:transform-origin "center" :transform (str "translate(60vw, 60vh) rotate("
           (val-cyc frame [120 120 120 120 -60 -60 -60 -60 80 80 80 80 245 245 245 245])
           "deg) scale(1.2)")})
   (style {:mix-blend-mode "color-dodge"})
   (draw)
   (when (nth-frame 3 frame)))
  
    #_(->>
     (gen-shape pink tri)
     (style {:transform-origin "center" :transform (str "translate(20vw, 20vh) rotate("
             (val-cyc frame [80 80 80 80 120 120 120 120 -60 -60 -60 -60 245 245 245 245])
             "deg) scale(2)")})
     (style {:mix-blend-mode "color-dodge"})
     (draw)
     (when (nth-frame 6 frame)))
  
  #_(->>
   (gen-shape (pattern (:id yellow-lines)) tri)
   (style {:transform-origin "center" :transform (str "translate(30vw, 30vh) rotate("
           (val-cyc frame [120 120 120 120 -60 -60 -60 -60 80 80 80 80 245 245 245 245])
           "deg) scale(3.2)")})
   (style {:mix-blend-mode "color-dodge"})
   (draw)
   (when (nth-frame 7 frame)))
  
  #_(->>
     (gen-shape (pattern (:id yellow-lines)) tri)
     (style {:transform-origin "center" :transform (str "translate(70vw, 70vh) rotate("
             (val-cyc frame [80 80 80 80 120 120 120 120 -60 -60 -60 -60 245 245 245 245])
             "deg) scale(3)")})
     (style {:mix-blend-mode "color-dodge"})
     (draw)
     (when (nth-frame 5 frame)))
  
  
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
   #_(style {:transform (str "translate(40vw, 40vh) scale("
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
  
  
    
  
    #_(->>
     (gen-shape (pattern (:id yellow-lines)) oct)
     (style {:opacity (val-cyc frame (concat 
                                      (repeat 4 .9)
                                      (repeat 4 .9)
                                      (repeat 4 .9)
                                      (repeat 4 .9)))})
     ;(style {:mix-blend-mode "color-dodge"})

     (style {:transform "translate(40vw, 40vh) scale(2)"})
     #_(style {:transform "translate(40vw, 40vh) scale(20)"})
     (style {:transform (str "translate(40vw, 40vh) scale("
                             (val-cyc frame (concat
                                             (repeat 8 12)
                                             (repeat 8 14)
                                             (repeat 8 8)
                                             (repeat 8 4)
                                             (repeat 8 6)
                                             (repeat 8 2)))
                             ")")})
     (draw)
     (when (nth-frame 8 frame))))
  
  #_(when (nth-frame 3 frame )(->>
   (gen-grid
     30 30
     {:col 200 :row 200}
     (->>
      (gen-shape white tri)))
     (map draw)
     (map #(gen-group {:style {
                               :transform-origin "center"
                               :transform "scale(.5)"
                               :mix-blend-mode "difference"
                               }} %))))
  
    #_(when (nth-frame 6 frame )(->>
     (gen-grid
       30 30
       {:col 200 :row 200}
       (->>
        (gen-shape pink oct)))
       (map draw)
       (map #(gen-group {:style {
                                 :transform-origin "center"
                                 :transform "scale(.5)"
                                 :mix-blend-mode "overlay"
                                 }} %))))
  
  

  #_(when (nth-frame 2 frame)
    (freak-out @width
               @height
               20
               100
               (pattern (:id white-dots))))


  
  

  ; 
  ; 
  ; 
  #_(when (or (nth-frame 1 frame) (nth-frame 4 (+ 1 frame)) (nth-frame 4 (+ 2 frame))) @lm2)
    #_(when (or (nth-frame 1 frame) (nth-frame 4 (+ 1 frame)) (nth-frame 4 (+ 2 frame))) @lm3)

  
  

    
  #_(->>
   (gen-shape br-orange l4)
   (style {:transform "translate(30vw, 30vh) scale(1.4)" :mix-blend-mode "difference"})
   (draw)
   (when (nth-frame 1 frame)))

  ;@babrect1
  ;@babrect2
  
    
    #_(when (or (nth-frame 1 frame) (nth-frame 4 (+ 1 frame)) (nth-frame 4 (+ 2 frame))) (doall (map deref worms)))

  #_(doall (map deref worms))
  


  

)) ; cx end


;;; Lol ok everything broke <3 u all ...

