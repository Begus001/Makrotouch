package makrotouch.main;

import makrotouch.display.IconManager;
import makrotouch.display.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Main implements Runnable {
	
	private static int programState = 0;
	private static IconManager icnmgr;
	private Graphics2D g;
	private BufferStrategy bs;
	private Thread thread;
	private Window window;
	private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
	private boolean running = false;
	
	public Main() {
		window = new Window("Makrotouch", 1024, 600, false, false);
		
		icnmgr = new IconManager(window);
	}
	
	public static int getProgramState() {
		return programState;
	}
	
	public static void setProgramState(int programState) {
		Main.programState = programState;
	}
	
	public static IconManager getIcnmgr() {
		return icnmgr;
	}
	
	public void run() {
		init();
		while (running) {
			tick();
			render();
			//return;
		}
	}
	
	private void init() {
		icnmgr.initIcons(4, 2, 75);
	}
	
	private void tick() {
	
	}
	
	private void render() {
		initRender();
		if (g == null) return;
		
		try {
			icnmgr.clear();
			//icnmgr.initIcons(4, 2, 75);
			icnmgr.drawIcons();
		} catch (NullPointerException e) { e.printStackTrace(); }
		
		g.dispose();
		bs.show();
	}
	
	private void initRender() {
		bs = window.getCanvas().getBufferStrategy();
		
		if (bs == null) {
			window.getCanvas().createBufferStrategy(2);
			return;
		}
		
		g = (Graphics2D) bs.getDrawGraphics();
		icnmgr.setGraphics(g);
	}
	
	synchronized void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			stop();
		}
	}
}
