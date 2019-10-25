package makrotouch.main;

import makrotouch.display.Icon;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {
	
	public static Image loadImage(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			return null;
		}
	}
	
	public static ArrayList<Icon> loadIcons(String configPath) {
		ArrayList<Icon> tmp = new ArrayList<>();
		try {
			NodeList icons = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configPath).getElementsByTagName("icon");
			for (int i = 0; i < icons.getLength(); i++) {
				Element current = (Element) icons.item(i);
				tmp.add(new Icon(Integer.parseInt(current.getAttribute("id")), getProperty(current, "name"), getProperty(current, "image_path")));
			}
		} catch (NumberFormatException | SAXException | IOException | ParserConfigurationException e) {
			return null;
		}
		
		return tmp;
	}
	
	private static String getProperty(Element element, String name) {
		return element.getElementsByTagName(name).item(0).getTextContent();
	}
	
}
