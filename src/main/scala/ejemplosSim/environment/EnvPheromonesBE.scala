package ejemplosSim.environment

import simulator.Behaviour
import simulator.Robot
import simulator.interactuators._

class EnvPheromonesBE(val delta:Double) extends Behaviour {
  
  var robotWithPheromones:Robot with IPheromones with IResource = _
  
  override def init(r:Robot) = {
    super.init(r)
    
    robot match  {
     case t:IPheromones with IResource => {robotWithPheromones = t}
     case _ => {println("EnvPheromonesBE: No se pudo utilizar IPheromones ya que la simulación no utiliza el entorno EnvPheromones o EnvResource")}
   }
    
   randomVel
   
  }
  
  def step() = {

    val vel = robot.vel
    vel.setX(vel.x + 2*(sim.randDouble-.5)*delta)
    vel.setY(vel.y + 2*(sim.randDouble-.5)*delta)
    
    robotWithPheromones.addPheromoneCircle(robotWithPheromones.pos.x.toInt,robotWithPheromones.pos.y.toInt,5, .03)
    
 
   
  }

}