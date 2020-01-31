package makrotouch.settings;

import makrotouch.main.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import static javax.swing.JOptionPane.showMessageDialog;

public class SettingsActions implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == Main.getSettings().getBtCancel()) {
            Main.getSettings().getWindow().dispose();
        } else if (actionEvent.getSource() == Main.getSettings().getBtConnect()) {
            try {
                Process connectCommand =
                        Runtime.getRuntime().exec("nmcli device wifi connect " + Main.getSettings().getLiWifi().getSelectedValue() +
                                " password " + Main.getSettings().getTfPassword().getText(), null);
            } catch (IOException e) {
                System.out.println("Couldn't connect to WiFi network");
                showMessageDialog(Main.getSettings().getWindow(), "Couldn't connect to WiFi network!");
            }
        }
    }
}
