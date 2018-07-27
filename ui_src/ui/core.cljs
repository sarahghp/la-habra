(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct b1 b2 b3 b4]]
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


;; ------------------------ WRAPPERS ---------------------

(defn circ
  [{:keys [x y r style mask] :or {mask ""}}]
  [:circle { :cx x
             :cy y
             :r r
             :style style
             :mask mask
             :key (random-uuid)} ])

(defn line
  [{:keys [first-point second-point color width style] :or { width 4 style {} }}]
  [:line { :x1 (first-point 0)
           :y1 (first-point 1) 
           :x2 (second-point 0)
           :y2 (second-point 1)
           :stroke color
           :stroke-width width
           :style style
           :key (random-uuid) }])

(defn polygon
  [{:keys [points style mask] :or {mask ""}}]
  [:polygon { :key (random-uuid)
              :points points
              :style style
              :mask mask }])

(defn rect
 [{:keys [x y w h style]}]
 [:rect { :x x
          :y y
          :width w
          :height h
          :style style
          :key (random-uuid)} ])
          
(defn shape
  [{:keys [d style mask] :or {mask ""}}]
  [:path { :key (random-uuid) 
           :d d 
           :style style 
           :mask mask }])



;; ------------------------ GENERATORS ---------------------

(defn gen-circ
  [fill-string x y radius & mask]
  { :x x
    :y y
    :r radius
    :mask mask
    :style { :fill fill-string }})
    
(defn gen-group
  ([internals] (gen-group {} internals))
  ([{ :keys [style mask] :or { style {} mask "" } } & internals]
    [:g { :key (random-uuid) :style style :mask mask } internals ]))

(defn gen-line
  [first-point second-point color & width]
  { :first-point first-point
    :second-point second-point
    :color color 
    :width width
    :style {}})

(defn gen-poly
  [fill-string points & mask]
  { :points points 
    :style { :fill fill-string } 
    :mask mask})
    
(defn gen-rect
  [fill-string x y w h]
  { :x x
    :y y
    :w w
    :h h
    :style {
      :fill fill-string }})
    
(defn gen-shape
  [fill-string path & mask]
  { :style { :fill fill-string }
    :mask mask
    :d path })

(defn gen-offset-lines
  [f h space-btw line-num]
  (let [adj-h (* h line-num 0.2)]
    [:rect { :x 0
             :y (* line-num (+ adj-h space-btw))
             :width (:width settings)
             :height adj-h
             :fill f
             :key (random-uuid)}]))

(defn gen-bg-lines
  ([color num] (gen-bg-lines color num {}))
  ([color num style]
  [:g {
    :key (random-uuid)
    :style style} 
    (map (partial gen-offset-lines color 1 4) (range num))]))

;; freakout varieties: circles with just r num color, default to @width/@height
;; ability to to pass max AND min to distro

(defn freak-out
  ([x y r num color] (freak-out x 0 y 0 r num color {}))
  ([x y r num color style] (freak-out 0 x 0 y r num color style))
  ([min-x max-x min-y max-y r num color] (freak-out min-x max-x min-y max-y r num color {}))
  ([min-x max-x min-y max-y max-r num color style]
  [:g { :key (random-uuid) 
        :style style } 
      (map 
        #(circ (gen-circ color (+ min-x (rand (- max-x min-x))) (+ min-y (rand (- max-y min-y))) (rand max-r))) 
        (range num))]))

(defn gen-grid
  ([cols rows offset base-obj]
    (let [x (base-obj :x) 
          y (base-obj :y)
          a-off (offset :col)
          b-off (offset :row)]
            (set (for [a (range cols) b (range rows)]
              (merge base-obj {:x (+ x (* a a-off))} {:y (+ y (* b b-off))}))))))
              

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

(back-and-forth! "scaley" "scale(1)" "scale(15)")

(a-to-b! "new-fi" "fill-opacity" "0" ".5")
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(125%)" (str "translateY("(* 0.15 @height)")"))

(make-frames
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80%, 50%) rotate(2deg) scale(1.2)"
                           "translate(380%, 100%) rotate(-200deg) scale(2.4)"
                           "translate(80%, 450%) rotate(120deg) scale(3.4)"
                           "translate(380%, 300%) rotate(-210deg) scale(2.2)"
                           "translate(80%, 50%) rotate(400deg) scale(6.2)"]))

(make-frames
  "woosh-2"
    [10, 35, 55, 85, 92]
   (make-body "transform" ["translate(80%, 50%) rotate(2deg) scale(1.2)"
                           "translate(380%, 100%) rotate(-200deg) scale(4.4)"
                           "translate(80%, 450%) rotate(120deg) scale(8.4)"
                           "translate(380%, 300%) rotate(-210deg) scale(10.2)"
                           "translate(80%, 50%) rotate(400deg) scale(16.2)"]))


(make-frames
 "woosh-3"
   [10, 35, 45, 65, 85]
  (make-body "transform" ["translate(480%, 50%) rotate(2deg) scale(4.2)"
                          "translate(180%, 250%) rotate(-200deg) scale(8.4)"
                          "translate(0%, 550%) rotate(220deg) scale(15.4)"
                          "translate(0%, 300%) rotate(-210deg) scale(20.2)"
                          "translate(280%, 150%) rotate(400deg) scale(24)"]))

(make-frames
 "woosh-4"
   [10, 35, 55, 75]
  (make-body "transform" ["translate(480%, 50%) rotate(2deg) scale(15.2)"
                          "translate(180%, 250%) rotate(-200deg) scale(18.4)"
                          "translate(0%, 550%) rotate(220deg) scale(20.4)"
                          "translate(280%, 150%) rotate(400deg) scale(30)"]))

  
(make-frames
 "woosh-5"
   [10, 35, 55, 85, 92]
  (make-body "transform" ["translate(480%, 50%) rotate(2deg) scale(1.2)"
                          "translate(180%, 250%) rotate(-200deg) scale(4.4)"
                          "translate(0%, 550%) rotate(220deg) scale(8.4)"
                          "translate(0%, 300%) rotate(210deg) scale(15.2)"
                          "translate(280%, 150%) rotate(400deg) scale(18)"]))

(make-frames
 "woosh-6"
   [10, 35, 55, 65, 85, 95]
  (make-body "transform" ["translate(80%, 50%) rotate(2deg) scale(1.2)"
                          "translate(380%, 100%) rotate(-200deg) scale(2.4)"
                          "translate(80%, 450%) rotate(120deg) scale(3.4)"
                          "translate(380%, 300%) rotate(-210deg) scale(2.2)"
                          "translate(80%, 50%) rotate(400deg) scale(6.2)"
                          "translate(80%, 50%) rotate(400deg) scale(10.2)"]))

(make-frames
 "woosh-7"
   [10, 35, 55, 65, 85, 95]
  (make-body "transform" ["translate(10%, 50%) rotate(2deg) scale(1.2)"
                          "translate(80%, 100%) rotate(-200deg) scale(2.4)"
                          "translate(380%, 450%) rotate(120deg) scale(4.4)"
                          "translate(480%, 300%) rotate(-210deg) scale(6.2)"
                          "translate(10%, 50%) rotate(400deg) scale(12.2)"
                          "translate(10%, 50%) rotate(400deg) scale(20.2)"]))
    

;; --------------- ATOMS STORAGE --------------------

(def drops
  (atom  (map
     #(->>
       (gen-rect mint (+ 30 (* % 160)) 10 200 24)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (rect))
     (range 6))))
     
 (def drops-2
   (atom  (map
      #(->>
        (gen-rect white (+ 30 (* % 160)) 10 200 24)
        (anim "etof" "1.2s" "infinite" {:delay (str (* .7 %) "s")})
        (rect))
      (range 6))))

(def bloops
  (->>
    (gen-circ white 0 100 40)
    (style {:opacity .7})
    (anim "bloop-x" "1s" "infinite" {:timing "ease-out"})
    (circ)
    (atom)))
                
    
(def move-me
  (->>
   (gen-shape mint hept)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "10s" "infinite")
   (shape)
   (atom)))

(def move-me-2
 (->>
  (gen-shape (pattern (:id navy-lines)) hept)
  (style {:opacity .7 :transform-origin "center" :transform "scale(4.4)"})
  (anim "woosh" "10s" "infinite" {:delay ".1s"})
  (shape)
  (atom)))

(def move-me-3
  (->>
   (gen-shape mint hept)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh-2" "8s" "infinite")
   (shape)
   (atom)))

(def move-me-4
 (->>
  (gen-shape (pattern (:id navy-lines)) hept)
  (style {:opacity .7 :transform-origin "center" :transform "scale(4.4)"})
  (anim "woosh-2" "8s" "infinite" {:delay ".1s"})
  (shape)
  (atom)))
 
(def move-me-5
 (->>
  (gen-shape (pattern (:id yellow-dots)) oct)
  (style {:opacity .7 :transform-origin "center" :transform "translate(880%, 50%) scale(4.4)"})
  (anim "woosh-3" "6s" "infinite" {:delay ".1s"})
  (shape)
  (atom)))

(def move-me-6
 (->>
  (gen-shape (pattern (:id mint-dots)) hex)
  (style {:opacity .7 :transform-origin "center" :transform "translate(880%, 50%) scale(4.4)"})
  (anim "woosh-3" "6s" "infinite" {:delay ".2s"})
  (shape)
  (atom)))


(def move-me-7
 (->>
  (gen-shape (pattern (:id br-orange-lines)) hept)
  (style {:opacity .7 :transform-origin "center" :transform "translate(880%, 50%) scale(4.4)"})
  (anim "woosh-5" "6s" "infinite" {:delay ".5s"})
  (shape)
  (atom)))

(def move-me-8
 (->>
  (gen-shape (pattern (:id white-lines)) hept)
  (style {:opacity .7 :transform-origin "center" :transform "translate(0%, 50%) scale(4.4)"})
  (anim "woosh-7" "6s" "infinite" {:delay ".5s"})
  (shape)
  (atom)))

(def bg (->> 
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "32s" "1" {:timing "linear" :delay "7s"})
  (circ)
  (atom)))

(def bg-2 (->> 
  (gen-circ (pattern (str "noise-" blue)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "52s" "1" {:timing "linear" :delay "13s"})
  (circ)
  (atom)))

(def bg-3 (->> 
  (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "4s" "infinite" {:timing "linear"})
  (circ)
  (atom)))

(def bg-4 (->> 
  (gen-circ (pattern (str "noise-" blue)) (* .5 @width) (* .5 @height) 1800)
  (style {:opacity 1 :transform-origin "center" :transform "scale(4)"})
  (anim "sc-rot" "7s" "infinite" {:delay "3s"})
  (circ)
  (atom)))

(def fade-rect
  (->>
    (gen-rect navy 0 0 (* 0.5 @width) @height)
    (anim "new-fi" "4s" "1")
    (rect)
    (atom)))



;; ------------------- DRAWING HELPERS ------------------------


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

(def slide-lines
 (->>
   (gen-group {:style {:animation "fi 39s 1"}} 
              (gen-group {:style {:opacity .3 
                                  :filter (url (:id noiz)) 
                                  :transform "translateY(0%)" 
                                  :animation "slide-up 12s 1 38s ease-in"}}
                         (doall (map #(thin white 1 true %) (range 80)))))
   (atom)))

(defn hold-lines [frame]
  (gen-group {:style {:opacity .3 :filter (url (:id noiz)) :transform "translateY(0%)"}}
             (doall (map #(thin white frame (flicker-test % frame) %) (range 80)))))

 ;; ----------- COLLECTION SETUP AND CHANGE ----------------
(def n 1)

(defonce collection (atom (list)))
;(reset! ran {})

(defn cx [frame]
  (list

   
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;; BACKGROUNDS ;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    (let [colors [ 
      navy navy navy navy navy
      orange
      ;navy orange mint pink     
      ] ; orange navy mint pink gray white
        n (count colors)]
        (->>
          (gen-rect (nth colors (mod frame n)) 0 0 "100%" "100%")
          (style {:opacity .9})
          (rect)))

    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;; OPENING ANIMATIONS ;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


    (fade-and-hold :base-rect frame 5
      @fade-rect
      
      (->>
        (gen-rect navy 0 0 (* 0.5 @width) @height)
        (style {:opacity .5})
        (rect)
        (when (nth-frame 1 frame))))
    
    #_(fade-and-hold :bg1 frame 39
                   @bg
                   (->>
                     (gen-circ (pattern (str "noise-" navy)) (* .5 @width) (* .5 @height) 1800)
                     (style {:opacity 1 :transform-origin "center" :transform "scale(30) rotate(-80deg)"})
                     (circ)
                     (when (nth-frame 1 frame))))
  
    #_(fade-and-hold :bg2 frame 66
                  @bg-2
                  (->>
                    (gen-circ (pattern (str "noise-" blue)) (* .5 @width) (* .5 @height) 1800)
                    (style {:opacity 1 :transform-origin "center" :transform "scale(30) rotate(-80deg)"})
                    (circ)
                    (when (nth-frame 1 frame))))

    #_(fade-and-hold :lines frame 52
                   @slide-lines
                   (hold-lines frame))
    
  
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;; BELLS ;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;:::: USING POLY-MASK & POLY-MASK-2 :::;;:::::::::
    ;;;;;;:::::::::::; ANIM: WOOSH-6 ;:::;;::::::::::::::;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    
    
    
    #_(fade-and-hold :first-bell frame 38
      (gen-group {:mask (url "poly-mask")}
                         (when (nth-frame 1 frame)
                           (freak-out @width
                                      @height
                                      40
                                      100
                                      white
                                      {:opacity .5}))) 
                          
                          (when (nth-frame 1 frame)
                            (freak-out @width
                                       @height
                                       40
                                       100
                                       white
                                       {:opacity .5})))
    
    #_(fade-and-hold :second-bell frame 58
     (gen-group {:mask (url "poly-mask-2")}
                        (when (nth-frame 1 frame)
                          (freak-out @width
                                     @height
                                     40
                                     100
                                     pink
                                     {:opacity .5}))) 
                  
                         (when (nth-frame 1 frame)
                           (freak-out @width
                                      @height
                                      40
                                      100
                                      pink
                                      {:opacity .5})))
    


    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;; ENTER THE POLYGONS: ABOVE ;;;;;;;;;;;;;;
    ;;;;;;::::::: USING MOVE-ME MOVE-ME-2 ::::::;;:::::::::
    ;;;;;;::::::::::::; ANIM: WOOSH :;;:::;;::::::::::::::;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    
    ;@move-me
    ;@move-me-2
        
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;; POLYGONS GROW AND BELOW ABOVE ;;;;;;;;;;;;
    ;;;;;;::::: USING MOVE-ME-3 MOVE-ME-4 MOVE-ME-5 ::::::;
    ;;;;;;:::::::: ANIM: WOOSH-2, WOOSH-3 :;;:::;;:::::::::
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    
    ; @move-me-3
    ; @move-me-4
    ; 
    ; (when (nth-frame n frame)
    ;   (freak-out @width
    ;              @height
    ;              40
    ;              100
    ;              white
    ;              {:opacity .5}))
    ; 
    ; (when (nth-frame (* 2 n) frame)
    ;      (freak-out @width
    ;                 @height
    ;                 40
    ;                 100
    ;                 pink
    ;                 {:opacity .5}))
    ; 
    ; @move-me-5
    
    
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;; AWAY FROM POLYGONS TO GEOM SET ;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    
    ;(gen-bg-lines white 60)
    ;;(when (nth-frame 36 frame) (gen-bg-lines white 60))
    ;;(when (nth-frame 24 frame) (gen-bg-lines white 60))
    
    #_(list (->>
      (gen-rect mint (* 0.4 @width) 100 (* 0.4 @width) (* 0.85 @height)) 
      (style {:opacity .3})
      (rect)
      (when (nth-frame 2 frame))) 
      
      
    (->>
      (gen-rect orange (* 0.1 @width) 500 (* 0.8 @width) (* 0.35 @height)) 
      (style {:opacity .3})
      (rect)
      (when (nth-frame 3 frame)))

    (->>
      (gen-rect mint (* 0.15 @width) 10 (* 0.4 @width) (* 0.75 @height)) 
      (style {:opacity .5})
      (rect)
      (when (nth-frame 4 frame)))
    
    (->>
      (gen-circ navy (* 0.5 @width) (* 0.5 @height) 200 (url "grad-mask")) 
      (style {:transform-origin "center" :transform "rotate(280deg)"})
      (circ)
      (when (nth-frame 12 frame)))
    
    (->>
      (gen-circ white (* 0.5 @width) (* 0.5 @height) 200 (url "grad-mask")) 
      (style {:transform-origin "center" :transform "rotate(80deg)"})
      (circ)
      (when-not (nth-frame 12 frame))))
    
    

    
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;; !!!! TIME FOR CHAOS PT 1 !!!! ;;;;;;;;;;;
    ;;;;;;;;;;; USING BG-3 & BG-4 WITH SC-ROT ;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    
    ;(gen-bg-lines pink (mod (* 8 frame) 80))
    
    ;(when (nth-frame 2 frame) (gen-bg-lines pink 60))
    
    ;@bg-3
    ;@bg-4
    
    ;(when (nth-frame 4 frame) (gen-bg-lines pink 60))
    

    
    #_(list (->>
      (gen-rect mint (* 0.4 @width) 100 (* 0.4 @width) (* 0.85 @height)) 
      (style {:opacity .3})
      (rect)
      (when (nth-frame 2 frame))) 
      
      
    (->>
      (gen-rect orange (* 0.1 @width) 500 (* 0.8 @width) (* 0.35 @height)) 
      (style {:opacity .3})
      (rect)
      (when (nth-frame 3 frame))) 

    (->>
      (gen-rect mint (* 0.15 @width) 10 (* 0.4 @width) (* 0.75 @height)) 
      (style {:opacity .5})
      (rect)
      (when (nth-frame 4 frame))) 
          
    
    (->>
      (gen-circ navy (* 0.5 @width) (* 0.5 @height) 200 (url "grad-mask")) 
      (style {:transform-origin "center" :transform "rotate(280deg)"})
      (circ)
      (when (nth-frame 6 frame))) 
    
    (->>
      (gen-circ white (* 0.5 @width) (* 0.5 @height) 200 (url "grad-mask")) 
      (style {:transform-origin "center" :transform "rotate(80deg)"})
      (circ)
      (when-not (nth-frame 6 frame))) 
          
    (->>
      (gen-circ navy (* 0.5 @width) (* 0.5 @height) 200 (url "grad-mask")) 
      (style {:transform-origin "center" :transform "rotate(280deg)"})
      (circ)
      (when (nth-frame 2 frame)))
    
    (->>
      (gen-circ pink (* 0.5 @width) (* 0.5 @height) 200 (url "grad-mask")) 
      (style {:transform-origin "center" :transform "rotate(80deg)"})
      (circ)
      (when (nth-frame 2 frame))))
    
    
      
      ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
      ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
      ;;;;;;;;;;;; !!!! TIME FOR CHAOS PT 2 !!!! ;;;;;;;;;;;;
      ;;;;;;;;;;; USING MOVE-ME-5, 6, 7, 8 & DROPS ;;;;;;;;;;
      ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
      ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    

#_(list 
 (->>
  (gen-rect mint (* 0.4 @width) 100 (* 0.4 @width) (* 0.85 @height)) 
  (style {:opacity .3})
  (rect)
  (when (nth-frame 2 frame))) 
    
  (->>
    (gen-rect orange (* 0.1 @width) 500 (* 0.8 @width) (* 0.35 @height)) 
    (style {:opacity .3})
    (rect)
    (when (nth-frame 3 frame))) 

  (->>
    (gen-rect mint (* 0.15 @width) 10 (* 0.4 @width) (* 0.75 @height)) 
    (style {:opacity .5})
    (rect)
    (when (nth-frame 4 frame))))
    
    
    ;@move-me-8
    ;@move-me-7
    
    ;@move-me-5
    ;@move-me-6
    
    #_(when (nth-frame 8 frame)
      (freak-out @width
                 @height
                 60
                 200
                 white
                 {:opacity .6}))
    
                 
   #_(when (nth-frame 16 frame)
     (freak-out @width
                @height
                20
                200
                gray
                {:opacity 1}))
    
    #_(->>
      (gen-circ navy (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask")) 
      (style {:transform-origin "center" :transform "rotate(280deg)"})
      (circ)
      (when (nth-frame 1 frame))) 
    
    #_(->>
      (gen-circ pink (* 0.5 @width) (* 0.5 @height) 300 (url "grad-mask")) 
      (style {:transform-origin "center" :transform "rotate(80deg)"})
      (circ)
      (when (nth-frame 1 frame)))

         
    
    ;@drops
    ;@drops-2
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    ;; SAVE THESE TO REVIST FOR BRIDGE
    
     #_(gen-group {:mask (url "poly-mask-3")}
                (when (nth-frame 1 frame)
                  (freak-out @width
                             @height
                             20
                             40
                             mint)) 
                (when (nth-frame 1 frame)
                  (freak-out @width
                             @height
                             4
                             200
                             pink)))
     
     #_(gen-group {:mask (url "poly-mask")}
                (gen-bg-lines br-orange (mod frame 60)))
    
    
    
  )) ; cx end
  
  
;; ----------- LOOP AND DRAW ------------------------------

(defonce frame (atom 0))

(defonce start-cx-timer
  (js/setInterval
    #(reset! collection (cx @frame)) 50))

(defonce start-frame-timer
  (js/setInterval
    #(swap! frame inc) 500))
    

(def gradient
  [:linearGradient { :id "grad" }
      [:stop { :offset "0" :stop-color "white" :stop-opacity "0" }]
      [:stop { :offset "1" :stop-color "white" :stop-opacity "1" }]])
    
(def grad-mask 
  [:mask { :id "grad-mask" }
    [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 260 :fill "url(#grad)" }]])
  

(def masks [[:mask { :id "poly-mask" :key (random-uuid)}
              [:path {:d hept :fill "#fff" :style { :transform-origin "center" :animation "woosh-6 20s 2"}}]]
            [:mask { :id "poly-mask-2" :key (random-uuid)}
              [:path {:d oct :fill "#fff" :style { :transform-origin "center" :animation "woosh-6 20s 1 40s"}}]]
            [:mask { :id "poly-mask-3" :key (random-uuid)}
              [:path {:d oct :fill "#fff" :style { :transform-origin "center" :animation "woosh 20s infinite 5.2s"}} ]]
            [:mask { :id "poly-mask-5" :key (random-uuid)}
              [:path {:d oct :fill "#fff" :style { :transform-origin "center" :animation "woosh-4 4s infinite"}} ]]
            [:mask { :id "poly-mask-4" :key (random-uuid)}
              [:path {:d hept :fill "#fff" :style { :transform-origin "center" :transform "translate(280%, 250%) rotate(-200deg) scale(5.2)"}} ]]
            ])
  

(def all-filters [turb noiz soft-noiz disappearing splotchy blur])
(def all-fills [gray mint navy blue orange br-orange pink white yellow])

(defn drawing []
  [:svg { :width (:width settings) :height (:height settings) }
     ;; filters
    (map #(:def %) all-filters)
    ;; masks and patterns
    [:defs 
     gradient grad-mask noise
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
