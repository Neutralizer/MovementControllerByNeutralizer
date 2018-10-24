package bgSubtraction.display;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DisplayJFrame extends JFrame{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame jFrame;
	JLabel label = new JLabel();
	 BufferedImage img = null;

	public DisplayJFrame() {
		jFrame = new JFrame("Movement");
		jFrame.setSize(640, 480);
		jFrame.setVisible(true);
//		jFrame.pack();
	}
	
	public JFrame getJFrame() {
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
	
	public void show(BufferedImage img) {
		this.img = img;
		jFrame.add( new JLabel(new ImageIcon(img)),BorderLayout.CENTER);
		jFrame.setIconImage(img);
		jFrame.setVisible(true);
        label.setVisible(true);
	}
	
	public void paint(Graphics g) {
	    super.paint(g);
	    g.drawImage(img, 0, 0, null);
	}
}
