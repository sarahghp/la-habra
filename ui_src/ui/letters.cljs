(ns ui.letters
  (:require 
   [reagent.core :as reagent :refer [atom]]
   [ui.helpers :refer [style url]]
   [ui.shapes :as shapes :refer [arc-half arc-bottom-j arc-bottom-u 
                                 s-half s-curve tri]]
   [ui.fills :as fills :refer
     [ mint
       midnight
       navy
       blue
       pink
       white
       red
       purple
       yellow ]]
   [ui.generators :refer
    [draw
     freak-out
     gen-circ
     gen-group
     gen-line
     gen-rect
     gen-shape
     gen-cols
     gen-rows
     gen-poly]]))


;; create shapes object and import into core ✔
;; iterate through double-depth to create defs with :id from the inner label (eg :id "a1")
;;    and a map of the id strings for each letter {:a ["a1" "a2" "a3"]}
;; create a pick helper that takes an array and gives a random pick from that array
;; create a letter function that takes a letter and emits a <use> with picked def 

(defn letter-defs [letter-map] 
  (apply concat (map (fn [[_ letter-val]]
                       (map (fn [[var-key var-val]]
                              (atom (gen-group {:id var-key} var-val)))
                            letter-val)) 
                     letter-map)))


#_(def punc-shapes {:amp {:amp1 
                        (gen-group)}})
(def alpha-shapes {:a {:a1 
                       (gen-group {}
                                  (->>
                                    (gen-poly yellow [10 130
                                                      120 130
                                                      65 10])
                                    (draw))
                                  (->>
                                    (gen-line [65 90] [65 130] red 8)
                                    (draw)))
                       :a2 
                       (gen-group {}
                                  (->>
                                    (gen-poly mint [10 130
                                                    120 130
                                                    65 10])
                                    (draw))
                                  (->>
                                    (gen-line [65 90] [65 130] yellow 8)
                                    (draw))
                                  (->>
                                    (gen-circ midnight 65 90 14)
                                    (style {:transform "translate(-1px, -1px)"})
                                    (draw))
                                  (->>
                                    (gen-circ pink 65 90 14)
                                    (draw)))
                       :a3 
                       (gen-group {}
                                (->>
                                  (gen-poly red [10 130
                                                 120 130
                                                 65 10])
                                  (draw))
                                  (->>
                                    (gen-line [65 90] [65 130] yellow 8)
                                    (draw)))}
                   ;; Bs
                   :b {:b1 
                       (gen-group {:mask (url "b")
                                   :transform "scale(1.25)"}
                                  (->>
                                    (gen-rect blue 10 10 120 120)
                                    (draw))
                                            
                                  (gen-rows navy 6 10 12)
                                      
                                  (->>
                                    (gen-line [10 30] [50 30] yellow 8)
                                    (style {:transform "translate(1px, 2px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                            
                                  (->>
                                    (gen-line [10 30] [50 30] yellow 8)
                                    (draw))
                                            
                                  (->>
                                    (gen-line [8 70] [50 70] yellow 8)
                                    (style {:transform "translate(3px, 3px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))

                                  (->>
                                    (gen-line [10 70] [50 70] yellow 8)
                                    (draw)))
                       :b2
                       (gen-group {:transform "scale(1.25)"}
                                  (->>
                                    (gen-rect pink 10 10 120 120 (url "b"))
                                    (draw))
                                  (->>
                                    (gen-line [10 30] [50 30] mint 8)
                                    (style {:transform "translate(1px, 2px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                            
                                  (->>
                                    (gen-line [10 30] [50 30] mint 8)
                                    (draw))
                                            
                                  (->>
                                    (gen-line [8 70] [50 70] mint 8)
                                    (style {:transform "translate(3px, 3px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))

                                  (->>
                                    (gen-line [10 70] [50 70] mint 8)
                                    (draw)))
                       :b3
                       (gen-group {:transform "scale(1.25)"}
                                  (->>
                                    (gen-rect yellow 10 10 120 120 (url "b"))
                                    (draw))          
                                  (->>
                                    (gen-line [10 30] [50 30] red 8)
                                    (draw))
                                  (->>
                                    (gen-line [10 70] [50 70] red 8)
                                    (draw)))}
                    ;; Cs
                   :c {:c1
                       (gen-group {:style {:transform "translate(-30px, -30px)"}}
                                  (->>
                                    (gen-shape purple arc-half)
                                      (style {:transform-origin "center" :transform "translate(120px, 12px) scale(1) rotateZ(65deg)"})
                                      (draw))
                                 
                                 (->>
                                   (gen-shape mint arc-half)
                                     (style {:transform-origin "center" :transform "translate(120px, 68px) scale(1) rotateZ(-65deg)"})
                                     (draw))
                                 
                                   (->>
                                     (gen-shape pink arc-half)
                                       (style {:transform-origin "center" :transform "translate(100px, 40px) scale(1)"})
                                       (draw)))
                       :c2
                       (gen-group {:style {:transform "translate(-30px, -30px)"}}
                                  (->>
                                    (gen-shape red arc-half)
                                      (style {:transform-origin "center" :transform "translate(120px, 12px) scale(1) rotateZ(65deg)"})
                                      (draw))
                                 
                                 (->>
                                   (gen-shape red arc-half)
                                     (style {:transform-origin "center" :transform "translate(120px, 68px) scale(1) rotateZ(-65deg)"})
                                     (draw))
                                 
                                   (->>
                                     (gen-shape red arc-half)
                                       (style {:transform-origin "center" :transform "translate(100px, 40px) scale(1)"})
                                       (draw)))
                       :c3
                       (gen-group {:style {:transform "translate(-30px, -30px)"}}
                                  (->>
                                    (gen-shape blue arc-half)
                                      (style {:transform-origin "center" :transform "translate(120px, 12px) scale(1) rotateZ(65deg)"})
                                      (draw))
                                 (->>
                                   (gen-shape blue arc-half)
                                     (style {:transform-origin "center" :transform "translate(120px, 68px) scale(1) rotateZ(-65deg)"})
                                     (draw))
                                   (->>
                                     (gen-shape blue arc-half)
                                       (style {:transform-origin "center" :transform "translate(100px, 40px) scale(1)"})
                                       (draw)))}
                   ;; Ds
                   :d {:d1
                       (gen-group {:mask (url "d") }
                                  (->>
                                    (gen-rect yellow 10 10 120 120)
                                    (draw))
                                          
                                    #_(freak-out 10 120
                                               10 120
                                               10
                                               20
                                               red)
                                          
                                  (->>
                                    (gen-line [10 70] [80 70] mint 8)
                                    (draw)))
                       :d2
                       (gen-group {:mask (url "d")}
                                  (->>
                                    (gen-rect purple 10 10 120 120)
                                    (draw))
                                  (->>
                                    (gen-line [10 70] [80 70] pink 8)
                                    (draw)))
                       :d3
                       (gen-group {:mask (url "d")}
                                  (->>
                                    (gen-rect blue 10 10 120 120)
                                    (draw))
                                          
                                    (gen-group {:style {:transform "translateX(0px) rotate(8deg)"}}
                                               (gen-cols purple 10 10 26))
                                  (->>
                                    (gen-line [10 70] [80 70] pink 8)
                                    (draw)))}
                   :e {:e1
                       (gen-group {}
                                  (->>
                                    (gen-rect pink 10 10 120 120 (url "e"))
                                    (draw))
                                  (->>
                                    (gen-line [60 42] [96 42] yellow 8)
                                    (style {:transform "translate(-1px, 6px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                  (->>
                                    (gen-line [60 42] [96 42] yellow 8)
                                    (draw))
                                  (->>
                                    (gen-line [60 66] [96 66] yellow 8)
                                    (style {:transform "translate(-1px, 6px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                  (->>
                                    (gen-line [60 66] [96 66] yellow 8)
                                    (draw)))
                       :e2
                       (gen-group {}
                                  (->>
                                    (gen-rect mint 10 10 120 120 (url "e"))
                                    (draw))
                                  (->>
                                    (gen-line [59 43] [96 43] red 11)
                                    (style {:transform "translate(-1px, -1px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                  (->>
                                    (gen-line [60 42] [96 42] red 8)
                                    (draw))
                                  (->>
                                    (gen-line [59 66] [96 66] red 11)
                                    (style {:transform "translate(-1px, -1px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                  (->>
                                    (gen-line [60 66] [96 66] red 8)
                                    (draw))
                                  (->>
                                    (gen-rect red 10 10 20 110)
                                    (draw))
                                  (->>
                                    (gen-rect yellow 30 10 10 110)
                                    (draw)))
                       :e3
                       (gen-group {}
                                  (->>
                                    (gen-rect yellow 10 10 120 120 (url "e"))
                                    (draw))
                                  (->>
                                    (gen-line [60 42] [96 42] red 8)
                                    (draw))
                                  (->>
                                    (gen-line [60 66] [96 66] red 8)
                                    (draw)))
                       :e4
                       (gen-group {}
                                  (->>
                                    (gen-rect blue 10 10 120 120 (url "e"))
                                    (draw))
                                  (->>
                                    (gen-line [60 42] [96 42] red 11)
                                    (style {:transform "translate(-1px, 0px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                  (->>
                                    (gen-line [60 42] [96 42] red 8)
                                    (draw))
                                  (->>
                                    (gen-line [60 66] [96 66] red 11)
                                    (style {:transform "translate(-1px, 0px)"})
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                  (->>
                                    (gen-line [60 66] [96 66] red 8)
                                    (draw)))}
                   :f {:f1
                       (gen-group {:mask (url "f")}
                                  (->>
                                    (gen-rect pink 10 10 120 120)
                                    (draw))

                                 #_(freak-out 10 120
                                            10 120
                                            10
                                            10
                                            blue
                                            {:stroke purple 
                                             :stroke-width 1 })
                                 (->>
                                   (gen-line [60 49] [84 49] blue 9)
                                   (style {:transform "translate(-1px, 2px)"})
                                   (style {:mix-blend-mode "color-burn"})
                                   (draw))
                                 (->>
                                   (gen-line [60 49] [84 49] blue 8)
                                   (draw)))
                       :f2
                       (gen-group {:mask (url "f")}
                                  (->>
                                    (gen-rect red 10 10 120 120)
                                    (draw))
                                  (->>
                                    (gen-line [60 49] [84 49] yellow 8)
                                    (draw)))}
                   :g {:g1
                       (gen-group {}
                                  (gen-group {:mask (url "g")}
                                             (->>
                                              (gen-circ mint 60 60 60)
                                              (draw))
                                            (->>
                                              (gen-circ purple 60 60 40)
                                              (style {:stroke midnight 
                                                      :stroke-width 3
                                                      :stroke-opacity .3})
                                              (draw))
                                            (->>
                                              (gen-circ mint 60 60 20)
                                              (style {:stroke midnight 
                                                      :stroke-width 1
                                                      :stroke-opacity .3})
                                              (draw)))
                                  (->>
                                    (gen-poly purple [65 58
                                                      120 58
                                                      120 116])
                                    (draw)))
                       :g2
                       (gen-group {}
                                  (gen-group {:mask (url "g")}
                                             (->>
                                              (gen-circ red 60 60 60)
                                              (draw))
                                             (->>
                                              (gen-circ yellow 60 60 20)
                                              (draw)))
                                  (->>
                                    (gen-poly red [65 58
                                                      120 58
                                                      120 116])
                                    (draw)))
                       :g3
                       (gen-group {}               
                                  (gen-group {:mask (url "g")}
                                             (->>
                                              (gen-circ purple 60 60 60)
                                              (draw))
                                             (->>
                                               (gen-circ blue 60 60 20)
                                               (style {:stroke midnight 
                                                       :stroke-width 3
                                                       :stroke-opacity .3})
                                               (draw)))
                                  (->>
                                    (gen-poly purple [65 58
                                                      120 58
                                                      120 116])
                                    (draw)))}
                   :h {:h1
                       (gen-group {}
                                  (->>
                                    (gen-rect pink 10 10 90 110)
                                    (draw))
                                  (->>
                                    (gen-line [55 10] [55 42] midnight 10)
                                    (style {:transform "translate(2px, 1px)"})
                                    (draw))
                                  (->>
                                    (gen-line [55 10] [55 40] mint 8)
                                    (draw))
                                  (->>
                                    (gen-line [55 80] [55 120] midnight 10)
                                    (style {:transform "translate(2px, -1px)"})
                                    (draw))
                                  (->>
                                    (gen-line [55 80] [55 120] mint 8)
                                    (draw)))
                       :h2
                       (gen-group {}
                                  (->>
                                    (gen-rect red 10 10 90 110)
                                    (draw))
                                  (->>
                                    (gen-line [55 10] [55 40] yellow 8)
                                    (draw))
                                  (->>
                                    (gen-line [55 80] [55 120] yellow 8)
                                    (draw)))
                       :h3
                       (gen-group {:mask (url "h")}
                                    (->>
                                      (gen-rect blue 10 10 90 110)
                                      (draw))

                                    (gen-rows navy 20 20 33)
                                    (->>
                                      (gen-line [55 10] [55 42] midnight 10)
                                      (style {:transform "translate(2px, 1px)"})
                                      (draw))
                                    
                                    (->>
                                      (gen-line [55 10] [55 40] yellow 8)
                                      (draw))
                                    (->>
                                      (gen-line [55 80] [55 120] midnight 10)
                                      (style {:transform "translate(2px, -1px)"})
                                      (draw))
                                    (->>
                                      (gen-line [55 80] [55 120] yellow 8)
                                      (draw)))}
                   :i {:i1
                       (gen-group {}
                                  (->>
                                    (gen-rect purple 10 10 40 110)
                                    (draw)))
                       :i2
                       (gen-group {}
                                  (->>
                                    (gen-rect pink 10 10 40 110)
                                    (draw)))
                       :i3
                       (gen-group {:mask (url "i")}
                                  (->>
                                    (gen-rect yellow 10 10 40 110)
                                    (draw))
                                   (gen-cols red 8 4 18))
                       :i4
                       (gen-group {}
                                  (->>
                                    (gen-rect mint 10 10 40 110)
                                    (draw))
                                  
                                  (->>
                                    (gen-rect blue 10 10 40 20)
                                    (draw)))}
                   :j {:j1
                       (gen-group {:transform "scale(.88)"}
                                  (->>
                                    (gen-shape pink arc-bottom-j)
                                      (style {:transform "translateY(80px)"})
                                      (draw))
                                  (->>
                                    (gen-rect pink 55 10 45 80)
                                    (draw))
                                  (->>
                                    (gen-line [50 80] [50 104] yellow 12)
                                    (style {:mix-blend-mode "color-burn"})
                                    (draw))
                                  (->>
                                    (gen-line [50 80] [50 100] yellow 10)
                                    (draw)))
                       :j2
                       (gen-group {:transform "scale(.88)"}
                                  (->>
                                    (gen-shape blue arc-bottom-j)
                                      (style {:transform "translateY(80px)"})
                                      (draw))
                                  
                                  (gen-group {:mask (url "j")}
                                             (->>
                                               (gen-shape red arc-bottom-j)
                                                 (style {:transform "translateY(80px)"})
                                                 (draw))
                                             (->>
                                               (gen-rect red 55 10 45 80)
                                               (draw)))
                                  (->>
                                    (gen-line [53 80] [53 142] midnight 3)
                                    (style {:opacity .6})
                                    (draw)))}
                   :k {:k1
                       (gen-group {:mask (url "k")
                                   :transform "scale(1.1)"}
                                  (->>
                                    (gen-rect yellow 10 10 90 110)
                                    (draw))
                                  (->>
                                    (gen-line [45 10] [45 42] midnight 9)
                                    (style {:transform "translate(1px, 1px)"})
                                    (draw))
                                  (->>
                                    (gen-line [45 10] [45 40] mint 8)
                                    (draw))
                                  (->>
                                    (gen-line [45 80] [45 120] midnight 9)
                                    (style {:transform "translate(1px, -1px)"})
                                    (draw))
                                  (->>
                                    (gen-line [45 80] [45 120] mint 8)
                                    (draw)))
                       :k2
                       (gen-group {:mask (url "k")
                                   :transform "scale(1.1)"}
                                  (->>
                                    (gen-rect purple 10 10 90 110)
                                    (draw))
                                  (->>
                                    (gen-line [45 10] [45 42] midnight 10)
                                    (style {:transform "translate(2px, 1px)"})
                                    (draw))
                                  (->>
                                    (gen-line [45 10] [45 40] yellow 8)
                                    (draw))
                                  (->>
                                    (gen-line [45 80] [45 120] midnight 10)
                                    (style {:transform "translate(2px, -1px)"})
                                    (draw))
                                  (->>
                                    (gen-line [45 80] [45 120] yellow 8)
                                    (draw))
                                  (gen-group {:mask (url "i")}
                                             (gen-cols pink 7 4 20)))}
                   :l {:l1
                       (gen-group {}
                                  (->>
                                    (gen-poly mint [10 10
                                                    45 10
                                                    45 70
                                                    85 70
                                                    85 120
                                                    10 120])
                                    (draw))
                                  (->>
                                    (gen-rect red 10 10 15 110)
                                    (draw))
                                  (->>
                                    (gen-rect yellow 25 10 7 110)
                                    (draw)))
                       :l2
                       (gen-group {}
                                  (->>
                                    (gen-poly red [10 10
                                                    45 10
                                                    45 70
                                                    85 70
                                                    85 120
                                                    10 120])
                                    (draw)))
                       :l3
                       (gen-group {}
                                  (->>
                                    (gen-poly blue [10 10
                                                    45 10
                                                    45 70
                                                    85 70
                                                    85 120
                                                    10 120])
                                    (draw)))}
                   :m {:m1
                       (gen-group {:mask (url "m")
                                   :transform "scale(1.1)"}
                                  (->>
                                    (gen-rect red 0 0 110 110)
                                    (draw))                    
                                  #_(freak-out 0 110
                                             0 120
                                             10
                                             20
                                             yellow)
                                  (->>
                                    (gen-line [40 75] [40 120] midnight 11)
                                    (style {:transform "translateY(-2px)"})
                                    (style {:opacity .7})
                                    (draw))
                                  (->>
                                    (gen-line [40 75] [40 120] mint 8)
                                    (draw))
                                  (->>
                                    (gen-line [70 75] [70 120] midnight 11)
                                    (style {:transform "translateY(-2px)"})
                                    (style {:opacity .7})
                                    (draw))
                                  (->>
                                    (gen-line [70 75] [70 120] mint 8)
                                    (draw)))
                       :m2
                       (gen-group {:mask (url "m")
                                   :transform "scale(1.08)"}
                                  (->>
                                    (gen-rect purple 0 0 110 110)
                                    (draw))                    
                                  (->>
                                    (gen-line [40 75] [40 120] midnight 11)
                                    (style {:transform "translateY(-2px)"})
                                    (style {:opacity .7})
                                    (draw))
                                  (->>
                                    (gen-line [40 75] [40 120] pink 8)
                                    (draw))
                                  (->>
                                    (gen-line [70 75] [70 120] midnight 11)
                                    (style {:transform "translateY(-2px)"})
                                    (style {:opacity .7})
                                    (draw))
                                  (->>
                                    (gen-line [70 75] [70 120] pink 8)
                                    (draw)))}
                   :n {:n1
                       (gen-group {}
                                  (->>
                                    (gen-poly yellow [10 10 
                                                    68 46 
                                                    68 10 
                                                    100 10
                                                    100 120 
                                                    10 120])
                                    (draw))
                                  (->>
                                    (gen-line [38 90] [38 120] red 8)
                                    (draw)))
                       :n2
                       (gen-group {:mask (url "n")}
                                  (->>
                                    (gen-rect purple 10 10 100 110)
                                    (draw))
                                 
                                    #_(freak-out 10 100
                                               10 110
                                               10
                                               14
                                               mint)
                                  (->>
                                    (gen-line [38 90] [38 120] red 8)
                                    (draw)))
                       :n3
                       (gen-group {}
                                  (->>
                                    (gen-poly pink [10 10 
                                                    68 46 
                                                    68 10 
                                                    100 10
                                                    100 120 
                                                    10 120])
                                    (draw))
                                  (->>
                                    (gen-line [38 90] [38 120] yellow 8)
                                    (draw)))}
                   :o {:o1
                       (gen-group {}
                                  (->>
                                    (gen-circ blue 60 60 60)
                                    (draw))
                                  (->>
                                    (gen-circ purple 60 60 8)
                                    (draw)))
                       :o2
                       (gen-group {}
                                  (->>
                                    (gen-circ red 60 60 60)
                                    (draw))
                                  (->>
                                    (gen-circ yellow 60 60 8)
                                    (draw)))}
                   :p {:p1
                       (gen-group {}
                                  (->>
                                    (gen-rect pink 10 10 50 110)
                                    (draw))
                                  (->>
                                    (gen-rect pink 60 10 20 68)
                                    (draw))
                                  (->>
                                    (gen-shape pink arc-half)
                                      (style {:transform "translate(120px, -16px) rotate(180deg) scale(.57)"})
                                      (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] midnight 8)
                                    (style {:transform "translate(2px, 2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] mint 6)
                                    (draw)))
                       :p2
                       (gen-group {}
                                  (->>
                                    (gen-rect yellow 10 10 50 110)
                                    (draw))
                                  (->>
                                    (gen-rect yellow 60 10 20 68)
                                    (draw))
                                  (->>
                                    (gen-shape yellow arc-half)
                                      (style {:transform "translate(120px, -16px) rotate(180deg) scale(.57)"})
                                      (draw))
                                  (->>
                                    (gen-circ red 50 40 14)
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] midnight 9)
                                    (style {:transform "translate(2px, 0px)"})
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] mint 8)
                                    (draw)))
                       :p3
                       (gen-group {}
                                  (->>
                                    (gen-rect purple 10 10 50 110)
                                    (draw))
                                  (->>
                                    (gen-rect purple 60 10 20 68)
                                    (draw))
                                  (->>
                                    (gen-shape purple arc-half)
                                      (style {:transform "translate(120px, -16px) rotate(180deg) scale(.57)"})
                                      (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] pink 8)
                                    (draw)))}
                   :q {:q1
                       (gen-group {}
                                  (->>
                                    (gen-circ pink 60 60 60)
                                    (draw))
                                  (->>
                                    (gen-poly pink [60 120
                                                    85 75
                                                    120 120])
                                    (draw))
                                  (->>
                                    (gen-circ purple 60 60 16)
                                    (draw)))
                       :q2
                       (gen-group {:mask (url "q")}
                                  (->>
                                    (gen-rect mint 0 0 120 120)
                                    (draw))
                                  (->>
                                    (gen-line [65 65] [118 122] purple 13)
                                    (draw))
                                  (->>
                                    (gen-circ red 60 60 18)
                                    (style {:stroke midnight 
                                            :stroke-width 3 
                                            :stroke-opacity .3})
                                    (draw)))}
                   :r {:r1
                       (gen-group {}
                                  (->>
                                    (gen-rect purple 10 10 50 110)
                                    (draw))
                                  (->>
                                    (gen-rect purple 60 10 20 68)
                                    (draw))
                                  (->>
                                    (gen-shape purple arc-half)
                                      (style {:transform "translate(120px, -16px) rotate(180deg) scale(.57)"})
                                      (draw))
                                  (->>
                                    (gen-poly purple [50 30
                                                    110 120
                                                    50 120])
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] midnight 9)
                                    (style {:transform "translate(0px, 0px)"})
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] pink 8)
                                    (draw))
                                  (->>
                                    (gen-line [50 90] [50 120] midnight 9)
                                    (style {:transform "translate(0px, -2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [50 90] [50 120] pink 8)
                                    (draw)))
                       :r2
                       (gen-group {}
                                  (->>
                                    (gen-rect mint 10 10 50 110)
                                    (draw))
                                  (->>
                                    (gen-rect mint 60 10 20 68)
                                    (draw))
                                  (->>
                                    (gen-shape mint arc-half)
                                      (style {:transform "translate(120px, -16px) rotate(180deg) scale(.57)"})
                                      (draw))
                                  (->>
                                    (gen-poly mint [50 30
                                                    110 120
                                                    50 120])
                                    (draw))
                                  (->>
                                    (gen-circ yellow 50 40 16)
                                    (style {:stroke midnight 
                                            :stroke-width 3 
                                            :stroke-opacity .2})
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] midnight 9)
                                    (style {:transform "translate(2px, 0px)"})
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] red 8)
                                    (draw))
                                  (->>
                                    (gen-line [50 90] [50 120] midnight 9)
                                    (style {:transform "translate(0px, -2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [50 90] [50 120] red 8)
                                    (draw)))
                       :r3
                       (gen-group {}
                                  (->>
                                    (gen-rect red 10 10 50 110)
                                    (draw))
                                  (->>
                                    (gen-rect red 60 10 20 68)
                                    (draw))
                                  (->>
                                    (gen-shape red arc-half)
                                      (style {:transform "translate(120px, -16px) rotate(180deg) scale(.57)"})
                                      (draw))
                                  (->>
                                    (gen-poly red [50 30
                                                    110 120
                                                    50 120])
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] midnight 9)
                                    (style {:transform "translate(0px, 0px)"})
                                    (draw))
                                  (->>
                                    (gen-line [10 40] [50 40] blue 8)
                                    (draw))
                                  (->>
                                    (gen-line [50 90] [50 120] midnight 9)
                                    (style {:transform "translate(0px, -2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [50 90] [50 120] blue 8)
                                    (draw)))}
                   :s {:s1
                       (gen-group {:transform "scale(1.22)"}
                                  (gen-group {:style {:transform "rotate(-2deg)"}}
                                             (->>
                                               (gen-shape pink s-half)
                                                 (style {:transform "translate(20px, 0px) rotate(-130deg) scale(1)"})
                                                 (draw))
                                             (->>
                                               (gen-shape pink s-half)
                                                 (style {:transform "translate(40px, 50px) rotate(50deg) scale(1)"})
                                                 (draw))))
                       :s2
                       (gen-group {:transform "scale(1.22)"}
                                  (gen-group {:mask (url "s")}
                                             (->>
                                               (gen-rect blue 0 0 120 120)
                                               (draw))
                                             (gen-group {:style {:transform "translateX(-64px) rotate(-45deg)"}}
                                                        (gen-rows purple 10 10 18))))
                       :s3
                       (gen-group {:transform "scale(1.22)"}
                                  (gen-group {:mask (url "s")}
                                   (->>
                                     (gen-rect yellow 0 0 120 120)
                                     (draw))
                                    (->>
                                      (gen-shape "hsla(360, 100%, 100%, 0)" s-curve)
                                        (style {:stroke red 
                                                :stroke-width 8})
                                        (style {:transform "translate(64px, 30px) scale(1)"})
                                        (draw))
                                    (->>
                                      (gen-shape "hsla(360, 100%, 100%, 0)" s-curve)
                                        (style {:stroke red 
                                                :stroke-width 8})
                                        (style {:transform "translate(34px, 73px) rotate(188deg) scale(1)"})
                                        (draw))))}
                   :t {:t1
                       (gen-group {:mask (url "t")}
                                  (->>
                                    (gen-rect yellow 0 0 120 120)
                                    (draw))
                                  
                                  (freak-out 0 120
                                             0 120
                                             10
                                             20
                                             red))
                       :t2
                       (gen-group {:mask (url "t")}
                                  (->>
                                    (gen-rect blue 0 0 120 120)
                                    (draw)))
                       :t3
                       (gen-group {:mask (url "t")}
                                  (->>
                                    (gen-rect mint 0 0 120 120)
                                    (draw))
                                 (->>
                                   (gen-rect red 0 0 46 120)
                                   (draw))
                                 (->>
                                   (gen-rect yellow 46 0 10 120)
                                   (draw)))}
                   :u {:u1
                       (gen-group {}
                                  (->>
                                    (gen-shape pink arc-bottom-u)
                                      (style {:transform "translateY(80px)"})
                                      (draw))
                                  (->>
                                    (gen-rect pink 0 10 100 78)
                                    (draw))
                                  (->>
                                    (gen-line [50 10] [50 68] midnight 13)
                                    (style {:opacity .6})
                                    (style {:transform "translate(1px, 2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [50 10] [50 68] mint 8)
                                    (draw)))
                       :u2
                       (gen-group {}
                                  (->>
                                    (gen-shape purple arc-bottom-u)
                                      (style {:transform "translateY(80px)"})
                                      (draw))
                                  (->>
                                    (gen-rect purple 0 10 100 78)
                                    (draw))
                                  
                                  (gen-group {:mask (url "u")}
                                             (->>
                                               (gen-shape red arc-bottom-u)
                                                 (style {:transform "translateY(80px)"})
                                                 (draw))
                                             (->>
                                               (gen-rect red 50 10 50 80)
                                               (draw)))

                                  (->>
                                    (gen-line [50 10] [50 68] yellow 8)
                                    (draw))
                                  (->>
                                    (gen-line [46 10] [46 68] midnight 3)
                                    (style {:opacity .6})
                                    (draw))
                                  (->>
                                    (gen-line [50 68] [50 128] midnight 3)
                                    (style {:opacity .6})
                                    (draw)))}
                   :v {:v1
                       (gen-group {}
                                  (->>
                                    (gen-poly yellow [10 10
                                                      120 10
                                                      65 120])
                                    (draw))
                                  (->>
                                    (gen-line [65 10] [65 50] purple 8)
                                    (draw))
                                  (->>
                                    (gen-circ red 65 50 14)
                                    (draw)))
                       :v2
                       (gen-group {}
                                 (->>
                                   (gen-poly red [10 10
                                                  120 10
                                                  65 120])
                                   (draw))
                                 (->>
                                   (gen-line [65 10] [65 50] midnight 11)
                                   (style {:transform "translate(1px, 1px)"})
                                   (draw))
                                 (->>
                                   (gen-line [65 10] [65 50] mint 8)
                                   (draw)))}
                   :w {:w1
                       (gen-group {:mask (url "w")}
                                  (->>
                                    (gen-rect mint 0 0 160 120)
                                    (draw))
                                  (gen-group {:style {:transform "translateX(-3px)"}}
                                             (gen-cols purple 8 10 16))
                                  (->>
                                    (gen-line [70 10] [70 50] midnight 10)
                                    (style {:transform "translate(0px, 2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [70 10] [70 50] yellow 8)
                                    (draw))
                                  (->>
                                    (gen-line [100 10] [100 50] midnight 10)
                                    (style {:transform "translate(0px, 2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [100 10] [100 50] yellow 8)
                                    (draw)))
                       :w2
                       (gen-group {:mask (url "w")}
                                  (->>
                                    (gen-rect pink 0 0 160 120)
                                    (draw))
                                  (->>
                                    (gen-line [70 10] [70 40] midnight 12)
                                    (style {:transform "translate(1px, 2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [70 10] [70 40] mint 8)
                                    (draw))
                                  (->>
                                    (gen-line [100 10] [100 40] midnight 12)
                                    (style {:transform "translate(1px, 2px)"})
                                    (draw))
                                  (->>
                                    (gen-line [100 10] [100 40] mint 8)
                                    (draw)))}
                   :x {:x1
                       (gen-group {}
                                  (->>
                                    (gen-poly purple [10 10
                                                      120 10
                                                      65 120])
                                    (draw))
                                  (->>
                                    (gen-poly purple [10 10
                                                      120 10
                                                      65 120])
                                    (style {:transform "rotate(180deg)"})
                                    (draw))
                                  (->>
                                    (gen-line [65 10] [65 40] pink 8)
                                    (draw))
                                  (->>
                                    (gen-line [65 90] [65 120] pink 8)
                                    (draw)))
                       :x2
                       (gen-group {}
                                  (->>
                                    (gen-poly red [10 10
                                                      120 10
                                                      65 120])
                                    (draw))
                                  (->>
                                    (gen-poly red [10 10
                                                      120 10
                                                      65 120])
                                    (style {:transform "rotate(180deg)"})
                                    (draw))
                                  (gen-group {:mask (url "x")}
                                             (->>
                                               (gen-poly pink [10 10
                                                                 120 10
                                                                 65 120])
                                               (draw))
                                             (->>
                                               (gen-poly pink [10 10
                                                                 120 10
                                                                 65 120])
                                               (style {:transform "rotate(180deg)"})
                                               (draw)))
                                  (->>
                                    (gen-line [65 10] [65 40] yellow 8)
                                    (draw))
                                  (->>
                                    (gen-line [65 90] [65 120] yellow 8)
                                    (draw))
                                 (->>
                                   (gen-circ midnight 64 66 15)
                                   (draw))
                                 (->>
                                   (gen-circ mint 65 65 14)
                                   (draw)))}
                   :y {:y1
                       (gen-group {}
                                  (gen-group {:mask (url "y")}
                                             (->>
                                               (gen-rect yellow 0 0 120 120)
                                               (draw))
                                             (gen-rows red 6 30 12))
                                  (->>
                                    (gen-line [65 10] [65 35] blue 8)
                                    (draw)))
                       :y2
                       (gen-group {}
                                  (gen-group {:mask (url "y")}
                                             (->>
                                               (gen-rect blue 0 0 120 120)
                                               (draw)))
                                  (->>
                                    (gen-line [65 7] [65 35] midnight 13)
                                    (style {:transform "translate(-1px, 3px)"})
                                    (draw))
                                  (->>
                                    (gen-line [65 10] [65 35] red 8)
                                    (draw)))}
                   :z {:z1
                       (gen-group {}
                                  (gen-group {:mask (url "z")}
                                             (->>
                                               (gen-rect purple 0 0 100 120)
                                               (draw))
                                             (->>
                                               (gen-rect red 0 0 50 120)
                                               (draw))
                                             (->>
                                               (gen-rect yellow 48 0 10 120)
                                               (draw))))
                       :z2
                       (gen-group {}
                                  (gen-group {:mask (url "z")}
                                             (->>
                                               (gen-rect mint 0 0 100 120)
                                               (draw))))}})

(def alpha-mask-list
  [["b"
    (gen-group {}
               (->>
                (gen-circ white 63 36 26)
                (style {:opacity 1})
                (draw))
              (->>
                (gen-rect white 10 10 60 50)
                (draw))
              (->>
                (gen-circ white 68 74 26)
                (style {:opacity 1})
                (draw))
              (->>
                (gen-rect white 10 50 60 50)
                (draw)))
    ]
  ["d" 
    (gen-group {:transform "scale(.95)"}
               (->>
                (gen-circ white 66 67 60)
                (style {:opacity 1})
                (draw))
              (->>
                (gen-rect white 10 10 68 116)
                (draw)))]
  ["e"
   (->>
     (gen-poly white [10 10 
                     110 10
                     110 40
                     95 40
                     95 70
                     110 70
                     110 120
                     10 120])
     (draw))]
  ["f"
   (->>
     (gen-poly white [10 10 
                     100 10
                     100 48
                     80 48
                     80 80
                     50 80
                     50 120
                     10 120])
     (draw))]
  ["g"
   (gen-group {}
    (->>
      (gen-rect white 0 0 130 130)
      (draw))
    (->>
     (gen-shape "#000" tri)
       (style {:transform "translate(10px, -20px) rotate(-90deg) scale(.5)"})
       (draw)))]
  ["h"
   (->>
     (gen-rect white 10 10 90 110)
     (draw))]
  ["i"
   (->>
     (gen-rect white 10 10 40 110)
     (draw))]
  ["j"
   (->>
     (gen-rect white 55 10 45 140)
     (draw))]
  ["k"
   (gen-group {}
              (->>
                (gen-rect white 0 0 400 122)
                (draw))
              (->>
                (gen-poly "#000" [102 0
                                  74 65
                                  102 130])
                (draw)))]
  ["m"
   (gen-group {}
              (->>
                (gen-rect white 0 0 110 110)
                (draw))
              (->>
                (gen-poly "#000" [0 0
                                 55 30
                                 110 0])
                (draw)))]
  
  ["n"
   (->>
     (gen-poly white [10 10 
                     68 46 
                     68 10 
                     100 10
                     100 120 
                     10 120])
     (draw))]
  ["q"
   (gen-group {}
             (->>
               (gen-circ white 60 60 60)
               (draw))
              (->>
                (gen-poly white [60 120
                                85 75
                                120 120])
                (draw)))]
  ["s" 
   (gen-group {:style {:transform "rotate(-2deg)"}}
              (->>
                (gen-shape white s-half)
                  (style {:transform "translate(20px, 0px) rotate(-130deg) scale(1)"})
                  (draw))
              (->>
                (gen-shape white s-half)
                  (style {:transform "translate(40px, 50px) rotate(50deg) scale(1)"})
                  (draw)))]
  ["t"
   (->>
     (gen-poly white [10 10
                       100 10
                       100 50
                       76 50
                       76 120
                       34 120
                       34 50
                       10 50])
     (draw))]
  ["u"
   (->>
     (gen-rect white 50 10 55 140)
     (draw))]
  ["w"
   (gen-group {}
              (->>
                (gen-poly white [10 10
                               120 10
                               65 120])
                (draw))
              (->>
                (gen-poly white [45 10
                               155 10
                               110 120])
                (draw)))]
  ["x"
   (->>
     (gen-rect white 70 10 75 140)
     (draw))]
  ["y"
   (gen-group {}
              (->>
                (gen-poly white [10 10
                                  120 10
                                  65 100])
                (draw))
              (->>
                (gen-rect white 45 50 38 70)
                (draw)))]
["z"
  (->>
    (gen-poly white [10 10
                    100 10
                    70 70 ; right point
                    100 70
                    100 120
                    10 120
                    40 45 ; left point
                    10 45
                    ])
    (draw))]])
