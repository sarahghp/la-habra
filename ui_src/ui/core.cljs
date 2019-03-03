(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                          b1 b2 b3 b4]]
            [ui.fills :as fills :refer
              [ gray
                mint
                forest
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
            [ui.filters :as filters :refer [turb noiz soft-noiz disappearing splotchy blur
                                            turbulence-1 turbulence-2 turbulence-3 turbulence-4 turbulence-5]]
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

(defn frames-to-seconds
  [frames]
  (* 0.5 frames))

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

(back-and-forth! "rx" "rotateX(0deg) scale(10)" "rotateX(360deg) scale(10)")
(back-and-forth! "ry" "rotateY(0deg)" "rotateY(360deg)")
(back-and-forth! "rp" "rotate(0deg)" "rotate(360deg)")


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
       (gen-rect white (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (draw))
     (range 10))))


(def move-me
  (->>
   (gen-shape (pattern (:id white-lines)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "exclusion"})
   (anim "loopy-left" "6s" "infinite")
   (draw)
   (atom)))

(def bg (->>
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "3s" "infinite" {:timing "linear"})
  (draw)
  (atom)))


(def blorp
  (atom (gen-group {:style {:isolation ""}}


    (->>
      (gen-rect forest 100 100 600 600 (url "bite"))
      ;(style {:mix-blend-mode "color-burn"})
      (anim "squiggle" ".4s" "infinite")
      (draw))



    #_(->>
      (gen-rect pink 400 200 400 400)
      (style {:mix-blend-mode "color-burn"})
      (style {:transform "rotate(20deg)"})
      (anim "rp" "6s" "infinite")
      (draw))

      #_(->>
        (gen-rect (pattern (str "noise-" navy)) 500 500 40 40)
        ; (style {:mix-blend-mode "color-burn"})
        (style {:opacity .8 :transform "scale(10)"})
        ;(anim "rx" "4s" "infinite")
        (style {:filter (url (:id noiz))})
        (draw))
)))

(def spins
  (atom (gen-group {:style {:transform (str "translate("
                            (* 0.55 @width) "px, "
                            (* 0.25 @height) "px) "
                            "scale(1.5)")}}
                   (->>
                     (gen-shape white oct)
                     (style {:opacity .7})
                     (anim "rot" "3s" "infinite")
                     (draw)))))



(->>
  (gen-shape white oct)
    (style {:transform (str "translate("
                            (* 0.25 @width) "px, "
                            (* 0.3 @height) "px) "
                            "scale(1.5)")})

    (style {:opacity .7})
    (draw)
    (when (nth-frame 1 frame)))




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
  ;;;;;;;;;;;;;;;;;; SM NUMBER ONE ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ; (let
  ;   [colors [
  ;     pink pink
  ;     ;yellow yellow
  ;     ;orange orange
  ;
  ;            ]]
  ;     (->>
  ;       (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
  ;       (style {:opacity .9 })
  ;
  ;       (draw)))
  ;
  ;       (gen-bg-lines pink 80)
  ;
  ;
  ;       (anim-and-hold :gf frame (frames-to-seconds 90)
  ;                      '()
  ;                      (when (nth-frame 6 frame)
  ;                        (freak-out (* 0.5 @width) @width
  ;                                   0 @height
  ;                                   16
  ;                                   60
  ;                                   gray
  ;                                   {:opacity .8})))
  ;
  ;       (anim-and-hold :mf frame (frames-to-seconds 157)
  ;                      '()
  ;                      (when (nth-frame 12 frame)
  ;                        (freak-out 0 (* 0.5 @width)
  ;                                   0 @height
  ;                                   20
  ;                                   60
  ;                                   mint
  ;                                   {:opacity .8})))
  ;
  ;
  ;     @spins
  ;
  ;     (gen-group {:style {:isolation "isolate"
  ;                         :mask (url "nf")}}
  ;
  ;                (anim-and-hold :lo frame (frames-to-seconds 48)
  ;                               '()
  ;                               (->>
  ;                                 (gen-shape white oct)
  ;                                   (style {:transform (str "translate("
  ;                                                           (* 0.25 @width) "px, "
  ;                                                           (* 0.3 @height) "px) "
  ;                                                           "scale("
  ;                                                           (val-cyc frame [1.2 2 1.4 1.4 2 1 4])
  ;                                                           ")")})
  ;
  ;                                   (style {:opacity .7})
  ;                                   (draw)
  ;                                   (when (nth-frame 1 frame))))
  ;
  ;                (anim-and-hold :ph frame (frames-to-seconds 90)
  ;                               '()
  ;                               (->>
  ;                                 (gen-shape pink hept)
  ;                                   (style {:transform (str "translate("
  ;                                                           (* 0.15 @width) "px, "
  ;                                                           (* 0.20 @height) "px) "
  ;                                                           "scale("
  ;                                                           (val-cyc (/ frame 3) [1.2 1.3 1.4 1.4 1.2 1.4])
  ;                                                           ")")})
  ;                                  (style {:mix-blend-mode "exclusion"})
  ;                                   (style {:opacity .7})
  ;                                   (draw)
  ;                                   (when (nth-frame 2 frame))))
  ;
  ;                (anim-and-hold :nh frame (frames-to-seconds 97)
  ;                               '()
  ;                               (->>
  ;                                 (gen-shape navy hept)
  ;                                   (style {:transform (str "translate("
  ;                                                           (* 0.35 @width) "px, "
  ;                                                           (* 0.40 @height) "px) "
  ;                                                           "scale(1.3)")})
  ;                                  (style {:mix-blend-mode "color-burn"})
  ;                                   (style {:opacity .7})
  ;                                   (draw)
  ;                                   (when (nth-frame 3 frame))))
  ;
  ;
  ;                (anim-and-hold :nhl frame (frames-to-seconds 157)
  ;                               '()
  ;                               (->>
  ;                                 (gen-shape (pattern (:id navy-lines)) hept)
  ;                                   (style {:transform (str "translate("
  ;                                                           (* 0.35 @width) "px, "
  ;                                                           (* 0.40 @height) "px) "
  ;                                                           "scale("
  ;                                                           (val-cyc (/ frame 5) [1.2 2 1.4 1.4 2 1 4])
  ;                                                           ")")})
  ;                                  (style {:mix-blend-mode "color-burn"})
  ;                                   (style {:opacity .7})
  ;                                   (draw)
  ;                                   (when (nth-frame 5 frame))))
  ;
  ;                )
  ;
  ;
  ;
  ;                (when (nth-frame 24 frame)
  ;                  (freak-out @width
  ;                             @height
  ;                             8
  ;                             200
  ;                             white))
  ;
  ;
  ;             (anim-and-hold :dots frame (frames-to-seconds 217)
  ;                            '()
  ;                             (gen-group {}
  ;                                        (freak-out @width
  ;                                                   @height
  ;                                                   8
  ;                                                   200
  ;                                                   white)
  ;                                        (freak-out @width
  ;                                                   @height
  ;                                                   6
  ;                                                   200
  ;                                                   mint)
  ;                                        (freak-out @width
  ;                                                   @height
  ;                                                   4
  ;                                                   200
  ;                                                   pink)))

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; SM NUMBER TWO ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  (let
    [colors [
      pink pink
      ;yellow yellow
      ;orange orange

             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:opacity .9 })

        (draw)))





        #_(->>
          (gen-rect mint 400 400 40 40)
          (style {:filter (url (:id noiz))})
          (style {:transform "scale(10)"})
          (draw))

          #_(->>
            (gen-rect mint 200 200 400 400)
            (style {:filter (url (:id noiz))})
            (style {:transform "scale(1)"})
            (draw))

          #_(->>
            (gen-rect (pattern (str "noise-" mint)) 200 200 400 400)
            (style {:transform "scale(1)"})
            (draw))



    (gen-group {:style {:isolation "isolate"}}

      #_(->>
        (gen-rect mint 100 100 400 400)
        (style {:mix-blend-mode "color-burn"})
        (style {:perspective "550px" :transform-style "preserve-3d"})
        (draw)
        (when (nth-frame 12 frame)))


        #_(->>
          (gen-rect pink 400 200 400 400)
          (style {:mix-blend-mode "color-burn"})
          (style {:transform "rotate(20deg)"})
          (draw)
          (when (nth-frame 4 frame)))


        #_(->>
          (gen-rect navy 300 300 400 400)
          (style {:mix-blend-mode "color-burn"})
          ;(style {:filter (url (:id noiz))})
          (draw)
          (when (nth-frame 1 frame))))


            #_(->>
              (gen-circ (pattern (:id yellow-lines)) (* 0.5 @width) (* 0.5 @height) 240)
                (style {:filter (url (:id turbulence-5))})
                (draw)
                (when (nth-frame 1 frame)))





                #_(->>
                  (gen-rect forest -100 -100 "120%" "120%" (url "bite"))
                  ;(style {:mix-blend-mode "color-burn"})
                  ;(anim "squiggle" ".4s" "infinite")
                  (draw))

                  #_(->>
                    (gen-rect white 100 100 600 600)
                    (draw))

                   #_(->>
                     (gen-shape "#000" hept)
                       (style {:transform "translate(50vw, 30vh) scale(3) rotate(60deg)"})
                       (draw))





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
              ["nn"
               [ :image {:key (random-uuid) :x "100" :y "200" :width "100%" :height "100%" :xlinkHref "img/blop.png" :style {:transform-origin "center" :transform "scale(10)"} }]]
              ["nm"
               [ :image {:key (random-uuid) :x "100" :y "200" :width "100%" :height "100%" :xlinkHref "img/blop.png" :style {:transform-origin "center" :transform "scale(1)"} }]]
              ["nf"
              (->>
                (gen-rect white 100 100 4000 4000)
                (style {:filter (url (:id noiz))})
                (style {:transform "scale(1)"})
                (draw))]

                ["bite"
                  (gen-group {} (->>
                    (gen-rect white 100 100 600 600)
                    (draw))

                   (->>
                     (gen-shape "#000" hept)
                       (style {:transform "translate(40vw, 20vh) scale(3) rotate(60deg)"})
                       (draw)))
                       ]
            ])


;(def collection (atom (cx @frame)))

(def masks (map (fn [[id & rest]] (apply gen-mask id rest)) mask-list))


(def all-filters [turb noiz soft-noiz disappearing splotchy blur
                  turbulence-1 turbulence-2 turbulence-3 turbulence-4 turbulence-5])
(def all-fills [gray mint navy blue orange br-orange pink white yellow midnight])


(defn drawing []
  [:svg {
    :style  {:mix-blend-mode
              (val-cyc @frame
                      [
                      ; "overlay"
                       ;"hard-light"
                       ;"color-dodge"
                      ;"difference"
                      "normal"
                      ;"screen"
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
