package bgSubtraction.display;

import static org.bytedeco.javacpp.opencv_core.cvFlip;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
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

	public Display() {
		converter = new OpenCVFrameConverter.ToIplImage();
		frame = createNewFrame("Movement", new java.awt.Point(0, 0));
		frame.setSize(640, 480);
	}

	public CanvasFrame getMovementFrame() {
		return this.frame;
	}

	public CanvasFrame createNewFrame(String name, java.awt.Point location) {
		frame = new CanvasFrame(name);
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		frame.setLocation(location);
		return frame;
	}

	/**
	 * Actual
	 * 
	 * @param canvasFrame
	 * @param mat
	 */
	public void showImage(CanvasFrame canvasFrame, Mat mat) {
		Frame frame = converter.convert(mat);
		canvasFrame.showImage(frame);
	}

	/**
	 * When the title of the frame needs to be updated every frame
	 * 
	 * @param canvasFrame
	 * @param mat
	 * @param title
	 */
	public void showImage(CanvasFrame canvasFrame, Mat mat, String title) {
		Frame frame = converter.convert(mat);
		canvasFrame.setTitle(title);
		canvasFrame.showImage(frame);
	}

	public void showImage(CanvasFrame canvasFrame, IplImage img) {
		Frame frame = converter.convert(img);
		canvasFrame.showImage(frame);
	}

	public IplImage convertFromFrameToIplImage(Frame frame) {
		return converter.convert(frame);
	}

	public IplImage flipImage(IplImage img) {
		cvFlip(img, img, 1);// mirror
		return img;
	}

	/**
	 * when the canvasFrame is in the correct thread, this will work<br>
	 * uses mouse listener
	 * 
	 * @param frame
	 * @return
	 */
	public Point testGetCoordinatesOfMouseClickedInFrame(CanvasFrame frame) {

		frame.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent me) {
			}

			public void mouseReleased(MouseEvent me) {
			}

			public void mouseEntered(MouseEvent me) {
			}

			public void mouseExited(MouseEvent me) {
			}

			public void mouseClicked(MouseEvent me) {
				int x = me.getX();
				int y = me.getY();
				System.out.println("X:" + x + " Y:" + y);
			}
		});

		return null;

	}

	/**
	 * when the canvasFrame is in the correct thread, this will work<br>
	 * uses mouseAdapter
	 * 
	 * @param frame
	 * @return
	 */
	public Point testClick(CanvasFrame frame) {
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				System.out.println("X:" + x + " Y:" + y);
			}
		});
		return null;
	}

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
