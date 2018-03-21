package ejemplosSim.follower

import simulator.mason.MasonAgentConsole
import simulator.behaviour.BEFSM

trait AgentConsole extends MasonAgentConsole{
  
  def getAgentState() = {
    robot().behaviour match  {
      case b:BEFSM => b.runningState
      case _ => "Error, el robot no ejecuta un comportamiento BEFSM "
    }
  }


} 