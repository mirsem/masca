package ejemplosSim.busquedaRF

import simulator.behaviour.BEFSM
import simulator.behaviour.BERandom
import sim.util.Double2D
import simulator.interactuators.IResource
import simulator.Robot
import util.Vector2D

class BEResourceFinder extends BEFSM {
  
  var robotWithResource: RobotLover with IResource = _
  
  val pWander = .274
  val pDiscover = .154
  val pGroup = .222
  
  val stepsForChange = 300
  val engInit = 500d;
  val incEng = 35d;
  val maxVel = 0.5
  
  //Creo el automata del comportamiento
  
  addState("Expand",withAction =onExpand)
  addState("Wander",withAction =onWander)
  addState("Group", withAction =onGroup)
  setInitialState("Expand")
  
  addTransition("Expand","Wander",withCondition=()=>{transitionWith(pWander)})
  addTransition("Wander","Expand",withCondition=()=>{transitionWith(pDiscover)})
  addTransition("Wander","Group",withCondition=()=>{transitionWith(pGroup)})
  addTransition("Group","Expand",withCondition=()=>{transitionWith(energy())})
  

  
  /**
   * Obtengo el robot con capacidades de lectura de recursos
   */
  override def init(r:Robot) = {
    super.init(r)
    
    r match  {
     case t:RobotLover with IResource => {robotWithResource = t}
     case _ => {println("BEFollower: No se pudo utilizar ITrace ya que la simulación no utiliza el entorno EnvResource")}
   }
  }
  

  def onExpand() = {
    if(robot.collision)  robot.vel = BERandom.mutateVel(robot.vel,0.5d,sim) 
    else {
      robot.vel = evitar(robot.pos,robotWithResource.robotB.pos)
      robot.vel.mul(sensorSuelo())
      robot.vel.mul(maxVel);
    }

  }
  
  def onWander() = {
    robot.vel = BERandom.mutateVel(robot.vel,0.5d,sim) 
    robot.vel.mul(maxVel);
  }
  
  def onGroup() = {
    if(robot.collision)  robot.vel = BERandom.mutateVel(robot.vel,0.5d,sim) 
    else {
      robot.vel = seguir(robot.pos,robotWithResource.robotA.pos)
      robot.vel.mul(sensorSuelo())
      robot.vel.mul(maxVel);
    }
  }
  
  def sensorSuelo() = math.pow(1-robotWithResource.getResourceAt(robotWithResource.pos.x.toInt, robotWithResource.pos.y.toInt),2)
  
  def evitar(org:Double2D, dest:Double2D) = {
    val v = new Vector2D();
    v.x = org.x-dest.x;
    v.y = org.y-dest.y;
    v.normalize();
    v
  }
  
  def seguir(org:Double2D, dest:Double2D) = {
    val v = new Vector2D();
    v.x = dest.x-org.x;
    v.y = dest.y-org.y;
    v.normalize();
    v
  }
  
  
  /**
   * Disminuyo la velocidad de deambular dependiendo de la cantidad de recursos
   * a mayor recurso más lento
   */
  def slowOnResource() = {
      val x = robot.pos.x.toInt
      val y = robot.pos.y.toInt
	  val factor = math.pow(1-robotWithResource.getResourceAt(x,y), 1);
      robot.vel.mul(factor);
  }
  
  
  def transitionWith(p:Double) = {
    val draw = if(sim.randDouble<p) true else false
    val stp = ((sim.simState.stepsExecuted+1)%stepsForChange)==0
    
    stp && draw
  }
  
  def energy() = math.pow((-(-engInit+incEng)/engInit),(sim.simState.stepsExecuted/stepsForChange));
  

}