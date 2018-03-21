package simulator.mason

import sim.display._
import javax.swing.JFrame
import sim.portrayal.continuous.ContinuousPortrayal2D
import simulator.Simulation
import sim.engine.SimState
import sim.portrayal.simple.OrientedPortrayal2D
import sim.portrayal.simple.OvalPortrayal2D
import java.awt.Color
import simulator.Environment
import sim.portrayal.grid.FastValueGridPortrayal2D


/**
 * Clase que implementar el interfaz gráfico necesario para que MASON cree el GUI de un SimState
 */
class MasonGUI(val sim:Simulation, val ms:MasonSwarm with Environment, 
    val mkRender:MasonPortrayal2DRenderFactory) extends GUIState(ms)  {
  
  val display = new Display2D(500, 500, MasonGUI.this);
  val displayFrame: JFrame = display.createFrame();
  val agentsPortrayal = new ContinuousPortrayal2D();
  
  //Y inicio los portrayals del entorno
   ms.initPortrayals
  
  override def getSimulationInspectedObject() = state
  
  override def start() = {
    super.start()
    setupPortrayals();
  }
  
  override def load(state:SimState) = {
    super.load(state)
    setupPortrayals();
  }
  
  def setupPortrayals() = {
    state match {
      case s:MasonSwarm => {
        
        ms.setupPortrayals
        
        //Ajusto el portrayal de los agentes
        agentsPortrayal.setField(s.agentField);
        //agentsPortrayal.setPortrayalForAll(new OrientedPortrayal2D(new OvalPortrayal2D(1d,true)));
        val render = mkRender.newInstance(Color.gray,1.0,true)
        agentsPortrayal.setPortrayalForAll(new OrientedPortrayal2D(render,0,1d));
        

        // reschedule the displayer
        display.reset();
        display.setBackdrop(Color.white);

        // redraw the display
        display.repaint();
        
      }
      case _ => {println("Error ar MasonUI: setupPortrayals "); sys.exit(0)};
    }

  }
  
  
  override def init(c:Controller) = {
    super.init(c)
    display.setClipping(false);
    
    displayFrame.setTitle("Swarm Simulator");
    c.registerFrame(displayFrame);   // register the frame so it appears in the "Display" list
    displayFrame.setVisible(true);
    
    ms.setupDisplay(display)
    display.attach(agentsPortrayal, "Robots");
    
    
  }
  
  
  override def quit() = {
    super.quit()
    if (displayFrame != null) {
      displayFrame.dispose();
    }
  }
  

}

object MasonGUI {
  def getName() = "Mason SCALA"
}