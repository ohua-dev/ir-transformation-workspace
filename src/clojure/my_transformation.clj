(ns my-transformation
  (:require [com.ohua.ir :as ir]
            [com.ohua.lang :as ohua]
            [clojure.pprint :refer [pprint]]))


(ohua/ohua-require [mathops])


(defn no-op-transform 
  "Transformations are simply functions which take a program and return an (altered) program.
   In this case it returns the input data completely unchanged."
  [data]
  (println "Transformation running!")
  data ; returns the data unchanged
  )

(defn print-graph 
  "Another no-op transformation which prints the IR graph, the most important part of the data handed to the transformation."
  [data]
  (pprint (:graph data))
  data)

(defn is-replace-trigger? [node]
  (= 'mathops/inc (:name node)))

(defn make-dec [node]
  (assoc node :name 'mathops/dec))

(defn replace-transform [ { graph :graph ; binds the value with key `:graph` to the name `graph`
                            :as data
                          } ; {} syntax unwraps the data, :as additionally binds it to `data`
                        ]
  (let [updated-graph (ir/change-nodes-where is-replace-trigger? make-dec graph)]
    (assoc data :graph updated-graph) ; as we dont want to depend on a specific structure for the data its better to `modify` 
                                      ; only the parts of the whole structure we are interested in
    ))
