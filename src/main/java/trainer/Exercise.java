package trainer;

public class Exercise {
	
	public enum LimitType {
		NONE,
		CHARS,
		TIME
	}
	private Integer id;
	private String name;
	private ExerciseGroup exerciseGroup;
	private String lineCreatorType;
	private String param;
	private LimitType limitType;
	private int limitUnits;
	
	public Exercise() {}
	
	public Exercise(String name, ExerciseGroup exerciseGroup, String lineCreatorType,
			String param, LimitType limitType, int limitUnits) {
		this.name = name;
		this.exerciseGroup = exerciseGroup;
		this.lineCreatorType = lineCreatorType;
		this.param = param;
		this.limitType = limitType;
		this.limitUnits = limitUnits;
	}
	
	public Exercise(int id, String name, ExerciseGroup exerciseGroup, String lineCreatorType,
			String param, LimitType limitType ,int limitUnits) {
		this(name, exerciseGroup, lineCreatorType, param, limitType, limitUnits);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ExerciseGroup getExerciseGroup() {
		return exerciseGroup;
	}
	public void setExerciseGroup(ExerciseGroup exerciseGroup) {
		this.exerciseGroup = exerciseGroup;
	}
	public String getLineCreatorType() {
		return lineCreatorType;
	}
	public void setLineCreatorType(String lineCreatorType) {
		this.lineCreatorType = lineCreatorType;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getParam() {
		return param;
	}
	public LimitType getLimitType() {
		return limitType;
	}
	public void setLimitType(LimitType limitType) {
		this.limitType = limitType;
	}
	public int getLimitUnits() {
		return limitUnits;
	}
	public void setLimitUnits(int limitUnits) {
		this.limitUnits = limitUnits;
	}
	
	@Override
	public String toString() {
		return    "Name="+name+'\n'
				+ "ExerciseGroup="+exerciseGroup.getName()+'\n'
				+ "LineCreatorType="+lineCreatorType+'\n'
				+ "Param="+param+'\n'
				+ "LimitType="+limitType.toString()+'\n'
				+ "LimitUnits="+limitUnits;
	}
	
}
