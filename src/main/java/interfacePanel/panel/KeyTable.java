package interfacePanel.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.bytedeco.javacv.CanvasFrame;

import bgSubtraction.detector.movementDetector.ROI;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.display.Display;
import bgSubtraction.keyboardControl.Key;
import bgSubtraction.keyboardControl.KeyPressType;
import bgSubtraction.properties.PropertiesOperations;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
@SuppressWarnings("serial")
public class KeyTable extends JTable {

	Display display;
	java.awt.Point clicked = null;
	AllowedKeys allowedKeysObject;
	String[] columnNames;
	ROIManipulator roi;
	PropertiesOperations prop;
	String folderPath;
	String currentPropFile;
	JTable table;
	DefaultTableModel model;
	Object[] row;

	JComboBox<String> comboBoxKeyName;
	JComboBox<String> comboBoxKeyType;
	JTextField locText;
	JButton btnAdd;
	JButton btnDelete;
	JButton btnUpdate;
	JButton btnSave;
	private String locationSelectionTooltip = "Click on the video feed to select square location";

	/**
	 * 
	 * @param display
	 *            - needed to get the coordinates from display for roi square
	 *            manipulation
	 * @param roi
	 * @param prop 
	 * @param currentPropFile
	 * @param selectedPropFile 
	 */
	public KeyTable(Display display, ROIManipulator roi, PropertiesOperations prop, String folderPath, String selectedPropFile) {

		this.display = display;
		allowedKeysObject = new AllowedKeys();
		comboBoxKeyName = new JComboBox<String>(allowedKeysObject.getAllowedKeys());
		columnNames = new String[] { "Keyboard Key", "Square Location", "Key Type" };
		this.roi = roi;
		this.prop = prop;
		this.folderPath = folderPath;
		this.currentPropFile = selectedPropFile;
		comboBoxKeyType = new JComboBox<String>(new String[] { KeyPressType.CONSTANT.toString(),
				KeyPressType.PRESS.toString(), KeyPressType.TOGGLE.toString() });
		attachMouseListener(display.getFrame());

	}

	// TODO maybe use observer pattern
	/**
	 *
	 * attaches listener to get square location from video feed frame
	 * 
	 * @param frame
	 */
	public void attachMouseListener(CanvasFrame frame) {
		frame.getCanvas().addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				clicked = new java.awt.Point(x, y);
				
				//TODO set only if point within range
				try {
					roi.isSizeWithinCameraRange(x, y, 40, 40);
					locText.setText(clicked.x + "," + clicked.y);
				} catch (IllegalStateException exception) {
					// TODO: handle exception
				}
				
				
				System.out.println("enters");// TOOD debug test
				System.out.println("X:" + x + " Y:" + y);
			}
		});
	}

	public void createTable(GridBagConstraints c, JPanel frame) {
		c.gridx = 0;
		c.gridy = 10;
		table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(400, 100));
		table.setFillsViewportHeight(true);

		/**
		 * anonymously overriding only 1 method
		 */
		model = new DefaultTableModel() {

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnIdentifiers(columnNames);

		table.setModel(model);

		table.setBackground(Color.LIGHT_GRAY);
		table.setForeground(Color.black);
		Font font = new Font("", 1, 12);// TODO choose appropriate font and size
		table.setFont(font);
		table.setRowHeight(30);

		locText = new JTextField();

		btnAdd = new JButton("Add");
		btnDelete = new JButton("Delete");
		btnUpdate = new JButton("Update");
		btnSave = new JButton("Save");


		JScrollPane pane = new JScrollPane(table);

		locText.setPreferredSize(new Dimension(70, 20));// TODO make it look bette r
		locText.setEditable(false);
		locText.setToolTipText(locationSelectionTooltip);

		frame.add(pane, c);

		c.gridx = 0;
		c.gridy = 15;
		frame.add(comboBoxKeyName, c);

		c.gridx = 0;
		c.gridy = 16;
		frame.add(locText, c);

		c.gridx = 0;
		c.gridy = 17;
		frame.add(comboBoxKeyType, c);

		// add JButtons to the jframe

		c.gridx = 0;
		c.gridy = 18;
		frame.add(btnAdd, c);

		c.gridx = 0;
		c.gridy = 19;
		frame.add(btnDelete, c);

		c.gridx = 0;
		c.gridy = 20;
		frame.add(btnUpdate, c);
		
		c.gridx = 0;
		c.gridy = 21;
		frame.add(btnSave, c);

		row = new Object[3];

		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				row[0] = comboBoxKeyName.getSelectedItem().toString();

				if (clicked != null) {
					row[1] = locText.getText();
				}

				row[2] = comboBoxKeyType.getSelectedItem().toString();

				// add row to the model
				model.addRow(row);

				// will add roi only if location is selected
				if (clicked != null) {
					int keyCode = allowedKeysObject.getKeyCode(row[0].toString());

					roi.addRoiToList(clicked.x, clicked.y, new Key(keyCode, KeyPressType.valueOf(row[2].toString())));
				}
			}
		});

		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// i = the index of the selected row
				int i = table.getSelectedRow();
				if (i >= 0) {
					// remove a row from jtable
					model.removeRow(i);
					roi.removeRoiFromList(i);// TODO test if indexes will not be the same
				} else {
					System.out.println("Delete Error");
				}
			}
		});

		// get selected row data From table to textfields
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				// i = the index of the selected row
				int i = table.getSelectedRow();
				if (i >= 0) {
					// keyText.setText(model.getValueAt(i, 0).toString());
					comboBoxKeyName.setSelectedItem(model.getValueAt(i, 0));
					if (model.getValueAt(i, 1) != null) {
						locText.setText(model.getValueAt(i, 1).toString());
					}
					// typeText.setText(model.getValueAt(i, 2).toString());//TODO check if next
					// works
					comboBoxKeyType.setSelectedItem(model.getValueAt(i, 2));
				}
			}
		});

		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// i = the index of the selected row
				int i = table.getSelectedRow();

				if (i >= 0) {

					model.setValueAt(comboBoxKeyName.getSelectedItem().toString(), i, 0);
					if (clicked != null) {
						model.setValueAt(locText.getText(), i, 1);

					}
					model.setValueAt(comboBoxKeyType.getSelectedItem().toString(), i, 2);

					if (clicked != null) {
						int keyCode = allowedKeysObject.getKeyCode(comboBoxKeyName.getSelectedItem().toString());

						roi.updateRoiFromList(i, clicked.x, clicked.y, keyCode,
								KeyPressType.valueOf(comboBoxKeyType.getSelectedItem().toString()));
					}
				} else {
					System.out.println("Update Error");
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				prop.saveRoiToPropFile(null,currentPropFile);//TODO changed to current dir
				
			}
		});

		loadTableDataFromListRoi();
	}

	private void loadTableDataFromListRoi() {
		populateMatrixDropdown(roi.getListRoi());
	}

	/**
	 * 
	 * @param listRoi
	 *            {@link bgSubtraction.detector.movementDetector.ROI#ROI(Point coordinate, Key key)}
	 *            </br>
	 *            {@link bgSubtraction.keyboardControl.Key#Key(int keyCode, KeyPressType keyPressType)}
	 * @return
	 */
	private void populateMatrixDropdown(ArrayList<ROI> listRoi) {
		for (int i = 0; i < listRoi.size(); i++) {
			Object[] row = new Object[3];

			int keyCode = listRoi.get(i).getKey().getKeyCode();
			String currentKey = KeyEvent.getKeyText(keyCode);
			row[0] = currentKey;

			Point loc = listRoi.get(i).getCoordinate();
			row[1] = loc.x + "," + loc.y;

			row[2] = listRoi.get(i).getKey().getKeyPressType();

			// add row to the model
			model.addRow(row);
		}
	}
}
