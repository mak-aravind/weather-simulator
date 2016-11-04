# Artificial weather data generation

A reference implementation in weather domain, to demonstrate the learning and application of functional programming, in parallel computing, with the choice of Apache Spark and scala.

#### The approach

 * For a set of pre determined locations using [markov-chain ](https://en.wikipedia.org/wiki/Examples_of_Markov_chains#A_simple_weather_model) model generated training data related to the weather: Condition, Temperature, Pressure, Humidity.
 * Format the training data into LIBSVM and feed to spark mlib's RandomForest
 * Build a RandomForest model with the training data for Temperature, Pressure, Humidity and Condition
 * For a new feed: time and location, with the aid of generated model predict the Condition, Temperature, Pressure and Humidity

#### Choice of technology stack:
 1. Apache Spark Mlib - RandomForest
 2. Scala 11.8
 3. Inspired by python's numpy and scipy in data science. Explored similar library in scala:
   * [probability-monad](https://github.com/jliszka/probability-monad) - Markov Chain and probability
 4. sbt - Build tool

#### Usage

Generate the training data

> sbt "run-main com.mak.weather.model.TrainingDataGenerator"

To generate simulated weather data for 10 random positions
> sbt "run-main com.mak.weather.station.Simulator"

To predict a weather for a known place say
Bangalore : 12.97, 77.59, 12

command : sbt "run-main com.mak.weather.station.Simulator [latitude longitude elevation time]"

> sbt "run-main com.mak.weather.station.Simulator 12.97 77.59 12 2016-11-04T14:12:43"
