package simulator.environment

import java.awt.Color
import sim.display.Display2D
import sim.field.grid.DoubleGrid2D
import sim.portrayal.grid.FastValueGridPortrayal2D
import sim.util.gui.SimpleColorMap
import simulator.Environment
import util.EnvResourceHelper
import java.io.File
import scala.collection.mutable.MutableList
import Array._



/**
 * Entorno que carga un mapa desde una secuencia de imagenes y lo muestra en el GUI. Además proporciona la posibilidad de leer los pixels
 * de la imagen en una determianda posición
 * @author fidel
 *
 */
trait EnvMovingResource extends Environment {
  
  //Entorno de la traza
  var resource:DoubleGrid2D = _
  var resourceUI: FastValueGridPortrayal2D = _
  
  var resourceURL: String = _
  var stepsForAnim : Int = 0
  var files: MutableList[String] = MutableList[String]()
  var seqIt: Int = 0
  
  override def initFields() = {
    super.initFields()
    
    //Obtengo el listado de ficheros de imagen de la animación. Solo acepta PNG o JPG
    for (file <- new File(resourceURL).listFiles) {
      if(file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) files += file.getAbsolutePath() 
    } 
    
    val helper = new EnvResourceHelper()
    resource = helper.groundFromImage(files.head)
  }
  
  override def initPortrayals() = {
    super.initPortrayals
    resourceUI = new FastValueGridPortrayal2D("Resource", false);
     
    
  }
  
  override def setupPortrayals() = {
    super.setupPortrayals()
    resourceUI.setField(resource)
    resourceUI.setMap(new SimpleColorMap(
                                      0,
                                      1,
                                      new Color(0,0,0,160),
                                      new Color(255,255,255,100)));
  }
  
  override def setupDisplay(display:Display2D) = {
    super.setupDisplay(display)
    display.attach(resourceUI,"Resource")
  }
  
  //Si ha pasado el numero de pasos indicados cambio la imagen a la que corresponda
  override def onSwarmStep() = {
    super.onSwarmStep()
    if(sim.stepsExecuted>0 && stepsForAnim>0) {
      if(sim.stepsExecuted%stepsForAnim==0){
        seqIt+=1; if(seqIt>=files.size) seqIt=0
        updateResource
      }
    }

  }
  
  
  def updateResource() = {
    val helper = new EnvResourceHelper()
    helper.updateGroundFromImage(resource, files(seqIt))
  }
  
  
  def getResourceAt(x:Int,y:Int): Double = {
    if(x>0 && y>0 && x<sim.width && y<sim.height) 1-resource.get(x,y) else -1
    
  }
  
    //Obtiene un cuadrado centrado en x,y de tama�o ofradius*2. Si la caja sale fuera de rango se rellena con defaultVal
  def getBoxAt(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Array[Array[Double]] = {
    
    val box = ofDim[Double](ofradius*2+1,ofradius*2+1)
    
    for(i <- 0 to ofradius*2){
      for(j <- 0 to ofradius*2)
      {
    	  val rx = x+i-ofradius
    	  val ry = y+j-ofradius
    	  
    	  box(i)(j) = if(rx>=0 && ry>=0 && rx<sim.width && ry<sim.height) 1-resource.get(rx, ry) else defaultVal;
    	  
      }
    }
    box
  }

}