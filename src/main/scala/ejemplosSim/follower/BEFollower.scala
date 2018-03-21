package ejemplosSim.follower

import simulator.behaviour.BEFSM
import simulator.behaviour.BERandom
import sim.util.Double2D
import simulator.interactuators.IResource
import simulator.Robot


/**
 * Ejemplo de comportamiento donde se intentan formar filas entre los robots que se encuentren. Si un robot se detecta por la derecha se
 * cede el paso
 * @author fidel
 *
 */
class BEFollower extends BEFSM {
  
  val minDist = 1.5d
  val maxVel = .3d
  var robotWithResource: Robot with IResource = _
  
  //Creo el automata del comportamiento
  addState("Fila", withAction = onFila)
  addState("Collision", withAction = onCollision)
  setInitialState("Fila")
  
  addTransition("Fila","Collision", withCondition={()=>robot.collision})
  addTransition("Collision","Fila", withCondition={()=>robot.collision==false})
  
  
  /**
   * Obtengo el robot con capacidades de lectura de recursos
   */
  override def init(r:Robot) = {
    super.init(r)
    
    robot match  {
     case t:IResource => {robotWithResource = t}
     case _ => {println("BEFollower: No se pudo utilizar ITrace ya que la simulación no utiliza el entorno EnvResource")}
   }
  }
  
  
  /**
   * Si colisiona entonces genera un vector al azar
   */
  def onCollision() = {
    robot.vel = BERandom.mutateVel(robot.vel,0.5d,sim) 
    robot.vel.mul(maxVel);
    slowOnResource
  }
  
  /**
   * Comportamiento de formar una fila. Sigue al robot más cercano del área frontal del sensor, 
   * intentado acercarse como mucho a minDist
   */
  def onFila() = {
    val r = robot
    
    val front = r.detectOtherRobotsBetween(-math.Pi/4,math.Pi/4,r.sonarRange);
    val left = r.detectOtherRobotsBetween(-math.Pi/4-math.Pi/2,-math.Pi/4,r.sonarRange);
    val cederPaso = if(left.size>0) true else false
    val seguir = if(front.size>0) true else false
    
    //Si cedo el paso bajo la velocidad hasta casi parar 
    if(cederPaso) {
      r.vel.normalize()
      r.vel.mul(0.007d)
    } else {
      //Si hay algo que seguir
      if(seguir) {
        //Obtengo la terna que caracteriza al robot más cercano e intento mantener una distancia
        val love = front(0)
        if(love._2<minDist) {
          //Me acerco demasiado. Disminuyo un 99% la velocidad 
          follow(love._1.pos)
          r.vel.mul(0.01)
        } else {
          //Lo sigo
          follow(love._1.pos)
          //r.vel.mul(maxVel);
        }
      } else {
        //Sino al azar
        r.vel = BERandom.mutateVel(robot.vel,0.1d,sim) 
        r.vel.mul(maxVel);
      }
    }
    slowOnResource
  }
  
  def follow(dest:Double2D) = {
     val r = robot
     r.vel.set(dest.x - r.pos.x,dest.y - r.pos.y);
     r.vel.normalize();
     r.vel.mul(maxVel);
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
  

}