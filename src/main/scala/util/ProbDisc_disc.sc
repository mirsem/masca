package util

object ProbDisc_disc {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  //Ejemplo de suma acumulativa de la dist
  List(1, 2, 3).scanLeft(0)(_+_).drop(1)          //> res0: List[Int] = List(1, 3, 6)
  
  val _prob = new ProbDist_disc(5)                //> _prob  : util.ProbDist_disc = util.ProbDist_disc@47f6473
  _prob.setEvent(0,0)                             //> res1: Array[Double] = Array(0.0, 0.25, 0.25, 0.25, 0.25)
  _prob.setEvent(1,0)                             //> res2: Array[Double] = Array(0.0, 0.0, 0.3333333333333333, 0.3333333333333333
                                                  //| , 0.3333333333333333)
  _prob.setEvent(2,0)                             //> res3: Array[Double] = Array(0.0, 0.0, 0.0, 0.5, 0.5)

   
  _prob.cumSum()                                  //> res4: Array[Double] = Array(0.0, 0.0, 0.0, 0.5, 1.0)
    
  _prob.draw()                                    //> res5: Double = 4.0
  
  _prob.convolve(Array(.2,.7,.1))                 //> res6: Array[Double] = Array(0.05, 0.0, 0.1, 0.44999999999999996, 0.399999999
                                                  //| 99999997)
  _prob.cumSum()                                  //> res7: Array[Double] = Array(0.05, 0.05, 0.15000000000000002, 0.6, 1.0)
  
}