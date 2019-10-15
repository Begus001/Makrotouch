package makrotouch.main;

import makrotouch.display.Drawer;
import makrotouch.display.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.*;

public class Main implements Runnable {

	private Graphics2D g;
	private BufferStrategy bs;
	private Thread thread;
	private Window window;
	private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
	private Drawer draw;

	private boolean running = false;

	public Main() {
		window = new Window("Test");
	}

	public void run() {
		System.out.println("Running");
		while (running) {
			render();
		}
		System.out.println("Thread stopping");
	}

	public void render() {
		bs = window.getCanvas().getBufferStrategy();

		if (bs == null) {
			window.getCanvas().createBufferStrategy(3);
			return;
		}

		g = (Graphics2D) bs.getDrawGraphics();
		draw = new Drawer(g, window);
		// Begin Draw
		draw.clear();
		
		draw.initIcons(4, 2, 100);
		
		// End Draw
		g.dispose();
		bs.show();
	}

	public synchronized void start() {
		System.out.println("Thread starting");
		running = true;
		thread = new Thread(this);
		thread.start();
		System.out.println("Thread started");
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
			System.out.println("Thread stopped");
		} catch (InterruptedException e) {
			stop();
		}
	}
}
