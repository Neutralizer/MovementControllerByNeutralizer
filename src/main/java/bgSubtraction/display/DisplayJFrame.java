package bgSubtraction.display;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class DisplayJFrame {

	
	private JFrame jFrame;

	public DisplayJFrame() {
		jFrame = new JFrame("Movement");
		jFrame.setSize(640, 480);
	}
	
	public JFrame getTestFrame() {
		return this.jFrame;
	}
	
	public void add() {
		final JTextField text = new JTextField();
		jFrame.add(text,BorderLayout.SOUTH);
		jFrame.addMouseListener(new MouseListener() {
	        public void mousePressed(MouseEvent me) { }
	        public void mouseReleased(MouseEvent me) { }
	        public void mouseEntered(MouseEvent me) { }
	        public void mouseExited(MouseEvent me) { }
	        public void mouseClicked(MouseEvent me) { 
	          int x = me.getX();
	          int y = me.getY();
	          text.setText("X:" + x + " Y:" + y); 
	        }
	    });
	}
}
