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
(println "*" (filt turb))

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

(make-frames "atob" [0 100] (make-body "transform" ["translate(100px, 0px) scale(1.4)" "translate(800px, 400px) scale(4.2)"]))
(make-frames "ctod" [0 100] (make-body "transform" ["translate(1000px, 0px)" "translate(200px, 500px)"]))
(make-frames "etof" [0 100] (make-body "transform" ["translateY(60px)" "translateY(800px)"]))

(defn back-and-forth!
  [name start-str finish-str]
  (make-frames name [0 50 100] 
    (make-body "transform" [
      (str start-str)
      (str finish-str)
      (str start-str)])))

(back-and-forth! "bf1" "scale(100.5) rotate(270deg)" "scale(10.5) rotate(0deg)")
(back-and-forth! "gg1" "scale(1.5)" "scale(4.5)")
(back-and-forth! "gg2" "translateY(0%)" "translateY(4200%)")

(def drops
  (atom  (map
     #(->>
       (gen-rect mint (+ 30 (* % 160)) 60 100 24)
       (anim "etof" "1.2s" "infinite" {:delay (str (* .5 %) "s")})
       (rect))
     (range 5))))
     
 (def drops-2
   (atom  (map
      #(->>
        (gen-rect navy (+ 30 (* % 160)) 60 100 24)
        (anim "etof" "1.2s" "infinite" {:delay (str (* .7 %) "s")})
        (rect))
      (range 5))))


;; ----------- COLLECTION SETUP AND CHANGE ----------------

(defonce collection (atom (list)))

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
   [10, 35, 45, 55, 85, 92]
  (make-body "transform" [
    "translate(800px, 50%) rotate(2deg) scale(1.2)"
    "translate(400px, 50%) rotate(-200deg) scale(4.4)"
    "translate(-200px, 150%) rotate(400deg) scale(8.4)"
    "translate(-1000px, 250%) rotate(210deg) scale(8.2)"
    "translate(550px, 250%) rotate(400deg) scale(4.2)"
    "translate(800px, 250%) rotate(400deg) scale(10.2)"
    ]))
    
(make-frames
 "woosh-3"
   [10, 35, 45, 55, 85, 92]
  (make-body "transform" [
    "translate(1000px, 300%) rotate(2deg) scale(1.2)"
    "translate(1000px, 200%) rotate(-200deg) scale(4.4)"
    "translate(0px, 100%) rotate(400deg) scale(6.4)"
    "translate(0px, 50%) rotate(210deg) scale(18.2)"
    "translate(1000px, 100%) rotate(400deg) scale(6.2)"
    "translate(1000px, 300%) rotate(400deg) scale(4.2)"
    ]))
    
    
(make-frames
 "woosh-4"
   [10, 35, 45, 55, 85, 92]
  (make-body "transform" [
    "translate(-1000px, 300%) rotate(200deg) scale(1.2)"
    "translate(-1000px, 200%) rotate(0deg) scale(4.4)"
    "translate(800px, 50%) rotate(-360deg) scale(8.4)"
    "translate(800px, 0%) rotate(0deg) scale(18.2)"
    "translate(0px, 100%) rotate(100deg) scale(10.2)"
    "translate(-1000px, 300%) rotate(400deg) scale(4.2)"
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
      

 
 
 

    
  


(def bloops
  (->>
    ((gen-sc white) 0 100 40)
    (anim "bloop-x" "1s" "infinite" {:timing "ease-out"})
    (circ)
    (atom)))
    
(def bloops2
  (->>
    ((gen-sc white) 0 200 80)
    (anim "bloop-x" "2s" "infinite" {:timing "ease-out"})
    (circ)
    (atom)))

(def bloops3
  (->>
    ;((gen-sc white) 0 260 40)
    ((gen-pc (:id navy-dots)) 0 260 40)
    (anim "bloop-x" ".5s" "infinite" {:timing "ease-out"})
    (circ)
    (atom)))

(def bloops4
  (->>
    ((gen-sc white) -80 400 40)
    (anim "bloop-x" "1s" "infinite" {:delay ".7s" :timing "ease-out"})
    (circ)
    (atom)))
    
(def bloops5
  (->>
    ;((gen-sc white) -80 500 80)
    ((gen-pc (:id gray-lines)) -80 500 80)
    (anim "bloop-x" "2s" "infinite" {:delay ".7s" :timing "ease-out"})
    (circ)
    (atom)))

(def bloops6
  (->>
    ((gen-sc white) -80 560 40)
    (anim "bloop-x" ".5s" "1" {:delay ".7s" :timing "ease-out"})
    (circ)
    (atom)))

(def move-me
  (->>
   ((gen-ps (:id yellow-lines)) hept)
   (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
   (anim "woosh" "8s" "infinite")
   (poly)
   (atom)))
   
   (def move-me-4
     (->>
      ((gen-ps (:id white-dots)) hept)
      (style {:opacity .5 :transform-origin "center" :transform "scale(4.4)"})
      (anim "woosh-2" "8s" "infinite" {:delay ".4s"})
      (poly)
      (atom)))
   
 (def move-me-2
   (->>
    ((gen-ss orange) hept)
    (style {:opacity .2 :transform-origin "center" :transform "translate(1000px, 400%) scale(4.4)"})
    (anim "woosh-3" "4s" "infinite")
    (poly)
    (atom)))
    
(def move-me-3
  (->>
   ((gen-ps (:id gray-patch)) hept)
   (style {:transform-origin "center" :transform "translate(-1000px, 300%) scale(4.4)"})
   (anim "woosh-4" "3s" "infinite")
   (poly)
   (atom)))
      
  (def bg (->> 
    (gen-nc "noise" (* .5 @width) (* .5 @height) 1800)
    (style {:opacity 1})
    (anim "fade-in-out" "100s" "infinite")
    (circ)
    (atom)))

(defn thin
  [color frame flicker? n]
  (let [op (if (and (nth-frame 4 frame) flicker?) (rand) 1)]
    (->>
     ((gen-sr color) (* 0.15 @width) (* 0.15 @height) (* 0.7 @width) 3)
     (style {:transform (str "translateY(" (* n 10) "px)") :opacity op})
     (rect)
     (when (nth-frame 1 frame)))))


(defn cx [frame]
  (list

    (let [colors [ 
      ;mint mint mint mint mint mint 
      navy navy navy navy navy navy ] ; orange navy mint pink gray white
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

    #_(->>
      (gen-grid
        40 1
        {:col 40 :row 40}
        (gen-rect white 5 0 2 @height)) 
       (map #(style {:opacity .5} %)) 
       (map rect) 
       (when (nth-frame 1 frame)))
       
       
       #_(->>
         (gen-grid
           1 40
           {:col 40 :row 40}
           (gen-rect white 0 5 @width 2)) 
          (map #(style {:opacity .5} %)) 
          (map rect) 
          (when (or  (nth-frame 1 frame) (nth-frame 4 frame))))
          
       
       #_(->>
         (gen-grid
           1 40
           {:col 40 :row 40}
           (gen-rect mint 0 5 @width 2)) 
          (map #(style {:opacity .5} %)) 
          (map rect) 
          (when (nth-frame 4 frame)))
          
          #_(->>
            (gen-grid
              1 40
              {:col 40 :row 40}
              (gen-rect navy 0 5 @width 2)) 
             (map #(style {:opacity .5} %)) 
             (map rect) 
             (when (nth-frame 3 frame)))
             
             
        
    #_(when (nth-frame 1 frame)
      (freak-out @width
                 @height
                 40
                 20
                 (pattern pink-lines)))
                 
     #_(when (or (nth-frame 5 frame) (nth-frame 7 frame)) 
       (freak-out @width
                  @height
                  4
                  1000
                  pink))
                 
     #_(when (nth-frame 4 frame) 
       (freak-out @width
                  @height
                  30
                  60
                  (pattern gray-dots-lg)))
                  
      #_(when (nth-frame 8 (+ 4 frame)) 
        (freak-out @width
                   @height
                   30
                   30
                   mint))
                
                 
     #_(when (nth-frame 6 frame) 
       (freak-out @width
                  @height
                  14
                  200
                  yellow))
                  
      #_(when (or (nth-frame 6 frame) (nth-frame 6 (+ 1 frame))) 
        (freak-out @width
                   @height
                   14
                   200
                   pink))
        
        
        ;@move-me
        

        
   #_(->> 
    (gen-nc "noise" (* .5 @width) (* .5 @height) 1800)
    (style {:opacity 1 :transform "translate(40px, 40px) rotate(15deg)"})
    (circ)
    (when (nth-frame 1 frame)))
    
          
  
     #_(->>
      ((gen-sc navy) (* 0.5 @width) (* 0.5 @height) 200)
      (style {:opacity .5 :filter (filt soft-noiz) })
      (circ)
      (when (nth-frame 2 frame)))
      
      (gen-bg-lines white (mod frame 70))
      
      (->>
       ((gen-sc navy) (* 0.5 @width) (* 0.5 @height) 200)
       (style {:opacity .5 :filter (filt turb) })
       (circ)
       (when (nth-frame 1 frame)))
      
       (gen-bg-lines white (mod frame 70))

      
      (->>
       ((gen-sc orange) (* (rand) @width) (* (rand) @height) (* 100 (rand)))
       (style {:opacity .4 :filter (filt noiz) })
       (circ)
       (when (nth-frame 1 frame)))
       
       (->>
        ((gen-sc orange) (* (rand) @width) (* (rand) @height) (* 100 (rand)))
        (style {:opacity .4 :filter (filt noiz) })
        (circ)
        (when (nth-frame 1 frame)))   
      
    ;@move-me
    ;@move-me-4
    @move-me-2 ; fix the animation to be livlier
  
     #_(->>
      ((gen-sr white) (* @width .10) (* @height .20) 400 40)
      (style {:transform-origin "center" :transform "rotate(-30deg)"})
      (rect)
      (when (nth-frame 4 frame)))
      
      #_(->>
       ((gen-sr (pattern white-lines)) (* @width .10) (* @height .20) 400 40)
       (style {:transform-origin "center" :transform "rotate(-30deg) translateY(-50px)"})
       (rect)
       (when (or (nth-frame 3 frame) (nth-frame 4 frame))))
       
       
       
       #_(->>
        ((gen-sr white) (* @width .50) (* @height .780) 400 40)
        (style {:transform-origin "center" :transform "rotate(-30deg)"})
        (rect)
        (when (nth-frame 4 frame)))
        
        #_(->>
         ((gen-sr (pattern white-lines)) (* @width .50) (* @height .780) 400 40)
         (style {:transform-origin "center" :transform "rotate(-30deg) translateY(-50px)"})
         (rect)
         (when (or (nth-frame 3 frame) (nth-frame 4 frame))))
       

        
       
  )) ; cx end

;; ----------- LOOP AND DRAW ------------------------------

(defonce frame (atom 0))

; should I replace with requestAnimationFrame?
(defonce start-cx-timer
  (js/setInterval
    #(reset! collection (cx @frame)) 50))
  
  #_(defonce start-cx-timer
    (reset! collection (cx @frame)) 
    (js/requestAnimationFrame start-cx-timer))
    
  #_(defonce start-anim
    (js/requestAnimationFrame start-cx-timer))

(defonce start-frame-timer
  (js/setInterval
    #(swap! frame inc) 500))

(defn drawing []
  [:svg { :width (:width settings) :height (:height settings) }
    (:def turb)
    (:def noiz)
    (:def soft-noiz)
    ;; eventually this should take in all the patterns
    [:defs (noise) (map pattern-def [ blue-dots
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
    @collection ])

(reagent/render-component [drawing]
                          (js/document.getElementById "app-container"))

;(hide-display)
