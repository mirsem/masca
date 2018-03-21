package simulator.mason

import sim.engine.MakesSimState
import sim.engine.SimState
import simulator.Simulation


/**
 * Factoria encargada de generar objetos MasonSwarm. Estos objetos encapsulan la funcionalidad del enjambre dentro del simulador MASON
 * Se requiere derivar esta clase cuando la construcción de MasonSwarm necesite utilizar un trait para ampliar su funcionalidad (EnvResource, EnvTrace...)
 * @author fidel
 *
 */
class MasonSwarmFactory extends MakesSimState {
  var sim: Simulation = null
  
  override def newInstance(seed:Long, args:Array[String]):SimState = new MasonSwarm(seed,sim);
  override def simulationClass() = classOf[MasonSwarm]
}

