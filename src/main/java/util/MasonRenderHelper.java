package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


public class MasonRenderHelper {
	
	public static void drawOval(Graphics2D graphics, int x, int y, int r1, int r2, Color line, Color fill){

        if(fill!=null){
            graphics.setPaint(fill);
            graphics.fillOval(x, y, r1, r2);
        }

        if(line!=null){
            graphics.setPaint(line);
            graphics.drawOval(x, y, r1, r2);
        }
	}
	
	public static void drawLine(Graphics2D graphics,int ix, int iy,int fx, int fy,Color line){
		graphics.setPaint(line);
		graphics.draw(new Line2D.Double(ix, iy, fx, fy));
	}

}
