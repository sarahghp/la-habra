(ns ui.generators)

;; ------------------------ SETTINGS  ---------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

(def settings {:width @width
               :height @height })

;; ------------------------ WRAPPERS ---------------------

(defn circ
  [{:keys [x y r style mask] :or {mask ""}}]
  [:circle { :cx x
             :cy y
             :r r
             :style style
             :mask mask
             :key (random-uuid)} ])

(defn line
  [{:keys [first-point second-point color width style] :or { width 4 style {} }}]
  [:line { :x1 (first-point 0)
           :y1 (first-point 1) 
           :x2 (second-point 0)
           :y2 (second-point 1)
           :stroke color
           :stroke-width width
           :style style
           :key (random-uuid) }])

(defn polygon
  [{:keys [points style mask] :or {mask ""}}]
  [:polygon { :key (random-uuid)
              :points points
              :style style
              :mask mask }])

(defn rect
 [{:keys [x y w h style mask] :or {mask ""}}]
 [:rect { :x x
          :y y
          :width w
          :height h
          :style style
          :mask mask
          :key (random-uuid)} ])
          
(defn shape
  [{:keys [d style mask] :or {mask ""}}]
  [:path { :key (random-uuid) 
           :d d 
           :style style 
           :mask mask }])



;; ------------------------ GENERATORS ---------------------

(defn gen-circ
  [fill-string x y radius & mask]
  { :x x
    :y y
    :r radius
    :mask mask
    :style { :fill fill-string }})
    
(defn gen-group
  ([internals] (gen-group {} internals))
  ([{ :keys [style mask] :or { style {} mask "" } } & internals]
    [:g { :key (random-uuid) :style style :mask mask } internals ]))

(defn gen-line
  [first-point second-point color & width]
  { :first-point first-point
    :second-point second-point
    :color color 
    :width width
    :style {}})

(defn gen-poly
  [fill-string points & mask]
  { :points points 
    :style { :fill fill-string } 
    :mask mask})
    
(defn gen-rect
  [fill-string x y w h & mask]
  { :x x
    :y y
    :w w
    :h h
    :mask mask
    :style {
      :fill fill-string }})
    
(defn gen-shape
  [fill-string path & mask]
  { :style { :fill fill-string }
    :mask mask
    :d path })

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
  ([x y r num color] (freak-out x 0 y 0 r num color {}))
  ([x y r num color style] (freak-out 0 x 0 y r num color style))
  ([min-x max-x min-y max-y r num color] (freak-out min-x max-x min-y max-y r num color {}))
  ([min-x max-x min-y max-y max-r num color style]
  [:g { :key (random-uuid) 
        :style style } 
      (map 
        #(circ (gen-circ color (+ min-x (rand (- max-x min-x))) (+ min-y (rand (- max-y min-y))) (rand max-r))) 
        (range num))]))

(defn gen-grid
  ([cols rows offset base-obj]
    (let [x (base-obj :x) 
          y (base-obj :y)
          a-off (offset :col)
          b-off (offset :row)]
            (set (for [a (range cols) b (range rows)]
              (merge base-obj {:x (+ x (* a a-off))} {:y (+ y (* b b-off))}))))))
