package makrotouch.events;

import makrotouch.display.IconManager;
import makrotouch.main.Main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TouchListener implements MouseListener {
	public int x, y;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Main.getIcnmgr().setPage(Main.getIcnmgr().getPage() + 1);
		if(Main.getIcnmgr().getPage() == IconManager.getNumPages()){
			Main.getIcnmgr().setPage(0);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
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
