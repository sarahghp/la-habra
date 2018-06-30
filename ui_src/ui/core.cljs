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
            [ui.filters :as filters :refer [filt turb noiz soft-noiz]]
            [ui.patterns :as patterns :refer
             [ pattern
               pattern-def
               blue-dots
               blue-lines
               pink-dots
               pink-lines
               gray-dots
               gray-dots-lg
               gray-lines
               gray-patch
               navy-dots
               navy-lines
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
              
  (defn gen-shadow
    [offset base-obj]
    (let [x (base-obj :x)
          y (base-obj :y)
          x-off (offset :x)
          y-off (offset :y)
          fill-id (:id shadow)
          shadow-obj (merge base-obj 
            {:x (+ x x-off)} 
            {:y (+ y y-off)}
            {:style {:opacity .7 :fill (str "url(#" fill-id ") #fff")}})]
      (list shadow-obj base-obj)))

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

(make-frames "etof" [0 100] (make-body "transform" ["translateY(60px)" "translateY(800px)"]))

(back-and-forth! "scaley" "scale(1)" "scale(15)")

(make-frames
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
     "translate(80%, 50%) rotate(2deg) scale(1.2)"
     "translate(380%, 350%) rotate(-200deg) scale(2.4)"
     "translate(280%, 450%) rotate(120deg) scale(3.4)"
     "translate(180%, 250%) rotate(-210deg) scale(5.2)"
     "translate(80%, 50%) rotate(400deg) scale(6.2)"
     ]))
     
(make-frames
 "woosh-2"
   [10, 35, 55, 85, 92]
  (make-body "transform" [
    "translate(480%, 50%) rotate(2deg) scale(1.2)"
    "translate(480%, 50%) rotate(-200deg) scale(4.4)"
    "translate(480%, 50%) rotate(120deg) scale(8.4)"
    "translate(480%, 50%) rotate(-210deg) scale(10.2)"
    "translate(480%, 50%) rotate(400deg) scale(4)"
    ]))
    
(make-frames
 "woosh-3"
   [10, 35, 55, 85, 92]
  (make-body "transform" [
    "translate(480%, 50%) rotate(2deg) scale(1.2)"
    "translate(180%, 150%) rotate(-200deg) scale(4.4)"
    "translate(80%, 250%) rotate(120deg) scale(3.4)"
    "translate(0%, 300%) rotate(-210deg) scale(4.2)"
    "translate(280%, 150%) rotate(400deg) scale(8)"
    ]))

(make-frames
  "creep"
  [10, 25, 40, 80, 91]
  (make-body "transform" [
    "translate(280%, 750%) scale(5)"
    "translate(280%, 650%) scale(5)"
    "translate(280%, 450%) scale(5)"
    "translate(280%, 250%) scale(5)"
    "translate(280%, 250%) scale(5)"
    ]))
    
(make-frames
  "bloop-x"
  [0 48 100]
  (make-body "transform" [
    (str "translateX(0px)")
    (str "translateX(400px)")
    (str "translateX(1000px)")
  ]))

;; --------------- ATOMS STORAGE --------------------

(def drops
  (atom  (map
     #(->>
       (gen-rect mint (+ 30 (* % 160)) 60 100 24)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (rect))
     (range 6))))
     
 (def drops-2
   (atom  (map
      #(->>
        (gen-rect white (+ 30 (* % 160)) 60 100 24)
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
   (gen-shape orange hept)
   (style {:opacity .2 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "4s" "infinite")
   (shape)
   (atom)))
   
   
(def bg (->> 
  (gen-circ (pattern "noise") (* .5 @width) (* .5 @height) 1800)
  (style {:opacity .4 :transform-origin "center" :transform "scale(10)"})
  (anim "scaley" "20s" "infinite")
  (circ)
  (atom)))

;; ------------------- DRAWING HELPERS ------------------------


(defn thin
  [color frame flicker? n]
  (let [op (if (and (nth-frame 4 frame) flicker?) (rand) 1)]
    (->>
     (gen-rect color (* 0.15 @width) (* 0.15 @height) (* 0.7 @width) 3)
     (style {:transform (str "translateY(" (* n 10) "px)") :opacity op})
     (rect)
     (when (nth-frame 1 frame)))))
     

 ;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defonce collection (atom (list)))

(defn cx [frame]
  (list

    (let [colors [ 
      mint mint mint mint mint mint 
      ;navy navy navy navy navy navy 
      ] ; orange navy mint pink gray white
          n (count colors)]
          (->>
            (gen-rect (nth colors (mod frame n)) 0 0 "100%" "100%")
            (style {:opacity .5})
            (rect)
          ))

    (let [patterns [ 
            ;gray-dots gray-dots gray-dots gray-dots gray-dots gray-dots
            ;gray-dots gray-dots gray-dots gray-dots gray-dots gray-dots 
            white-dots white-dots white-dots white-dots white-dots white-dots
            white-dots white-dots white-dots white-dots white-dots white-dots ] ; orange navy mint pink gray white
          n (count patterns)]
          (->>
            (gen-rect (pattern (:id (nth patterns (mod frame n)))) 0 0 "100%" "100%")
            (style {:transform "scale(1.2)" :opacity .4})
            (rect)
          ))
  
                      
  (gen-group { :mask "url(#poly-mask)" } 
    (gen-bg-lines orange (mod frame 60))
    (when (nth-frame 1 frame)
      (freak-out @width
               @height
               80
               40
               mint))
               
     (freak-out @width
              @height
              20
              40
              pink))
    
  (gen-group { :mask "url(#poly-mask-3)" } 
    (gen-bg-lines navy (mod (* 2 frame) 80)))
  
  (gen-group { :mask "url(#poly-mask-2)" } 
    (->> 
      (gen-circ (pattern "noise") (* .5 @width) (* .5 @height) 1800)
      (style {:opacity .4 :transform-origin "center" :transform "scale(10)"})
      (circ)))
                  
  #_(->>
   (gen-circ navy (* 0.5 @width) (* 0.5 @height) 200)
   (style {:opacity .7 })
   (circ)
   (when (nth-frame 1 frame)))
  
  (->>
    (gen-circ white (* 0.5 @width) (* 0.5 @height) 200 "url(#grad-mask)")
    (style {:opacity .7 :transform-origin "center" :transform "rotate(65deg)" })
    (circ)
    (when (nth-frame 1 frame)))
  

  )) ; cx end
  
  
;; ----------- LOOP AND DRAW ------------------------------

(defonce frame (atom 0))

; should I replace with requestAnimationFrame?
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
    [:circle { :cx (* 0.5 @width) :cy (* 0.5 @height) :r 200 :fill "url(#grad)" }]
  ])
  
  
(def poly-mask 
  [:mask { :id "poly-mask" }
    [:path {:d hept :fill "#fff" :style { :transform-origin "center" :animation "woosh-2 10s infinite"}} ]
])

(def poly-mask-2
  [:mask { :id "poly-mask-2" }
    [:path {:d oct :fill "#fff" :style { :transform-origin "center" :animation "woosh-3 3s infinite"}} ]
])

(def poly-mask-3
  [:mask { :id "poly-mask-3" }
    [:path {:d oct :fill "#fff" :style { :transform-origin "center" :animation "woosh 5s infinite"}} ]
])



(defn drawing []
  [:svg { :width (:width settings) :height (:height settings) }
    (:def turb)
    (:def noiz)
    (:def soft-noiz)
    [:defs gradient grad-mask poly-mask poly-mask-2 poly-mask-3
           (noise) 
           ;; eventually this should take in all the patterns
           (map pattern-def [ blue-dots
                              blue-lines
                              pink-dots
                              pink-lines
                              gray-dots
                              gray-dots-lg
                              gray-lines
                              gray-patch
                              navy-dots
                              navy-lines
                              yellow-dots
                              yellow-lines
                              white-dots
                              white-dots-lg
                              white-lines
                              shadow
                              noise ])]

    ;; then here dereference a state full of polys
    @collection 
    ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))

;(hide-display)
