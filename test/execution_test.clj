(ns execution-test
  (:require [clojure.test :refer [is deftest]]
            [com.ohua.lang :refer [<-ohua ohua-require]])
  (:use my-transformation))


(ohua-require [mathops :refer [inc dec]])

; prints that the transformation is running and otherwise just executes the entered program
(deftest print-in-noop-transform
  (is (= 0 
    (<-ohua (id (int 0)) 
      :compile-with-config {:df-transformations [no-op-transform]}))))



(deftest make-inc-to-dec
  (is 
    (= 2
      (<-ohua
        (inc (inc (int 0))))))
  (is 
    (= 0 
      (<-ohua
        (inc (inc (int 2)))
        :compile-with-config {:df-transformations [replace-transform]})))
  (is 
    (= 
      (<-ohua
        (dec (dec (int 2))))
      (<-ohua
        (inc (inc (int 2)))
        :compile-with-config {:df-transformations [replace-transform]}))))
