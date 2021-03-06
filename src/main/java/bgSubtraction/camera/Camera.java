package bgSubtraction.camera;

import org.bytedeco.javacpp.opencv_videoio.VideoCapture;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;

/**
 * 0 camera num is always integrated camera; 1 camera num is always the droid
 * camera; num 2 is the external camera and so on
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class Camera {
	private int cameraWidth;
	private int cameraHeight;
	private final int cameraNum;
	private FrameGrabber grabber;
	private long startTime = System.currentTimeMillis();
	private long startTimeLimiter = System.currentTimeMillis();
	private int fps = 0;
	private int currentfps = 0;
	private boolean limitFPS = true;

	public Camera(int cameraNum) {
		this.cameraNum = cameraNum;

		VideoCapture v = new VideoCapture();
		boolean openingSuccessful = v.open(cameraNum);
		if (!openingSuccessful) {
			v.close();
			throw new IllegalStateException("Camera is busy or unavailable");
		}

		// v.set(5,24);//TODO FPS limiter - video capture is not used actually
		cameraWidth = (int) v.get(3);
		cameraHeight = (int) v.get(4);
		v.set(39, 0);//39 - autofocus
		// v.set(37, 1);//TODO testing camera settings panel-setting prop after opening
		// the cam
//		if (isCameraBiggerThan640()) {//TODO make scaling down if needed
//			v.set(3, 640);
//			v.set(4, 480);
//			cameraWidth = 640;
//			cameraHeight = 480;
//		}
		v.release();
		v.close();

		try {
			grabber = FrameGrabber.createDefault(cameraNum);
			grabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isCameraBiggerThan640() {
		if (cameraWidth > 640 && cameraHeight > 480) {
			return true;
		}
		return false;
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
		if(limitfps()) {
			return null;
		}
		
		getFps();
		try {
			return grabber.grab();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void releaseCurrentCamera() {
		try {
			grabber.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * must allow execution once in 33ms (for 30 fps limiter) </br>
	 * if 33ms have not passed and another execution is requested - block it/skip it
	 * 
	 * @param fpsLimit
	 * @return
	 */
	private boolean limitfps() {
		int desiredMiliExecutionLimiterTime = 1000 / 30;// 33.3
		long currentTime = System.currentTimeMillis();

		if ((currentTime - startTimeLimiter) < desiredMiliExecutionLimiterTime) {
			return true;
		} else {
			startTimeLimiter = currentTime;
			return false;
		}

	}

	/**
	 * get current time milis - if 1 second has passed - check a counter++ value and
	 * return the fps - set the current time as the start time
	 */
	private void getFps() {
		long currentTime = System.currentTimeMillis();
		currentfps++;
		if ((currentTime - startTime) > 1000) {
			fps = currentfps;
			currentfps = 0;
			startTime = currentTime;
		}
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

	public int getFPS() {
		return fps;
	}

}
