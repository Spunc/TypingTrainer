package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import trainer.LineMonitor;
import trainer.PracticeController;

public class KeyTypedMonitor extends KeyAdapter {
	
	private PracticeController pc;
	private LineMonitor lm;
	
	public KeyTypedMonitor(PracticeController pc) {
		this.pc = pc;
		this.lm = pc.getLineMonitor();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		//start timer at first key press
		if(pc.getState() == PracticeController.State.READY) {
			pc.run();
		}
		lm.advanceIfCorrect(e.getKeyChar());
	}

}
