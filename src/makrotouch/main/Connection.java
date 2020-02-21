package makrotouch.main;

import makrotouch.display.Icon;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection implements Runnable {

	private int timeout = 2000;

	private DatagramSocket receiveSocket, sendSocket, keepAliveSocket, keepAliveSocketReceiver;

	private int receivePort, sendPort;
	private String address;
	private boolean connected = false;
	private String localIP = null;
	private long lastPong;

	private Runnable CommandListening = () -> {
		try {
			receiveSocket = new DatagramSocket(receivePort);

			byte[] receiveBuffer = new byte[64];
			DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

			while (true) {
				receiveSocket.receive(receivePacket);
				String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());


				if (receivedMessage.equals(("refresh"))) {

					Main.getIcnmgr().initIcons();

					System.out.println("Refreshing Icons");

					System.out.println(Main.getIcnmgr().getPage() + " > " + (Main.getIcnmgr().getNumPages() - 1));

					if (Main.getIcnmgr().getPage() > Main.getIcnmgr().getNumPages() - 1) {
						System.out.println("Page too large");
						Main.getIcnmgr().setPage(Main.getIcnmgr().getNumPages() - 1);
						Main.getIcnmgr().drawIcons();
					} else if (Main.getIcnmgr().getPage() == -1) {
						System.out.println("No icons");
						Main.getIcnmgr().setPage(0);
					}
					Main.setProgramState(1);
				} else if (receivedMessage.equals("makrotouch pong")) {
					lastPong = System.currentTimeMillis();
				}

				/*
				else if (receivedMessage.equals(("release"))) {
					//TouchListener.getReleaseTimer().shutdownNow();
					TouchListener.setReleased(true);
					System.out.println("Icons released");
				}
				
				 */

				Thread.sleep(250);
			}
		} catch (IOException e) {
			System.out.println("Couldn't listen for commands");
		} catch (InterruptedException e) {
			System.out.println("Thread sleep interrupted");
		}
	};

	private Runnable KeepAlivePolling = () -> {
		try {
			String keepAliveMessage = "makrotouch " + localIP;

			keepAliveSocket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(keepAliveMessage.getBytes(), keepAliveMessage.length()
					, InetAddress.getByName(address), sendPort);

			while (true) {
				keepAliveSocket.send(sendPacket);

				if (System.currentTimeMillis() - lastPong >= timeout) {
					System.out.println("Connection timed out");
					resetConnection();
				}

				Thread.sleep(500);
			}
		} catch (IOException e) {
			System.out.println("Couldn't send heartbeat");
		} catch (InterruptedException e) {
			System.out.println("Thread sleep interrupted");
		}
	};

	public Connection(int receivePort, int sendPort) throws SocketException {
		this.receivePort = receivePort;
		this.sendPort = sendPort;
		Thread connectionConfigurator = new Thread(this);
		connectionConfigurator.start();
	}

	public boolean isConnected() {
		return connected;
	}

	public int SendIcon(Icon icon) {
		try {
			if (connected) {
				sendSocket = new DatagramSocket();
				String id = "exec " + icon.getId();
				DatagramPacket sendPacket = new DatagramPacket(id.getBytes(), id.length(),
						InetAddress.getByName(address), sendPort);
				sendSocket.send(sendPacket);
				return 0;
			} else {
				return -1;
			}
		} catch (IOException e) {
			System.out.println("Couldn't send execute command");
			return -1;
		}
	}

	@Override
	public void run() {
		do {
			try {
				localIP = getLocalIP();
			} catch (SocketException e) {
				System.out.println("Couldn't get local IP");
			}
		} while (localIP == null);

		do {
			ListenForIP();
		} while (!connected);

		Thread commandListener = new Thread(CommandListening);
		commandListener.start();

		Thread keepAlivePoller = new Thread(KeepAlivePolling);
		keepAlivePoller.start();
		while (true) {
			if (!(keepAlivePoller.isAlive())) {
				resetConnection();
			}
			if (!(commandListener.isAlive())) {
				resetConnection();
			}
		}
	}

	private String getLocalIP() throws SocketException {
		if (!Main.isRelease()) {
			String command;
			if (System.getProperty("os.name").equals("Linux"))
				if (!Main.isRelease()) {
					command = "ip a";
				} else {
					command = "ifconfig";
				}
			else
				command = "ipconfig";
			Runtime r = Runtime.getRuntime();
			Process p = null;
			try {
				p = r.exec(command);
			} catch (IOException e) {
				System.out.println("Couldn't get local IP");
			}
			Scanner s = new Scanner(p.getInputStream());

			StringBuilder sb = new StringBuilder();
			while (s.hasNext())
				sb.append(s.next());
			String ipconfig = sb.toString();
			Pattern pt = Pattern.compile("172\\.168\\.[0-9]{1,3}\\.[0-9]{1,3}");
			Matcher mt = pt.matcher(ipconfig);
			mt.find();

			try {
				return mt.group();
			} catch (IllegalStateException e) {
				r = Runtime.getRuntime();
				p = null;
				try {
					p = r.exec(command);
				} catch (IOException ex) {
					System.out.println("Reverting to class A network");
				}
				s = new Scanner(p.getInputStream());

				sb = new StringBuilder();
				while (s.hasNext())
					sb.append(s.next());
				ipconfig = sb.toString();
				pt = Pattern.compile("10\\.0\\.[0-9]{1,3}\\.[0-9]{1,3}");
				mt = pt.matcher(ipconfig);
				mt.find();

				try {
					return mt.group();
				} catch (IllegalStateException ex) {
					r = Runtime.getRuntime();
					p = null;
					try {
						p = r.exec(command);
					} catch (IOException exe) {
						System.out.println("Reverting to class C network");
					}
					s = new Scanner(p.getInputStream());

					sb = new StringBuilder();
					while (s.hasNext())
						sb.append(s.next());
					ipconfig = sb.toString();
					pt = Pattern.compile("192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3}");
					mt = pt.matcher(ipconfig);
					mt.find();

					try {
						return mt.group();
					} catch (IllegalStateException exe) {
						return null;
					}
				}
			}

		} else {
			Enumeration e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();

				if (n.getDisplayName().equals("wlan0")) {

					Enumeration ee = n.getInetAddresses();

					while (ee.hasMoreElements()) {
						InetAddress a = (InetAddress) ee.nextElement();
						if (!a.getHostAddress().substring(0, 3).equals("127") && a.getHostAddress().contains(".")) {
							return a.getHostAddress();
						}
					}
				}
			}
		}
		return null;
	}

	private void ListenForIP() {
		try {

			receiveSocket = new DatagramSocket(receivePort);

			byte[] receiveBuffer = new byte[64];

			DatagramPacket receivedPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

			System.out.println("Local IP: " + localIP);

			while (true) {
				System.out.println("Listening to port " + receivePort);
				receiveSocket.receive(receivedPacket);

				String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

				String identifier = "makrotouch ";
				if (receivedMessage.contains(identifier)) {
					address =
							receivedMessage.substring(receivedMessage.indexOf(identifier) + identifier.length());
					connected = true;
					lastPong = System.currentTimeMillis();
					receiveSocket.close();
					System.out.println("Connected to " + address);
					break;
				}

				Thread.sleep(250);
			}

		} catch (IOException | InterruptedException e) {
			System.out.println("Couldn't listen for Makrotouch control application");
			address = "";
			connected = false;

		}
	}

	private void resetConnection() {
		if (sendSocket != null) sendSocket.close();
		if (receiveSocket != null) receiveSocket.close();
		if (keepAliveSocket != null) keepAliveSocket.close();
	}
}