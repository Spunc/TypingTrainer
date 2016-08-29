package trainer;

public class ExerciseGroup {
	
	private Integer id;
	private String name;
	private boolean editable;
	
	public ExerciseGroup() {}
	
	public ExerciseGroup(String name, boolean editable) {
		this.name = name;
		this.editable = editable;
	}
	
	public ExerciseGroup(int id, String name, boolean editable) {
		this(name, editable);
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}
