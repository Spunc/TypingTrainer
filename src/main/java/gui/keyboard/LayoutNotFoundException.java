package gui.keyboard;

import java.util.Locale;

/**
 * Probably, there won't be implementations for keyboard layout of all existing locales.
 * Therefore, classes that represent keyboard layouts in any way might throw an
 * <code>LayoutNotFoundException</code> during object creation.
 * 
 * @author Lasse Osterhagen
 *
 */

@SuppressWarnings("serial")
public class LayoutNotFoundException extends Exception {
	
	/**
	 * The Locale, for which the keyboard layout is not implemented.
	 */
	public final Locale locale;
	
	/**
	 * Create a <code>LayoutNotFoundException</code>
	 * @param locale the locale, for which the keyboard layout is not implemented
	 */
	public LayoutNotFoundException(Locale locale) {
		super("Could not find keyboard layout for locale " + locale);
		this.locale = locale;
	}

}
