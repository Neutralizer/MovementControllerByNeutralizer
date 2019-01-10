package interfacePanel.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;

import bgSubtraction.detector.movementDetector.ROIManipulator;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
@SuppressWarnings("serial")
public class KeyTable extends JTable{
	
	String[] columnNames;
	ROIManipulator roi;
	JTable table;
	
	public KeyTable(ROIManipulator roi){
		columnNames = new String[] {"Keyboard Key", "Square Location", "Key Type"};
		this.roi = roi; 
	}
	
	public void fill(String selectedPropFile, GridBagConstraints c, JPanel panelForm) {
//		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 10;
		
		Object[][] data = {
			    {"R", "150,250",
			     "Press"},
			    {"T", "250,250",
			     "Press"},
			    {"C", "0,100",
			     "Press"}
			};
		
		table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(200, 100));
		table.setFillsViewportHeight(true);
		
		JScrollPane jsp = new JScrollPane(table);
		
		panelForm.add(jsp, c);
	}

}
