package testInterface.panel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.properties.PropertiesOperations;

public class SelectButtonsFrame {

	// Display display;
	int cameraNum;
	private JFrame frame;
	Camera camera;
	ROIManipulator roi;
	PropertiesOperations prop;
	static String[] letters = { "Q", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K",
			"L", "Z", "X", "C", "V", "B", "N", "M" };
	// TODO array is from available buttons loaded from prop preset
	static String[] choose = { "Active", "Disabled" };

	final static JComboBox<String> comboBoxKey1 = new JComboBox<String>(letters);

	static JButton buttonStartCamera = new JButton("Start Camera");
	static JButton buttonLoadPreset = new JButton("Load Preset");
	static JButton selectSquareLocation = new JButton("Select Square Location");

	//loads available cameras
	private JComboBox<String> comboBoxCamera;
	final static JComboBox<String> comboBoxloadPresets = new JComboBox<String>(choose);

	// final static JLabel label = new JLabel("Choose potions!");

	public void initializeFrame() {
		// display = new Display();
		frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void initializeCamera() {
		// new String[] {"no camera"}//TODO set default
		String[] cameras = getAvailableCameras();
		comboBoxCamera = new JComboBox<String>(cameras);
	}

	//TODO this currently starts the camera
	public void initializePropertiesAfterCameraIsLoaded() {
		Camera camera = new Camera(cameraNum);
		roi = new ROIManipulator(camera);
		prop = new PropertiesOperations(roi);
		//TODO trigger with button press - choose filename from combobox
		prop.loadPropertiesFile("config.properties");// TODO prop
	}

	//TODO currently may be creating 2 objects (in prop - roi, camera) - may cause problems  
	public static void main(String[] args) throws AWTException {
		SelectButtonsFrame obj = new SelectButtonsFrame();
		obj.initializeCamera();
		obj.initializeFrame();
		obj.start();

		obj.initializePropertiesAfterCameraIsLoaded();

	}

	private int getSelectedCameraNum() {
		if (cameraNum > -1) {
			return cameraNum;
		}
		throw new IllegalStateException("Camera not initialized");
	}

	public void start() throws AWTException {

		JPanel panel = new JPanel(new GridBagLayout());
		// TODO add constraints dynamically
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);

		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(comboBoxKey1, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		// panel.add(comboBox2, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		// panel.add(comboBox3, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		// panel.add(comboBox4, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		panel.add(comboBoxCamera);
		// panel.add(comboBox5, constraints);
		panel.add(buttonStartCamera);

		frame.add(panel, BorderLayout.WEST);

		// TODO start the camera from the dropdown
		//TODO camera starts without asking
		buttonStartCamera.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				String s1 = comboBoxKey1.getSelectedItem().toString();
//				System.out.println(s1);
//				// TODO select camera num selected and pass it to prop-getlastindexInt -movetometod
//				cameraNum = comboBoxKey1.getSelectedItem().toString()
//						.codePointAt(comboBoxKey1.getSelectedItem().toString().length() - 1);
//
//				if (s1.equals("Active")) {
//					boolean temp = true;
//				}

			}
		});

		//TODO add preset without giving it camera
		buttonLoadPreset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO load preset with keys here - combobox
			}
		});
	}

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
