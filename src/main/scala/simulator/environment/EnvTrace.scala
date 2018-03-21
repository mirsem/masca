package simulator.environment

import java.awt.Color
import sim.display.Display2D
import sim.field.grid.DoubleGrid2D
import sim.portrayal.grid.FastValueGridPortrayal2D
import sim.util.gui.SimpleColorMap
import simulator.Environment
import simulator.Robot
import util.Shape2Array


/**
 * Entorno que proporciona un mapa de traza, que guarda cierto tiempo el movimiento realizado por los robots
 * y lo muestra en el GUI
 * @author fidel
 *
 */
trait EnvTrace extends Environment {
  
  //Tamaño de la traza
  var traceRadius = 1
  
  //Decay de la traza
  var traceDecay = 0.999
  
  //Modo de traza (0: automático conforme se mueva o 1:cuando se solicite se marca la posición en la que se encuentre
  var traceMode=0
  
  //Entorno de la traza
  var trace:DoubleGrid2D = _
  var traceUI: FastValueGridPortrayal2D = _
  
  override def initFields() = {
    super.initFields()
    trace = new DoubleGrid2D(sim.width,sim.height,0)
  }
  
  override def initPortrayals() = {
    super.initPortrayals
    traceUI = new FastValueGridPortrayal2D("Traza", false);
  }
  
  override def setupPortrayals() = {
    super.setupPortrayals()
    traceUI.setField(trace)
    traceUI.setMap(new SimpleColorMap(
                                      0,
                                      1,
                                      new Color(255,255,255,100),
                                      new Color(255,0,0,100)));
  }
  
  override def setupDisplay(display:Display2D) = {
    super.setupDisplay(display)
    display.attach(traceUI,"Traza")
  }
  
  def addCircle(x:Int, y:Int, radius:Int, value:Double) = {
    
    val width = sim.width
    val height = sim.height
    
    //Genero un circulo del tamaño indicado
    val arrayCircle: Array[Array[Double]] = Shape2Array.generateCircleOfRadius(radius,1, 0)
    
    //Relleno el doubleGrid2D
    for(i <- 0 to radius*2-1){
      for(j <- 0 to radius*2-1){
        val px = j-radius + x
        val py = i-radius + y
        
        if(arrayCircle(i)(j)!=0){
          if(px>0 && py>0 && px<sim.width &&py<sim.height) trace.set(px,py,1d)
        }
      }
    } 
  }
  
  override def onRobotStep(robot:Robot) = {
    if(traceMode==0 && robot.pos.x>0 && robot.pos.y>0 && robot.pos.x<sim.width && robot.pos.y<sim.height) 
      if(traceRadius>1) addCircle(robot.pos.x.toInt,robot.pos.y.toInt, 10,1d)
      else trace.set(robot.pos.x.toInt,robot.pos.y.toInt, 1d);
  }
  
  override def onSwarmStep() = {
    super.onSwarmStep()
    trace.multiply(traceDecay);
  }
  
  
  def getTrace(x:Int,y:Int): Double = {
    if(x>0 && y>0 && x<sim.width && y<sim.height) trace.get(x,y) else -1
  }

}