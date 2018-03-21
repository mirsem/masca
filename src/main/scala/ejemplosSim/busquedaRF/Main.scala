package ejemplosSim.busquedaRF

import sim.engine.SimState
import simulator.Robot
import simulator.Simulation
import simulator.Swarm
import simulator.behaviour.BEFSM
import simulator.behaviour.BERandom
import simulator.environment.EnvResource
import simulator.environment.EnvTrace
import simulator.interactuators.IResource
import simulator.interactuators.ITrace
import simulator.mason.MasonSwarm
import simulator.mason.MasonSwarmFactory
import simulator.mason.MasonAgentFactory
import simulator.mason.MasonAgent
import sim.util.Double2D



object MainMason {
 
  def main(args: Array[String]): Unit = {
    
    //Creo las consolas personalizadas para el enjambre y los robots a partir de
    //sus factorias
    
    val masonSwarmFactory = new MasonSwarmFactory() {
      override def newInstance(seed:Long, args:Array[String]):SimState = 
        new MasonSwarm(seed,sim) 
      	  with EnvTrace
      	  with EnvResource {resourceURL="./src/main/scala/ejemplosSim/busquedaRF/resource.png" };
    }
    
    val masonAgentFactory = new MasonAgentFactory() { 
      override def newInstance(robot:Robot):MasonAgent =
        new MasonAgent(robot) with AgentConsole;
    }

    
    val mySwarm = new Swarm();
    
    
    //Y uso las factorias para crear los robots
    val numRobots = 40
    for (i <- 0 until numRobots) { 
      //Creo los robots y pongo las parejas correlativas
      mySwarm += new RobotLover(new BEResourceFinder,masonAgentFactory) with IResource
    }
    
    //Ya a cada robot lo enlazo con otros dos
    for (i <- 0 until numRobots) { 
      val current = mySwarm.robots(i).asInstanceOf[RobotLover]
      current.robotA = if(i+1<numRobots) mySwarm.robots(i+1) else mySwarm.robots(0)
      current.robotB = if(i>0) mySwarm.robots(i-1) else mySwarm.robots(numRobots-1)
    }
    
    


    //Y la simulación
    val mySim = new Simulation(mySwarm,1000,662,masonSwarmFactory)
    
    
    //mySim.run()
    mySim.runWithUI()
    
  }
}