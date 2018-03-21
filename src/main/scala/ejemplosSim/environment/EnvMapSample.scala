package ejemplosSim.environment

import sim.engine.SimState
import simulator.Robot
import simulator.Simulation
import simulator.Swarm
import simulator.behaviour.BEWanderLog
import simulator.environment.EnvMap
import simulator.environment.EnvTrace
import simulator.interactuators.IResource
import simulator.interactuators.ITrace
import simulator.mason.MasonSwarm
import simulator.mason.MasonSwarmFactory
import simulator.mason.MasonAgentFactory
import simulator.mason.MasonAgent


/**
 * Este ejemplo demuentra como ampliar los sensores del robot con uno que permite leer la intensidad de la huella dejada en el suelo por él o otro compañero
 * Para lograrlo se deben realizar dos cosas: por una parte agregar al antorno el trait EnvTrace, que es el que proporciona la habilidad de guardar la marca
 * dejada por los robots al desplazarse. Por otra parte el robot debe implementar el trait ITrace, que es el que le proporcionará los métodos necesarios para
 * obtener que valor de traza hay justo debajo de él.
 * 
 * Concretamente es el comportamiento BERandomWithTrace el que hace uso de dicho método para mostrar por consola que lectura tiene su robot del mapa de huellas
 */

object EnvMapSample {
 
  def main(args: Array[String]): Unit = {
    
    //Creo las consolas personalizadas para el enjambre y los robots a partir de
    //sus factorias
    
    val masonSwarmFactory = new MasonSwarmFactory() {
      override def newInstance(seed:Long, args:Array[String]):SimState = 
        new MasonSwarm(seed,sim) 
      	  with EnvTrace 
      	  with EnvMap {resourceURL="./src/main/scala/ejemplosSim/environment/laberinto.png" };
    }
    
    
    val mySwarm = new Swarm();
    
    val masonAgentFactory = new MasonAgentFactory() { 
      override def newInstance(robot:Robot):MasonAgent =
        new MasonAgent(robot) with AgentConsole;
    }
    
    
    //Y uso las factorias para crear los robots
    for (i <- 1 to 1) { 
      mySwarm += new Robot(new BEAvoidMapObstacle,masonAgentFactory) with ITrace with IResource
    }

    //Y la simulación
    val mySim = new Simulation(mySwarm,300,300,masonSwarmFactory)
    
    
    //mySim.run()
    mySim.runWithUI()
    

    
  }

}