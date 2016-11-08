package gui.keyboard;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import trainer.KeyTypedEvent;
import trainer.LineMonitor;

/**
 * A visual representation of a keyboard, that highlights keys in response to
 * {@link trainer.KeyTypedEvent} that were sent by a {@link trainer.LineMonitor}.
 * 
 * <p>Keys that must be pressed next are highlighted in yellow. Correctly pressed keys will
 * flash in green, while wrongly typed keys will flash in red.
 * 
 * <p>The connection to a <code>LineMonitor</code> is twofold: First, <code>LineMonitor</code>
 * notifies <code>Keyboard</code> as an <code>Observable</code> about <code>KeyTypedEvent</code>.
 * For this purpose <code>Keyboard</code> must be added as an <code>Observer</code> to a
 * <code>LineMonitor</code>. Second, <code>Keyboard</code> directly fetches the key that must be
 * typed next from <code>LineMonitor</code>. Therefore, you need to call
 * {@link #setLineMonitor(LineMonitor)}, before you add <code>Keyboard</code> as an
 * <code>Observer</code>.
 * 
 * @author Lasse Osterhagen
 *
 */

public class Keyboard implements Observer {
	
	private static final int blinkTime = 200;
	private LineMonitor lineMonitor;
	private KeyboardImage image;
	
	/**
	 * Create a Keyboard with the locale specific layout.
	 * @param layoutID the Locale of the keyboard layout
	 */
	public Keyboard(String layoutID) {
		image = new KeyboardImage(layoutID, KeyboardImage.Color.BLACK_WHITE,
				KeyboardImage.Color.GREEN, KeyboardImage.Color.YELLOW,
				KeyboardImage.Color.RED);
	}
	
	/**
	 * Get the keyboard image as a Component to be used in AWT/Swing applications.
	 * @return the image Component
	 */
	public Component getKeyboardComponent() {
		return image;
	}
	
	/**
	 * Set the <code>LineMonitor</code> to allow to fetch the character that must be typed next.
	 * This method <strong>must</strong> be called before <code>Keyboard</code> is added as an
	 * <code>Observer</code> to the <code>LineMonitor</code>.
	 * @param lineMonitor the LineMonitor with information about the characters that must be typed
	 */
	public void setLineMonitor(LineMonitor lineMonitor) {
		this.lineMonitor = lineMonitor;
		image.colorKey(lineMonitor.getCurrentChar(), KeyboardImage.Color.YELLOW);
	}
	
	/**
	 * Stop following a <code>LineMonitor</code> to color the character that must be typed next.
	 * Remember to remove <code>Keyboard</code> as <code>Observer</code> of <code>LineMonitor</code>
	 * first.
	 */
	public void removeLineMonitor() {
		image.decolorAll();
		this.lineMonitor = null;
	}

	/**
	 * React to {@link trainer.KeyTypedEvent} that are pushed to this object by a
	 * {@link trainer.LineMonitor}.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(lineMonitor == null)
			throw new IllegalStateException("LineMonitor not set.");
		KeyTypedEvent kte = (KeyTypedEvent) arg;
		if(kte.correct) {
			image.decolorAll();
			image.colorKeyBlink(kte.c, KeyboardImage.Color.GREEN, blinkTime);
			image.colorKey(lineMonitor.getCurrentChar(), KeyboardImage.Color.YELLOW);
		}
		else
			image.colorKeyBlink(kte.c, KeyboardImage.Color.RED, blinkTime);
	}
	
	/**
	 * Get the IDs of all keyboard layouts that are available.
	 * 
	 * <p>IDs are Strings consisting of
	 * a language ID (e. g. <i>DE</i> for German) and a keyboard layout ID (e. g. <i>qw</i>
	 * for a Qwerty layout).
	 * 
	 * <p>Side note: The implemented keyboard layouts must have an entry in
	 * <i>gui.keyboard/impl.layouts.txt</i>.
	 * @return an array of all available keyboard layout IDs
	 */
	public static String[] getAvailableLayouts() {
		try(BufferedReader r = new BufferedReader(
				new InputStreamReader(Keyboard.class.getResourceAsStream("impl_layouts.txt"),
						persistence.Constants.PROJECT_CHARSET))) {
			return r.lines().toArray(s -> new String[s]);
			
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
