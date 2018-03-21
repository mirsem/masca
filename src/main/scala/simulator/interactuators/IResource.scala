package simulator.interactuators

import simulator.Interactuator
import simulator.environment.EnvResource
import simulator.mason.MasonSwarm
import simulator.environment.EnvMovingResource
import util.Vector2D


/**
 * Usado en conjunto con un EnvResource permite que un robot pueda leer la cantidad de recurso que exite en una posición
 * del mapa
 * @author fidel
 *
 */
trait IResource extends Interactuator {
  
  var simWithEnvResource:MasonSwarm with EnvResource = null 
  
  override def initInteractuator() = {
    super.initInteractuator();
    
    simWithEnvResource = masonAgent.masonSim match  {
     						case sim: EnvResource => sim;
     						case _ => {println("IResource.getTrace: No se pudo utilizar ITrace ya que la simulación no utiliza el entorno EnvResource");sys.exit(0); null}
     				  }
  }
  
  def getResourceAt(x:Int, y:Int):Double = simWithEnvResource.getResourceAt(x, y)
  def getBoxAt(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Array[Array[Double]] = simWithEnvResource.getBoxAt(x, y, ofradius, defaultVal)
  
  /*Devuelve un vecto que apunta a la mayor cantidad de recurso, donde los recursos afectan al vector con la distancia
   * al cuadrado
   */
  def getVectorToResourceDistNormalized(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Vector2D = {
    
    var vcompass = new Vector2D(0,0);
    val box = simWithEnvResource.getBoxAt(x, y, ofradius, defaultVal)
    val rangoSensor = ofradius
    
    
    for(i <- 0 to ofradius*2){
      for(j <- 0 to ofradius*2){
        
        val vij = new Vector2D(j-ofradius,i-ofradius)
        
        val d = if(vij.mod()==0) 1 else 1.0/(vij.mod()*vij.mod())
        val intensity = (box(i)(j))*d //Intensidad del recurso por su distancia al cuadrado
        vij.mul(intensity)
        vcompass.add(vij)
      }
    }
    
    val vcompassp = new Vector2D(vcompass.x,vcompass.y)
    vcompassp
  }
  
  
    /*Devuelve un vecto que apunta a la mayor cantidad de recurso, donde los recursos afectan al vector con la distancia
   * al cubo
   */
  def getVectorToResourceDist3Normalized(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Vector2D = {
    
    var vcompass = new Vector2D(0,0);
    val box = simWithEnvResource.getBoxAt(x, y, ofradius, defaultVal)
    val rangoSensor = ofradius
    
    
    for(i <- 0 to ofradius*2){
      for(j <- 0 to ofradius*2){
        
        val vij = new Vector2D(j-ofradius,i-ofradius)
        
        val d = if(vij.mod()==0) 1 else 1.0/(vij.mod()*vij.mod()*vij.mod())
        val intensity = (box(i)(j))*d //Intensidad del recurso por su distancia al cuadrado
        vij.mul(intensity)
        vcompass.add(vij)
      }
    }
    
    val vcompassp = new Vector2D(vcompass.x,vcompass.y)
    vcompassp
  }
  

}