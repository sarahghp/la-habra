(ns ui.patterns)

;; patterns

(def pink-circs { :id "rect-circles-2"
                    :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nMS41JyBjeT0nMS41JyByPScxLjUnIGZpbGw9JyNmZjAwNDgnLz4KPC9zdmc+Cg==" })

(def pink-stripes { :id "pink-stripe-3"
                    :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPHJlY3QgeD0nMCcgeT0nMCcgd2lkdGg9JzEwJyBoZWlnaHQ9JzMnIGZpbGw9JyNmZjAwNDgnIC8+Cjwvc3ZnPg=="})

(def blue-circs { :id "hex-circles-4"
                  :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nMi41JyBjeT0nMi41JyByPScyLjUnIGZpbGw9JyMwMDU0YTgnLz4KPC9zdmc+"})

(def gray-circs { :id "gray-circles-3"
                  :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nMicgY3k9JzInIHI9JzInIGZpbGw9JyM0YTRmNTQnLz4KPC9zdmc+"})

(def gray-circs-lg { :id "gray-circles-7"
                     :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nNCcgY3k9JzQnIHI9JzQnIGZpbGw9JyM0YTRmNTQnLz4KPC9zdmc+"})

(def shadow { :id "gray-circles-9"
              :image-link "data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMCcgaGVpZ2h0PScxMCc+CiAgPHJlY3Qgd2lkdGg9JzEwJyBoZWlnaHQ9JzEwJyBmaWxsPSdoc2xhKDM2MCwgMTAwJSwgMTAwJSwgMCknIC8+CiAgPGNpcmNsZSBjeD0nNScgY3k9JzUnIHI9JzUnIGZpbGw9JyM0YTRmNTQnLz4KPC9zdmc+"})

;; pattern gen fn for defs on init
(defn pattern
  [{ :keys [id image-link] }]
  [:pattern { :id id
              :patternUnits "userSpaceOnUse"
              :width "10"
              :height "10"
              :key (random-uuid)}
    [:image { :xlinkHref image-link
              :x "0"
              :y "0"
              :width "10"
              :height "10"
              :key (random-uuid) }]])
