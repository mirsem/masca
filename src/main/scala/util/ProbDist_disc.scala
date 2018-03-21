package util

import scala.util.control.Breaks._

//Distribusción de prob. discreta
class ProbDist_disc(val dom_size:Int) {
  
  //Distribución de prob.
  var dist = new Array[Double](dom_size) 
  
  //Inicialmente todas las posiciones tienen la misma prob.
  dist = dist.map { x => 1d/dom_size }
  
  def uniform() = dist = dist.map { x => 1d/dom_size }
  def zero() =  dist = dist.map { x => 0d }
  
  
  //Añade prob. de un evento a la distribución
  def addEvent(event:Int, prob:Double) = {
    require(event>=0 && event<dom_size,println("ProbDist_disc.addProb espera valor dentro del dominio: " + event))
    dist(event) += prob;
    
    normalize()
  }
  
  //Añade prob. de un evento a la distribución
  def setEvent(event:Int, prob:Double) = {
    require(event>=0 && event<dom_size,println("ProbDist_disc.addProb espera valor dentro del dominio: " + event))
    dist(event) = prob;
    
    normalize()
  }
  
  //Normaliza la distribución
  def normalize() = {
      val sum:Double = dist.sum
      dist = dist.map { x => x/sum }
      dist
  }
  
  //Devuelve la suma acumulada de la dist
  def cumSum():Array[Double] = {dist.scanLeft(0d)(_+_).drop(1)}
  
  //Extrae un evento siguiendo la distribución
  def draw():Double = {
    val rand = scala.util.Random.nextDouble()
    val csum = cumSum()
    var event=0d
    
    var i=0
    while(i<dom_size){
      if(rand<=csum(i)) return i
      i+=1
    }
    
    event
  }
  
  //Convolución de la distribución con un kernel
  def convolve(kernel:Array[Double]) = {
    
    val distCopy = dist.clone()
    zero()
    
    val width = ((kernel.length - 1) / 2).toInt
    val N = dist.length
    
     for (i <- 0 until N){
       for (k <- 0 until kernel.length){
          var index = (i + (width-k)) % N
          if(index<0) index += dist.length
          dist(i) += distCopy(index) * kernel(k)
       }
       
     }
    dist
  }
  
  
}