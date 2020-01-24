package makrotouch.display;

import makrotouch.events.TouchListener;
import makrotouch.main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

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

        initWindow(false);
    }

    private void initWindow(boolean layout) {
        setTitle(title);

        setPreferredSize(size);

        setLocationRelativeTo(null);

        if(!layout) {
            canvas = new Canvas();
            canvas.setPreferredSize(size);

            canvas.addMouseListener(new TouchListener());

            setDefaultCloseOperation(EXIT_ON_CLOSE);

            add(canvas);
        } else {
        	this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}

        pack();

        setVisible(true);
    }

    public Window(String title, int width, int height, boolean resizable, boolean undecorated, boolean layout) {
        this.title = title;
        this.size = new Dimension(width, height);

        setResizable(resizable);
        setUndecorated(undecorated);

        initWindow(layout);
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
