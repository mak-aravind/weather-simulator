package com.mak.weather.model

import probability_monad.Distribution


object TrainingDataStatsModel{
  
  import probability_monad.Distribution.discrete  
  private def transitionMatrix(condition: Int): Distribution[Int] = condition match {
    case 0 => discrete(0 -> 1 / 3, 1 -> 1 / 3, 2 -> 1 / 3)
    case 1 => discrete(0 -> 0.10, 1 -> 0.80, 2 -> 0.10)
    case 2 => discrete(0 -> 0.90, 1 -> 0.05, 2 -> 0.05)
  }
  
  def markovChain(num: Int): Distribution[List[Int]] = {
    import probability_monad.Distribution.discreteUniform
    val zeroOrOneOrTwo = discreteUniform(0 to 2) 
    val samplePicked = zeroOrOneOrTwo.sample(1).head
    
    import probability_monad.Distribution.always
    val distribution = always(List(samplePicked))
    
    distribution.markov(num - 1)(sequence => for {
      next <- transitionMatrix(sequence.last)
    } yield sequence :+ next)
  }
}