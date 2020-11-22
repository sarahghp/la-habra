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

      (anim "woosh" "4s" "infinite" {:delay ".4s"})
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
             midnight midnight midnight midnight midnight midnight
             ;mint mint mint mint
            pink pink pink pink pink pink
            ;br-orange br-orange br-orange br-orange
            yellow yellow yellow yellow yellow yellow
            ;white white white white
            ;blue blue blue 
             ;
             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:opacity .95})
        (draw)))
        
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
            
            
            ;@spinlm4
            ;@spinlm2
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
      

        #_(->>
         (gen-circ orange (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
         (style {:transform "rotate(135deg)"})
         (draw)
         (when-not (nth-frame 4 frame)))


#_(->>
 (gen-circ pink (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
 (style {:transform "rotate(20deg)"})
 (draw)
 (when-not (nth-frame 6 frame)))
 
 #_(when (nth-frame 6 frame)
   (freak-out @width
              @height
              40
              300
              midnight))

#_(->>
 (gen-circ navy (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
 (style {:transform "rotate(20deg)"})
 (draw)
 (when (nth-frame 4 frame)))

  #_(gen-group {:mask (url "grad-mask")} (->>
   (gen-shape (pattern (:id navy-lines)) tri)
   (style {:transform "translate(40vw, 40vh) scale(4) rotate(180deg)"})
   (draw)
   (when-not (nth-frame 1 frame))))
   
     #_(gen-group {:mask (url "grad-mask")} (->>
      (gen-shape (pattern (:id pink-lines)) tri)
      (style {:transform "translate(40vw, 40vh) scale(4) rotate(180deg)"})
      (draw)
      (when (nth-frame 1 frame))))
      
  #_(doall (map deref worms))
  
  (gen-group {:style {:mix-blend-mode "color-dodge"}}
    (when (nth-frame 4 frame)(gen-line-grid white 2
      80 80
      {:col 20 :row 20})))
  
  (->>
   (gen-rect charcoal 0 "20vh" "100%" "100%")
   (draw)
   (when (nth-frame 4 slow-frame)))
   
   
     (->>
      (gen-rect mint "70vh" 0 "100%" "100%")
      (style {:mix-blend-mode "color-dodge"})
      (draw)
      (when (nth-frame 6 slow-frame)))
   

   (->>
    (gen-rect (pattern (:id white-lines)) "70vw" "10vh" 100 400)
    (draw)
    (when (nth-frame 3 slow-frame)))
    
   (->>
    (gen-rect charcoal "70vw" "30vh" "31vw" 100)
    (draw)
    (when (nth-frame 4 slow-frame)))
    
   (->>
    (gen-rect mint "70vw" "30vh" "31vw" 100)
    (style {:transform "translateY(4vw)"})
    (draw)
    (when (nth-frame 6 slow-frame)))
  
     (->>
      (gen-rect (pattern (:id white-lines)) "70vw" "10vh" 100 400)
      (draw)
      (when (nth-frame 3 slow-frame)))
      
      

    (->>
     (gen-rect charcoal "22vw" "70vh" "31vw" 100)
     (style {:transform "rotate(90deg)"})
     (draw)
     (when (nth-frame 6 slow-frame)))
     
    (->>
     (gen-rect pink "22vw" "70vh" "31vw" 100)
     (style {:transform "rotate(90deg) translateY(4vw)"})
     (draw)
     (when (nth-frame 2 slow-frame)))
       

   (->>
    (gen-rect (pattern (:id white-dots)) "20vw" "8vh" 300 100)
    (draw)
    (when (nth-frame 2 slow-frame)))
    
     (->>
      (gen-rect (pattern (:id white-lines)) "16vw" "40vh" 300 100)
      (draw)
      (when (nth-frame 3 slow-frame)))
      
   (->>
    (gen-rect (pattern (:id white-dots)) "24vw" "80vh" 300 100)
    (draw)
    (when (nth-frame 4 slow-frame)))
    
    

      
      
  #_(gen-group {:style {:mix-blend-mode "color-dodge"}}
    (when (nth-frame 6 frame)(gen-line-grid white 2
      80 80
      {:col 20 :row 20})))
   
   ;@bb6a
   
   
   





  
)) ; cx end
