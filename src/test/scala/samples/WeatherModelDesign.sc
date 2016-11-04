package samples

import probability_monad.Distribution._
import probability_monad.Distribution

object WeatherModelDesign {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  discreteUniform(1 to 100).sample(1)             //> res0: List[Int] = List(27)
  
  discreteUniform(1 to 30).sample(10)             //> res1: List[Int] = List(4, 24, 11, 7, 16, 22, 21, 12, 27, 20)
  
	discreteUniform(0 to 2).sample(1).head    //> res2: Int = 2
	
	def transition(condition: Int): Distribution[Int] = condition match {
    case 0 => discrete(0 -> 1 / 3, 1 -> 1 / 3, 2 -> 1 / 3)
    case 1 => discrete(0 -> 0.10,  1 -> 0.80,  2 -> 0.10)
    case 2 => discrete(0 -> 0.99,  1 -> 0.01,  2 -> 0.00)
  }                                               //> transition: (condition: Int)probability_monad.Distribution[Int]

  def chain(num: Int): Distribution[List[Int]] = {
    val first = 0 // can be random
    always(List(first)).markov(num - 1)(sequence => for {
      next <- transition(sequence.last)
    } yield sequence :+ next)
  }                                               //> chain: (num: Int)probability_monad.Distribution[List[Int]]
  
   val weatherCondition = chain(30).sample(2)     //> weatherCondition  : List[List[Int]] = List(List(0, 0, 1, 1, 2, 0, 2, 0, 0, 1
                                                  //| , 2, 0, 1, 1, 0, 1, 1, 1, 1, 2, 0, 0, 1, 2, 0, 1, 1, 1, 1, 2), List(0, 1, 1,
                                                  //|  0, 2, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 0, 2, 0, 1, 1, 2, 
                                                  //| 0, 0))
   weatherCondition.head.size                     //> res3: Int = 30
}