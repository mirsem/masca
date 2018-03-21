package simulator.mason

import sim.portrayal._
import java.awt.Paint
import java.awt.geom.Ellipse2D
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D


/**
 * Renderizador de objetos robot para un SimplePortrayal2D
 * @author fidel
 *
 */
class MasonPortrayal2DRender(val paint:Paint, val scale:Double = 0d, val filled:Boolean) extends SimplePortrayal2D {
  
  var offset = 0.0d
  @transient var preciseEllipse = new Ellipse2D.Double();

  
  /**
   * Función que dibuja los robots o objetos del entorno portrayal2D
   */
  override def draw(o:Object,graphics:Graphics2D, info:DrawInfo2D):Unit = {
    
    val draw = info.draw.asInstanceOf[Rectangle2D.Double]
    val width = draw.width*scale + offset;
    val height = draw.height*scale + offset;
    
    graphics.setPaint(paint)
    
    if(info.precise) {
      if (preciseEllipse == null) preciseEllipse = new Ellipse2D.Double();    // could get reset because it's transient
      preciseEllipse.setFrame(info.draw.x - width/2.0, info.draw.y - height/2.0, width, height);
      if (filled) graphics.fill(preciseEllipse);
      else graphics.draw(preciseEllipse);
      return
    }

    
    o match {
      case agent: MasonAgent => agent.robot.draw(graphics, info, scale, offset)
      case _ => {println("MasonPortRayal2DRender: No se como dibujar un objecto, ignorándolo")}
    }
    
  }
  
  /**
   * Detecta colisiones.
   */
  override def hitObject(o:Object,range:DrawInfo2D) = {
    
    if(preciseEllipse == null) preciseEllipse = new Ellipse2D.Double()
    
    val SLOP = 1.0d;  // need a little extra area to hit objects
    val width = range.draw.width*scale;
    val height = range.draw.height*scale;
    
    preciseEllipse.setFrame( range.draw.x-width/2-SLOP, range.draw.y-height/2-SLOP, width+SLOP*2,height+SLOP*2 )
    preciseEllipse.intersects( range.clip.x, range.clip.y, range.clip.width, range.clip.height )
    
  }
  
}