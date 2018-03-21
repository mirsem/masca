package simulator.mason

//import scala.reflect.BeanProperty
import simulator.Robot

/**
 * Este trair permite visualizar en el GUI de MASON (parte del agente, haciendo sobre click sobre cualquier robot)
 * cualquier propiedad definiendo métodos getX() y setX(). Con estos métodos se mostraria la propiedad X en el GUI del simulador
 * @author fidel
 *
 */
trait MasonAgentConsole {
    /**
     * Método a implementar para usar la interfaz. Debe retornar el robot actual
     */
    def robot():Robot
    
    /**
     * A partir de aqui se puede derivar esta clase y definir métodos get y set para que el GUI de MASON muestre 
     * las propiedades en la consola por ejemplo: def getNumRobots():Int = sim.swarm.robots.length
     */
}