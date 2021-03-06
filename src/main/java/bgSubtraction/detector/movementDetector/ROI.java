package bgSubtraction.detector.movementDetector;

import java.awt.Point;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;

import bgSubtraction.keyboardControl.Key;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class ROI {

	/**
	 * 5 - very high detection; 95 - very low detection
	 */
	private int percentageToDetect;
	private Point coordinate;
	private Rect roi;
	private Key key;
	private MovementDetector movementDetector = new MovementDetector();
	
	/**
	 * square size - 40 x 40 px 
	 * @param coordinate
	 * @param key
	 */
	public ROI(Point coordinate, Key key) {
		percentageToDetect = 20;//normal detection //10 for night mode ; 50 for normal mode
		this.coordinate = coordinate;
		roi = new Rect(coordinate.x, coordinate.y, 40, 40);
		this.key = key;
	}
	
	public ROI(Point coordinate, int size1, int size2, Key key) {
		percentageToDetect = 2;//very high detection//5 originally
		this.coordinate = coordinate;
		roi = new Rect(coordinate.x, coordinate.y, size1, size2);
		this.key = key;
	}

	public Rect getRoi() {
		return this.roi;
	}

	public Point getCoordinate() {
		return this.coordinate;
	}

	public Key getKey() {
		return this.key;
	}

	/**
	 * Finds if there is movement in this roi and if there is - execute the key
	 * action
	 * 
	 * @param bgResult
	 *            bg subtraction black and white mat of camera
	 */
	public void execute(Mat bgResult) {
		boolean isDetected = movementDetector.isMovementDetectedInRect(bgResult, roi, percentageToDetect);
		key.executeAction(isDetected);
	}

}
