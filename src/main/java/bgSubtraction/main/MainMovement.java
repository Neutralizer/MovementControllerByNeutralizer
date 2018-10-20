package bgSubtraction.main;

import java.awt.AWTException;
import java.awt.event.KeyEvent;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.MovementDetector;
import bgSubtraction.detector.movementDetector.ROI;
import bgSubtraction.display.Display;
import bgSubtraction.keyboardControl.KeyController;
import bgSubtraction.keyboardControl.KeyPressType;
import bgSubtraction.keyboardControl.SpecialKey;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class MainMovement implements Runnable {
	private Camera camera;
	private Display display;
	private MovementDetector movementDetector;
	private KeyController keyController;

	public static void main(String[] args) throws AWTException {
		Camera camera = new Camera(1);
		Display display = new Display();
		MovementDetector movementDetector = new MovementDetector();
		KeyController keyController = new KeyController();
		MainMovement movementDetectorMain = new MainMovement(camera, display,
				movementDetector, keyController);
		Thread th = new Thread(movementDetectorMain);
		th.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				
			}
		});
	}

	public MainMovement(Camera camera, Display display,
			MovementDetector movementDetector, KeyController keyController) {
		this.camera = camera;
		this.display = display;
		this.movementDetector = movementDetector;
		this.keyController = keyController;

		if (!camera.isDetected()) {
			throw new IllegalStateException("Camera not detected (tududu duuummm)");
		}
		
		//TODO test
		System.out.println("Camera width and height " + camera.getCameraWidthAndHeight());
	}

	public void run() {
		try {
			IplImage img;

			CanvasFrame frameMovement = display.createNewFrame("Movement",
					new java.awt.Point(0, 0));
			frameMovement.setSize(camera.getCameraWidth(), camera.getCameraHeight());
			// frameMovement.setResizable(false);

			SpecialKey wsKey = new SpecialKey(KeyEvent.VK_DOLLAR, KeyPressType.SPECIAL,1000);

			ROI.addRoiToList(100, 440, 450, 40, KeyEvent.VK_W, KeyPressType.CONSTANT);//must be 1st
			ROI.addRoiToList(100, 0, KeyEvent.VK_R, KeyPressType.PRESS);
			ROI.addRoiToList(600, 0, KeyEvent.VK_P, KeyPressType.PRESS);
			ROI.addRoiToList(520, 0, KeyEvent.VK_E, KeyPressType.PRESS);
			ROI.addRoiToList(0, 0, KeyEvent.VK_ESCAPE, KeyPressType.PRESS);
//			ROI.addRoiToList(280, 0, KeyEvent.VK_SPACE, KeyPressType.PRESS);
//			ROI.addRoiToList(0, 440, KeyEvent.VK_CONTROL, KeyPressType.PRESS);
			ROI.addRoiToList(0, 250, wsKey);
			
			while (true) {
				img = display.convertFromFrameToIplImage(camera.getFrame());
				if (img != null) {
					img = display.flipImage(img);

					Mat bgResult = new Mat();
					movementDetector.processImage(img, bgResult);
					
					int keyWS = keyController.switchKeyToBePressed(wsKey.getSwitched(), KeyEvent.VK_W, KeyEvent.VK_S);
					ROI.getListRoi().get(0).getKey().setKeyCode(keyWS);
					
					ROI.executeAllROI(ROI.getListRoi(), bgResult);

					display.drawAllROI(ROI.getListRoi(), bgResult);
					display.showImage(frameMovement, bgResult);

				}
			}
		} catch (Exception e) {
			System.err.println("Unexpected error");
			e.printStackTrace();
		}
	}
}
