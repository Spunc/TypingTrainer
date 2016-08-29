package gui.chart;

public class Range {
	double min = Double.MAX_VALUE;
	double max = Double.MIN_VALUE;
	
	public Range() {}
	
	public Range(double min, double max) {
		if(min > max)
			throw new IllegalArgumentException("Maximum lower than minimum!");
		this.min = min;
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		if(min > max)
			throw new IllegalArgumentException("Minimum higher than maximum!");
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		if(min > max)
			throw new IllegalArgumentException("Maximum lower than minimum!");
		this.max = max;
	}
	
	public double getLength() {
		return max-min;
	}
	
	@Override
	public String toString() {
		return "[" + min + "/" + max + "]";
	}
}
