(ns clojure-machine-learning-ispit.dataset-processor
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(def dota-path "resources/dota-2-ti-2014-2016.csv")

(with-open [reader (io/reader dota-path)]
  (doall (csv/read-csv reader)))

;; Get all entries of the csv without the header
(def dota-2-raw
  (drop 1 (with-open [reader (io/reader dota-path)]
            (doall (csv/read-csv reader :separator \tab)))))

;; Removes an element of a vector at a given position (used to remove team names and years)
(defn vec-remove
  [pos coll]
  (into (subvec coll 0 pos) (subvec coll (inc pos))))

;; Removes years and team names from the data set and splits into inputs and outputs
(defn trim-and-split
  [collection]
  (let [trimmed (into []
                      (map #(vec-remove 5 (vec-remove 0 (vec-remove 0 %)))) collection)]
    (into []
          (map #(split-at 10 %)) trimmed)))

(defn parse-string-inputs
  [in]
  (Float/parseFloat in))

(defn make-map
  [sequence]
  {:input (drop-last sequence)
   :output (last sequence)})

;; Transforms string inputs into float values
(def transform-inputs
  (comp
    (map first)
    (map #(map parse-string-inputs %))
    (map vec)))

;; Transforms "1" and "2" outputs into [1 0] and [0 1] respectively
(def transform-outputs
  (comp
    (map last)
    (map (fn [winner]
           (let [w (first winner)]
             (case w
               "1" [1 0]
               "2" [0 1]))))))

;; Trims and splits the data set, transforms inputs and outputs into necessary formats of each row
;; (float and two-value vector respectively) separately and joins them back into a single collection.
(defn generate-transformed-dataset []
  (let [data (trim-and-split dota-2-raw)]
    (let [ins (into [] transform-inputs data)
          outs (into [] transform-outputs data)]
      (map conj ins outs))))

(defn final [collection]
  (seq (into []
             (map #(make-map %)) collection)))
