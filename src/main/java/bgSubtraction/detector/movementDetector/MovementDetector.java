package bgSubtraction.detector.movementDetector;

import java.util.Arrays;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.opencv_video;
import org.bytedeco.javacpp.opencv_video.BackgroundSubtractorMOG2;
import org.bytedeco.javacpp.indexer.UByteIndexer;

import bgSubtraction.detector.Detector;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class MovementDetector implements Detector {

	private static final int[] WHITE = { 255, 255, 255 };
	Mat firstKernelErode;
	Mat secondKernelDilate;
	BackgroundSubtractorMOG2 fgbg;

	public MovementDetector() {
		this.firstKernelErode = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT, new Size(5, 5));// 5.5
		secondKernelDilate = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT, new Size(21, 21));// 5.5
		this.fgbg = opencv_video.createBackgroundSubtractorMOG2(5, 16, false);// 1,16,false for 1.4;1,100,false for1.4.2

	}

	public void processImage(IplImage img, Mat bgResult) {
		fgbg.apply(new Mat(img), bgResult);

		opencv_imgproc.erode(bgResult, bgResult, firstKernelErode);
		opencv_imgproc.dilate(bgResult, bgResult, secondKernelDilate);//TODO new addition since 001
	}

	/**
	 * is movement detected in a given rect
	 * 
	 * @param bgResult
	 *            - main mat for the black and white image
	 * @param roi
	 *            - rectangle to be processed
	 * @param percentageLimit
	 *            - lower limit for found percentage white pixels; EX - pLimit - 40
	 *            -> if the white pixels percentage is more than 40 - movement
	 *            detected
	 * @return true if movement is detected above the percentageLimit threshold
	 */

	public boolean isMovementDetectedInRect(Mat bgResult, Rect roi, int percentageLimit) {
		// roi = new Rect(100, 400, 450, 80);== will be given to the method
		Mat croppedImage = new Mat(bgResult, roi);/// 2
		int percentageFound = findPercentageWhitePixelsDetected(croppedImage);/// 3

		return percentageFound >= percentageLimit;
	}

	private int findPercentageWhitePixelsDetected(Mat croppedImage) {
		int whitePixelCounter = 0;
		// if there are a lot of white pixels in the rect - do work
		UByteIndexer srcIndexer = croppedImage.createIndexer();
		for (int x = 0; x < srcIndexer.rows(); x++) {
			for (int y = 0; y < srcIndexer.cols(); y++) {
				int[] values = new int[3];
				srcIndexer.get(x, y, values);
				if (Arrays.equals(values, WHITE)) {
					whitePixelCounter++;
				}
			}
		}
		croppedImage.close();
		// white pixels / total number of pixels
		double percentage = ((double) whitePixelCounter / (double) (srcIndexer.rows() * srcIndexer.cols())) * 100;
		return (int) percentage;
	}

}
