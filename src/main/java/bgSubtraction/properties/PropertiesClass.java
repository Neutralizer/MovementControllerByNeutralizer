package bgSubtraction.properties;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
//		cl.loadPropertiesFile("config.properties");
		 cl.createPropFile("config.properties");
	}

	public void loadPropertiesFile(String filename) {

		try {

			input = new FileInputStream("c:\\MovementController\\" + filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}

			// load a properties file from class path, inside static method
			prop.load(input);

			// get the property value and print it out
			// System.out.println(prop.getProperty("database"));
			// System.out.println(prop.getProperty("dbuser"));
			// System.out.println(prop.getProperty("dbpassword"));

			ROIManipulator roiObj = new ROIManipulator(new Camera(0));
			String[] result = prop.getProperty("F").split(",");
			// TODO based on the length of the result - choose overloaded constructor
			System.out.println(Arrays.asList(result));
			roiObj.addRoiToList(3, 3, 3, KeyPressType.getType(3));

			// if 6 - w; if 4 - regular key; if 3 - special

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

			output = new FileOutputStream("c:\\MovementController\\" + filename);

			// set the properties value
//			prop.setProperty("camera", "0");
			// prop.setProperty("W", "100, 440, 450, 40, KeyEvent.VK_W,
			// KeyPressType.CONSTANT");
			// prop.setProperty("R", "100, 0, KeyEvent.VK_R, KeyPressType.PRESS");
			addRoiToProperty("F", 600, 0, KeyEvent.VK_F, KeyPressType.PRESS);
			addCamera("Hercules", 2);

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

	public void addCamera(String cameraName, int cameraNum) {
		prop.setProperty(cameraName, cameraNum + "");
	}

	public void addRoiToProperty(String keyString, int width, int height, int keyEvent, KeyPressType type) {
		int[] f = new int[] { width, height, keyEvent, type.ordinal() };
		prop.setProperty(keyString, f[0] + "," + f[1] + "," + f[2] + "," + f[3] + "");
	}

	public void addRoiToProperty(String key, int width, int height, int sizeWidth, int sizeHeight, int keyEvent,
			KeyPressType type) {
		int[] f = new int[] { width, height, sizeWidth, sizeHeight, keyEvent, type.ordinal() };
		prop.setProperty(key, f[0] + "," + f[1] + "," + f[2] + "," + f[3] + "," + f[4] + "," + f[5] + "");
	}
}
