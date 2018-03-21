package simulator.behaviour

import simulator.Behaviour
import scala.collection.mutable.Map
import simulator.Robot

class BEFSM extends Behaviour{ 
  
  val states = Map[String,()=>Unit]()
  val transitions = Map[String,Map[String,()=>Boolean]]()
  var initialState:String = null
  
  //Estado actual de la máquina de estados en funcionamiento
  var runningState:String = null
  
  
  override def init(r:Robot) = {
    super.init(r)
    runningState = initialState
  }
  
  def addState(name:String, withAction:()=>Unit) = states(name) = withAction
  
  def setInitialState(name:String) = initialState = name
  
  def addTransition(state1:String,state2:String, withCondition:()=>Boolean) = {
    //Compruebo que los dos estados esten definidos previamente
    if(states.contains(state1) && states.contains(state2)){
      // Si no hab�a ninguna transici�n desde este estado
      if(!transitions.contains(state1)) transitions(state1) =Map(state2 -> withCondition)
      // Si ya hab�a alguna reutilizo el mapa a�adiendo las nuevas transiciones
      else transitions(state1) += (state2 -> withCondition)
    }
    //if(states.contains(state1) && states.contains(state2)) transitions(state1) =Map(state2 -> withCondition)
    else printf("Error en BEFSM.addTransition: transición no agregada porque no existe el estado 1 o el estado 2, creelos previamente")
  }
  
  override def step = {
    
    var changeState = false
    
    //Pongo el primer estado como el inicial
    if(runningState==null) {
      //Si no se ha dispuesto estado inicial cojo el primero
      if(initialState==null) {
        println("Error en BEFSM.step: no se definió un estado inicial")
        sys.exit(1)
      } 
      runningState = initialState
      
    //O intento disparar la transición
    } else {
      //Si existe alguna transición a partir del estado actual
      if(transitions.contains(runningState)) {
        val canGoTo = transitions(runningState)
        for(t <- canGoTo if !changeState) {
          val toState = t._1
          //println("** " + toState)
          //Si se da la transición cambio el estado actual al siguiente
          changeState = t._2()
          if(changeState) {
            runningState = toState
          }
        }
      }
      //println("_______")
    }
    
    //Ejecuto la acción del estado actual
    val action = states(runningState)
    action() 
    
  }

} 