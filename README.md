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

## License

Copyright © 2021 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
