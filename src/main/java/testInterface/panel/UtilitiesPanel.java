package testInterface.panel;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.sarxos.webcam.Webcam;

public class UtilitiesPanel {

	public static final String FILE_DIR = "c:\\MovementController\\";
	public static final String FILE_TEXT_EXT = ".properties";

	public String[] listFile(String folder, String ext) {

		GenericExtFilter filter = new GenericExtFilter(ext);

		File dir = new File(folder);

		if (dir.isDirectory() == false) {
			System.out.println("Directory does not exists : " + FILE_DIR);
			return new String[0];
		}

		String[] list = dir.list(filter);

		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return list;
		}

		// TODO remove sysout
		for (String file : list) {
			String temp = new StringBuffer(FILE_DIR).append(File.separator).append(file).toString();
			System.out.println("file : " + temp);
		}
		return list;
	}

	public class GenericExtFilter implements FilenameFilter {

		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}

		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}

	public Map<String, Integer> populateCameraMap() {
		Map<String, Integer> cameras = new TreeMap<String, Integer>();
		String[] cameraNames = getAvailableCameras();

		for (int i = 0; i < cameraNames.length; i++) {
			String cameraName = cameraNames[i].substring(0, cameraNames[i].length() - 2);
			char camNum = cameraNames[i].charAt(cameraNames[i].length() - 1);
			int cameraNum = Integer.valueOf(String.valueOf(camNum));
			cameras.put(cameraName, cameraNum);
		}
		return cameras;

	}

	//TODO make private and work with the map
	public String[] getAvailableCameras() {
		List<Webcam> list = Webcam.getWebcams();
		String[] result = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			Webcam w = list.get(i);
			result[i] = w.getName();
		}
		return result;
	}

}
