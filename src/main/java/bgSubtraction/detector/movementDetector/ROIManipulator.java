package bgSubtraction.detector.movementDetector;

import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.Mat;

import bgSubtraction.camera.Camera;
import bgSubtraction.keyboardControl.Key;
import bgSubtraction.keyboardControl.KeyPressType;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
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

	// TODO uses 40 size hardcoded value
	public void addRoiToList(int point1, int point2, int keyEvent, KeyPressType keyPressType) {
		isCoordinateWithinCameraRange(point1, point2);
		isSizeWithinCameraRange(point1, point2, 40, 40);
		list.add(new ROI(new Point(point1, point2), new Key(keyEvent, keyPressType)));
	}

	// TODO uses 40 size hardcoded value
	/**
	 * percentage overload
	 * 
	 * @param percentage1
	 * @param percentage2
	 * @param keyEvent
	 * @param keyPressType
	 */
	public void addRoiToList(double percentage1, double percentage2, int keyEvent, KeyPressType keyPressType) {
		int point1 = (int) (currentCameraWidth * percentage1);
		int point2 = (int) (currentCameraHeight * percentage2);
		isCoordinateWithinCameraRange(point1, point2);
		isSizeWithinCameraRange(point1, point2, 40, 40);
		list.add(new ROI(new Point(point1, point2), new Key(keyEvent, keyPressType)));
	}

	public void addRoiToList(int point1, int point2, int point3, int point4, int keyEvent, KeyPressType keyPressType) {
		isCoordinateWithinCameraRange(point1, point2);
		isSizeWithinCameraRange(point1, point2, point3, point4);
		list.add(new ROI(new Point(point1, point2), point3, point4, new Key(keyEvent, keyPressType)));
	}

	/**
	 * percentage overload
	 * 
	 * @param percentage1
	 * @param percentage2
	 * @param percentage3
	 * @param percentage4
	 * @param keyEvent
	 * @param keyPressType
	 */
	public void addRoiToList(double percentage1, double percentage2, double percentage3, double percentage4,
			int keyEvent, KeyPressType keyPressType) {
		int point1 = (int) (currentCameraWidth * percentage1);
		int point2 = (int) (currentCameraHeight * percentage2);
		int point3 = (int) (currentCameraWidth * percentage3);
		int point4 = (int) (currentCameraHeight * percentage4);
		isCoordinateWithinCameraRange(point1, point2);
		isSizeWithinCameraRange(point1, point2, point3, point4);
		list.add(new ROI(new Point(point1, point2), point3, point4, new Key(keyEvent, keyPressType)));
	}

	public void addRoiToList(int point1, int point2, Key key) {
		isCoordinateWithinCameraRange(point1, point2);
		isSizeWithinCameraRange(point1, point2, 40, 40);
		list.add(new ROI(new Point(point1, point2), key));
	}

	/**
	 * 
	 * percentage overload
	 * 
	 * @param percentage1
	 * @param percentage2
	 * @param key
	 */
	public void addRoiToList(double percentage1, double percentage2, Key key) {
		int point1 = (int) (currentCameraWidth * percentage1);
		int point2 = (int) (currentCameraHeight * percentage2);
		isCoordinateWithinCameraRange(point1, point2);
		isSizeWithinCameraRange(point1, point2, 40, 40);
		list.add(new ROI(new Point(point1, point2), key));
	}

	public void updateRoiFromList(int index, int point1, int point2, int keyEvent, KeyPressType keyPressType) {
		isCoordinateWithinCameraRange(point1, point2);
		isSizeWithinCameraRange(point1, point2, 40, 40);
		list.set(index, new ROI(new Point(point1, point2), new Key(keyEvent, keyPressType)));
	}

	public void removeRoiFromList(int index) {
		list.remove(index);
	}

	// TODO reduce with 0.01-0.02 percent if out of bounds when saving with high res
	// and loading at low res
	public double[] convertToPercentage(Point location) {
		double[] percentagePoints = new double[2];
		double x = ((double) location.x / (double) currentCameraWidth);
		double y = ((double) location.y / (double) currentCameraHeight);
		DecimalFormat df = new DecimalFormat("#.##");//TODO find another way if possible
		percentagePoints[0] = Double.parseDouble(df.format(x));
		percentagePoints[1] = Double.parseDouble(df.format(y));
		return percentagePoints;
	}

	public void executeAllROI(Mat bgResult) {
		for (ROI roi : list) {
			roi.execute(bgResult);
		}
	}

	/**
	 * Throws exception when the parameters are not in camera range
	 * 
	 * @param width
	 * @param height
	 */
	private void isCoordinateWithinCameraRange(int width, int height) {
		if (!isWithinCameraRangeWidth(width)) {
			throw new IllegalStateException("ROI Width (Vertical) coordinate is not in range");
		}

		if (!isWithinCameraRangeHeight(height)) {
			throw new IllegalStateException("ROI Height (Horisontal) coordinate is not in range");
		}
	}

	private boolean isWithinCameraRangeWidth(int width) {
		return (width >= 0 && width <= currentCameraWidth);
	}

	private boolean isWithinCameraRangeHeight(int height) {
		return (height >= 0 && height <= currentCameraHeight);
	}

	/**
	 * Throws exception when the constructed roi size is outside of camera bounds
	 * 
	 * @param point1
	 * @param point2
	 * @param point3
	 * @param point4
	 */
	public void isSizeWithinCameraRange(int point1, int point2, int point3, int point4) {
		// from point1 starts point 3 and from point 2 starts point 4
		int widthLength = point1 + point3;
		int heightLength = point2 + point4;

		if (!isWithinCameraRangeWidth(widthLength)) {
			throw new IllegalStateException("ROI Width (Horisontal) length is not in range");
		}

		if (!isWithinCameraRangeHeight(heightLength)) {
			throw new IllegalStateException("ROI Height (Vertical) length is not in range");
		}
	}

}
