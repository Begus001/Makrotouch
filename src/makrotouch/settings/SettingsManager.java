package makrotouch.settings;

import javax.swing.*;

public class SettingsManager {

    private JFrame frame;

    private JButton btConnect, btCancel, btUp, btDown;
    private JList liWLAN;
    private JLabel lbLoading;

    public SettingsManager(JFrame frame) {
        this.frame = frame;
        initLoading();
        initElem();
    }

    private void initLoading() {
        lbLoading = new JLabel("Loading W-LAN Networks...", JLabel.CENTER);
        lbLoading.setVisible(true);
        frame.add(lbLoading);
        frame.pack();
    }

    private void initElem() {

    }
}
