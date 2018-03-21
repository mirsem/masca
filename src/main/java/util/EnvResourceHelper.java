package util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sim.field.grid.DoubleGrid2D;

public class EnvResourceHelper {
	
	public int width;
	public int height;
	
	//Genera un doublegrid2d a partir de una imagen
    public DoubleGrid2D groundFromImage(String url){


      //Cargo la Imagen que rellenará el DDG2D
      BufferedImage img = null;
      try {
        img = ImageIO.read(new File(url));

      } catch (IOException e) {
          System.err.println("Error: No se encontró la imagen "+url);
      }

      //El tamaño del entorno lo determina la imagen
      width = img.getWidth();
      height = img.getHeight();

      //Creo el DoubleGrid2D a utilizar
      DoubleGrid2D dg = new DoubleGrid2D((int)width,(int)height,0);

      //Si la imagen se ha cargado relleno el grid
      if(img!=null){

          //Si no tiene el tamaño de dg entonces la escalo para que lo tenga
          if(img.getWidth()!=width && img.getHeight()!=height){
               AffineTransform tx = new AffineTransform();
               tx.scale(width/img.getWidth(), height/img.getHeight());
               AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
               img = op.filter(img, null);
          }

          int pixel, rpixel;
          //Recorro la imagen y dispongo los pixels en el grid
          for(int y=0;y<height;y++){
              for(int x=0;x<width; x++){
                  pixel = img.getRGB(x, y);
                  //Me quedo con el canal R
                  rpixel = (pixel >>> 16) & 0xFF;
                  dg.set(x, y, rpixel/255f);
              }
          }

      }

      return dg;
    }
    
  /*Actualiza un DoubleGrid2D a partir de una imagen. ¡El DoubleGrid2D debe ser creado para que sea actualizable (parámetro del constructor)! y debe
    tener el mismo tamaño que la imagen*/
    public void updateGroundFromImage(DoubleGrid2D dg, String url){


      //Cargo la Imagen que rellenará el DDG2D
      BufferedImage img = null;
      try {
        img = ImageIO.read(new File(url));

      } catch (IOException e) {
          System.err.println("Error: No se encontró la imagen "+url);
      }

      //El tamaño del entorno lo determina la imagen
      width = img.getWidth();
      height = img.getHeight();

      //Verifico el tamaño del DoubleGrid2D a utilizar
      if(width!= dg.getWidth()||height!=dg.getHeight()){
    	  System.err.println("Error, en util.EnvResourceHelper.updateGroundFromImage: el tamaño del DoubleGrid2D y la imagen no coinciden");
    	  System.exit(1);
      }

      //Si la imagen se ha cargado relleno el grid
      if(img!=null){

          //Si no tiene el tamaño de dg entonces la escalo para que lo tenga
          if(img.getWidth()!=width && img.getHeight()!=height){
               AffineTransform tx = new AffineTransform();
               tx.scale(width/img.getWidth(), height/img.getHeight());
               AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
               img = op.filter(img, null);
          }

          int pixel, rpixel;
          //Recorro la imagen y dispongo los pixels en el grid
          for(int y=0;y<height;y++){
              for(int x=0;x<width; x++){
                  pixel = img.getRGB(x, y);
                  //Me quedo con el canal R
                  rpixel = (pixel >>> 16) & 0xFF;
                  dg.set(x, y, rpixel/255f);
              }
          }

      }

    }
    

}
