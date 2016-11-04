package samples

import probability_monad.Distribution._
import probability_monad.Distribution

object WeatherModelDesign {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(167); 
  println("Welcome to the Scala worksheet");$skip(39); val res$0 = 

  discreteUniform(1 to 100).sample(1);System.out.println("""res0: List[Int] = """ + $show(res$0));$skip(41); val res$1 = 
  
  discreteUniform(1 to 30).sample(10);System.out.println("""res1: List[Int] = """ + $show(res$1));$skip(43); val res$2 = 
  
	discreteUniform(0 to 2).sample(1).head;System.out.println("""res2: Int = """ + $show(res$2));$skip(252); 
	
	def transition(condition: Int): Distribution[Int] = condition match {
    case 0 => discrete(0 -> 1 / 3, 1 -> 1 / 3, 2 -> 1 / 3)
    case 1 => discrete(0 -> 0.10,  1 -> 0.80,  2 -> 0.10)
    case 2 => discrete(0 -> 0.99,  1 -> 0.01,  2 -> 0.00)
  };System.out.println("""transition: (condition: Int)probability_monad.Distribution[Int]""");$skip(219); 

  def chain(num: Int): Distribution[List[Int]] = {
    val first = 0 // can be random
    always(List(first)).markov(num - 1)(sequence => for {
      next <- transition(sequence.last)
    } yield sequence :+ next)
  };System.out.println("""chain: (num: Int)probability_monad.Distribution[List[Int]]""");$skip(49); 
  
   val weatherCondition = chain(30).sample(2);System.out.println("""weatherCondition  : List[List[Int]] = """ + $show(weatherCondition ));$skip(30); val res$3 = 
   weatherCondition.head.size;System.out.println("""res3: Int = """ + $show(res$3))}
}
