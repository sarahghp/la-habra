(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.helpers :refer [style url val-cyc]]
            [ui.letters :refer [alpha-shapes alpha-mask-list letter-defs]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct 
                                          arc-half arc-bottom-j arc-bottom-u 
                                          s-half s-curve
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
                red
                purple ]]
            [ui.generators :refer
             [draw
              freak-out
              gen-use
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
               sized-pattern-def
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
               noise
               pink-scale-dots
               pink-scale-lines]]
            [ui.animations :as animations :refer
              [ anim
                make-body
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
   (gen-shape white hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:filter (url (:id noiz))})
   (anim "woosh-4" "3s" "infinite")
   (draw)
   (atom)))

(def move-me-2
  (->>
   (gen-shape pink hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:filter (url (:id noiz))})
   (anim "woosh-4" "6s" "infinite")
   (draw)
   (atom)))

(def move-me-3
  (->>
   (gen-shape (pattern (:id mint-dots)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:filter (url (:id noiz))})
   (style {:mix-blend-mode "exclusion"})
   (anim "woosh-4" "4s" "infinite")
   (draw)
   (atom)))

(def bg (->>
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "3s" "infinite" {:timing "linear"})
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


(def alpha-keys (doall (apply concat (map (fn [[_ letter-val]]
                     (map name (keys letter-val))) 
                   alpha-shapes))))


(def alpha-display (map-indexed (fn [idx l-key]
                                  (let [num-horiz (.floor js/Math (/ (- @width 120) 160))
                                        row (.floor js/Math (/ idx num-horiz))
                                        col (mod idx num-horiz)]
                                  (gen-use {:href (str "#" l-key) 
                                            :x (+ 40 (* 150 col)) 
                                            :y (+ 30 (* 160 row))})))
                                (sort alpha-keys)))



(defn gen-letter [letter attrs]
  (let [l-key (->>
               (alpha-shapes letter)
               keys
               rand-nth
               name)]
    (gen-use (merge {:href (str "#" l-key)
                     :x 10
                     :y 10}
                    attrs))))

(def L (atom (gen-letter :l {:x 200})))


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
      ;mint 
      midnight
             ;"#000"

             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:filter (url (:id noiz))})
        (style {:transform "scale(11)"})
        (style {:opacity .5})
        (draw)))
  
  
  #_(gen-group {:transform "scale(1)"}
             (doall alpha-display))
  
(gen-group {:style {:transform "translate(200px, 200px)"}}
           (gen-letter :p {:x 10})
           (gen-letter :r {:x 130})
           (gen-letter :e {:x 250})
           (gen-letter :s {:x 370})
           (gen-letter :e {:x 490})
           (gen-letter :n {:x 610})
           (gen-letter :t {:x 730})
             
           (gen-group {:style {:transform "translateX(850px)"}}
                      (->>
                        (gen-circ yellow 60 44 30)
                        (draw))
                      (->>
                        (gen-circ mint 60 44 6)
                        (style {:stroke midnight 
                                :stroke-width 3 
                                :stroke-opacity .3})
                        (draw))
                      (gen-group {:mask (url "q")
                                  :style {:transform "translate(20px, 60px) scale(.7)"}}
                                 (->>
                                   (gen-rect yellow 0 0 120 120)
                                   (draw))
                                 
                                 (->>
                                   (gen-circ mint 60 60 10)
                                   (style {:stroke midnight 
                                           :stroke-width 3 
                                           :stroke-opacity .3})
                                   (draw))))
             
             (gen-letter :c {:x 0 :y 140})
             (gen-letter :o {:x 130 :y 140})
             (gen-letter :r {:x 250 :y 140})
             (gen-letter :r {:x 370 :y 140})
             (gen-letter :e {:x 490 :y 140})
             (gen-letter :c {:x 610 :y 140})
             (gen-letter :t {:x 730 :y 140}))
  
  #_(gen-group {:mask (url "d") :style {:transform "translate(490px, 190px)"}}
             (freak-out 10 120
                          10 120
                          10
                          20
                          red))
  
  #_(gen-group {:mask (url "f") :style {:transform "translate(640px, 350px)"}}
             (freak-out 10 120
                       10 120
                       10
                       10
                       blue
                       {:stroke purple 
                        :stroke-width 1 }))
  
  #_(gen-group {:mask (url "m")
              :transform "scale(1.1)"
              :style {:transform "translate(790px, 830px)"}}
              (freak-out 0 110
                        0 120
                        10
                        20
                        yellow))
  
  #_(gen-group {:mask (url "n")
              :style {:transform "translate(340px, 990px)"}}
              (freak-out 10 100
                          10 110
                          10
                          14
                          mint))
  

  
  
)) ; cx end

#_(defonce collection (atom (list (cx 1))))



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
               (gen-rect "#000" 10 12 (* 0.94 @width) (* 0.88 @height))
               (draw))
             (->>
               (gen-circ white  (* 0.7 @width) (* 0.7 @height) 100)
                (draw))]
              ["rect-buds"
               (->>
                 (gen-rect white 10 12 (* 0.3 @width) (* 0.5 @height))
                 (draw))
                 ]
                
              
                ["nn" [ :image {:key (random-uuid) :x "100" :y "200" :width "100%" :height "100%" :xlinkHref "img/blop.png" :style {:transform-origin "center" :transform "scale(10)" :animation "woosh 6s infinite"} }]]
                              

            ])



(def masks (map (fn [[id & rest]] (apply gen-mask id rest)) mask-list))
(def alpha-masks (map (fn [[id & rest]] (apply gen-mask id rest)) alpha-mask-list))


(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [gray mint navy blue orange br-orange pink white yellow midnight])


(defn drawing []
  [:svg {
    :style  {:mix-blend-mode
             (val-cyc @frame
                      ["multiply" "multiply"
                       ]) }
    :width  (:width settings)
    :height 2000}
    ;:height (:height settings)}
     ;; filters
    (map #(:def %) all-filters)
    ;; masks and patterns
    [:defs
     noise
     (map identity gradients)
     (map identity masks)
     (map identity alpha-masks)
     (map gen-color-noise all-fills)
     (doall (map deref (letter-defs alpha-shapes)))
     (sized-pattern-def pink-scale-dots 20 105)
     (sized-pattern-def pink-scale-lines 200 200)
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
                        shadow
                        pink-scale-dots
                        pink-scale-lines ])]

    ;; then here dereference a state full of polys
    @collection

    ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))

;(hide-display)
