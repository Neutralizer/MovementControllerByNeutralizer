package testInterface.droidTestPhoneToCamera;

import java.util.List;

import org.bytedeco.javacv.FrameGrabber.Exception;

import com.github.sarxos.webcam.Webcam;

/**
 * Displays all cameras attached
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class LoadAvailableWebcams{

	private int cameraNum;

	
	public static void main(String[] args) throws Exception {

		List<Webcam> list = Webcam.getWebcams();

		for (int i = 0; i < list.size(); i++) {
			Webcam w = list.get(i);
			System.out.println(w.getName());
		}

	}

}
