package makrotouch.display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Window extends JFrame {

	private static final long serialVersionUID = -514326015088809635L;

	private Dimension size;
	private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
	private Canvas canvas;
	private BufferStrategy bs;

	private String title;

	public Window(String title) {
		this.title = title;
		this.size = screenBounds;

		setUndecorated(true);
		setResizable(false);
		setExtendedState(MAXIMIZED_BOTH);

		initWindow();
	}

	public Window(String title, int width, int height, boolean resizable, boolean undecorated) {
		this.title = title;
		this.size = new Dimension(width, height);

		setResizable(resizable);
		setUndecorated(undecorated);

		initWindow();
	}

	private void initWindow() {
		setTitle(title);

		setPreferredSize(size);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		canvas = new Canvas();
		canvas.setPreferredSize(size);

		add(canvas);
		pack();
		
		setVisible(true);
	}

	public String getTitle() {
		return title;
	}

	public Dimension getSize() {
		return size;
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	public BufferStrategy getBS() {
		return bs;
	}
}
