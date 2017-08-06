(ns ui.core
  (:require [reagent.core :as reagent :refer [atom]]
            [clojure.string :as string :refer [split-lines]]))

(enable-console-print!)

(println "Loaded.")

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

(def settings {:width @width
               :height @height
               :variation 12
               :stroke "#ff7d66"
               :stroke-width 2
               })

(def test-fill-id "rect-circles-2")

(defn gen-test-params
  [fill-id transform]
  { :class ""
    :style { :fill (str "url(#" fill-id ") #fff")
             :transform transform}
    :d "M103.5 0 186.407601 37.9720691 206.884087 123.294534 149.510247 191.717838 57.4897527 191.717838 0.115912866 123.294534 20.5923992 37.9720691z"
    :id fill-id
    :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nMS41JyBjeT0nMS41JyByPScxLjUnIGZpbGw9JyNmZjAwNDgnLz4KPC9zdmc+Cg==" })

(def test-params-01 (gen-test-params test-fill-id "translate(100px, 100px)"))
(def test-params-02 (gen-test-params test-fill-id "translate(0px, 0px)"))

(defn shape
  [{ :keys [class d style] }]
  [:path {:class class :d d :style style}])

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

(def show? (atom false))

(defonce show-updater
  (js/setInterval
    #(swap! show? not)
    500))

(defn drawing []
  ;; add event listener for when width changes
  [:svg { :height (:width settings), :width (:height settings) }
    [:defs (pattern test-params-01)]
    (shape test-params-01)
    (when (= true @show?) (shape test-params-02))])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))
