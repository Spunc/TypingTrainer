package gui.keyboard;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Manual test of the <code>KeyboardImage</code> class for visual inspection.
 * 
 * @author Lasse Osterhagen
 *
 */
public class Man_KeyboardImageTest {
	
	public void initAndShowGUI() {
		KeyboardImage keyboardImage = new KeyboardImage("DE_qw",
				KeyboardImage.Color.BLACK_WHITE,
				KeyboardImage.Color.YELLOW,
				KeyboardImage.Color.GREEN);
		keyboardImage.colorKey('L', KeyboardImage.Color.YELLOW);
		keyboardImage.colorKey('g', KeyboardImage.Color.GREEN);
		keyboardImage.colorKey('\n', KeyboardImage.Color.GREEN);
		keyboardImage.colorKey('m', KeyboardImage.Color.GREEN);
		keyboardImage.removeKeyColor('m', KeyboardImage.Color.GREEN);
		keyboardImage.colorKeyBlink('ö', KeyboardImage.Color.GREEN, 2000);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(keyboardImage);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater( () -> new Man_KeyboardImageTest().initAndShowGUI());
	}

}
