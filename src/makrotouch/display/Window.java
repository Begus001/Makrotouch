package makrotouch.display;

import makrotouch.events.TouchListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

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
	
	private void initWindow() {
		setTitle(title);
		
		setPreferredSize(size);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		canvas = new Canvas();
		canvas.setPreferredSize(size);
		
		//canvas.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16,
		//BufferedImage.TYPE_INT_ARGB),new Point(0, 0), "blank"));
		
		canvas.addMouseListener(new TouchListener());
		
		add(canvas);
		pack();
		
		setVisible(true);
	}
	
	public Window(String title, int width, int height, boolean resizable, boolean undecorated) {
		this.title = title;
		this.size = new Dimension(width, height);
		
		setResizable(resizable);
		setUndecorated(undecorated);
		
		initWindow();
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
