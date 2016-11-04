package samples

import probability_monad.Distribution
import probability_monad.Distribution.discrete
import probability_monad.Distribution.{always,tf}

object WeatherPrediction extends App {
   def weather = {
    def transition(rain: Boolean): Distribution[Boolean] = rain match {
      case true => discrete(true -> 0.5, false -> 0.5)
      case false => discrete(true -> 0.1, false -> 0.9)
    }
    for {
      monday <- tf()
      thursday <- always(monday).markov(3)(transition)
    } yield (monday, thursday)
  }
  def runWeather = weather.given{ case (monday, thursday) => thursday }.map(_._1)
  
  runWeather.hist
}