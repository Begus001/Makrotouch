package makrotouch.events;

import makrotouch.display.IconManager;
import makrotouch.main.Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TouchListener implements MouseListener {

	private static int x, y;
	private boolean holding;

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
		long currentTime = System.currentTimeMillis();
		System.out.println(e.getX() + "/" + e.getY());
		x = e.getX();
		y = e.getY();
		int iconX;
		int iconY;
		int iconW;
		int iconH;
		try {
			iconX = IconManager.getIcons().get(0).getX();
			iconY = IconManager.getIcons().get(0).getY();
			iconW = IconManager.getIcons().get(0).getWidth();
			iconH = IconManager.getIcons().get(0).getHeight();
		} catch (IndexOutOfBoundsException f) {
			return;
		}
		if (x > iconX && x < iconX + iconW && y > iconY && y < iconY + iconH) {
			holding = true;
			while (holding) {
				if (System.currentTimeMillis() - currentTime > 1000) {
					Main.setProgramState(1);
					return;
				}
			}
		}
	}




	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Released");
		holding = false;
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
