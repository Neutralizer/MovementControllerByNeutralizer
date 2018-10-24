package testInterface.cameraTest.frameTest;

import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PotionDrinkerDeluxe {

	static Robot r;

	static String[] choose = { "Active", "Disabled" };
	final static JComboBox comboBox1 = new JComboBox(choose);
	final static JComboBox comboBox2 = new JComboBox(choose);
	final static JComboBox comboBox3 = new JComboBox(choose);
	final static JComboBox comboBox4 = new JComboBox(choose);
	final static JComboBox comboBox5 = new JComboBox(choose);
	static JButton b = new JButton("Aplly");
	final static JLabel label = new JLabel("Choose potions!");

	private static boolean potion1 = false;
	private static boolean potion2 = false;
	private static boolean potion3 = false;
	private static boolean potion4 = false;
	private static boolean potion5 = false;

	public static void main(String[] args) throws AWTException {
		JFrame f = new JFrame();
		f.setVisible(true);
		f.setSize(600, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		c.gridx = 0;
		c.gridy = 1;
		p.add(label, c);
		p.add(comboBox1, c);

		c.gridx = 0;
		c.gridy = 2;
		p.add(label, c);
		p.add(comboBox2, c);

		c.gridx = 0;
		c.gridy = 3;
		p.add(label, c);
		p.add(comboBox3, c);

		c.gridx = 0;
		c.gridy = 4;
		p.add(label, c);
		p.add(comboBox4, c);

		c.gridx = 0;
		c.gridy = 5;
		p.add(label);
		p.add(comboBox5, c);
		p.add(b);

		f.add(p);

		b.addActionListener(new ActionListener() {

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

		r = new Robot();
		r.setAutoDelay(20);
		r.setAutoWaitForIdle(true);
		//pot 1-5 populated - execute the action
		
		//prev - if user pressed - drink pot

	}

}
