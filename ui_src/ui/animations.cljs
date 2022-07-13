(ns ui.animations
  (:require 
   [clojure.string :as string :refer [split join]]
   [ui.fills :as fills :refer
     [gray charcoal mint midnight navy blue orange
       br-orange pink white yellow]]
   [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                 b1 b2 b3 b4
                                 l1 l2 l3 l4 l5 l6]]
   [ui.generators :refer
    [freak-out new-freakout scatter lerp
     gen-circ gen-line gen-poly gen-rect gen-shape draw
     gen-group gen-offset-lines gen-bg-lines gen-mask
     gen-grid gen-line-grid gen-cols gen-rows]]
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
        shadow noise]]))


;; ------------------------------------------------------ ;;
;; --------------------- ANIMATIONS --------------------- ;;
;; ------------------------------------------------------ ;;

(def width (atom (.-innerWidth js/window)))
(def height (atom (.-innerHeight js/window)))

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
        keyframes (str "@keyframes " name "{ " (frames-and-bodies frames bodies) "}" )]
        (.insertRule sheet keyframes sheet-length)
          name ))

(defn seconds-to-frames
  [seconds]
  (* 2 seconds))

(defn frames-to-seconds
  [frames]
  (* 0.5 frames))


;; ------------------- BLINKING ---------------------------

(defn frame-mod [val n frame] (= val (mod frame n)))
(def nth-frame (partial frame-mod 0))
(defn even-frame [frame] (nth-frame 2 frame))
(defn odd-frame [frame] (frame-mod 1 2 frame))


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

(defn back-and-forth!
  [name start-str finish-str]
  (make-frames! name [0 50 100]
    (make-body "transform" [
      (str start-str)
      (str finish-str)
      (str start-str)])))

(defn a-to-b!
  [name att start-str finish-str]
  (make-frames! name [0 100]
    (make-body att [
      (str start-str)
      (str finish-str)])))


(defn fade-start!
  [name op-end]
  (make-frames! name [0 99 100]
    (make-body "fill-opacity" [
      (str 0)
      (str 0)
      (str op-end)])))

;; -------------------- ANIMATION DEFINITIONS ---------------------------

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


(fade-start! "fi" 1)

(make-frames!
  "colorcolor"
    [10, 35, 55, 85, 92]
   (make-body "fill" [pink yellow br-orange white midnight]))

(make-frames!
  "colorcolorcolor"
    [10, 35, 55, 85, 92]
   (make-body "fill" [
                      (pattern (str "noise-" pink))
                      (pattern (str "noise-" yellow))
                      (pattern (str "noise-" br-orange))
                      (pattern (str "noise-" white))
                      (pattern (str "noise-" midnight))]))

 (make-frames!
   "loopy-left"
     [10, 35, 55, 85, 92]
    (make-body "transform" [
                            "translate(90vw, 10vh) rotate(2deg) scale(2.2)"
                            "translate(80vw, 30vh) rotate(220deg) scale(6.4)"
                            "translate(60vw, 40vh) rotate(0deg) scale(4.2)"
                            "translate(30vw, 80vh) rotate(-300deg) scale(2.2)"
                            "translate(10vw, 90vh) rotate(400deg) scale(3.2)"]))

(make-frames!
   "loopy-right"
     [10, 35, 55, 85, 92]
    (make-body "transform" [
                            "translate(10vw, 10vh) rotate(2deg) scale(2.2)"
                            "translate(30vw, 80vh) rotate(220deg) scale(6.4)"
                            "translate(60vw, 40vh) rotate(0deg) scale(4.2)"
                            "translate(80vw, 30vh) rotate(-300deg) scale(2.2)"
                            "translate(90vw, 90vh) rotate(400deg) scale(3.2)"]))

(make-frames!
 "dashy"
 [100]
 (make-body "stroke-dashoffset" [0]))

(make-frames!
 "morph"
  [0 15 30 45 60 75 100]
 (make-body "d" [
  (str "path('"tri"')")
  (str "path('"square"')")
  (str "path('"pent"')")
  (str "path('"hex"')")
  (str "path('"hept"')")
  (str "path('"oct"')")
  (str "path('"tri"')")
]))

(make-frames!
 "morph-2"
  [0 15 30 45 60 75 100]
 (make-body "d" [
  (str "path('"pent"')")
  (str "path('"square"')")
  (str "path('"pent"')")
  (str "path('"hex"')")
  (str "path('"hept"')")
  (str "path('"oct"')")
  (str "path('"hept"')")
]))

(make-frames!
 "lump-morph"
  [0 15 30 45 60 75 100]
 (make-body "d" [
  (str "path('"l1"')")
  (str "path('"l2"')")
  (str "path('"l3"')")
  (str "path('"l4"')")
  (str "path('"l5"')")
  (str "path('"l6"')")
  (str "path('"l1"')")
]))


(make-frames!
  "peak-r"
  [30 75]
  (make-body "transform" [
    "translate(80vw, 50vh) scale(3)"
    "translate(30vw, 80vh) scale(1.7)"
  ]))
  
(make-frames!
  "peak-r-rot"
  [30 75]
  (make-body "transform" [
    "translate(80vw, 50vh) scale(3) rotate(200deg)"
    "translate(30vw, 80vh) scale(1.7) rotate(-2000deg)"
  ])) 
  
(make-frames!
  "peak-l"
  [0 30 75 100]
  (make-body "transform" [
    "translate(80vw, 0vh) scale(1.7)"
    "translate(0vw, 50vh) scale(4)"
    "translate(30vw, 80vh) scale(2.7)"
    "translate(80vw, 0vh) scale(1.7)"
  ]))

(make-frames!
  "peak-l-rot"
  [0 30 75 100]
  (make-body "transform" [
    "translate(80vw, 0vh) scale(1.7) rotate(200deg)"
    "translate(0vw, 50vh) scale(4) rotate(-1000deg)"
    "translate(30vw, 80vh) scale(2.7) rotate(200deg)"
    "translate(80vw, 0vh) scale(1.7) rotate(1000deg)"
  ]))
  
(make-frames!
  "woosh"
  [0 30 55 75 100]
  (make-body "transform" [
    "translate(20vw, 0vh) scale(1.7) rotate(200deg)"
    "translate(30vw, 80vh) scale(4) rotate(-1000deg)"
    "translate(0vw, 50vh) scale(4) rotate(-1000deg)"
    "translate(80vw, 400vh) scale(2.7) rotate(200deg)"
    "translate(80vw, 0vh) scale(1.7) rotate(1000deg)"
  ]))
  
(make-frames!
  "woosh-2"
  [0 30 55 75 100]
  (make-body "transform" [
    "translate(20vw, 0vh) scale(1.7) rotate(200deg)"
    "translate(30vw, 80vh) scale(4) rotate(-1000deg)"
    "translate(0vw, 50vh) scale(8) rotate(1000deg)"
    "translate(80vw, 40vh) scale(2.7) rotate(200deg)"
    "translate(20vw, 10vh) scale(1.7) rotate(1000deg)"
  ]))

(a-to-b!
  "ascend"
  "transform"
  "translateY(0vh)"
  "translateY(-100vh)")
  
(a-to-b!
  "descend"
  "transform"
  "translateY(0vh)"
  "translateY(100vh)")