package makrotouch.settings;

import makrotouch.display.Window;
import makrotouch.main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingsManager {
	
	private final int listSize = 22;
	
	private JFrame window;
	private ActionListener listener = new SettingsActions();
	private OnScreenKeyboardLauncher focusListener = new OnScreenKeyboardLauncher();
	private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
	
	private JButton btConnect, btCancel, btUp, btDown;
	private JList liWifi;
	private JLabel lbLoading;
	private JPasswordField tfPassword;
	
	private ArrayList<String> wifiNetworks;
	private DefaultListModel<String> wifiNetworksVisible;
	private boolean open = true;
	private int scrollIndex = 0;
	
	public SettingsManager() {
		if(Main.isRelease()) {
			window = new Window("Settings", (int) screenBounds.getWidth(), (int) screenBounds.getHeight(), false, false, true);
		} else {
			window = new Window("Settings", 1024, 600, false, true, true);
		}
		
		window.setExtendedState(window.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				open = false;
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				open = false;
			}
		});
		
		window.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent componentEvent) {
				window.setState(window.MAXIMIZED_BOTH);
			}
			
			@Override
			public void componentMoved(ComponentEvent componentEvent) {
				window.setLocationRelativeTo(null);
			}
			
			@Override
			public void componentShown(ComponentEvent componentEvent) {
			
			}
			
			@Override
			public void componentHidden(ComponentEvent componentEvent) {
				window.dispose();
			}
		});
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {
				window.setState(window.NORMAL);
				window.setState(window.MAXIMIZED_BOTH);
				window.setLocationRelativeTo(null);
			}
		});
		
		wifiNetworks = new ArrayList<>();
		wifiNetworksVisible = new DefaultListModel<>();
		
		lbLoading = new JLabel("Loading WiFi Networks...", JLabel.CENTER);
		
		window.getContentPane().setBackground(Color.black);
		
		initLoading();
		initNetworks();
	}
	
	private void initLoading() {
		lbLoading.setPreferredSize(new Dimension((int) screenBounds.getWidth(), 40));
		lbLoading.setMaximumSize(new Dimension((int) screenBounds.getWidth(), 40));
		lbLoading.setMinimumSize(new Dimension((int) screenBounds.getWidth(), 40));
		lbLoading.setForeground(Color.WHITE);
		window.getContentPane().add(lbLoading);
		window.pack();
		lbLoading.setVisible(true);
	}
	
	private void initNetworks() {
		new Thread(() -> {
            try {
                
                System.out.print("Fetching WiFi networks...");
                Process wifiCommand = Runtime.getRuntime().exec("nmcli -f ssid dev wifi", null);
                
                BufferedReader commandOutputReader = new BufferedReader(new InputStreamReader(wifiCommand.getInputStream()));
                String currentLine;
                int i = 0;
                while ((currentLine = commandOutputReader.readLine()) != null) {

                    if (i != 0 && !wifiNetworks.contains(currentLine))
                        wifiNetworks.add(currentLine);
                    i++;
                }

                System.out.println("done");

                wifiNetworks.forEach(System.out::println);

                wifiCommand.destroy();
			
			initElem();
           
            } catch (IOException e) {
                javax.swing.JOptionPane.showMessageDialog(this.getWindow(), "Couldn't fetch WiFi networks!");
                window.dispose();
            }
		}).start();
		
	}
	
	private void initElem() {
		
		lbLoading.setVisible(false);
		window.getContentPane().remove(lbLoading);
		
		btUp = new JButton("UP");
		btUp.setPreferredSize(new Dimension(800, 20));
		btUp.setMaximumSize(new Dimension(800, 20));
		btUp.setMinimumSize(new Dimension(800, 20));
		btUp.setBackground(Color.lightGray);
		btUp.setAlignmentX(Component.CENTER_ALIGNMENT);
		btUp.addActionListener(listener);
		window.getContentPane().add(btUp);
		window.pack();
		btUp.setVisible(true);
		
		liWifi = new JList(wifiNetworks.toArray());
		liWifi.setPreferredSize(new Dimension(800, 400));
		liWifi.setMaximumSize(new Dimension(800, 400));
		liWifi.setMinimumSize(new Dimension(800, 400));
		liWifi.setAlignmentX(Component.CENTER_ALIGNMENT);
		window.getContentPane().add(liWifi);
		window.pack();
		liWifi.setVisible(true);
		
		btDown = new JButton("DOWN");
		btDown.setPreferredSize(new Dimension(800, 20));
		btDown.setMaximumSize(new Dimension(800, 20));
		btDown.setMinimumSize(new Dimension(800, 20));
		btDown.setBackground(Color.lightGray);
		btDown.setAlignmentX(Component.CENTER_ALIGNMENT);
		btDown.addActionListener(listener);
		window.getContentPane().add(btDown);
		window.pack();
		btDown.setVisible(true);
		
		tfPassword = new JPasswordField();
		tfPassword.setPreferredSize(new Dimension(600, 20));
		tfPassword.setMaximumSize(new Dimension(600, 20));
		tfPassword.setMinimumSize(new Dimension(600, 20));
		tfPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		tfPassword.addFocusListener(focusListener);
		window.getContentPane().add(tfPassword);
		window.pack();
		tfPassword.setVisible(true);
		
		btConnect = new JButton("Connect");
		btConnect.setPreferredSize(new Dimension(200, 50));
		btConnect.setMaximumSize(new Dimension(200, 50));
		btConnect.setMinimumSize(new Dimension(200, 50));
		btConnect.setBackground(Color.GREEN);
		btConnect.setAlignmentX(Component.CENTER_ALIGNMENT);
		btConnect.addActionListener(listener);
		window.getContentPane().add(btConnect);
		window.pack();
		btConnect.setVisible(true);
		
		btCancel = new JButton("Cancel");
		btCancel.setPreferredSize(new Dimension(200, 50));
		btCancel.setMaximumSize(new Dimension(200, 50));
		btCancel.setMinimumSize(new Dimension(200, 50));
		btCancel.setBackground(Color.RED);
		btCancel.addActionListener(listener);
		btCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
		window.getContentPane().add(btCancel);
		window.pack();
		btCancel.setVisible(true);
	}
	
	public void scrollDown() {
		if(!(scrollIndex >= wifiNetworks.size() - listSize)) {
			wifiNetworksVisible.remove(0);
			wifiNetworksVisible.addElement(wifiNetworks.get(scrollIndex + listSize));
			liWifi.removeAll();
			liWifi.setModel(wifiNetworksVisible);
			scrollIndex++;
		}
	}
	
	public void scrollUp() {
		if(scrollIndex != 0) {
		    wifiNetworksVisible.remove(listSize - 1);
		    wifiNetworksVisible.insertElementAt(wifiNetworks.get(scrollIndex - 1), 0);
		    liWifi.removeAll();
		    liWifi.setModel(wifiNetworksVisible);
		    scrollIndex--;
		}
	}
	
	public int getScrollIndex() {
		return scrollIndex;
	}
	
	public int getListSize() {
		return listSize;
	}
	
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public JTextField getTfPassword() {
		return tfPassword;
	}
	
	public JButton getBtConnect() {
		return btConnect;
	}
	
	public JFrame getWindow() {
		return window;
	}
	
	public JButton getBtCancel() {
		return btCancel;
	}
	
	public JButton getBtUp() {
		return btUp;
	}
	
	public JButton getBtDown() {
		return btDown;
	}
	
	public JList getLiWifi() {
		return liWifi;
	}
}
