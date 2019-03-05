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
                yellow
                ps-light-green
                ps-dark-green]]
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

(defonce frame (atom 0))

;; -------------------- HELPERS ---------------------------

(defn sin [x] (.sin js/Math x))

(defn style
  [changes shape]
  (update-in shape [:style] #(merge % changes)))

(defn url
  ([ fill-id ]
    (str "url(#" fill-id ")")))

(defn val-cyc
  [frame vals]
  (let [n (count vals)]
    (nth vals (mod frame n))))

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
(back-and-forth! "scaley-huge" "scale(20)" "scale(50)")


(a-to-b! "new-fi" "fill-opacity" "0" ".5")
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(125%)" (str "translateY("(* 0.15 @height)")"))
(a-to-b! "grow2to3" "transform" "rotate(280deg) scale(1)" "rotate(280deg) scale(1.2)")

(make-frames!
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 50vh) rotate(2deg) scale(1.2)"
                           "translate(60vw, 60vh) rotate(-200deg) scale(2.4)"
                           "translate(40vw, 40vh) rotate(120deg) scale(3.4)"
                           "translate(20vw, 30vh) rotate(-210deg) scale(2.2)"
                           "translate(60vw, 80vh) rotate(400deg) scale(6.2)"]))

(make-frames!
  "woosh-2"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 50vh) rotate(2deg) scale(11.2)"
                           "translate(60vw, 60vh) rotate(-200deg) scale(12.4)"
                           "translate(40vw, 40vh) rotate(120deg) scale(13.4)"
                           "translate(20vw, 30vh) rotate(-210deg) scale(12.2)"
                           "translate(60vw, 80vh) rotate(400deg) scale(6.2)"]))


(make-frames!
  "woosh-3"
    [10, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 10vh) rotate(2deg) scale(2.2)"
                           "translate(40vw, 40vh) rotate(120deg) scale(8.4)"
                           "translate(50vw, 30vh) rotate(0deg) scale(12.2)"
                           "translate(60vw, 80vh) rotate(400deg) scale(4.2)"]))
(make-frames!
  "woosh-4"
    [10, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 10vh) rotate(2deg) scale(2.2)"
                           "translate(40vw, 40vh) rotate(120deg) scale(4.4)"
                           "translate(50vw, 30vh) rotate(0deg) scale(6.2)"
                           "translate(60vw, 80vh) rotate(400deg) scale(4.2)"]))

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

(make-frames!
 "fio"
 [0 5 10 90 100]
 (make-body "opacity"
             [0 0 .2 .6 .4 .6]))


;; --------------- ATOMS STORAGE --------------------

(def drops
  (atom  (map
     #(->>
       (gen-rect midnight (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (draw))
     (range 10))))

(def drops-2
 (atom  (map
    #(->>
      (gen-rect white (+ 30 (* % 160)) 10 200 36)
      (anim "etof" "1.2s" "infinite" {:delay (str (* .7 %) "s")})
      (draw))
    (range 10))))

(def bloops
  (->>
    (gen-circ white 0 100 40)
    (style {:opacity .7})
    (anim "bloop-x" "1s" "infinite" {:timing "ease-out"})
    (draw)
    (atom)))

(def move-me
  (->>
   (gen-shape (pattern (:id white-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "10s" "infinite")
   (draw)
   (atom)))

(def move-me-2
  (->>
   (gen-shape (pattern (:id white-lines)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-2" "10s" "infinite")
   (draw)
   (atom)))

(def move-me-3
  (->>
   (gen-shape (pattern (:id navy-lines)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "6s" "infinite")
   (draw)
   (atom)))

(def move-me-3a
  (->>
   (gen-shape (pattern (:id mint-lines)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "4s" "infinite" {:delay ".4s"})
   (draw)
   (atom)))

(def move-me-4
  (->>
   (gen-shape (pattern (:id navy-lines)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-2" "6s" "infinite")
   (draw)
   (atom)))

(def move-me-5
  (->>
   (gen-shape navy hept)
   (style {:mix-blend-mode "multiply"})
   (style {:opacity .7 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-4" "3s" "infinite")
   (draw)
   (atom)))


(def move-me-6
  (->>
   (gen-shape mint hept)
   (style {:mix-blend-mode "lighten"})
   (style {:opacity .7 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-4" "4s" "infinite" {:delay ".2s"})
   (draw)
   (atom)))

(def bg (->>
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "3s" "infinite" {:timing "linear"})
  (draw)
  (atom)))

(def tri-dash
  (->>
    (gen-shape "hsla(360, 10%, 10%, 0)" oct)
    (style {:transform-origin "center"
            :transform (str "translate(" 40 "vw," 40 "vh)"
                            "scale(2)")})
    (style {:stroke pink
            :stroke-width 10
            :stroke-dasharray 20
            :stroke-dashoffset 1000
            :stroke-linecap :round
            :stroke-linejoin :round})
    (anim "dashy" "4s" "infinite")
    (draw)
    (atom)))

(def scale-me
        (->>
          (gen-rect (pattern (str "noise-" br-orange)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" "8s" "infinite")
          (draw)
          (atom)))

(def scale-me-2
        (->>
          (gen-rect (pattern (str "noise-" pink)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" "10s" "infinite" {:delay ".4s"})
          (draw)
          (atom)))


(def rot-me (->>
             (gen-rect (pattern (str "noise-" pink)) 0 0 @width @height)
             (style {:transform "scale(50) rotate(240deg)"})
             (style {:mix-blend-mode "color-dodge"} )
             (anim "rot" "6s" "infinite")
             (draw)
             (atom)))


  (def bb1
    (->>
      (gen-shape mint oct)
        (style {:transform "translate(20vw, 30vh) scale(2)"})
        (style {:mix-blend-mode "color-burn" } )
      (anim "woosh" "4s" "infinite")
      (draw)
      (atom)))

      (def bb2
        (->>
          (gen-shape mint oct)
            (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
            (style {:mix-blend-mode "luminosity" :filter (url (:id noiz))} )
            (anim "woosh" "4s" "infinite")
          (draw)
          (atom)))

      (def bb4
        (->>
          (gen-shape yellow oct)
            (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
            (style {:mix-blend-mode "luminosity" :filter (url (:id noiz))} )
            (anim "woosh" "6s" "infinite")
          (draw)
          (atom)))


          (def bb3
            (->>
              (gen-shape pink hept)
                (style {:transform "translate(30vw, 44vh) scale(2.4)"})
                (style {:mix-blend-mode "color-burn"} )
                (anim "woosh-3" "6s" "infinite")
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

(defn full-thin
  [color frame flicker? n]
  (let [op (if (and (nth-frame 4 frame) flicker?) (rand) 1)]
    (->>
     (gen-rect color 0 0 @width 3)
     (style {:transform (str "translateY(" (* n 10) "px)") :opacity op})
     (draw))))


(defn flicker-test [n frame]
  (or (and (= n 10) (nth-frame 12 frame))
      (and (= n 12) (nth-frame 8 frame))
      (= n 44) (= n 45)
      (and (= n 46) (nth-frame 8 frame))))

(def slide-lines
 (->>
  (gen-group {:style {:filter (url (:id noiz))
                      :transform "translateY(0%)"
                      :animation "slide-up 12s 1 ease-in"}}
             (doall
               (map #(thin navy 1 true %)
                    (range 80))))
   (atom)))

(defn hold-lines [frame]
  (gen-group {:style {:transform "translateY(0%)" :filter (url (:id noiz)) }}
             (doall
               (map #(thin navy frame (flicker-test % frame) %)
                    (range 80)))))


;(doall (map deref levels))
(def levels
  (map-indexed
    (fn [idx color]
          (->>
            (gen-rect color -100 -100 "120%" "120%" (url "cutout"))
            (style {:opacity .7
                    :transform-origin "center"
                    :transform (str
                                "translate(" (- (rand-int 200) 100) "px, " (- (rand-int 300) 100) "px)"
                                "scale(" (nth [ 1 2 1 2 2 ] (mod idx 5)) ")"
                                "rotate(" (- 360 (rand-int 720)) "deg)")})
            (anim "fade-in-out" "16s" "infinite" {:delay (str (* .1 idx) "s")})
            (draw)
            (gen-group {:style {:animation (str "fio " (* 20 (+ 2 idx)) "s infinite")}})
            (gen-group {:style {:animation (str "woosh " (* 7 (+ 2 idx)) "s infinite")}})
            (atom)))
    (take 10 (repeatedly #(nth [(pattern (:id br-orange-dots))
                                (pattern (:id br-orange-dots))
                                (pattern (:id yellow-dots))
                                (pattern (:id yellow-dots))
                                (pattern (:id yellow-dots))] (rand-int 4))))))



(def cached-levels (atom (map deref levels)))


(def pframe 0) ;   0  6 4 1
(def yframe 0) ;  0 12 3 1
(def broframe 0) ; 0  7 5 1
(def lgframe 0) ;  0  8 8 1
(def mframe 0) ;    0  4 4 1

; (def yframe 12)
; (def lgframe 8)
; (def broframe 7)
; (def mframe 4)


; (def yframe 1)
; (def lgframe 1)
; (def pframe 1)
; (def broframe 1)
; (def mframe 1)

 ;; ----------- COLLECTION SETUP AND CHANGE ----------------


(defonce collection (atom (list)))
;(reset! ran {})


(defn cx [frame]
  (list

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  (let
    [colors [
            ;navy navy navy navy navy
            ;white white white
            ;midnight midnight midnight midnight
            navy navy navy navy
            ;mint mint mint mint
            ;yellow yellow yellow white
             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100%" "100%")
        (style {:opacity .9})
        (draw)))

  ;; DAWN



  #_(when (nth-frame 12 frame)
    (gen-rows pink 1 60 40))

  #_(when (nth-frame 9 frame)
      (gen-cols pink 1 60 40))




  ;@cached-levels
  (doall (map deref levels))

  ;; SHAPE

  ;; base
  (->>
    (gen-shape navy tri)
      (style {:transform-origin "center" :transform "translate(30vw, 40vh) rotate(-20deg) scale(5.2)"})
      (style {:opacity .9})
      (style {:mix-blend-mode "screen"})
      (draw)
      (when (nth-frame 1 frame)))

  ;; lone cross
  #_(gen-group {:style {:transform "translate(10vw, 86vh) rotate(10deg)"}}
             (->>
               (gen-line [10 10] [40 40] white 5)
               (draw)
               (when (nth-frame 1 frame)))
             (->>
               (gen-line [10 40] [40 10] white 5)
               (draw)
               (when (nth-frame 1 frame))))

  ;; top a
  (gen-group {:style {:transform-origin "center"
                      :transform "translate(20vw, 18vh) rotate(-40deg) scale(1.4)"}}
               (->>
                 (gen-rect pink 5 10 20 20)
                 (draw)
                 (when (nth-frame pframe frame)))
               (->>
                 (gen-rect ps-light-green 5 40 100 40)
                 (draw)
                 (when (nth-frame lgframe frame)))
               (->>
                 (gen-rect yellow 20 80 140 10)
                 (draw)
                 (when (nth-frame yframe frame)))
             (->>
               (gen-rect br-orange -30 120 200 60)
               (style {:opacity .7})
               (draw)
               (when (nth-frame broframe frame)))
             (->>
               (gen-rect midnight 0 180 220 10)
               (style {:opacity .7})
               (draw)
               (when (nth-frame mframe frame))))

  ;; right
  (gen-group {:style {:transform "translate(74vw, 55vh) rotate(100deg) scale(1.6)"}}
               (->>
                 (gen-circ midnight 100 40 20)
                 (draw)
                 (when (nth-frame mframe frame)))

               (->>
                 (gen-rect yellow 10 140 120 10)
                 (draw)
                 (when (nth-frame yframe frame)))

               (->>
                 (gen-rect pink 70 70 50 50)
                 (style {:transform "rotate(2deg)"})
                 (draw)
                 (when (nth-frame pframe frame)))

                 (->>
                   (gen-rect ps-light-green 20 220 140 10)
                   (draw)
                   (when (nth-frame lgframe frame)))

                 (->>
                   (gen-rect ps-light-green 10 240 160 10)
                   (draw)
                   (when (nth-frame lgframe frame)))

               (->>
                 (gen-shape midnight tri)
                   (style {:transform "translate(0px, 160px) scale(.8)"})
                 (style {:opacity .9})
                   (draw)
                   (when (nth-frame mframe frame))))







   ;; left
   (gen-group {:style {:transform "translate(18vw, 72vh) rotate(15deg) scale(1.4)"}}
              (->>
                (gen-rect yellow 10 70 200 60)
                (draw)
                (when (nth-frame yframe frame)))

                (->>
                (gen-rect ps-light-green 10 10 300 60)
                (draw)
                (when (nth-frame lgframe frame))))




  ;; POOL


  #_(gen-line-grid "hsla(360, 0%, 100%, .4)" 2
    80 60
    {:col 40 :row 40})


  (->>
    (gen-circ (pattern (str "noise-" white)) (* 0.5 @width) (* 0.5 @height) 2000)
    (style {:opacity .5})
    (draw)
    (when (nth-frame 1 frame)))



  (when (nth-frame 1 frame)
    (freak-out @width
               @height
               4
               40
               white
               {:opacity .5}))


)) ; cx end


;; ----------- LOOP TIMERS ------------------------------

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
              [:path {:d b2 :fill "#fff" :style { :transform-origin "center" :animation "woosh 2s infinite"}}]]
            [:mask { :id "poly-mask-2" :key (random-uuid)}
                          [:path {:d b3 :fill "#fff" :style { :transform-origin "center" :animation "woosh-3 3s infinite"}}]]
            [:mask { :id "grad-mask" :key (random-uuid)}
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" }]]
            [:mask {:id "cutout" :key (random-uuid)}
             (->>
               ;(gen-rect white 10 12 (* 0.94 @width) (* 0.88 @height))
               (gen-shape white b2)
               (draw))
             (->>
               (gen-circ "#000" (* 0.7 @width) (* 0.7 @height) 100)
                (draw))]
            ])


(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [gray mint navy blue orange br-orange pink white yellow ps-light-green ps-dark-green])


(defn drawing []
  [:svg {
    :style  {:mix-blend-mode
             (val-cyc @frame
                      ["multiply" "multiply" "multiply"]) }
    :width  (:width settings)
    :height (:height settings)}
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
