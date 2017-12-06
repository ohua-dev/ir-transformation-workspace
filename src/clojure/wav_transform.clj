(ns wav-transform
  (:import WavTransform))



(defn -main
  "A main function in clojure"
  [& args]
  (WavTransform/fromFile "oxp.wav" "oxp-transformed.wav"))
