package interfacePanel.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import bgSubtraction.detector.movementDetector.ROI;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.keyboardControl.KeyPressType;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
@SuppressWarnings("serial")
public class KeyTable extends JTable {

	AllowedKeys allowedKeysObject;
	String[] columnNames;
	// KeyPressType[] keyPressType;
	ROIManipulator roi;
	String currentPropFile;
	JTable table;
	JComboBox<String> comboBoxKeyName;
	JComboBox<String> comboBoxKeyType;

	public KeyTable(ROIManipulator roi, String currentPropFile) {

		allowedKeysObject = new AllowedKeys();
		comboBoxKeyName = new JComboBox<String>(allowedKeysObject.getAllowedKeys());
		columnNames = new String[] { "Keyboard Key", "Square Location", "Key Type" };
		this.roi = roi;
		this.currentPropFile = currentPropFile;
		comboBoxKeyType = new JComboBox<String>(new String[] { KeyPressType.CONSTANT.toString(),
				KeyPressType.PRESS.toString(), KeyPressType.TOGGLE.toString() });
		// keyPressType = new KeyPressType[] { KeyPressType.CONSTANT,
		// KeyPressType.PRESS, KeyPressType.TOGGLE };
		// TODO get its index and transform for key creation

	}

	public void fill(GridBagConstraints c, JPanel panelForm) {
		// c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 10;

		Object[][] tableData = loadTableDataFromListRoi();
		table = new JTable(tableData, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(200, 100));
		table.setFillsViewportHeight(true);

		JScrollPane jsp = new JScrollPane(table);

		panelForm.add(jsp, c);// TODO remove the old table?
	}

	private Object[][] loadTableDataFromListRoi() {

		// System.out.println(Arrays.asList(keyPressType));// TODO debug
		// [CONSTANT, PRESS, TOGGLE]
		// System.out.println(keyPressType[1]);// TODO debug

		ArrayList<ROI> listRoi = roi.getListRoi();
		System.out.println(Arrays.asList(listRoi.get(0).getCoordinate()));// TODO debug
		System.out.println(Arrays.asList(listRoi.get(0).getKey().getKeyCode()));// TODO debug
		System.out.println(Arrays.asList(listRoi.get(0).getKey().getKeyPressType()));// TODO debug
		// [java.awt.Point[x=0,y=0]]
		// [27]
		// [PRESS]

		Object[][] data = populateMatrix(listRoi);

		// Object[][] data = { { "R", "150,250", "Press" }, { "T", "250,250", "Press" },
		// { "C", "0,100", "Press" } };

		return data;
	}

	private Object[][] populateMatrix(ArrayList<ROI> listRoi) {
		Object[][] result = new Object[listRoi.size()][columnNames.length];
		for (int i = 0; i < listRoi.size(); i++) {
			int code = listRoi.get(i).getKey().getKeyCode();
			result[i][0] = KeyEvent.getKeyText(code);

			Point loc = listRoi.get(i).getCoordinate();
			result[i][1] = loc.x + ", " + loc.y;
			result[i][2] = listRoi.get(i).getKey().getKeyPressType();
		}
		return result;
	}

}
