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

public class PropertiesOperations {

	InputStream input = null;
	OutputStream output = null;
	Properties prop = new Properties();

	// Temporary here
	static ROIManipulator roiObj = new ROIManipulator(new Camera(0));

	public static void main(String[] args) {
		PropertiesOperations cl = new PropertiesOperations();
		// cl.loadPropertiesFile("config.properties");
		cl.createPropFile("config.properties");
	}

	public void loadPropertiesFile(String filename) {
		try {
			input = new FileInputStream("c:\\MovementController\\" + filename);
			if (input == null) {
				System.err.println("Error - unable to find " + filename);
				return;
			}

			// load a properties file from class path, inside static method
			prop.load(input);//TODO add from here

			// get the property value and print it out
			// System.out.println(prop.getProperty("database"));
			// System.out.println(prop.getProperty("dbuser"));
			// System.out.println(prop.getProperty("dbpassword"));
			int cameraNum = loadCameraFromProperty();
			String[] result = getPropertyValues("F");
			
			loadROIToListFromProperties(result);
			System.out.println(Arrays.asList(result));
			roiObj.addRoiToList(3, 3, 3, KeyPressType.getType(3));

			// if 6 - w(custom size); if 4 - regular key; if 3 - special

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

	// TODO do not add camera info as ROI
	// TODO change here to percentages
	public void loadROIToListFromProperties(String[] result) {
		int resultInt[] = convertToInt(result);
		if (resultInt.length == 7) {// key +6 values
			roiObj.addRoiToList(resultInt[1], resultInt[2], resultInt[3], resultInt[4], resultInt[5],
					KeyPressType.getType(resultInt[6]));
		}
		if (resultInt.length == 5) {// key + 4 values
			roiObj.addRoiToList(resultInt[1], resultInt[2], resultInt[3], KeyPressType.getType(resultInt[4]));
		}
	}
	
	public int loadCameraFromProperty() {
		int cameraNum = Integer.valueOf(prop.getProperty("camera"));
		return cameraNum;
	}

	// first index is the string key - do not convert it to int
	// TODO does it throw error when length is 0 - add 0 check in wrapper method
	private int[] convertToInt(String[] result) {
		int resultInt[] = new int[result.length];
		for (int i = 1; i < resultInt.length; i++) {
			resultInt[i] = Integer.valueOf(result[i]);
		}
		return resultInt;
	}

	public String[] getPropertyValues(String property) {
		String[] result = prop.getProperty(property).split(",");
		return result;
	}

	public void createPropFile(String filename) {
		try {

			output = new FileOutputStream("c:\\MovementController\\" + filename);

			addRoiToProperty("F", 600, 0, KeyEvent.VK_F, KeyPressType.PRESS);
			addRoiToProperty("W", 100, 440, 450, 40, KeyEvent.VK_W, KeyPressType.CONSTANT);
			addCameraToProperty(2);

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

	//TODO get camera name in frontend
	public void addCameraToProperty(int cameraNum) {
		prop.setProperty("camera", cameraNum + "");
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
