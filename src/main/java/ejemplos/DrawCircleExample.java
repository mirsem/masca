package ejemplos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class DrawCircleExample extends Canvas {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 320;
    private static final int HEIGHT = 320;

    public static void main(String[] args) {
        JFrame f = new JFrame("Draw circle example");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new DrawCircleExample());
        f.pack();
        f.setVisible(true);
    }

    private final BufferedImage img;

    public DrawCircleExample() {
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillOval(8, 8, 140, 140);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(),img.getHeight());
    }
}