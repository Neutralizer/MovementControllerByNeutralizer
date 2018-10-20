package bgSubtraction.detector.movementDetector;

import java.awt.Point;
import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.Mat;

import bgSubtraction.camera.Camera;
import bgSubtraction.keyboardControl.Key;
import bgSubtraction.keyboardControl.KeyPressType;

public class ROIManipulator {

	private int currentCameraWidth;
	private int currentCameraHeight;
	private ArrayList<ROI> list;

	public ROIManipulator(Camera camera) {
		currentCameraWidth = camera.getCameraWidth();
		currentCameraHeight = camera.getCameraHeight();
		list = new ArrayList<>();
	}

	public ArrayList<ROI> getListRoi() {
		return list;
	}

	/**
	 * percentage overload
	 * @param percentage1
	 * @param percentage2
	 * @param keyEvent
	 * @param keyPressType
	 */
	public void addRoiToList(double percentage1, double percentage2, int keyEvent, KeyPressType keyPressType) {
		list.add(new ROI(new Point((int) (currentCameraWidth * percentage1), (int) (currentCameraHeight * percentage2)),
				new Key(keyEvent, keyPressType)));
	}

	public void addRoiToList(int point1, int point2, int keyEvent, KeyPressType keyPressType) {
		list.add(new ROI(new Point(point1, point2), new Key(keyEvent, keyPressType)));
	}

	public void addRoiToList(int point1, int point2, int point3, int point4, int keyEvent, KeyPressType keyPressType) {
		list.add(new ROI(new Point(point1, point2), point3, point4, new Key(keyEvent, keyPressType)));
	}

	/**
	 * percentage overload
	 * @param percentage1
	 * @param percentage2
	 * @param percentage3
	 * @param percentage4
	 * @param keyEvent
	 * @param keyPressType
	 */
	public void addRoiToList(double percentage1, double percentage2, double percentage3, double percentage4,
			int keyEvent, KeyPressType keyPressType) {
		list.add(new ROI(new Point((int) (currentCameraWidth * percentage1), (int) (currentCameraHeight * percentage2)),
				(int) (currentCameraWidth * percentage3), (int) (currentCameraHeight * percentage4),
				new Key(keyEvent, keyPressType)));
	}

	public void addRoiToList(int point1, int point2, Key key) {
		list.add(new ROI(new Point(point1, point2), key));
	}

	/**
	 * 
	 * percentage overload
	 * @param percentage1
	 * @param percentage2
	 * @param key
	 */
	public void addRoiToList(double percentage1, double percentage2, Key key) {
		list.add(new ROI(new Point((int) (currentCameraWidth * percentage1), (int) (currentCameraHeight * percentage2)),
				key));
	}

	public void executeAllROI(Mat bgResult) {
		for (ROI roi : list) {
			roi.execute(bgResult);
		}
	}

}
