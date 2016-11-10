package gui.keyboard;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

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
 * <p>New keyboard layouts can be added, by first providing the required image and coordinate files.
 * Second, the keyboard layout ID must be appended to the <i>impl_layouts.txt</i> file.
 * 
 * @see KeyboardImage
 * @see KeyMapper
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
	 * Get an array of all keyboard layouts that are currently available.
	 * @return all keyboard layouts that are currently available
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
	
	/**
	 * Get the ID of the default keyboard layout for a specific language
	 * @param languageID the language ID, typically a two uppercase letters ID, like DE for
	 * German
	 * @return the default ID of the keyboard layout for the specified language ID or empty,
	 * if no default has been defined.
	 */
	public static Optional<String> getDefaultKeyboardLayout(String languageID) {
		try(BufferedReader r = new BufferedReader(
				new InputStreamReader(
						Keyboard.class.getResourceAsStream("map_lang_default_keyboard.txt"),
						persistence.Constants.PROJECT_CHARSET))) {
			Optional<String> val = r.lines().filter(s -> s.split(" ")[0].equals(languageID)).findFirst();
			return Optional.of(val.get().split(" ")[1]);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
