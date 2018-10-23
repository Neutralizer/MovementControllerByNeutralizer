package bgSubtraction.properties;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.keyboardControl.KeyPressType;

public class PropertiesOperations {

	private InputStream input = null;
	private OutputStream output = null;
	private Properties prop = new Properties();

	private ROIManipulator roiObj;

	// public static void main(String[] args) {
	// PropertiesOperations cl = new PropertiesOperations();
	// // cl.loadPropertiesFile("config.properties");
	// cl.createPropFile("config.properties");
	// }

	public PropertiesOperations(ROIManipulator roiObj) {
		super();
		this.roiObj = roiObj;
	}

	public void loadPropertiesFile(String filename) {
		try {
			input = new FileInputStream("c:\\MovementController\\" + filename);
			if (input == null) {
				System.err.println("Error - unable to find " + filename);
				return;
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				loadEachROI(key);
			}
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

	/**
	 * saves every roi from prop inside the list
	 * 
	 * @param propertyKey
	 */
	private void loadEachROI(String propertyKey) {
		String[] result = getPropertyValues(propertyKey);
		loadROIToListFromProperties(result);
	}

	private String[] getPropertyValues(String property) {
		String[] result = prop.getProperty(property).split(",");
		return result;
	}

	// TODO change here to percentages
	private void loadROIToListFromProperties(String[] result) {
		int resultInt[] = convertToInt(result);
		if (resultInt.length == 6) {// 6 values
			roiObj.addRoiToList(resultInt[0], resultInt[1], resultInt[2], resultInt[3], resultInt[4],
					KeyPressType.getType(resultInt[5]));
		}
		if (resultInt.length == 4) {// 4 values
			roiObj.addRoiToList(resultInt[0], resultInt[1], resultInt[2], KeyPressType.getType(resultInt[3]));
		}
	}

	// TODO does it throw error when length is 0 - add 0 check in wrapper method
	private int[] convertToInt(String[] result) {
		int resultInt[] = new int[result.length];
		for (int i = 0; i < resultInt.length; i++) {
			resultInt[i] = Integer.valueOf(result[i]);
		}
		return resultInt;
	}

	/**
	 * will not be used for now
	 * 
	 * @deprecated
	 * @return
	 */
	private int loadCameraFromProperty() {
		int cameraNum = Integer.valueOf(prop.getProperty("camera"));
		return cameraNum;
	}

	public void createPropFile(String filename) {
		try {

			output = new FileOutputStream("c:\\MovementController\\" + filename);

			addRoiToProperty("F", 600, 0, KeyEvent.VK_F, KeyPressType.PRESS);
			// addRoiToProperty("W", 100, 440, 450, 40, KeyEvent.VK_W,
			// KeyPressType.CONSTANT);
			addRoiToProperty("E", 520, 0, KeyEvent.VK_E, KeyPressType.PRESS);

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

	/**
	 * get camera name in frontend will not be used for now
	 * 
	 * @deprecated
	 * @param cameraNum
	 */
	private void addCameraToProperty(int cameraNum) {
		prop.setProperty("camera", cameraNum + "");
	}

	private void addRoiToProperty(String keyString, int width, int height, int keyEvent, KeyPressType type) {
		int[] f = new int[] { width, height, keyEvent, type.ordinal() };
		prop.setProperty(keyString, f[0] + "," + f[1] + "," + f[2] + "," + f[3] + "");
	}

	private void addRoiToProperty(String key, int width, int height, int sizeWidth, int sizeHeight, int keyEvent,
			KeyPressType type) {
		int[] f = new int[] { width, height, sizeWidth, sizeHeight, keyEvent, type.ordinal() };
		prop.setProperty(key, f[0] + "," + f[1] + "," + f[2] + "," + f[3] + "," + f[4] + "," + f[5] + "");
	}
}
