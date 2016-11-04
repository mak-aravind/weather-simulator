package com.mak.weather.station

class LocalTimeTest extends UnitTest("LocalTime") {

  it should "allow construction if type is valid" in {
    LocalTime("2016-08-22T12:00:00")
  }

}
