package testInterface.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.properties.PropertiesOperations;

@SuppressWarnings("serial")
public class MainPanel extends JFrame {

	UtilitiesPanel util = new UtilitiesPanel();
	int cameraNum;
	// private JFrame frame;
	Camera camera;
	ROIManipulator roi;
	PropertiesOperations prop;

	private JComboBox<String> comboBoxCamera;
	private JComboBox<String> comboBoxPresets;
	// = new JComboBox<String>(
	// new String[] { "config.properties", "quake.properties" });
	private JButton buttonStartCamera = new JButton("Start Camera");
	// private JButton buttonLoadPreset= new JButton("Load Preset");

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainPanel().setVisible(true);
			}
		});
	}

	public MainPanel() {
		loadCameras();
		loadPresets();
		createView();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 200);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	private void loadPresets() {
		String[] presets = util.listFile(UtilitiesPanel.FILE_DIR, UtilitiesPanel.FILE_TEXT_EXT);
		comboBoxPresets = new JComboBox<String>(presets);

	}

	public void loadCameras() {
		String[] cameras = util.getAvailableCameras();
		cameras = removeCameraNumbers(cameras);
		comboBoxCamera = new JComboBox<String>(cameras);
	}

	private String[] removeCameraNumbers(String[] cameras) {
		for (int i = 0; i < cameras.length; i++) {
			cameras[i] = cameras[i].substring(0, cameras[i].length() - 2);
		}
		return cameras;
	}

	private void createView() {
		JPanel panelMain = new JPanel();
		getContentPane().add(panelMain);
		JPanel panelForm = new JPanel(new GridBagLayout());
		panelMain.add(panelForm);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		c.anchor = GridBagConstraints.LINE_END;

		panelForm.add(comboBoxCamera, c);
		c.gridy++;
		panelForm.add(comboBoxPresets, c);

		c.anchor = GridBagConstraints.LINE_START;

		c.gridx = 1;
		c.gridy = 0;
		panelForm.add(buttonStartCamera, c);

		buttonStartCamera.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				initializePropertiesAfterCameraIsLoaded();

			}
		});

	}

	public void initializePropertiesAfterCameraIsLoaded() {
		int cameraNum = getCameraNum();
		Camera camera = new Camera(cameraNum);
		roi = new ROIManipulator(camera);
		prop = new PropertiesOperations(roi);
		prop.loadPropertiesFile(UtilitiesPanel.FILE_DIR, comboBoxPresets.getSelectedItem().toString());
	}

	private int getCameraNum() {
		String cameraName = comboBoxCamera.getSelectedItem().toString();
		char camNum = cameraName.charAt(cameraName.length() - 1);
		int cameraNum = Integer.valueOf(String.valueOf(camNum));
		return cameraNum;
	}

}
