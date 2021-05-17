(ns ui.cx1
  (:require [reagent.core :as reagent :refer [atom]]
            [ui.text :as t :refer [cd1 cd2 cd3 cd4]]
            [ui.helpers :refer [cos sin style url val-cyc deform]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                          b1 b2 b3 b4 
                                          l1 l2 l3 l4 l5 l6
                                          ul2 ul3
                                          scr1 scr2 scr3 scr4 scr5]]
            [ui.fills :as fills :refer
              [gray charcoal mint midnight navy blue orange
                br-orange pink white yellow clear]]
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

(def drops
  (atom  (map
     #(->>
       (gen-rect white (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "2.2s" "infinite" {:delay (str (* .5 %) "s")})
       (style {:mix-blend-mode "color-dodge"})
       (draw))
     (range 20))))
     
     
     (def dr
       (atom  (map
          #(->>
            (gen-rect white (+ 0 (* % 160)) 10 400 260)
            (anim "etof" "3.2s" "infinite" {:delay (str (* .5 %) "s")})
            (style {:mix-blend-mode "color-dodge"})
            (draw))
          (range 10))))
          
               (def op
                 (atom  (map
                    #(->>
                      (gen-rect white (+ 0 (* % 160)) 10 400 260)
                      (anim "ascend" "3.2s" "infinite" {:delay (str (* .5 %) "s")})
                      (style {:mix-blend-mode "color-dodge"})
                      (draw))
                    (range 10))))

(def drops3
  (atom  (map
     #(->>
       (gen-rect white (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "2.2s" "infinite" {:delay (str (* .7 %) "s")})
       (style {:mix-blend-mode "color-dodge"})
       (draw))
     (range 20))))


(def drops2
  (atom  (map
     #(->>
       (gen-rect white (+ 30 (* % 160)) 10 200 36)
       (anim "slide-up" "2.2s" "infinite" {:delay (str (* .5 %) "s")})
       (style {:mix-blend-mode "color-dodge"})
       (draw))
     (range 10))))

(def spinlm 
  (->>
   (gen-shape yellow l1)
   (style {:opacity .7 :transform "translate(30vw, 30vh) scale(1.4)"})
    (anim "lump-morph" "5s" "infinite")

   (draw)
   #_(gen-group {:style {:animation "rot 2s infinite"}})
   (atom)))

(def spinlm2
  (->>
   (gen-shape blue l1)
   (style {:opacity .7 :transform "translate(30vw, 30vh) scale(2)"})
   
    (anim "l1l6" "33s" "infinite")
   (draw)
   #_(gen-group {:style {:animation "rot 2s infinite .4s"}})
   (atom)))
   
   (def spinlm4
     (->>
      (gen-shape mint l1)
      (style {:opacity .7 :transform "translate(-10vw, -10vh) scale(2)"})
      (style {:mix-blend-mode "luminosity"})
       (anim "l1l6" "33s" "infinite")
      (draw)
      #_(gen-group {:style {:animation "rot 2s infinite .4s"}})
      (atom)))

(def lm (->>
 (gen-shape pink l1)
 (style {:transform "translate(30vw, 30vh) scale(3.4)" :opacity .7})
 (anim "lump-morph" "40s" "infinite")
 (draw)
 (atom)))

(def lm2 (->>
 (gen-shape pink l1)
 (style {:transform "translate(40vw, 20vh) scale(2.5)" :opacity .7})
 (anim "l1l6" "16s" "infinite")
 (draw)
 (atom)))

(def lm5 (->>
 (gen-shape mint l1)
 (style {:transform "translate(10vw, 10vh) scale(2.5)"})
(style {:mix-blend-mode "overlay"})

 (anim "l1l6" "16s" "infinite")
 (draw)
 (atom)))

(def lm4 (->>
 (gen-shape blue l1)
 (style {:transform "translate(40vw, 20vh) scaleX(-2.5)"})
 (style {:mix-blend-mode "color-dodge"})
 (anim "l1l6" "6s" "infinite")
 (draw)
 (atom)))

(def lm6 (->>
 (gen-shape mint l1)
 (style {:transform "translate(42vw, 18vh) scale(-3.5)"})
 (style {:mix-blend-mode "overlay" :opacity .7})
 (anim "l1l6" "16s" "infinite")
 (draw)
 (gen-group {:style {:transform-origin "center" :animation "ascend 8s infinite"}})
 (atom)))



(def lm3 (->>
 (gen-shape pink l1)
 (style {:transform "translate(20vw, 20vh) scale(1)" :mix-blend-mode "color-dodge"})
 (anim "l2l4" "3s" "infinite")
 (draw)
 (atom)))

(def babrect1 
  (->>
   (gen-rect navy 0 0 @width (* .05 @height))
   (style {:mix-blend-mode "luminosity"})
   (anim "small-scale" "3.2s" "infinite")
   (draw)
   (atom)))

(def babrect2 
  (->>
   (gen-rect navy 0 (* .9 @height) @width (* .05 @height))
   (style {:mix-blend-mode "luminosity"})
   (anim "small-scale" "3.2s" "infinite")
   (draw)
   (atom)))

(def move-me
  (->>
   (gen-shape (pattern (:id mint-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "luminosity"})
   (anim "wee" "8s" "infinite")
   (draw)
   (atom)))
   
(def move-me-2
  (->>
   (gen-shape (pattern (:id mint-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "luminosity"})
   (anim "woosh" "8s" "infinite")
   (draw)
   (atom)))
   
   (def move-me-a
     (->>
      (gen-circ orange 30 30 30)
      (anim "wee" "13s" "infinite")
      (draw)
      (atom)))
      
         (def move-me-b
           (->>
            (gen-circ (pattern (:id yellow-lines-4)) 30 30 30)
            (anim "wee-2" "12.8s" "infinite")
            (draw)
            (atom)))

(def move-me-3
  (->>
   (gen-shape clear scr5)
   (style {:stroke gray
           :stroke-width 5})
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "6s" "infinite")
   (draw)
   (atom)))
   
(def move-me-5
  (->>
   (gen-shape clear scr1)
   (style {:stroke pink
           :stroke-width 5})
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "8s" "infinite")
   (draw)
   (atom)))
   
   (def move-me-7
     (->>
      (gen-shape clear scr4)
      (style {:stroke yellow
              :stroke-width 5})
      (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
      (anim "woosh" "7s" "infinite" {:delay "2s"})
      (draw)
      (atom)))
      
         (def move-me-9
           (->>
            (gen-shape clear scr4)
            (style {:stroke white
                    :stroke-width 8})
            (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
            (anim "woosh" "7s" "infinite")
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
          #_(style {:mix-blend-mode "color-dodge"} )

      (anim "loopy-left" "4s" "infinite")
    (draw)
    (atom)))

(def bb6a
  (->>
    (gen-shape mint oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "color-dodge"} )

      (anim "woosh-2" "2s" "infinite" {:delay ".4s"})
    (draw)
    (atom)))
    
    (def bb6b
      (->>
        (gen-shape midnight oct)
          (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
          ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
              (style {:mix-blend-mode "color-dodge"} )

          (anim "woosh-2" "5s" "infinite" {:delay "1s"})
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
          (gen-rect (pattern (str "noise-" gray)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" "3s" "infinite" {:delay ".5s"})
          (draw)
          (atom)))
          
          (def scale-me-3
            (->>
             (gen-circ pink (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
             (anim "scaley" "1s" "infinite" {:delay ".7s"})
             (draw)
             (atom)))

(def scale-me-4
  (->>
   (gen-circ (pattern (:id white-dots)) (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
   (style {:mix-blend-mode "difference"})
   (anim "scaley" "1s" "infinite" {:delay "1s"})
   (draw)
   (atom)))


(def sc-circ
  (->>
   (gen-circ (pattern (:id orange-lines)) (* 0.5 @width) (* 0.4 @height) 100)
   (anim "scaley-huge" "5s" "infinite")
   (draw)
   (atom)))

(def sc-circ-2
  (->>
   (gen-circ (pattern (:id pink-lines)) (* 0.5 @width) (* 0.4 @height) 100)
   (anim "scaley-huge" "5s" "infinite" {:delay "1.2s"})
   (draw)
   (atom)))

(def mf
  (->>
   (gen-shape "hsla(100, 100%, 100%, 0)" l1)
   (style {:stroke orange
           :stroke-width 14
           :stroke-dasharray 100
           :stroke-dashoffset 100
           :stroke-linecap "round"
           :stroke-join "round"})
   (anim "lump-morph" "12s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(5)"}})
   (atom)))
   
   
   (def mf6
     (->>
      (gen-shape "hsla(100, 100%, 100%, 0)" l1)
      (style {:stroke orange
              :stroke-width 14
              :stroke-dasharray 100
              :stroke-dashoffset 100
              :stroke-linecap "round"
              :stroke-join "round"})
      (anim "lump-morph" "10s" "infinite")
      (draw)
      (gen-group {:style {:transform-origin "center" :transform "translate(-20vw, -25vh) scale(5)"}})
      (atom)))

(def mf3
  (->>
   (gen-shape "hsla(100, 100%, 100%, 0)" l1)
   (style {:stroke yellow
           :stroke-width 14
           :stroke-dasharray 100
           :stroke-dashoffset 100
           ;å:stroke-linecap "round"
           ;:stroke-join "round"
           })
   (anim "lump-morph" "14s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(3)" :animation "ascend 12s infinite" }})
   (atom)))
   
   (def mf3a
     (->>
      (gen-shape "hsla(100, 100%, 100%, 0)" l1)
      (style {:stroke yellow
              :stroke-width 14
              :stroke-dasharray 100
              :stroke-dashoffset 100
              ;å:stroke-linecap "round"
              ;:stroke-join "round"
              })
      (anim "lump-morph" "14s" "infinite")
      (draw)
      (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(3)" }})
      (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(3)" :animation "ascend 18s infinite" }})
      (atom)))

(def mf2
  (->>
   (gen-shape "hsla(100, 100%, 100%, 0)" l1)
   (style {:stroke mint
           :stroke-width 14
           :stroke-dasharray 100
           :stroke-dashoffset 100
           :stroke-linecap "round"
           :stroke-join "round"})
   (anim "l1l6" "8s" "infinite" {:delay "1.2s"} )
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(-10vw, -10vh) scale(5)"}})
   (atom)))
   
  (def slide1
    (->>
     (gen-circ mint 0 0 40)
     (anim "right-down-1" "2s" "infinite")
     (draw)
     (atom)))
     
   (def slide2
     (->>
      (gen-circ (pattern (:id white-dots-3)) 0 0 40)
      (anim "right-down-1" "2s" "infinite" {:timing "ease-out"})
      (draw)
      (atom)))
      
     (def slide3
       (->>
        (gen-circ (pattern (:id pink-lines-3)) 0 0 40)
        (anim "left-up-1" "3s" "infinite" {:timing "ease-out"})
        (draw)
        (atom)))
        
             (def slide4
               (->>
                (gen-circ white 0 0 40)
                (anim "left-up-1" "3s" "infinite" {:timing "ease-out"})
                (draw)
                (atom)))


  (def slide5
    (->>
     (gen-circ mint 0 0 40)
     (anim "right-down-2" "2s" "infinite")
     (draw)
     (atom)))
     
   (def slide6
     (->>
      (gen-circ (pattern (:id white-dots-3)) 0 0 40)
      (anim "right-down-2" "2s" "infinite" {:timing "ease-out"})
      (draw)
      (atom)))
      
     (def slide7
       (->>
        (gen-circ (pattern (:id pink-lines-3)) 0 0 40)
        (anim "left-up-2" "3s" "infinite" {:timing "ease-out"})
        (draw)
        (atom)))
        
             (def slide8
               (->>
                (gen-circ white 0 0 40)
                (anim "left-up-2" "3s" "infinite" {:timing "ease-out"})
                (draw)
                (atom)))
                
    (def grr
      (->>
       (gen-rect (pattern (:id yellow-dots-4)) 0 0 "100%" "100%")
       (style {:opacity .6})
       (anim "rot" "10s" "infinite")
       (draw)
       (atom)))
       
           (def grr2
             (->>
              (gen-rect (pattern (:id br-orange-dots-3)) 0 0 "200%" "200%")
                     (style {:opacity .6})

              (anim "rot" "8s" "infinite" {:delay "3s" :timing "ease-out"})
              (draw)
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
      8 8
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
            (gen-line [10 10] [200 100] white 10)
            (draw))))

(def stst (atom (gen-group {:style {:transform-origin "center" :animation "rot 4s infinite"}} @streaks)))

(defonce streaks2
  (scatter 40 
           (->>
            (gen-line [10 10] [200 100] white 10)
            (draw))))

(def trio (atom 
      (gen-group
       {:style {:transform-origin "center" 
                :animation "rott 3s infinite"
                }}
       (->>
         (gen-grid
           30 20
           {:col 300 :row 300}
           (->>
            (gen-shape blue tri)))
           (map draw)
           (map #(gen-group {:style 
                             {:transform-origin "center" 
                              :transform "translate(-10vw, -10vh) scale(.3)"}} %))
           (map #(gen-group {:style {:transform-origin "center" :opacity ".7" :animation "ascend 1s infinite"}} %))))))
           
           (def trio2 (atom 
                 (gen-group
                  {:style {:transform-origin "center" 
                           :animation "rott 3s infinite"
                           }}
                  (->>
                    (gen-grid
                      30 20
                      {:col 310 :row 310}
                      (->>
                       (gen-shape mint tri)))
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
       (style {:mix-blend-mode "color-dodge"})
       (anim "small-scale-y" "1s" "infinite" 
             {:delay (str (* idx .1) "s")})
       (draw)
       atom))
    (range 20)))


(def blobs 
  (atom 
   (->>
    (gen-grid
      10 10
      {:col 800 :row 800}
      (->>
       (gen-shape pink l2)))
      #_(map #(style styles %))
      #_(map #(anim "l2l4" "12s" "infintite" %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center" :transform "scale(.25)" }} %))
    (map #(gen-group {:style {:transform-origin "center" :animation "descend-m 4s infinite"}} %)))))


(def blobs3 
  (atom 
   (->>
    (gen-grid
      10 10
      {:col 800 :row 800}
      (->>
       (gen-shape mint l2)))
      #_(map #(style styles %))
      #_(map #(anim "l2l4" "12s" "infintite" %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center" :transform "scale(.25)" }} %))
    (map #(gen-group {:style {:transform-origin "center" :animation "ascend 12s infinite"}} %)))))


(def blobs4 
  (atom 
   (->>
    (gen-grid
      10 10
      {:col 800 :row 800}
      (->>
       (gen-shape midnight l1)))
      #_(map #(style styles %))
      #_(map #(anim "l2l4" "12s" "infintite" %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center" :transform "scale(.25)" }} %))
    (map #(gen-group {:style {:transform-origin "center" :animation "ascend 12s infinite"}} %)))))

(def blobs2
  (atom 
   (->>
    (gen-grid
      10 10
      {:col 800 :row 800}
      (->>
       (gen-shape yellow l2)))
      (map #(style {:mix-blend-mode "color-dodge"} %))
      #_(map #(anim animations %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center" :transform "scale(.25)" }} %))
            (map #(gen-group {:style {:transform-origin "center" :animation "rot 1s infinite"}} %)))))


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
             ;midnight (pattern (:id midnight-lines-1)) (pattern (:id midnight-lines-5)) (pattern (:id midnight-lines-3))

             ;mint mint (pattern (:id mint-dots)) (pattern (:id mint-dots)) mint (pattern (:id mint-lines))
            ;mint mint mint mint
            pink pink pink pink
            ;pink (pattern (:id pink-lines-4)) pink (pattern (:id pink-dots-5))
            ;br-orange br-orange br-orange br-orange
            ;yellow yellow
            ;white white white white
            ;blue blue blue 
             ;
             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:opacity .95})
        (draw)))
        
        ;(doall (map deref levels))
        
        ;@grr
        ;@grr2
        
        (when (nth-frame 4 frame)(gen-line-grid midnight 2
          20  20
          {:col 80 :row 80}))
          
          ;#_@blobs4
        
        #_(when (nth-frame 1 slow-frame) (->>
         (gen-grid
           12 12
           {:col 100 :row 100}
           (->>
            (gen-rect midnight 4 4 20 20)))
           ;(map #(style styles %))
           ;(map #(anim animations %))
           (map draw)
           (map-indexed
            (fn [idx item]
              (when (nth-frame (* idx .5) frame) item)))
           (map #(gen-group {:style {:transform-origin "center" }} %))))
           
         #_(when (nth-frame 1 slow-frame) (->>
          (gen-grid
            8 12
            {:col 400 :row 400}
            (->>
             (gen-shape blue oct)))
            (map draw)
            (map-indexed
             (fn [idx item]
               (when (nth-frame (+ 1 idx) frame) item)))
            (map #(gen-group {:style {:transform-origin "center" :transform "scale(.2)" }} %))))

     #_(when (nth-frame 1 slow-frame) (->>
      (gen-grid
        12 12
        {:col 400 :row 400}
        (->>
         (gen-shape br-orange hex)))
        (map draw)
        (map-indexed
         (fn [idx item]
           (when (nth-frame (+ 2 idx) frame) item)))
        (map #(gen-group {:style {:transform-origin "center" :transform "scale(.2)" }} %))))
      
      #_(when (nth-frame 6 frame) (gen-line-grid pink 2
        40 40
        {:col 80 :row 80}))
        
              #_(when (nth-frame 8 frame) (gen-line-grid white 2
                20 5
                {:col 40 :row 80}))

 
  #_(gen-group {:mask (url "grad-mask")} (->>
   (gen-shape (pattern (:id navy-lines)) tri)
   (style {:transform "translate(40vw, 40vh) scale(4) rotate(180deg)"})
   (draw)
   (when-not (nth-frame 2 frame))))
   
     #_(gen-group {:mask (url "grad-mask")} (->>
      (gen-shape (pattern (:id pink-lines)) tri)
      (style {:transform "translate(40vw, 40vh) scale(4) rotate(180deg)"})
      (draw)
      (when (nth-frame 4 frame))))
      
           #_(gen-group {:mask (url "grad-mask") :style {:transform-origin "center" :transform "translate(30vh, 0vh)"}} (->>
            (gen-shape (pattern (:id pink-lines)) tri)
            (style {:transform "translate(40vw, 40vh) scale(4) rotate(180deg)"})
            (draw)
            (when (nth-frame 3 (+ 1 frame)))))
            
            #_(gen-group {:mask (url "grad-mask") :style {:transform-origin "center" :transform "translate(-30vh, 0vh)"}} (->>
             (gen-shape (pattern (:id pink-lines)) tri)
             (style {:transform "translate(40vw, 40vh) scale(4) rotate(180deg)"})
             (draw)
             (when (nth-frame 3 (- 1 frame)))))

;@trio
;@trio2

;@lm6

;@move-me-2
;@bb6
;@bb6a


  #_(->>
   (gen-shape clear scr2)
   (style {:stroke pink
           :stroke-width 3})
   (style {:transform "translate(40vw, 25vh) scale(4)"})
   (draw)
   (when (nth-frame 5 frame)))
   
     #_(->>
      (gen-shape clear scr2)
      (style {:stroke midnight
              :stroke-width 3})
      (style {:transform "translate(40vw, 25vh) scale(4)"})
      (draw)
      (when (nth-frame 5 (- 1 frame))))
   
   
   #_(->>
    (gen-shape clear scr3)
    (style {:stroke white
            :stroke-width 2})
    (style {:transform "translate(40vw, 70vh) scale(-4.5)"})
    (draw)
    (when (nth-frame 0  (+ 1 frame))))
    
  #_(->>
   (gen-shape clear scr4)
   (style {:stroke yellow
           :stroke-width 2
           :stroke-dasharray 15
           :stroke-dashoffset 15})
   (style {:transform "translate(50vw, 40vh) scale(7)"})
   (draw)
   (when (nth-frame 3 slow-frame)))   
   
   
     #_(->>
      (gen-shape clear scr4)
      (style {:stroke yellow
              :stroke-width 2
              :stroke-dasharray 15
              :stroke-dashoffset 15})
      (style {:transform "translate(20vw, 40vh) scale(7)"})
      (draw)
      (when (nth-frame 5 slow-frame)))   
      
      
        #_(->>
         (gen-shape clear scr4)
         (style {:stroke yellow
                 :stroke-width 2
                 :stroke-dasharray 15
                 :stroke-dashoffset 15})
         (style {:transform "translate(70vw, 40vh) scale(7)"})
         (draw)
         (when (nth-frame 4 slow-frame)))   
         
   
     #_(->>
      (gen-circ (pattern (:id gray-lines)) (* 0.5 @width) (* 0.5 @height) 200)
      (style {:transform "rotate(45deg)"})
      (draw)
      (when (nth-frame 3 frame)))
        
      #_(->>
       (gen-circ (pattern (:id orange-lines)) (* 0.5 @width) (* 0.5 @height) 180)
       (style {:transform "rotate(-45deg)"})
       (draw)
       (when (nth-frame 11 frame))) 
         
  #_(->>
   (gen-circ gray (* 0.5 @width) (* 0.5 @height) 140)
   (style {:transform "rotate(45deg)"})
   (draw)
   (when (nth-frame 1 frame)))

;@scale-me

    


  #_(->>
   (gen-circ (pattern (:id navy-lines-5)) (* 0.5 @width) (* 0.5 @height) 200)
   (draw)
   (when (nth-frame 4 frame)))
   

   ;@move-me-5
   
   ;@move-me-7
   
   ;@move-me-9
   
   
    #_(->>
     (gen-circ (pattern (:id white-lines-3)) (* 0.5 @width) (* 0.5 @height) 300)
     (draw)
     (when (nth-frame 4 (+ 2 frame))))
     
     
         #_(->>
          (gen-circ (pattern (:id orange-lines-5)) (* 0.5 @width) (* 0.5 @height) 300)
          (draw)
          (when (nth-frame 6 frame)))
          
     
          #_(->>
           (gen-rect white (* 0.25 @width) (* 0.25 @height) (* 0.6 @width) 100)
           (draw)
           (when (nth-frame 3 frame)))
           
           #_(->>
            (gen-rect white (* 0.25 @width) (* 0.45 @height) (* 0.6 @width) 100)
            (draw)
            (when (nth-frame 3 (+ 1 frame))))
            
               #_(->>
                (gen-rect white (* 0.25 @width) (* 0.65 @height) (* 0.6 @width) 100)
                (draw)
                (when (nth-frame 3 (+ 2 frame))))
          
        (->>
         (gen-rect midnight (* 0.1 @width) (* 0.1 @height) 400 400)
         (draw)
         (when (nth-frame 1 (+ 2 frame))))
         
         (->>
          (gen-rect midnight (* 0.7 @width) (* 0 @height) (* 0.8 @width) @height)
          (draw)
          (when (nth-frame 1 (+ 4 frame))))
          
          ;@mf3
          ;@mf3a
     
     

     
 #_(->>
  (gen-circ (pattern (:id orange-lines-5)) (* 0.5 @width) (* 0.5 @height) 200)
  (draw)
  (when (nth-frame 7 frame)))


    
     ;(doall (map deref line-growth))
    
    ;@dr
    ;@op
    
    #_(->>
     (gen-shape (pattern (:id midnight-dots-4)) pent)
     (style {:transform "translate(38vw, 40vh) scale(2)"})
     (draw)
     (when (nth-frame 5 frame)))
    
    (when (nth-frame 4 frame)
      (freak-out @width
                 @height
                 30
                 200
                 pink))
                 
                     (when (nth-frame 8 frame)
                       (freak-out @width
                                  @height
                                  30
                                  200
                                  mint
                                  {:mix-blend-mode "luminosity"}))
                 
                 
                     (when (nth-frame 1 frame)
                       (freak-out @width
                                  @height
                                  50
                                  300
                                  white))
                                  
                   (->>
                    (gen-circ (pattern (:id midnight-lines-5)) (* 0.5 @width) (* 0.5 @height) 300)
                    (draw)
                    (when (nth-frame 1 frame)))
  
)) ; cx end
