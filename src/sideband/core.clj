(ns sideband.core
  (:use [penumbra.opengl])
  (:require [penumbra
             [app :as app] 
             [data :as data]
             [text :as text]]))

(defn init [state]
  (app/title! "Sideband")
  ;(enable :texture-2d)
;  (point-size 5)
  (app/vsync! false)
  (enable :depth-test)
  (enable :lighting)
  (enable :light0)
  (shade-model :flat)
  state)

(defn reshape [[x y w h] state]
  (frustum-view 60.0 (/ (double w) h) 1.0 100.0)
  (load-identity)
  (translate 0 0 -10)
  (light 0 :position [1 1 1 0])
  state)

(defn mouse-drag 
  [[dx dy] [x y] button state]
  (assoc state :point [x y]))

(defn display 
  [[delta time] state]
  (let [rot (Math/abs (Math/sin time))]
    (text/with-font 
      (text/font "Inconsolata" :size (+ 10 (* 50 rot)))
      (text/write-to-screen (format "%d" (int (+ 10 (* 20 (rem time 2))))) 
                            (* 400 rot) (* 300 rot))
      (text/write-to-screen (format "%f" (* 100 (Math/abs (Math/sin time)))) 
                            (* 100 rot) (* 200 rot))
      ))
    (app/repaint!))

(defn display-proxy [& args]
  (apply display args))

(defn start 
  []
  (app/start {:display display-proxy 
              :mouse-drag mouse-drag :reshape reshape :init init} {}))
