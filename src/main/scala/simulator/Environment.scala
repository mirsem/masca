package simulator

import sim.display.Display2D


/**
 * Entorno de simulación. Un entorno posibilita agregar funcionalidades a la simulación por defecto (agregar nuevos mapas,
 * nuevos recursos...). Habitualmente un Entorno lleva aparejado un Interactuador que proporciona funciones al robot para 
 * explorar dicho entorno
 * @author fidel
 *
 */
trait Environment {
  
  /** Los objetos que implementen este entorno deben tener una funcion que retorne la simulación actual */
  def sim():Simulation
  
  /** Dentro de esta función se deben construir los fields de mason que se deseen agregar (o cualquier otro objeto)*/
  def initFields() = {}
  /** En este método Se deben construir los visualizadores de los fields definids en initFields */
  def initPortrayals() = {}
  /** Se deben vincular los fields con sus visualizadores en este método*/
  def setupPortrayals() = {}
  /** Se deben agregar los visualizadores a display en este método*/
  def setupDisplay(display:Display2D) = {}
  
  /**Si se requiere, codigo a ejecutar en cada paso del enjambre, se ejecuta justo después del onStep de MasonSwarm*/
  def onSwarmStep()={};
  /**Si se requiere, codigo a ejectuar en cada paso del robot. Se ejecura despues de robot.onStep*/
  def onRobotStep(robot:Robot)={}
  /**Si el entorno se debe tener en cuenta en las colisiones, devuelve true o false si hay colisión en la 
   * posición que se encuentre el robot después de aplicar su velocidad */
  def isCollision(robot:Robot):Boolean={false}
}