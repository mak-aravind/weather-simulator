package com.mak.weather.station

case class Temperature(value: Double = Double.NaN, unit: String = "celsius") {}

case class Pressure(value: Double = Double.NaN, unit: String = "hPa") {}

case class Humidity(value: Double = Double.NaN, unit: String = "percent") {}


