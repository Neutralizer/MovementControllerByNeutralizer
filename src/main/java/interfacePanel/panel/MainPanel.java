package interfacePanel.panel;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bytedeco.javacpp.opencv_videoio.VideoCapture;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.MovementDetector;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.keyboardControl.KeyController;
import bgSubtraction.keyboardControl.KeyPressType;
import bgSubtraction.keyboardControl.SpecialKey;
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
	MovementDetector detector = new MovementDetector();
	private String[] cameras;
//	Camera camera;
	ROIManipulator roi;
	PropertiesOperations prop;
	
	KeyTable kt;

	private JComboBox<String> comboBoxCamera;
	private JComboBox<String> comboBoxPresets;
	// = new JComboBox<String>(
	// new String[] { "config.properties", "quake.properties" });
	private JButton buttonStartCamera = new JButton("Start Camera");
	private JButton buttonCameraProperties = new JButton("Unstable:Camera Properties");
	// private JButton buttonLoadPreset= new JButton("Load Preset");
	String buttonCameraPropertiesHover = "Opens current camera properties - Experimental";
	String buttonCameraHover = "Starts the camera and the controller";
	String boxCameraHover = "Shows available cameras";
	String boxPropertiesHover = "Shows available presets as .properties files in current folder";
	String erodeHover = "Remove noise from camera input";
	String dilateHover = "Makes found pixels bigger";
	String historyHover = "Creates trail from detected pixels";
	String threshHover = "Controls main detection - more means less detection";
	JSlider sliderErode1 = new JSlider(1, 15, 5);
	JSlider sliderDilate2 = new JSlider(1, 15, 1);
	JSlider sliderHistory = new JSlider(1, 50, 1);
	JSlider sliderThresh = new JSlider(1, 100, 16);
	private boolean started = false;
	GridBagConstraints c;
	JPanel panelMain;
	JPanel panelForm;
	private boolean useWS = false;//TODO not dynamically changed
	

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
		setSize(500, 600);
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
		panelMain = new JPanel();
		getContentPane().add(panelMain);
		panelForm = new JPanel(new GridBagLayout());
		panelMain.add(panelForm);
		c= new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		c.anchor = GridBagConstraints.LINE_END;

		comboBoxCamera.setToolTipText(boxCameraHover);
		panelForm.add(comboBoxCamera, c);
		c.gridy++;
		comboBoxPresets.setToolTipText(boxPropertiesHover);
		panelForm.add(comboBoxPresets, c);
		
		//when preset is changed fire this
		comboBoxPresets.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
//				String selectedPropFile = comboBoxPresets.getSelectedItem().toString();
//				populateKeyTable(selectedPropFile,c,panelForm); //gridy 6
			}
		});
		
		sliderTemplate(panelForm, c,sliderErode1,"Erode", erodeHover,2,1);
		sliderTemplate(panelForm, c,sliderDilate2,"Dilate",dilateHover,2,1);
		sliderTemplate(panelForm, c,sliderHistory,"History",historyHover,10,1);
		sliderTemplate(panelForm, c,sliderThresh,"Threshold",threshHover,10,1);
		
		sliderErode1.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int)source.getValue();
					detector.changeErode(value);
				}
			}
		});
		
		sliderDilate2.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int)source.getValue();
					detector.changeDilate(value);

				}
			}
		});
		
		sliderHistory.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int)source.getValue();
					detector.changeHistory(value);
				}
			}
		});
		
		sliderThresh.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int)source.getValue();
					detector.changeSubTresh(value);
				}
			}
		});
		
		
		
		// 2nd column
		c.anchor = GridBagConstraints.LINE_START;
		
		c.gridx = 1;
		c.gridy = 0;
		buttonStartCamera.setToolTipText(buttonCameraHover);
		panelForm.add(buttonStartCamera, c);
		
		buttonStartCamera.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				startExecution();
			}
		});
		
		c.gridy++;
		buttonCameraProperties.setToolTipText(buttonCameraPropertiesHover);
		panelForm.add(buttonCameraProperties,c);
		
		buttonCameraProperties.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				VideoCapture v = null;
				try {
					v = new VideoCapture(util.getCameraNum(cameras,comboBoxCamera.getSelectedItem().toString()));
					v.set(37, 1);
					
				} catch (java.lang.Exception ex) {
				} finally {
//					v.close();
				}
			}
		});
		
		//TODO insert tick check for ws combo - boolean isAcive
//		String selectedPropFile = comboBoxPresets.getSelectedItem().toString();
//		populateKeyTable(selectedPropFile);

	}

//	protected void populateKeyTable(String selectedPropFile) {
//		
//		
////		if(started) {
//			c.gridx = 0;
//			c.gridy = 10;
////			kt.fill(selectedPropFile,c,panelForm);
//			String[] columnNames = new String[] {"Keyboard Key", "Square Location", "Key Type"};
//			JTable table;
//			Object[][] data = {
//				    {"R", "150,250",
//				     "Press"},
//				    {"T", "250,250",
//				     "Press"},
//				    {"C", "0,100",
//				     "Press"}
//				};
//			
//			table = new JTable(data, columnNames);
//			table.setPreferredScrollableViewportSize(new Dimension(200, 100));
//			table.setFillsViewportHeight(true);
//			
//			JScrollPane jsp = new JScrollPane(table);
////			table = new JTable(2, 2);
//			
//			panelForm.add(jsp, c);
//			this.pack();
////			panelForm.remove(jsp);
////			panelForm.add(new JScrollPane(table), c);
//
////		}
//		
//	}

	private void sliderTemplate(JPanel panelForm, GridBagConstraints c,JSlider slider, String labelName, String tooltip, int majorSpacing,int minorSpacing) {
		c.gridy++;
		c.anchor = GridBagConstraints.ABOVE_BASELINE;
		JLabel sliderLabel = new JLabel(labelName);
		sliderLabel.setToolTipText(tooltip);
		panelForm.add(sliderLabel,c);
		c.gridy++;
		slider.setMajorTickSpacing(majorSpacing);
		slider.setMinorTickSpacing(minorSpacing);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		panelForm.add(slider,c);
	}
	
	private void startExecution() {
		int cameraNum = util.getCameraNum(cameras,comboBoxCamera.getSelectedItem().toString());
		String selectedPropFile = comboBoxPresets.getSelectedItem().toString();
		Camera camera = new Camera(cameraNum);
		roi = new ROIManipulator(camera);
		prop = new PropertiesOperations(roi);//cpp way - give obj and populate it
		prop.loadPropertiesFileIntoInternalRoiManipulator(UtilitiesPanel.FILE_DIR, selectedPropFile);// "config.properties"

		if(useWS) {
		SpecialKey wsKey = new SpecialKey(KeyEvent.VK_DOLLAR, KeyPressType.SPECIAL);
		roi.addRoiToList(0, 0.52, wsKey);
		roi.addRoiToList(0.16, 0.96, 0.70, 0.04, KeyEvent.VK_W, KeyPressType.CONSTANT);// must be last
		}
		kt = new KeyTable(roi,selectedPropFile);
		
//		kt.fill(c, panelForm);
		kt.createTable(c,panelForm);
		this.pack();
		
		try {
			MainMovement.startAlgorithm(camera, selectedPropFile,detector, roi, useWS);
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
		prop.loadPropertiesFileIntoInternalRoiManipulator(UtilitiesPanel.FILE_DIR, comboBoxPresets.getSelectedItem().toString());
	}

}