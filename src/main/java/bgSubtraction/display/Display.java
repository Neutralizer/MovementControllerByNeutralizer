package bgSubtraction.display;

import static org.bytedeco.javacpp.opencv_core.cvFlip;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import bgSubtraction.detector.movementDetector.ROI;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class Display {

	private CanvasFrame frame;

	private OpenCVFrameConverter.ToIplImage converter;
	Java2DFrameConverter c = new Java2DFrameConverter();// for BufferedImage

	public Display() {
		converter = new OpenCVFrameConverter.ToIplImage();
		frame = createNewFrame("Movement", new java.awt.Point(0, 0));
		frame.setSize(640, 480);
//		attachMouseListener();//TODO moved in jtable to get square location
	}

	/**
	 * passed to jtable to get square location info easily
	 * @return
	 */
	public CanvasFrame getFrame() {
		return frame;
	}

	private CanvasFrame createNewFrame(String name, java.awt.Point location) {
		frame = new CanvasFrame(name);
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		frame.setLocation(location);
		return frame;
	}

	/**
	 * Actual - the original one
	 * 
	 * @param canvasFrame
	 * @param mat
	 */
	public void showImage(CanvasFrame canvasFrame, Mat mat) {
		Frame frame = converter.convert(mat);
		canvasFrame.showImage(frame);
	}

	/**
	 * uses internally CanvasFrame
	 * 
	 * @param mat
	 */
	public void showImage(Mat mat) {
		Frame frameForDisplay = converter.convert(mat);
		frame.showImage(frameForDisplay);
	}

	public BufferedImage convertMatToBufferedImage(Mat mat) {
		Frame frame = converter.convert(mat);
		return c.convert(frame);
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public IplImage convertFromFrameToIplImage(Frame frame) {
		return converter.convert(frame);
	}

	public IplImage flipImage(IplImage img) {
		cvFlip(img, img, 1);// mirror
		return img;
	}

//	public void attachMouseListener() {
//		frame.getCanvas().addMouseListener(new MouseListener() {
//			@Override
//			public void mouseReleased(MouseEvent e) {
//			}
//
//			@Override
//			public void mousePressed(MouseEvent e) {
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) {
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e) {
//			}
//
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				int x = e.getX();
//				int y = e.getY();
//				clicked = new java.awt.Point(x, y);
//				System.out.println("enters");
//				System.out.println("X:" + x + " Y:" + y);
//			}
//		});
//	}

	public void drawAllROI(ArrayList<ROI> listRoi, Mat bgResult) {
		for (ROI roi : listRoi) {
			opencv_imgproc.rectangle(bgResult, roi.getRoi(), Scalar.WHITE, 1, 0, 0);
			opencv_imgproc.putText(bgResult, KeyEvent.getKeyText(roi.getKey().getKeyCode()),
					new Point(roi.getCoordinate().x, roi.getCoordinate().y), 1, 1, Scalar.WHITE);// upwards

			opencv_imgproc.putText(bgResult, KeyEvent.getKeyText(roi.getKey().getKeyCode()),
					new Point(roi.getCoordinate().x, roi.getCoordinate().y + 50), 1, 1, Scalar.WHITE);// downwards
		}

	}

}
