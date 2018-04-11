(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct b1 b2 b3 b4]]
            [ui.fills :as fills :refer
              [ gray
                mint
                navy
                orange
                br-orange
                pink
                white]]
            [ui.patterns :as patterns :refer
             [ pattern
               blue-circs
               pink-circs
               pink-lines
               gray-circs
               gray-circs-lg
               gray-lines
               gray-patch
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
  [{ :keys [d style] }]
  [:path { :key (random-uuid) :d d :style style }])

(defn circ
  [{:keys [x y r style] }]
  [:circle { :cx x
             :cy y
             :r r
             :style style
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
  [fill-string x y radius]
  { :x x
    :y y
    :r radius
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
  [fill-string path]
  { :style { :fill fill-string }
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
  [color num]
  [:g {:key (random-uuid)} (map (partial gen-offset-lines color 1 4) (range num))])

(defn freak-out
  [x y r num color]
  [:g {:key (random-uuid)} (map #(circ ((gen-sc color) (rand x) (rand y) (rand r))) (range num))])

;;(defn gen-offsets)

(defn gen-grid
  ;;([offset base-obj] (gen-grid (gen-offsets offset base-obj) offset base-obj)) 
  ([cols rows offset base-obj]
    (let [x (base-obj :x) 
          y (base-obj :y)
          a-off (offset :col)
          b-off (offset :row)]
            (set (for [a (range cols) b (range rows)]
              (merge base-obj {:x (+ x (* a a-off))} {:y (+ y (* b b-off))}))))))
              
#_(defn gen-grid
  ;;([offset base-obj] (gen-grid (gen-offsets offset base-obj) offset base-obj)) 
  ([cols rows offset base-obj]
    (let [x (base-obj :x) 
          y (base-obj :y)
          a-off (offset :col)
          b-off (offset :row)]
            (map (fn [a b] (merge base-obj {:x (+ x (* a a-off))} {:y (+ y (* b b-off))})) (range cols) (range rows)) 
            )))
  
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

(make-frames "atob" [0 100] (make-body "transform" ["translate(100px, 0px) scale(1.4)" "translate(800px, 400px) scale(4.2)"]))
(make-frames "ctod" [0 100] (make-body "transform" ["translate(1000px, 0px)" "translate(200px, 500px)"]))
(make-frames "etof" [0 100] (make-body "transform" ["translateY(60px)" "translateY(800px)"]))


(def fade-me
  (->>
    ((gen-ps (:id pink-circs)) hept)
    (anim "fade-out" "14s" "infinite")
    (poly)
    (atom)
  ))

(def drops
(atom  (map
   #(->>
     (gen-rect navy (+ 60 (* % 160)) 60 100 24)
     (anim "etof" "1.6s" "infinite" {:delay (str ".01" % "s")})
     (rect))
   (range 5))))

(def ac
  (->>
    ((gen-sc orange) 200 200 100)
    (anim "wee-oo" "5s" "infinite")
    (circ)
    (atom)))
    
(def rot-rect
  (->>
    (gen-rect mint 60 500 360 60)
    (style {:transform-origin "center"})
    (anim "rot" "1s" "infinite")
    (rect)
    (atom)))
    
(def rot-rect-2
  (->>
    (gen-rect mint 560 700 360 60)
    (style {:transform-origin "center"})
    (anim "rot" "1s" "infinite")
    (rect)
    (atom)))
    
(def rot-rect-3
  (->>
    (gen-rect mint 160 200 360 60)
    (style {:transform-origin "center"})
    (anim "rot" "1s" "infinite")
    (rect)
    (atom)))
    
(def rot-rect-4
  (->>
    (gen-rect mint 460 100 360 60)
    (style {:transform-origin "center"})
    (anim "rot" "1s" "infinite")
    (rect)
    (atom)))
    
(def rot-o
  (->>
    ((gen-ps (:id blue-circs)) oct)
    (style {:transform-origin "center"})
    (anim "cent-rot" "1s" "infinite")
    (poly)
    (atom)))

;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defonce collection (atom (list)))

(make-frames
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
     "translate(80%, 50%) rotate(2deg) scale(11.2)"
     "translate(80%, 50%) rotate(100deg) scale(14.4)"
     "translate(80%, 50%) rotate(194deg) scale(110.4)"
     "translate(80%, 50%) rotate(210deg) scale(5.2)"
     "translate(80%, 50%) rotate(400deg) scale(1)"
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

(def move-me
  (->>
   ((gen-ps (:id blue-circs)) hept)
   (style {:transform-origin "center" :transform "scale(1.4)"})
   (anim "woosh" "4s" "infinite")
   (poly)
   (atom)))
   
   
(def creepy (->>
  ((gen-ps (:id pink-lines)) hex)
  (style {:transform-origin "center" :transform "translate(280%, 750%)"})
  (anim "creep" "14s" "infinite")
  (poly)
  (atom)))

(defn cx [frame]
  (list

  (let [colors [ navy navy navy navy navy navy navy navy ] ; orange navy mint pink gray white
        n (count colors)]
        (->>
          (gen-rect (nth colors (mod frame n)) 0 0 "100%" "100%")
          (style {:opacity .7})
          (rect)
        ))
        
  (->>
    (gen-grid
      40 1
      {:col 40 :row 40}
      (gen-rect navy 10 0 2 @height)) 
     (map #(style {:opacity .5} %)) 
     (map rect) 
     (when-not (nth-frame 24 frame)))
     
   (->>
     (gen-grid
       1 40
       {:col 40 :row 40}
       (gen-rect navy 10 0 @width 2)) 
      (map #(style {:opacity .5} %)) 
      (map rect) 
      (when (nth-frame 24 frame)))

  (->> 
    (gen-nc "noise" (* .5 @width) (* .5 @height) 200)
    (style {:opacity .5})
    (circ)
    (when (nth-frame 6 frame)))
    
    (->> 
      (gen-nc "noise" (* .75 @width) (* .15 @height) 80)
      (style {:opacity .5})
      (circ)
      (when (nth-frame 12 frame)))
      
      @move-me
    
  (when (nth-frame 24 frame)
    (freak-out @width
               @height
               20
               200
               pink))

              

  )) ; cx end

;; ----------- LOOP AND DRAW ------------------------------

(defonce frame (atom 0))

(defonce start-cx-timer
  (js/setInterval
    #(reset! collection (cx @frame)) 50))

(defonce start-frame-timer
  (js/setInterval
    #(swap! frame inc) 500))

(defn drawing []
  [:svg { :width (:width settings), :height (:height settings) }

    ;; eventually this should take in all the patterns
    [:defs (noise) (map pattern [ blue-circs
                          pink-circs
                          pink-lines
                          gray-circs
                          gray-circs-lg
                          gray-lines
                          gray-patch
                          shadow])]

    ;; then here dereference a state full of polys
    @collection ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))

;(hide-display)
