package bgSubtraction.properties;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import bgSubtraction.detector.movementDetector.ROI;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.keyboardControl.KeyPressType;
import interfacePanel.panel.UtilitiesPanel;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class PropertiesOperations {

	private InputStream input = null;
	private OutputStream output = null;
	private Properties prop = new Properties();

	private ROIManipulator roiManipulator;

	// public static void main(String[] args) {
	// PropertiesOperations cl = new PropertiesOperations();
	// // cl.loadPropertiesFile("config.properties");
	// cl.createPropFile(UtilitiesPanel.FILE_DIR,"config.properties");
	// }

	public PropertiesOperations(ROIManipulator roiManipulator) {
		super();
		this.roiManipulator = roiManipulator;
	}

	public void saveRoiToPropFile(String folderPath, String currentPropFile) {
		try {

			output = new FileOutputStream(folderPath + "\\" + currentPropFile);

			//TODO make unique intermediary check with map
			for (ROI roi : roiManipulator.getListRoi()) {
				String keyName = KeyEvent.getKeyText(roi.getKey().getKeyCode());
				double percentage[] = roiManipulator.convertToPercentage(roi.getCoordinate());
				addRoiToProperty(keyName, percentage[0], percentage[1], roi.getKey().getKeyCode(),
						roi.getKey().getKeyPressType());
			}

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

	public void createPropFile(String folderPath, String filename) {
		try {

			output = new FileOutputStream(folderPath + filename);

			addRoiToProperty("R", 0.14, 0.0, KeyEvent.VK_R, KeyPressType.PRESS);
			addRoiToProperty("E", 0.80, 0.0, KeyEvent.VK_E, KeyPressType.PRESS);
			addRoiToProperty("F", 0.92, 0.0, KeyEvent.VK_F, KeyPressType.PRESS);
			addRoiToProperty("ESCAPE", 0.0, 0.0, KeyEvent.VK_ESCAPE, KeyPressType.PRESS);

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

	private void addRoiToProperty(String keyString, int width, int height, int keyEvent, KeyPressType type) {
		int[] f = new int[] { width, height, keyEvent, type.ordinal() };
		prop.setProperty(keyString, f[0] + "," + f[1] + "," + f[2] + "," + f[3] + "");
	}

	private void addRoiToProperty(String keyString, double width, double height, int keyEvent, KeyPressType type) {
		prop.setProperty(keyString, width + "," + height + "," + keyEvent + "," + type.ordinal() + "");
	}

	private void addRoiToProperty(String key, int width, int height, int sizeWidth, int sizeHeight, int keyEvent,
			KeyPressType type) {
		int[] f = new int[] { width, height, sizeWidth, sizeHeight, keyEvent, type.ordinal() };
		prop.setProperty(key, f[0] + "," + f[1] + "," + f[2] + "," + f[3] + "," + f[4] + "," + f[5] + "");
	}

	/**
	 * Loads properties file and populates given ROIManipulator object with values
	 * from the prop file
	 * 
	 * @param folderPath
	 * @param filename
	 */
	public void loadPropertiesFileIntoInternalRoiManipulator(String folderPath, String filename) {
		try {
			input = new FileInputStream(folderPath + "\\" + filename);
			// input = new FileInputStream("REMOVEME.properties");//TODO for prop inside jar
			if (input == null) {
				System.err.println("Error - unable to find " + filename);// TODO throw for future displaying of err
				return;
			}

			prop.load(input);

			loopThroughEachProp();
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

	private void loopThroughEachProp() {
		Enumeration<?> e = prop.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			loadEachSquare(key);
		}
	}

	/**
	 * saves every roi from prop inside the list
	 * 
	 * @param propertyKey
	 */
	private void loadEachSquare(String propertyKey) {
		String[] result = getPropertyValues(propertyKey);
		loadROIToListFromProperties(result);
	}

	private String[] getPropertyValues(String property) {
		String[] result = prop.getProperty(property).split(",");
		return result;
	}

	private void loadROIToListFromProperties(String[] result) {
		// int resultInt[] = convertToInt(result);
		float resultFloat[] = convertToFloat(result);
		if (result.length == 6) {// 6 values
			roiManipulator.addRoiToList(resultFloat[0], resultFloat[1], resultFloat[2], resultFloat[3],
					(int) resultFloat[4], KeyPressType.getType((int) resultFloat[5]));
		}
		if (result.length == 4) {// 4 values
			roiManipulator.addRoiToList(resultFloat[0], resultFloat[1], (int) resultFloat[2],
					KeyPressType.getType((int) resultFloat[3]));
		}
	}

	/**
	 * if percentage values are used
	 * 
	 * @param result
	 * @return
	 */
	private float[] convertToFloat(String[] result) {
		float resultInt[] = new float[result.length];
		for (int i = 0; i < resultInt.length; i++) {
			resultInt[i] = Float.valueOf(result[i]);
		}
		return resultInt;
	}

	/**
	 * if no percentage values are used
	 * 
	 * @param result
	 * @return
	 */
	private int[] convertToInt(String[] result) {
		int resultInt[] = new int[result.length];
		for (int i = 0; i < resultInt.length; i++) {
			resultInt[i] = Integer.valueOf(result[i]);
		}
		return resultInt;
	}

	@Deprecated
	// TODO load only ; load prop and get their strings
	public String[][] loadPropertiesFileStringsOnly(String filename) {
		try {
			input = new FileInputStream("c:\\MovementController\\" + filename);
			if (input == null) {
				System.err.println("Error - unable to find " + filename);
				throw new FileNotFoundException("Error - unable to find " + filename);
			}

			prop.load(input);

			String[][] propResultAsString = getPropResultAsString();
			return propResultAsString;

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
		return null;
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

	/**
	 * get camera name in frontend will not be used for now
	 * 
	 * @deprecated
	 * @param cameraNum
	 */
	private void addCameraToProperty(int cameraNum) {
		prop.setProperty("camera", cameraNum + "");
	}

	@Deprecated
	/**
	 * gets props as strings for displaying
	 * 
	 * @return
	 */
	private String[][] getPropResultAsString() {
		return loopThroughEachPropStrings();
	}

	@Deprecated
	private String[][] loopThroughEachPropStrings() {
		Enumeration<?> e = prop.propertyNames();
		String[][] propStrings = new String[100][100];
		int i = 0;
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			propStrings[i] = loadEachSquareStrings(key);
			i++;
		} // TODO remove 100 100
		return propStrings;
	}

	@Deprecated
	/**
	 * saves every roi from prop inside the list
	 * 
	 * @param propertyKey
	 * @return
	 */
	private String[] loadEachSquareStrings(String propertyKey) {
		String[] result = getPropertyValues(propertyKey);
		return result;
		// add to list and return
	}
}
