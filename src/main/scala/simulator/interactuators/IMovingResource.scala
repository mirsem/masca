package simulator.interactuators

import simulator.Interactuator
import simulator.environment.EnvResource
import simulator.mason.MasonSwarm
import simulator.environment.EnvMovingResource


/**
 * Usado en conjunto con un EnvResource permite que un robot pueda leer la cantidad de recurso que exite en una posición
 * del mapa
 * @author fidel
 *
 */
trait IMovingResource extends Interactuator {
  
  var simWithEnvResource:MasonSwarm with EnvMovingResource = null 
  
  override def initInteractuator() = {
    super.initInteractuator();
    
    simWithEnvResource = masonAgent.masonSim match  {
     						case sim: EnvMovingResource => sim;
     						case _ => {println("IResource.getTrace: No se pudo utilizar ITrace ya que la simulación no utiliza el entorno EnvResource");sys.exit(0); null}
     				  }
  }
  
  def getResourceAt(x:Int, y:Int):Double = simWithEnvResource.getResourceAt(x, y)
  def getBoxAt(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Array[Array[Double]] = simWithEnvResource.getBoxAt(x, y, ofradius, defaultVal)

}