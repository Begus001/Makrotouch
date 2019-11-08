package makrotouch.display;

import makrotouch.main.FileManager;

import java.awt.*;

public class Icon implements Comparable<Icon> {
	private int x, y, width, height, id;
	private Image image;
	private String name;
	private boolean visible = false;
	
	public Icon(int x, int y, int width, int height, int id, boolean visible, String name, String image) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		this.id = id;
		this.visible = visible;
		this.image = FileManager.loadImage(image);
	}
	
	public Icon(int id, String name, String image) {
		this.name = name;
		this.id = id;
		this.image = FileManager.loadImage(image);
		System.out.println("icon loaded image " + image);
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
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
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	@Override
	public int compareTo(Icon icon) {
		return Integer.compare(this.id, icon.getId());
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
