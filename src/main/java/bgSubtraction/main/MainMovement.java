package bgSubtraction.main;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.MovementDetector;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.display.Display;
import bgSubtraction.keyboardControl.KeyController;
import bgSubtraction.keyboardControl.KeyPressType;
import bgSubtraction.keyboardControl.SpecialKey;
import bgSubtraction.properties.PropertiesOperations;
import interfacePanel.panel.UtilitiesPanel;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class MainMovement implements Runnable {
	private Camera camera;
	private Display display;
	private MovementDetector movementDetector;
	ROIManipulator roi;//TODO new addition
//	private KeyController keyController;
//	private static String selectedPropertiesFile;// TODO make it not static

	public static void startAlgorithm(Camera camera, String selectedPropFile, MovementDetector detector, ROIManipulator roi)
			throws AWTException {
		Display display = new Display();
		MovementDetector movementDetector = detector;
//		KeyController keyController = new KeyController();// TODO may be moved to mpanel to access keys and roi creation
//		selectedPropertiesFile = selectedPropFile;//TODO moved
		MainMovement movementDetectorMain = new MainMovement(camera, display, movementDetector, roi
//				, keyController
				);
		Thread th = new Thread(movementDetectorMain);
		th.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {

			}
		});
	}

	public MainMovement(Camera camera, Display display, MovementDetector movementDetector, ROIManipulator roi
//			,KeyController keyController
			) {
		this.camera = camera;
		this.display = display;
		this.movementDetector = movementDetector;
		this.roi = roi;//TODO new addition
//		this.keyController = keyController;

		if (!camera.isDetected()) {
			throw new IllegalStateException("Camera not detected (tududu duuummm)");
		}

	}

	public void run() {
		try {
			IplImage img;

//			ROIManipulator roi = new ROIManipulator(camera);//TODO move initialization to frame
//			PropertiesOperations prop = new PropertiesOperations(roi);

//			prop.loadPropertiesFile(UtilitiesPanel.FILE_DIR, selectedPropertiesFile);// "config.properties"
			//TODO if wscombo active here
			KeyController keyController = new KeyController();
			SpecialKey wsKey = new SpecialKey(KeyEvent.VK_DOLLAR, KeyPressType.SPECIAL);
			roi.addRoiToList(0, 0.52, wsKey);
			roi.addRoiToList(0.16, 0.96, 0.70, 0.04, KeyEvent.VK_W, KeyPressType.CONSTANT);// must be 1st

			/**
			 * roi.addRoiToList(100, 440, 450, 40, KeyEvent.VK_W,
			 * KeyPressType.CONSTANT);//must be 1st // roi.addRoiToList(0.16, 0.92, 0.70,
			 * 0.09, KeyEvent.VK_W, KeyPressType.CONSTANT);//must be 1st
			 * roi.addRoiToList(100, 0, KeyEvent.VK_R, KeyPressType.PRESS);
			 * roi.addRoiToList(600, 0, KeyEvent.VK_F, KeyPressType.PRESS);
			 * roi.addRoiToList(520, 0, KeyEvent.VK_E, KeyPressType.PRESS);
			 * roi.addRoiToList(0, 0, KeyEvent.VK_ESCAPE, KeyPressType.PRESS); //
			 * roi.addRoiToList(280, 0, KeyEvent.VK_SPACE, KeyPressType.PRESS); //
			 * roi.addRoiToList(0.20, 0.0, KeyEvent.VK_SPACE, KeyPressType.PRESS); //
			 * roi.addRoiToList(0, 440, KeyEvent.VK_CONTROL, KeyPressType.PRESS);
			 */
			Thread.sleep(500);// delay for camera
			while (true) {
				img = display.convertFromFrameToIplImage(camera.getFrame());
				if (img != null) {
					img = display.flipImage(img);

					Mat bgResult = new Mat();
					movementDetector.processImage(img, bgResult);

					//TODO if wscombo active here
					int keyWS = keyController.switchKeyToBePressed(wsKey.getSwitched(), KeyEvent.VK_W, KeyEvent.VK_S);
					roi.getListRoi().get(roi.getListRoi().size()-1).getKey().setKeyCode(keyWS);

					roi.executeAllROI(bgResult);

					display.drawAllROI(roi.getListRoi(), bgResult);
					display.showImage(bgResult);

					display.setTitle("Resolution: " + camera.getCameraWidthAndHeight() + "; FPS: "
							+ Integer.toString(camera.getFPS()));

//					display.attachMouseListener();//TODO attaches mouse listener
				}
			}
		} catch (Exception e) {
			System.err.println("Unexpected error");
			e.printStackTrace();
		}
	}
}
