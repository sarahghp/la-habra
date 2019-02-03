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
              gen-rows
              gen-mask]]
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
(defn cos [x] (.cos js/Math x))

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

(make-frames! "etof" [0 100] (make-body "transform" ["translateY(10px)" "translateY(1000px)"]))

(back-and-forth! "scaley" "scale(1)" "scale(15)")
(back-and-forth! "scaley-huge" "scale(20)" "scale(50)")


(a-to-b! "new-fi" "fill-opacity" "0" ".5")
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(125%)" (str "translateY("(* 0.15 @height)")"))
(a-to-b! "grow2to3" "transform" "rotate(280deg) scale(1)" "rotate(280deg) scale(1.2)")

(defn fade-start!
  [name op-end]
  (make-frames! name [0 99 100]
    (make-body "fill-opacity" [
      (str 0)
      (str 0)
      (str op-end)])))

(fade-start! "fi" 1)

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
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 10vh) rotate(2deg) scale(2.2)"
                           "translate(40vw, 40vh) rotate(220deg) scale(10.4)"
                           "translate(50vw, 30vh) rotate(0deg) scale(4.2)"
                           "translate(50vw, 30vh) rotate(-300deg) scale(2.2)"
                           "translate(60vw, 80vh) rotate(400deg) scale(1.2)"]))


 (make-frames!
   "loopy-left"
     [10, 35, 55, 85, 92]
    (make-body "transform" [
                            "translate(90vw, 10vh) rotate(2deg) scale(2.2)"
                            "translate(80vw, 30vh) rotate(220deg) scale(6.4)"
                            "translate(60vw, 40vh) rotate(0deg) scale(4.2)"
                            "translate(30vw, 80vh) rotate(-300deg) scale(2.2)"
                            "translate(10vw, 90vh) rotate(400deg) scale(3.2)"]))

(make-frames!
   "loopy-right"
     [10, 35, 55, 85, 92]
    (make-body "transform" [
                            "translate(10vw, 10vh) rotate(2deg) scale(2.2)"
                            "translate(30vw, 80vh) rotate(220deg) scale(6.4)"
                            "translate(60vw, 40vh) rotate(0deg) scale(4.2)"
                            "translate(80vw, 30vh) rotate(-300deg) scale(2.2)"
                            "translate(90vw, 90vh) rotate(400deg) scale(3.2)"]))

(make-frames!
 "dashy"
 [0 50 100]
 (make-body "stroke-dashoffset" ["60%" 0 "6s0%"]))

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
 "morph-2"
  [0 30 45 75 100]
 (make-body "d" [
  (str "path('"b1"')")
  (str "path('"b4"')")
  (str "path('"b1"')")
  (str "path('"b4"')")
  (str "path('"b1"')")
]))


(make-frames!
 "morph-3"
  [0 30 45 75 100]
 (make-body "d" [
  (str "path('"b3"')")
  (str "path('"b2"')")
  (str "path('"b3"')")
  (str "path('"b2"')")
  (str "path('"b3"')")
]))

;; --------------- ATOMS STORAGE --------------------

(def drops
  (atom  (map
     #(->>
       (gen-rect white (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (draw))
     (range 10))))

     (def drops-2
       (atom  (map
          #(->>
            (gen-rect mint (+ 30 (* % 160)) 10 200 36)
            (anim "etof" "1.2s" "infinite" {:delay (str (* .7 %) "s")})
            (draw))
          (range 10))))


(def move-me
  (->>
   (gen-shape white hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-4" "6s" "infinite")
   (draw)
   (atom)))

(def move-me-2
  (->>
   (gen-shape (pattern (:id white-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "10s" "infinite")
   (draw)
   (atom)))

(def move-me-3
 (->>
  (gen-shape (pattern (:id navy-lines)) oct)
  (style {:opacity .6 :transform-origin "center" :transform "translate(120vw, -10vh)"})
  (anim "loopy-left" "8s" "infinite")
  (draw)
  (atom)))


(def move-me-4
  (->>
   (gen-shape (pattern (:id navy-dots)) oct)
   (style {:opacity .6 :transform-origin "center" :transform "translate(-100vw, -10vh)"})
   (anim "loopy-right" "8s" "infinite")
   (draw)
   (atom)))


(def move-me-5
  (->>
   (gen-shape (pattern (:id orange-lines)) oct)
   (style {:opacity .9 :transform-origin "center" :transform "translate(-100vw, -10vh)"})
   (anim "loopy-right" "4s" "infinite")
   (draw)
   (atom)))


(def bg (->>
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "3s" "infinite" {:timing "linear"})
  (draw)
  (atom)))


(def scale-me
        (->>
          (gen-rect (pattern (str "noise-" br-orange)) 0 0 @width @height)
          (style {:transform "scale(50)"})
          (anim "scaley-huge" "8s" "infinite")
          (draw)
          (atom)))


  (def bb1
    (->>
      (gen-shape mint oct)
        (style {:transform "translate(20vw, 30vh) scale(2)"})
        (style {:mix-blend-mode "color-dodge" } )
      (anim "woosh" "2s" "infinite")
      (draw)
      (atom)))

      (def bb1a
        (->>
          (gen-shape mint oct)
            (style {:transform "translate(20vw, 30vh) scale(2)"})
            (style {:mix-blend-mode "color-burn" } )
          (anim "woosh" "2s" "infinite" {:delay ".4s"})
          (draw)
          (atom)))

(def beano
  (->>
    (gen-shape (pattern (:id white-dots)) b1)
    (style {:transform "translate(30vw, 20vh) scale(4)"})
    (anim "morph-2" "6s" "infinite")
    (draw)
    (atom)))


    (def beano-7
      (->>
        (gen-shape (pattern (:id gray-dots-lg)) b1)
        (style {:transform "translate(40vw, 40vh) scale(5)"})
        (anim "morph-2" "4s" "infinite")
        (draw)
        (atom)))

  (def beano-2
    (->>
      (gen-shape pink b1)
      (style {:transform "translate(20vw, 20vh) scale(2)"})
      (anim "morph-3" "5s" "infinite")
      (draw)
      (atom)))


      (def beano-4
        (->>
          (gen-shape br-orange b1)
          (style {:transform "translate(25vw, 30vh) scale(3)"})
          (style {:mix-blend-mode "color-burn"})
          (anim "morph-3" "4s" "infinite")
          (draw)
          (atom)))

(def beano-3
  (->>
    (gen-shape "rgba(100, 100, 100, 0)" b3)
      (style {:transform "translate(600px, 400px) scale(3)"})
      (style {:stroke mint
              :stroke-width 10
              :stroke-dasharray 40
              :stroke-dashoffset 600
              :stroke-linecap "round"})
              (anim "dashy" "10s" "infinite")
      (draw)
      (atom)))


  (def beano-5
    (->>
      (gen-shape "rgba(100, 100, 100, 0)" b3)
        (style {:transform "translate(800px, 700px) scale(4)"})
        (style {:stroke yellow
                :stroke-width 10
                :stroke-dasharray 40
                :stroke-dashoffset 600
                :stroke-linecap "round"})
                (anim "dashy" "6s" "infinite")
        (draw)
        (atom)))


        #_(def beano-6
          (->>
            (gen-shape "rgba(100, 100, 100, 0)" b3)
              (style {:transform "translate(800px, 700px) scale(4)"})
              (style {:stroke yellow
                      :stroke-width 10
                      :stroke-dasharray 40
                      :stroke-dashoffset 600
                      :stroke-linecap "round"})
                      (anim "dashy" "6s" "infinite")
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
    (take 10 (repeatedly #(nth [orange pink white yellow] (rand-int 6))))))



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
      midnight midnight
      yellow yellow
      mint mint
      pink pink
             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:opacity .9})
        (draw)))


(doall (map deref levels))


;@beano-2
;@beano
;@beano-4
@beano-7

;@move-me-3
;@move-me-4

;@beano-3
;@beano-5

(when (nth-frame 1 frame)
  (freak-out @width
             @height
             4
             1000
             white))





  #_(when (nth-frame 3 frame)
    (freak-out @width
               @height
               40
               100
               br-orange))

               #_(when (nth-frame 4 frame)
                 (freak-out @width
                            @height
                            20
                            200
                            gray))


                            #_(when (nth-frame 5 frame)
                              (freak-out @width
                                         @height
                                         40
                                         100
                                         yellow))

                                         #_(when (nth-frame 6 frame)
                                           (freak-out @width
                                                      @height
                                                      20
                                                      200
                                                      mint))

  ;(gen-bg-lines midnight (mod frame 80))

  ;@drops
  ;@drops-2


  #_(when (nth-frame 2 frame)(gen-line-grid white 4
    100 100
    {:col 20 :row 20}))

    #_(when (nth-frame 3 frame)(gen-line-grid pink 4
      100 100
      {:col 20 :row 20}))

;@bb1
;@bb1a

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


(def mask-list [
            [ "poly-mask"
              [:path {:d b2 :fill "#fff" :style { :transform-origin "center" :animation "woosh 2s infinite"}}]]
            [ "poly-mask-2"
                          [:path {:d b3 :fill "#fff" :style { :transform-origin "center" :animation "woosh-3 3s infinite"}}]]
            [ "grad-mask"
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" }]]
            [ "cutout"
             (->>
               (gen-rect white 10 12 (* 0.94 @width) (* 0.88 @height))
               (draw))
             (->>
               (gen-circ "#000" (* 0.7 @width) (* 0.7 @height) 100)
                (draw))]
              ["rect-buds"
               (->>
                 (gen-rect white 10 12 (* 0.3 @width) (* 0.5 @height))
                 (draw))
                 ]
            ])



(def masks (map (fn [[id & rest]] (apply gen-mask id rest)) mask-list))


(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [gray mint navy blue orange br-orange pink white yellow midnight])


(defn drawing []
  [:svg {
    :style  {:mix-blend-mode
             (val-cyc @frame
                      [
                      "multiply"
                      "difference"
                      "luminosity"
                       ]) }
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
