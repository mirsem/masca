package ejemplosSim.environment

import simulator.Behaviour
import simulator.interactuators.IResource
import simulator.Robot
import util.Vector2D
import simulator.behaviour.BERandom

class BEAvoidMapObstacle extends Behaviour {
  
  var robotI: Robot with IResource = _
  var rangoSensor = 0
  
    
  /**
   * Obtengo el robot con capacidades de lectura de recursos
   */
  override def init(r:Robot) = {
    super.init(r)
    
    robot match  {
     case t:Robot with IResource => {robotI = t}
     case _ => {println("BEAvoidMapObstacles: No se pudo utilizar ITrace ya que la simulación no utiliza el entorno EnvResource")}
   }
    
   robotI.sonarRange= 20
   rangoSensor = robotI.sonarRange.toInt
    
    
  }
  
  override def step(){
    //Leo el sensor de mapa
    val box:Array[Array[Double]] = robotI.getBoxAt(robotI.pos.x.toInt, robotI.pos.y.toInt, rangoSensor, 0)
    
    //Obtengo el vector que más se aleje de los obstáculos
    val vobstacles = robotI.getVectorToResourceDistNormalized(robotI.pos.x.toInt, robotI.pos.y.toInt, rangoSensor, 0)
    vobstacles.mul(-1);
    
    
    
    robotI.vel = BERandom.mutateVel(robotI.vel,0.2d,sim) 
    
    //TODO: para que no continue con la misma velocidad (y por ejemplo de vueltas en circulo) si ya tiene la orientación
    //indicada por el vector hay que ver el ángulo del robot y el ángulo del vector de repulsión, si son parecidos solo habría
    //que dejar velocidad traslacional y no rotacional. Pensar como hacerlo fácil
    
    robotI.vel.add(vobstacles)
   

    robotI.vel.normalize()

    
  }

}