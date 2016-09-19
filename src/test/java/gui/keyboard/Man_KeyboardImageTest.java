package gui.keyboard;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Man_KeyboardImageTest {
	
	private KeyboardImage keyboardImage = new KeyboardImage("de", KeyboardImage.Color.BLACK_WHITE,
			KeyboardImage.Color.YELLOW, KeyboardImage.Color.GREEN);
	
	public void initAndShowGUI() {
		keyboardImage.colorChar('F', KeyboardImage.Color.YELLOW);
		keyboardImage.colorChar('g', KeyboardImage.Color.GREEN);
		keyboardImage.colorChar('m', KeyboardImage.Color.GREEN);
		keyboardImage.removeChar('m', KeyboardImage.Color.GREEN);
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
