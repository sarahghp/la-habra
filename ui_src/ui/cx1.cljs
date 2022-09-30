(ns ui.cx1
  (:require [reagent.core :as reagent :refer [atom]]
            [ui.sets :refer [worms worms-2]]
            [ui.text :as t :refer [cd1 cd2 cd3 cd4]]
            [ui.helpers :refer [cos sin style url val-cyc deform]]
            [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                          b1 b2 b3 b4 
                                          l1 l2 l3 l4 l5 l6
                                          ul2 ul3
                                          scr1 scr2 scr3 scr4 scr5]]
            [ui.fills :as fills :refer
              [gray charcoal mint midnight navy blue orange
                br-orange pink white yellow clear]]
            [ui.generators :refer
             [freak-out new-freakout scatter lerp
              gen-circ gen-line gen-poly gen-rect gen-shape draw
              gen-group gen-offset-lines gen-bg-lines gen-mask
              gen-grid gen-line-grid gen-cols gen-rows]]
            [ui.filters :as filters :refer [turb noiz soft-noiz disappearing splotchy blur]]
            [ui.patterns :as patterns :refer
             [gen-color-noise pattern pattern-def
              blue-dots blue-dots-1 blue-dots-2 blue-dots-3 blue-dots-4 blue-dots-5
              blue-lines blue-lines-1 blue-lines-2 blue-lines-3 blue-lines-4 blue-lines-5
              pink-lines pink-lines-1 pink-lines-2 pink-lines-3 pink-lines-4 pink-lines-5
              pink-dots pink-dots-1 pink-dots-2 pink-dots-3 pink-dots-4 pink-dots-5
              gray-dots gray-dots-lg gray-dots-1 gray-dots-2 gray-dots-3 gray-dots-4 gray-dots-5
              gray-lines gray-lines-1 gray-lines-2 gray-lines-3 gray-lines-4 gray-lines-5
              gray-patch
              mint-dots mint-dots-1 mint-dots-2 mint-dots-3 mint-dots-4 mint-dots-5
              mint-lines mint-lines-1 mint-lines-2 mint-lines-3 mint-lines-4 mint-lines-5
              navy-dots navy-dots-1 navy-dots-2 navy-dots-3 navy-dots-4 navy-dots-5
              navy-lines navy-lines-1 navy-lines-2 navy-lines-3 navy-lines-4 navy-lines-5
              orange-dots orange-dots-1 orange-dots-2 orange-dots-3 orange-dots-4 orange-dots-5 orange-lines orange-lines-1 orange-lines-2 orange-lines-3 orange-lines-4 orange-lines-5
              br-orange-dots br-orange-dots-1 br-orange-dots-2 br-orange-dots-3 br-orange-dots-4 br-orange-dots-5  br-orange-lines br-orange-lines-1 br-orange-lines-2 br-orange-lines-3 br-orange-lines-4 br-orange-lines-5
              yellow-dots yellow-dots-1 yellow-dots-2 yellow-dots-3 yellow-dots-4 yellow-dots-5
              yellow-lines yellow-lines-1 yellow-lines-2 yellow-lines-3 yellow-lines-4 yellow-lines-5
              white-dots white-dots-1 white-dots-2 white-dots-3 white-dots-4 white-dots-5
              white-dots-lg
              white-lines white-lines-1 white-lines-2 white-lines-3 white-lines-4 white-lines-5
              midnight-dots-1 midnight-dots-2 midnight-dots-3 midnight-dots-4 midnight-dots-5  midnight-lines-1 midnight-lines-2 midnight-lines-3 midnight-lines-4 midnight-lines-5
              shadow noise]]
            [ui.animations :as animations :refer
              [ nth-frame anim anim-and-hold
                ]]))

;; --------------- SETTINGS --------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))
(def transparent "hsla(0, 0%, 0%, 0)")

;; --------------- ATOMS STORAGE --------------------

(def trim
  (->>
   (gen-shape (pattern (:id white-dots-1))  square)
   (anim "loopy-left" "10s" "infinite")
   (draw)
   (atom)))
   
(def trim-2
  (->>
   (gen-shape (pattern (:id white-dots-1)) square)
   (anim "loopy-right" "10s" "infinite")
   (draw)
   (atom)))
   
(def peak
  (->>
   (gen-shape (pattern (:id mint-lines-5)) oct)
    (anim "peak-r" "10s" "infinite")
   (draw)
   (atom)))
   
(def peak-2
  (->>
   (gen-shape (pattern (:id pink-lines-1)) oct)
    (anim "peak-r-rot" "10s" "infinite" {:delay ".4s" :timing "ease-out"})
   (draw)
   (atom)))
   
(def peak-3
  (->>
   (gen-shape (pattern (:id white-dots-3)) oct)
    (anim "peak-l-rot" "10s" "infinite" {:delay ".4s" :timing "ease-out"})
   (draw)
   (atom)))
   
(def longgi
  (->>
   (gen-line [300 300] [300 (* 0.8 @height)] white 10)
   (anim "rot" "12s" "infinite")
   (draw)
   (atom)))

(def longgi-2
  (->>
   (gen-line [(* 0.2 @width) 300] [(* 0.7 @width) (* 0.8 @height)] white 10)
   (anim "rot" "12s" "infinite")
   (draw)
   (atom)))
   
 (def longgi-3
   (->>
    (gen-line [1500 300] [1500 (* 0.8 @height)] white 10)
    (anim "rot" "12s" "infinite")
    (draw)
    (atom)))
    
(def move-me
  (->>
   (gen-line [0 0] [100 100] white 10)
   (style {:stroke-linecap "round"})
   (anim "peak-l-rot" "12s" "infinite")
   (draw)
   (atom)))

(def move-me-2
  (->>
   (gen-line [0 0] [100 100] white 10)
   (style {:stroke-linecap "round"})
   (anim "peak-r-rot" "12s" "infinite")
   (draw)
   (atom)))
   
(def move-me-3
  (->>
   (gen-shape br-orange tri)
   (anim "peak-r-rot" "12s" "infinite")
   
   (draw)
   (atom)))
   
(def move-me-4
  (->>
   (gen-shape orange tri)
   (anim "woosh" "8s" "infinite")
   (draw)
   (atom)))
   
   (def move-me-5
     (->>
      (gen-shape (pattern (:id white-lines-1)) oct)
      (anim "woosh-2" "18s" "infinite")
      (draw)
      (atom)))
   
(def down
  (->>
   (gen-shape (pattern (:id yellow-lines-5)) oct)
   (anim "descend" "20s" "infinite")
   (draw)
   (gen-group {:style {:transform "translateX(12vw) scale(4)"}})
   (atom)))
   
 (def up
   (->>
    (gen-shape (pattern (:id mint-lines-5)) oct)
    (anim "ascend" "20s" "infinite")
    (draw)
    (gen-group {:style {:transform "translateX(12vw) scale(4)"}})
    (atom)))
    
    (def down-2
      (->>
       (gen-shape (pattern (:id yellow-lines-5)) oct)
       (anim "descend" "20s" "infinite")
       (draw)
       (gen-group {:style {:transform "translateX(42vw) scale(4)"}})
       (atom)))
       
     (def up-2
       (->>
        (gen-shape (pattern (:id mint-lines-5)) oct)
        (anim "ascend" "20s" "infinite")
        (draw)
        (gen-group {:style {:transform "translateX(42vw) scale(4)"}})
        (atom)))

 ;; ----------- GROUPS AND GRIDS --------------------------


(def blobbo 
      (->>
       (gen-grid
         10 10
         {:col 200 :row 200}
         (->>
          (gen-circ mint 0 0 100)))
         ;(map #(style styles %))
         ;(map #(anim "ascend" "30s" "infinite" %))
         (map draw)
         (map #(gen-group {:style {:transform-origin "center" :animation "ascend 30s infinite" }} %))))


(def trio 
      (->>
       (gen-grid
         10 10
         {:col 200 :row 200}
         (gen-shape yellow tri))
         (map draw)
         (map #(gen-group {:style {:transform-origin "center" :transform "skew(40deg)" }} %))
         (map #(gen-group {:style {:transform-origin "center" :animation "ascend 30s infinite" }} %))))
         
(def longgis 
      (->>
       (gen-grid
         10 10
         {:col 200 :row 200}
           (gen-line [30 30] [30 (* 0.5 @height)] white 10))
         ;(map #(style styles %))
         (map draw)
         (map #(gen-group {:style {:transform-origin "center" :animation "rot 10s infinite" }} %))))

 ;; ----------- COLLECTION SETUP AND CHANGE ----------------
 
 ;; start end dur frame no-repeat
 (def lerp1 (lerp))
 (def lerp2 (lerp))
 (def lerp3 (lerp))
 
 (defn list1 [frame fast-frame slow-frame svg-frame]
   (list
     (let
       [colors [
               ;navy navy navy navy
               pink pink pink pink

                ]]
         (->>
           (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
           (style {:opacity .95})
           (draw)))

          ;blobbo

          ;@move-me-4 
          ;@move-me-3
          
          

 
           ;@move-me
           ;@move-me-2
           
                     ;@trim
                     ;@trim-2
          
          #_(->>
           (gen-shape midnight tri)
           (style {:transform (str "skew("(val-cyc frame [10 30 400 -10 60])"deg) translate(30vw, 30vh) scale(2)")})
           (draw)
           (when (nth-frame 1 frame)))
           
           trio
           @move-me-5

           
           
    ))
           
(defn list2 [frame fast-frame  slow-frame svg-frame]
     (list
       (let
         [colors [

                  ;navy navy (pattern (:id blue-dots-4)) (pattern (:id blue-dots-3)) navy navy 
                  
                  pink

                  ]]
           (->>
             (gen-rect (val-cyc fast-frame colors) 0 0 "100vw" "100%")
             (style {:opacity .95})
             (draw)))
             
            ;@down
            ;@up
            
                        ;@down-2
                        ;@up-2
          
            
          @trim
          @trim-2
            
                  
                  
                        
            ))
            
(defn list3 [frame fast-frame  slow-frame svg-frame]
                
     (list
       (let
         [colors [
                  navy navy navy navy

                  ]]
           (->>
             (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
             (style {:opacity .95})
             (draw)))
             
             ;blobbo
             
             @longgi
             @longgi-2
             @longgi-3
             
             (when (nth-frame 4 slow-frame) trio)
             
                          ;longgis
                          
              #_(->>
               (gen-shape br-orange tri)
               (style {:transform "translate(20vw, 20vh) scale(4) rotate(135deg)"})
                                         (style {:mix-blend-mode "luminosity"})

               (draw)
               (when (nth-frame 8 frame)))
               
            #_(->>
              (gen-shape br-orange tri)
              (style {:transform "translate(26vw, 40vh) scale(4) rotate(-135deg)"})
              (draw)
              (when (nth-frame 6 frame)))
              
              
              #_(gen-group {:style {:transform "translate(40vw, 20vh)"}}
                  (->>
                   (gen-shape br-orange tri)
                   (style {:transform "translate(20vw, 20vh) scale(4) rotate(135deg)"})
                                             (style {:mix-blend-mode "luminosity"})

                   (draw)
                   (when (nth-frame 4 frame)))
                   
                (->>
                  (gen-shape br-orange tri)
                  (style {:transform "translate(26vw, 40vh) scale(4) rotate(-135deg)"})
                  (draw)
                  (when (nth-frame 6 frame))))
                  
                  
                      #_(gen-group {:style {:transform "translate(60vw, 30vh)"}}
                          (->>
                           (gen-shape br-orange tri)
                           (style {:transform "translate(20vw, 20vh) scale(4) rotate(135deg)"})
                           (draw)
                           (when (nth-frame 4 (+ 1 frame))))
                           
                        (->>
                          (gen-shape br-orange tri)
                          (style {:transform "translate(26vw, 40vh) scale(4) rotate(-135deg)"})
                          (style {:mix-blend-mode "luminosity"})
                          (draw)
                          (when (nth-frame 6 (+ 1 frame)))))
             

               
                            
                         
  
             
                                     
  ))
                        
(defn list4 [frame fast-frame  slow-frame svg-frame]
  
  (list
    (let
      [colors [
               midnight midnight midnight midnight
              
               ]]
        (->>
          (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
          (style {:opacity .95})
          (draw)))

                       (gen-group {:style {:transform "translateX(100px)"}} longgis)
                       longgis
  
  ))
  
(defn list5 [frame fast-frame  slow-frame svg-frame]
  (list
    (let
      [colors [
               midnight midnight midnight midnight
               ]]
        (->>
          (gen-rect (val-cyc frame colors) 0 0 "100vw" "100%")
          (style {:opacity .95})
          (draw)))
          

  
  ))

(defn cx [fast-frame frame slow-frame svg-frame]
  (val-cyc svg-frame [
    (list1 fast-frame frame slow-frame svg-frame)
        (list1 fast-frame frame slow-frame svg-frame)

    ;(list3 fast-frame frame slow-frame svg-frame)
    ;(list3 fast-frame frame slow-frame svg-frame)
    ;(list2 fast-frame frame slow-frame svg-frame)
        ;(list2 fast-frame frame slow-frame svg-frame)

    ;(list3 fast-frame frame slow-frame svg-frame)
        ;(list3 fast-frame frame slow-frame svg-frame)

    ;(list4 fast-frame frame slow-frame svg-frame)


    

  ])) ; cx end
  
