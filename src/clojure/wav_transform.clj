(ns wav-transform
  (:require [com.ohua.lang :refer :all]
            [com.ohua.compile :refer [enable-ir-graphs]])
  (:import WavTransform
           (com.ohua.engine RuntimeProcessConfiguration RuntimeProcessConfiguration$Parallelism)
           (java.util Properties)))

;;;
;;; Just like in any other program, we have to declare which stateful
;;; functions our algorithm is supposed to use.
;;;
(ohua-require [ohua :refer [mergeChannels ifft fft filter]])

;;;
;;; The below line is a compiler flag which enables compiler output.
;;; Use it to see the form of the program in the dataflow IR before
;;; and after transformations were applied. It will print the IR to
;;; standard out and also produce files (in the "test" folder) that
;;; allow you to see the final dataflow graph using graphviz.
;;;
;(enable-ir-graphs)


;;;
;;; Runtime configuration that enables a parallel execution.
;;;
(defn parallel-config []
  (doto (RuntimeProcessConfiguration.)
    (.setProperties (doto (new Properties)
                      (.setProperty "core-thread-pool-size" "5")
                      (.setProperty "execution-mode" (.name (RuntimeProcessConfiguration$Parallelism/MULTI_THREADED)))
                      ))))

(defn ohua-based-wav-transform [inFilePath outFilePath]
  (let [inputFile (WavTransform/openWavFile inFilePath)
        outputFile (WavTransform/createTargetFile inputFile outFilePath)
        frames (WavTransform/loadFrames inputFile)
        _ (assert (== 2 (.getNumChannels inputFile)))
        ;;;
        ;;; Here goes our Ohua algorithm that maps over the blocks of the
        ;;; WAV file (= pipeline parallelism). It assumes (for the sake of
        ;;; this exercise) that there exist exactly 2 channels in the WAV
        ;;; file and processes them independently (= task-level parallelism).
        ;;; It returns the resulting blocks.
        ;;;
        resultBlocks (<-ohua
                       ; our wav-transform algorithm
                       (smap
                         (algo [block]
                               (let [[channels read] block
                                     [channel1 channel2] channels
                                     result1 (ifft (filter (fft channel1)))
                                     result2 (ifft (filter (fft channel2)))]
                                 (mergeChannels result1 result2 read)))
                         frames)

                       ; enable concurrent/parallel execution (default is single-threaded)
                       :run-with-config (parallel-config)
                       )]
    (WavTransform/writeFrames resultBlocks outputFile)
    (.close inputFile)
    (.close outputFile)
    )
  )

(defn -main
  "A main function in clojure"
  [& args]
  ;(WavTransform/fromFile "oxp.wav" "oxp-transformed.wav")
  (ohua-based-wav-transform "oxp.wav" "oxp-transformed.wav")
  )
