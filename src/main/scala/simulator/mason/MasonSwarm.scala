package simulator.mason

import sim.engine.SimState
import sim.engine.Steppable
import simulator.Swarm
import sim.field.continuous.Continuous2D
import sim.field.grid.DoubleGrid2D
import simulator.Simulation
import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer
import simulator.Environment
import simulator.environment.EnvResource



/**
 * Contiene el objeto de simulación de MASON. 
 * Además propociona el evento onStep, que se lanza para cada paso de la simulación.
 * Si existen los métodos getX o setX provocarán que se muestre la propiedad X en el GUI del simulador. 
 * 
 * @constructor Crea un objeto MasonSim
 * @param seed semilla utilizada para la generación de los números aleatorios. Se puede usar para recrear una simulación 
 * (dada una semilla la simulación es idéntica)
 * @param simulacion una simulación de MASON debe estar vinculada a un objeto simulación
 * @author fidel
 *
 */


class MasonSwarm(seed:Long, val sim:Simulation) extends SimState(seed) with Environment {
  
  var agentField:Continuous2D = _ 
  @BeanProperty var bounded = true
  var stepsExecuted:Int=0
  
  
  /** Evento onStep, que se lanza en casa paso de la simulación, después de haber ejecutado el step de todos los agentes del enjambre*/
  var onStep: (Simulation) => Unit =(_)=>{
    stepsExecuted+=1;
    refresh
    if(sim.onSimulationStep!=null){
      sim.onSimulationStep(sim)
    }
  }
  
  
  override def start()={
    
    super.start;
    stepsExecuted=0
    initFields()
    
    //Actualizo el simState del simulador 
    sim.setSimState(MasonSwarm.this)
    
    //Si existe el metodo de inicialización del enjambre lo ejecuto 
    if(sim.swarmInitiallizer!=null) sim.swarmInitiallizer(sim.swarm)
     
    //Inicializo los agentes del enjambre
    sim.swarm.initRobots
    
    //Para cada robot creo un agente de Mason que lo representa y lo añado a la simulacion (por defecto orden 0)
    sim.swarm.robots.map(r => {
      r.masonAgent.masonSim = MasonSwarm.this
      r.masonAgent.init
      schedule.scheduleRepeating(r.masonAgent)
    })
    
    //Creo un evento al final de actualizar todos los agentes en cada iteracion (orden 1 (despues de los agentes que son 0) cada 1 segundo)
    schedule.scheduleRepeating(new Steppable(){
            override def step(state: SimState) { 
               onStep(sim)
               sim.simState.onSwarmStep()
            }
          }, 1,1
        );
  }
  
  
  //Verifica que todos los agentes del enjambre tengan su reflejo en mason
  def refresh() = {
    sim.swarm.robots.map(r => {
      if(r.masonAgent.masonSim==null){
        r.masonAgent.masonSim = MasonSwarm.this
        r.masonAgent.init
        r.onInit
        schedule.scheduleRepeating(r.masonAgent)
      }

    })
  }
  
  //Elimina la simulacion
  override def finish() = {
    super.finish
    stepsExecuted=0
    sim.swarm.robots.clear
  }
  
  override def initFields() = {
	 agentField = new Continuous2D(sim.width,sim.width,sim.height)
  }
  
}

