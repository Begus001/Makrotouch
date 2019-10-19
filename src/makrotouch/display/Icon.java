package makrotouch.display;

import makrotouch.main.FileManager;

import java.awt.*;

public class Icon {
	private int x, y, width, height, id;
	private Image image;
	private String name;
	private FileManager flmgr = new FileManager();
	private boolean dragging = false;

	public Icon(int x, int y, int width, int height, int id, String name, String image) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		this.id = id;
		this.image = flmgr.loadImage(image);
	}

	public Icon(int id, String name, String image) {
		this.name = name;
		this.id = id;
		this.image = flmgr.loadImage(image);
	}

	public boolean isDragging() {
		return dragging;
	}

	public void setDragging(boolean dragging) {
		this.dragging = dragging;
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

	public Image getIcon() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
