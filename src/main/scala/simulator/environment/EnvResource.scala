package simulator.environment

import java.awt.Color

import sim.display.Display2D
import sim.field.grid.DoubleGrid2D
import sim.portrayal.grid.FastValueGridPortrayal2D
import sim.util.gui.SimpleColorMap
import simulator.Environment
import util.EnvResourceHelper
import Array._


/**
 * Entorno que carga un mapa desde una imagen y lo muestra en el GUI. Además proporciona la posibilidad de leer los pixels
 * de la imagen en una determianda posición
 * @author fidel
 *
 */
trait EnvResource extends Environment {
  
  //Entorno de la traza
  var resource:DoubleGrid2D = _
  var resourceUI: FastValueGridPortrayal2D = _
  var resourceURL: String = _
  
  override def initFields() = {
    super.initFields()
    
    val helper = new EnvResourceHelper()
    resource = helper.groundFromImage(resourceURL)
    
    if(resource.getWidth()!=sim.width || resource.getHeight()!=sim.height){
      println("Error en EnvResource: el mapa ha de ser del tamaño del simulador");
      sys.exit(1)
    }
    
  }
  
  override def initPortrayals() = {
    super.initPortrayals
    resourceUI = new FastValueGridPortrayal2D("Resource", true);
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
  
  
  def getResourceAt(x:Int,y:Int): Double = {
    if(x>=0 && y>=0 && x<sim.width && y<sim.height) 1-resource.get(x,y) else -1
    
  }
  
  //Obtiene un cuadrado centrado en x,y de tama�o ofradius*2. Si la caja sale fuera de rango se rellena con defaultVal
  def getBoxAt(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Array[Array[Double]] = {
    
    val box = ofDim[Double](ofradius*2+1,ofradius*2+1) 
    
    for(i <- 0 to ofradius*2){
      for(j <- 0 to ofradius*2)
      {
    	  val rx = x+j-ofradius
    	  val ry = y+i-ofradius
    	  
    	  box(i)(j) = if(rx>=0 && ry>=0 && rx<sim.width && ry<sim.height) 1-resource.get(rx, ry) else defaultVal;
    	  
      }
    }
    box
  }

}