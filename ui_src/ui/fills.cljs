(ns ui.fills)

;; solid colors
(def mint "#00baa9")
(def orange "#f80")
(def navy "#161f93")
(def pink "#fb5d67")

;; patterns

(def pink-squares { :id "rect-circles-2"
                    :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nMS41JyBjeT0nMS41JyByPScxLjUnIGZpbGw9JyNmZjAwNDgnLz4KPC9zdmc+Cg==" })

;; pattern gen fn for defs on init
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
