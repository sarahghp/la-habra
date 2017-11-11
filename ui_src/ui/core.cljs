(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines]]))

(enable-console-print!)

(println "Loaded.")

;; settings
(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

(def settings {:width @width
               :height @height })


;; shape primitives
(def hept "M103.5 0 186.407601 37.9720691 206.884087 123.294534 149.510247 191.717838 57.4897527 191.717838 0.115912866 123.294534 20.5923992 37.9720691z")

;; fill primitives
;; patterns
(defn pattern
  [{ :keys [id image-link] }]
  [:pattern { :id id
              :patternUnits "userSpaceOnUse"
              :width "10"
              :height "10"}
    [:image { :xlinkHref image-link
              :x "0"
              :y "0"
              :width "10"
              :height "10" }]])

(def pink-squares "rect-circles-2")

;; solid colors

(defn gen-test-params
  [fill-id transform]
  { :class ""
    :style { :fill (str "url(#" fill-id ") #fff")
             :transform transform}
    :d hept
    :id fill-id
    :item-key (random-uuid)
    ;; break this into pattern
    :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nMS41JyBjeT0nMS41JyByPScxLjUnIGZpbGw9JyNmZjAwNDgnLz4KPC9zdmc+Cg=="
    })

(def test-params-01 (gen-test-params pink-squares "translate(100px, 100px)"))
(def test-params-02 (gen-test-params pink-squares "translate(0px, 0px)"))

(defn shape
  [{ :keys [class d style item-key] }]
  [:path { :key item-key :class class :d d :style style }])

(defn even-frame [frame] (= 0 (mod frame 2)))


;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defonce collection (atom (list)))

(defn cx [frame] (list
  (when (even-frame frame) (shape test-params-02))
  (shape test-params-01)
  ))


;; ----------- LOOP AND DRAW ------------------------------


(defonce frame (atom 0))

(defn frame-and-draw []
  (swap! frame inc)
  (reset! collection (cx @frame)))

(defonce start-timer
  (js/setInterval
    #(frame-and-draw) 500))

(defn drawing []
  [:svg { :height (:width settings), :width (:height settings) }
    ;; eventually this should take in all the patterns
    [:defs (pattern test-params-01)]

    ;; then here dereference a state full of shapes
    @collection ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))
