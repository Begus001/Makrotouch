package makrotouch.main;

import makrotouch.display.Icon;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

	public static Image loadImage(String filename, Icon sender) {
		try {
			if (!filename.equals("")) {
				return ImageIO.read(new File("res/icons/" + filename));
			} else {
				return null;
			}
		} catch (IOException e) {
			System.out.println("Couldn't load file " + filename + " of Icon " + sender.getId());
			return null;
		}
	}

	public static Image loadImage(String filename) {
		try {
			if (!filename.equals("")) {
				return ImageIO.read(new File("res/icons/" + filename));
			} else {
				return null;
			}
		} catch (IOException e) {
			System.out.println("Couldn't load file " + filename);
			return null;
		}
	}

	public static ArrayList<Icon> loadIcons(String configPath) {
		ArrayList<Icon> tmp = new ArrayList<>();
		try {
			NodeList icons = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configPath).getElementsByTagName("icon");

			for (int i = 0; i < icons.getLength(); i++) {
				Element current = (Element) icons.item(i);

				if (!getProperty(current, "name").equals("") && !getProperty(current, "image_name").equals("")) {
					tmp.add(new Icon(Integer.parseInt(current.getAttribute("id")), getProperty(current, "name"), getProperty(current,
							"image_name")));
				} else if (!getProperty(current, "name").equals("")) {
					tmp.add(new Icon(Integer.parseInt(current.getAttribute("id")), getProperty(current, "name"), false));
				} else {
					tmp.add(new Icon(Integer.parseInt(current.getAttribute("id")), getProperty(current, "image_name"), true));
				}
			}
		} catch (NumberFormatException | SAXException | IOException | ParserConfigurationException e) {
			System.out.println("Couldn't load icons");
		}

		return tmp;
	}

	private static String getProperty(Element element, String name) {
		return element.getElementsByTagName(name).item(0).getTextContent();
	}

}
