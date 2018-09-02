(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct b1 b2 b3 b4 s1]]
            [ui.fills :as fills :refer
              [ gray
                mint
                navy
                blue
                orange
                br-orange
                pink
                white
                yellow]]
            [ui.generators :refer 
             [circ
              line
              polygon
              rect
              shape
              gen-circ
              gen-group
              gen-line
              gen-poly
              gen-rect
              gen-shape
              gen-offset-lines
              gen-bg-lines
              freak-out
              gen-grid]]
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
                make-frames
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

(defn style
  [changes shape]
  (update-in shape [:style] #(merge % changes)))

(defn url
  ([ fill-id ]
    (str "url(#" fill-id ")")))

(defn sin [x] (.sin js/Math x))
(defn cos [x] (.cos js/Math x))

(defn seconds-to-frames
  [seconds]
  (* 2 seconds))

(defonce ran (atom {}))

(defn fade-and-hold
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
; (make-frames
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
  [name start-str finish-str]
  (make-frames name [0 50 100] 
    (make-body "transform" [
      (str start-str)
      (str finish-str)
      (str start-str)])))

(defn a-to-b!
  [name att start-str finish-str]
  (make-frames name [0 100] 
    (make-body att [
      (str start-str)
      (str finish-str)])))

(defn fade-start!
  [name op-end]
  (make-frames name [0 99 100] 
    (make-body "fill-opacity" [
      (str 0)
      (str 0)
      (str op-end)])))

(fade-start! "fi" 1)

(make-frames "etof" [0 100] (make-body "transform" ["translateY(10px)" "translateY(1000px)"]))

(back-and-forth! "scaley" "scale(1)" "scale(8)")
(fade-start! "oio" .7)

(a-to-b! "new-fi" "fill-opacity" "0" ".5")
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(125%)" (str "translateY("(* 0.15 @height)")"))
(a-to-b! "grow2to3" "transform" "rotate(280deg) scale(1)" "rotate(280deg) scale(1.2)")

(make-frames
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80%, 50%) rotate(2deg) scale(1.2)"
                           "translate(604%, 100%) rotate(-200deg) scale(2.4)"
                           "translate(80%, 450%) rotate(120deg) scale(3.4)"
                           "translate(604%, 300%) rotate(-210deg) scale(2.2)"
                           "translate(80%, 50%) rotate(400deg) scale(6.2)"]))



(make-frames
 "woosh-2"
   [10, 35, 55, 85, 92]
  (make-body "transform" [
                          "translate(604%, 50%) rotate(2deg) scale(2.2)"
                          "translate(50%, 450%) rotate(-200deg) scale(4.4)"
                          "translate(400%, 450%) rotate(120deg) scale(8.4)"
                          "translate(604%, 300%) rotate(-210deg) scale(12.2)"
                          "translate(80%, 50%) rotate(400deg) scale(4.2)"]))

(make-frames
 "dashy"
 [100]
 (make-body "stroke-dashoffset" [0]))

    

;; --------------- ATOMS STORAGE --------------------

(def drops
  (atom  (map
     #(->>
       (gen-rect navy (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (rect))
     (range 10))))
     
(def drops-2
 (atom  (map
    #(->>
      (gen-rect white (+ 30 (* % 160)) 10 200 36)
      (anim "etof" "1.2s" "infinite" {:delay (str (* .7 %) "s")})
      (rect))
    (range 10))))

(def bloops
  (->>
    (gen-circ white 0 100 40)
    (style {:opacity .7})
    (anim "bloop-x" "1s" "infinite" {:timing "ease-out"})
    (circ)
    (atom)))
                
    
(def move-me
  (->>
   (gen-shape (pattern (:id blue-lines)) hept)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "10s" "infinite")
   (shape)
   (atom)))


 (def move-me-2
   (->>
    (gen-shape (pattern (:id blue-dots)) oct)
    (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
    (anim "woosh-2" "10s" "infinite")
    (shape)
    (atom)))


(def move-me-3
  (->>
   (gen-shape navy hept)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "10s" "infinite" {:delay "1s"})
   (shape)
   (atom)))

(def bg (->> 
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "32s" "1" {:timing "linear" :delay "7s"})
  (circ)
  (atom)))

(def fi-1
  (->>
    (gen-rect white 0 0 "100%" "100%")
    (style {:opacity .2})
    (anim "fade-in-out" "6s" "infinite")
    (rect)
    (atom)))

(def fi-2
  (->>
    (gen-rect orange 0 0 "100%" "100%")
    (style {:opacity .8})
    (anim "fade-in-out" "6s" "infinite" {:delay "3s"})
    (rect)
    (atom)))

(def fi-3
  (->>
    (gen-rect navy 0 0 "100%" "100%")
    (style {:opacity .8})
    (anim "fade-in-out" "36s" "infinite" {:delay "2s" :timing "linear"})
    (rect)
    (atom)))

(def fi-4
  (->>
    (gen-rect mint 0 0 "100%" "100%")
    (style {:opacity .7})
    (anim "fade-in-out" "36s" "infinite" {:timing "linear"})
    (rect)
    (atom)))


(def throb
  (->>
    (gen-circ white (* 0.5 @width) (* 0.3 @height) 100)
    (style {:opacity .4 :transform-origin "center"})
    (anim "scaley" "5s" "infinite")
    (circ)
    (atom)))

(def throb-2
  (->>
    (gen-circ (pattern (:id br-orange-lines)) (* 0.5 @width) (* 0.3 @height) 100)
    (style {:opacity .7 :transform-origin "center"})
    (anim "scaley" "3s" "infinite" {:delay ".1s"})
    (circ)
    (atom)))

    
(def throb-3
  (->>
    (gen-circ (pattern (:id pink-dots)) (* 0.5 @width) (* 0.3 @height) 100)
    (style {:opacity .7 :transform-origin "center"})
    (anim "scaley" "5s" "infinite" {:delay ".3s"})
    (circ)
    (atom)))


(def throb-4
  (->>
    (gen-circ white (* 0.5 @width) (* 0.3 @height) 100)
    (style {:opacity .4 :transform-origin "center"})
    (anim "scaley" "5s" "infinite")
    (circ)
    (atom)))

(def throb-5
  (->>
    (gen-circ (pattern (:id br-orange-lines)) (* 0.5 @width) (* 0.5 @height) 100)
    (style {:opacity .7 :transform-origin "center"})
    (anim "scaley" "5s" "infinite" {:delay "1s"})
    (circ)
    (atom)))

    
(def throb-6
  (->>
    (gen-circ (pattern (:id pink-dots)) (* 0.5 @width) (* 0.8 @height) 100)
    (style {:opacity .7 :transform-origin "center"})
    (anim "scaley" "3s" "infinite" {:delay "3s"})
    (circ)
    (atom)))

(def go-dash 
    (->>
      (gen-shape "hsla(100, 0%, 0%, 0)" s1)
        (style {:transform "translate(100px, 100px) scale(.5)"})
        (style {:stroke "#000" :stroke-width 10 :stroke-dasharray 4500 :stroke-dashoffset 4500})
        (anim "dashy" "5s" "infinite" {:timing "linear"})
        (shape)
        (atom)))



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
            (rect)
            (atom)))
    (take 10 (repeatedly #(nth [orange pink white yellow] (rand-int 6))))))


(defonce splotch
  (->>
    (gen-circ pink (* 0.5 @width) (* 0.5 @height) 500)
    (style {:opacity .5 :filter (url "filter-splotchy")})
    (circ)
    (atom)))

;; ------------------- DRAWING HELPERS ------------------------

;; use with (doall (map fn range))
(defn thin
  [color frame flicker? n]
  (let [op (if (and (nth-frame 4 frame) flicker?) (rand) 1)]
    (->>
     (gen-rect color (* 0.15 @width) (* 0.15 @height) (* 0.7 @width) 3)
     (style {:transform (str "translateY(" (* n 10) "px)") :opacity op})
     (rect))))

(defn flicker-test [n frame]
  (or (and (= n 10) (nth-frame 12 frame))
      (and (= n 12) (nth-frame 8 frame))
      (= n 44) (= n 45)
      (and (= n 46) (nth-frame 8 frame))))


 ;; ----------- COLLECTION SETUP AND CHANGE ----------------


(defonce collection (atom (list)))
;(reset! ran {})


(defn cx [frame]
  (list

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    (let [colors [ 
        ;mint mint mint
        navy navy navy navy navy
        ;white white
        ] ; orange navy mint pink gray white
          n (count colors)]
          (->>
            (gen-rect (nth colors (mod frame n)) 0 0 "100%" "100%")
            (style {:opacity .7})
            (rect)))
  
  ;@fi-1
  ;@fi-2
  ;@fi-3
  ;@fi-4

  #_(->>
    (gen-grid
      40 1
      {:col 40 :row 10}
      (gen-rect white 5 0 2 @height)) 
     (map #(style {:opacity .5} %)) 
     (map rect) 
     (when (nth-frame 6 frame)))

 #_(->>
   (gen-grid
     1 80
     {:col 40 :row 20}
     (gen-rect white 5 0 @width 2)) 
    (map #(style {:opacity .5} %)) 
    (map rect) 
    (when (nth-frame 6 frame)))
  
  
  
    #_(doall (map deref levels))
  
    
  ; @throb-2
  ; (when (or (nth-frame 12 (+ 3 frame) (nth-frame 12 (+ 4 frame))))
  ;   (freak-out @width
  ;              @height
  ;              4
  ;              1000
  ;              yellow))
  
  ;@move-me
  ;@move-me-2
  
  ;@throb
  ;@throb-2
  ;@throb-3

  #_(->>
    (gen-shape mint b2)
      (style {:transform "translate(100px, 100px) scale(2)"})
      (shape)
      (when (nth-frame 1 frame)))
  
  ;@go-dash
  
    ;@bg
  
  
    (when (nth-frame 1 frame)
      (freak-out @width
                 @height
                 4
                 200
                 yellow))
  
  (when (nth-frame 1 frame)
    (freak-out @width
               @height
               4
               100
               gray))
  
  (when (nth-frame 1 frame)
    (freak-out @width
               @height
               4
               200
               mint))
  
  
    @throb-4
    @throb-5
    @throb-6

  
  
  #_(let [patterns [ 
      yellow-lines yellow-lines yellow-lines yellow-lines
      yellow-dots yellow-dots yellow-dots yellow-dots      
      ] ; orange navy mint pink gray white
        n (count patterns)]
          (->>
            (gen-shape (pattern (:id (nth patterns (mod frame n)))) s1)
              (style {:transform "translate(100px, 100px) scale(.5)"})
              (style {:stroke "#000" :stroke-width 10 :stroke-dasharray 100})
              (shape)
              (when (nth-frame 1 frame))))
    
    
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
              [:path {:d hept :fill "#fff" :style { :transform-origin "center" :animation "scaley 5s infinite"}}]]
            [:mask { :id "grad-mask" :key (random-uuid)}
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" }]]
              [:mask { :id "poly-mask-2" :key (random-uuid)}
                            [:path {:d hept :fill "#fff" :style { :transform-origin "center" :transform "translateX(500px)" :animation "woosh-2 5s infinite"}}]]
            [:mask {:id "cutout" :key (random-uuid)}
             (->>
               (gen-rect white 10 12 (* 0.94 @width) (* 0.88 @height))
               (rect))
             (->>
               (gen-circ "#000" (* 0.7 @width) (* 0.7 @height) 100)
               (circ))]
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
