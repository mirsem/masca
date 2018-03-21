package simulator.behaviour

import simulator.Behaviour 
import simulator.Robot
import simulator.interactuators._

class BEWanderLog(val delta:Double) extends Behaviour {
  
  var robotWithTraceResource:Robot with ITrace with IResource = _
  
  override def init(r:Robot) = {
    super.init(r)
    
    robot match  {
     case t:ITrace with IResource => {robotWithTraceResource = t}
     case _ => {println("BERandomLog: No se pudo utilizar ITrace ya que la simulación no utiliza el entorno EnvTrace o EnvResource")}
   }
    
   randomVel
   
  }
  
  def step() = {

    val vel = robot.vel
    vel.setX(vel.x + 2*(sim.randDouble-.5)*delta)
    vel.setY(vel.y + 2*(sim.randDouble-.5)*delta)
    
    val tl = robotWithTraceResource.getTrace(robotWithTraceResource.pos.x.toInt,robotWithTraceResource.pos.y.toInt)
    val rl = robotWithTraceResource.getResourceAt(robotWithTraceResource.pos.x.toInt,robotWithTraceResource.pos.y.toInt)
    
    //println("(resource,trace)=("+rl+","+tl+")")
   
  }

}