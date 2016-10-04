package gui.keyboard;

import java.awt.Component;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import gui.KeyTypedEvent;
import trainer.LineMonitor;

public class Keyboard implements Observer {
	
	private static final int blinkTime = 300;
	private LineMonitor lineMonitor;
	private KeyboardImage image;
	
	public Keyboard(Locale localeID) {
		image = new KeyboardImage(localeID, KeyboardImage.Color.BLACK_WHITE,
				KeyboardImage.Color.GREEN, KeyboardImage.Color.YELLOW,
				KeyboardImage.Color.RED);
	}
	
	public Component getKeyboardComponent() {
		return image;
	}
	
	public void setLineMonitor(LineMonitor lineMonitor) {
		this.lineMonitor = lineMonitor;
	}
	
	public void removeLineMonitor() {
		this.lineMonitor = null;
	}

	@Override
	public void update(Observable o, Object arg) {
		assert lineMonitor != null: "LineMonitor is null.";
		KeyTypedEvent kte = (KeyTypedEvent) arg;
		if(kte.correct) {
			image.colorKeyBlink(kte.c, KeyboardImage.Color.GREEN, blinkTime);
			image.colorKey(lineMonitor.getCurrentChar(), KeyboardImage.Color.YELLOW);
		}
		else
			image.colorKeyBlink(kte.c, KeyboardImage.Color.RED, blinkTime);
	}

}
