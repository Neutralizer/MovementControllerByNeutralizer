package interfacePanel.cameraTest.frameTest;

import java.awt.BorderLayout;
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

public class Potioner3 {
	
	static String[] choose = { "Active", "Disabled"};
	final static JComboBox comboBox1 = new JComboBox(choose);
	final static JComboBox comboBox2 = new JComboBox(choose);
	final static JComboBox comboBox3 = new JComboBox(choose);
	final static JComboBox comboBox4 = new JComboBox(choose);
	final static JComboBox comboBox5 = new JComboBox(choose);
	static JButton b = new JButton("Aplly");
	final static JLabel l = new JLabel("Choose potions!");
	final static JLabel label = new JLabel("App");
	
	public static void main(String[] args) {
		
		
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(10, 10, 10, 10);
		
		c.gridx = 0;
		c.gridy = 1;
		p.add(l,c);
		p.add(comboBox1,c);
		
		
		c.gridx = 0;
		c.gridy = 2;
		p.add(l,c);
		p.add(comboBox2,c);
		
		
		c.gridx = 0;
		c.gridy = 3;
		p.add(l,c);
		p.add(comboBox3,c);
		
		
		c.gridx = 0;
		c.gridy = 4;
		p.add(l,c);
		p.add(comboBox4,c);
		
		
		c.gridx = 0;
		c.gridy = 5;
		p.add(l);
		p.add(comboBox5,c);
		p.add(b);
		
		
		
		
		frame.add(p);
		
		b.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String s = comboBox5.getSelectedItem().toString();
				l.setText(s);
				
			}
		});
		
		
		

	}

}
