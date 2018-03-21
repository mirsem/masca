package simulator.interactuators

import simulator.Interactuator
import simulator.environment.EnvTrace
import simulator.mason.MasonSwarm


/**
 * Usado junto con EnvTrace permite que un robot obtenga lecturas de las huellas que dejan los robots al desplazarse
 * @author fidel
 *
 */
trait ITrace extends Interactuator {
  
  var simWithEnvTrace:MasonSwarm with EnvTrace = null 
  
  override def initInteractuator() = {
    super.initInteractuator();
    
    simWithEnvTrace = masonAgent.masonSim match  {
     						case sim: EnvTrace => sim
     						case _ => {println("Itrace.getTrace: No se pudo utilizar ITrace ya que la simulación no utiliza el entorno EnvTrace");sys.exit(0); null}
     				  }
  }
  
  def getTrace(x:Int, y:Int):Double = simWithEnvTrace.getTrace(x, y)
  
  def setTraceAtCurrentPos() = {
    
    val robot=masonAgent.robot;
    
    if(simWithEnvTrace.traceRadius>1) simWithEnvTrace.addCircle(robot.pos.x.toInt,robot.pos.y.toInt, 10,1d)
    else simWithEnvTrace.trace.set(robot.pos.x.toInt,robot.pos.y.toInt, 1d);
 
  }

}