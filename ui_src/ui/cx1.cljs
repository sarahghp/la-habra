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
(def transparent "hsla(0, 0%, 0%, 0)")

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
   
   (def mm
     (->>
      (gen-shape (pattern (:id yellow-lines-5)) hept)
      (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
      (anim "woosh-2" "8s" "infinite")
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
      (gen-circ orange 30 30 120)
      (anim "wee" "13s" "infinite")
      (draw)
      (atom)))
      
         (def move-me-b
           (->>
            (gen-circ (pattern (:id yellow-lines-4)) 30 30 120)
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
   (style {:stroke yellow
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
          (style {:mix-blend-mode "color-dodge"} )

      (anim "loopy-left" "10s" "infinite")
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
    
    (def bb6c
      (->>
        (gen-shape white oct)
          (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
          ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
              ;(style {:mix-blend-mode "color-dodge"} )

          (anim "woosh" "12s" "infinite" {:delay ".4s"})
        (draw)
        (atom)))
        
        
            (def bb6d
              (->>
                (gen-shape orange oct)
                  (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
                  ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
                      ;(style {:mix-blend-mode "color-dodge"} )

                  (anim "woosh" "12s" "infinite" {:delay "1s"})
                (draw)
                (atom)))
    
    (def bb6b
      (->>
        (gen-shape white oct)
          (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
          ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
              ;(style {:mix-blend-mode "color-burn"} )

          (anim "woosh-2" "5s" "infinite" {:delay "0s"})
        (draw)
        (atom)))

(def scale-me
        (->>
          (gen-rect (pattern (str "noise-" white)) 0 0 @width @height)
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
   (anim "scaley" "6s" "infinite" {:delay "0s"})
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
   (anim "lump-morph" "52s" "infinite")
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
      (anim "lump-morph" "40s" "infinite")
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
   (anim "lump-morph" "54s" "infinite")
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
      (anim "lump-morph" "54s" "infinite")
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
   (anim "l1l6" "48s" "infinite" {:delay "1.2s"} )
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(-10vw, -10vh) scale(5)"}})
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
            (anim "fade-in-out" "12s" "infinite" {:delay (str (* .5 idx) "s")})
            (draw)
            (atom)))
    (take 10 (repeatedly #(nth [blue blue blue midnight mint yellow] (rand-int 6))))))

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
                          :transform (str "translate(" (+ (rand-int 20) (rand-int 40)) 
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
   
(defonce recties
  (scatter 100 400 100 500 2 6 10
    (->>
     (gen-rect (pattern (:id navy-dots-1)) 0 0 40 50)
     (draw))))
     
     (defonce recties-4
       (scatter 100 400 100 500 2 6 20
         (->>
          (gen-rect (pattern (:id navy-dots-3)) 0 0 40 50)
          (draw))))
          
          
               (defonce recties-5
                 (scatter 100 400 500 800 2 6 20
                   (->>
                    (gen-rect (pattern (:id white-dots-3)) 0 0 40 50)
                    (draw))))
                    
                    (defonce recties-6
                      (scatter 500 800 100 500 2 6 20
                        (->>
                         (gen-rect (pattern (:id pink-dots-3)) 0 0 40 50)
                         (draw))))
                    
     
  (defonce recties-3
    (scatter 300 800 100 500 2 6 20
      (->>
       (gen-rect (pattern (:id pink-dots-1)) 0 0 40 50)
       (draw))))

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
                :animation "rot 3s infinite"
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
           (map #(gen-group {:style {:transform-origin "center" :opacity ".7" :animation "ascend 4s infinite"}} %))))))
           
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
    
     
(def slidey-blobs
  (atom 
    (gen-group {:style {:transform-origin "center" :opacity ".7" :animation "ascend 10s infinite"}}
      (->>
          (gen-shape mint l1)
          (style {:transform "translate(10vw, 10vh) scale(2.5)"})
          (style {:mix-blend-mode "overlay"})
          (anim "l1l6" "16s" "infinite")
          (draw))
      (->> 
         (gen-shape pink l1)
         (style {:transform "translate(40vw, 20vh) scale(2.5)" :opacity .7})
         (anim "l1l6" "16s" "infinite")
         (draw))
      )))




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
      (map #(gen-group {:style {:transform-origin "center" :transform "scale(.15)" }} %))
    (map #(gen-group {:style {:transform-origin "center" :animation "ascend 12s infinite"}} %)))))

(def blobs2
  (atom 
   (->>
    (gen-grid
      10 10
      {:col 800 :row 800}
      (->>
       (gen-shape yellow l6))) (map #(style {:mix-blend-mode "color-dodge"} %))
      #_(map #(anim animations %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center" :transform "scale(.15)" }} %))
            (map #(gen-group {:style {:transform-origin "center" :animation "rot 1s infinite"}} %)))))
            
(def sqrts1
  (->>
   (gen-rect pink 100 100 300 300)
      (anim "right-right" "6s" "infinite")
   (draw)
   (atom)))
   
(def sqrts2
  (->>
   (gen-rect orange 100 40 300 300)
      (anim "right-right" "6s" "infinite")
   (draw)
   (atom)))
   
   
   (def sqrts3
     (->>
      (gen-rect (pattern (:id mint-dots-5)) 100 40 400 400)
         (anim "right-right-2" "12s" "infinite")
      (draw)
      (atom)))
      
      
 (def sqrts4
   (->>
    (gen-rect (pattern (:id mint-dots-5)) 100 40 400 400)
       (anim "ascend" "12s" "infinite")
    (draw)
    (atom)))
    
 (def sqrts5
   (->>
    (gen-rect (pattern (:id white-dots-4)) 100 40 400 400)
       (anim "ascend" "12s" "infinite" {:delay ".4s"})
    (draw)
    (atom)))
    
            
(def sqrts6
  (->>
   (gen-rect pink "70vw" 100 300 300)
      (anim "descend" "16s" "infinite")
   (draw)
   (atom)))
   
(def sqrts7
  (->>
   (gen-rect orange "70vw" 40 300 300)
      (anim "descend" "16s" "infinite")
   (draw)
   (atom)))
   

;; start end dur frame no-repeat
(def lerp1 (lerp))
(def lerp2 (lerp))
(def lerp3 (lerp))

 ;; ----------- COLLECTION SETUP AND CHANGE ----------------
 
 (defn list1 [frame fast-frame slow-frame svg-frame]
   (list
     (doall (map deref worms))
     (let
       [colors [
                ;midnight midnight midnight midnight
                ;midnight (pattern (:id midnight-lines-1)) (pattern (:id midnight-lines-5)) (pattern (:id midnight-lines-3))

                ;mint mint (pattern (:id mint-dots)) (pattern (:id mint-dots)) mint (pattern (:id mint-lines))
               ;mint mint mint mint
               ;mint mint mint mint
               ;pink pink pink pink
               ;pink (pattern (:id pink-lines-4)) pink (pattern (:id pink-dots-5))
               ;br-orange br-orange br-orange br-orange
               ;yellow yellow yellow yellow
               ;yellow yellow yellow yellow
               ;white white white white
               blue blue blue 
                ;
                ]]
         (->>
           (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
           (style {:opacity .95})
           (draw)))
           
           (->>
            (gen-rect "#00f" 0 0 (* 0.2 @width) @height)
            (draw))
            
           (->>
            (gen-rect "#00c" (* 0.2 @width) 0 (* 0.2 @width) @height)
            (draw))
           
          (->>
           (gen-rect "#00a" (* 0.4 @width) 0 (* 0.2 @width) @height)
           (draw))
           
         (->>
          (gen-rect "#009" (* 0.6 @width) 0 (* 0.2 @width) @height)
          (draw))
          
         (->>
          (gen-rect "#006" (* 0.8 @width) 0 (* 0.2 @width) @height)
          (draw))
      ; (->>
                ;  (gen-rect (pattern (:id white-lines-4)) 100 100 "30%" "30%")
                ;  (style {:mix-blend-mode "luminosity"})
                ;  (draw)
                ;  (when (nth-frame 2 slow-frame)))
                ; 
                ;  (->>
                ;   (gen-rect (pattern (:id white-dots-5)) 600 100 "30%" "20%")
                ;   (style {:mix-blend-mode "luminosity" })
                ; 
                ;   (draw)
                ;   (when (nth-frame 3 slow-frame)))
                ; 
                ; 
                ;   (->>
                ;    (gen-rect (pattern (:id white-lines-4)) 300 300 "50%" "50%")
                ;   (style {:mix-blend-mode "luminosity"})
                ; 
                ;    (draw)
                ;    (when (nth-frame 4 slow-frame)))
                  
                  ;@move-me-5
                  ;@bb6a
                  @bb6c
                  
                  @bb6d
           
           
           ))
           
   (defn list2 [frame fast-frame  slow-frame svg-frame]
     (list
      (doall (map deref worms))

       (let
         [colors [
                  ;midnight midnight midnight midnight
                  ;midnight (pattern (:id midnight-lines-1)) (pattern (:id midnight-lines-5)) (pattern (:id midnight-lines-3))

                  midnight midnight (pattern (:id midnight-dots-4)) (pattern (:id midnight-dots-3)) midnight midnight 
                 ;mint mint mint mint
                 ;mint mint mint mint
                 ;pink pink pink pink
                 ;pink (pattern (:id pink-lines-4)) pink (pattern (:id pink-dots-5))
                 ;br-orange br-orange br-orange br-orange
                 ;yellow yellow yellow yellow
                 ;yellow yellow yellow yellow
                 ;white white white white
                 ;(pattern (:id blue-lines-5)) 
                 ;blue 
                  ;
                  ]]
           (->>
             (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
             (style {:opacity .95})
             (draw)))
             
             ;@scale-me-4
             ;@spinlm2
             @bb6
            
            
                  
                  
                        
            ))
            
            
(defn list3 [frame fast-frame  slow-frame svg-frame]
                
                 (list
                   (doall (map deref worms))
                   (let
                     [colors [
                              ;midnight midnight midnight midnight
                              ;midnight (pattern (:id midnight-lines-1)) (pattern (:id midnight-lines-5)) (pattern (:id midnight-lines-3))

                              ;mint mint (pattern (:id mint-dots)) (pattern (:id mint-dots)) mint (pattern (:id mint-lines))
                             ;mint mint mint mint
                             mint mint mint mint
                             pink pink pink pink
                             ;pink (pattern (:id pink-lines-4)) pink (pattern (:id pink-dots-5))
                             br-orange br-orange br-orange br-orange
                             ;yellow yellow yellow yellow
                             ;yellow yellow yellow yellow
                             ;white white white white
                             ;(pattern (:id blue-lines-5)) blue (pattern (:id blue-lines-2))blue
                              ;
                              ]]
                       (->>
                         (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
                         (style {:opacity .95})
                         (draw)))
                         
                         
                         
                         
                         ;@trio
                         ;@trio2
                         
                         #_(->>
                          (gen-shape midnight tri)
                          (style {:transform "translate(40vw, 20vh) scale(3) rotate(135deg)"})
                          (draw)
                          (when (nth-frame 4 frame)))
                          
                                   #_(->>
                                    (gen-shape midnight tri)
                                    (style {:transform "translate(30vw, 40vh) scale(3) rotate(-135deg)"})
                                    (draw)
                                    (when (nth-frame 3 frame)))
                                    
                                    
                        #_(gen-group {:style {:transform "translate(50vh)"}}
                          
                          #_(->>
                           (gen-shape midnight tri)
                           (style {:transform "translate(60vw, 20vh) scale(3) rotate(135deg)"})
                           (draw)
                           (when (nth-frame 8 frame)))
                           
                                    #_(->>
                                     (gen-shape midnight tri)
                                     (style {:transform "translate(30vw, 60vh) scale(3) rotate(-135deg)"})
                                     (draw)
                                     (when (nth-frame 2 frame))))
                                     
                                     @mf
                                     @mf2
                                     



                        ))
                        
                        
(defn list4 [frame fast-frame  slow-frame svg-frame]
  
  (list
    (let
      [colors [
               ;midnight midnight midnight midnight
               ;blue blue blue blue
               ;midnight (pattern (:id midnight-lines-1)) (pattern (:id midnight-lines-5)) (pattern (:id midnight-lines-3))

               ;mint mint (pattern (:id mint-dots)) (pattern (:id mint-dots)) mint (pattern (:id mint-lines))
              ;mint mint mint mint
              ;mint mint mint mint
              ;"#4400a3" "#4400a3" "#4400a3" "#4400a3"
              ;pink pink pink pink
              ;pink (pattern (:id pink-lines-4))  (pattern (:id pink-dots-5))
              ;br-orange br-orange br-orange br-orange
              yellow yellow yellow yellow
              ;yellow yellow yellow yellow
              ;white white white white
              ;(pattern (:id blue-lines-5)) blue (pattern (:id blue-lines-2))blue
               ;
               ]]
        (->>
          (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
          (style {:opacity .95})
          (draw)))
          
          ;@blobs3
          ;@blobs2
          ;@blobs4
          
  
  ))
  
(defonce a
  (scatter 100 (->>
   (gen-circ pink 10 10 60)
   ;(style {:mix-blend-mode "color-dodge"})
   (draw))))
   
   
   (defonce b
     (scatter 100 (->>
      (gen-circ pink 10 10 60)
      (style {:mix-blend-mode "color-dodge"})
      (draw))))
   
(def aaa (atom (gen-group {:style {:animation "ascend 3s infinite"}} @a)))
(def aa (atom (gen-group {:style {:transform-origin "center":animation "rot 3s infinite"}} @a)))
(def cc (atom (gen-group {:style {:transform-origin "center":animation "rot 3s infinite"}} @b)))

  
(defn list5 [frame fast-frame  slow-frame svg-frame]
  (list
    (doall (map deref worms))

    (let
      [colors [
               ;midnight midnight midnight midnight
              ;blue blue blue blue
              ;orange orange orange orange 
              ;transparent
               ;midnight (pattern (:id midnight-lines-1)) (pattern (:id midnight-lines-5)) (pattern (:id midnight-lines-3))

               ;mint mint (pattern (:id mint-dots)) (pattern (:id mint-dots)) mint (pattern (:id mint-lines))
              ;mint mint mint mint
              ;mint mint mint mint
              ;"#4400a3" "#4400a3" "#4400a3" "#4400a3"
              ;pink pink pink pink
              ;pink (pattern (:id pink-lines-4))  (pattern (:id pink-dots-5))
              br-orange br-orange br-orange br-orange
              ;yellow yellow yellow yellow
              ;yellow yellow yellow yellow
              ;white white white white
              ;(pattern (:id blue-lines-5)) blue (pattern (:id blue-lines-2))blue
              
              ;white white pink pink blue blue
              
              ;"#000"
               ;
               ]]
        (->>
          (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
          (style {:opacity .95})
          (draw)))
          



      @mm
      ;@trio
      
      ;@move-me
      @move-me-2
      
      @move-me-5
      
      
        
        @aa
          
          @cc
      
      @aaa
      
      
      
  
  ))

(defn cx [fast-frame frame slow-frame svg-frame]
  (val-cyc svg-frame [
    (list4 fast-frame frame slow-frame svg-frame)
    ;(list1 fast-frame frame slow-frame svg-frame)
    ;(list5 fast-frame frame slow-frame svg-frame)
    ;(list2 fast-frame frame slow-frame svg-frame)
    ;(list1 fast-frame frame slow-frame svg-frame)
    ;(list2 fast-frame frame slow-frame svg-frame)
    ; (list1 fast-frame frame slow-frame svg-frame)
    (list3 fast-frame frame slow-frame svg-frame)
    (list3 fast-frame frame slow-frame svg-frame)


  ])) ; cx end
  
