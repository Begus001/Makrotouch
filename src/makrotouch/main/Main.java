package makrotouch.main;

import makrotouch.display.IconManager;
import makrotouch.display.Window;
import makrotouch.settings.SettingsManager;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class Main implements Runnable {
	
	//MUST BE TRUE WHEN BUILDING FOR RELEASE
	private static boolean release = true;
	////////////////////////////////////////
	
	private static int programState = 0;
	private static IconManager icnmgr;
	private static Connection connection;
	private static SettingsManager settings;
	private Graphics2D g;
	private BufferStrategy bs;
	private Thread thread;
	private Window window;
	private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
	private boolean running = false;
	private int actualFrames = -1;
	
	public Main() {
		if(!release) {
			window = new Window("Makrotouch", 1024, 600, true, false, false);
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
		2: Open Settings
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
	
	public static SettingsManager getSettings() {
		return settings;
	}
	
	public void run() {
		init();
		int frames = 0;
		
		long lastTime = System.nanoTime();
		long oneSecond = System.nanoTime();
		
		while(running) {
			long now = System.nanoTime();
			
			if(now - lastTime > 9500000) {
				render();
				tick();
				frames++;
				lastTime = System.nanoTime();
			}
			if((System.nanoTime() - oneSecond) > 1000000000) {
				System.out.println("FPS: " + frames);
				oneSecond = System.nanoTime();
				actualFrames = frames;
				frames = 0;
			}
			try {
				Thread.sleep(5);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void init() {
		icnmgr.initIcons(4, 2, 75);
		try {
			connection = new Connection(42069, 42070);
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.setProperty("sun.java2d.opengl", "true");
	}
	
	private void render() {
		initRender();
		if(g == null)
			return;
		
		switch(programState) {
			case 0:
				try {
					icnmgr.clear();
					//icnmgr.initIcons(4, 2, 75);
					icnmgr.drawIcons();
					if(actualFrames != -1)
						icnmgr.printString(Integer.toString(actualFrames), 20, 540, 20);
				} catch(NullPointerException e) {
					e.printStackTrace();
				}
				break;
			
			case 1:
				icnmgr.initIcons(4, 2, 75);
				setProgramState(0);
				break;
			
			case 2:
				System.out.println("Starting settings page");
				settings = new SettingsManager();
				while(settings.isOpen()) {
					try {
						Thread.sleep(50);
						if(window.isFocused()) {
							settings.getWindow().requestFocus();
						}
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Resuming normal operation");
				setProgramState(0);
		}
		g.dispose();
		bs.show();
	}
	
	private void tick() {
	
	}
	
	private void initRender() {
		bs = window.getCanvas().getBufferStrategy();
		
		if(bs == null) {
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
		} catch(InterruptedException e) {
			stop();
		}
	}
}
