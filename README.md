# A workspace for building ohua IR transformations



## Contents

- *src/clojure/my_transformation.clj* 

    Contains the implementation for the custom IR passes.

- *src/java/mathops/* 

    Contains the stateful functions used in the `replace-transform` transformation and the test cases.

- *test/execution_test.clj* 
    
    Contains a series of tests to verify the defined transformations work as intended.
    This also serves as an example on how to invoke the ohua compiler with custom transformations.
