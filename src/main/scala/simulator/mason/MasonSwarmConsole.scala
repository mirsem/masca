package simulator.mason

import scala.beans.BeanProperty
import simulator.Simulation


/**
 * Este trair permite visualizar en el GUI de MASON cualquier propiedad definiendo métodos getX() y setX(). Con estos métodos
 * se mostraria la propiedad X en el GUI del simulador
 * @author fidel
 *
 */
trait MasonSwarmConsole {
    /**
     * Método a implementar para usar la interfaz. Debe retornar la simulación actual
     */
    def sim():Simulation
    
    /**
     * A partir de aqui se puede derivar esta clase y definir métodos get y set para que el GUI de MASON muestre 
     * las propiedades en la consola por ejemplo: def getNumRobots():Int = sim.swarm.robots.length
     */
    
}