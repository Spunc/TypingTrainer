package gui.keyboard;

class Coordinates {
	
	int x1, y1, x2, y2;
	
	public Coordinates() {
		this(0, 0, 0, 0);
	}
	
	public Coordinates(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Coordinates)) return false;
		Coordinates other = (Coordinates) obj;
		return x1 == other.x1 && y1 == other.y1 && x2 == other.x2 && y2 == other.y2;
	}
	
	@Override
	public int hashCode() {
		return x1+y1*3+x2*7+y2*11;
	}

}
