package simulator

import mason.MasonAgent
import util.Vector2D
import sim.util.Double2D
import sim.engine.SimState
import simulator.mason.MasonAgentFactory
import scala.collection.mutable.ArrayBuffer
import sim.util.Bag
import java.awt.geom.Rectangle2D
import java.awt.Graphics2D
import java.awt.Graphics2D
import java.awt.Graphics2D
import sim.portrayal.DrawInfo2D
import util.MasonRenderHelper
import java.awt.Color


/**
 * Robot que formará parte de un enjambre. Se trata de un robot situado en un entorno 2D y que por defecto 
 * dispone de un vector de movimiento que se aplicará en cada iteración. Como sensores puede percibir aquellos
 * robots dentro de un intervalo de distancia y ángulo. El robot ejecuta el comportamiento pasado por parámetro 
 * en cada iteración
 * 
 * Se proporcionan varios eventos que se lanzarán a lo largo de la simulación, no obstante en la implementación por
 * defecto no se require reescribir ninguno de ellos. El orden de llamada es el siguiente: 
 * constructor->onInit->onStart->onStep (dicho orden no implica que justo después del contructor se llame a onInit pero
 * si de que siempre se llamara antes al constructor que a onInit.
 * 
 * @param behaviour Comportamiento a ejecutar por el robot
 * @param masonAgentFactory Factoria de contrucción de agentes Mason. Solo se necesita proveer cuando se desee 
 * añadir alguna funcionalidad extra al agente o configurar el GUI del agente mostrado en Mason
 * @author fidel
 *
 */
class Robot(val behaviour:Behaviour, masonAgentFactory: MasonAgentFactory = new MasonAgentFactory) extends Interactuator {
  
  /** Agente MASON que lo representa en dicho simulador */
  val masonAgent:MasonAgent = masonAgentFactory.newInstance(this)
  
  /** Id único del robot **/
  var id = 0;
  
  /** Máxima velocidad que puede alcanzar el robot. Por defecto 1m por paso*/
  var maxVel = 1d;
  
  /** Enjambre al que pertence el robot. No tiene valor ni en el constructor ni en onInit, pero si en las 
   * siguientes funciones (onStart, onStep)
   */
  var swarm: Swarm = _
  
  /** Posición del robot */
  var pos:Double2D = new Double2D(0,0)
  
  /** Vector velocidad del robot. El desplazamiento del mismo se consigue alterando este valor */
  var vel: Vector2D = new Vector2D(0,0)
  
  /** Rango del sensor, por defecto 5 metros*/
  var sonarRange: Double = 5
  
  /** Determina si se incluye el robot en el cálculo de colisiones. Por defecto todos se incluyen */
  var robotWillCollide = true
  
  /** Recuento de colisiones de un robot entre robots */
  var collisions_intrarobot = 0
  
  /** Recuento de colisiones de un robot entre el entorno */
  var collisions_environment = 0
  

  
  /**
   * Obtiene el ángulo del robot
   */
  def angle() = scala.math.atan2(vel.y,vel.x);
  
  /**
   * Para construir los objetos necesarios para el robot o los comportamientos. No se debe acceder aún al objeto simulación
   */
  def onInit() = behaviour.init(this)

  /**
   * Justo antes de lanzar onStep. Ya esta disponible el objeto simulacion
   */
  def onStart() = {initInteractuator()}
  
  /**
   * Cada paso del agente ejecuta este comportamiento
   */
  def onStep() = {behaviour.step}
  
  
  /**
   * Determina si el robot ha colisionado o no
   */
  def collision = masonAgent.collision
  
  
  /**
   * Obtiene las lecturas del sonar de ultrasonidos que esten entre los ángulos a1 y a2.
   * Retorna un array con los robots encontrados, la distancia respecto a este robot a la que se encuentran y
   * su angulo
   * 
   */
  def detectOtherRobotsBetween(a1:Double, a2:Double, range:Double):ArrayBuffer[(Robot,Double,Double)]={
    val r = new ArrayBuffer[(Robot,Double,Double)]()
    val sonar = masonAgent.sonarReadingsOnlyForOtherRobots(range)
    for (i <- 0 until sonar.size()) { 
      val l = sonar.objs(i).asInstanceOf[Bag]
      val a = l.objs(2).asInstanceOf[Double]
      if(a>=a1 && a<=a2) r += ((l.get(0).asInstanceOf[Robot],l.get(1).asInstanceOf[Double],l.get(2).asInstanceOf[Double]))
    }
   r
  }
  
  
  /**
   * Determina como dibujar al robot. Sobreescribiendo este método se pueden diseñar representaciones a medida
   */
  def draw(graphics:Graphics2D,info:DrawInfo2D, scale:Double, offset:Double) = {
    
    //Robot
    val draw = info.draw.asInstanceOf[Rectangle2D.Double]
    val width = draw.width*scale + offset
    val height = draw.height*scale + offset
    val x = (draw.x - width / 2.0).toInt
    val y = (draw.y - height / 2.0).toInt
    val w = width.toInt
    val h = height.toInt
    
    //Sonar
    val widths = sonarRange*2*draw.width*scale + offset
    val heights = sonarRange*2*draw.width*scale + offset
    val xs = (draw.x - widths / 2.0).toInt
    val ys = (draw.y - heights / 2.0).toInt
    val ws = widths.toInt
    val hs = widths.toInt
    
    //Dibujo el rango de los sensores
    MasonRenderHelper.drawOval(graphics,xs,ys,ws,hs,Color.LIGHT_GRAY,null);
    
    //Dibujo el robot
    val filledColor = if(collision) Color.CYAN else Color.BLACK
    MasonRenderHelper.drawOval(graphics,x,y,w,h,Color.BLACK,filledColor);
    
  }
  
}