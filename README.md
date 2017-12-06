# A workspace for building ohua IR transformations

This repository is intended as a starting point for creating a custom compiler pass/program transformation on ohuas intermediate representation.


## Contents

- *src/java/* 

    Contains the java helper classes for the WAVE filter.

- *src/java/WavTransform.java*

    Is the pure java implementation for the WAVE filter. This contains the algorithm that you should disassemble and make into a ohua program.

- *src/clojure/my_transformation.clj* 

    Contains the example implementation for the custom IR passes.

- *src/java/mathops/* 

    Contains the stateful functions used in the `replace-transform` transformation and the test cases.

- *test/execution_test.clj* 
    
    Contains a series of tests to verify the defined transformations work as intended.
    This also serves as an example on how to invoke the ohua compiler with custom transformations.
