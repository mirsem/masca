package ejemplosSim.follower

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
      	  with EnvResource {resourceURL="./src/main/scala/ejemplosSim/follower/resource.png" };
    }
    
    val masonAgentFactory = new MasonAgentFactory() { 
      override def newInstance(robot:Robot):MasonAgent =
        new MasonAgent(robot) with AgentConsole;
    }

    
    val mySwarm = new Swarm();
    
    
    //Y uso las factorias para crear los robots
    for (i <- 1 to 40) { 
      mySwarm += new Robot(new BEFollower,masonAgentFactory) with IResource
    }


    //Y la simulación
    val mySim = new Simulation(mySwarm,200,200,masonSwarmFactory)
    
    
    //mySim.run()
    mySim.runWithUI()
    
  }
}