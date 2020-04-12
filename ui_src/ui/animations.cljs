(ns ui.animations
  (:require 
   [clojure.string :as string :refer [split join]]
   [ui.fills :as fills :refer
     [gray charcoal mint midnight navy blue orange
       br-orange pink white yellow]]
   [ui.shapes :as shapes :refer [tri square pent hex hept oct
                                 b1 b2 b3 b4]]
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


(fade-start! "fi" 1)


(make-frames! "etof" [0 100] (make-body "transform" ["translateY(10px)" "translateY(1000px)"]))

(back-and-forth! "scaley" "scale(1)" "scale(15)")
(back-and-forth! "scaley-huge" "scale(20)" "scale(50)")


(a-to-b! "new-fi" "fill-opacity" "0" ".5")
(a-to-b! "bbll" "fill" pink white)
(a-to-b! "sc-rot" "transform" "scale(4) rotate(0deg)" "scale(30) rotate(-80deg)")
(a-to-b! "slide-up" "transform" "translateY(110vh)" (str "translateY("(* 0.15 @height)")"))
(a-to-b! "grow2to3" "transform" "rotate(280deg) scale(1)" "rotate(280deg) scale(1.2)")

(fade-start! "fi" 1)

(make-frames! "scud"
             [10 20 25 60 73]
            (make-body "transform" [
                                    "translateX(-10vw)"
                                    "translateX(20vw)"
                                    "translateX(50vw)"
                                    "translateX(80vw)"
                                    "translateX(110vw)"]) )

(make-frames!
  "supercolor"
    [10, 35, 55, 85, 92]
   (make-body "fill" [pink pink yellow midnight midnight]))

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
  "colorcolorcolorcolor"
    [10, 35, 55, 85, 92]
   (make-body "fill" [
                      (pattern (str "noise-" midnight))
                      (pattern (str "noise-" mint))
                      (pattern (str "noise-" navy))
                      (pattern (str "noise-" white))
                      (pattern (str "noise-" midnight))]))

(make-frames!
  "woosh"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 50vh) rotate(2deg) scale(1.2)"
                           "translate(60vw, 60vh) rotate(-200deg) scale(2.4)"
                           "translate(40vw, 40vh) rotate(120deg) scale(4.4)"
                           "translate(20vw, 30vh) rotate(-1000deg) scale(3.2)"
                           "translate(60vw, 80vh) rotate(300deg) scale(6.2)"]))

(make-frames!
  "woosh-2"
    [10, 35, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 50vh) rotate(2deg) scale(11.2)"
                           "translate(60vw, 60vh) rotate(-200deg) scale(12.4)"
                           "translate(40vw, 40vh) rotate(120deg) scale(13.4)"
                           "translate(20vw, 30vh) rotate(-210deg) scale(12.2)"
                           "translate(60vw, 80vh) rotate(400deg) scale(6.2)"]))

(make-frames!
  "woosh-3"
    [10, 55, 85, 92]
   (make-body "transform" [
                           "translate(80vw, 10vh) rotate(2deg) scale(2.2)"
                           "translate(40vw, 40vh) rotate(120deg) scale(8.4)"
                           "translate(50vw, 30vh) rotate(0deg) scale(12.2)"
                           "translate(60vw, 80vh) rotate(400deg) scale(4.2)"]))

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
 "descend"
 [0 100]
 (make-body "transform"
             ["translate(-30vw, -30vh)"
              "translate(110vw, 80vh)"]))

(make-frames! 
 "ascend"
 [0 100]
 (make-body "transform"
             ["translate(110vw, 80vh)"
              "translate(-30vw, -30vh)"]))