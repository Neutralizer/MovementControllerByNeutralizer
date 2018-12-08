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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.MovementDetector;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.main.MainMovement;
import bgSubtraction.properties.PropertiesOperations;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
@SuppressWarnings("serial")
public class MainPanel extends JFrame{

	UtilitiesPanel util = new UtilitiesPanel();
	private MovementDetector detector = new MovementDetector();//TODO this should not be here
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
	JSlider sliderHistory = new JSlider(1, 50, 1);
	JSlider sliderThresh = new JSlider(1, 100, 16);

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
		setSize(400, 400);
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
		
		sliderTemplate(panelForm, c,sliderErode1,"Erode",2,1);
		sliderTemplate(panelForm, c,sliderDilate2,"Dilate",2,1);
		sliderTemplate(panelForm, c,sliderHistory,"History",10,1);
		sliderTemplate(panelForm, c,sliderThresh,"Threshold",10,1);
		detector.changeErode(1);//TODO does not work/ or is overriden from movement detector object in mainpanel

		
		sliderErode1.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int erodeValue = (int)source.getValue();
					detector.changeErode(erodeValue);
				}
			}
		});
		
		sliderDilate2.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int dilateValue = (int)source.getValue();
					detector.changeDilate(dilateValue);

				}
			}
		});
		
		sliderHistory.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int historyValue = (int)source.getValue();
					detector.changeDilate(historyValue);
				}
			}
		});
		
		sliderThresh.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int threshValue = (int)source.getValue();
					detector.changeDilate(threshValue);
				}
			}
		});
		

		
		
		
		
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

	private void sliderTemplate(JPanel panelForm, GridBagConstraints c,JSlider slider, String labelName,int majorSpacing,int minorSpacing) {
		c.gridy++;
		c.anchor = GridBagConstraints.ABOVE_BASELINE;
		JLabel sliderLabel = new JLabel(labelName);
		panelForm.add(sliderLabel,c);
		c.gridy++;
		slider.setMajorTickSpacing(majorSpacing);
		slider.setMinorTickSpacing(minorSpacing);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		panelForm.add(slider,c);
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