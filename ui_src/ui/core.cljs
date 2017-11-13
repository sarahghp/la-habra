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


;; ------------------------ PRIMITIVES ---------------------

;; shape primitives

;; polygons
(def tri "M92.0428016 0 183.878562 151.279046 0.207041521 151.279046z")
(def square "M106.042802 0 212.085603 100.852697 106.042802 201.705395 0 100.852697z")
(def pent "M101.042802 0 201.895499 69.6875 163.373196 182.444244 38.7124067 182.444244 0.190104131 69.6875z")
(def hex "M92.0428016 0 183.878562 50.4263487 183.878562 151.279046 92.0428016 201.705395 0.207041521 151.279046 0.207041521 50.4263487z")
(def hept "M103.5 0 186.407601 37.9720691 206.884087 123.294534 149.510247 191.717838 57.4897527 191.717838 0.115912866 123.294534 20.5923992 37.9720691z")
(def oct "M106.042802 0 181.026386 29.5390712 212.085603 100.852697 181.026386 172.166324 106.042802 201.705395 31.0592175 172.166324 0 100.852697 31.0592175 29.5390712z")

;; beans
(def b1 "M50.0555552,84.5712327 C162.680555,-5.60064227 207.735229,-42.6318923 323.657104,72.3290452 C439.578979,187.289983 416.44618,353.711858 286.086805,318.352483 C155.72743,282.993107 50.0555565,332.133733 50.0555565,332.133733 C50.0555565,332.133733 -62.5694448,174.743108 50.0555552,84.5712327 L50.0555552,84.5712327 Z")
(def b2 "M519.09375,484.882814 C451.984379,569.992191 171.499687,526.703126 121.296563,500.515625 C71.0934394,474.328124 -69.2971885,310.609372 41.5778129,231.585937 C152.452814,152.562503 188.921562,224.437497 267.16375,176.054687 C345.405939,127.671878 334.366874,3.17972922e-06 410.07,4.54747351e-13 C485.773126,-3.17972854e-06 586.203121,399.773438 519.09375,484.882814 L519.09375,484.882814 Z")
(def b3 "M17.9546804,30.1258588 C42.7694777,-2.6260035 51.5623779,0.0529521793 78.3358154,0.0529521793 C105.109253,0.0529521793 131.04553,-2.63044637 157.335815,40.3498272 C209.167302,125.085746 204.119181,108.491012 131.929565,115.54514 C59.7399501,122.599268 39.1561305,145.490452 39.1561305,145.490452 C39.1561305,145.490452 -32.4026937,96.5901449 17.9546804,30.1258588 L17.9546804,30.1258588 Z")
(def b4 "M35.6188634,28.7213441 C85.9762375,-37.742942 261.171725,20.8619855 313.003212,105.597904 C364.834699,190.333822 230.42145,197.804199 158.231835,204.858327 C86.0422195,211.912455 3.04826479,81.1145179 3.04826479,81.1145179 C3.04826479,81.1145179 -14.7385108,95.1856303 35.6188634,28.7213441 L35.6188634,28.7213441 Z")

;; fill primitives

;; solid colors

;; patterns

(def pink-squares { :id "rect-circles-2"
                    :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nMS41JyBjeT0nMS41JyByPScxLjUnIGZpbGw9JyNmZjAwNDgnLz4KPC9zdmc+Cg==" })


;; ------------------------ GENERATORS ---------------------

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

(defn shape
  [{ :keys [class d style item-key] }]
  [:path { :key item-key :class class :d d :style style }])

(defn gen-ps ;; makes pattern shape
  [fill-id path transform class]
  { :class (or class "")
    :style { :fill (str "url(#" fill-id ") #fff")
             :transform transform }
    :d path
    :item-key (random-uuid)})

;; ------------------------------------------------------ ;;
;; --------------------- ANIMATIONS --------------------- ;;
;; ------------------------------------------------------ ;;


;; -------------------- MOVING ---------------------------

(defn trans [x y] (str "translate(" x "px, " y "px)"))


;; ------------------- BLINKING ---------------------------

(defn frame-mod [val n frame] (= val (mod frame n)))
(def nth-frame (partial frame-mod 0))
(defn even-frame [frame] (nth-frame 2 frame))
(defn odd-frame [frame] (frame-mod 1 2 frame))

;; -------------- FADING & INTERPOLATION  -----------------
;; (println (js-keys (aget js/document "styleSheets" "0")))

;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defonce collection (atom (list)))

(def fade-me (atom (shape (gen-ps (:id pink-squares) tri (trans 100 100) "fade-o"))))

(defn cx [frame] (list
  (when (even-frame frame)
    (shape (gen-ps (:id pink-squares) hept (trans 10 40) nil)))
  ; (shape (gen-ps (:id pink-squares) tri (trans 100 100) "fade-o"))
  ;@fade-me

  ))


;; ----------- LOOP AND DRAW ------------------------------

(defonce frame (atom 0))

(defn frame-and-draw []
  (swap! frame inc)
  (reset! collection (cx @frame)))

(defonce start-cx-timer
  (js/setInterval
    #(reset! collection (cx @frame)) 20))

(defonce start-frame-timer
  (js/setInterval
    #(swap! frame inc) 500))

(defn drawing []
  [:svg { :height (:width settings), :width (:height settings) }

    ;; eventually this should take in all the patterns
    [:defs (pattern pink-squares)]

    ;; then here dereference a state full of shapes
    @collection ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))
