(defproject transformation-workspace "0.1.0-SNAPSHOT"
  :description "A workspace template for defining an ohua IR transformation."
  :url "https://github.com/ohua-dev/ir-transformation-workspace"
  ; :license {:name "Eclipse Public License"
  ;           :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ohua/ohua "0.7.1"]
                 [rhizome "0.2.5"]]

  :source-paths ["src/clojure"]
  :test-source-paths ["test"]
  :java-source-paths ["src/java"]

  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options" "-g"]

  :jar-exclusions [#"\.java$"]
  )
