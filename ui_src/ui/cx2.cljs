(ns ui.cx2
  (:require [reagent.core :as reagent :refer [atom]]
            [ui.text :as t :refer [cd1 cd2 cd3 cd4]]
            [ui.helpers :refer [cos sin style url val-cyc deform]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                          b1 b2 b3 b4]]
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
       (style {:mix-blend-mode "overlay"})
       (draw))
     (range 10))))



(def move-me
  (->>
   (gen-shape (pattern (:id mint-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "luminosity"})
   (anim "woosh" "3s" "infinite")
   (draw)
   (atom)))




(def move-me-2
  (->>
   (gen-shape (pattern (:id cd2)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "difference"})
   (anim "loopy-left" "16s" "infinite")
   (draw)
   (atom)))

(def move-me-3
  (->>
   (gen-shape (pattern (:id cd3)) tri)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "difference"})
   (anim "loopy-right" "16s" "infinite")
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


(def bnz-2
  (->>
   (gen-shape (pattern (str "noise-" white)) b2)
   (style {:opacity 1 :transform-origin "center" :transform "scale(.4)"})
   (style {:mix-blend-mode "difference"})
   (anim "sc-rot" "6s" "infinite")
   (draw)
   (atom)))

(def bb2
  (->>
    (gen-shape mint oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      (style {:mix-blend-mode "luminosity" :filter (url (:id noiz))} )
      ;(style {:mix-blend-mode "difference"} )
      (anim "woosh" "4s" "infinite")
    (draw)
    (atom)))

(def bb4
  (->>
    (gen-shape yellow oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          ;(style {:mix-blend-mode "difference"} )

      (anim "woosh" "3s" "infinite")
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

      (anim "woosh-2" "6s" "infinite" {:delay ".4s"})
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
   (gen-shape (pattern (:id pink-dots)) tri)
   (anim "morph" "4s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(3)"}})
   (atom)))

(def mf2
  (->>
   (gen-shape (pattern (:id white-dots)) tri)
   (anim "morph" "3s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(50vw, 15vh) scale(3)"}})
   (atom)))

(def mf3
  (->>
   (gen-shape (pattern (:id mint-dots)) tri)
   (anim "morph" "7s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(10vw, 35vh) scale(3)"}})
   (atom)))

(def mf4
  (->>
   (gen-shape (pattern (:id white-dots)) tri)
   (anim "morph" "2s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(0vw, 10vh) scale(5)"}})
   (atom)))

(def mf5
  (->>
   (gen-shape (pattern (:id white-dots)) tri)
   (anim "morph" "10s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(50vw, 40vh) scale(3)"}})
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


(def astro-mots
  (map-indexed
    (fn [idx [color val]]
          (atom (gen-group 
                 #_{:style {:animation (str "woosh-3 " (rand-int 4) "s infinite " (rand) "s")}}
                 {}
                 [:text {:key (random-uuid)
                  :x (* (rand) @width)
                  :y (* (rand) @height)
                  :text-anchor "middle"
                  :style {:font "bold 240px monospace"
                          :fill white}}
           val])))
    (take 3 (repeatedly #(nth [[pink 10] [pink 9] [pink 8] [pink 7] [pink 6] [pink 5] [pink 4] [pink 3] [pink 2] [white 1]] (rand-int 9))))))

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

(defonce c
  (scatter 40 (->>
   (gen-circ mint 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))


(defonce d
  (scatter 40 (->>
   (gen-circ navy 10 10 80)
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
             (gen-shape mint tri)))
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
             (gen-shape yellow tri)))
              (map #(style {:opacity 1 :mix-blend-mode "difference"} %))

            (map #(anim "morph" "5s" "infinite" %))
            (map draw)
                  (map #(gen-group {:style {:transform-origin "center" :transform "scale(2)"}} %))
          (map #(gen-group {:style {:transform-origin "center" :transform "translate(-200px, 100px)"}} %))
            (map #(gen-group { :style {:transform-origin "center"  :animation "rot 5s infinite" }} %)))))

(def l1 (lerp))

(defn cx2 [frame]
  (list
      
   ; grid (80/20), b, c, d
    
   (when (nth-frame 4 frame) @b)
      (when (nth-frame 5 frame) @c)
      (when (nth-frame 6 frame) @d)
   
   (when (nth-frame 2 frame)(gen-line-grid white 2
     80 80
     {:col 20 :row 20}))



   ))


   ;; ok time for an experiment ... let's see if this works ... 