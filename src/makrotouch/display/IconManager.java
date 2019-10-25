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
	private static final String configPath = "res/config/makrotouch.xml";
	private static int numPages;
	private ArrayList<Icon> icons = new ArrayList<>();
	private ArrayList<int[]> iconPos = new ArrayList<>();
	private int iconPageSize = 8;
	private int page = 0;
	
	private Graphics2D g;
	private Rectangle2D windowBounds;
	private JFrame window;
	
	public IconManager(JFrame window) {
		this.window = window;
		windowBounds = this.window.getContentPane().getBounds();
	}
	
	public static String getConfigPath() {
		return configPath;
	}
	
	public static int getNumPages() {
		return numPages;
	}
	
	public ArrayList<Icon> getIcons() {
		return icons;
	}
	
	public void setIcons(ArrayList<Icon> icons) {
		this.icons = icons;
	}
	
	public ArrayList<int[]> getIconPos() {
		return iconPos;
	}
	
	public void setIconPos(ArrayList<int[]> iconPos) {
		this.iconPos = iconPos;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int geticonPageSize() {
		return iconPageSize;
	}
	
	public void initIcons(int columns, int rows, int margin) throws NullPointerException {
		double iconWidth, iconHeight;
		iconPos.clear();
		icons.clear();
		icons = FileManager.loadIcons(configPath);
		
		iconPageSize = columns * rows;
		numPages = (int) Math.ceil((double) icons.size() / iconPageSize);
		
		iconWidth = windowBounds.getWidth() / columns - (margin + margin / columns);
		iconHeight = windowBounds.getHeight() / rows - (margin + margin / rows);
		
		posInit(columns, rows, margin, iconWidth, iconHeight);
	}
	
	private void posInit(int columns, int rows, int margin, double width, double height) {
		int x, y;
		if (numPages > 1) {
			if (page == 0) {
				initPage0(columns, rows, margin, width, height);
				
			} else if (page + 1 == numPages) {
				initPageMax(columns, rows, margin, width, height);
				
			} else {
				initPage(columns, rows, margin, width, height);
				
			}
		} else {
			for (int i = 0; i < rows; i++)
				for (int k = 0; k < columns; k++) {
					x = (int) (margin + k * (margin + width));
					y = (int) (margin + i * (margin + height));
					iconPos.add(new int[]{x, y});
				}
		}
	}
	
	private void initPage0(int columns, int rows, int margin, double width, double height) {
		int x, y;
		
		//Initialize Positions
		for (int outOfBounds = 0; outOfBounds < 2; outOfBounds++)
			for (int i = 0; i < rows; i++)
				for (int k = 0; k < columns; k++) {
					x = (int) ((margin + k * (margin + width)) + (outOfBounds * windowBounds.getWidth()));
					y = (int) (margin + i * (margin + height));
					iconPos.add(new int[]{x, y});
				}
		
		//Set Icon Positions
		for (int i = 0; i < iconPageSize * 2; i++) {
			icons.get(i).setWidth((int) width);
			icons.get(i).setHeight((int) height);
			
			if (i < iconPos.size()) {
				icons.get(i).setX(iconPos.get(i)[0]);
				icons.get(i).setY(iconPos.get(i)[1]);
				icons.get(i).setVisible(true);
			} else {
				icons.get(i).setVisible(false);
			}
		}
	}
	
	private void initPageMax(int columns, int rows, int margin, double width, double height) {
		int x, y;
		
		//Initialize Positions
		for (int outOfBounds = -1; outOfBounds <= 0; outOfBounds++)
			for (int i = 0; i < rows; i++)
				for (int k = 0; k < columns; k++) {
					x = (int) (margin + k * (margin + width) + outOfBounds * windowBounds.getWidth());
					y = (int) (margin + i * (margin + height));
					iconPos.add(new int[]{x, y});
				}
		
		//Set Icon Positions
		int posCount = 0;
		for (int i = (numPages - 2) * iconPageSize; i < icons.size(); i++) {
			icons.get(i).setWidth((int) width);
			icons.get(i).setHeight((int) height);
			
			icons.get(i).setX(iconPos.get(posCount)[0]);
			icons.get(i).setY(iconPos.get(posCount)[1]);
			icons.get(i).setVisible(true);
			
			posCount++;
		}
	}
	
	private void initPage(int columns, int rows, int margin, double width, double height) {
		int x, y;
		
		//Initialize Positions
		for (int outOfBounds = -1; outOfBounds <= 1; outOfBounds++)
			for (int i = 0; i < rows; i++)
				for (int k = 0; k < columns; k++) {
					x = (int) ((margin + k * (margin + width)) + (outOfBounds * windowBounds.getWidth()));
					y = (int) (margin + i * (margin + height));
					iconPos.add(new int[]{x, y});
				}
		
		//Set Icon Positions
		int posCount = 0;
		for (int i = (page - 1) * iconPageSize; i < (page - 1) * iconPageSize + iconPos.size(); i++) {
			if (i < icons.size()) {
				icons.get(i).setWidth((int) width);
				icons.get(i).setHeight((int) height);
				
				icons.get(i).setX(iconPos.get(posCount)[0]);
				icons.get(i).setY(iconPos.get(posCount)[1]);
				icons.get(i).setVisible(true);
			}
			
			posCount++;
		}
	}
	
	public void drawIcons() {
		g.setColor(Color.white);
		
		//Draw Icons
		for (int i = 0; i < icons.size(); i++) {
			if (icons.get(i).isVisible()) {
				int x = icons.get(i).getX(), y = icons.get(i).getY(), width = icons.get(i).getWidth(), height = icons.get(i).getHeight(), stringWidth =
						g.getFontMetrics().stringWidth(Integer.toString(i));
				
				try {
					g.drawRect(x + (int) (window.getMousePosition().getX() - 512) * 2, y, width, height);
					g.setFont(new Font("Tahoma", Font.PLAIN, 16));
					g.drawString(Integer.toString(i), x + (width / 2) - (stringWidth / 2) + (int) (window.getMousePosition().getX() - 512) * 2, y + (height / 2));
				} catch (NullPointerException e) {
					return;
				}
			}
		}
		
		//Draw Icon Positions
		for (int[] i : iconPos) {
			try {
				g.drawOval(i[0] + (int) (window.getMousePosition().getX() - 512) * 2, i[1], 50, 50);
			} catch (NullPointerException e) {
				return;
			}
		}
	}
	
	public void clear() {
		windowBounds = window.getContentPane().getBounds();
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
	
	public void setGraphics(Graphics2D g) {
		this.g = g;
	}
}
