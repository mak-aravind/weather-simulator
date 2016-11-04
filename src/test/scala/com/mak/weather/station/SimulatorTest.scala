package com.mak.weather.station

import org.apache.spark.mllib.tree.configuration.Algo
import org.apache.spark.mllib.tree.model.RandomForestModel

import com.mak.weather.domain.{Position}
import com.mak.weather.prediction.Predictor

class SimulatorTest extends UnitTest("Predictor") {

  it should "generate a sample with a new time&location pair" in {
    val sample = Predictor.predict(LocalTime("2016-08-22T12:00:00"), Position(-33.86, 151.21, 39.00))
    sample.size shouldEqual 4

    sample("Condition").isInstanceOf[Double] shouldEqual true
    sample("Temperature").isInstanceOf[Double] shouldEqual true
    sample("Pressure").isInstanceOf[Double] shouldEqual true
    sample("Humidity").isInstanceOf[Double] shouldEqual true
  }

  it should "build and return one classification model, three regression models" in {
    val models = Predictor.buildModels()
    models.size shouldEqual 4

    models.values.foreach(model => model.isInstanceOf[RandomForestModel] shouldEqual true)

    models("Condition").algo.compare(Algo.Classification) shouldEqual 0
    models("Temperature").algo.compare(Algo.Regression) shouldEqual 0
    models("Pressure").algo.compare(Algo.Regression) shouldEqual 0
    models("Humidity").algo.compare(Algo.Regression) shouldEqual 0
  }

}
