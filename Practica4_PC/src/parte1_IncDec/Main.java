package parte1_IncDec;

public class Main {
	private static final int M = 10000, N = 10000;
	private static MonitorIncDec m = new MonitorIncDec();

	public static void main(String[] args) throws InterruptedException {
		
        Thread[] hilos = new Thread[2*M];
		
		for (int i = 0; i < M; ++i) {
			hilos[i] = new Thread(()->funcionInc());
			hilos[i].start();
		}
		
		for (int i = M; i < 2*M; ++i) {
			hilos[i] = new Thread(()->funcionDec());
			hilos[i].start();
		}
		
		for (int i = 0; i < 2*M; ++i) {
			hilos[i].join();
		}
		
		System.out.println("Ya han acabado\n");
		System.out.println(m.getN());
	}
	
	public static void funcionInc(){
		for (int j = 0;j < N; ++j) {
			m.inc();
		}
	}

	public static void funcionDec() {
		for (int j = 0;j < N; ++j) {
			m.dec();
		}
	}

}

