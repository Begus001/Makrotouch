package makrotouch.settings;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

public class OnScreenKeyboardLauncher implements FocusListener {
	@Override
	public void focusGained(FocusEvent focusEvent) {
		try {
			System.out.println("Launching keyboard");
			Runtime.getRuntime().exec("matchbox-keyboard");
		} catch (IOException e) {
			System.out.println("Couldn't open on-screen keyboard");
		}
	}

	@Override
	public void focusLost(FocusEvent focusEvent) {
		try {
			Runtime.getRuntime().exec("killall matchbox-keyboa");
		} catch (IOException e) {
			System.out.println("Couldn't dispose on-screen keyboard");
		}
	}
}
