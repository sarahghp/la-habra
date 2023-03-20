(ns ui.masks
  (:require
   [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                 b1 b2 b3 b4
                                l1 l2 l3 l4 l5 l6
                                ul2 ul3]]
  [ui.helpers :refer [cos sin style url val-cyc]]
  [ui.generators :refer
      [freak-out new-freakout scatter lerp
       gen-circ gen-line gen-poly gen-rect gen-shape draw
       gen-group gen-offset-lines gen-bg-lines gen-mask
       gen-grid gen-line-grid gen-cols gen-rows]]
   [ui.fills :as fills :refer
     [gray charcoal mint midnight navy blue orange
       br-orange pink white yellow]]
   [ui.patterns :as patterns :refer
    [ gen-color-noise pattern pattern-def
      blue-dots blue-lines
      pink-dots pink-lines pink-dots-1 pink-dots-2 pink-dots-3 pink-dots-4 pink-dots-5
      gray-dots gray-dots-lg gray-lines gray-patch
      mint-dots mint-lines
      navy-dots navy-lines
      orange-dots orange-lines
      br-orange-dots br-orange-lines
      yellow-dots yellow-lines
      white-dots white-dots-lg white-lines
      shadow noise]]
      [ui.animations :as animations :refer
        [ anim ]]))


;; --------------- SETTINGS --------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

(def move-mask-1 [
  "move-mask-1"
  (gen-group {}
    (->>
     (gen-circ "#fff" 100 100 20)
     (anim "loopy-left" "10s" "infinite")
     (draw))
   (->>
    (gen-circ "#fff" 100 100 20)
    (anim "loopy-left" "8s" "infinite" {:delay ".3s"})
    (draw))
  (->>
   (gen-circ "#fff" 100 100 20)
   (anim "loopy-right" "10s" "infinite")
   (draw)))])


(def still-mask-1 [
 "still-mask-1"
 (gen-group {}
   (->>
    (gen-circ "#fff" 400 400 60)
    #_(anim "loopy-left" "10s" "infinite")
    (draw))
  (->>
   (gen-circ "#fff" 800 300 60)
   #_(anim "loopy-left" "8s" "infinite" {:delay ".3s"})
   (draw))
 (->>
  (gen-circ "#fff" 200 200 60)
  #_(anim "loopy-right" "10s" "infinite")
  (draw)))])


(def box-1 [
 "box-1"
 (->>
  (gen-rect white 0 0 (* 0.33 @width) (* 0.33 @height))
  (draw))])

(def box-2 [
 "box-2"
 (->>
  (gen-rect white (* 0.33 @width) 0 (* 0.66 @width) (* 0.33 @height))
  (draw))])

(def box-3 [
 "box-3"
 (->>
  (gen-rect white (* 0.66 @width) 0 @width (* 0.33 @height))
  (draw))])

(def box-4 [
 "box-4"
 (->>
  (gen-rect white 0 (* 0.33 @height) (* 0.33 @width) (* 0.33 @height))
  (draw))])

(def box-5 [
"box-5"
(->>
  (gen-rect white (* 0.33 @width) (* 0.33 @height) (* 0.33 @width) (* 0.33 @height))
  (draw))])

(def box-6 [
 "box-6"
 (->>
  (gen-rect white (* 0.66 @width) (* 0.33 @height) (* 0.33 @width) (* 0.33 @height))
  (draw))])

(def box-7 [
  "box-7"
  (->>
    (gen-rect white 0 (* 0.66 @height) (* 0.33 @width) (* 0.33 @height))
    (draw))])

(def box-8 [
  "box-8"
  (->>
    (gen-rect white (* 0.33 @width) (* 0.66 @height) (* 0.33 @width) (* 0.33 @height))
    (draw))])

(def box-9 [
 "box-9"
 (->>
  (gen-rect white (* 0.66 @width) (* 0.66 @height) (* 0.33 @width) (* 0.33 @height))
  (draw))])

(def mask-list
  [ move-mask-1 still-mask-1
  box-1 box-2 box-3 box-4 box-5 box-6 box-7 box-8 box-9
    [ "poly-mask"
      [:path {:d b2 :fill "#fff" :style { :transform-origin "center" :animation "woosh 2s infinite"}}]]
    [ "poly-mask-2"
      [:path {:d b3 :fill "#fff" :style { :transform-origin "center" :transform "translate(20vw, 20vh)" :animation "woosh-2 10s infinite"}}]]
    [ "poly-mask-3"
      [:path {:d l1 :fill "#fff" :style { :transform-origin "center" :animation "lump-morph 10s infinite"}}]]

                [ "poly-mask-4"
                              [:path {:d l1 :fill "#fff" :style { :transform-origin "center" :transform "translate(20vw, 20vh)" }}]]
            ["bitey"
               (->>
                (gen-circ (pattern (str "noise-" white)) (* 0.5 @width) (* 0.5 @height) 1000)
                (style {:opacity 1 :transform "scale(.3)"})
                (draw))]
            ["cblt"
             (->>
              (gen-circ white (* 0.25 @width) (* 0.25 @height) 200)
              (draw))]
            ["cbrt"
             (->>
              (gen-circ white (* 0.75 @width) (* 0.25 @height) 200)
              (draw))]
            ["cblb"
             (->>
              (gen-circ white (* 0.25 @width) (* 0.75 @height) 200)
              (draw))]

            ["cbrb"
             (->>
              (gen-circ white (* 0.75 @width) (* 0.75 @height) 200)
              (draw))]
            ["clt"
               (map
                #(->>
                 (gen-circ white (* (rand) (* 0.5 @width)) (* (rand) (* 0.5 @height)) 100)
                 (style {:opacity 1 :transform "scale(.3)"})
                 (draw))
                (range 6))]
            ["crt"
               (map
                #(->>
                 (gen-circ white (+ (* 0.5 @width) (* (rand) (* 0.5 @width))) (* (rand) (* 0.5 @height)) 100)
                 (style {:opacity 1 :transform "scale(.3)"})
                 (draw))
                (range 6))]
            ["clb"
               (map
                #(->>
                 (gen-circ white (* (rand) (* 0.25 @width)) (+ (* 0.5 @height) (* (rand) (* 0.5 @height))) 100)
                 (style {:opacity 1 :transform "scale(.3)"})
                 (draw))
                (range 6))]
            ["crb"
               (map
                #(->>
                 (gen-circ white (+ (* 0.5 @width) (* (rand) (* 0.5 @width))) (+ (* 0.5 @height) (* (rand) (* 0.5 @height))) 100)
                 (style {:opacity 1 :transform "scale(.3)"})
                 (draw))
                (range 6))]
            ["circs"
              (gen-group {:style {:animation "ascend 10s infinite" }} (->>
                (gen-rect "#fff" 0 0 (* 1 @width) (* 1 @height))
                (draw))
               (map
                #(->>
                 (gen-circ "#000" (* (rand) @width) (* (rand) @height) 100)
                 (style {:opacity 1 :transform "scale(1)"})
                 (draw))
                (range 10)))]
            [ "cutout"
             (gen-group {} (->>
               (gen-rect white 10 12 (* 0.94 @width) (* 0.88 @height))
               (draw))
             (->>
               (gen-circ "#000" (* 0.7 @width) (* 0.7 @height) 100)
                (draw)))]
              ["rect-buds"
               (->>
                 (gen-rect white 10 12 (* 0.3 @width) (* 0.5 @height))
                 (draw))
                 ]
                ["na" [ :image {:key (random-uuid)
                                :x "0"
                                :y "0"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/blop.png"
                                :style {:transform-origin "center"
                                        :transform "scale(2)"} }]]
                ["nn" [ :image {:key (random-uuid)
                                :x "100"
                                :y "200"
                                :width "100%"
                                :height "100%"
                                :xlinkHref "img/blop.png"
                                :style {:transform-origin "center"
                                        :transform "scale(4)"
                                        } }]]
                ["bc" (->>
                 (gen-circ white (* 0.5 @width) (* 0.5 @height) 400)
                 (draw))]


            ])


(def masks (map (fn [[id & rest]] (apply gen-mask id rest)) mask-list))
