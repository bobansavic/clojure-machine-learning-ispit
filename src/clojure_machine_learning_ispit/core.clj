(ns clojure-machine-learning-ispit.core
  (:require
    [cortex.experiment.train :as train]
    [cortex.nn.execute :as execute]
    [cortex.nn.layers :as layers]
    [cortex.nn.network :as network]
    [clojure-machine-learning-ispit.dataset-processor :as dsp]
    :reload))

(def total-attempts (atom 0))
(def successful-predictions (atom 0))

(def team-og
  "OG")
(def team-liquid
  "Team Liquid")
(def team-rngu
  "Royal Never Give Up")
(def team-ig
  "Infamous Gaming")
(def team-secret
  "Team Secret")
(def team-lgd
  "PSG.LGD")
(def team-mineski
  "Mineski")
(def team-eg
  "Evil Geniuses")


;; Teams and performance stats scraped from https://www.dotabuff.com/esports/events/283-ti9-main-event
(def ti-2019-teams-and-stats
  [{:team team-og
    :stats [100, 75, 4.69, 2290, 2657]},
   {:team team-liquid
    :stats [85, 75, 4.22, 2223, 2536]},
   {:team team-rngu
    :stats [66, 60, 2.94, 2292, 2709]},
   {:team team-ig
    :stats [66, 50, 2.31, 2245, 2463]},
   {:team team-secret
    :stats [60, 58, 3.68, 2190 2537]},
   {:team team-lgd
    :stats [50, 60, 3.91, 2284, 2658]},
   {:team team-mineski
    :stats [50, 50, 3.67, 2584, 2746]},
   {:team team-eg
    :stats [33, 38, 3.65, 2097, 2443]}])

;;Match-ups for The DotA 2 International 2019 Main Event which can be examined at https://www.dotabuff.com/esports/events/283-ti9-main-event
(def ti-2019-matchups
  [{:team1 team-og :team2 team-liquid},
   {:team1 team-liquid :team2 team-lgd},
   {:team1 team-liquid :team2 team-secret},
   {:team1 team-og :team2 team-lgd},
   {:team1 team-secret :team2 team-eg},
   {:team1 team-liquid :team2 team-eg},
   {:team1 team-secret :team2 team-ig},
   {:team1 team-liquid :team2 team-rngu},
   {:team1 team-og :team2 team-eg}])

;; Network definition
(def neural-network (network/linear-network
                  [(layers/input 10 1 1 :id :input)
                   (layers/linear->relu 40)
                   (layers/batch-normalization)
                   (layers/dropout 0.9)
                   (layers/linear->relu 20)
                   (layers/linear->relu 10)
                   (layers/linear 2)
                   (layers/softmax :id :output)]))

;; Training
(def trained-network
  (binding [*out* (clojure.java.io/writer "dota-2-training.log")]
    (train/train-n neural-network (dsp/final(dsp/generate-transformed-dataset)) (dsp/final(dsp/generate-transformed-dataset))
                               :batch-size 27
                               :network-filestem "dota-2"
                               :epoch-count 5000)))

;; Can be used to load saved network from file
;(def trained-network (util/read-nippy-file "dota-2.nippy"))

;; Prints evaluation results and increments total-attempts and successful-predictions appropriately
(defn process-result [team1 stats1 team2 stats2 winner output]
  (swap! total-attempts inc)
  (prn "#################################################")
  (prn team1 stats1)
  (prn team2 stats2)
  (if (= team1 winner)
    (do
      (prn (str " -> Match winner was " winner ". Network predicted: " winner ". [CORRECT!]"))
      (swap! successful-predictions inc)
      (constantly nil))
    (do
      (prn (str " -> Match winner was " winner ". Network predicted: " winner ". [INCORRECT!]"))
      (constantly nil))))

;; Returns network accuracy percentage as a string
(defn network-accuracy
  [total success]
  (str (* 100 (float (/ success total))) "%"))

;; Goes through all 2019 matchups to validate the network and print the results.
(defn process-matchup
  [matchup]
  (let [team1 (get matchup :team1)
        team2 (get matchup :team2)]
    (let [stats1 (get (first (filter (comp #{team1} :team) ti-2019-teams-and-stats)) :stats)
          stats2 (get (first (filter (comp #{team2} :team) ti-2019-teams-and-stats)) :stats)]
      (let [output (get (first (execute/run trained-network [{:input (vec(concat stats1 stats2))}])) :output)]
        (let [output1 (bigdec (format "%.1f" (first output)))
              output2 (bigdec (format "%.1f" (last output)))]
          (cond
            (> output1 output2) (process-result team1 stats1 team2 stats2 team1 output1)
            (> output1 output2) (process-result team1 stats1 team2 stats2 team2 output2)))
        ))))



(defn -main
  []
  (prn "Viewing trained Network...")
  (prn "Trained network epochs/max error: " (str (select-keys trained-network [:epoch-count :cv-loss])))
  (prn "Validating network with statistics from The DotA 2 International 2019 Main Event...")
  (prn "[Team name] [Series win rate] [Matches win rate] [KDA - Kill/Death/Assistance avg] [GPM - Gold per minute] [XPM - Experience per minute]")
  (doall (map #(process-matchup %) ti-2019-matchups))
  (prn (str "Network accuracy: " (network-accuracy @total-attempts @successful-predictions)))
  )