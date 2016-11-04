package com.mak.weather.model

import java.time.LocalDateTime
import java.io.BufferedWriter
import java.io.FileWriter

import com.mak.weather.domain.{ Locations, Position, Condition, Sensor }

import probability_monad.Distribution.discreteUniform

object TrainingDataGenerator extends App {

  for (location <- Locations.map.values) {

    val numOfSamples = discreteUniform(1 to 10).sample(1).head

    val weatherCondition = TrainingDataStatsModel.markovChain(numOfSamples)
      .sample(1)
      .flatten.map(s => Condition(s))

    val startDate = LocalDateTime.of(2015, 1, 1, 0, 0, 0)
    val timeStamps = weatherCondition.indices.map(i => startDate.plusDays(i))
    val timeStampedWeatherConditions = weatherCondition.zip(timeStamps)

    timeStampedWeatherConditions.foreach(weatherCondition =>
      generateSample(weatherCondition, location))
  }

  def generateSample(weatherCondition: (Condition.condition, LocalDateTime),
                     position: Position): Unit = {
    Sensor.values
      .map(s => s -> Sensor.generateSensorSample(s, weatherCondition._1))
      .foreach {
        case (k, v) =>
          writeTrainingData(fileName = k.toString, sample = formatLIBSVM(v, position, weatherCondition._2.getDayOfWeek.getValue))
      }

    val fileName = "Condition"
    writeTrainingData(fileName, formatLIBSVM(weatherCondition._1.id, position, weatherCondition._2.getDayOfWeek.getValue))
  }

  def formatLIBSVM(weather: Double, position: Position, time: Int): String = {
    "%.2f".format(weather) + " " +
      "1:" + "%.2f".format(position.latitude) + " " +
      "2:" + "%.2f".format(position.longitude) + " " +
      "3:" + "%.2f".format(position.elevation) + " " +
      "4:" + "%d".format(time) +
      "\n"
  }

  def writeTrainingData(fileName: String, sample: String): Unit = {
    val ROOT = "src/main/resources/trainingData/"
    val writer = new BufferedWriter(new FileWriter(ROOT + fileName + ".txt", true))
    writer.write(sample)
    writer.close()
  }

}