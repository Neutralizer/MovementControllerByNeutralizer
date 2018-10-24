package testInterface.cameraTest.frameTest;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class TestJFrameGetPoint {

	public static void main(String[] arguments) {
		 
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JFrame frame = new JFrame("Get X and Y coordinates");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	    frame.setLayout(new BorderLayout());
	    frame.setSize(640,480);
	 
	    final JTextField text = new JTextField();
	    frame.add(text,BorderLayout.SOUTH);
	 
	    frame.addMouseListener(new MouseListener() {
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
	 
	    frame.setVisible(true);
	  }
}
