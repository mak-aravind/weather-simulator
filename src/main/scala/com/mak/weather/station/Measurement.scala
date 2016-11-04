package com.mak.weather.station

import com.mak.weather.domain.{Position,Condition,Locations}


case class Measurement(number: Int,
                       position: Position, localTime: LocalTime,
                       location: Option[String] = Option("Unknown"),
                       condition: Option[Condition.condition] = Option(Condition.Unknown),
                       temperature: Option[Temperature] = Option(Temperature()),
                       pressure: Option[Pressure] = Option(Pressure()),
                       humidity: Option[Humidity] = Option(Humidity())
                      ) {
  measurement =>

  def emit(): Measurement = {
    require(measurement.condition.get.equals(Condition.Unknown), 
        "Measurement has weather data already")
    require(measurement.temperature.get.value.isNaN, 
        "Measurement has weather data already")
    require(measurement.pressure.get.value.isNaN, 
        "Measurement has weather data already")
    require(measurement.humidity.get.value.isNaN, 
        "Measurement has weather data already")

    import com.mak.weather.prediction.Predictor
    
    val sample = Predictor.predict(localTime, position)

    val condition = Option(Condition.apply(sample("Condition").toInt))
    val temperature = Option(Temperature(sample("Temperature")))
    val pressure = Option(Pressure(sample("Pressure")))
    val humidity = Option(Humidity(sample("Humidity")))
    
    val swapKeyToPosition = Locations.map.map(_.swap)

    val mappedLocation = swapKeyToPosition.get(position).orElse(location)

    Measurement(number, position, localTime, mappedLocation, condition, temperature, pressure, humidity)
  }

  override def toString = {
    "%d|%s|%s|%s|%s|%+.2f|%.2f|%.2f".format(number, location.get, position.toString, localTime.timeStamp,
      condition.get, temperature.get.value, pressure.get.value, humidity.get.value)
  }
}
