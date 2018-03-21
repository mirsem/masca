package simulator

/**
 * Comportamiento de un robot. Este comportamiento debe tener un método init que lo inicializa y además lo vincula con un robot.
 * Además tiene que tener un método step que se llamará a cada paso de la simulación
 */
abstract class Behaviour {
  
  /** Robot al que se aplica el comportamiento */
  var robot:Robot = _ 
  /** Simulación en la que se encuentra el comportamiento */
  var sim: Simulation = _
  
  /** Inicializador del comportamiento. Si se deriva se debe llamar a super.init */
  def init(r:Robot) = {
    robot=r
    sim = robot.swarm.sim
  }
  
  /** Cada paso se ejecutara el contenido de la función step */
  def step();
  
  
  //Dispone un vector de velocidad aleatorio 
  def randomVel() {
    val vel = robot.vel
    vel.setX(sim.randDouble- 0.5)
    vel.setY(sim.randDouble- 0.5)
    vel.normalize()
    vel.mul(robot.maxVel)
  }

}