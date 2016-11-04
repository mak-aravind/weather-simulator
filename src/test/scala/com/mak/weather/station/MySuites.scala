package com.mak.weather.station

import org.scalatest.Suites

class MySuites extends Suites (
  new PositionTest,
  new MeasurementTest,
  new LocalTimeTest,
  new SimulatorTest
)