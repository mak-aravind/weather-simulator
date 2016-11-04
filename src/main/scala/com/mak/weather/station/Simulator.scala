package com.mak.weather.station

import java.time.LocalDateTime
import java.time.format.{DateTimeFormatter, DateTimeParseException}

import com.mak.weather.domain.Locations
import com.mak.weather.domain.Position

import probability_monad.Distribution._

object Simulator {
  
  def runScenarios(){

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    val startDate = LocalDateTime.now()

    println("\n")

    // Scenario
    println("10 random locations forcast for the next few days...")
    val numOfLocations = Locations.map.size

    val sequence = discreteUniform(0 until Locations.map.size).sample(numOfLocations)
    val locations = sequence.map(i => Locations.map.toIndexedSeq.apply(i))

    locations.indices.foreach(i => {
      val timeStamp = startDate.plusDays(i)
      val formattedDateTime = timeStamp.format(formatter)
      val position = locations.apply(i)._2
      println(Measurement(i, position, LocalTime(formattedDateTime)).emit())
    })

  }
  
  
  def main(args: Array[String]): Unit = {
    val usage =
      """
      Usage:
      sbt "run-main com.mak.weather.station.Simulator [latitude longitude elevation time]"
      Example:
      sbt "run-main com.mak.weather.station.Simulator -33.86 151.21 39 2014-08-22T00:00:00"
      """

    if (args.length == 0) {
      runScenarios()
    } else if (args.length == 4) {
      try {
        val latitude = args(0).toDouble
        val longitude = args(1).toDouble
        val elevation = args(2).toDouble
        val localTime = args(3)
        println("\n")
        println(Measurement(0, Position(latitude, longitude, elevation), 
                LocalTime(localTime)).emit())
        println("\n")
      } catch {
        case ex: NumberFormatException => {
          println("Wrong number format for position")
          println(usage)
        }
        case ex: DateTimeParseException => {
          println("Wrong format for time stamp")
          println(usage)
        }
        case ex: IllegalArgumentException => {
          println(ex.getMessage)
          println(usage)
        }

      }
    } else {
      println("Wrong number of input arguments")
      println(usage)
    }
  }
  
}