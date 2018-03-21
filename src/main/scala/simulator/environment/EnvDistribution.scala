package simulator.environment

import java.awt.Color
import java.io._

import sim.display.Display2D
import sim.field.grid.DoubleGrid2D
import sim.portrayal.grid.FastValueGridPortrayal2D
import sim.util.gui.SimpleColorMap
import simulator.Environment
import simulator.Robot


/**
 * Entorno que proporciona un mapa de traza, que guarda cierto tiempo el movimiento realizado por los robots
 * y lo muestra en el GUI
 * @author fidel
 *
 */
trait EnvDistribution extends Environment {
  
  //Entorno de la traza
  var trace:DoubleGrid2D = _
  var traceUI: FastValueGridPortrayal2D = _
  var stepsForSave : Int = 0
  var filename: String = "distribution.txt";
  var distributionWidth: Int = 0
  var factor = 0.0d
  
  override def initFields() = {
    super.initFields()
    
    if(distributionWidth==0) trace = new DoubleGrid2D(sim.width,sim.height,0)
    else {
      trace = new DoubleGrid2D(distributionWidth,distributionWidth,0)
    }
    
  }
  
  override def initPortrayals() = {
    super.initPortrayals
    traceUI = new FastValueGridPortrayal2D("Distribution", false);
  }
  
  override def setupPortrayals() = {
    super.setupPortrayals()
    traceUI.setField(trace)
    traceUI.setMap(new SimpleColorMap(
                                      0,
                                      10000,
                                      new Color(255,255,255,255),
                                      new Color(255,0,0,255)));
  }
  
  override def setupDisplay(display:Display2D) = {
    super.setupDisplay(display)
    display.attach(traceUI,"UIDistribution")
  }
  
  override def onRobotStep(robot:Robot) = {
    
    
    if(distributionWidth==0) {
          if(robot.pos.x>0 && robot.pos.y>0 && robot.pos.x<sim.width && robot.pos.y<sim.height) 
        	  trace.set(robot.pos.x.toInt,robot.pos.y.toInt, trace.get(robot.pos.x.toInt,robot.pos.y.toInt)+1);
    } else {
          if(robot.pos.x>0 && robot.pos.y>0 && robot.pos.x<sim.width && robot.pos.y<sim.height) {
        	  val tx = (robot.pos.x*factor).toInt
        	  val ty = (robot.pos.y*factor).toInt
        	  

        	  trace.set(tx,ty, trace.get(tx,ty)+1);
          }
    }
    

  }
  
  override def onSwarmStep() = {
    super.onSwarmStep()
    factor = distributionWidth/(sim.width*1d)
    if(this.sim.stepsExecuted==stepsForSave){
      toFile(filename);
    }
   // trace.multiply(0.999);
  }
  
  def getTrace(x:Int,y:Int): Double = {
    if(x>0 && y>0 && x<sim.width && y<sim.height) trace.get(x,y) else -1
  }
  
  def toFile(url:String) {
    
    if(distributionWidth==0){
	    val writer = new PrintWriter(new File(url))
	    writer.write("[\n");
	     for(i <- 0 until trace.getHeight()){
	       writer.write("  ");
	       for(j <- 0 until trace.getWidth()){
	         if(j+1==sim.width) writer.write(trace.get(j,i)+"");
	         else writer.write(trace.get(j,i)+",");
	       }
	       writer.write(";\n");
	     }
	     writer.write("]");
	     writer.close()
    } else {
	    val writer = new PrintWriter(new File(url))
	    writer.write("[\n");
	     for(i <- 0 until distributionWidth){
	       writer.write("  ");
	       for(j <- 0 until distributionWidth){
	         if(j+1==sim.width) writer.write(trace.get(j,i)+"");
	         else writer.write(trace.get(j,i)+",");
	       }
	       writer.write(";\n");
	     }
	     writer.write("]");
	     writer.close()
    }
    
    
  }

}