package ejemplosSim.environment

import simulator.mason.MasonAgentConsole
import simulator.behaviour.BEFSM
import simulator.interactuators.IResource
import simulator.Robot
import sim.field.grid.DoubleGrid2D
import sim.portrayal.grid.FastValueGridPortrayal2D

trait AgentConsole extends MasonAgentConsole{
  
  def getSensorReading() = {

    
    robot() match  {
     case r:Robot with IResource => {
       r.getBoxAt(r.pos.x.toInt, r.pos.y.toInt, 30, 0)
     }
     case _ => {println("BEAvoidMapObstacles: No se pudo utilizar ITrace ya que la simulación no utiliza el entorno EnvResource")}
   }
  }


} 