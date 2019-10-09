(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join trim]]
            [ui.helpers :refer [cos sin style url val-cyc]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                          b1 b2 b3 b4]]
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
                anim anim-and-hold add-remove
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
(back-and-forth! "scaley-huge" "scale(20)" "scale(100)")


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
  "colorcolorcolorcolor"
    [10, 35, 55, 85, 92]
   (make-body "fill" [
                      (pattern (str "noise-" midnight))
                      (pattern (str "noise-" mint))
                      (pattern (str "noise-" navy))
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
  "woosh-sm"
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
       (gen-rect midnight (+ 30 (* % 160)) 10 200 36)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
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
   (gen-shape yellow hept)
   (style {:opacity 1 :transform-origin "center" :transform "scale(4.4)"})
   (style {:mix-blend-mode "difference"})
   (anim "woosh-2" "16s" "infinite")
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


(def bb5
  (->>
    (gen-shape mint oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
      (anim "woosh-3" "2s" "infinite")
    (draw)
    (atom)))

(def bb5s
  (->>
    (gen-shape (pattern (:id orange-lines))  oct)
      (style {:transform "translate(10vw, 30vh) scale(2) rotate(45deg)"})
      ;(style {:mix-blend-mode "color-dodge" :filter (url (:id noiz))} )
          (style {:mix-blend-mode "difference"} )

      (anim "woosh-3" "3s" "infinite")
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

      (anim "loopy-left" "3s" "infinite")
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
          (gen-rect (pattern (str "noise-" white)) 0 0 @width @height)
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
   (gen-group {:style {:transform-origin "center" :transform "translate(15vw, 15vh) scale(3)"}})
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
      #_(gen-group {:mask (url "dmask")})
      (atom)))
   (take 4
         (repeatedly #(rand-nth [[10 gray-dots-lg] [9 pink-lines] [12 white-dots]])))))



(def bb
  (->>
   (->>
    (gen-grid
      20 20
      {:col 40 :row 40}
      (->>
      (gen-shape white oct)))
      (map #(style {:mix-blend-mode "overlay"}  %))
      (map #(anim "colorcolorcolorcolor" (str (rand-int 60) "s") "infinite" %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center"
                                :transform (str
                                            "rotate(" (rand-int 360) "deg)"
                                            "scale(8) translate(-20vh, -20vh)")}} %)))
   (atom)))


(defn deform [shape num-points amount]
  (let
    [shape-vec (split shape #"[Mz ]")
     to-change (set (take num-points (repeatedly #(rand-nth shape-vec))))
     ; idx-to-change (take num-points (repeatedly (rand-int (count shape-vec))))
     new-vec (map-indexed
      (fn [idx item]
        (if (contains? to-change item)
          (+ (js/Number item) (rand-int amount))
          item))
      shape-vec)
     ]
        
    (str "M" (join " " (rest new-vec)) "z")
     
    ))

(defonce b
  (scatter 100 (->>
   (gen-circ navy 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))

(defonce c
  (scatter 10 (->>
   (gen-circ pink 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))


(defonce d
  (scatter 20 (->>
   (gen-circ mint 10 10 60)
   (style {:mix-blend-mode "color-dodge"})
   (draw))))

(defonce k
  (scatter 40 
           (->>
            (gen-line [10 10] [200 100] white 10)
            (draw))))


(defonce h 
  (scatter 30
           (->>
            (gen-shape white hept)
            (draw))))

(defonce j 
  (scatter 14 2.2
           (->>
            (gen-shape white hept)
            (style {:mix-blend-mode "color-dodge"})
            (draw))))


(def bbb (atom (gen-group {:style {:animation "rot 6s infinite"}} @bb)))


(def hd (deform hex 8 30))



(def rrect 
  (atom 
   (->>
    (gen-grid
      2 2
      {:col 300 :row 300}
      (->>
       (gen-rect white 0 0 200 200)))
      ;(map #(style styles %))
      ;(map #(anim "rot" "2s" "infinite" %))
      (map draw)
      (map #(gen-group {:style {:transform-origin "center" :transform "translate(40vw, 30vh)" }} %))
      #_(map #(gen-group {:style {:transform-origin "center" :animation "rot 2s infinite" }} %)))))



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
          (map #(gen-group {:style {:transform-origin "center" :transform "translate(-200px, 100px)"}} %))
            (map #(gen-group { :style {:transform-origin "center"  :animation "rot 5s infinite" }} %)))))

(def l1 (lerp))

(defn cx2 [frame]
  (list
   
   
    ;@bb6s
    ;@bb6
    ;@bb4
    
    ;(when (nth-frame 4 frame) @b)
    ;(when (nth-frame 2 frame) @c)
    ;(when (nth-frame 3 frame) @d)
    
    ;@bbb
    
    
    #_(when (nth-frame 4 frame)
      (freak-out @width
                 @height
                 20
                 80
                 mint))
    
      #_(when (nth-frame 2 frame)
        (freak-out @width
                   @height
                   10
                   200
                   orange))

   ))



(defn cx [frame]
  (list

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

  (when (nth-frame 1 frame)
    (freak-out @width
               @height
               6
               400
               gray))

  (let
    [colors [
             ;midnight 
             ;midnight midnight midnight 
             ;yellow 
             ;pink pink
             ;white 
             ;white white
             ;pink 
             ;pink pink
             
             navy navy navy navy
             ;navy navy navy navy
             pink pink pink pink
             ;pink pink pink pink
             yellow yellow yellow yellow
             ;yellow yellow yellow yellow 
             br-orange br-orange br-orange
            ; br-orange br-orange br-orange br-orange br-orange
             
             ;charcoal

             ]]
      (->>
        (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
        (style {:opacity .9})
        (draw)))

  
  
  



  


  

  


  #_(list (->>
   (gen-rect pink 
             (* 0.1 @width) (* 0.1 @height) 
             (* 0.2 @width) (* 0.2 @height))
   (draw)
   (when (nth-frame 3 frame)))
  
  (->>
   (gen-rect yellow 
             (* 0.32 @width) (* 0.1 @height) 
             (* 0.52 @width) (* 0.2 @height))
   (draw)
   (when (nth-frame 3 (+ 1 frame))))
  
    (->>
     (gen-rect midnight 
               (* 0.1 @width) (* 0.32 @height) 
               (* 0.2 @width) (* 0.2 @height))
     (draw)
     (when (nth-frame 4 frame)))
  
    (->>
     (gen-rect mint 
               (* 0.32 @width) (* 0.32 @height) 
               (* 0.52 @width) (* 0.52 @height))
     ;(style {:mix-blend-mode "overlay"})
     (style {:transform (str "scale("
                            (val-cyc frame (concat
                                            (repeat 2 1)
                                            (repeat 2 .5)
                                            (repeat 2 2)
                                            (repeat 2 2.5)
                                            (repeat 2 13)
                                            (repeat 2 2)))
                            ")")})
     (draw)
     (when (nth-frame 5 frame)))
  
  
(->>
       (gen-rect white 
                 (* 0.1 @width) (* 0.54 @height) 
                 (* 0.2 @width) (* 0.3 @height))
       (style {:mix-blend-mode "overlay"})
       (style {:transform (str "scale("
                              (val-cyc frame (concat
                                              (repeat 3 10)
                                              (repeat 3 .5)
                                              (repeat 3 2)
                                              (repeat 3 2.5)
                                              (repeat 3 3)
                                              (repeat 3 12)))
                              ")")})
       (draw)
       (when (nth-frame 3 frame))))



  
  #_(when (nth-frame 9 frame)(->>
   (gen-grid
     40 40
     {:col 220 :row 220}
     (->>
      (gen-shape navy hex)))
     ;(map #(anim animations %))
     (map draw)
     (map #(gen-group {:style {:transform-origin "center" :transform "scale(.6)"}} %))))
    

  ; (add-remove "chewed-grid" frame 1 9
  ;               (gen-group {:mask (url "na")}
  ;                          (when (nth-frame 2 (+ 1 frame)) 
  ;                            (gen-line-grid white 2
  ;                                           100 80
  ;                                           {:col 20 :row 20}))
  ; 
  ;                 (when (nth-frame 2 (+ 0 frame)) 
  ;                   (gen-line-grid white 6
  ;                                  100 80
  ;                                 {:col 20 :row 20}))))
  ; 
  ; (add-remove "just-grid" frame 9 19
  ;               (gen-group {:mask (url "")}
  ;                          (when (nth-frame 2 (+ 1 frame)) 
  ;                            (gen-line-grid white 2
  ;                                           100 80
  ;                                           {:col 20 :row 20}))
  ; 
  ;                 (when (nth-frame 2 (+ 0 frame)) 
  ;                   (gen-line-grid white 6
  ;                                  100 80
  ;                                 {:col 20 :row 20}))))
  ; 
  ; (add-remove "circle" frame 19 29
  ;             (->>
  ;              (gen-circ white (* 0.5 @width) (* 0.5 @height) "16vw")
  ;              (style {:transform "rotate(135deg)"})
  ;                 #_(style {:transform (str "rotate(135deg) scale("
  ;                                        (val-cyc frame (concat
  ;                                                        (repeat 2 1)
  ;                                                        (repeat 2 .5)
  ;                                                        (repeat 2 2)
  ;                                                        (repeat 2 2.5)
  ;                                                        (repeat 2 13)
  ;                                                        (repeat 2 2)))
  ;                                        ")")})
  ;              (draw)
  ;              (when (nth-frame 1 frame))))
  ; 
  ; (add-remove "squares" frame 29 39 @rrect)
  ; 
  ; (add-remove "lines" frame 39 49 (->>
  ;  (gen-circ (pattern (:id white-lines)) (* 0.5 @width) (* 0.5 @height) "16vw")
  ;  (style {:transform "scale(12)"})
  ;  (draw)
  ;  (when (nth-frame 1 frame))))
  ; 
  ; (add-remove "spin" frame 49 59 @lr1)
  ; 
  ; (add-remove "noise" frame 59 69 @sc-circ)
  ; (add-remove "anim-lines" frame 69 79 @scale-me)
  ; 
  ; (add-remove "b1" frame 79 89 @bnz)
  ; (add-remove "b2" frame 89 99 @bnz-2)
  ; 
  #_(gen-group {:mask (url "na")}
             (when (nth-frame 2 (+ 1 frame)) 
               (gen-line-grid white 2
                              100 80
                              {:col 20 :row 20}))
    
    (when (nth-frame 2 (+ 0 frame)) 
      (gen-line-grid white 6
                     100 80
                    {:col 20 :row 20})))
  
  
  #_(->>
   (gen-circ (pattern (:id white-lines)) (* 0.5 @width) (* 0.5 @height) "16vw")
   (style {:transform "rotate(135deg)"})
      #_(style {:transform (str "rotate(135deg) scale("
                             (val-cyc frame (concat
                                             (repeat 2 1)
                                             (repeat 2 .5)
                                             (repeat 2 2)
                                             (repeat 2 2.5)
                                             (repeat 2 13)
                                             (repeat 2 2)))
                             ")")})
   (draw)
   (when (nth-frame 1 frame)))
  
  #_(->>
   (gen-circ white (* 0.5 @width) (* 0.5 @height) "16vw")
   (style {:transform "rotate(135deg)"})
      #_(style {:transform (str "rotate(135deg) scale("
                             (val-cyc frame (concat
                                             (repeat 2 1)
                                             (repeat 2 .5)
                                             (repeat 2 2)
                                             (repeat 2 2.5)
                                             (repeat 2 13)
                                             (repeat 2 2)))
                             ")")})
   (draw)
   (when (nth-frame 1 frame)))
  
  
  ;@rrect
  
  #_(->>
   (gen-circ (pattern (:id white-lines)) (* 0.5 @width) (* 0.5 @height) "16vw")
   (style {:transform "scale(12)"})
   (draw)
   (when (nth-frame 1 frame)))
  
  ;@lr1
  
  ;@sc-circ
  ;@scale-me
  
  
  @bnz
  ;@bnz-2
  



)) ; cx end

(when DEBUG
  (defonce collection (atom (cx 1))))

(when DEBUG
  (defonce collection2 (atom (cx2 1))))

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
            ["dmask" @b]
                ["jmask" @j]
            [ "grad-mask"
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" }]]
            [ "grad-mask-an"
              [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" :animation "woosh 3s infinite" }]]
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
                ["na" [ :image {:key (random-uuid)
                                :x "0"
                                :y "0"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/blop.png"
                                :style {:transform-origin "center"
                                        :transform "scale(2)"} }]]
                ["nn" [ :image {:key (random-uuid)
                                :x "100"
                                :y "200"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/blop.png"
                                :style {:transform-origin "center"
                                        :transform "scale(10)"
                                        } }]]
            ])


(def masks (map (fn [[id & rest]] (apply gen-mask id rest)) mask-list))


(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [gray mint navy blue orange br-orange pink white yellow midnight])


(defn drawing []
  [:svg {
    :style  {:mix-blend-mode
             (val-cyc @frame
                      [
                      ;"luminosity" "luminosity"
                      ;"difference"
                      ;"multiply"
                       
                      "multiply" "multiply" "multiply" "multiply"
                       ;"multiply" "multiply" "multiply" "multiply"
                      ;"difference" "difference" "difference" 
                       ;"difference"
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
     [:circle {:id "testCirc3"
               :cx 0 :cy 0 :r 100
               :style {:animation "colorcolorcolor 100s infinite"}
               :fill (pattern (str "noise-" yellow))}]
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
                           "multiply" "multiply" "multiply" "multiply" ;"multiply" "multiply" "multiply" "multiply"
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

(reagent/render [:div
                 [:div {:style {:position "absolute" :top 0 :left 0}} [drawing]] [:div {:style {:position "absolute" :top 0 :left 0}} [drawing2]]]
                          (js/document.getElementById "app-container"))

#_(reagent/render [drawing]
                          (js/document.getElementById "app-container"))

;(hide-display)
