package simulator.mason

import simulator.Robot


/**
 * Factoria encargada de generar objetos MasonAgent. Estos objetos encapsulan la funcionalidad de los agentes dentro del simulador MASON
 * Se requiere derivar esta clase cuando la construcción de MasonAgent necesite utilizar un trait para ampliar su funcionalidad (ITrace,IResource...)
 * @author fidel
 *
 */
class MasonAgentFactory {
  def newInstance(robot:Robot): MasonAgent = new MasonAgent(robot)
}