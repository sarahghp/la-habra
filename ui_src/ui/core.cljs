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

;; ------------------------ GENERATORS ---------------------

(defn poly
  [{ :keys [d style mask] :or {mask ""} } ]
  [:path { :key (random-uuid) :d d :style style :mask mask }])

(defn circ
  [{:keys [x y r style mask] :or {mask ""} }]
  [:circle { :cx x
             :cy y
             :r r
             :style style
             :mask mask
             :key (random-uuid)} ])

(defn rect
 [{:keys [x y w h style] }]
 [:rect { :x x
          :y y
          :width w
          :height h
          :style style
          :key (random-uuid)} ])

(defn gen-rect
  [fill-string x y w h]
  { :x x
    :y y
    :w w
    :h h
    :style {
      :fill fill-string }})
      
(defn gen-sr
  [fill]
  (partial gen-rect fill))

(defn gen-pr
  [fill-id]
  (partial gen-rect (str "url(#" fill-id ") #fff")))

(defn gen-circ
  [fill-string x y radius mask]
  { :x x
    :y y
    :r radius
    :mask mask
    :style { :fill fill-string }})
    
(defn gen-nc
  [filter-id x y radius]
  { :x x
    :y y
    :r radius
    :style { :fill (str "url(#" filter-id ") #fff") }})

(defn gen-sc
  [fill]
  (partial gen-circ fill))

(defn gen-pc
  [fill-id]
  (partial gen-circ (str "url(#" fill-id ") #fff")))

(defn gen-shape
  [fill-string path mask]
  { :style { :fill fill-string }
    :mask mask
    :d path })

(defn gen-ps ;; makes pattern shape
  [fill-id]
  (partial gen-shape (str "url(#" fill-id ") #fff")))

(defn gen-ss ;; makes solid shape
  [fill]
  (partial gen-shape fill))

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
  [x y r num color]
  [:g {:key (random-uuid)} (map #(circ ((gen-sc color) (rand x) (rand y) (rand r))) (range num))])

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
     "translate(80%, 50%) rotate(-200deg) scale(4.4)"
     "translate(80%, 50%) rotate(120deg) scale(8.4)"
     "translate(80%, 50%) rotate(-210deg) scale(15.2)"
     "translate(80%, 50%) rotate(400deg) scale(12)"
     ]))
     
     (make-frames
       "woosh-2"
         [10, 35, 55, 85, 92]
        (make-body "transform" [
          "translate(480%, 50%) rotate(2deg) scale(1.2)"
          "translate(480%, 50%) rotate(-200deg) scale(4.4)"
          "translate(480%, 50%) rotate(120deg) scale(8.4)"
          "translate(480%, 50%) rotate(-210deg) scale(15.2)"
          "translate(480%, 50%) rotate(400deg) scale(12)"
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
    ((gen-sc white) 0 100 40)
    (style {:opacity .7})
    (anim "bloop-x" "1s" "infinite" {:timing "ease-out"})
    (circ)
    (atom)))
                
    
(def move-me
  (->>
   ((gen-ss orange) hept)
   (style {:opacity .2 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "4s" "infinite")
   (poly)
   (atom)))
   
   
(def bg (->> 
  (gen-nc "noise" (* .5 @width) (* .5 @height) 1800)
  (style {:opacity .4 :transform-origin "center" :transform "scale(10)"})
  (anim "scaley" "20s" "infinite")
  (circ)
  (atom)))

;; ------------------- DRAWING HELPERS ------------------------


(defn thin
  [color frame flicker? n]
  (let [op (if (and (nth-frame 4 frame) flicker?) (rand) 1)]
    (->>
     ((gen-sr color) (* 0.15 @width) (* 0.15 @height) (* 0.7 @width) 3)
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
            ((gen-pr (:id (nth patterns (mod frame n)))) 0 0 "100%" "100%")
            (style {:transform "scale(1.2)" :opacity .4})
            (rect)
          ))


       
      

        (->>
         ((gen-sr orange) (* 0.25 @width) (* 0.55 @height) 600 800)
         (style {:opacity .2})
         (rect)
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
    [:path {:d "M103.5 0 186.407601 37.9720691 206.884087 123.294534 149.510247 191.717838 57.4897527 191.717838 0.115912866 123.294534 20.5923992 37.9720691z" :fill "url(#grad)"} ]
  ])

(def clippy 
  [:clipPath { :id "clippy" } [:circle { :cx "50%" :cy "50%" :r "8%"  :style { :fill "#ffffff" } }]])


(defn drawing []
  [:svg { :width (:width settings) :height (:height settings) }
    (:def turb)
    (:def noiz)
    (:def soft-noiz)
    [:defs gradient grad-mask poly-mask clippy
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
