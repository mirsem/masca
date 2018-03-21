package ejemplosSim.masonGUI

import simulator.mason.MasonAgentConsole

trait AgentConsole extends MasonAgentConsole{
  
  def getMaxVel() = robot().maxVel
  def setMaxVel(vel:Double) = robot().maxVel = vel

}