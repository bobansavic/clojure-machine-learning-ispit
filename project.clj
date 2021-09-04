(defproject clojure-machine-learning-ispit "0.1.0-SNAPSHOT"
  :description "A machine learning project using Cortex and featuring a multi-layer perceptron network model used to predict the outcome of professional DotA 2 matches."
  :plugins [[org.clojars.benfb/lein-gorilla "0.6.0"]]
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.csv "0.1.4"]
                 [thinktopic/experiment "0.9.22"]]
  :main "clojure-machine-learning-ispit.core"
  :repl-options {:init-ns clojure-machine-learning-ispit.core})
