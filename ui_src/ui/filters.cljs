(ns ui.filters)

(enable-console-print!)

(def turb
  (let [id "filter-tirb"]
    {:id id
     :def [:filter {:id id :key (random-uuid)}
           [:feTurbulence {:type "turbulence"
                           :baseFrequency "0.1 0.1"
                           :numOctaves "2"
                           :stitchTiles "stitch"
                           :result "turbulence"}]
           [:feDisplacementMap {:in2 "turbulence"
                                :in "SourceGraphic"
                                :scale "100"
                                :xChannelSelector "R"
                                :yChannelSelector "B"}]]}))

(def splotchy
  (let [id "filter-splotchy"]
    {:id id
     :def [:filter {:id id :key (random-uuid)}
           [:feTurbulence {:type "turbulence"
                           :baseFrequency "0.1 0.1"
                           :numOctaves "20"
                           :result "turbulence"}]
           [:feDisplacementMap {:in2 "turbulence"
                                :in "SourceGraphic"
                                :scale "600"
                                :xChannelSelector "R"
                                :yChannelSelector "B"}]]}))


(def disappearing
  (let [id "filter-disappearing"]
    {:id id
     :def [:filter {:id id :key (random-uuid)}
           [:feTurbulence {:type "turbulence"
                           :baseFrequency "0.5 0.8"
                           :numOctaves "2"
                           :result "turbulence"}]
           [:feDisplacementMap {:in2 "turbulence"
                                :in "SourceGraphic"
                                :scale "600"
                                :xChannelSelector "R"
                                :yChannelSelector "B"}]]}))

(def noiz
  (let [id "filter-noiz"]
    {:id id
     :def [:filter {:id id :key (random-uuid)}
           [:feTurbulence {:type "fractalNoise" :result "f1" :stitchTiles "noStitch" :baseFrequency "0.6" :numOctaves "1" :seed "0"}]
           [:feColorMatrix {:type "matrix" :values "-18 0 0 0 8 -18 0 0 0 8 -18 0 0 0 8 0 0 0 0 1" :in "f1" :result "f2"}]
           [:feColorMatrix {:type "luminanceToAlpha" :in "f2" :result "f3"}]
           [:feComposite {:in "SourceGraphic" :in2 "f3" :result "f4" :operator "in"}]
           [:feColorMatrix {:type "matrix" :values "1 0 0 0 0.22 0 1 0 0 0.22 0 0 1 0 0.22 0 0 0 1 0" :in "f4" :result "f5"}]
           [:feMerge [:feMergeNode {:in "SourceGraphic"}] [:feMergeNode {:in "f5"}]]]}))


(def soft-noiz
  (let [id "filter-soft-noiz"]
    { :id id
      :def [:filter {:id id :key (random-uuid)}
            [:feTurbulence {:type "fractalNoise" :result "f1" :stitchTiles "noStitch" :baseFrequency "0.6" :numOctaves "1" :seed "0"}]
            [:feColorMatrix {:type "luminanceToAlpha" :in "f2" :result "f3"}]
            [:feComposite {:in "SourceGraphic" :in2 "f3" :result "f4" :operator "in"}]
            [:feColorMatrix {:type "matrix" :values "1 0 0 0 0.22 0 1 0 0 0.22 0 0 1 0 0.22 0 0 0 1 0" :in "f4" :result "f5"}]
            [:feMerge [:feMergeNode {:in "SourceGraphic"}] [:feMergeNode {:in "f5"}]]]}))


(def blur
  (let [id "blurry"]
    {:id id
     :def [:filter
           {:id id :key (random-uuid)}
           [:feGaussianBlur {:in "SourceGraphic" :stdDeviation "2 0"}]]}))
