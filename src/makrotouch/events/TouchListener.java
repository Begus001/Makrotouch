package makrotouch.events;

import makrotouch.display.IconManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TouchListener implements MouseListener {


	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println(e.getX() + "/" + e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
