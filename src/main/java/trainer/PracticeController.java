package trainer;

import java.util.Observable;

import persistence.SessionPersist;
import trainer.lineCreators.LineCreator;
import trainer.lineCreators.LineCreatorFactory;
import trainer.lineCreators.LineCreatorFactory.ImplementationNotFound;
import trainer.lineCreators.LineCreatorProvider.InitException;

public class PracticeController extends Observable {
	
	public enum State {
		INIT,
		READY,
		RUNNING,
		REG_STOPPED, // regularly stopped
		USER_STOPPED // stopped by user
	}
	
	public enum Event {
		STATE_CHANGED,
		NEW_LINE
	}
	
	private Exercise exercise;
	private State state;
	private PerformanceStats performanceStats;
	protected LineCreator lineCreator;
	private LineMonitor lineMonitor;
	
	private int maxLineLength;
	private String line1; // the line that the typist has to type
	private String line2; // the following line
	private boolean isLastRow = false; //line1 currently displays the last row
	private int correctTypedChars;
	private long startTime;
	private long stopTime;
	private long requiredTime;
	private SimpleTimerInterface stopWatch = new NullStopWatch();
	private SessionPersist sessionPersist = new SessionPersist();
	
	public PracticeController(Exercise exercise, int maxLineLength)
			throws ImplementationNotFound, InitException {
		this.exercise = exercise;
		this.maxLineLength = maxLineLength;
		performanceStats = new PerformanceStats();
		lineCreator = LineCreatorFactory.getLineCreator(exercise.getLineCreatorType(),
				exercise.getParam(), performanceStats);
		lineMonitor = new LineMonitor(this);
		if(exercise.getLimitType() == Exercise.LimitType.TIME) {
			stopWatch = new StopWatch(exercise.getLimitUnits()*1000,
					evt->regStop());
		}
		state = State.INIT;
	}
	
	public void ready() {
		if(!(state == State.INIT))
			throw new IllegalStateException("ready() only allowed to be called during State.INIT.");
		//prepare line2 that will become line1 after call to newLine()
		line2 = lineCreator.create(maxLineLength);
		newLine();
		setState(State.READY);
	}
	
	public void run() {
		setState(State.RUNNING);
		stopWatch.start();
		startTime = System.currentTimeMillis();
	}
	
	public void userStop() {
		stopWatch.stop();
		stop();
		setState(State.USER_STOPPED);
	}
	
	private void regStop() {
		stop();
		sessionPersist.saveSession2DB(exercise.getId(),
				performanceStats.getTotalPerformanceRate(), requiredTime);
		setState(State.REG_STOPPED);
	}
	
	private void stop() {
		stopTime = System.currentTimeMillis();
		requiredTime = stopTime-startTime;
		lineCreator.stop();
	}
	
	public Exercise getExercise() {
		return exercise;
	}
	
	public LineMonitor getLineMonitor() {
		return lineMonitor;
	}
	
	public PerformanceStats getPerformanceStats() {
		return performanceStats;
	}
	
	public String getLine1() {
		return line1;
	}

	public String getLine2() {
		return line2;
	}
	
	public void newLine() {
		// line1 shows the last row that the lineCreator can provide
		if(isLastRow) {
			lineMonitor.setLine("\0");
				// Use the zero char as placeholder, so that a keyboard won't display anything
			regStop();
			return;
		}
		setChanged();
		line1 = line2;
		if(lineCreator.hasNext()) {
			line2 = lineCreator.create(maxLineLength);
			lineMonitor.setLine(line1);
			notifyObservers(Event.NEW_LINE);
		}
		else {
			line2 = "";
			isLastRow = true;
		}
	}

	private void setState(State state) {
		if(this.state != state) {
			setChanged();
			this.state = state;
			notifyObservers(Event.STATE_CHANGED);
		}
	}
	
	public State getState() {
		return state;
	}
	
	public long getRequiredTime() {
		return requiredTime;
	}
	
	public long getCurrentTime() {
		long timeDifference = System.currentTimeMillis() - startTime;
		if (exercise.getLimitType() == Exercise.LimitType.TIME)
			return exercise.getLimitUnits()*1000-timeDifference;
		else
			return timeDifference;
	}

	public void incrementCorrectTypedChars() {
		++correctTypedChars;
		if( exercise.getLimitType() == Exercise.LimitType.CHARS &&
				correctTypedChars >= exercise.getLimitUnits() )
			regStop();
	}

}
