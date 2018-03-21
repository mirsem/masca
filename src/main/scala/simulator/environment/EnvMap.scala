package simulator.environment

import simulator.Simulation
import simulator.Robot
import sim.portrayal.grid.FastValueGridPortrayal2D


/** Entorno de recursos donde cualquier pixel negro significa que es un obstáculo que no se puede atravesar */
trait EnvMap extends EnvResource {
  
  override def isCollision(robot:Robot):Boolean={
    
    val other = super.isCollision(robot)
    var col = false
    
    val resource = getResourceAt((robot.pos.x+ robot.vel.x).toInt,(robot.pos.y + robot.vel.y).toInt)
    if(resource == -1) {
      println("Warning: Se está en una posición sin mapa, asumiendo entorno libre")
    }else if(resource==1) {
      col = true
    }
    
   (other||col)
  }

}