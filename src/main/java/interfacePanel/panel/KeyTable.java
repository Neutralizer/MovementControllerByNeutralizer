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
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import bgSubtraction.detector.movementDetector.ROI;
import bgSubtraction.detector.movementDetector.ROIManipulator;
import bgSubtraction.keyboardControl.Key;
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

	
	public void createTable(GridBagConstraints c,JPanel frame) {
		c.gridx = 0;
		c.gridy = 10;
        table = new JTable(); 
        table.setPreferredScrollableViewportSize(new Dimension(400, 100));
		table.setFillsViewportHeight(true);
        
        final DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        
        table.setModel(model);
        
        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.black);
        Font font = new Font("",1,12);//TODO choose appropriate font and size
        table.setFont(font);
        table.setRowHeight(30);

        final JTextField locText= new JTextField();
        
//        JButton locText = new JButton("Choose Location");//TODO change variable
        
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnUpdate = new JButton("Update");   
        
        JScrollPane pane = new JScrollPane(table);
        
//        keyText.setPreferredSize(new Dimension(40, 20));
        
        frame.add(pane,c);
        
        c.gridx = 0;
		c.gridy = 15;
        frame.add(comboBoxKeyName,c);
        
        c.gridx = 0;
		c.gridy = 16;
        frame.add(locText,c); // TODO replace with key to get the loc
        
        c.gridx = 0;
		c.gridy = 17;
        frame.add(comboBoxKeyType,c);
    
        // add JButtons to the jframe
        
        c.gridx = 0;
		c.gridy = 18;
        frame.add(btnAdd,c);
        
        c.gridx = 0;
		c.gridy = 19;
        frame.add(btnDelete,c);
        
        c.gridx = 0;
		c.gridy = 20;
        frame.add(btnUpdate,c);
        
        final Object[] row = new Object[3];
        
        btnAdd.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
             
//                row[0] = keyText.getText();
            	row[0] = comboBoxKeyName.getSelectedItem().toString();
            	
                row[1] = locText.getText();
//                row[2] = typeText.getText();//TODO check if next works
                row[2] = comboBoxKeyType.getSelectedItem().toString();//get name or arr index
                
                // add row to the model
                model.addRow(row);
            }
        });
        
        btnDelete.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
            
                // i = the index of the selected row
                int i = table.getSelectedRow();
                if(i >= 0){
                    // remove a row from jtable
                    model.removeRow(i);
                }
                else{
                    System.out.println("Delete Error");
                }
            }
        });
        
        // get selected row data From table to textfields 
        table.addMouseListener(new MouseAdapter(){
        
        @Override
        public void mouseClicked(MouseEvent e){
            
            // i = the index of the selected row
            int i = table.getSelectedRow();
            if(i >= 0) {
//            keyText.setText(model.getValueAt(i, 0).toString());
            comboBoxKeyName.setSelectedItem(model.getValueAt(i, 0));
            	
            locText.setText(model.getValueAt(i, 1).toString());
//            typeText.setText(model.getValueAt(i, 2).toString());//TODO check if next works
            comboBoxKeyType.setSelectedItem(model.getValueAt(i, 2));
            }
        }
        });
        
        btnUpdate.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
             
                // i = the index of the selected row
                int i = table.getSelectedRow();
                
                if(i >= 0) {
//                   model.setValueAt(keyText.getText(), i, 0);
                	
                	model.setValueAt(comboBoxKeyName.getSelectedItem().toString(), i, 0);
                   model.setValueAt(locText.getText(), i, 1);
//                   model.setValueAt(typeText.getText(), i, 2);//TODO check if next works
                   model.setValueAt(comboBoxKeyType.getSelectedItem().toString(), i, 2);
                }
                else{
                    System.out.println("Update Error");
                }
            }
        });
        
	}
	
	public void fill(GridBagConstraints c, JPanel panelForm) {
//		c.anchor = GridBagConstraints.SOUTH;
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

		ArrayList<ROI> listRoi = roi.getListRoi();
		System.out.println(Arrays.asList(listRoi.get(0).getCoordinate()));// TODO debug
		System.out.println(Arrays.asList(listRoi.get(0).getKey().getKeyCode()));// TODO debug
		System.out.println(Arrays.asList(listRoi.get(0).getKey().getKeyPressType()));// TODO debug

		Object[][] data = populateMatrixDropdown(listRoi);

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

	
	
	/**
	 * 
	 * @param listRoi {@link bgSubtraction.detector.movementDetector.ROI#ROI(Point coordinate, Key key)}
	 * </br>
	 * {@link bgSubtraction.keyboardControl.Key#Key(int keyCode, KeyPressType keyPressType)}
	 * @return
	 */
	private Object[][] populateMatrixDropdown(ArrayList<ROI> listRoi) {
		Object[][] result = new Object[listRoi.size()][columnNames.length];
		for (int i = 0; i < listRoi.size(); i++) {
			int code = listRoi.get(i).getKey().getKeyCode();
			String currentKey = KeyEvent.getKeyText(code);
			//TODO perform check if key is valid here	
			JComboBox<String> comboBox = new JComboBox<String>(allowedKeysObject.getAllowedKeys());
//			comboBox.setSelectedItem(currentKey);
			result[i][0] = comboBox;

			Point loc = listRoi.get(i).getCoordinate();
			result[i][1] = loc.x + ", " + loc.y;
			result[i][2] = listRoi.get(i).getKey().getKeyPressType();
		}
		return result;
	}//TODO set the combobox to letter from roi - for location - hidden
	//TODO new combobox in array for every cell
}
