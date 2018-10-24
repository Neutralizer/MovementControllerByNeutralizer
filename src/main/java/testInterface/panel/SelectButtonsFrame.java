package testInterface.panel;

import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectButtonsFrame {

	// TODO array is from available buttons loaded from prop preset
	static String[] choose = { "Active", "Disabled" };
	final static JComboBox<String> comboBox1 = new JComboBox<String>(choose);
	final static JComboBox<String> comboBox2 = new JComboBox<String>(choose);
	final static JComboBox<String> comboBox3 = new JComboBox<String>(choose);
	final static JComboBox<String> comboBox4 = new JComboBox<String>(choose);
	final static JComboBox<String> comboBox5 = new JComboBox<String>(choose);
	static JButton buttonStartCamera = new JButton("Start Camera");
	static JButton buttonLoadPreset = new JButton("Load Preset");

	// TODO load available camera names - in constructor
	final static JComboBox<String> comboBoxCamera = new JComboBox<String>(choose);
	final static JComboBox<String> comboBoxloadPresets = new JComboBox<String>(choose);

	// final static JLabel label = new JLabel("Choose potions!");

	private static boolean potion1 = false;
	private static boolean potion2 = false;
	private static boolean potion3 = false;
	private static boolean potion4 = false;
	private static boolean potion5 = false;

	public static void main(String[] args) throws AWTException {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridBagLayout());
		// TODO add constraints dynamically
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);

		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(comboBoxCamera, constraints);
		panel.add(comboBox1, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(comboBoxCamera, constraints);
		panel.add(comboBox2, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(comboBoxCamera, constraints);
		panel.add(comboBox3, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		panel.add(comboBoxCamera, constraints);
		panel.add(comboBox4, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		panel.add(comboBoxCamera);
		panel.add(comboBox5, constraints);
		panel.add(buttonStartCamera);

		frame.add(panel);

		// TODO starts the selected camera
		buttonStartCamera.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String s1 = comboBox1.getSelectedItem().toString();
				String s2 = comboBox2.getSelectedItem().toString();
				String s3 = comboBox3.getSelectedItem().toString();
				String s4 = comboBox4.getSelectedItem().toString();
				String s5 = comboBox5.getSelectedItem().toString();

				if (s1.equals("Active")) {
					potion1 = true;
				}
				if (s2.equals("Active")) {
					potion2 = true;
				}
				if (s3.equals("Active")) {
					potion3 = true;
				}
				if (s4.equals("Active")) {
					potion4 = true;
				}
				if (s5.equals("Active")) {
					potion5 = true;
				}

			}
		});

		buttonLoadPreset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//TODO load preset
			}
		});
	}
}
