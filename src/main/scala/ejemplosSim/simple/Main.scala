package ejemplosSim.simple

import simulator.Swarm
import simulator.Robot
import simulator.Simulation
import simulator.behaviour.BERandom
//import scala.reflect.BeanProperty
import sim.engine.MakesSimState 
import sim.engine.SimState
import simulator.mason.MasonSwarm


/**
 * Ejemplo donde se crea un enjambre de 100 agentes ejecutando una conducta de desplazamiento aleatoria
 * @author fidel
 *
 */
object MainSimple {
 
  def main(args: Array[String]): Unit = {
    
    val mySwarm = new Swarm();
    
    for (i <- 1 to 100) { 
      mySwarm += new Robot(new BERandom(0.1)) // como no se a�ade factoria de agentes, se crea en el constructor de robot (new MasonAgentFactory)
    }
   
    val mySim = new Simulation(mySwarm)
    
     
    //mySim.run()
    mySim.runWithUI()

    
  }

}