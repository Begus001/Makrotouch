package makrotouch.display;

import makrotouch.main.FileManager;
import makrotouch.main.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IconManager {
	private final String configPath = "res/config/makrotouch.xml";
	
	private int numPages;
	private int iconPageSize = 8;
	private int page = 0;
	private boolean connected = false;
	
	private ArrayList<Icon> icons = new ArrayList<>();
	private ArrayList<int[]> iconPos = new ArrayList<>();
	private Graphics2D g;
	private Rectangle2D windowBounds;
	private JFrame window;
	
	public IconManager(JFrame window) {
		this.window = window;
		windowBounds = this.window.getContentPane().getBounds();
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public String getConfigPath() {
		return configPath;
	}
	
	public int getNumPages() {
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
		long start = System.nanoTime();
		
		double iconWidth, iconHeight;
		iconPos.clear();
		icons.clear();
		icons = FileManager.loadIcons(configPath);
		sortIcons();
		
		iconPageSize = columns * rows;
		if(icons != null) {
			numPages = (int) Math.ceil((double) icons.size() / iconPageSize);
		}
		
		iconWidth = windowBounds.getWidth() / columns - (margin + (double) margin / columns);
		iconHeight = windowBounds.getHeight() / rows - (margin + (double) margin / rows);
		
		posInit(columns, rows, margin, iconWidth, iconHeight);
		
		Main.reportDrawDuration(System.nanoTime() - start);
	}
	
	private void sortIcons() {
		Collections.sort(icons);
	}
	
	private void posInit(int columns, int rows, int margin, double width, double height) {
		int x, y;
		if(numPages > 1) {
			if(page == 0) {
				initPage0(columns, rows, margin, width, height);
				
			} else if(page + 1 == numPages) {
				initPageMax(columns, rows, margin, width, height);
				
			} else {
				initPage(columns, rows, margin, width, height);
				
			}
		} else {
			
			//Initialize Positions
			for(int i = 0; i < rows; i++)
				for(int k = 0; k < columns; k++) {
					x = (int) (margin + k * (margin + width));
					y = (int) (margin + i * (margin + height));
					iconPos.add(new int[]{x, y});
				}
			
			//Set Icon Positions
			for(int i = 0; i < icons.size(); i++) {
				icons.get(i).setWidth((int) width);
				icons.get(i).setHeight((int) height);
				icons.get(i).setX(iconPos.get(i)[0]);
				icons.get(i).setY(iconPos.get(i)[1]);
				icons.get(i).setVisible(true);
			}
		}
	}
	
	private void initPage0(int columns, int rows, int margin, double width, double height) {
		int x, y;
		
		//Initialize Positions
			for(int i = 0; i < rows; i++)
				for(int k = 0; k < columns; k++) {
					x = (int) (margin + k * (margin + width));
					y = (int) (margin + i * (margin + height));
					iconPos.add(new int[]{x, y});
				}
		
		//Set Icon Positions
		for(int i = 0; i < iconPageSize; i++) {
			if(i < icons.size()) {
				icons.get(i).setWidth((int) width);
				icons.get(i).setHeight((int) height);
				
				icons.get(i).setX(iconPos.get(i)[0]);
				icons.get(i).setY(iconPos.get(i)[1]);
				
				icons.get(i).setVisible(true);
			}
		}
	}
	
	private void initPageMax(int columns, int rows, int margin, double width, double height) {
		int x, y;
		
		//Initialize Positions
			for(int i = 0; i < rows; i++)
				for(int k = 0; k < columns; k++) {
					x = (int) (margin + k * (margin + width));
					y = (int) (margin + i * (margin + height));
					iconPos.add(new int[]{x, y});
				}
		
		//Set Icon Positions
		int posCount = 0;
		for(int i = (numPages - 1) * iconPageSize; i < icons.size(); i++) {
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
			for(int i = 0; i < rows; i++)
				for(int k = 0; k < columns; k++) {
					x = (int) ((margin + k * (margin + width)));
					y = (int) (margin + i * (margin + height));
					iconPos.add(new int[]{x, y});
				}
		
		//Set Icon Positions
		int posCount = 0;
		for(int i = page * iconPageSize; i < page * iconPageSize * 2; i++) {
			if(i < icons.size()) {
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
		long start = System.nanoTime();
		
		if(g == null)
			return;
		g.setColor(Color.white);
		
		//Draw Icons
		for(int i = 0; i < icons.size(); i++) {
			
			if(icons.get(i).isVisible()) {
				
				//if (icons.get(i).getX() + offset < 1024 && icons.get(i).getX() + icons.get(i).getWidth() + offset > 0) {
				
				int id = icons.get(i).getId(), x = icons.get(i).getX(), y = icons.get(i).getY(), width = icons.get(i).getWidth(), height = icons.get(i).getHeight(), idStringWidth =
						g.getFontMetrics().stringWidth(Integer.toString(id));
				
				String name = icons.get(i).getName();
				
				Image image = icons.get(i).getImage();
				
				try {
					
					g.setStroke(new BasicStroke(2)); //Set stroke
					byte fontSize = 16;
					g.setFont(new Font("Tahoma", Font.PLAIN, fontSize)); //Set font
					
					double aspect;
					
					if(image != null && name != null) {
						
						aspect = (double) image.getHeight(null) / (double) image.getWidth(null);
						g.drawImage(image, x, (int) (y + height / 2 - (width * aspect) / 2), width, (int) (width * aspect), null); //Draw Images
						g.drawString(name, x + width / 2 - g.getFontMetrics().stringWidth(name) / 2, y + height + 15); //Draw Names
						
					} else if(name != null) {
						
						g.drawString(name, x + width / 2 - g.getFontMetrics().stringWidth(name) / 2, y + height / 2 + fontSize / 2); //Draw Names
						g.drawRoundRect(x, y, width, height, 15, 15); //Draw Icon Borders
						
					} else {
						
						aspect = (double) image.getHeight(null) / (double) image.getWidth(null);
						g.drawImage(image, x, (int) (y + height / 2 - (width * aspect) / 2), width, (int) (width * aspect), null); //Draw Images
						
					}
					
					
				} catch(NullPointerException e) {
					System.out.println("Couldn't draw icon");
					return;
				}
				//}
			}
			
			g.setFont(new Font("Tahoma", Font.PLAIN, 25));
			g.drawString((page + 1) + "/" + numPages, 10, 27); //Draw Page Number
			
			//Draw WiFi icon
			if(connected) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.WHITE);
			}
			g.setStroke(new BasicStroke(5));
			g.drawLine(977, 15, 1009, 15);
			g.drawLine(977, 28, 1009, 28);
			g.drawLine(977, 41, 1009, 41);
			
			g.setColor(Color.WHITE);
			
			//Draw page switchers
			g.fillPolygon(new int[]{1010, 970, 970, 1010}, new int[]{300, 400, 200, 300}, 4);
			g.fillPolygon(new int[]{14, 54, 54, 14}, new int[]{300, 400, 200, 300}, 4);
			
			Main.reportDrawDuration(System.nanoTime() - start);
		}
		
		//Draw Icon Positions
		/*
		for (int[] i : iconPos) {
			try {
				g.drawOval(i[0], i[1], 50, 50);
			} catch (NullPointerException e) {
				return;
			}
		}
		
		 */
		
		
	}
	
	private String getLocalIP() {
		String command;
		if(System.getProperty("os.name").equals("Linux"))
			command = "ifconfig";
		else
			command = "ipconfig";
		Runtime r = Runtime.getRuntime();
		Process p = null;
		try {
			p = r.exec(command);
		} catch(IOException e) {
			System.out.println("Couldn't get local IP");
		}
		Scanner s = new Scanner(p.getInputStream());
		
		StringBuilder sb = new StringBuilder("");
		while(s.hasNext())
			sb.append(s.next());
		String ipconfig = sb.toString();
		Pattern pt = Pattern.compile("192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3}");
		Matcher mt = pt.matcher(ipconfig);
		mt.find();
		return mt.group();
	}
	
	public void clear() {
		if(g == null)
			return;
		windowBounds = window.getContentPane().getBounds();
		g.setColor(Color.black);
		g.fillRect(0, 0, (int) windowBounds.getWidth(), (int) windowBounds.getHeight());
	}
	
	public BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(IconManager.class.getResource(path));
		} catch(IOException e) {
			return null;
		}
	}
	
	public void printString(String string, float x, float y, int size) {
		if(g == null)
			return;
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, size));
		g.drawString(string, x, y);
	}
	
	public void setGraphics(Graphics2D g) {
		this.g = g;
	}
}
