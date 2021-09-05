# clojure-machine-learning-ispit

A machine learning project using Cortex and featuring a multi-layer perceptron network model used to predict the outcome of professional DotA 2 matches.

Using the team performance and match-up statistics from 2014 to 2016 available on dotabuff.com we are able to train the network and then introduce new data from 2019 to validate it.

We are also able to save the trained network in the dota-2.nippy file as well as view the training log within dota-2-training.log.

# Stats
The statistics used for the training data set and validation have been scraped from:

https://www.dotabuff.com/esports/events/8-the-international-2014-main-event

https://www.dotabuff.com/esports/events/41-ti5-main-event

https://www.dotabuff.com/esports/events/125-ti6-main-event

https://www.dotabuff.com/esports/events/283-ti9-main-event

## Usage

lein run

Just keep in mind it will take up to about a minute for the training to be complete before any results can be examined.
