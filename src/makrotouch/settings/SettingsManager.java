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
    private String[] wifiNetworksFinal;

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
    }

    private void initLoading() {
        lbLoading.setPreferredSize(new Dimension((int) screenBounds.getWidth(), 40));
        lbLoading.setMaximumSize(new Dimension((int) screenBounds.getWidth(), 40));
        lbLoading.setMinimumSize(new Dimension((int) screenBounds.getWidth(), 40));
        lbLoading.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        lbLoading.setVisible(true);
        window.getContentPane().add(lbLoading);
        window.pack();
    }

    private void initNetworks() {
        new Thread(() -> {
            try {
                System.out.print("Fetching WiFi networks...");
                Process wifiCommand = Runtime.getRuntime().exec("nmcli dev wifi", null);

                BufferedReader commandOutputReader = new BufferedReader(new InputStreamReader(wifiCommand.getInputStream()));
                StringBuilder output = new StringBuilder();
                String currentLine;
                int i = 0;
                while ((currentLine = commandOutputReader.readLine()) != null) {

                    if (i == 0) {
                        ESSIDPos = currentLine.indexOf("SSID");
                        infraPos = currentLine.indexOf("MODE");
                    } else {
                        if (!wifiNetworks.contains(currentLine.substring(ESSIDPos, infraPos).trim())) {
                            wifiNetworks.add(currentLine.substring(ESSIDPos, infraPos).trim());
                        }
                    }

                    output.append(currentLine + "\n");
                    i++;
                }

                System.out.println("done");

                System.out.println(output);

                wifiNetworks.forEach(System.out::println);

                wifiCommand.destroy();

                initElem();

            } catch (IOException e) {

            }
        }).start();

    }

    private void initElem() {

        lbLoading.setVisible(false);
        window.getContentPane().remove(lbLoading);

        btUp = new JButton("UP");
        btUp.setPreferredSize(new Dimension(100, 50));
        btUp.setMaximumSize(new Dimension(100, 50));
        btUp.setMinimumSize(new Dimension(100, 50));
        btUp.setBackground(Color.GREEN);
        btUp.setVisible(true);
        window.pack();
        window.getContentPane().add(btUp);

        liWifi = new JList(wifiNetworks.toArray());
        liWifi.setPreferredSize(new Dimension(600, 400));
        liWifi.setMaximumSize(new Dimension(600, 400));
        liWifi.setMinimumSize(new Dimension(600, 400));
        liWifi.setVisible(true);
        window.pack();
        window.getContentPane().add(liWifi);

        btDown = new JButton("DOWN");
        btDown.setPreferredSize(new Dimension(100, 50));
        btDown.setMaximumSize(new Dimension(100, 50));
        btDown.setMinimumSize(new Dimension(100, 50));
        btDown.setBackground(Color.RED);
        btDown.setVisible(true);
        window.pack();
        window.getContentPane().add(btDown);
    }
}
