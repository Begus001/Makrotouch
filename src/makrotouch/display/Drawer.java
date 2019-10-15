package makrotouch.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Drawer {
	private Graphics2D g;
	private Rectangle2D windowBounds;

	public Drawer(Graphics g, JFrame window) {
		this.g = (Graphics2D) g;
		windowBounds = window.getContentPane().getBounds();

	}

	public void initIcons(int columns, int rows, int margin) {
		int width, height;

		width = (int) ((windowBounds.getWidth()) / columns - (margin + margin / columns));
		height = (int) ((windowBounds.getHeight()) / rows - (margin + margin / rows));

		System.out.println(windowBounds.getWidth() + " x " + windowBounds.getHeight());
		
		g.setColor(Color.black);

		for (int i = 0; i < columns; i++) {
			for (int k = 0; k < rows; k++) {
				g.setStroke(new BasicStroke(3));
				g.drawRoundRect((width + margin) * i + margin, (height + margin) * k + margin, width, height, 10, 10);
			}
		}
		
		g.setColor(Color.red);
		g.fillRect(0, 0, (int) windowBounds.getWidth(), margin);
		g.fillRect(0, (int) windowBounds.getHeight() - margin, (int) windowBounds.getWidth(), margin);
	}

	public void clear() {
		g.clearRect(0, 0, (int) windowBounds.getWidth(), (int) windowBounds.getHeight());
	}

	public BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Drawer.class.getResource(path));
		} catch (IOException e) {
			return null;
		}
	}

	public void drawImage(BufferedImage image, int x, int y, int width, int height) {
		g.drawImage(image, x, y, width, height, null);
	}
}
