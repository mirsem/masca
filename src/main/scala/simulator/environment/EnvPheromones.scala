package simulator.environment

import java.awt.Color

import sim.display.Display2D
import sim.field.grid.DoubleGrid2D
import sim.portrayal.grid.FastValueGridPortrayal2D
import sim.util.gui.SimpleColorMap
import simulator.Environment
import simulator.Robot
import Array._


/**
 * Entorno que proporciona un mapa de traza, que guarda cierto tiempo el movimiento realizado por los robots
 * y lo muestra en el GUI
 * @author fidel
 *
 */
trait EnvPheromones extends Environment {
  
  //Entorno de la traza
  var phero:DoubleGrid2D = _
  var pheroUI: FastValueGridPortrayal2D = _
  var decay = 0.999
  var initPheroValue = 0
  
  override def initFields() = {
    super.initFields()
    phero = new DoubleGrid2D(sim.width,sim.height,initPheroValue)
  }
  
  override def initPortrayals() = {
    super.initPortrayals
    pheroUI = new FastValueGridPortrayal2D("Pheromones", false);
  }
  
  override def setupPortrayals() = {
    super.setupPortrayals()
    pheroUI.setField(phero)
    pheroUI.setMap(new SimpleColorMap(
                                      0,
                                      1,
                                      new Color(255,255,255,255),
                                      new Color(100,100,255,255)));
  }
  
  override def setupDisplay(display:Display2D) = {
    super.setupDisplay(display)
    display.attach(pheroUI,"Pheromones")
  }
  
  
  override def onSwarmStep() = {
    super.onSwarmStep()
    phero.multiply(decay);
  }
  
  def getPheromones(x:Int,y:Int): Double = {
    if(x>0 && y>0 && x<sim.width && y<sim.height) phero.get(x,y) else -1
  }
  
  def setPheromones(x:Int,y:Int, v:Double) = {
    if(x>0 && y>0 && x<sim.width && y<sim.height) phero.set(x,y,v) 
  }
  
  def addPheromones(x:Int,y:Int, v:Double) = {
    if(x>0 && y>0 && x<sim.width && y<sim.height) phero.set(x,y,phero.get(x,y)+v) 
  }
  
  def addPheromonesUntil(x:Int,y:Int, v:Double, limit:Double) = {
    
    if(x>0 && y>0 && x<sim.width && y<sim.height) {
      val t = phero.get(x,y)+v
      val vt = if(t>limit) limit else t
      phero.set(x,y,t) 
    }
  }
  
  //Obtiene un cuadrado centrado en x,y de tama�o ofradius*2. Si la caja sale fuera de rango se rellena con defaultVal
  def getPheromonesBoxAt(x:Int, y:Int, ofradius:Int, defaultVal:Int=0):Array[Array[Double]] = {
    
    val box = ofDim[Double](ofradius*2+1,ofradius*2+1) 
    
    for(i <- 0 to ofradius*2){
      for(j <- 0 to ofradius*2)
      {
    	  val rx = x+j-ofradius
    	  val ry = y+i-ofradius
    	  
    	  box(i)(j) = if(rx>=0 && ry>=0 && rx<sim.width && ry<sim.height) 1-phero.get(rx, ry) else defaultVal;
    	  
      }
    }
    box
  }

}