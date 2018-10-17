(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct 
                                          b1 b2 b3 b4]]
            [ui.fills :as fills :refer
              [gray
               white
               yellow
               light-green
               green
               dark-green
               light-pink
               pink
               dark-pink 
               mauve]]
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
  [name start-str finish-str]
  (make-frames! name [0 50 100] 
    (make-body "transform" [
      (str start-str)
      (str finish-str)
      (str start-str)])))

(defn a-to-b!
  [name att start-str finish-str]
  (make-frames! name [0 100] 
    (make-body att [
      (str start-str)
      (str finish-str)])))

(back-and-forth! "scaley" "scale(1)" "scale(15)")

(a-to-b! "new-fi" "fill-opacity" "0" ".8")
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(125%)" (str "translateY("(* 0.15 @height)")"))

(make-frames!
  "woosh"
    [10, 35, 55, 75, 92]
   (make-body "transform" [
                           "translate(10vw, 10vh) rotate(2deg) scale(1)"
                           "translate(28vw, 12vh) rotate(-20deg) scale(1)"
                           "translate(36vw, 24vh) rotate(-40deg) scale(6)"
                           "translate(60vw, 12vh) rotate(60deg) scale(4)"
                           "translate(90vw, 50vh) rotate(2deg) scale(1)"
                           ]))

(make-frames!
  "woosh-2"
    [10, 35, 55, 75, 92]
   (make-body "transform" [
                           "translate(90vw, 80vh) rotate(2deg) scale(1)"
                           "translate(78vw, 82vh) rotate(-20deg) scale(1)"
                           "translate(66vw, 64vh) rotate(-40deg) scale(6)"
                           "translate(40vw, 92vh) rotate(60deg) scale(4)"
                           "translate(10vw, 50vh) rotate(2deg) scale(1)"
                           ]))

(make-frames!
  "woosh-3"
    [10, 35, 55, 75, 92]
   (make-body "transform" [
                           "translate(90vw, 80vh) rotate(2deg) scale(10)"
                           "translate(78vw, 82vh) rotate(-20deg) scale(10)"
                           "translate(66vw, 64vh) rotate(-40deg) scale(60)"
                           "translate(40vw, 92vh) rotate(60deg) scale(40)"
                           "translate(10vw, 50vh) rotate(2deg) scale(10)"
                           ]))


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

(def ffa 
  (->>
   (->>
     (gen-rect light-green 
               (* 0.1 @width) 
               (* 0.1 @height)
               (* 0.4 @width) 
               (* 0.4 @height)))
   (anim "new-fi" "2s" "1") draw atom))

(def ffb 
  (->>
   (->>
     (gen-rect mauve
               (* 0.1 @width) 
               (* 0.52 @height) 
               (* 0.6 @width) 
               (* 0.4 @height)))
   (anim "new-fi" "2s" "1") draw atom))

(def ffc 
  (->>
   (->>
     (gen-rect green
               (* 0.52 @width) 
               (* 0.1 @height) 
               (* 0.18 @width) 
               (* 0.4 @height)))
   (anim "new-fi" "2s" "1") draw atom))

(def ffd 
  (->>
   (->>
     (gen-rect dark-pink
               (* 0.72 @width) 
               (* 0.1 @height) 
               (* 0.18 @width) 
               (* 0.19 @height)))
   (anim "new-fi" "2s" "1") draw atom))

(def ffe 
  (->>
   (->>
     (gen-rect yellow
               (* 0.72 @width) 
               (* 0.31 @height) 
               (* 0.18 @width) 
               (* 0.19 @height)))
   (anim "new-fi" "2s" "1") draw atom))

(def fff 
  (->>
   (->>
     (gen-rect green
               (* 0.72 @width) 
               (* 0.52 @height) 
               (* 0.18 @width) 
               (* 0.4 @height)))
   (anim "new-fi" "2s" "1") draw atom))

    
(def move-me
  (->>
   (gen-circ (pattern (:id white-dots)) 100 100 100)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "10s" "infinite")
   (draw)
   (atom)))

(def move-me-2
  (->>
   (gen-circ (pattern (:id yellow-lines)) 100 100 100)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-2" "10s" "infinite")
   (draw)
   (atom)))

(def move-me-3
  (->>
   (gen-circ (pattern (str "noise-" pink)) 10 10 400)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-3" "4s" "infinite")
   (draw)
   (atom)))


(def bg (->> 
  (gen-circ (pattern (str "noise-" dark-green)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "32s" "1" {:timing "linear" :delay "7s"})
  (draw)
  (atom)))

(def bgs 
  (map-indexed 
    (fn [idx color]
          (->> 
            (gen-circ (pattern (str "noise-" color)) (* .5 @width) (* .5 @height) 1800)
            (style {:opacity .4 :transform-origin "center" :transform "scale(4)"})
            (anim "sc-rot" (str (rand 32) "s") "1" {:timing "linear" :delay (str (rand 4) "s")})
            (draw)
            (atom)))
    (take 4 (repeatedly #(nth [dark-green dark-green dark-green dark-green] (rand-int 4))))))


(def morphy
  (->>
    (gen-shape pink tri)
    (style {:opacity .4 :transform "translate(400px, 400px) scale(4)"})
    (anim "morph" "10s" "infinite")
    (draw)
    (atom)))

(def morphy-2
  (->>
    (gen-shape (pattern (:id gray-dots)) tri)
    (style {:opacity .4 :transform "translate(400px, 400px) scale(4)"})
    (anim "morph" "10s" "infinite")
    (draw)
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




 ;; ----------- COLLECTION SETUP AND CHANGE ----------------


(defonce collection (atom (list)))
;(reset! ran {})


(defn cx [frame]
  (list

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    (let [colors [ 
        ;white
        dark-green dark-green dark-green dark-green dark-green
        ] ; gray white yellow light-green green dark-green light-pink pink dark-pink  mauve
          n (count colors)]
          (->>
            (gen-rect (nth colors (mod frame n)) 0 0 "100%" "100%")
            (style {:opacity .9})
            (draw)))
    
  ;(doall (map deref bgs))
  
  ;@ffa
  #_(->>
    (gen-rect light-green 
              (* 0.1 @width) 
              (* 0.1 @height)
              (* 0.4 @width) 
              (* 0.4 @height))
    (draw)
    (when-not (nth-frame 0 frame)))
  
  ;@ffb
  #_(->>
    (gen-rect mauve
              (* 0.1 @width) 
              (* 0.52 @height) 
              (* 0.6 @width) 
              (* 0.4 @height))
    (draw)
    (when-not (nth-frame 0 frame)))
  
  ;@ffc
  #_(->>
    (gen-rect green
              (* 0.52 @width) 
              (* 0.1 @height) 
              (* 0.18 @width) 
              (* 0.4 @height))
  (draw)
  (when-not (nth-frame 0 frame)))

  ;@ffd
  #_(->>
    (gen-rect dark-pink
              (* 0.72 @width) 
              (* 0.1 @height) 
              (* 0.18 @width) 
              (* 0.19 @height))
  (draw)
  (when-not (nth-frame 0 frame)))

  ;@ffe
  #_(->>
    (gen-rect yellow
              (* 0.72 @width) 
              (* 0.31 @height) 
              (* 0.18 @width) 
              (* 0.19 @height))
  (draw)
  (when (nth-frame 1 frame)))

  ;@fff
  #_(->>
    (gen-rect green
              (* 0.72 @width) 
              (* 0.52 @height) 
              (* 0.18 @width) 
              (* 0.4 @height))
    (draw)
    (when-not (nth-frame 0 frame)))
  
  ;@bg
  ;@morphy
  ;@morphy-2
  
  ;@move-me
  ;@move-me-2
  ;@move-me-3
  
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
(def all-fills [gray white yellow light-green green dark-green light-pink pink dark-pink mauve])

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
