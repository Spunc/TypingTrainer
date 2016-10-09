package gui;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Util {
	
	public static String getGUIText(String key) {
		ResourceBundle textBundle = ResourceBundle.getBundle("txtBundles.guiText");
		return textBundle.getString(key);
	}
	
	public static String getExerciseGroupText(String key) {
		ResourceBundle textBundle = ResourceBundle.getBundle("txtBundles.exerciseGroupText");
		return textBundle.getString(key);
	}
	
	public static String getKeyboardLayoutText(String key) {
		return ResourceBundle.getBundle("txtBundles.kbLayoutText").getString(key);
	}
	
	/**
	 * I18n the name of an <tt>Exercise</tt>. The name will be internationalized, if it is not
	 * a user defined <tt>Exercise</tt>.
	 * @param key The key entry in <i>exerciseName_Text.properties</i>
	 * @param groupId The id of the associated <tt>ExerciseGroup</tt>
	 * @return the name of the <tt>Exercise</tt> in the language of the current locale
	 */
	public static String getExerciseNameText(String key, int groupId) {
		if(groupId == persistence.Constants.userDefExerciseGroup)
			return key;
		ResourceBundle textBundle = ResourceBundle.getBundle("txtBundles.exerciseNameText");
		return textBundle.getString(key);
	}
	
	public static int getKeyCodeFromString(String key) {
		return java.awt.event.KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
	}
	
	public static String milli2TimeLabel(long time) {
		int seconds = (int) time/1000;
		int minutes = seconds/60;
		seconds = seconds%60;
		return String.format("%02d:%02d", minutes, seconds);
	}
	
	public static String rateLabel(double rate) {
		return String.format("%.2f %%", rate*100);
	}
	
	public static String hitsPerMinLabel(int hits, long requiredTime) {
		double min = requiredTime/60000.0;
		if(min < 0.02) // too short time to evaluate rate
			return "0";		
		int rate = (int) Math.round(hits/min);
		return Integer.toString(rate);
	}
	
	public static JTextArea makeLabelStyle(String text) {
		JTextArea textArea = new JTextArea(text);
	    textArea.setEditable(false);  
	    textArea.setCursor(null);  
	    textArea.setOpaque(false);  
	    textArea.setFocusable(false);  
	    return textArea;  
	}
	
	/**
	 * Surround a {@link java.awt.Component} with a border 
	 * @param c the Component to be wrapped
	 * @param top the width of the top
	 * @param left the width of the left side
	 * @param bottom the width of the bottom
	 * @param right the width of the right side
	 * @return the wrapped Component
	 */
	public static JComponent wrapInEmtpyBorder(Component c,
			int top, int left, int bottom, int right) {
		JPanel borderedPanel = new JPanel();
		borderedPanel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		borderedPanel.add(c);
		return borderedPanel;
	}
	
}
