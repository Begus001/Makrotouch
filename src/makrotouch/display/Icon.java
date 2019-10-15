package makrotouch.display;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Icon {
	private int x, y, width, height;
	private BufferedImage image;
	private String name;

	public Icon(int x, int y, String image, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.image = loadImage(image);
	}

	private BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Drawer.class.getResource(path));
		} catch (IOException e) {
			return null;
		}
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public BufferedImage getIcon() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
