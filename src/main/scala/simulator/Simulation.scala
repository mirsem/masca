package simulator

import mason.MasonSwarm
import sim.engine.SimState
import sim.engine.MakesSimState
import mason.MasonGUI
import sim.display.Console
import scala.beans.BeanProperty
import simulator.mason.MasonSwarmFactory
import simulator.mason.MasonPortrayal2DRenderFactory


/**
 * Esta clase construye la simulación. Esta se basa en el simulador Mason más concretamente en una instancia
 * de dicho simulador llamada simState. La clase simulación permita ejecutar una simulación en modo texto o
 * usando el GUI heredado de Mason
 * 
 * @param swarm Enjambre sobre el que se construye la simulación
 * @param with Ancho del entorno sobre el que se moveran los agentes (aunque estos puedan salir de este entorno no se 
 * garantiza que los sensores esten disponibles fuera del mismo)
 * @param height Alto del entorno
 * @param factorySimState Factoria para la creación del estado de simulación de Mason. Esta factoria hay que utilizarla si
 * se desea personalizar la simulación (GUI o añadir funcionalidades al entorno), en otro caso no es necesaria
 * @param onSimulationStep Procedimiento que se llamará en cada paso de la simulación. Debe recibir como parámetro un objeto simulación y no devolver nada
 *  
 */

class Simulation(val swarm:Swarm, val width:Int=200, val height:Int=200, 
    val masonSwarmFactory:MasonSwarmFactory = null, 
    val masonPortrayal2DRenderFactory: MasonPortrayal2DRenderFactory = null,
    val onSimulationStep: (Simulation)=>Unit = null,
    val robotsWillCollide:Boolean = true,
    val swarmInitiallizer: (Swarm) => Unit = null) {
  
  var simState: MasonSwarm = null;
  swarm.sim = this
  
  
  /**
   * Ejecuta la simulación en modo texto
   */
  def run() = {
    val mkSimState = if(masonSwarmFactory==null) new MasonSwarmFactory else masonSwarmFactory
    mkSimState.sim = this
    SimState.doLoop(mkSimState, Array[String]())
  }
  
  /**
   * Ejecuta las simulacion utilizando el UI de MASON
   */ 
  def runWithUI() = {
    val mkSimState = if(masonSwarmFactory==null) new MasonSwarmFactory else masonSwarmFactory
    val mkPortrayalRender = if(masonPortrayal2DRenderFactory==null) new MasonPortrayal2DRenderFactory else masonPortrayal2DRenderFactory
    
    mkSimState.sim = this
    simState = mkSimState.newInstance(System.currentTimeMillis(),Array[String]()).asInstanceOf[MasonSwarm]
    
    val mav = new MasonGUI(this,simState,mkPortrayalRender);  
    val c = new Console(mav);
    c.setVisible(true);
  }
  
  private[simulator] def setSimState(s:MasonSwarm) = simState = s
  
  def randDouble = simState.random.nextDouble()
  
  /**
   * Número de pasos que se han ejecutado desde el inicio de la simulación
   */
  def stepsExecuted = simState.stepsExecuted

}