package simulator.mason

import sim.engine.Steppable
import sim.portrayal.Oriented2D
import sim.engine.SimState
import simulator.Robot
import sim.util.Double2D
import scala.beans.BeanProperty
import simulator.Environment
import util.Vector2D 
import sim.util.Bag
import scala.math
import java.util.Comparator

/**
 * Agente MASON que pertenecerá a un MasonSIM. Cada Agente Mason contiene un robot, que es el que determina
 * su comportamiento. Esta clase la utiliza MASON para mostrar las propiedades de los agentes cuando se hace doble
 * click sobre ellos en el GUI. Cualquier propiedad o método getX, setX provocará que dicha propiedad X se muestre 
 * automáticamente.
 * @author fidel
 *
 */
class MasonAgent(val robot:Robot) extends Steppable with Oriented2D {
  
  var masonSim:MasonSwarm with Environment = null
  @BeanProperty var collision = false

  override def orientation2D() = robot.angle
  
  /** Mason llama a esta función en cada paso de simulación. Lo que realiza es lo siguiente:
   * Actualiza la posición del robot a partir de la del agente de simulación.
   * Ejecuta el método onStep del robot
   * Obtiene la nueva posición del robot, actualizándola a partir de su posición anterior y de
   * su vector velocidad (que puede haber modificado robot.onStep).
   * Dispone en MASON la nueva posición del agente
   */
  override def step(sim:SimState):Unit = {
    
	    robot.pos = masonSim.agentField.getObjectLocation(this)
	    
	    //Si no hay objeto en el field es que se elimino de MASON, no actualizar, salgo
	    if(robot.pos==null) return
	    
	    robot.onStep
	
	    //Por defecto ya no tiene colisión hasta que se compruebe lo contrario con su nueva vel
	    //collision = false
	    
	    //Ahora si colisiona no puede descolisionar...
	    
	    //Detecto si se utiliza un mapa para que ningún robot pueda acceder a las posiciones ocupadas
	    if(collision) {
	      	//TODO: Solo para esta simulacion. Si hay colision desaparece el agente
	        //masonSim.agentField.remove(this)
	    } else {
  	    if(masonSim.bounded) {
  	      val (col,cpos) = calculateWallCollisionAndDestination(robot.pos,robot.vel)
  	      if(!col) {
  	        if(masonSim.sim.robotsWillCollide) collision = calculateRobotCollision(robot)
  	        //Compruebo colisiones con el entorno
  	        if(!collision) {
  	        	collision = calculateEnvironmentCollision(robot)
  	        }
  	        if(!collision) masonSim.agentField.setObjectLocation(this, cpos)
  	      } else collision = true
  	    } else {
  	      //Compruebo colisiones con otros robots
  	      if(masonSim.sim.robotsWillCollide)  collision = calculateRobotCollision(robot)
  	      //Compruebo colisiones con el entorno
  	      if(!collision) {
  	        collision = calculateEnvironmentCollision(robot)
  	      }
  	      //Si no hay colisiones actualizo la posición a la nueva
  	      if(!collision) masonSim.agentField.setObjectLocation(this, new Double2D(robot.pos.x+robot.vel.x,robot.pos.y+robot.vel.y))
  	      
  	    }
	    }
	    
	    masonSim.onRobotStep(robot)
    
  }
  
  def init() = {
    masonSim.agentField.setObjectLocation(this, robot.pos)
    collision = false
    robot.onStart
  }
  
  
  /**
   * Calcula si un robot colisionará con los márgenes del mundo dada su posición y su vector velocidad
   * Y si es así retorna la nueva posición del robot al ejecutar vel. justo antes de chocar
   */
  def calculateWallCollisionAndDestination(robotPos:Double2D, vel:Vector2D) = {
    
    val pos = new Double2D(robotPos.x+vel.x,robotPos.y+vel.y);
    
    var posX:Double = pos.x
    var posY:Double = pos.y
    var col: Boolean = false;
    val width = masonSim.sim.width
    val height = masonSim.sim.height
    
    if(pos.x<0) {posX = 0 ;col = true;} 
    if(pos.y<0) {posY = 0 ;col = true;} 
    if(pos.x>=width) {posX = width-1 ;col = true;} 
    if(pos.y>=height) {posY = height-1 ;col = true;} 
    
    (collision,new Double2D(posX,posY))
  
  }
  
  /**
   * Calcula si el robot va a colisionar con otro agente si aplica el vector de velocidad actual
   */
  def calculateRobotCollision(robot:Robot):Boolean = {
    
    val agents= masonSim.agentField.allObjects
    val id = masonSim.agentField.getObjectIndex(this);
    val p = new Double2D(robot.pos.x+robot.vel.x,robot.pos.y+robot.vel.y);
    
     for (i <- 0 until agents.size()) { 
       if(i!=id) {
         val a = agents.get(i).asInstanceOf[MasonAgent]
         if(a.robot.robotWillCollide && robot.robotWillCollide){
           val r = new Double2D(a.robot.pos.x+ a.robot.vel.x,a.robot.pos.y+ a.robot.vel.y)
           val dist = math.sqrt((r.x - p.x) * (r.x - p.x) + (r.y - p.y) * (r.y - p.y));
           if(dist<1) {
             a.robot.collisions_intrarobot+=1
             robot.collisions_intrarobot+=1
             return true
           }
         }
       }
     }
    false
  }
  
  
  /**
   * Calcula si el robot ha colisionado con el entorno
   */
  def calculateEnvironmentCollision(robot:Robot):Boolean = {
    val c = robot.masonAgent.masonSim.isCollision(robot)
    if(c) robot.collisions_environment+=1
    return c
  }
  
  
  /**
   * Retornar todos los robots que esten a una distancia del actual range o menor
   */
  def sonarReadingsOnlyForOtherRobots(range:Double):Bag = {
    //Obtengo, como mínimo todas las lecturas que caen dentro del sensor
    val raw = masonSim.agentField.getObjectsWithinDistance(robot.pos, range)
    
    //Genero una Bag no solo con las lecturas sino también con la distancia y orientacion
    val infb = new Bag()
    
    for(i <- raw.objs){
      val t = i.asInstanceOf[MasonAgent].robot
      val dist = math.sqrt((robot.pos.x-t.pos.x)*(robot.pos.x-t.pos.x)+(robot.pos.y-t.pos.y)*(robot.pos.y-t.pos.y));
      val ang = math.atan2(t.pos.y-robot.pos.y,t.pos.x-robot.pos.x) - robot.angle
      
      //Si la distancia está dentro del rango de detección
      if(dist<range && dist>0){
        val l = new Bag(3)
        l.add(t); l.add(dist); l.add(ang)
        infb.add(l)
      }
      
      //Una vez que tengo la información de distancia y orientación ordeno por distancia al robot
      //Ordeno la bolsa por distancia al robot
      infb.sort(new Comparator[Bag]() {
          override def compare(a1:Bag, a2:Bag):Int = {
            
            val da1 = a1.objs(1).asInstanceOf[Double]
            val da2 = a2.objs(1).asInstanceOf[Double]
            
            if(da1>da2) 1 else {if(da1<da2) -1 else 0}
          }
      }
      )
    }
    
    infb
  }
  

}