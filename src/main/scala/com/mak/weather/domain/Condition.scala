package com.mak.weather.domain

object Condition extends Enumeration {
  type condition = Value
  val Rain, Snow, Sunny, Unknown = Value
}