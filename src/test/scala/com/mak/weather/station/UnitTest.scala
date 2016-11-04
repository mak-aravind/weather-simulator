package com.mak.weather.station

import org.scalatest.{FlatSpec, Matchers}

abstract class UnitTest(component: String) extends FlatSpec with Matchers {

  behavior of component

}