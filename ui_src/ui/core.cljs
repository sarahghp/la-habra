(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct 
                                          b1 b2 b3 b4]]
            [ui.fills :as fills :refer
              [red
               blue
               light-purple
               purple
               dark-purple
               light-gray
               gray
               dark-gray
               pink]]
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

(a-to-b! "new-fi" "fill-opacity" "0" ".5")
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(125%)" (str "translateY("(* 0.15 @height)")"))
(a-to-b! "grow2to3" "transform" "rotate(280deg) scale(1)" "rotate(280deg) scale(1.2)")
(a-to-b! "bloop-x" "transform" "translateX(-10vw)" (str "translateX(120vw)") )
(a-to-b! "bloop-y" "transform" "translateY(-10vh)" (str "translateY(120vh)") )
(a-to-b! "bloop-x-back" "transform" (str "translateX(120vw)") "translateX(-10vw)")
(a-to-b! "bloop-y-back" "transform" (str "translateY(120vh)") "translateY(-10vh)")


(make-frames!
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80%, 50%) rotate(2deg) scale(1.2)"
                           "translate(604%, 100%) rotate(-200deg) scale(2.4)"
                           "translate(80%, 450%) rotate(120deg) scale(3.4)"
                           "translate(604%, 300%) rotate(-210deg) scale(2.2)"
                           "translate(80%, 50%) rotate(400deg) scale(6.2)"]))

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
  (str "path('"tri"')")]))

(make-frames!
 "short-morph"
  [0 15 30 45 60 75 100]
 (make-body "d" [
  (str "path('"hex"')")
  (str "path('"oct"')")
  (str "path('"pent"')")
  (str "path('"hept"')")
  (str "path('"tri"')")
  (str "path('"oct"')")
  (str "path('"square"')")]))   


;; --------------- ATOMS STORAGE --------------------


(def bloops
  (->>
    (gen-circ gray 0 100 40)
    (style {:opacity .7})
    (anim "bloop-x" "20s" "infinite" {:timing "ease-out"})
    (draw)
    (atom)))
    
(def move-me
  (->>
   (gen-shape purple hept)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "10s" "infinite")
   (draw)
   (atom)))

(def morphy
  (->>
    (gen-shape blue tri)
    (style {:opacity .1 :transform "translate(400px, 400px) scale(4)"})
    (anim "morph" "100s" "infinite")
    (draw)
    (atom)))

(def move-morphy
  (->>
    (gen-shape blue tri)
    (style {:opacity .1 :transform "translateY(100px) scale(4)"})
    (anim "morph" "100s" "infinite")
    (draw)
    (gen-group {:style {:animation "bloop-x 200s infinite"}})
    (atom)))

(def move-morphy-2
  (->>
    (gen-shape red tri)
    (style {:opacity .1 :transform "translateX(300px) scale(4)"})
    (anim "morph" "100s" "infinite")
    (draw)
    (gen-group {:style {:animation "bloop-y 30s ease-in infinite"}})
    (atom)))

(def move-morphy-3
  (->>
    (gen-shape light-gray tri)
    (style {:opacity .1 :transform "translateX(60vh) scale(2)"})
    (anim "morph" "100s" "infinite")
    (draw)
    (gen-group {:style {:animation "woosh 200s ease-in 100s infinite"}})
    (atom)))

(def morphy-movers
  (map-indexed 
   (fn [idx color]
     (->>
       (gen-shape color tri)
       (style {:opacity .1 :transform (str 
                                       "translate(" 
                                       (rand-int 100) "vw, " 
                                       (rand-int 100) "vh) "
                                       "scale("(rand-int 8)")")})
       (anim "short-morph" "100s" "infinite")
       (draw)
       (gen-group {:style {:animation (str (nth ["bloop-x" "bloop-y" "bloop-x-back" "bloop-y-back"] (rand 3)) 
                                            "200s" 
                                            "ease-in" 
                                            (+ 200 (rand-int 100)) "s" 
                                            "infinite")}})
       (atom)))
   (take 6 (repeatedly #(nth [light-purple red red blue blue purple] (rand-int 6))))))


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
            (anim "fade-in-out" "60s" "infinite" {:delay (str (* .1 idx) "s")})
            (draw)
            (atom)))
    (take 10 (repeatedly #(nth [light-purple pink gray purple] (rand-int 4))))))



 ;; ----------- COLLECTION SETUP AND CHANGE ----------------


(defonce collection (atom (list)))
;(reset! ran {})


(defn cx [frame]
  (list

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    (let [colors [ 
        ;blue blue blue blue blue
        dark-gray    
        ] ;red blue light-purple purple dark-purple light-gray gray dark-gray pink
          n (count colors)]
          (->>
            (gen-rect (nth colors (mod frame n)) 0 0 "100%" "100%")
            (style {:opacity .98})
            (draw)))
  
       (doall (map
          #(->>
            (gen-circ dark-gray (rand-int @width) (rand-int @height) 2)
            (style {:transform (str "scaleX(" (mod (* frame 10) 60) " )")})
            (draw)
            (when (nth-frame 1 frame)))
          (range 10)))
  
      ;(doall (map deref levels))
  
      ;(gen-bg-lines blue (mod (* .5 frame) 80))
      ;@move-morphy
      ;@move-morphy-2
      ;@move-morphy-3
      
      ;(doall (map deref morphy-movers))
  
      #_(when (nth-frame 1 frame)
        (freak-out @width
                   @height
                   10
                   100
                   light-purple))
  
  ;@bloops
  
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
               (gen-rect gray 10 12 (* 0.94 @width) (* 0.88 @height))
               (draw))
             (->>
               (gen-circ "#000" (* 0.7 @width) (* 0.7 @height) 100)
                (draw))]
            ])
  

(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [red blue light-purple purple dark-purple light-gray gray dark-gray pink])

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
