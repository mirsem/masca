package simulator

import scala.collection.mutable.ArrayBuffer
import sim.util.Double2D

/**
 * Enjambre formado por robots. Se pueden añadir los robots que se deseen utilizando el operador +=
 * @param robotInitiallizer Función que inicializa el enjambre, de manera que por ejemplo coloque los robots
 * en cada simulación en una determinada posición. La función recibe dos parámetros: un arrayBuffer que contiene
 * los robots y una instancia de la simulación. Por defecto se implementa una distribución aleatoria de los robots
 * por el entorno
 * 
 * @author fidel
 *
 */
class Swarm (robotInitiallizer:(ArrayBuffer[Robot],Simulation)=>Unit=
								(a,s)=>{a.map(r => {r.onInit; r.pos = new Double2D(s.randDouble*s.width,s.randDouble*s.height)})}) {
  
  // robotInitiallizer es una funcion que no devuelve nada (Unit-void)
  // a.map(r => {r.onInit; r.pos = new Double2D(s.randDouble*s.width,s.randDouble*s.height)})
  // a es un arraybuffer de robots al que se le pasa una funcion por parametro para modificar su estado
  // para cada robot r, se inicializa y se calcula su posicion de manera aleatoria en funcion de las dimensiones de entorno de simulacion
  
    
  /** Array de robots*/
  val robots = ArrayBuffer[Robot]()
  /** Simulador al que está vinculado en enjambre. Solo se garantiza que exista dentro del método initRobots*/
  var sim: Simulation = _
  
  def initRobots = {
    if(robotInitiallizer!=null) robotInitiallizer(robots,sim)
    robots.map(r => {
       r.onInit
    })
  };
  
  /**
   * Permite añadir un robot al enjambre
   */
  def +=(robot:Robot) = {
    robot.swarm = this
    robots += robot
    robot.id = robots.length-1
  }
  

}