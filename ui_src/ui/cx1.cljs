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
     (range 20))))

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
   (gen-shape pink l1)
   (style {:opacity .7 :transform "translate(30vw, 30vh) scale(.8)"})
   
    (anim "l1l6" "3s" "infinite")
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
   (anim "woosh" "13s" "infinite")
   (draw)
   (atom)))

(def move-me-3
  (->>
   (gen-shape (pattern (:id pink-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "6s" "infinite")
   (draw)
   (atom)))

(def move-me-2
  (->>
   (gen-shape (pattern (:id white-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   ;(style {:mix-blend-mode "luminosity"})
   (anim "woosh-2" "8s" "infinite")
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

      (anim "loopy-left" "6s" "infinite")
    (draw)
    (atom)))

(def bb6a
  (->>
    (gen-shape mint oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "color-dodge"} )

      (anim "woosh" "3s" "infinite" {:delay ".4s"})
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
   (style {:stroke mint
           :stroke-width 14
           :stroke-dasharray 100
           :stroke-dashoffset 100
           :stroke-linecap "round"
           :stroke-join "round"})
   (anim "lump-morph" "12s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(5)"}})
   (atom)))

(def mf3
  (->>
   (gen-shape "hsla(100, 100%, 100%, 0)" l1)
   (style {:stroke yellow
           :stroke-width 14
           :stroke-dasharray 100
           :stroke-dashoffset 100
           :stroke-linecap "round"
           :stroke-join "round"})
   (anim "lump-morph" "14s" "infinite")
   (draw)
   (gen-group {:style {:transform-origin "center" :transform "translate(20vw, 15vh) scale(3)"}})
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
           20 20
           {:col 300 :row 300}
           (->>
            (gen-shape blue tri)))
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
       (gen-shape navy l2)))
      #_(map #(style styles %))
      #_(map #(anim "l2l4" "12s" "infintite" %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center" :transform "scale(.25)" }} %))
    (map #(gen-group {:style {:transform-origin "center" :animation "ascend 12s infinite 2s"}} %)))))

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
             ;mint mint mint mint
            ;pink pink 
            ;yellow yellow
            white white white white
             ;
             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:opacity .95})
        (draw)))
      




#_(->>
 (gen-shape "hsla(0, 0%, 0%, 0)" tri)
 (style {:stroke white
         :stroke-width 4
         :stroke-dasharray 10
         :stroke-dashoffset 10
         :stroke-linecap "round"})
 (style {:transform-origin "center" :transform (str "translate(60vw, 60vh) rotate("
         (val-cyc frame [120 120 120 120 -60 -60 -60 -60 80 80 80 80 245 245 245 245])
         "deg) scale(.2)")})
 (style {:mix-blend-mode "color-dodge"})
 (draw)
 (when (nth-frame 3 frame)))
  
  #_(->>
   (gen-shape "hsla(0, 0%, 0%, 0)" hept)
   (style {:stroke white
           :stroke-width 4
           :stroke-dasharray 10
           :stroke-dashoffset 10
           :stroke-linecap "round"})
   (style {:transform-origin "center" :transform (str "translate(40vw, 40vh) rotate("
           (val-cyc frame [120 120 120 120 -60 -60 -60 -60 80 80 80 80 245 245 245 245])
           "deg) scale(3.2)")})
   (style {:mix-blend-mode "color-dodge"})
   (draw)
   (when (nth-frame 4 frame)))
  
  
    #_(->>
     (gen-shape "hsla(0, 0%, 0%, 0)" tri)
     (style {:stroke white
             :stroke-width 4
             :stroke-dasharray 10
             :stroke-dashoffset 10})
     (style {:transform-origin "center" :transform (str "translate(20vw, 10vh) rotate("
             (val-cyc frame [120 120 120 120 -60 -60 -60 -60 80 80 80 80 245 245 245 245])
             "deg) scale(6.2)")})
     (style {:mix-blend-mode "color-dodge"})
     (draw)
     (when (nth-frame 7 frame)))
  

  
  

  

  ; (->>
  ;  (gen-rect (pattern (:id blue-lines)) 0 0 (* 0.5 @width) (* 0.5 @height))
  ;  (style {:transform "rotate(135deg) scale(3)"})
  ;  (draw)
  ;  (when (nth-frame 4 fast-frame)))
  ; 
  ;    (->>
  ;     (gen-rect (pattern (:id white-dots)) 0 0 (* 0.5 @width) (* 0.5 @height))
  ;     (style {:transform "rotate(135deg) scale(3)"})
  ;     (draw)
  ;     (when (nth-frame 8 fast-frame)))
  

  @blobs2
  @blobs4

  
  #_(->>
   (gen-rect white (* 0.25 @width) (* 0.25 @height) (* 0.5 @width) (* 0.5 @height))
   (style {:transform-origin "center" :transform "scale(4)"})
   (style {:mix-blend-mode (val-cyc frame 
                                    ["multiply"
                                    "multiply"
                                    "multiply"
                                    "multiply"
                                    "color-dodge"
                                    "color-dodge"
                                    "color-dodge"
                                    "color-dodge"]
                            
                                    )})
   (draw)
   (when (nth-frame 4 frame)))
  

    ;@bb6

  
  ; (->>
  ;  (gen-rect white "6vw" "6vw" (* 0.4 @width) (* 0.4 @height))
  ;  (draw)
  ;  (when-not (nth-frame 4 frame)))
  ; 
  ;   (->>
  ;    (gen-rect blue "6vw" "6vw" (* 0.4 @width) (* 0.4 @height))
  ;   (style {:transform "translate(50vw, 50vh)"})
  ;    (draw)
  ;    (when-not (nth-frame 3 frame)))
  ; 
  ;     (->>
  ;      (gen-rect br-orange "6vw" "6vw" (* 0.4 @width) (* 0.4 @height))
  ;     (style {:transform "translate(0vw, 50vh)"})
  ;      (draw)
  ;      (when-not (nth-frame 6 frame)))
  ; 
  ;            (->>
  ;             (gen-rect yellow "6vw" "6vw" (* 0.4 @width) (* 0.4 @height))
  ;            (style {:transform "translate(50vw, 0vh)"})
  ;             (draw)
  ;             (when-not (nth-frame 7 frame)))
    
  ;(doall (map deref line-growth))

(->>
 (gen-circ pink (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
 (style {:transform "rotate(135deg)"})
 (draw)
 (when-not (nth-frame 12 frame)))
 

   
  
  #_(->>
   (gen-circ yellow (* 0.5 @width) (* 0.5 @height) 200 (url "grad-mask"))
   (draw)
   (when (nth-frame 2 frame)))
  
  
  #_(->>
   (gen-circ (pattern (:id pink-lines)) (* 0.5 @width) (* 0.5 @height) 200)
   (draw)
   (when (nth-frame 5 frame)))
  

  
  
  #_(gen-group {} (->>
   (gen-line [80 (lerp1 0 700 50 frame)] [80 (lerp1 100 800 50 frame)] midnight 12)
   (style {:stroke-linecap "round"})
   (draw)
   (when (nth-frame 1 frame)))
  
    (->>
     (gen-line [100 (lerp1 20 700 50 frame)] [100 (lerp1 120 820 50 frame)] midnight 12)
     (style {:stroke-linecap "round"})
     (draw)
     (when (nth-frame 1 frame))))
  
    #_(gen-group {} (->>
     (gen-line [600 (lerp1 0 700 50 (+ 3 frame))] [600 (lerp1 100 800 50 frame)] midnight 12)
     (style {:stroke-linecap "round"})
     (draw)
     (when (nth-frame 1 frame)))
    
      (->>
       (gen-line [620 (lerp3 20 700 30 frame)] [620 (lerp3 120 820 100 frame)] midnight 12)
       (style {:stroke-linecap "round"})
       (draw)
       (when (nth-frame 1 frame))))
  
    #_(gen-group {} (->>
     (gen-line [400 (lerp1 0 700 50 (+ 2 frame))] [400 (lerp1 100 800 50 frame)] midnight 12)
     (style {:stroke-linecap "round"})
     (draw)
     (when (nth-frame 1 frame)))
    
      (->>
       (gen-line [420 (lerp2 20 700 80 (+ 2 frame))] [420 (lerp2 120 820 50 frame)] midnight 12)
       (style {:stroke-linecap "round"})
       (draw)
       (when (nth-frame 1 frame))))


  ;@move-me-2
  ;@move-me-3
  
          ;@mf
          ;@mf2
         ;@mf3
          ; 
  
  

    

  
  #_(gen-group {:style {:transform-origin "center" :transform "translateX(80px)"}}
             (gen-group {} (->>
              (gen-line [80 (lerp1 0 700 50 frame)] [80 (lerp1 100 800 50 frame)] midnight 14)
              (style {:stroke-linecap "round"})
              (draw)
              (when (nth-frame 1 frame)))
             
               (->>
                (gen-line [100 (lerp1 20 700 50 frame)] [100 (lerp1 120 820 50 frame)] midnight 14)
                (style {:stroke-linecap "round"})
                (draw)
                (when (nth-frame 1 frame))))
             
               (gen-group {} (->>
                (gen-line [600 (lerp1 0 700 50 (+ 3 frame))] [600 (lerp1 100 800 50 frame)] midnight 14)
                (style {:stroke-linecap "round"})
                (draw)
                (when (nth-frame 1 frame)))
               
                 (->>
                  (gen-line [620 (lerp3 20 700 30 frame)] [620 (lerp3 120 820 100 frame)] midnight 14)
                  (style {:stroke-linecap "round"})
                  (draw)
                  (when (nth-frame 1 frame))))
             
               (gen-group {} (->>
                (gen-line [400 (lerp1 0 700 50 (+ 2 frame))] [400 (lerp1 100 800 50 frame)] midnight 12)
                (style {:stroke-linecap "round"})
                (draw)
                (when (nth-frame 1 frame)))
               
                 (->>
                  (gen-line [420 (lerp2 20 700 80 (+ 2 frame))] [420 (lerp2 120 820 50 frame)] midnight 14)
                  (style {:stroke-linecap "round"})
                  (draw)
                  (when (nth-frame 1 frame)))))
  
  
    #_(gen-group {:style {:transform-origin "center" :transform "translateY(300px)"}}
               (gen-group {} (->>
                (gen-line [80 (lerp1 0 700 50 frame)] [80 (lerp1 100 800 50 frame)] midnight 12)
                (style {:stroke-linecap "round"})
                (draw)
                (when (nth-frame 1 frame)))
               
                 #_(->>
                  (gen-line [100 (lerp1 20 700 50 frame)] [100 (lerp1 120 820 50 frame)] midnight 12)
                  (style {:stroke-linecap "round"})
                  (draw)
                  (when (nth-frame 1 frame))))
               
                 (gen-group {} (->>
                  (gen-line [600 (lerp1 0 700 50 (+ 3 frame))] [600 (lerp1 100 800 50 frame)] midnight 12)
                  (style {:stroke-linecap "round"})
                  (draw)
                  (when (nth-frame 1 frame)))
                 
                  #_(->>
                    (gen-line [620 (lerp3 20 700 30 frame)] [620 (lerp3 120 820 100 frame)] midnight 12)
                    (style {:stroke-linecap "round"})
                    (draw)
                    (when (nth-frame 1 frame))))
               
                 (gen-group {} #_(->>
                  (gen-line [400 (lerp1 0 700 50 (+ 2 frame))] [400 (lerp1 100 800 50 frame)] midnight 12)
                  (style {:stroke-linecap "round"})
                  (draw)
                  (when (nth-frame 1 frame)))
                 
                   (->>
                    (gen-line [420 (lerp2 20 700 80 (+ 2 frame))] [420 (lerp2 120 820 50 frame)] midnight 12)
                    (style {:stroke-linecap "round"})
                    (draw)
                    (when (nth-frame 1 frame)))))
  
  ;@bb6a
  
  #_(when (nth-frame 4 frame) @blobs)
    ;@blobs4
    ;@blobs3
  #_(when (nth-frame 3 slow-frame) @blobs2)

  
  
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
     (style {:transform-origin "center" :transform (str "translate(40vw, 40vh) rotate("
             (val-cyc frame [120 120 120 120 -60 -60 -60 -60 80 80 80 80 245 245 245 245])
             "deg) scale(6.2)")})
     (style {:mix-blend-mode "color-dodge"})
     (draw)
     (when (nth-frame 4 frame)))



  
  
  
@spinlm
 @spinlm2

(when (nth-frame 3 frame)(gen-line-grid midnight 4
  80 80
  {:col 20 :row 20})  )
  
  (when (nth-frame 3 (- 1 frame))(gen-line-grid br-orange 4
    80 80
    {:col 20 :row 20})  )

  
)) ; cx end
