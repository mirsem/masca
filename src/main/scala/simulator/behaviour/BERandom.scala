package simulator.behaviour

import simulator.Behaviour
import simulator.Robot
import util.Vector2D
import simulator.Simulation

/**
 * Comportamiento de desplazamiento aleatorio, donde delta es la mutación respecto a la velocidad actual
 * @author fidel
 */
class BERandom(val delta:Double) extends Behaviour {
  
  override def init(r:Robot) = {
    super.init(r)
    randomVel
    robot.vel.mul(robot.maxVel)
  }
  
  def step() = {robot.vel = BERandom.mutateVel(robot.vel,delta,sim)}

}

object BERandom {
  def mutateVel(vel:Vector2D,delta:Double,sim:Simulation) = {
    val v = new Vector2D(vel.x + 2*(sim.randDouble-.5)*delta,vel.y + 2*(sim.randDouble-.5)*delta)
    v.normalize()
    v
  }
}