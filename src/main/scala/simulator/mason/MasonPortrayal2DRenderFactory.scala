package simulator.mason

import java.awt.Paint

/**
 * Factoria de construcción de objetos  MasonPortrayal2DRender
 */
class MasonPortrayal2DRenderFactory {
  def newInstance(paint:Paint, scale:Double = 0d, filled:Boolean) = new MasonPortrayal2DRender(paint,scale,filled)
}