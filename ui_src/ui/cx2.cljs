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
      (gen-group { :style {
                          :transform-origin "center" 
                          :transform (str "translate(" (+ (rand-int 20) (rand-int 20)) 
                                          "vw, "(+ (rand-int 20) (rand-int 20)) "vh) scale(" scale ")")}})
      (atom)))
   (take 10
         (repeatedly #(rand-nth [[midnight 4] [navy 4] [navy 3] [pink 4.4] [mint 2.4]])))))


(def drops
  (atom  (map
     #(->>
       (gen-rect midnight (+ 30 (* % 160)) 10 36 36)
       (anim "etof" "2.2s" "infinite" {:delay (str (* .5 %) "s")})
       (style {:mix-blend-mode "color-dodge"})
       (draw))
     (range 30))))


(def drops2
  (atom  (map
     #(->>
       (gen-rect pink (+ 30 (* % 160)) 10 36 36)
       (anim "slide-up" "2.2s" "infinite" {:delay (str (* .5 %) "s")})
       #_(style {:mix-blend-mode "overlay"})
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
    (gen-shape white oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "color-dodge"} )

      (anim "woosh" "3s" "infinite")
    (draw)
    (atom)))


(def bb6a
  (->>
    (gen-shape mint oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "color-dodge"} )

      (anim "woosh-2" "1s" "infinite" {:delay ".4s"})
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
          (gen-rect (pattern (str "noise-" yellow)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" "4s" "infinite")
          (draw)
          (atom)))

(def rot-me
        (->>
          (gen-rect (pattern (str "noise-" blue)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "rot" ".5s" "infinite")
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
         (repeatedly #(rand-nth [[4 gray-dots-lg] [6 pink-lines] [2 mint-dots]])))))



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


(defonce a
  (scatter 100 (->>
   (gen-circ pink 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))
   
   (defonce s
     (scatter 30 (->>
      (gen-circ white 10 10 60)
      (style {:mix-blend-mode "color-dodge"})
      (draw))))

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

(def aaa (atom (gen-group {:style {:animation "ascend 3s infinite"}} @a)))

(def sss (atom (gen-group {:style {:animation "ascend 3s infinite"}} @s)))

 ;; ----------- COLLECTION SETUP AND CHANGE ----------------


(def babrect1 
  (->>
   (gen-rect navy 0 0 @width (* .05 @height))
   (style {:mix-blend-mode "luminosity"})
   (anim "small-scale" "6.4s" "infinite")
   (draw)
   (atom)))

(def babrect2 
  (->>
   (gen-rect navy 0 (* .9 @height) @width (* 1 @height))
   (style {:mix-blend-mode "luminosity"})
   (anim "small-scale" "6.4s" "infinite" {:delay "3.2s"})
   (draw)
   (atom)))
   
   
   (def babrect3 
     (->>
      (gen-rect navy 0 (* 0 @height) @width (* 1 @height))
      (style {:mix-blend-mode "luminosity"})
      ;(anim "small-scale" "1.4s" "infinite" {:delay "1.2s"})
      (draw)
      (atom)))

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
          (map #(gen-group {:mask (url "bitey") :style {:transform-origin "center" :animation "rot 10s 1" }} %)))))

(def rr2 (atom
         (->>
          (gen-grid
            5 5
            {:col 100 :row 150}
            (->>
             (gen-shape yellow oct)))
              (map #(style {:opacity 1 :mix-blend-mode "difference"} %))

            (map #(anim "lump-morph" "5s" "infinite" %))
            (map draw)
                  (map #(gen-group {:style {:transform-origin "center" :transform "scale(3)"}} %))
          (map #(gen-group {:style {:transform-origin "center" :transform "translate(-300px, 100px)"}} %))
            (map #(gen-group { :style {:transform-origin "center"  :animation "rot 5s infinite" }} %)))))
            
            (defonce streaks
              (scatter 40 
                       (->>
                        (gen-line [10 10] [200 100] midnight 10)
                        (draw))))
                        
                    (defonce streaks2
                      (scatter 100 
                               (->>
                                (gen-line [10 10] [200 100] white 10)
                                (draw))))


(def lerp1 (lerp))


(def open 
  (atom  (gen-group {:style 
      {:transform-origin "center" :animation "scaley 6s infinite"}}
      (->>
      (gen-circ pink (* 0.5 @width) (* 0.5 @height) 20 (url "grad-mask"))
      (style {:transform "rotate(135deg)"})
      (draw)))))

(defn cx2 [frame fast-frame slow-frame]
  (list
    
    #_(doall (map deref all-the-moves))
    
    
    ;@rr
    ;(doall (map deref levels))
      
   ; grid (80/20), b, c, d
    
   ;(when (nth-frame 9 slow-frame) @a)
   ;(when-not (nth-frame 3 slow-frame) @aaa)

   ;(when (nth-frame 5 frame) @c)
   
   
     #_(->>
      (gen-rect white "6vw" "6vw" (* 0.4 @width) (* 0.4 @height))
      (draw)
      (when (nth-frame 4 frame)))
     
       #_(->>
        (gen-rect blue "6vw" "6vw" (* 0.4 @width) (* 0.4 @height))
       (style {:transform "translate(50vw, 50vh)"})
        (draw)
        (when (nth-frame 3 frame)))
     
         #_(->>
          (gen-rect br-orange "6vw" "6vw" (* 0.4 @width) (* 0.4 @height))
         (style {:transform "translate(0vw, 50vh)"})
          (draw)
          (when (nth-frame 1 frame)))
          
                #_(->>
                 (gen-rect yellow "6vw" "6vw" (* 0.4 @width) (* 0.4 @height))
                (style {:transform "translate(50vw, 0vh)"})
                 (draw)
                 (when (nth-frame 1 frame)))
   
   ;(when (nth-frame 4 slow-frame) @sss)
   
   ;(when (nth-frame 6 frame) @d)
   
   #_(when (nth-frame 2 frame)(gen-line-grid mint 2
     80 80
     {:col 20 :row 20}))

    ;@bb6
    ;@bb6s
   
   ;@scale-me
   ;@rot-me

   ;@drops
   ;@drops2
   
   ;@babrect1
   ;@babrect2

    

    #_(when (nth-frame 2 frame)
      (freak-out @width
                 @height
                 40
                 100
                 orange))


  #_(when (nth-frame 1 frame)
    (freak-out @width
               @height
               20
               200
               white))
               
               #_(when (nth-frame 1 frame)
                 (freak-out @width
                            @height
                            30
                            300
                            white))
     

      #_(->>
      (gen-circ pink (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
      (style {:transform "rotate(135deg)"})
      (draw)
      (when (nth-frame 4 frame)))
      
      #_(->>
      (gen-circ yellow (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
      (style {:transform "rotate(135deg)"})
      (draw)
      (when (nth-frame 0 frame)))
      
      #_(when (nth-frame 6 frame)
        (freak-out @width
                   @height
                   30
                   100
                   mint))
                   
#_(when (nth-frame 2 (+ 1 frame))(gen-line-grid white 2
  80 80
  {:col 20 :row 20})   ) 
  
       ;@bb6a
       
       ;@babrect3
               @open
               
               ;(when-not (nth-frame 12 frame)(doall (map deref worms)))
               
                   #_(->>
                     (gen-circ pink (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask"))
                     (style {:transform "rotate(135deg)"})
                     (draw)
                     (when (nth-frame 4 frame)))
                     
                       #_(when (nth-frame 1 frame)
                         (freak-out @width
                                    @height
                                    20
                                    200
                                    charcoal))
                                    
                                    #_(when (nth-frame 1 frame)
                                      (freak-out @width
                                                 @height
                                                 30
                                                 300
                                                 white))


   ))

