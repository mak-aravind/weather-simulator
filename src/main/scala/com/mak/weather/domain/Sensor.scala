package com.mak.weather.domain

import com.mak.weather.domain.Condition.condition
import probability_monad.Distribution.{normal, uniform}

object Sensor extends Enumeration {
  type Sensor = Value
  val Temperature, Humidity, Pressure = Value

  private def generateTemperature(condition: condition) = condition match {
    case Condition.Snow  => (-10.0 + 5.0) * normal.sample(1).head
    case Condition.Rain  => (20.5 + 5.0) * normal.sample(1).head
    case Condition.Sunny => (3.0 + 5.0) * normal.sample(1).head
  }

  /**
   * Weather model for HUMIDITY - Uniform distributed
   *
   * @param condition The weather condition that the humidity is conditioned on
   * @return A humidity value
   */
  private def generateHumidity(condition: condition) = condition match {
    case Condition.Snow  => (70.0 - 40.0) * uniform.sample(1).head + 40.0
    case Condition.Rain  => (95.0 - 70.0) * uniform.sample(1).head + 70.0
    case Condition.Sunny => (70.0 - 30.0) * uniform.sample(1).head + 30.0
  }

  /**
   * Weather model for PRESSURE - Gaussian distributed
   *
   * @param condition The weather condition that the pressure is conditioned on
   * @return A pressure value
   */
  private def generatePressure(condition: condition) = condition match {
    case Condition.Snow  => 800.0 + 2.0 * normal.sample(1).head
    case Condition.Rain  => 900.0 + 1.5 * normal.sample(1).head
    case Condition.Sunny => 700.0 + 0.2 * normal.sample(1).head
  }

  /**
   * Generate a sensor sample based on the sensor type
   *
   * @param sensor    Sensor type
   * @param condition Weather condition
   * @return A sensor measurement
   */
  def generateSensorSample(sensor: Sensor, condition: condition) = sensor match {
    case Sensor.Temperature => generateTemperature(condition)
    case Sensor.Humidity    => generateHumidity(condition)
    case Sensor.Pressure    => generatePressure(condition)
  }

}