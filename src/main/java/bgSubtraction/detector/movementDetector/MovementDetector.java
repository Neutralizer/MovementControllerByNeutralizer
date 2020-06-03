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
	int blurSize = 5;// 21 max Even numbers only
	int firstKernelErodeSize = 3;// 5
	int secondKernelDilateSize = 3;// 1; even number above 0 - 1 = no effect
	int subtractorHistoryLength = 4;// 1; not 0
	int subractorThreshold = 10;// 16
	int detectionLimitPercentage = 75;
	int totalMovementFoundPercentage = 0;
	int backgroundUpdateRate = 5;//0-100

	public MovementDetector() {
		this.firstKernelErode = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT,
				new Size(firstKernelErodeSize, firstKernelErodeSize));// 5.5
		this.secondKernelDilate = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT,
				new Size(secondKernelDilateSize, secondKernelDilateSize));// 5.5 (0 - was not in the original design)
		this.fgbg = opencv_video.createBackgroundSubtractorMOG2(subtractorHistoryLength, subractorThreshold, false);// 1,16,false
																													// for
																													// 1.4;1,100,false
																													// for1.4.2
	}
	
	/**
	 * only odd numbers 
	 * @param blur
	 */
	public void changeBlur(int blur) {
		if (blurSize != blur) {
			blurSize = blur;
		}
	}

	public void changeErode(int erode) {
		if (firstKernelErodeSize != erode) {
			firstKernelErodeSize = erode;
			this.firstKernelErode = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT,
					new Size(firstKernelErodeSize, firstKernelErodeSize));// 5.5
		}
	}

	public void changeDilate(int dilate) {
		if (secondKernelDilateSize != dilate) {
			secondKernelDilateSize = dilate;
			this.secondKernelDilate = opencv_imgproc.getStructuringElement(opencv_imgproc.MORPH_RECT,
					new Size(secondKernelDilateSize, secondKernelDilateSize));// 5.5
		}
	}

	public void changeHistory(int history) {
		if (subtractorHistoryLength != history) {
			subtractorHistoryLength = history;
			this.fgbg.setHistory(subtractorHistoryLength);
		}
	}

	public void changeSubTresh(int subThreshold) {
		if (subractorThreshold != subThreshold) {
			subractorThreshold = subThreshold;
			this.fgbg.setVarThreshold(subractorThreshold);
		}
	}
	
	public void changeDetectionLimit(int detLimit) {
		if (detectionLimitPercentage != detLimit) {
			detectionLimitPercentage = detLimit;
		}
	}
	
	public void changebackgroundUpdateRate(int bgUpdateRate) {
		if (backgroundUpdateRate != bgUpdateRate) {
			backgroundUpdateRate = bgUpdateRate;
		}
	}
	
	public int getTotalMovementPercentage() {
		return totalMovementFoundPercentage;
	}

	public void processImage(IplImage img, Mat bgResult) {
		Mat imgMat = new Mat(img);

		fgbg.apply(imgMat, bgResult, backgroundUpdateRate  / 100d);

		opencv_imgproc.medianBlur(bgResult, bgResult, blurSize);

		opencv_imgproc.erode(bgResult, bgResult, firstKernelErode);
		opencv_imgproc.dilate(bgResult, bgResult, secondKernelDilate);

	}
	
	public String tellIfMovementDetectionIsBlocked() {
		if(totalMovementFoundPercentage >= detectionLimitPercentage ) {
			return "MOVEMENT DETECTION BLOCKED";
		} else {
			return "";
		}
	}
	
	/**
	 * 
	 * If too much movement is detected on the whole screen, then it is a flicker and do not press buttons.
	 * DetectionLimitPercentage - percentage threshold - if the total movement detection is above it - do not press
	 * @param bgResult whole screen
	 *  
	 * @return true if there is more than detectionLimitPercentage (75% for ex) of the screen detected as movement
	 */
	public boolean isMovementDetectedAndLimitReached(Mat bgResult) {
		int percentageFound = findPercentageWhitePixelsWholeScreen(bgResult);
		totalMovementFoundPercentage = percentageFound;
		return percentageFound >= detectionLimitPercentage;
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
	
	/**
	 * This one does not close the image, because it needs to be further processed by individual buttons
	 * @param image whole screen
	 * @return percentage of white pixels in image - where movement is detected
	 */
	private int findPercentageWhitePixelsWholeScreen(Mat image) {
		int whitePixelCounter = 0;
		// if there are a lot of white pixels - do work
		// -1 because it crashes 
		UByteIndexer srcIndexer = image.createIndexer();
		for (int x = 0; x < srcIndexer.rows()-1; x++) {
			for (int y = 0; y < srcIndexer.cols()-1; y++) {
				int[] values = new int[3];
				srcIndexer.get(x, y, values);
				if (Arrays.equals(values, WHITE)) {
					whitePixelCounter++;
				}
			}
		}
//		image.close();
		// white pixels / total number of pixels
		double percentage = ((double) whitePixelCounter / (double) (srcIndexer.rows() * srcIndexer.cols())) * 100;
		return (int) percentage;
	}

	
	/**
	 * Closes the roi cutout of the main image from video feed
	 * @param croppedImage roi virtual button
	 * @return percentage of white pixels in image - where movement is detected
	 */
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
