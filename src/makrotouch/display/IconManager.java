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

public class IconManager {
	private final String configPath = "res/config/makrotouch.xml";

	private int numPages;
	private int iconPageSize = 8;
	private int page = 0;
	private boolean connected = false;
	private long start;
	private boolean animate = false;

	private ArrayList<Icon> icons = new ArrayList<>();
	private ArrayList<int[]> iconPos = new ArrayList<>();
	private Graphics2D g;
	private Rectangle2D windowBounds;
	private JFrame window;

	public IconManager(JFrame window) {
		this.window = window;
		windowBounds = this.window.getContentPane().getBounds();
	}

	public boolean isAnimate() {
		return animate;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
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

	public void init() {
		init(4, 2, 75);
	}

	public void init(int columns, int rows, int margin) {
		start = System.nanoTime();

		g.setColor(Color.white);
		g.setFont(new Font("Tahoma", Font.PLAIN, 25));
		g.drawString("Loading Icons...", (int) (windowBounds.getWidth() / 2 - g.getFontMetrics().stringWidth("Loading Icons...") / 2), 585);

		Main.getBs().show();

		double iconWidth, iconHeight;
		iconPos.clear();
		icons.clear();
		icons = FileManager.loadIcons(configPath);
		sortIcons();

		iconPageSize = columns * rows;
		if (icons != null) {
			numPages = (int) Math.ceil((double) icons.size() / iconPageSize);
		}

		iconWidth = windowBounds.getWidth() / columns - (margin + (double) margin / columns);
		iconHeight = windowBounds.getHeight() / rows - (margin + (double) margin / rows);

		int x, y;
		//Initialize Positions
		for (int i = 0; i < rows; i++)
			for (int k = 0; k < columns; k++) {
				x = (int) (margin + k * (margin + iconWidth));
				y = (int) (margin + i * (margin + iconHeight));
				iconPos.add(new int[]{x, y});
			}

		for (int i = 0; i < icons.size(); i++) {
			icons.get(i).setWidth((int) iconWidth);
			icons.get(i).setHeight((int) iconHeight);
			icons.get(i).setVisible(true);
		}

		Main.reportDrawDuration(System.nanoTime() - start);
	}

	private void sortIcons() {
		Collections.sort(icons);
	}

	public void drawIcons() {

		start = System.nanoTime();

		if (g == null)
			return;
		g.setColor(Color.white);

		int posCount = 0;
		//Draw Icons
		for (int i = page * iconPageSize; i < (page * iconPageSize) + (iconPageSize); i++) {
			try {
				if (icons.get(i).isVisible()) {


					if (!animate) {
						icons.get(i).setX(iconPos.get(posCount)[0]);
						icons.get(i).setY(iconPos.get(posCount)[1]);
					}
					posCount++;

					//if (icons.get(i).getX() + offset < 1024 && icons.get(i).getX() + icons.get(i).getWidth() + offset > 0) {

					int id = icons.get(i).getId(), x = icons.get(i).getX(), y = icons.get(i).getY(), width = icons.get(i).getWidth(), height =
							icons.get(i).getHeight(), idStringWidth = g.getFontMetrics().stringWidth(Integer.toString(id));

					String name = icons.get(i).getName();

					Image image = icons.get(i).getImage();

					try {

						g.setStroke(new BasicStroke(2)); //Set stroke
						byte fontSize = 16;
						g.setFont(new Font("Tahoma", Font.PLAIN, fontSize)); //Set font

						double aspect;

						if (image != null && name != null) {

							aspect = (double) image.getHeight(null) / (double) image.getWidth(null);
							if (width * aspect < height - 5) {
								g.drawImage(image, x, (int) (y + height / 2 - (width * aspect) / 2), width, (int) (width * aspect),
										null); //Draw Images
							} else {
								g.drawImage(image, (int) (x + width / 2 - (height / aspect - 20) / 2), (int) (y + height / 2 - (width) / 2),
										(int) (height / aspect) - 20,
										(int) height - 20,
										null); //Draw Images
							}
							g.drawString(name, x + width / 2 - g.getFontMetrics().stringWidth(name) / 2, y + height + 15); //Draw Names

						} else if (name != null) {

							g.drawString(name, x + width / 2 - g.getFontMetrics().stringWidth(name) / 2, y + height / 2 + fontSize / 2); //Draw Names
							g.drawRoundRect(x, y, width, height, 15, 15); //Draw Icon Borders

						} else {

							aspect = (double) image.getHeight(null) / (double) image.getWidth(null);
							g.drawImage(image, x, (int) (y + height / 2 - (width * aspect) / 2), width, (int) (width * aspect), null); //Draw Images

						}


					} catch (NullPointerException e) {
						System.out.println("Couldn't draw icon");
						return;
					}
					//}
				}
			} catch (IndexOutOfBoundsException e) {

			}
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

		drawStaticObjects();


	}

	private void drawStaticObjects() {
		g.setFont(new Font("Tahoma", Font.PLAIN, 25));

		if (numPages > 0) {
			g.drawString((page + 1) + "/" + numPages, 10, 27); //Draw Page Number
		} else {
			setPage(-1);
			g.drawString("1/1", 10, 27); //Draw Page Number
		}

		//Draw WiFi icon
		if (connected) {
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

		//Draw connected
		if (Main.getConnection().isConnected()) {
			g.setFont(new Font("Tahoma", Font.PLAIN, 20));
			g.setColor(Color.GREEN);
			g.drawString("Connected", 1024 / 2 - g.getFontMetrics().stringWidth("Connected") / 2, 25);
			g.setColor(Color.WHITE);
		}

		Main.reportDrawDuration(System.nanoTime() - start);
	}

	public void clear() {
		if (g == null)
			return;
		windowBounds = window.getContentPane().getBounds();
		g.setColor(Color.black);
		g.fillRect(0, 0, (int) windowBounds.getWidth(), (int) windowBounds.getHeight());
	}

	public void setGraphics(Graphics2D g) {
		this.g = g;
	}
}
