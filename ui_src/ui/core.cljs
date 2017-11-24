(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct b1 b2 b3 b4]]
            [ui.fills :as fills :refer [pattern mint navy orange pink pink-squares]]
            [ui.animations :as animations :refer
              [ make-body
                splice-bodies
                make-frames
                nth-frame
                even-frame
                odd-frame]]))

(enable-console-print!)

(println "Loaded.")

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

(defn gen-circ
  [fill-string x y radius]
  { :x x
    :y y
    :r radius
    :style { :fill fill-string }})

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

;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defonce collection (atom (list)))

(def fade-me
  (->>
    ((gen-ps (:id pink-squares)) hept)
    (anim "fade-out" "14s" "infinite")
    (poly)
    (atom)
  ))

(def ac
  (->>
    ((gen-sc orange) 200 200 20)
    (anim "wee-oo" "15s" "infinite")
    (circ)
    (atom)))

(def rot-rect
  (->>
    (gen-rect navy 400 200 80 10)
    (style {:transform-origin "center"})
    (anim "rot" "5s" "infinite")
    (rect)
    (atom)
    ))

(def rot-rect-2
  (->>
    (gen-rect pink 400 200 80 10)
    (style {:transform-origin "center"})
    (anim "rot" "7s" "infinite" { :delay ".5s"})
    (rect)
    (atom)
    ))

(defn abs
  [n]
  (max n (- n)))

(defn cx [frame]
  ;; write animation/atom helper and define those here
  (list
    #_(when-not (nth-frame 14 frame)
        (poly ((gen-ps (:id pink-squares)) hept (trans 10 40))))
  ; (println (abs (- @height (* 10 frame))))
  #_(rect (gen-rect navy 0 (if
    (< 0 (- @height (* 10 frame)))
      (- @height (* 10 (mod frame @height)))
      0) "100%" @height))
  (rect (gen-rect mint 0 0 "100%" "100%"))

  ; (when (nth-frame 2 frame) (circ ((gen-sc orange) 100 100 20)))
  ; (when (nth-frame 4 frame) (circ ((gen-sc orange) 600 450 60)))
  (when (nth-frame 2 frame) (circ ((gen-sc navy) 100 100 20)))
  @rot-rect
  @rot-rect-2
  ;(when (nth-frame 2 frame) (rect (gen-rect navy 400 200 80 10)))
  ;(when (nth-frame 6 frame) (circ ((gen-sc navy) 180 660 200)))
  ;(when (nth-frame 12 frame) (circ ((gen-sc orange) 40 700 200)))

  ;(poly ((gen-ps (:id pink-squares)) hex (trans 600 (mod (* 2 frame) @height)) nil))
  @fade-me
  ;(gen-bg-lines navy (mod frame 60))
  ;(gen-bg-lines mint (mod frame 60))
  ;(poly ((gen-ps (:id pink-squares)) hept (trans 10 40) nil))
  @ac
  (when (nth-frame 4 frame)(freak-out (/ @width 1) (/ @height 1) 200 10 pink))
  ;(poly ((gen-ps (:id pink-squares)) hex (trans 100 (mod (* 4 (+ 12 frame)) @height)) nil))


  ))


;; ----------- LOOP AND DRAW ------------------------------

(defonce frame (atom 0))

(defn frame-and-draw []
  (swap! frame inc)
  (reset! collection (cx @frame)))

(defonce start-cx-timer
  (js/setInterval
    #(reset! collection (cx @frame)) 50))

(defonce start-frame-timer
  (js/setInterval
    #(swap! frame inc) 500))

(defn drawing []
  [:svg { :height (:width settings), :width (:height settings) }

    ;; eventually this should take in all the patterns
    [:defs (pattern pink-squares)]

    ;; then here dereference a state full of polys
    @collection ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))
