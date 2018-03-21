package simulator.interactuators

import simulator.Interactuator
import simulator.mason.MasonSwarm
import simulator.environment.EnvPheromones
import util.Shape2Array
import util.Vector2D


/**
 * Usado junto con EnvTrace permite que un robot obtenga lecturas de las huellas que dejan los robots al desplazarse
 * @author fidel
 *
 */
trait IPheromones extends Interactuator {
  
  var simWithEnvPheromones:MasonSwarm with EnvPheromones = null 
  
  override def initInteractuator() = {
    super.initInteractuator();
    
    simWithEnvPheromones = masonAgent.masonSim match  {
     						case sim: EnvPheromones => sim
     						case _ => {println("IPheromones.getPheromones: No se pudo utilizar IPheromones ya que la simulación no utiliza el entorno EnvPheromones");sys.exit(0); null}
     				  }
  }
  
  def getPheromones(x:Int, y:Int):Double = simWithEnvPheromones.getPheromones(x, y)
  
  
  def getPheromonesBoxAt(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Array[Array[Double]] = simWithEnvPheromones.getPheromonesBoxAt(x, y, ofradius, defaultVal)
  
  /*Devuelve un vecto que apunta a la mayor cantidad de recurso, donde los recursos afectan al vector con la distancia
   * al cuadrado
   */
  def getVectorToPheromonesDistNormalized(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Vector2D = {
    var vcompass = new Vector2D(0,0);
    val box = simWithEnvPheromones.getPheromonesBoxAt(x, y, ofradius, defaultVal)
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
    
    vcompass.mul(-1)
    vcompass
  }
  
   /*Devuelve un vecto que apunta a la mayor cantidad de recurso
   */
  def getVectorToPheromones(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Vector2D = {
    var vcompass = new Vector2D(0,0);
    val box = simWithEnvPheromones.getPheromonesBoxAt(x, y, ofradius, defaultVal)
    val rangoSensor = ofradius
    
    
    for(i <- 0 to ofradius*2){
      for(j <- 0 to ofradius*2){
        
        val vij = new Vector2D(j-ofradius,i-ofradius)
        val intensity = (box(i)(j)) //Intensidad del recurso por su distancia al cuadrado
        vij.mul(intensity)
        vcompass.add(vij)
      }
    }
    
    vcompass.mul(-1)
    vcompass
  }
  
  def addPheromoneCircle(x:Int, y:Int, radius:Int, value:Double) = {
    
    val width = masonAgent.masonSim.sim.width
    val height = masonAgent.masonSim.sim.height
    
    //Genero un circulo del tamaño indicado
    val arrayCircle: Array[Array[Double]] = Shape2Array.generateCircleOfRadius(radius,1, 0)
    
    //Relleno el doubleGrid2D
    for(i <- 0 to radius*2-1){
      for(j <- 0 to radius*2-1){
        val px = j-radius + x
        val py = i-radius + y
        
        if(arrayCircle(i)(j)!=0){
            //El rango se comprueba dentro de la funcion, no hace falta hacerlo aquí
        	simWithEnvPheromones.addPheromonesUntil(px,py,value,1)
        }
      }
    } 
  }
  
  def addPheromoneCircleGradient(x:Int, y:Int, radius:Int, value:Double, alpha:Double=1) = {
    
    val width = masonAgent.masonSim.sim.width
    val height = masonAgent.masonSim.sim.height
    
    //Genero un circulo del tamaño indicado
    val arrayCircle: Array[Array[Double]] = Shape2Array.generateCircleOfRadius(radius,1, 0)
    
    //Relleno el doubleGrid2D
    for(i <- 0 to radius*2-1){
      for(j <- 0 to radius*2-1){
        val px = j-radius + x
        val py = i-radius + y
        
        if(arrayCircle(i)(j)!=0){
            val vn = value * alpha/(new Vector2D(j,i)).mod()
            //El rango se comprueba dentro de la funcion, no hace falta hacerlo aquí
        	simWithEnvPheromones.addPheromonesUntil(px,py,vn,1)
        }
      }
    } 
  }
  
  def setPheromoneCircle(x:Int, y:Int, radius:Int, value:Double) = {
    
    val width = masonAgent.masonSim.sim.width
    val height = masonAgent.masonSim.sim.height
    
    //Genero un circulo del tamaño indicado
    val arrayCircle: Array[Array[Double]] = Shape2Array.generateCircleOfRadius(radius,1, 0)
    
    //Relleno el doubleGrid2D
    for(i <- 0 to radius*2-1){
      for(j <- 0 to radius*2-1){
        val px = j-radius + x
        val py = i-radius + y
        
        if(arrayCircle(i)(j)!=0){
            //El rango se comprueba dentro de la funcion, no hace falta hacerlo aquí
        	simWithEnvPheromones.setPheromones(px,py,value)
        }

      }
    }
    
  }
  
   def setPheromoneCircleGradient(x:Int, y:Int, radius:Int, value:Double, alpha:Double=1) = {
    
    val width = masonAgent.masonSim.sim.width
    val height = masonAgent.masonSim.sim.height
    
    //Genero un circulo del tamaño indicado
    val arrayCircle: Array[Array[Double]] = Shape2Array.generateCircleOfRadius(radius,1, 0)
    
    //Relleno el doubleGrid2D
    for(i <- 0 to radius*2-1){
      for(j <- 0 to radius*2-1){
        val px = j-radius + x
        val py = i-radius + y
        
        if(arrayCircle(i)(j)!=0){
            //El rango se comprueba dentro de la funcion, no hace falta hacerlo aquí
        	val vn = value * alpha * (1-((new Vector2D(px-x,py-y)).mod()/radius))
        	simWithEnvPheromones.setPheromones(px,py,vn)
        }

      }
    }
    
  }

}