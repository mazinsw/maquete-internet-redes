package common;

public class Average {
	private double avg;
	private int count;
	
	public Average() {
		avg = 0;
		count = 0;
	}
	
	public void add(double value) {
	    count++;
	    avg = ( avg * (count - 1)  + value) / count;
	}
	
	public double get()
	{
		return avg;
	}
	
	public int getCount()
	{
		return count;
	}
	
}
