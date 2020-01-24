package makrotouch.settings;

import makrotouch.display.Window;
import makrotouch.main.Main;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingsManager {

    private JFrame window;

    private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();

    private JButton btConnect, btCancel, btUp, btDown;
    private JList liWifi;
    private JLabel lbLoading;
    
    private ArrayList<String> wifiNetworks;

    private int ESSIDPos, infraPos;

    public SettingsManager() {
        if (Main.isRelease()) {
            window = new Window("Settings", (int) screenBounds.getWidth(), (int) screenBounds.getHeight(), false, true, true);
        } else {
            window = new Window("Settings", 1024, 600, false, true, true);
        }

        wifiNetworks = new ArrayList<>();
        lbLoading = new JLabel("Loading WiFi Networks...", JLabel.CENTER);

        initLoading();
        initNetworks();
        initElem();
    }

    private void initLoading() {
        lbLoading.setPreferredSize(new Dimension(100, 20));
        lbLoading.setMaximumSize(new Dimension(100, 20));
        lbLoading.setMinimumSize(new Dimension(100, 20));
        lbLoading.setVisible(true);
        window.getContentPane().add(lbLoading);
        window.pack();
    }

    private void initNetworks() {
        try {
            System.out.print("Fetching WiFi networks...");
            Process wifiCommand = Runtime.getRuntime().exec("nmcli dev wifi", null);

            BufferedReader commandOutputReader = new BufferedReader(new InputStreamReader(wifiCommand.getInputStream()));
            StringBuilder output = new StringBuilder();
            String currentLine;
            int i = 0;
            while ((currentLine = commandOutputReader.readLine()) != null) {

                if(i == 0) {
                    ESSIDPos = currentLine.indexOf("SSID");
                    infraPos = currentLine.indexOf("MODE");
                } else {
                    wifiNetworks.add(currentLine.substring(ESSIDPos, infraPos).trim());
                }

                output.append(currentLine + "\n");
                i++;
            }

            System.out.println("done");
            Thread.sleep(500);
            System.out.println(output);

            wifiNetworks.forEach(System.out::println);

            wifiCommand.destroy();

        } catch (IOException | InterruptedException e) {

        }
    }

    private void initElem() {

    }
}
