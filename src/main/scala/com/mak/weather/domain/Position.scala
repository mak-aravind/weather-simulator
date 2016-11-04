package com.mak.weather.domain

case class Position(latitude: Double, longitude: Double, elevation: Double) {
  require(latitude >= -90.0 && latitude <= 90.0,
    "Range of latitude is [-90, 90] in degrees")
  require(longitude >= -180.0 && latitude <= 180.0,
    "Range of longitude is [-180, 180] in degrees")
  require(elevation >= 0.0 && elevation <= 8872.0,
    "Range of elevation is [0, summit of Mt. Everest] in meters")

  override def toString = "%.2f,%.2f,%.2f".format(latitude, longitude, elevation)
}