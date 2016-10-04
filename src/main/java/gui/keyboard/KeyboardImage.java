package gui.keyboard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import gui.keyboard.KeyMapper.NotInKeySetException;

/**
 * A visual representation of a keyboard.
 * 
 * <p>Individual keys or key combinations can be colored in different colors than the background
 * color.
 * 
 * <p>This class needs separate .gif image files that are located in this package
 * (<i>gui.keyboard</i>) for every color and every locale. The file names always end with the code
 * that belongs to the <code>Locale</code> (e. g. <i>en_UK</i>), before the file extension
 * <i>.gif</i> is appended:
 * 
 * <table summary="Keyboard image files">
 * 	<tr>
 * 		<th>File name</th>
 * 		<th>Keyboard color</th>
 * 	</tr>
 * 	<tr>
 * 		<td>im_bw_</td>
 * 		<td>black and white</td>
 * 	</tr>
 * 	<tr>
 * 		<td>im_green_</td>
 * 		<td>green</td>
 * 	</tr>
 * 	<tr>
 * 		<td>im_red_</td>
 * 		<td>red</td>
 * 	</tr>
 * 	<tr>
 * 		<td>im_yellow_</td>
 * 		<td>yellow</td>
 * 	</tr>
 * </table>
 * 
 * @author Lasse Osterhagen
 *
 */

@SuppressWarnings("serial")
public class KeyboardImage extends Component {
	
	/**
	 * Checks whether the image files for the specified <code>Locale</code> exists.
	 * 
	 * <p>The current implementation only checks if the black and white image for
	 * this <code>Locale</code> can be found.
	 * 
	 * @param localeID the Locale
	 * @return true if the image files for the keyboard layout exist
	 */
	public static boolean exists(Locale localeID) {
		String bwImageFileName = "im_bw_" + localeID + ".gif";
		return KeyboardImage.class.getResource(bwImageFileName) != null;
	}
	
	/**
	 * Colors for keys
	 */
	public enum Color {
		BLACK_WHITE("bw"),
		RED("red"),
		YELLOW("yellow"),
		GREEN("green");
		private String fileName;
		private Color(String fileName) {this.fileName = fileName;}
	}
	
	/**
	 * A rectangle that is associated with a color.
	 */
	private static class ColoredCoordinates {
		Coordinates c;
		Color col;
		ColoredCoordinates(Coordinates c, Color col) {
			this.c = c;
			this.col = col;
		}
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ColoredCoordinates))
				return false;
			ColoredCoordinates o = (ColoredCoordinates) obj;
			return c.equals(o.c) && col == o.col;
		}
	}
	
	private KeyMapper keyMapper;
	private BufferedImage backgroundImage;
	private Map<Color, BufferedImage> colorImages;
	private Dimension imageSize;
	private LinkedList<ColoredCoordinates> coordinates2color = new LinkedList<>();
	
	/**
	 * Create a keyboard image for a keyboard of a specific locale.
	 * @param localeID the locale of the keyboard
	 * @param backgroundColor the standard (uncolored) color of the keyboard
	 * @param additionalColors color in which keys should be highlighted
	 */
	public KeyboardImage(Locale localeID, Color backgroundColor, Color... additionalColors) {
		keyMapper = new KeyMapper(localeID);
		colorImages = new HashMap<>(additionalColors.length);
		try {
			backgroundImage = getImage(backgroundColor, localeID);
			for(Color c : additionalColors) {
				colorImages.put(c, getImage(c, localeID));
			}
		} catch (IOException e) {
			throw new RuntimeException("Missing image file.", e);
		}
		imageSize = new Dimension(backgroundImage.getWidth(), backgroundImage.getHeight());	
	}
	
	/**
	 * Color the key(s) that correspond to the specified character.
	 * <p>This function may color more than one key, if a modifying key must be pressed in order
	 * to type that character (e. g. for uppercase letters).
	 * <p>If this function is called several times for the same character but for different colors,
	 * the key(s) will be colored in the order of function calls. Typically, this will result in
	 * the key(s) to be colored in the color of the last function call.
	 * @param c the character for which the key(s) should be colored
	 * @param color the color of the key(s)
	 */
	public void colorKey(char c, Color color) {
		if(!colorImages.containsKey(color))
			throw new RuntimeException("Specified color not in intialized color set.");
		try {
			Coordinates[] cos = keyMapper.getCoordinatesFor(c);
			colorCoordinates(cos, color);
		}
		catch (NotInKeySetException exc) {
			// Do not try to color a key that does not exist on the keyboard.
		}
	}
	
	/**
	 * Color the key(s) that correspond to the specified <code>KeyEvent</code>.
	 * <p>This function may color more than one key, if a modifying key must be pressed in order
	 * to type that character (e. g. for uppercase letters).
	 * <p>If this function is called several times for the same character but for different colors,
	 * the key(s) will be colored in the order of function calls. Typically, this will result in
	 * the key(s) to be colored in the color of the last function call.
	 * <p>Use this function to color individual modifier keys, as they cannot be specified as a
	 * character.
	 * @param e the KeyEvent for which the key(s) should be colored
	 * @param color the color of the key(s)
	 */
	public void colorKey(KeyEvent e, Color color) {
		if(!colorImages.containsKey(color))
			throw new RuntimeException("Specified color not in intialized color set.");
		try {
			Coordinates[] cos = keyMapper.getCoordinatesFor(e);
			colorCoordinates(cos, color);
		}
		catch (NotInKeySetException exc) {
			// Do not try to color a key that does not exist on the keyboard.
		}
	}
	
	private void colorCoordinates(Coordinates[] cos, Color color) {
		for(Coordinates co : cos) {
			coordinates2color.add(new ColoredCoordinates(co, color));
		}
		repaint();
	}
	
	/**
	 * Do not color the key(s) that correspond to the specified character any longer.
	 * <p>A call of this function only makes sense after a call to {@link #colorKey(char, Color)}
	 * with the same arguments. It will then remove the color of the key(s) so that they will be
	 * painted in the background color.
	 * @param c the character for which the color of the key(s) should be removed
	 * @param color the color of the key(s) that should be removed
	 */
	public void removeKeyColor(char c, Color color) {
		try {
			Coordinates[] cos = keyMapper.getCoordinatesFor(c);
			removeCoordinates(cos, color);
		}
		catch (NotInKeySetException e) {
			// Do not try to remove the color of a key that does not exist on the keyboard.
		}
	}
	
	/**
	 * Do not color the key(s) that correspond to the specified <code>KeyEvent</code> any longer.
	 * <p>A call of this function only makes sense after a call to {@link #colorKey(KeyEvent, Color)}
	 * with the same arguments. It will then remove the color of the key(s) so that they will be
	 * painted in the background color.
	 * @param e the KeyEvent for which the color of the key(s) should be removed
	 * @param color the color of the key(s) that should be removed
	 */
	public void removeKeyColor(KeyEvent e, Color color) {
		try {
			Coordinates[] cos = keyMapper.getCoordinatesFor(e);
			removeCoordinates(cos, color);
		}
		catch (NotInKeySetException exc) {
			// Do not try to remove the color of a key that does not exist on the keyboard.
		}
	}
	
	private void removeCoordinates(Coordinates[] cos, Color color) {
		for(Coordinates co : cos) {
			coordinates2color.remove(new ColoredCoordinates(co, color));
		}
		repaint();
	}
	
	/**
	 * Color the key(s) that correspond to the specified character for a certain time span.
	 * This function can be used e.g. to let key(s) blink for a short time period.
	 * @param c the character for which the key(s) should be colored
	 * @param color the color of the key(s)
	 * @param time the time span in milliseconds
	 */
	public void colorKeyBlink(char c, Color color, int time) {
		colorKey(c, color);
		Timer t = new Timer(time, evt -> removeKeyColor(c, color));
		t.setRepeats(false);
		t.start();
	}
	
	/**
	 * Color the key(s) that correspond to the specified <code>KeyEvent</code> for a certain time
	 * span. This function can be used e.g. to let key(s) blink for a short time period.
	 * @param e the character for which the key(s) should be colored
	 * @param color the color of the key(s)
	 * @param time the time span in milliseconds
	 */
	public void colorKeyBlink(KeyEvent e, Color color, int time) {
		colorKey(e, color);
		Timer t = new Timer(time, evt -> removeKeyColor(e, color));
		t.setRepeats(false);
		t.start();
	}
	
	private static BufferedImage getImage(Color c, Locale localeID) throws IOException {
		return ImageIO.read(KeyboardImage.class.getResourceAsStream(
				"im_" + c.fileName + '_' + localeID + ".gif"));
	}
	
	@Override
	public Dimension getPreferredSize() {
		return imageSize;
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
		for(ColoredCoordinates cc : coordinates2color) {
			g.drawImage(colorImages.get(cc.col), cc.c.x1, cc.c.y1, cc.c.x2, cc.c.y2,
					cc.c.x1, cc.c.y1, cc.c.x2, cc.c.y2, null);
		}
	}

}
