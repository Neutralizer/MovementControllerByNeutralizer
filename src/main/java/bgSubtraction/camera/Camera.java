package bgSubtraction.camera;

import org.bytedeco.javacpp.opencv_videoio.VideoCapture;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;


/**
 * 0 camera num is always integrated camera; 1 camera num is always the droid camera;
 *  num 2 is the external camera and so on
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class Camera {
	private final int cameraWidth;
	private final int cameraHeight;
	private final int cameraNum;
	private FrameGrabber grabber;

	public Camera(int cameraNum) {
		this.cameraNum = cameraNum;
		VideoCapture v = new VideoCapture(cameraNum);
		cameraWidth = (int) v.get(3);
		cameraHeight = (int) v.get(4);
		v.close();

		try {
			grabber = FrameGrabber.createDefault(cameraNum);
			grabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isDetected() {
		return (cameraWidth != 0 && cameraHeight != 0);
	}

	/**
	 * FromGrabber
	 * 
	 * @return
	 */
	public Frame getFrame() {
		try {
			return grabber.grab();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getCameraWidth() {
		return cameraWidth;
	}

	public int getCameraHeight() {
		return cameraHeight;
	}
	
	public String getCameraWidthAndHeight() {
		return cameraWidth + " " + cameraHeight;
	}

	public int getCameraNum() {
		return cameraNum;
	}

}
