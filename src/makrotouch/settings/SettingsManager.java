package makrotouch.settings;

import makrotouch.display.Window;
import makrotouch.main.Main;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SettingsManager {

    private JFrame window;

    private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();

    private JButton btConnect, btCancel, btUp, btDown;
    private JList liWifi;
    private JLabel lbLoading;

    private int ESSIDPos, infraPos;

    public SettingsManager() {
        if (Main.isRelease()) {
            window = new Window("Settings", (int) screenBounds.getWidth(), (int) screenBounds.getHeight(), false, true, true);
        } else {
            window = new Window("Settings", 1024, 600, false, true, true);
        }
        initLoading();
        initNetworks();
        initElem();
    }

    private void initLoading() {
        lbLoading = new JLabel("Loading WiFi Networks...", JLabel.CENTER);
        lbLoading.setVisible(true);
        window.add(lbLoading);
        window.pack();
        window.setVisible(true);
    }

    private void initNetworks() {
        try {
            System.out.print("Fetching WiFi networks...");
            Process wifiCommand = Runtime.getRuntime().exec("nmcli dev wifi", null);

            BufferedReader commandOutputReader = new BufferedReader(new InputStreamReader(wifiCommand.getInputStream()));
            StringBuilder wifiNetworks = new StringBuilder();
            String currentLine;
            while ((currentLine = commandOutputReader.readLine()) != null) {
                wifiNetworks.append(currentLine + "\n");
            }

            System.out.println("done");
            Thread.sleep(500);
            System.out.println(wifiNetworks);
            wifiCommand.destroy();

        } catch (IOException | InterruptedException e) {

        }
    }

    private void initElem() {
        lbLoading.setVisible(false);
    }
}
