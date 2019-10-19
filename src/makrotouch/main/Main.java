package makrotouch.main;

import makrotouch.display.IconManager;
import makrotouch.display.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Main implements Runnable {

	private static final String configPath = "res/config/makrotouch.xml";
	private static IconManager icnmgr;
	private static int programState = 0;
	private Graphics2D g;
	private BufferStrategy bs;
	private Thread thread;
	private Window window;
	private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
	private boolean running = false;

	public Main() {
		window = new Window("Makrotouch");
		icnmgr = new IconManager(window);
	}

	public static int getProgramState() {
		return programState;
	}

	public static void setProgramState(int programState) {
		Main.programState = programState;
	}

	public static String getConfigPath() {
		return configPath;
	}

	public static IconManager getIcnmgr() {
		return icnmgr;
	}

	public void run() {
		while (running) {
			tick();
			render();
//			render();
//			return;
		}
	}

	public void tick() {
		switch (programState) {
			case 0:
				icnmgr.initIcons(4, 2, 75);
				break;
			case 1:
				icnmgr.initIcons(4, 2, 75);
				IconManager.getIcons().get(0).setX((int) MouseInfo.getPointerInfo().getLocation().getX());
				IconManager.getIcons().get(0).setY((int) MouseInfo.getPointerInfo().getLocation().getY());
				break;
		}
	}

	public void render() {
		bs = window.getCanvas().getBufferStrategy();

		if (bs == null) {
			window.getCanvas().createBufferStrategy(3);
			return;
		}

		g = (Graphics2D) bs.getDrawGraphics();
		icnmgr.setG(g);

		switch (programState) {
			case 1:
			case 0:
				try {
					icnmgr.clear();
					icnmgr.drawIcons();
				} catch (NullPointerException e) {
					g.dispose();
					bs.show();
					return;
				}
				break;
			default:
				break;
		}

		g.dispose();
		bs.show();
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			stop();
		}
	}
}
