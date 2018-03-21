package ejemplosSim.masonGUI

import simulator.Swarm
import simulator.Robot
import simulator.Simulation
import simulator.behaviour.BERandom
//import scala.reflect.BeanProperty
import simulator.mason.MasonSwarmFactory
import sim.engine.MakesSimState
import sim.engine.SimState
import simulator.mason.MasonSwarm
import simulator.mason.MasonAgentFactory
import simulator.mason.MasonAgent
import simulator.environment.EnvTrace
import simulator.interactuators.ITrace
import simulator.behaviour.BEWanderLog


/**
 * Ejemplo básico donde los robots se mueven al azar. Además se muestra en la consola de los robots (haciendo doble click sobre alguno de ellos) su velocidad
 * máxima. En la consola general del enjambre se muestra el número de robots disponibles.
 * Para agregar dichos datos al UI se deben construir tanto los agentes de mason (masonAgent) como el enjambre (masonSwarm) creando dos nuevos traits
 * que deriven de masonSwarmConsole y masonAgentConsole. Además se debe modificar la factoria de enjambre y agentes (MasonSwarmFactory y MasonAgentFactory) para
 * que las instancias que generen implementes los traits anteriores.
 */
object MainMason {
 
  def main(args: Array[String]): Unit = {
    
    //Creo las consolas personalizadas para el enjambre y los robots a partir de
    //sus factorias
    
    val masonSwarmFactory = new MasonSwarmFactory() {
      override def newInstance(seed:Long, args:Array[String]):SimState = 
        new MasonSwarm(seed,sim) with SwarmConsole; //Se pueden añadir todos los traits que se quieran a continuación de este con "with <otroTrait> with ...."
    }
    
    val masonAgentFactory = new MasonAgentFactory() {
      override def newInstance(robot:Robot):MasonAgent =
        new MasonAgent(robot) with AgentConsole;
    }
    
    
    val mySwarm = new Swarm();
    
    
    //Y uso las factorias para crear los robots
    for (i <- 1 to 100) { 
      mySwarm += new Robot(new BERandom(0.1),masonAgentFactory) 
    }

    //Y la simulación
    val mySim = new Simulation(mySwarm,200,200,masonSwarmFactory)
    
    
    //mySim.run()
    mySim.runWithUI()
    

    
  }

}