package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
 
public class Shape2Array {
	
	 public static void main(String[] args) {
		 generateCircleOfRadius(10,5,12);
	 }
	
	/** Se genera un array bidimensional de tamaño radius*radius x radius*radius que contiene un circulo donde circleId marca
	 * el valor de los puntos donde esta el circulo y backgroundId donde no esta */
	public static double [][] generateCircleOfRadius(int radius, double circleId, double backgroundId){
		
		int width = radius*2;
		int height= radius*2;
		double data[][] = new double[height][width];
		
		
		//Creo una imagen donde dibujo un circulo blanco sobre fondo negro. Esto lo hago para utilizar las funciones
		//de dibujado de JAVA en vez de crearme las mias.
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
	    Graphics2D g = img.createGraphics();
	    g.setColor(Color.WHITE);
	    g.fillOval(0, 0, width, height);
	    
	    //Accedo al contenido de la imagen (grises) y relleno el array de datos con los valores indicados
	    for (int i=0; i<height; i++){
	    	for (int j=0; j<width; j++){
	    		int  pixel = (img.getRGB(j, i) & 0x00ff0000) >> 16;
	    		if(pixel==0){
	    			data[i][j] = backgroundId;
	    		} else {
	    			data[i][j] = circleId;
	    		}
	    	}
	    }
		
		return data;
		
	}

}
