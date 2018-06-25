(ns ui.filters)

(enable-console-print!)

(defn filt 
  [{ fill-id :id }]
  (str "url(#" fill-id ")"))

(def turb
  (let [id "filter-tirb"]
    { :id id
      :def [:filter { :id id :key (random-uuid) }
        [:feTurbulence { :type "turbulence" 
                         :baseFrequency "0.05"
                         :numOctaves "2" 
                         :result "turbulence" }]
        [:feDisplacementMap { :in2 "turbulence" 
                              :in "SourceGraphic"
                              :scale "50" 
                              :xChannelSelector "R" 
                              :yChannelSelector "G" }]]}))
    
(def noiz 
  (let [id "filter-noiz"]
    { :id id
      :def [:filter { :id id :key (random-uuid) }
        [ :feTurbulence { :type "fractalNoise" :result "f1" :stitchTiles "noStitch" :baseFrequency "0.6" :numOctaves "1" :seed "0" } ]
        [ :feColorMatrix { :type "matrix" :values "-18 0 0 0 8 -18 0 0 0 8 -18 0 0 0 8 0 0 0 0 1" :in "f1" :result "f2" } ]
        [ :feColorMatrix { :type "luminanceToAlpha" :in "f2" :result "f3" }]
        [ :feComposite { :in "SourceGraphic" :in2 "f3" :result "f4" :operator "in" } ]
        [ :feColorMatrix { :type "matrix" :values "1 0 0 0 0.22 0 1 0 0 0.22 0 0 1 0 0.22 0 0 0 1 0" :in "f4" :result "f5" } ]
        [ :feMerge [:feMergeNode { :in "SourceGraphic" } ] [:feMergeNode { :in "f5" } ]]]
    }))

(def soft-noiz 
  (let [id "filter-soft-noiz"]
    { :id id
      :def [:filter { :id id :key (random-uuid) }
        [ :feTurbulence { :type "fractalNoise" :result "f1" :stitchTiles "noStitch" :baseFrequency "0.6" :numOctaves "1" :seed "0" } ]
        [ :feColorMatrix { :type "luminanceToAlpha" :in "f2" :result "f3" }]
        [ :feComposite { :in "SourceGraphic" :in2 "f3" :result "f4" :operator "in" } ]
        [ :feColorMatrix { :type "matrix" :values "1 0 0 0 0.22 0 1 0 0 0.22 0 0 1 0 0.22 0 0 0 1 0" :in "f4" :result "f5" } ]
        [ :feMerge [:feMergeNode { :in "SourceGraphic" } ] [:feMergeNode { :in "f5" } ]]]
    }))
