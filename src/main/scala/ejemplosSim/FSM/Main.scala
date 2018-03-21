package ejemplosSim.FSM

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



/**
 * Este ejemplo muestra el uso de BEFSM, un comportamiento basado en un autómata, donde se especifican tanto las acciones cuando el robot
 * se encuentre en un nodo como las transiciones. Se puede utilizar directamente o derivando de la clase BEFSM. En este caso concreto el comportamiento será 
 * alternar enter dos estados (deambular y parar) cada 50 iteraciones
 */
object MainMason {
 
  def main(args: Array[String]): Unit = {
    
    //Creo las consolas personalizadas para el enjambre y los robots a partir de
    //sus factorias
    
    val masonSwarmFactory = new MasonSwarmFactory() {
      override def newInstance(seed:Long, args:Array[String]):SimState = 
        new MasonSwarm(seed,sim) 
      	  with EnvTrace 
      	  with EnvResource {resourceURL="./src/main/scala/ejemplosSim/environment/resource.png" };
    }
    
    val masonAgentFactory = new MasonAgentFactory() { 
      override def newInstance(robot:Robot):MasonAgent =
        new MasonAgent(robot) with AgentConsole;
    }

    
    val mySwarm = new Swarm();
    
    
    //Y uso las factorias para crear los robots
    for (i <- 1 to 100) { 
      mySwarm += new Robot(createFSMBehaviour(),masonAgentFactory) with ITrace with IResource
    }

    //Y la simulación
    val mySim = new Simulation(mySwarm,200,200,masonSwarmFactory)
    
    
    //mySim.run()
    mySim.runWithUI()
    
  }
  
  def createFSMBehaviour() = {
    
    val fsm = new BEFSM()
    
    fsm addState("Wander", withAction=onWander)
    fsm addState("Stop", withAction=()=>{fsm.robot.vel.mul(0.01)})			  
    fsm setInitialState("Wander")
    
    fsm addTransition( state1 = "Wander",
        			   state2 = "Stop",
        			   withCondition = () => {
        			     val steps = 1+fsm.sim.stepsExecuted
        			     if(steps%50==0) true else false
        			   })
      
    fsm addTransition(state1 ="Stop",state2 ="Wander",withCondition=gotoFromStopToWanderIf)
    
    def onWander() = {
      fsm.robot.vel = BERandom.mutateVel(fsm.robot.vel,0.1d,fsm.sim)
    }
  
    def gotoFromStopToWanderIf() = {
      val steps = 1+fsm.sim.stepsExecuted
      if(steps%50==0) true else false
    }
    
    fsm
  }
  


}