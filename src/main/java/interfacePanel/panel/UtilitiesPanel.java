package interfacePanel.panel;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import com.github.sarxos.webcam.Webcam;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class UtilitiesPanel {

	public static final String FILE_DIR = System.getProperty("user.dir");
	public static final String FILE_TEXT_EXT = ".properties";

	public String[] listFile(String folder) {

		GenericExtFilter filter = new GenericExtFilter(FILE_TEXT_EXT);

		File dir = new File(folder);

		if (dir.isDirectory() == false) {
			//dir does not exist
			return new String[0];
		}

		String[] list = dir.list(filter);

		if (list.length == 0) {
			//no files found
			return list;//TODO throw 
		}

		return list;
	}

	private class GenericExtFilter implements FilenameFilter {

		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}

	public String[] getAvailableCameras() {
		List<Webcam> list = Webcam.getWebcams();
		String[] result = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			Webcam w = list.get(i);
			String cameraName = w.getName();
			result[i] = cameraName.substring(0, cameraName.length() - 2);
		}
		return result;
	}

	/**
	 * Camera num corresponds to the order of the camera name in the cameras
	 * array<br>
	 * Ex: camera num 0 is at the 0 position in the array
	 * @param cameras 
	 * 
	 * @param cameraName
	 * @return
	 */
	protected int getCameraNum(String[] cameras, String cameraName) {
		if (cameras.length == 0) {
			throw new IllegalStateException("No connected camera");
		}
		for (int i = 0; i < cameras.length; i++) {
			if (cameraName.equals(cameras[i])) {
				return i;
			}
		}
		return -1;
	}

}
