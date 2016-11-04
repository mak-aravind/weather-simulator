package com.mak.weather.prediction

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.RandomForest
import org.apache.spark.mllib.tree.model.RandomForestModel
import org.apache.spark.mllib.util.MLUtils

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import probability_monad.Distribution.discreteUniform

import com.mak.weather.station.{Temperature,Pressure,Humidity,LocalTime}

import com.mak.weather.domain.{Condition, Position}

/**
  *
  * A weather simulator to generate weather conditions and sensor measurements (Temperature/Pressure/Humidity).
  * Predictive models are established based on training data sets. Simulated weather data can then be produced
  * with a new time&position pair.
  *
  * The pick of the Machine Learning algorithm is Spark MLib's Random Forest, which accepts the training data sets in LIBSVM format
  * 
  */

sealed trait TrainingData {
  val ROOT = "src/main/resources/trainingData/"
}

sealed trait SparkBase {
  private val master = "local[*]"
  private val appName = this.getClass.getSimpleName

  val conf = new SparkConf().setMaster(master).setAppName(appName)
}

sealed trait Model {
  def build(training: RDD[LabeledPoint]): RandomForestModel
}

object Model {

  private class Condition extends Model {

 
    override def build(training: RDD[LabeledPoint]): RandomForestModel = {

      val numClasses = Condition.maxId
      val categoricalFeaturesInfo = Map[Int, Int]()
      val numOfTrees = 10 
      val featureSubsetStrategy = "auto"
      val impurity = "gini"
      val maxDepth = 4
      val maxBins = 32
      val model = RandomForest.trainClassifier(training, numClasses, categoricalFeaturesInfo,
        numOfTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)

      model
    }
  }

  private class Sensor extends Model {

     override def build(training: RDD[LabeledPoint]): RandomForestModel = {

      val categoricalFeaturesInfo = Map[Int, Int]()
      val numOfTrees = 5 
      val featureSubsetStrategy = "auto"
      val impurity = "variance"
      val maxDepth = 4
      val maxBins = 32
      val model = RandomForest.trainRegressor(training, categoricalFeaturesInfo,
        numOfTrees, featureSubsetStrategy, impurity, maxDepth, maxBins)

      model
    }

  }

  def apply(modelName: String): Model = {
    if (modelName == "Condition") new Condition
    else new Sensor
  }

}

object Predictor extends SparkBase with TrainingData {
  val models = buildModels()

  def loadTrainingData(sc: SparkContext, fileName: String): RDD[LabeledPoint] = {
    val path = ROOT + fileName + ".txt"
    val data = MLUtils.loadLibSVMFile(sc, path)
    val splits = data.randomSplit(Array(0.7, 0.3), seed = 123L)
    val (trainingData, testData) = (splits(0), splits(1))
    trainingData
  }

  /**
    * Build models for weather (Condition, Temperature/Pressure/Humidity)
    *
    * @return Predictive models
    */
  def buildModels(): Map[String, RandomForestModel] = {
    val sc = new SparkContext(conf)
    val models = List(Temperature, Pressure, Humidity, Condition)
      .map(v => v.toString).map(m => m -> Model.apply(m).build(loadTrainingData(sc, m))).toMap
    sc.stop()
    models
  }

  /**
    * Generate a sample using established predictive models
    *
    * @param localTime ISO8601 date time
    * @param position  A triplet containing latitude, longitude and elevation
    * @return A generated weather sample containing CONDITION, TEMPERATURE, PRESSURE and HUMIDITY
    */
  def predict(localTime: LocalTime, position: Position): Map[String, Double] = {
    val noise = discreteUniform(-1 to 1).sample(1).head // inject randomness to simulate sensor noise
    val dayOfWeek = localTime.localDateTime.getDayOfWeek.getValue + noise 
    val newPoint = Vectors.dense(position.latitude, position.longitude, position.elevation, dayOfWeek)

    val predictions = models.map { case (k, v) => (k, v.predict(newPoint)) }
    predictions
  }

}
