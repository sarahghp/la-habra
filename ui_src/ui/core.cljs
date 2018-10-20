(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct 
                                          b1 b2 b3 b4]]
            [ui.fills :as fills :refer
              [ gray
                mint
                midnight
                navy
                blue
                orange
                br-orange
                pink
                white
                yellow]]
            [ui.generators :refer 
             [draw
              freak-out
              gen-circ
              gen-group
              gen-line
              gen-poly
              gen-rect
              gen-shape
              gen-offset-lines
              gen-bg-lines
              gen-grid
              gen-line-grid
              gen-cols
              gen-rows]]
            [ui.filters :as filters :refer [turb noiz soft-noiz disappearing splotchy blur]]
            [ui.patterns :as patterns :refer
             [ gen-color-noise
               pattern
               pattern-def
               blue-dots
               blue-lines
               pink-dots
               pink-lines
               gray-dots
               gray-dots-lg
               gray-lines
               gray-patch
               mint-dots
               mint-lines
               navy-dots
               navy-lines
               orange-dots
               orange-lines
               br-orange-dots
               br-orange-lines
               yellow-dots
               yellow-lines
               white-dots
               white-dots-lg
               white-lines
               shadow
               noise]]
            [ui.animations :as animations :refer
              [ make-body
                splice-bodies
                make-frames!
                nth-frame
                even-frame
                odd-frame]]))

(enable-console-print!)

(println "Loaded.")

;; hides heads up display for performance
(defn hide-display [] (let [heads-up-display (.getElementById js/document "figwheel-heads-up-container")]
  (.setAttribute heads-up-display "style" "display: none")
))

;; ------------------------ SETTINGS  ---------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

(def settings {:width @width
               :height @height })

;; -------------------- HELPERS ---------------------------

(defn sin [x] (.sin js/Math x))

(defn style
  [changes shape]
  (update-in shape [:style] #(merge % changes)))

(defn url
  ([ fill-id ]
    (str "url(#" fill-id ")")))

(defn seconds-to-frames
  [seconds]
  (* 2 seconds))

(defonce ran (atom {}))

(defn anim-and-hold
  [name frame duration fader solid]
  (let [init-frame (@ran name)
        ran? (and init-frame (<= (+ init-frame (seconds-to-frames duration)) frame))
        ret (if ran? solid fader)]
    (when-not init-frame (swap! ran assoc name frame))
    ret))


              

;; -------------------- SHAPE ANIMATION HELPER ---------------------------

(defn anim
  ([name duration count shape] (anim name duration count {} shape))
  ([name duration count opts shape]
  (let [animations
    { :animation-name name
      :animation-fill-mode "forwards"
      :animation-duration duration
      :animation-iteration-count count
      :animation-delay (or (:delay opts) 0)
      :animation-timing-function (or (:timing opts) "ease")}]
          (update-in shape [:style] #(merge % animations)))))

;; ----------- ANIMATIONS ----------------

;; syntax reminder
; (make-frames!
;   "NAME"
;   [frames]
;   (make-body "ATTRIBUTE" [values]))

; (trans x y)
; (nth-frame num FRAME)
; (even-frame FRAME)
; (odd-frame FRAME)

; "fade-in-out" "fade-out" "wee-oo" "rot" "rev"

;; --------------- ANIMATIONS STORAGE --------------------

(defn back-and-forth!
  ([name start-str finish-str] (back-and-forth! name "transform" start-str finish-str))
  ([name att start-str finish-str]
  (make-frames! name [0 50 100] 
    (make-body "transform" [
      (str start-str)
      (str finish-str)
      (str start-str)]))))

(defn a-to-b!
  [name att start-str finish-str]
  (make-frames! name [0 100] 
    (make-body att [
      (str start-str)
      (str finish-str)])))

(defn fade-start!
  [name op-end]
  (make-frames! name [0 99 100] 
    (make-body "fill-opacity" [
      (str 0)
      (str 0)
      (str op-end)])))

(fade-start! "fi" 1)

(make-frames! "etof" [0 100] (make-body "transform" ["translateY(10px)" "translateY(1000px)"]))

(back-and-forth! "scaley" "scale(1)" "scale(15)")
(back-and-forth! "sm-scaley" "scale(1)" "scale(4)")
(back-and-forth! "big-scaley" "scale(0)" "scale(150)")

(back-and-forth! "op" "fill-opacity" "0" "1")

(a-to-b! "new-fi" "fill-opacity" "0" ".5")
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(125%)" (str "translateY("(* 0.15 @height)")"))
(a-to-b! "grow2to3" "transform" "rotate(280deg) scale(1)" "rotate(280deg) scale(1.2)")

(make-frames!
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(20vw, 20vh) rotate(2deg) scale(1.2)"
                           "translate(80vw, 400vh) rotate(-200deg) scale(2.4)"
                           "translate(60vw, 60vh) rotate(120deg) scale(3.4)"
                           "translate(80vw, 30vh) rotate(-210deg) scale(2.2)"
                           "translate(20vw, 80vh) rotate(400deg) scale(6.2)"]))

(make-frames!
  "woosh-2"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(20vw, 20vh) rotate(2deg) scale(11.2)"
                           "translate(80vw, 400vh) rotate(-200deg) scale(12.4)"
                           "translate(60vw, 60vh) rotate(120deg) scale(13.4)"
                           "translate(80vw, 30vh) rotate(-210deg) scale(12.2)"
                           "translate(20vw, 80vh) rotate(400deg) scale(16.2)"]))

(make-frames!
  "woosh-3"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 100vh) rotate(2deg) scale(11.2)"
                           "translate(60vw, 60vh) rotate(-200deg) scale(12.4)"
                           "translate(40vw, 80vh) rotate(120deg) scale(13.4)"
                           "translate(20vw, 10vh) rotate(-210deg) scale(12.2)"
                           "translate(40vw, 40vh) rotate(400deg) scale(16.2)"]))

(make-frames!
 "dashy"
 [100]
 (make-body "stroke-dashoffset" [0]))

(make-frames!
 "morph"
  [0 15 30 45 60 75 100]
 (make-body "d" [
  (str "path('"tri"')")
  (str "path('"square"')")
  (str "path('"pent"')")
  (str "path('"hex"')")
  (str "path('"hept"')")
  (str "path('"oct"')")
  (str "path('"tri"')")
]))   


;; --------------- ATOMS STORAGE --------------------

(def drops
  (atom  (map
     #(->>
       (gen-rect mint (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (draw))
     (range 10))))
     
(def drops-2
 (atom  (map
    #(->>
      (gen-rect white (+ 30 (* % 160)) 10 200 36)
      (style {:opacity .7})
      (anim "etof" "1.2s" "infinite" {:delay (str (* .7 %) "s")})
      (draw))
    (range 10))))

(def drops-3
 (atom  (map
    #(->>
      (gen-rect pink (+ 30 (* % 160)) 10 200 36)
      (anim "etof" "2.4s" "infinite" {:delay (str (* .7 %) "s")})
      (draw))
    (range 10))))


(def drops-4
 (atom  (map
    #(->>
      (gen-rect (pattern (:id white-dots)) (+ 30 (* % 50)) 10 36 36)
      (anim "etof" "2.4s" "infinite" {:delay (str (* .7 %) "s")})
      (draw))
    (range 30))))

(def drops-5
 (atom  (map
    #(->>
      (gen-rect (pattern (:id yellow-dots)) (+ 30 (* % 58)) 10 36 36)
      (anim "etof" "1.2s" "infinite" {:delay (str (* .7 %) "s")})
      (draw))
    (range 30))))

(def bloops
  (->>
    (gen-circ white 0 100 40)
    (style {:opacity .7})
    (anim "bloop-x" "1s" "infinite" {:timing "ease-out"})
    (draw)
    (atom)))
    
(def move-me
  (->>
   (gen-shape mint hept)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "10s" "infinite")
   (draw)
   (atom)))

(def move-me-2
  (->>
   (gen-shape (pattern (:id yellow-dots)) oct)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-2" "10s" "infinite")
   (draw)
   (atom)))

(def move-me-3
  (->>
   (gen-shape (pattern (:id pink-lines)) oct)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-3" "6s" "infinite")
   (draw)
   (atom)))

(def bg (->> 
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "32s" "1" {:timing "linear" :delay "7s"})
  (draw)
  (atom)))

(def tri-dash
  (->>
    (gen-shape "hsla(360, 10%, 10%, 0)" oct)
    (style {:transform-origin "center" 
            :transform (str "translate(" 40 "vw," 40 "vh)"
                            "scale(6)")})
    (style {:stroke pink 
            :stroke-width 10 
            :stroke-dasharray 60 
            :stroke-dashoffset 1000
            :stroke-linecap :round
            :stroke-linejoin :round})
    (anim "morph" "15s" "infinite")
    (draw)
    (atom)))

(def tri-dash-2
  (->>
    (gen-shape "hsla(360, 10%, 10%, 0)" oct)
    (style {:transform-origin "center" 
            :opacity .4
            :transform (str "translate(" 42 "vw," 42 "vh)"
                            "scale(6)")})
    (style {:stroke pink 
            :stroke-width 20 
            :stroke-dasharray 60 
            :stroke-dashoffset 1000
            :stroke-linecap :round
            :stroke-linejoin :round})
    (anim "morph" "15s" "infinite")
    (draw)
    (atom)))


(def at (atom 
           (gen-group {:style {:animation "big-scaley 12s infinite" :transform-origin "center"}}
                      (->>
                       (gen-shape (pattern (:id mint-dots)) oct)
                        (style {:opacity .5 :transform "translate(40vw, 40vh)"})
                        (draw)))))

(def at2 (atom 
           (gen-group {:style {:animation "scaley 6s infinite" :transform-origin "center"}}
                      (->>
                       (gen-shape (pattern (:id yellow-lines)) oct)
                        (style {:opacity .5 :transform "translate(40vw, 20vh)"})
                        (draw)))))

(def at3 (atom 
           (gen-group {:style {:animation "scaley 3s infinite" :transform-origin "center"}}
                      (->>
                       (gen-shape (pattern (:id gray-dots-lg)) oct)
                        (style {:opacity .5 :transform "translate(40vw, 60vh)"})
                        (draw)))))

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
    (take 10 (repeatedly #(nth [orange pink white yellow] (rand-int 3))))))



 ;; ----------- COLLECTION SETUP AND CHANGE ----------------


(defonce collection (atom (list)))
;(reset! ran {})


(defn cx [frame]
  (list

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    (let [colors [ 
        ;navy navy navy navy navy
        midnight    
        ] ; orange navy mint pink gray white
          n (count colors)]
          (->>
            (gen-rect (nth colors (mod frame n)) 0 0 "100%" "100%")
            (style {:opacity .9})
            (draw)))
  
            
  (when (nth-frame 1 frame)
    (freak-out @width
               @height
               2
               500
               white))
  ;@at3
  ;@at
  ;@at2
  ;@tri-dash
  ;@tri-dash-2
  
#_(->>
    (gen-shape (pattern (:id yellow-lines)) oct)
      (style {:transform "translate(20vw, 20vh) scale(1.8)"})
      (draw)
      (when-not (nth-frame 4 frame)))
#_(->>
  (gen-rect pink (* 0.1 @width) (* 0.53 @height) (* 0.3 @width) (* 0.1 @height))
  (draw)
  (when (nth-frame 1 frame)))
  
#_(->>
  (gen-circ mint (* 0.16 @width) (* 0.8 @height) 100)
  (draw)
  (when (nth-frame 9 frame)))
  
#_(->>
  (gen-circ mint (* 0.36 @width) (* 0.8 @height) 100)
  (draw)
  (when (nth-frame 12 frame)))
  

#_(->>
  (gen-circ mint (* 0.76 @width) (* 0.15 @height) 100)
  (draw)
  (when (nth-frame 3 frame)))
  
#_(->>
  (gen-rect pink (* 0.6 @width) (* 0.35 @height) (* 0.3 @width) (* 0.1 @height))
  (draw)
  (when (nth-frame 4 frame)))
  
#_(->>
  (gen-shape (pattern (:id white-dots)) oct)
    (style {:opacity .7 :transform "translate(70vw, 60vh) scale(2)"})
    (draw)
    (when-not (nth-frame 4 frame)))
  
#_(->>
  (gen-circ mint (* 0.4 @width) (* 0.5 @height) 240)
  (draw)
  (when (nth-frame 1 frame)))
  
  #_(->>
    (gen-circ white (* 0.4 @width) (* 0.5 @height) 240 (url "grad-mask"))
    (draw)
    (when (nth-frame 1 frame)))

  ; (map deref levels)
  ; 
  ;@drops
  ;@drops-2
  ;@drops-3
  ;@drops-4
  ;@drops-5
  
  #_(when (nth-frame 2 frame)
    (freak-out 0 @width
               (* .5 @height) @height
               10
               100
               white))
  
    #_(when-not (nth-frame 2 frame)
      (freak-out 0 @width
                 0 (* .5 @height)
                 10
                 100
                 pink))
  
  ;@move-me
  ;@move-me-2
  ;@move-me-3
  
    #_(when (nth-frame 1 frame)
      (freak-out 
                 (* .5 @width) @width
                 0 @height
                 
                 20
                 100
                 orange))
  
      #_(when (nth-frame 1 frame)
        (freak-out 
                   0 (* .6 @width)
                   0 @height
                   20
                   100
                   mint))
  

    #_(when (nth-frame 1 frame)
      (freak-out @width
                 @height
                 40
                 100
                 white))
  
  (when (nth-frame 1 frame)
    (freak-out @width
               @height
               40
               100
               (pattern (:id white-dots))))
  
  (when-not (nth-frame 0 frame) (gen-line-grid navy 4 
    100 100 
    {:col 20 :row 20}))
  
  
  )) ; cx end
  
  
;; ----------- LOOP TIMERS ------------------------------

(defonce frame (atom 0))

(defonce start-cx-timer
  (js/setInterval
    #(reset! collection (cx @frame)) 50))

(defonce start-frame-timer
  (js/setInterval
    #(swap! frame inc) 500))
    

;; ----------- DEFS AND DRAW ------------------------------

(def gradients [[:linearGradient { :id "grad" :key (random-uuid)}
                 [:stop { :offset "0" :stop-color "white" :stop-opacity "0" }]
                 [:stop { :offset "1" :stop-color "white" :stop-opacity "1" }]]])

(def masks [[:mask { :id "poly-mask" :key (random-uuid)}
              [:path {:d hept :fill "#fff" :style { :transform-origin "center" :animation "woosh-6 20s 2"}}]]
            [:mask { :id "grad-mask" :key (random-uuid)}
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" }]]
            [:mask {:id "cutout" :key (random-uuid)}
             (->>
               (gen-rect white 10 12 (* 0.94 @width) (* 0.88 @height))
               (draw))
             (->>
               (gen-circ "#000" (* 0.7 @width) (* 0.7 @height) 100)
                (draw))]
            ])
  

(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [gray mint navy blue orange br-orange pink white yellow])

(defn drawing []
  [:svg { :width (:width settings) :height (:height settings) }
     ;; filters
    (map #(:def %) all-filters)
    ;; masks and patterns
    [:defs 
     noise
     (map identity gradients)
     (map identity masks)
     (map gen-color-noise all-fills)
     (map pattern-def [ blue-dots
                        blue-lines
                        pink-dots
                        pink-lines
                        gray-dots
                        gray-dots-lg
                        gray-lines
                        gray-patch
                        mint-dots
                        mint-lines
                        navy-dots
                        navy-lines
                        orange-dots
                        orange-lines
                        br-orange-dots
                        br-orange-lines
                        yellow-dots
                        yellow-lines
                        white-dots
                        white-dots-lg
                        white-lines
                        shadow ])]

    ;; then here dereference a state full of polys
    @collection 
    ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))

;(hide-display)
