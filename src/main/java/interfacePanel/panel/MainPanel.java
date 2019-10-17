package interfacePanel.panel;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bytedeco.javacpp.opencv_videoio.VideoCapture;

import bgSubtraction.camera.Camera;
import bgSubtraction.detector.movementDetector.MovementDetector;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.display.Display;
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
public class MainPanel extends JFrame {

	UtilitiesPanel util = new UtilitiesPanel();
	MovementDetector detector = new MovementDetector();
	private String[] cameras;
	Camera camera;
	ROIManipulator roi;
	PropertiesOperations prop;

	KeyTable kt;

	private JComboBox<String> comboBoxCamera;
	private JComboBox<String> comboBoxPresets;

	private JButton buttonStartCamera = new JButton("Start Camera");
	private JButton buttonCameraProperties = new JButton("UNSTABLE:Camera Properties");

	String buttonCameraPropertiesHover = "Opens current camera properties. It is advised "
			+ "after accessing the properties to start the camera, or it may become locked";
	String buttonCameraHover = "Starts the camera and the controller";
	String boxCameraHover = "Shows available cameras";
	String boxPropertiesHover = "Shows available presets as .properties files in current folder";
	String erodeHover = "Remove noise from camera input. More means less noise.";
	String dilateHover = "Makes found pixels bigger";
	String historyHover = "Creates trail from detected pixels. Also removes noise.";
	String threshHover = "Controls main detection. More means less detection";
	String blurHover = "Blurs the image to reduce noise";
	String limiterHover = "When there is too much detection on the screen the buttons will not be pressed. "
			+ "At 50 - when there is more than 50% white on the screen the buttons will not be pressed."
			+ "To disable the limiter set it to 100%";

	JSlider sliderBlur = new JSlider(1, 19, 5);
	JSlider sliderErode1 = new JSlider(1, 15, 3);
	JSlider sliderDilate2 = new JSlider(1, 15, 3);
	JSlider sliderHistory = new JSlider(1, 50, 4);
	JSlider sliderThresh = new JSlider(1, 100, 10);
	JSlider sliderLimiter = new JSlider(50, 100, 75);

	GridBagConstraints c;
	JPanel panelMain;
	JPanel panelForm;
	private boolean started = false;
	private boolean useWS = false;// TODO not dynamically changed

	public static void main(String[] args) {
		org.bytedeco.javacpp.avutil.av_log_set_level(org.bytedeco.javacpp.avutil.AV_LOG_QUIET);// disable javacv logging
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

		addCloseListener(this);

		setSize(500, 600);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	private void addCloseListener(Frame frame) {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (started) {
					camera.releaseCurrentCamera();// TODO throws those cpp errors - try first or stop the main loop
													// first
					KeyController.unpressAllRoiButtons(roi.getListRoi());
					// try {
					// Thread.sleep(100);
					// } catch (InterruptedException e1) {
					// // TODO Auto-generated catch block
					// e1.printStackTrace();
					// }

				}
				System.exit(0);
			}
		});
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
		// frame.setLayout(null);//TODO maybe will look better?
		panelMain = new JPanel();
		getContentPane().add(panelMain);
		panelForm = new JPanel(new GridBagLayout());
		panelMain.add(panelForm);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		c.anchor = GridBagConstraints.LINE_END;

		comboBoxCamera.setToolTipText(boxCameraHover);
		panelForm.add(comboBoxCamera, c);
		c.gridy++;
		comboBoxPresets.setToolTipText(boxPropertiesHover);
		panelForm.add(comboBoxPresets, c);

		// when preset is changed fire this
		comboBoxPresets.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// String selectedPropFile = comboBoxPresets.getSelectedItem().toString();
				// populateKeyTable(selectedPropFile,c,panelForm); //gridy 6
			}
		});

		sliderTemplate(panelForm, c, sliderBlur, "Blur", blurHover, 10, 2);
		sliderTemplate(panelForm, c, sliderErode1, "Erode", erodeHover, 2, 1);
		sliderTemplate(panelForm, c, sliderDilate2, "Dilate", dilateHover, 2, 1);
		sliderTemplate(panelForm, c, sliderHistory, "History", historyHover, 10, 1);
		sliderTemplate(panelForm, c, sliderThresh, "Threshold", threshHover, 10, 1);
		sliderTemplate(panelForm, c, sliderLimiter, "Limiter", limiterHover, 10, 5);

		sliderBlur.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int) source.getValue();
					if (value % 2 == 0) {// need only odd numbers
						value = value - 1;
					}
					detector.changeBlur(value);
				}
			}
		});

		sliderErode1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int) source.getValue();
					detector.changeErode(value);
				}
			}
		});

		sliderDilate2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int) source.getValue();
					detector.changeDilate(value);

				}
			}
		});

		sliderHistory.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int) source.getValue();
					detector.changeHistory(value);
				}
			}
		});

		sliderThresh.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int) source.getValue();
					detector.changeSubTresh(value);
				}
			}
		});
		
		sliderLimiter.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int value = (int) source.getValue();
					detector.changeDetectionLimit(value);
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
		panelForm.add(buttonCameraProperties, c);

		buttonCameraProperties.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				VideoCapture v = null;
				try {
					v = new VideoCapture(util.getCameraNum(cameras, comboBoxCamera.getSelectedItem().toString()));
					v.set(37, 1);// 37 - properties

				} catch (java.lang.Exception ex) {
					// TODO logger
				} finally {
					if (v.isOpened()) {
						v.release();
					}
					v.close();
				}
			}
		});

		// TODO insert tick check for ws combo - boolean isAcive
		// String selectedPropFile = comboBoxPresets.getSelectedItem().toString();
		// populateKeyTable(selectedPropFile);

	}

	private void sliderTemplate(JPanel panelForm, GridBagConstraints c, JSlider slider, String labelName,
			String tooltip, int majorSpacing, int minorSpacing) {
		c.gridy++;
		c.anchor = GridBagConstraints.ABOVE_BASELINE;
		JLabel sliderLabel = new JLabel(labelName);
		sliderLabel.setToolTipText(tooltip);
		panelForm.add(sliderLabel, c);
		c.gridy++;
		slider.setMajorTickSpacing(majorSpacing);
		slider.setMinorTickSpacing(minorSpacing);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		panelForm.add(slider, c);
	}

	private void startExecution() {
		comboBoxCamera.setEnabled(false);
		comboBoxPresets.setEnabled(false);
		buttonStartCamera.setEnabled(false);
		buttonCameraProperties.setEnabled(false);

		int cameraNum = util.getCameraNum(cameras, comboBoxCamera.getSelectedItem().toString());
		String selectedPropFile = comboBoxPresets.getSelectedItem().toString();
		camera = new Camera(cameraNum);
		roi = new ROIManipulator(camera);
		Display display = new Display(camera, roi);
		prop = new PropertiesOperations(roi);// cpp way - give obj and populate it
		prop.loadPropertiesFileIntoInternalRoiManipulator(selectedPropFile);// "config.properties"
		started = true;

		if (useWS) {// TODO dropped to deprecated status
			SpecialKey wsKey = new SpecialKey(KeyEvent.VK_DOLLAR, KeyPressType.SPECIAL);
			roi.addRoiToList(0, 0.52, wsKey);
			roi.addRoiToList(0.16, 0.96, 0.70, 0.04, KeyEvent.VK_W, KeyPressType.CONSTANT);// must be last
		}
		kt = new KeyTable(display, roi, prop, selectedPropFile);

		kt.createTable(c, panelForm);
		this.pack();

		MainMovement.startAlgorithm(camera, display, selectedPropFile, detector, roi, useWS);

	}

	@Deprecated
	public void initializePropertiesAfterCameraIsLoaded() {
		int cameraNum = util.getCameraNum(cameras, comboBoxCamera.getSelectedItem().toString());
		Camera camera = new Camera(cameraNum);
		roi = new ROIManipulator(camera);
		prop = new PropertiesOperations(roi);
		// prop.loadPropertiesFileIntoInternalRoiManipulator(UtilitiesPanel.FILE_DIR,
		// comboBoxPresets.getSelectedItem().toString());
	}

}