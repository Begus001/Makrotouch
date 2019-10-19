package makrotouch.events;

import makrotouch.main.Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TouchListener implements MouseListener {

	private static int x, y;

	public static int getX() {
		return x;
	}

	public static int getY() {
		return y;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println(e.getX() + "/" + e.getY());
		Main.setProgramState(1);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Released");
		x = e.getX();
		y = e.getY();
		Main.setProgramState(0);
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
