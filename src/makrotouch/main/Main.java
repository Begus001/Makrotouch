package makrotouch.main;

import makrotouch.display.IconManager;
import makrotouch.display.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Main implements Runnable {
	
	//MUST BE TRUE WHEN BUILDING FOR RELEASE
	private static boolean release = false;
	////////////////////////////////////////
	
	private static int programState = 0;
	private static IconManager icnmgr;
	private static Connection connection;
	private Graphics2D g;
	private BufferStrategy bs;
	private Thread thread;
	private Window window;
	private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
	private boolean running = false;
	
	public Main() {
		if (!release) {
			window = new Window("Makrotouch", 1024, 600, true, false);
		} else {
			window = new Window("Makrotouch");
		}
		
		icnmgr = new IconManager(window);
	}
	
	public static boolean isRelease() {
		return release;
	}
	
	public static int getProgramState() {
		return programState;
	}
	
	public static void setProgramState(int programState) {
		Main.programState = programState;
		
		/*
		PROGRAM STATES:
		0: Nominal drawing
		1: Init once and return to 0
		2: Swiping
		*/
	}
	
	public static IconManager getIcnmgr() {
		return icnmgr;
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
	public static void setConnection(Connection connection) {
		Main.connection = connection;
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
		try {
			connection = new Connection(42069, 42070);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.setProperty("sun.java2d.opengl", "true");
	}
	
	private void tick() {
	
	}
	
	private void render() {
		initRender();
		if (g == null)
			return;
		
		switch (programState) {
			case 0:
				try {
					icnmgr.clear();
					//icnmgr.initIcons(4, 2, 75);
					icnmgr.drawIcons();
				} catch (NullPointerException e) { e.printStackTrace(); }
				break;
			
			case 1:
				icnmgr.initIcons(4, 2, 75);
				setProgramState(0);
				break;
		}
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
		//g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
