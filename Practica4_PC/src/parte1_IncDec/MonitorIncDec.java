package parte1_IncDec;

public class MonitorIncDec {
	private int n = 0;
	synchronized void inc() {
		n++;
	}
	synchronized void dec() {
		n--;
	}
	
	synchronized int getN() {
		return this.n;
	}
}
