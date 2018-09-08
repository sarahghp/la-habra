(ns ui.generators)

;; ------------------------ SETTINGS  ---------------------

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

(def settings {:width @width
               :height @height })

(enable-console-print!)

;; ------------------- SIMPLE GENERATORS ---------------------

(defn gen-circ
  [fill-string x y radius & mask]
  { :cx x
    :cy y
    :r radius
    :mask mask
    :type :circle
    :style {:fill fill-string
            :transform-origin "center"}})
    
(defn gen-line
  [first-point second-point color & width]
  { :x1 (first-point 0)
    :y1 (first-point 1) 
    :x2 (second-point 0)
    :y2 (second-point 1)
    :stroke color 
    :stroke-width (or width 4)
    :type :line
    :style {:transform-origin "center"}})

(defn gen-poly
  [fill-string points & mask]
  { :points points
    :mask mask 
    :type :polygon
    :style {:fill fill-string
            :transform-origin "center"}})

(defn gen-rect
  [fill-string x y w h & mask]
  { :x x
    :y y
    :width w
    :height h
    :mask mask
    :type :rect
    :style {:fill fill-string
            :transform-origin "center"}})

(defn gen-shape
  [fill-string path & mask]
  { :d path
    :mask mask
    :type :path
    :style {:fill fill-string
            :transform-origin "center"}})


;; -------------------------- DRAW ----------------------------

(defn draw [{:keys [type] :as attrs}]
  (let [basics #{:circle :line :polygon :rect :path}]
    (if (contains? basics type) 
      [type (merge attrs {:key (random-uuid)})]
      '())))


;; ------------------- COMPOUND GENERATORS ---------------------

(defn gen-group
  ([internals] (gen-group {} internals))
  ([{ :keys [style mask] :or { style {} mask "" } } & internals]
    [:g { :key (random-uuid) :style style :mask mask } internals ]))

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


(defn gen-cols
  [color stroke-width num-cols offset]
  (->> (range num-cols)
       (map #(gen-line 
               [(* % offset) 0] 
               [(* % offset) @height] 
               color stroke-width))
       (map draw)))

(defn gen-rows
  [color stroke-width num-rows offset]
  (->> (range num-rows)
       (map #(gen-line 
               [0 (* % offset)] 
               [@width (* % offset)] 
               color stroke-width))
       (map draw)))

(defn gen-line-grid
  [color stroke-width cols rows offset]
      (let [a-off (offset :col)
            b-off (offset :row)]
            (->> (range cols)
                 (map #(gen-line 
                         [(* % a-off) 0] 
                         [(* % a-off) @height] 
                         color stroke-width))
                 ((fn [coll] 
                  (into coll
                        (map #(gen-line 
                                [0 (* % b-off)] 
                                [@width (* % b-off)] 
                                color stroke-width)
                             (range rows)))))
                 (map draw))))

(defn gen-grid-raw
  ([cols rows offset base-obj]
    (let [x-key (if (base-obj :cx) :cx :x)
          y-key (if (base-obj :cy) :cy :y)
          x (base-obj x-key)  
          y (base-obj y-key)
          a-off (offset :col)
          b-off (offset :row)]
            (set (for [a (range cols) b (range rows)]
              (merge base-obj {x-key (+ x (* a a-off))} {y-key (+ y (* b b-off))}))))))

(def gen-grid (memoize gen-grid-raw))

(defonce positions 
  (atom {}))

(defn update-me-positions
  [old-pos {:keys [min-x min-y max-x max-y max-r] :as id}]
  (let [new-vals (->> 
                 old-pos
                 last
                 (map #(assoc 
                        % 
                        :cx (+ min-x (rand (- max-x min-x)))
                        :cy (+ min-y (rand (- max-y min-y)))
                        :r (rand max-r))))
        update-fn (fn [atom-val new-vals]
                      (merge (atom-val id) [1] new-vals))]
    (swap! positions update-fn new-vals)
    (@positions id)))

(defn generate-and-add-positions
  [{:keys [min-x min-y max-x max-y max-r] :as min-maxes} color style num]
  (let [drawing [:g { :key (random-uuid) 
                      :style style } 
                    (map 
                      #(draw (gen-circ 
                              color 
                              (+ min-x (rand (- max-x min-x))) 
                              (+ min-y (rand (- max-y min-y))) 
                              (rand max-r))) 
                      (range num))]] 
  (swap! positions
         assoc
         min-maxes)
    drawing))

(defn add-and-retrieve!
  [min-maxes color style num] ;a map with the keys 
  (if-let [pos (@positions min-maxes)]
    (update-me-positions min-maxes pos) 
    (generate-and-add-positions min-maxes color style num)))

(defn freak-out
  ([x y r num color] (freak-out x 0 y 0 r num color {}))
  ([x y r num color style] (freak-out 0 x 0 y r num color style))
  ([min-x max-x min-y max-y r num color] (freak-out min-x max-x min-y max-y r num color {}))
  ([min-x max-x min-y max-y max-r num color style]
    (add-and-retrieve! 
     {:min-x min-x 
      :max-x max-x 
      :min-y min-y 
      :max-y max-y 
      :max-r max-r}
     color 
     style
     num)))
