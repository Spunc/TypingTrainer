package gui;

import java.awt.event.ActionListener;

import javax.swing.Timer;

public class TimeTask {
	
	private int time;
	private ActionListener action;
	
	public TimeTask(int time, ActionListener action) {
		this.time = time;
		this.action = action;
	}
	
	public static void doTasks(TimeTask... timeTasks) {
		for(TimeTask timeTask : timeTasks) {
			Timer timer = new Timer(timeTask.time, timeTask.action);
			timer.setRepeats(false);
			timer.start();
		}
	}
}
