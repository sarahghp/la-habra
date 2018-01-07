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
               pink-stripes
               gray-circs
               gray-circs-lg]]
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
    ((gen-sc orange) 200 200 20)
    (anim "wee-oo" "5s" "infinite")
    (circ)
    (atom)))

(def rot-rect
  (->>
    (gen-rect navy 60 500 160 20)
    (style {:transform-origin "center"})
    (anim "rot" "5s" "infinite")
    (rect)
    (atom)))

;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defonce collection (atom (list)))

(make-frames
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
     "translate(80%, 50%) rotate(2deg) scale(1.2)"
     "translate(80%, 50%) rotate(100deg) scale(4.4)"
     "translate(80%, 50%) rotate(194deg) scale(10.4)"
     "translate(80%, 50%) rotate(210deg) scale(5.2)"
     "translate(80%, 50%) rotate(400deg) scale(1)"
     ]))
     
     (make-frames
       "woosh-2"
         [10, 35, 55, 85, 92]
        (make-body "transform" [
          "translate(500px, 50%) rotate(2deg) scale(2.2)"
          "translate(500px, 50%) rotate(100deg) scale(6.4)"
          "translate(500px, 50%) rotate(194deg) scale(12.4)"
          "translate(500px, 50%) rotate(210deg) scale(6.2)"
          "translate(500px, 50%) rotate(400deg) scale(1)"
          ]))
          
          (make-frames
            "woosh-2"
              [10, 35, 55, 85, 92]
             (make-body "transform" [
               "translate(500px, 600px) rotate(2deg) scale(2.2)"
               "translate(500px, 600px) rotate(100deg) scale(6.4)"
               "translate(500px, 600px) rotate(194deg) scale(12.4)"
               "translate(500px, 600px) rotate(210deg) scale(6.2)"
               "translate(500px, 600px) rotate(400deg) scale(1)"
               ]))
  
  ; 500px, 50% // 500px, 600px // 2 6 12 6 1
  





(def move-me
  (->>
   ((gen-ps (:id gray-circs-lg)) hept)
   (style {:transform-origin "center" :transform "scale(1.4)"})
   (anim "woosh" "20s" "infinite")
   (poly)
   (atom)))
   
   (def move-me-2
     (->>
      ((gen-ps (:id gray-circs-lg)) hept)
      (style {:transform-origin "center" :transform "scale(1.4)"})
      (anim "woosh-2" "22s" "infinite")
      (poly)
      (atom)))
      
      (def move-me-3
        (->>
         ((gen-ps (:id pink-circs) hex)
         (style {:transform-origin "center" :transform "scale(1.4)"})
         (anim "woosh-3" "18s" "infinite")
         (poly)
         (atom)))
         
         (def move-me-4
           (->>
            ((gen-ps (:id pink-circs) hept)
            (style {:transform-origin "center" :transform "scale(1.4)"})
            (anim "woosh-3" "42s" "infinite")
            (poly)
            (atom)))

; gray-circs-lg hept woosh



(defn cx [frame]
  (list


  #_(let [h (settings :height)]
    (->>
      (gen-rect
        navy 0 (- h (mod (* 2 frame) (* 10 h))) 
        "100%" h)
      (rect)
        ))

  (let [colors [ gray gray gray gray ] ; orange navy mint pink gray white
        n (count colors)]
        (->>
          (gen-rect (nth colors (mod frame n)) 0 0 "2000%" "2000%")
          (style {:opacity .7})
          (rect)
        ))

  (->>
   (gen-rect navy 60 510 180 20)
   (style {:transform-origin "center ":transform "rotate(-30deg)"})
   (when (nth-frame 3 frame)) 
   (rect))

 (->>
  (gen-rect navy 80 540 180 20)
  (style {:transform-origin "center ":transform "rotate(-30deg)"})
  (when (nth-frame 3 frame)) 
  (rect))
  
  (->>
   (gen-rect navy 560 210 180 20)
   (style {:transform-origin "center ":transform "rotate(-30deg)"})
   (when (nth-frame 4 frame)) 
   (rect))

 (->>
  (gen-rect navy 580 240 180 20)
  (style {:transform-origin "center ":transform "rotate(-30deg)"})
  (when (nth-frame 4 frame)) 
  (rect))
  
  
  
  (->>
   ((gen-sc mint) 400 400 100)
   (circ)
   (when (nth-frame 4 frame)))
   
   (->>
    ((gen-sc mint) 480 240 60)
    (circ)
    (when (nth-frame 6 frame)))
    

   
   @move-me
  @move-me-2
   
   (->>
    ((gen-pc (:id pink-stripes)) 400 100 60)
   (style {:transform "rotate(10deg)"})
    (circ)
    (when (nth-frame 4 frame)))
    
  
   @move-me-4
   
   (when (nth-frame 12 frame)
     (freak-out @width
                @height
                20
                200
                white))



  )) ; cx end


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
    [:defs (map pattern [ blue-circs
                          pink-circs
                          pink-stripes
                          gray-circs
                          gray-circs-lg])]

    ;; then here dereference a state full of polys
    @collection ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))

(hide-display)
