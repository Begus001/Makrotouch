package makrotouch.display;

import makrotouch.main.FileManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class IconManager {
	private Graphics2D g;
	private Rectangle2D windowBounds;
	private ArrayList<Icon> icons = new ArrayList<>();
	private ArrayList<int[]> iconPos = new ArrayList<>();
	private FileManager flmgr = new FileManager();

	public IconManager(Graphics g, JFrame window) {
		this.g = (Graphics2D) g;
		windowBounds = window.getContentPane().getBounds();
	}

	public void initIcons(int columns, int rows, int margin) throws NullPointerException {
		int x, y, width, height;

		width = (int) ((windowBounds.getWidth() / columns) - (margin + (margin / columns)));
		height = (int) ((windowBounds.getHeight() / rows) - (margin + (margin / rows)));

		iconPos.clear();
		icons.clear();
		icons = flmgr.loadIcons();
		if (icons == null) {
			return;
		}

		for (int i = 0; i < icons.size(); i++) {
			icons.get(i).setWidth(width);
			icons.get(i).setHeight(height);
		}

		for (int i = 0; i < rows; i++) {
			for (int k = 0; k < columns; k++) {
				x = ((width + margin) * k) + margin;
				y = ((height + margin) * i) + margin;
				int[] pos = {x, y};
				iconPos.add(pos);
			}
		}

		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < icons.size(); k++) {
				if (icons.get(k).getId() == i) {
					icons.get(k).setX(iconPos.get(i)[0]);
					icons.get(k).setY(iconPos.get(i)[1]);
					icons.get(k).setImage(flmgr.loadImage("res/icons/" + k + ".png"));
				}
			}
		}
	}

	public void drawIcons() {
		for (int i = 0; i < icons.size(); i++) {
			Icon current = icons.get(i);

			g.setColor(Color.white);
			g.setStroke(new BasicStroke(2));

			g.setFont(new Font("Arial", Font.BOLD, 20));
			int width = g.getFontMetrics().stringWidth(current.getName());

			g.drawRoundRect(current.getX(), current.getY(), current.getWidth(), current.getHeight(), 20, 20);
			g.drawString(current.getName(), current.getX() + current.getWidth() / 2 - width / 2, current.getY() + current.getHeight() + 25);

			Image icon = current.getIcon();

			g.drawImage(icon, current.getX(), current.getY(), current.getWidth(), current.getHeight(), null);
		}
	}

	public void clear() {
		g.setColor(Color.black);
		g.fillRect(0, 0, (int) windowBounds.getWidth(), (int) windowBounds.getHeight());
	}

	public BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(IconManager.class.getResource(path));
		} catch (IOException e) {
			return null;
		}
	}

	public void drawImage(BufferedImage image, int x, int y, int width, int height) {
		g.drawImage(image, x, y, width, height, null);
	}
}
