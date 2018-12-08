package interfacePanel.panel;

import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.main.MainMovement;
import bgSubtraction.properties.PropertiesOperations;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
@SuppressWarnings("serial")
public class MainPanel extends JFrame {

	UtilitiesPanel util = new UtilitiesPanel();
	private String[] cameras;
	// private JFrame frame;//TODO to be removed
	Camera camera;
	ROIManipulator roi;
	PropertiesOperations prop;

	private JComboBox<String> comboBoxCamera;
	private JComboBox<String> comboBoxPresets;
	// = new JComboBox<String>(
	// new String[] { "config.properties", "quake.properties" });
	private JButton buttonStartCamera = new JButton("Start Camera");
	// private JButton buttonLoadPreset= new JButton("Load Preset");
	JSlider sliderErode1 = new JSlider(1, 15, 5);
	JSlider sliderDilate2 = new JSlider(1, 15, 1);
	JSlider sliderTest;

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
		String[] presets = util.listFile(UtilitiesPanel.FILE_DIR);
		comboBoxPresets = new JComboBox<String>(presets);

	}

	public void loadCameras() {
		cameras = util.getAvailableCameras();
		comboBoxCamera = new JComboBox<String>(cameras);
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
		
		c.gridy++;
		c.anchor = GridBagConstraints.ABOVE_BASELINE;
		JLabel sliderLabel = new JLabel("Erode");
		panelForm.add(sliderLabel,c);
		c.gridy++;
		sliderErode1.setMajorTickSpacing(2);
		sliderErode1.setMinorTickSpacing(1);
		sliderErode1.setPaintTicks(true);
		sliderErode1.setPaintLabels(true);
		panelForm.add(sliderErode1,c);
		
		
		
		// 2nd column
		c.anchor = GridBagConstraints.LINE_START;
		
		c.gridx = 1;
		c.gridy = 0;
		panelForm.add(buttonStartCamera, c);
		
		buttonStartCamera.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				startAlgorithm();

			}
		});

	}
	
	private void startAlgorithm() {
		int cameraNum = util.getCameraNum(cameras,comboBoxCamera.getSelectedItem().toString());
		String selectedPropFile = comboBoxPresets.getSelectedItem().toString();
		try {
			MainMovement.startAlgorithm(cameraNum, selectedPropFile);
		} catch (AWTException e) {
			e.printStackTrace();//TODO show which is the error
		}
		
	}

	@Deprecated
	public void initializePropertiesAfterCameraIsLoaded() {
		int cameraNum = util.getCameraNum(cameras,comboBoxCamera.getSelectedItem().toString());
		Camera camera = new Camera(cameraNum);
		roi = new ROIManipulator(camera);
		prop = new PropertiesOperations(roi);
		prop.loadPropertiesFile(UtilitiesPanel.FILE_DIR, comboBoxPresets.getSelectedItem().toString());
	}

}
