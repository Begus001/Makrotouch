package makrotouch.events;

import makrotouch.display.Icon;
import makrotouch.main.Main;
import makrotouch.settings.SettingsManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class TouchListener implements MouseListener, MouseMotionListener {
	
	//private static boolean released = true;
	//private static ScheduledExecutorService releaseTimer;
	private ArrayList<Icon> icons;
	private ArrayList<int[]> iconPos;
	
	/*
	private Runnable releaseTimeout = () -> {
		released = true;
		System.out.println("Icons released");
	};
	
	public static boolean isReleased() {
		return released;
	}
	
	public static void setReleased(boolean released) {
		TouchListener.released = released;
	}
	
	public static ScheduledExecutorService getReleaseTimer() {
		return releaseTimer;
	}
	
	 */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//Next page trigger area
		if (e.getX() <= 70 && e.getY() <= 70) {
			Main.setProgramState(1);
			if (Main.getIcnmgr().getPage() + 2 <= Main.getIcnmgr().getNumPages()) {
				Main.getIcnmgr().setPage(Main.getIcnmgr().getPage() + 1);
			} else {
				Main.getIcnmgr().setPage(0);
			}
			
			//Exit trigger area
		} else if (e.getX() >= 960 && e.getY() <= 50) { //DEV
			Main.setProgramState(2);
		}
		/*
		else if (e.getX() <= 70 && e.getY() >= 525) {
			released = true;
		}
		
		 */
		
		icons = Main.getIcnmgr().getIcons();
		iconPos = Main.getIcnmgr().getIconPos();
		//Icon trigger detection
		//if (released) {
		for (Icon i : icons) {
			if (e.getX() >= i.getX() && e.getX() <= (i.getX() + i.getWidth()) && e.getY() >= i.getY() && e.getY() <= (i.getY() + i.getHeight())) {
				if (i.getName() != null) {
					
					if (Main.getConnection().SendIcon(i) == 0) {
							/*
							released = false;
							System.out.println("Icons locked");
							
							 */
						
						iconPressAnimation(i);
						System.out.println("Icon " + i.getName() + " triggered!");
						
						//releaseTimer = Executors.newSingleThreadScheduledExecutor();
						//releaseTimer.schedule(releaseTimeout, 6, TimeUnit.SECONDS);
					} else {
						System.out.println("Couldn't send execute command!");
					}
					
				} else {
					if (Main.getConnection().SendIcon(i) == 0) {
							/*
							released = false;
							System.out.println("Icons locked");
							
							 */
						
						iconPressAnimation(i);
						System.out.println("Icon " + i.getImage_name() + " triggered!");
						
						//releaseTimer = Executors.newSingleThreadScheduledExecutor();
						//releaseTimer.schedule(releaseTimeout, 6, TimeUnit.SECONDS);
					} else {
						System.out.println("Couldn't send execute command!");
					}
				}
			}
		}
		//}
	}
	
	private void iconPressAnimation(Icon i) {
		Thread anim = new Thread(() -> {
			i.setWidth(i.getWidth() - 20);
			i.setHeight(i.getHeight() - 20);
			i.setX(i.getX() + 10);
			i.setY(i.getY() + 10);
			
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			i.setWidth(i.getWidth() + 20);
			i.setHeight(i.getHeight() + 20);
			i.setX(i.getX() - 10);
			i.setY(i.getY() - 10);
		});
		anim.start();
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
	
	@Override
	public void mouseDragged(MouseEvent e) {
	
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
