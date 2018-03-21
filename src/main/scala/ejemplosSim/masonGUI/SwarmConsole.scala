package ejemplosSim.masonGUI

import simulator.mason.MasonSwarmConsole 

trait SwarmConsole extends MasonSwarmConsole{
  
    //Todos los métodos getX y setX provocaran que se visualice la propiedad en el GUI de Mason (parte del enjambre)
	def getNumRobots():Int = sim.swarm.robots.length
}