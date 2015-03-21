package tools;

public class Chrono {
	private long start;
	
	public Chrono() {
		start = System.currentTimeMillis();
	}
	
	public double elapsedTime() {
		return (System.currentTimeMillis() - start) / 1000.0;
	}
	
	public void reset() {
		start = System.currentTimeMillis();
	}

}
