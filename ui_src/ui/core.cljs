(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines split join]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct b1 b2 b3 b4]]
            [ui.fills :as fills :refer [pattern mint orange pink-squares]]
            [ui.animations :as animations :refer
              [ make-body
                splice-bodies
                make-frames
                trans
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
  [fill-string path transform]
  { :style { :fill fill-string
             :transform transform }
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
  [:g {:key (random-uuid)} (map (partial gen-offset-lines color 4 10) (range num))])


;; -------------------- SHAPE ANIMATION HELPER ---------------------------

(defn anim
  [shape name duration count delay timing]
  (let [animations
    { :animation-name name
      :animation-fill-mode "forwards"
      :animation-duration duration
      :animation-iteration-count count
      :animation-delay delay
      :animation-timing-function (or timing "ease")}]
          (update-in shape [:style] #(merge % animations))))

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

;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defonce collection (atom (list)))

(def fade-me (atom
  (poly
    (anim
      ((gen-ps (:id pink-squares)) hept (trans 0 0))
      "fade-out" "14s" 1 0 nil))))

(def me-too (atom
  (poly
    (anim
      ((gen-ss mint) hex (trans 0 0))
      "fade-out" "15s" "infinite" 0 nil))))

(def ac (atom
  (circ
    (anim ((gen-sc orange) 200 200 20)
    "wee-oo" "45s" "infinite" 0 nil))))

(defn cx [frame] (list
    #_(when-not (nth-frame 14 frame)
    (poly ((gen-ps (:id pink-squares)) hept (trans 10 40))))
  (poly ((gen-ps (:id pink-squares)) hex (trans 600 (mod (+ 10 frame) @height)) nil))
  @fade-me
  @me-too
  (gen-bg-lines mint (mod frame 60))
  (poly ((gen-ps (:id pink-squares)) hept (trans 10 40) nil))
  @ac

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
    #(swap! frame inc) 300))

(defn drawing []
  [:svg { :height (:width settings), :width (:height settings) }

    ;; eventually this should take in all the patterns
    [:defs (pattern pink-squares)]

    ;; then here dereference a state full of polys
    @collection ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))
