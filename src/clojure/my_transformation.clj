(ns my-transformation
  (:require [com.ohua.ir :as ir]
            [com.ohua.lang :as ohua]))


(ohua/ohua-require [mathops])

; Transformations are always a function IRData -> IRData, hence we must return the altered data in the end
(defn no-op-transform [data]
  (println "Transformation running!")
  data ; returns the data unchanged
  )


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
