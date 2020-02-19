package makrotouch.settings;

import makrotouch.main.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static javax.swing.JOptionPane.showMessageDialog;

public class SettingsActions implements ActionListener {
	
	private boolean connected = false;
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		SettingsManager settings = Main.getSettings();
		
		if(actionEvent.getSource() == settings.getBtCancel()) {
			settings.getWindow().dispose();
			System.out.println("Cancel");
		} else if(actionEvent.getSource() == settings.getBtConnect()) {
			if(settings.getLiWifi().getSelectedIndex() == -1) {
				System.out.println("Connect triggered, no network selected");
				showMessageDialog(settings.getWindow(), "Please select a WiFi network from the list!");
				return;
			}
			
			try {
				if(Main.isRelease()) {
					Runtime.getRuntime().exec("nmcli dev disconnect iface wlan0");
				} else {
					Runtime.getRuntime().exec("nmcli dev disconnect iface wlp3s0");
				}
				Thread.sleep(1000);
				
				String ssid = (String) settings.getLiWifi().getSelectedValue();
				Runtime.getRuntime().exec("nmcli dev wifi connect " + ssid +
						" password " + settings.getTfPassword().getText(), null);
				
				int i = 0;
				while(!connected) {
					Process checkConnected = Runtime.getRuntime().exec("nmcli -f in-use dev wifi", null);
					BufferedReader reader = new BufferedReader(new InputStreamReader(checkConnected.getInputStream()));
					
					Thread.sleep(500);
					
					String currentLine;
					while((currentLine = reader.readLine()) != null) {
						if(currentLine.contains("*")) {
							connected = true;
						}
					}
					i++;
					if(i >= 20) {
						break;
					}
				}
				
				if(connected) {
					showMessageDialog(settings.getWindow(), "Connected!");
					settings.getWindow().dispose();
				} else {
					showMessageDialog(settings.getWindow(), "Could not connect to wifi network!");
				}
				
			} catch(IOException | InterruptedException e) {
				System.out.println("Couldn't connect to WiFi network");
				showMessageDialog(settings.getWindow(), "Couldn't connect to WiFi network!");
			}
		} else if(actionEvent.getSource() == settings.getBtDown()) {
			settings.scrollDown();
		} else if(actionEvent.getSource() == settings.getBtUp()) {
			settings.scrollUp();
		}
	}
}
