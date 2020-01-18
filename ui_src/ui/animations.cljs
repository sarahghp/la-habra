(ns ui.animations
  (:require [clojure.string :as string :refer [split join]]))


;; ------------------------------------------------------ ;;
;; --------------------- ANIMATIONS --------------------- ;;
;; ------------------------------------------------------ ;;

;; -------------------- CSS MANIP HELPERS ------------------

(defn make-body
  [att values]
  (let [decorated-att (str att ": ")
        decorated-vals (map #(str % ";") values)]
    (map join (partition 2
               (interleave
                 (repeat (count decorated-vals) decorated-att)
                 decorated-vals)))))

(defn splice-bodies
  [& bodies]
  (->> bodies
    (map #(split % #";"))
    (apply map vector)
    (apply map #(str % ";" %2 ";"))))

(defn frames-and-bodies
  [frames bodies]
  (->> bodies
      (map #(apply str % " }"))
      (interleave (map #(str % "% { ") frames))
      (apply str)))

;; get the key frames string, append it to the stylesheet, return name
(defn make-frames!
  [name frames bodies]
  (let [sheet (aget js/document "styleSheets" "0")
        sheet-length (aget sheet "cssRules" "length")
        keyframes (str "@keyframes " name "{ " (frames-and-bodies frames bodies) "}")]
       (.insertRule sheet keyframes sheet-length)
       name))

(defn seconds-to-frames
  [seconds]
  (* 2 seconds))

(defn frames-to-seconds
  [frames]
  (* 0.5 frames))

;; -------------------- SHAPE ANIMATION SWITCHER ---------------------------

(defonce ran (atom {}))

(defn anim-and-hold
  [name frame duration fader solid]
  (let [init-frame (@ran name)
        ran? (and init-frame (<= (+ init-frame (seconds-to-frames duration)) frame))
        ret (if ran? solid fader)]
    (when-not init-frame (swap! ran assoc name frame))
    ret))


;; -------------------- SHAPE ANIMATION HELPER ---------------------------

(defn anim
  ([name duration count shape] (anim name duration count {} shape))
  ([name duration count opts shape]
   (let [animations
         { :animation-name name
           :animation-fill-mode "forwards"
           :animation-duration duration
           :animation-iteration-count count
           :animation-delay (or (:delay opts) 0)
           :animation-timing-function (or (:timing opts) "ease")}]
        (update-in shape [:style] #(merge % animations)))))

;; -------------------- SOME BASE KEYFRAMES ---------------------------

(make-frames!
  "fade-in-out"
  [0 4 8 50 54 94]
  (make-body "fill-opacity" [1 0.7 0.5 0.2 0 1]))

(make-frames!
  "fade-out"
  [0 100]
  (make-body "fill-opacity" [1 0]))

(make-frames!
  "fade-in"
  [0 30 80 90 100]
  (make-body "fill-opacity" [0 0 0.5 1 1]))

(make-frames!
  "wee-oo"
  [0 17 37 57 100]
  (make-body "transform"
    [ "translateX(1%) scale(1)"
      "translateX(60%) scale(1.4)"
      "translateX(70%) scale(2.5)"
      "translateX(100%) scale(13.9)"
      "translateX(1%) scale(1)"]))

(make-frames!
  "rot"
  [0 100]
  (make-body "transform" ["rotate(0deg)" "rotate(360deg)"]))

(make-frames!
  "cent-rot"
  [0 100]
  (make-body "transform" ["translate(300px, 300px) scale(6.2) rotate(0deg)" "translate(300px, 300px) scale(1.2) rotate(360deg)"]))

(make-frames!
  "rev"
  [0 100]
  (make-body "transform" ["rotate(0deg)" "rotate(-360deg)"]))

;; ------------------- BLINKING ---------------------------

(defn frame-mod [val n frame] (= val (mod frame n)))
(def nth-frame (partial frame-mod 0))
(defn even-frame [frame] (nth-frame 2 frame))
(defn odd-frame [frame] (frame-mod 1 2 frame))
