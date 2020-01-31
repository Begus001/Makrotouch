package makrotouch.settings;

import makrotouch.display.Window;
import makrotouch.main.Main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingsManager {

    private JFrame window;
    private ActionListener listener = new SettingsActions();
    private Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();

    private JButton btConnect, btCancel, btUp, btDown;
    private JList liWifi;
    private JLabel lbLoading;
    private JTextField tfPassword;

    private ArrayList<String> wifiNetworks;
    private String[] wifiNetworksFinal;

    private int ESSIDPos, infraPos;
    private boolean open = true;

    public SettingsManager() {
        if (Main.isRelease()) {
            window = new Window("Settings", (int) screenBounds.getWidth(), (int) screenBounds.getHeight(), false, true, true);
        } else {
            window = new Window("Settings", 1024, 600, false, true, true);
        }

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

        wifiNetworks = new ArrayList<>();

        lbLoading = new JLabel("Loading WiFi Networks...", JLabel.CENTER);

        window.getContentPane().setBackground(Color.black);

        initLoading();
        initNetworks();
    }

    public boolean isOpen() {
        return open;
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

    public JTextField getTfPassword() {
        return tfPassword;
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

    private void initElem() {

        lbLoading.setVisible(false);
        window.getContentPane().remove(lbLoading);

        btUp = new JButton("UP");
        btUp.setPreferredSize(new Dimension(800, 20));
        btUp.setMaximumSize(new Dimension(800, 20));
        btUp.setMinimumSize(new Dimension(800, 20));
        btUp.setBackground(Color.lightGray);
        btUp.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        window.getContentPane().add(btDown);
        window.pack();
        btDown.setVisible(true);

        tfPassword = new JTextField();
        tfPassword.setPreferredSize(new Dimension(600, 20));
        tfPassword.setMaximumSize(new Dimension(600, 20));
        tfPassword.setMinimumSize(new Dimension(600, 20));
        tfPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
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
}
