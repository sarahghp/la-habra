(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.helpers :refer [cos sin style url val-cyc]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                          b1 b2 b3 b4]]
            [ui.fills :as fills :refer
              [gray mint midnight navy blue orange
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
              [ make-body splice-bodies make-frames!
                nth-frame even-frame odd-frame
                seconds-to-frames frames-to-seconds
                anim anim-and-hold
                ]]))

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
(a-to-b! "bbll" "fill" pink white)
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(125%)" (str "translateY("(* 0.15 @height)")"))
(a-to-b! "grow2to3" "transform" "rotate(280deg) scale(1)" "rotate(280deg) scale(1.2)")

(fade-start! "fi" 1)

(make-frames! "scud"
             [10 20 25 60 73]
            (make-body "transform" [
                                    "translateX(-10vw)"
                                    "translateX(20vw)"
                                    "translateX(50vw)"
                                    "translateX(80vw)"
                                    "translateX(110vw)"]) )

(make-frames!
  "supercolor"
    [10, 35, 55, 85, 92]
   (make-body "fill" [pink pink yellow midnight midnight]))

(make-frames!
  "colorcolor"
    [10, 35, 55, 85, 92]
   (make-body "fill" [pink yellow br-orange white midnight]))

(make-frames!
  "colorcolorcolor"
    [10, 35, 55, 85, 92]
   (make-body "fill" [
                      (pattern (str "noise-" pink))
                      (pattern (str "noise-" yellow))
                      (pattern (str "noise-" br-orange))
                      (pattern (str "noise-" white))
                      (pattern (str "noise-" midnight))]))

(make-frames!
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 50vh) rotate(2deg) scale(1.2)"
                           "translate(60vw, 60vh) rotate(-200deg) scale(2.4)"
                           "translate(40vw, 40vh) rotate(120deg) scale(4.4)"
                           "translate(20vw, 30vh) rotate(-1000deg) scale(3.2)"
                           "translate(60vw, 80vh) rotate(300deg) scale(6.2)"]))

(make-frames!
  "woosh-2"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 50vh) rotate(2deg) scale(11.2)"
                           "translate(60vw, 60vh) rotate(-200deg) scale(4.4)"
                           "translate(40vw, 40vh) rotate(120deg) scale(13.4)"
                           "translate(20vw, 30vh) rotate(-210deg) scale(4.2)"
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
       (gen-rect pink (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (draw))
     (range 10))))



(def move-me
  (->>
   (gen-shape (pattern (:id white-lines)) hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "luminosity"})
   (anim "woosh" "3s" "infinite")
   (draw)
   (atom)))


(def move-me-2
  (->>
   (gen-shape yellow hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "color-dodge"})
   (anim "woosh-2" "4s" "infinite")
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
    (gen-shape pink oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "difference"} )

      (anim "loopy-right" "2s" "infinite")
    (draw)
    (atom)))

(def bb6s
  (->>
    (gen-shape (pattern (:id pink-dots))  oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "difference"} )

      (anim "loopy-left" "8s" "infinite")
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
          (anim "scaley-huge" "5s" "infinite")
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
   (gen-shape white tri)
   (anim "morph" "10s" "infinite")
   (draw)
   (gen-group {::transform-origin "center" :transform "translate(50vh, 50vh)"})
   (atom)))


(make-frames!
  "woosh-s"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 50vh) rotate(2deg) scale(1.2)"
                           "translate(60vw, 60vh) rotate(-200deg) scale(2.4)"
                           "translate(40vw, 40vh) rotate(120deg) scale(3.4)"
                           "translate(20vw, 30vh) rotate(-210deg) scale(2.2)"
                           "translate(60vw, 80vh) rotate(400deg) scale(6.2)"]))

  (def bb1
    (->>
      (gen-shape mint oct)
        (style {:transform "translate(20vw, 30vh) scale(2)"})
        (style {:mix-blend-mode "color-dodge" } )
      (anim "woosh-s" "12s" "infinite")
      (draw)
      (atom)))

  (def bb1a
    (->>
      (gen-shape mint oct)
        (style {:transform "translate(20vw, 30vh) scale(2)"})
        (style {:mix-blend-mode "color-burn" } )
      (anim "woosh-s" "8s" "infinite")
      (draw)
      (atom)))

  (def bb1aa
    (->>
      (gen-shape pink oct)
        (style {:transform "translate(20vw, 30vh) scale(2)"})
        (style {:mix-blend-mode "exclusion" } )
      (anim "woosh-s" "6s" "infinite")
      (draw)
      (atom)))

  (def bb1aaa
    (->>
      (gen-shape pink oct)
        (style {:transform "translate(20vw, 30vh) scale(2)"})
        (style {:mix-blend-mode "exclusion" } )
      (anim "loopy-right" "8s" "infinite")
      (draw)
      (atom)))

(def bg (->>
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "13s" "infinite" {:timing "linear"})
  (draw)
  (atom)))


  (def bg2 (->>
    (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
    (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
    (anim "sc-rot" "8s" "infinite" {:timing "linear" :delay "2s"})
    (draw)
    (atom)))

(def scale-me-2
        (->>
          (gen-circ (pattern (str "noise-" pink)) (* 0.5 @width) (* 0.5 @height) 200)
          (style {:transform "scale(1)"})
          (anim "scaley" "3s" "infinite")
          (draw)
          (atom)))


(def move-me-3
  (->>
   (gen-shape (pattern (:id white-lines)) oct)
   (style {:opacity .6 :transform-origin "center" :transform "translate(120vw, -10vh)"})
   (style {:mix-blend-mode "color-burn"})
   (anim "loopy-left" "12s" "infinite")
   (draw)
   (atom)))


   (def move-me-4
     (->>
      (gen-shape (pattern (:id white-lines)) oct)
      (style {:opacity .6 :transform-origin "center" :transform "translate(-100vw, -10vh)"})
         (style {:mix-blend-mode "color-burn"})
      (anim "loopy-right" "16s" "infinite")
      (draw)
      (atom)))


      (def move-me-3a
        (->>
         (gen-shape (pattern (:id mint-dots)) oct)
         (style {:opacity .6 :transform-origin "center" :transform "translate(120vw, -10vh)"})
         (anim "loopy-left" "8s" "infinite")
         (draw)
         (atom)))


         (def move-me-4a
           (->>
            (gen-shape (pattern (:id mint-dots)) oct)
            (style {:opacity .6 :transform-origin "center" :transform "translate(-100vw, -10vh)"})
            (anim "loopy-right" "8s" "infinite")
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



(def bb
  (->>
   (->>
    (gen-grid
      40 28
      {:col 40 :row 40}
      (->>
      (gen-shape mint oct)))
      (map #(style {:mix-blend-mode "difference"}  %))
      (map #(anim "supercolor" (str (rand-int 100) "s") "infinite" %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center"
                                :transform (str
                                            "rotate(" (rand-int 360) "deg)"
                                            "scale(60) translate(-20vh, -20vh)")}} %)))
   (atom)))

(defonce b
  (scatter 20 (->>
   (gen-circ navy 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))

(defonce c
  (scatter 20 (->>
   (gen-circ pink 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))


(defonce d
  (scatter 20 (->>
   (gen-circ yellow 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))

(defonce e
  (scatter 20 (->>
   (gen-circ navy 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))

(defonce f
  (scatter 20 (->>
   (gen-circ pink 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))


(defonce g
  (scatter 20 (->>
   (gen-circ yellow 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))






 ;; ----------- COLLECTION SETUP AND CHANGE ----------------

(def DEBUG false)

(when-not DEBUG
  (defonce collection (atom (list))))

(when-not DEBUG
  (defonce collection2 (atom (list))))

;(reset! ran {})

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
          (map #(gen-group {:mask (url "bitey") :style {:transform-origin "center" :animation "rot 10s infinite" }} %)))))

(def rr2 (atom
         (->>
          (gen-grid
            20 30
            {:col 100 :row 150}
            (->>
             (gen-shape yellow tri)))
              (map #(style {:opacity 1 :mix-blend-mode "difference"} %))

            (map #(anim "morph" "5s" "infinite" %))
            (map draw)
                  (map #(gen-group {:style {:transform-origin "center" :transform "scale(2)"}} %))
          (map #(gen-group {:style {:transform-origin "center" :transform "translate(-200px, -100px)"}} %))
            (map #(gen-group { :style {:transform-origin "center"  :animation "rot 5s infinite" }} %)))))

(def l1 (lerp))


;; ---------------------------------------------------------
;; ------------------ v2 based on @party -------------------
;; ---------------------------------------------------------
 
(defn cx2 [frame]
  (list))

(defn cx [frame]
  (list

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (when (nth-frame 1 frame)
    (freak-out @width
               @height
               4
               400
               gray))

  (let
    [colors [
      ;mint mint mint mint
      yellow yellow yellow yellow
      pink pink pink pink
             orange orange orange orange
             ;midnight midnight midnight midnight
             ;navy navy navy navy

             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100%" "100%")
        (style {:opacity .9})
        (draw)))

;;  (anim-and-hold name frame duration 
;;      fader solid)
  
(defn add-remove 
  [name frame start end item]
    (anim-and-hold name frame start
                 (list) 
                   (anim-and-hold name frame end
                                  item (list))))

  ;; ------------------------------------START ACCEPTABLE SECTION -------------------------------------------------------

  
    (->>
      (gen-rect (pattern (str "noise-" br-orange)) 0 0 @width @height)
      (style {:transform "scale(50)"})
      (draw))
    

    (anim-and-hold "scale01" frame 2
                   (list) @scale-me)
  
  
    (anim-and-hold "scale02" frame 4
                   (list) @scale-me-2)
  
  
    (add-remove "rectangles" frame 12 120
     (gen-group {}
                (->>
                  (gen-rect white (* 0.25 @width) (* 0.15 @height) (* 0.35 @width) (* 0.75 @height))
                  (style {:opacity .7})
                  (draw)
                  (when (nth-frame 2 frame)))

                (->>
                  (gen-rect (pattern (:id gray-dots-lg)) (* 0.5 @width) (* .2 @height) (* 0.35 @width) (* 0.75 @height))
                  (style {:opacity .5 :transform "scale(1.4)"})
                  ; (style {:mix-blend-mode "screen"})
                  (draw)
                  (when (nth-frame 4 frame)))

                (->>
                  (gen-rect mint (* 0.2 @width) (* .7 @height) (* 0.65 @width) (* 0.3 @height))
                  (style {:opacity .7})
                  (draw)
                  (when (nth-frame 3 frame)))));


  (add-remove "triangles" frame 15 45
              (gen-group {}
                         (->>
                           (gen-shape navy tri)
                             (style {:transform "translate(20vw, 60vh) rotate(135deg) scale(2)"})
                             (style {:opacity .5})
                             (draw)
                             (when (nth-frame 1 frame)))


                         (->>
                           (gen-shape br-orange tri)
                             (style {:transform (str "translate(45vw, 10vh) rotate(15deg) scale(" (val-cyc frame [1 1 2 2 4 4 8 8]) ")")})
                             (style {:opacity .5})
                               (style {:mix-blend-mode "multiply"})

                             (draw)
                             (when (nth-frame 1 frame)))


                         (gen-group {:style {:transform "translate(30vw, 20vh) rotate(2200deg)"}}
                           (->>
                             (gen-shape navy tri)
                               (style {:transform (str "translate(20vw, 60vh) rotate(135deg) scale(" (val-cyc frame [1 2 2 8 4 16]) ")")})
                               (style {:opacity .5 })
                               (draw)
                               (when (nth-frame 1 frame)))


                         (->>
                           (gen-shape br-orange tri)
                             (style {:transform "translate(45vw, 10vh) rotate(15deg) scale(2)"})
                             (style {:opacity .5})
                             (draw)
                             (when (nth-frame 1 frame))))))
  
  
  (add-remove "lines" frame 22 120 
              (gen-group {}
                (->>
                  (gen-line [100 100] [400 100] navy 60)
                  (style {:transform "translateY(20vh) rotate(68deg)"})
                  (draw)
                  (when (nth-frame 3 frame)))

                (->>
                  (gen-line [800 800] [1200 800] navy 60)
                  (style {:transform "rotate(-68deg)"})
                  (draw)
                  (when (nth-frame 5 frame)))


                (->>
                  (gen-line [100 100] [400 100] navy 60)
                  (style {:transform "translate(20vw, 30vh) rotate(68deg)"})
                  (draw)
                  (when (nth-frame 2 frame)))

                (->>
                  (gen-line [800 800] [1200 800] navy 60)
                  (style {:transform "translate(-10vw, -40vh) rotate(-68deg)"})
                  (draw)
                  (when (nth-frame 3 frame)))))



  
    (add-remove "fo-yo-flash" frame 72 108
                (list
                 (when (nth-frame 7 frame)
                   (freak-out @width
                              @height
                              20
                              200
                              white))

                 (when (nth-frame 12 frame)
                  (freak-out @width
                             @height
                             30
                             100
                             yellow))


               (when (nth-frame 9 frame)
                 (freak-out @width
                            @height
                            60
                            600
                            white
                            {:opacity .6}))))
  
  (add-remove "fo-yo" frame 108 180
              (list
               (when (nth-frame 1 frame)
                 (freak-out @width
                            @height
                            20
                            200
                            white))
               
               (when (nth-frame 1 frame)
                 (freak-out @width
                            @height
                            20
                            200
                            midnight))

               (when (nth-frame 1 frame)
                (freak-out @width
                           @height
                           30
                           100
                           yellow))

             (when (nth-frame 1 frame)
               (freak-out @width
                          @height
                          60
                          600
                          white
                          {:opacity .6}))))


  (add-remove "sparkles" frame 130 180
              (list
               (when (nth-frame 12 frame)
                 (freak-out @width
                            @height
                            20
                            100
                            pink))
               
               (when (nth-frame 1 frame)
                 (freak-out @width
                            @height
                            20
                            200
                            white))
               
               (when (nth-frame 7 frame)
                 (freak-out @width
                            @height
                            20
                            200
                            yellow))))
  
  (add-remove "small tris" frame 90 180
              (list
                 (gen-group {:style {:transform "translate(-10vh, -10vh)"}}
                            (when (nth-frame 5 frame)
                              (->>
                               (gen-grid
                                 20 10
                                 {:col 130 :row 120}
                                 (->>
                                  (gen-shape yellow tri)))
                                 ;(map #(style styles %))
                                 ;(map #(anim animations %))
                                 (map draw)
                                 (map #(gen-group {:style {:transform-origin "center" }} %)))))
                 
                 (gen-group {:style {:transform "translate(-10vh, -10vh)"}}
                            (when (nth-frame 7 frame)
                              (->>
                               (gen-grid
                                 20 10
                                 {:col 130 :row 120}
                                 (->>
                                  (gen-shape blue tri)))
                                 ;(map #(style styles %))
                                 ;(map #(anim animations %))
                                 (map draw)
                                 (map #(gen-group {:style {:transform-origin "center" }} %)))))))
  
   
  (add-remove "snims" frame 55 120 
               (list 
                 (add-remove "biggo" frame 65 120 @move-me-2)

                 @bb1
                 @bb1aa
                 @bb1aaa
                 
                 @move-me-3
                 @move-me-4
                 
                 @move-me-3a
                 @move-me-4a))  

  (add-remove "orange shapes" frame 98 180
              (when (nth-frame 3 frame)
                (->>
                 (gen-grid
                   10 20
                   {:col 330 :row 220}
                   (->>
                    (gen-shape br-orange oct)))
                   (map #(style {:mix-blend-mode "hard-light"} %))
                   (map draw)
                   (map #(gen-group {:style {:transform-origin "center" }} %)))))

  (add-remove "white shapes" frame 108 180
              (list 
               (gen-group {:style {:transform "translate(-10vw, -20vh)"}}
                          (when (nth-frame 5 frame)
                             (->>
                              (gen-grid
                                10 20
                                {:col 330 :row 120}
                                (->>
                                 (gen-shape white pent)))
                                (map #(style {:mix-blend-mode "color-dodge"} %))
                                (map draw)
                                (map #(gen-group {:style {:transform-origin "center" :transform "scale(2)" }} %)))))
             
               #_(gen-group {:style {:transform "translate(-10vw, -20vh)"}}
                          (when (nth-frame 7 frame)
                           (->>
                            (gen-grid
                              10 20
                              {:col 280 :row 120}
                              (->>
                               (gen-shape white pent)))
                              (map #(style {:mix-blend-mode "difference"} %))
                              (map draw)
                              (map #(gen-group {:style {:transform-origin "center" :transform "scale(1.2)" }} %)))))))
  

  (add-remove "giant lines" frame 106 138
              (->>
                 (gen-circ (pattern (:id 
                                     
                                      white-lines
                                     )) (* 0.5 @width) (* 0.5 @height) 200)
                 (style {:transform (str
                                     "scale("
                                         (val-cyc frame (concat
                                                         (repeat 12 0)
                                                         (repeat 1 6)
                                                         (repeat 1 8)
                                                         (repeat 1 12)
                                                         (repeat 1 14)
                                                         (repeat 1 20)))
                                         ")")})
                 (style {:mix-blend-mode "exclusion"})
                 (draw)
                 (when (nth-frame 1 frame))))
  
  (add-remove "squigg frands" frame 130 180
              @rr2)

  (add-remove "noise fo" frame 136 180
              (new-freakout @width @height 100 100 "testCirc"))
  (add-remove "noise fo 2" frame 138 180
              (new-freakout @width @height 100 100 "testCirc2"))
  #_(add-remove "noise fo 3" frame 140 180
              (new-freakout @width @height 100 100 "testCirc3"))
  #_(add-remove "noise fo 4" frame 142 180
              (new-freakout @width @height 100 100 "testCirc4"))
  
  (add-remove "griddo" frame 144 180
              (when (nth-frame 3 frame)
                (gen-line-grid midnight 3
                  120 80
                  {:col 20 :row 20})))
  
  (add-remove "griddo-o" frame 144 180
              (when (nth-frame 3 (+ 1 frame))
                (gen-line-grid white 3
                  120 80
                  {:col 20 :row 20})))
  
  (add-remove "END" frame 170 300
              (->>
               (gen-rect "#000" 0 0 @width @height)
               (draw)))
  

  ;; ------------------------------------END ACCEPTABLE SECTION -------------------------------------------------------



  #_(when (nth-frame 2 frame)(gen-line-grid white 10
    120 80
    {:col 20 :row 20}))

    #_(->>
      (gen-circ (pattern (:id (val-cyc frame [navy-lines navy-lines pink-lines pink-lines]))) (* 0.5 @width) (* 0.5 @height) 200 (url "grad-mask"))
      (style {:transform "scale(2) rotate(135deg)"})
      (draw)
      (when-not (nth-frame 3 frame)))



)) ; cx end

(when DEBUG
  (defonce collection (atom (cx 1))))

;; ----------- LOOP TIMERS ------------------------------

(defonce frame (atom 0))

(when-not DEBUG
  (defonce start-cx-timer
    (js/setInterval
      #(reset! collection (cx @frame)) 50))

  (defonce start-cx-timer-2
    (js/setInterval
      #(reset! collection2 (cx2 @frame)) 50))

  (defonce start-frame-timer
    (js/setInterval
      #(swap! frame inc) 500)))


;; ----------- DEFS AND DRAW ------------------------------

(def gradients [[:linearGradient { :id "grad" :key (random-uuid)}
                 [:stop { :offset "0" :stop-color "white" :stop-opacity "0" }]
                 [:stop { :offset "1" :stop-color "white" :stop-opacity "1" }]]])

(def mask-list [
            [ "poly-mask"
              [:path {:d b2 :fill "#fff" :style { :transform-origin "center" :animation "woosh 2s infinite"}}]]
            [ "poly-mask-2"
                          [:path {:d b3 :fill "#fff" :style { :transform-origin "center" :animation "woosh-3 3s infinite"}}]]
            ["bitey"
               (->>
                (gen-circ (pattern (str "noise-" white)) (* 0.5 @width) (* 0.5 @height) 1000)
                (style {:opacity 1 :transform "scale(.3)"})
                (draw))]
            [ "grad-mask"
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" }]]
            [ "cutout"
             (->>
               (gen-rect white 10 12 (* 0.94 @width) (* 0.88 @height))
               (draw))
             (->>
               (gen-circ "#000" (* 0.7 @width) (* 0.7 @height) 100)
                (draw))]


            ])


(def masks (map (fn [[id & rest]] (apply gen-mask id rest)) mask-list))


(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [gray mint navy blue orange br-orange pink white yellow midnight])




(defn drawing []
  [:svg {
    :style  {:mix-blend-mode
             (anim-and-hold "modes" @frame 130
              "multiply"
              (val-cyc @frame
                       [
                       "luminosity"
                       "multiply" "multiply" "multiply" "multiply"
                        "difference" "difference"
                        ])) }
    :width  (:width settings)
    :height (:height settings)}
     ;; filters
    (map #(:def %) all-filters)
    ;; masks and patterns
    [:defs
     noise
     [:circle {:id "weeCirc" :cx 0 :cy 0 :r 4
               :style {:animation "colorcolor 100s infinite"
                       :opacity .6}}]
     [:circle {:id "testCirc" :cx 0 :cy 0 :r 100 :fill (pattern (str "noise-" white))}]
     [:circle {:id "testCirc4" :cx 0 :cy 0 :r 100 :fill (pattern (str "noise-" midnight))}]
     [:circle {:id "testCirc3"
               :cx 0 :cy 0 :r 100
               :style {:animation "colorcolorcolor 100s infinite"}
               :fill (pattern (str "noise-" pink))}]
          [:circle {:id "testCirc2" :cx 0 :cy 0 :r 100 :fill (pattern (str "noise-" mint))}]

     (map identity gradients)
     (map identity masks)
     (map gen-color-noise all-fills)
     (map pattern-def [ blue-dots
                        blue-lines
                        pink-dots pink-dots-1 pink-dots-2 pink-dots-3 pink-dots-4 pink-dots-5
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


    (defn drawing2 []
      [:svg {
        :style  {:mix-blend-mode
                 (val-cyc @frame
                          [
                          ;"luminosity"
                           "multiply"
                           "difference"
                           ;"multiply" "multiply" "multiply" "multiply" ;"multiply" "multiply" "multiply" "multiply"
                           ;"difference" "difference" ;"difference" "difference"
                           ;"difference" "difference" "difference" "difference"
                           ]) }
        :width  (:width settings)
        :height (:height settings)}
         ;; filters
        (map #(:def %) all-filters)
        ;; masks and patterns
        [:defs
         noise
         [:circle {:id "weeCirc" :cx 0 :cy 0 :r 4
                   :style {:animation "colorcolor 100s infinite"
                           :opacity .6}}]
         [:circle {:id "testCirc" :cx 0 :cy 0 :r 100 :fill (pattern (str "noise-" white))}]
         (map identity gradients)
         (map identity masks)
         (map gen-color-noise all-fills)
         (map pattern-def [ blue-dots
                            blue-lines
                            pink-dots pink-dots-1 pink-dots-2 pink-dots-3 pink-dots-4 pink-dots-5
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
        @collection2
        ])

#_(reagent/render [:div
                 [:div {:style {:position "absolute" :top 0 :left 0}} [drawing]] [:div {:style {:position "absolute" :top 0 :left 0}} [drawing2]]]
                          (js/document.getElementById "app-container"))

(reagent/render [drawing]
                          (js/document.getElementById "app-container"))

;(hide-display)
