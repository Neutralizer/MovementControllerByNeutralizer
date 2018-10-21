package bgSubtraction.properties;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.keyboardControl.KeyPressType;

public class PropertiesClass {

	InputStream input = null;
	OutputStream output = null;
	Properties prop = new Properties();

	public static void main(String[] args) {
		PropertiesClass cl = new PropertiesClass();
		cl.loadPropertiesFile();
//		cl.createPropFile("config.properties");
	}

	public void loadPropertiesFile() {

		try {

			String filename = "config.properties";
			input = new FileInputStream("c:\\MovementController\\" + filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}

			// load a properties file from class path, inside static method
			prop.load(input);

			// get the property value and print it out
//			System.out.println(prop.getProperty("database"));
//			System.out.println(prop.getProperty("dbuser"));
//			System.out.println(prop.getProperty("dbpassword"));
			
			ROIManipulator roi = new ROIManipulator(new Camera(0));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void createPropFile(String filename) {
		try {

//			String filename = "config.properties";
			output = new FileOutputStream("c:\\MovementController\\" + filename);

			// set the properties value
			prop.setProperty("camera", "0");
			prop.setProperty("W", "100, 440, 450, 40, KeyEvent.VK_W, KeyPressType.CONSTANT");
			prop.setProperty("R", "100, 0, KeyEvent.VK_R, KeyPressType.PRESS");
			prop.setProperty("F", "600, 0, KeyEvent.VK_F, KeyPressType.PRESS");

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
