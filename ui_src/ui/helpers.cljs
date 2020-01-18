(ns ui.helpers)

(defn cos [x] (.cos js/Math x))
(defn sin [x] (.sin js/Math x))

(defn style
  [changes shape]
  (update-in shape [:style] #(merge % changes)))

(defn url
  ([fill-id]
   (str "url(#" fill-id ")")))

(defn val-cyc
  [frame vals]
  (let [n (count vals)]
    (nth vals (mod frame n))))
