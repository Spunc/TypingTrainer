package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

import trainer.LineMonitor;
import trainer.PracticeController;

public class KeyTypedMonitor extends Observable implements KeyListener {
	
	private PracticeController pc;
	private LineMonitor lm;
	
	public KeyTypedMonitor(PracticeController pc) {
		this.pc = pc;
		this.lm = pc.getLineMonitor();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// for the moment: do nothing
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// for the moment: do nothing
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		setChanged();
		//start timer at first key press
		if(pc.getState() == PracticeController.State.READY) {
			pc.run();
		}
		char c = e.getKeyChar();
		if(lm.advanceIfCorrect(c))
			notifyObservers(new KeyTypedEvent(c, true));
		else
			notifyObservers(new KeyTypedEvent(c, false));
	}

}
