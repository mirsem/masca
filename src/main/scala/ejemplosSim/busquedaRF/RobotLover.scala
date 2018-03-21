package ejemplosSim.busquedaRF

import simulator.Robot
import simulator.Behaviour
import simulator.mason.MasonAgentFactory

class RobotLover(behaviour:Behaviour, masonAgentFactory: MasonAgentFactory = new MasonAgentFactory) extends Robot(behaviour, masonAgentFactory){
  
  var robotA:Robot=_;
  var robotB:Robot=_;

}