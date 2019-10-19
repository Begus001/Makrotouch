package makrotouch.main;

import makrotouch.display.IconManager;
import makrotouch.display.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Main implements Runnable {

	private static final String configPath = "res/config/makrotouch.xml";
	private Graphics2D g;
	private BufferStrategy bs;
	private Thread thread;
	private Window window;
	private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
	private static IconManager icnmgr;

	private boolean running = false;
	private int programState = 0;

	public Main() {
		window = new Window("Makrotouch", 1024, 600, true, false);
		icnmgr = new IconManager(window);
	}

	public static String getConfigPath() {
		return configPath;
	}

	public void run() {
		while (running) {
			render();
//			render();
			tick();
//			return;
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
			case 0:
				try {
					icnmgr.clear();
					icnmgr.initIcons(4, 2, 75);
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

	public void tick() {

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
