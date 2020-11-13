package ejercicio1;

import java.util.concurrent.Semaphore;

public class Main {

private static final int M = 10000, N = 1000;
public static int n = 0;

/*Solo puede acceder uno a la variable n, usaremos un semáforo
 * como si fuese un cerrojo para garantizar que sólo un proceso,
 * incrementador o decrementador, accede a la sección crítica.
 */

private static Semaphore mutex = new Semaphore(1);

	
	public static void main(String [] args) throws InterruptedException {
		Thread[] hilos = new Thread[2*M];
		
		for (int i = 0; i < M; ++i) {
			int j = i;
			hilos[i] = new Thread(()->funcionInc(j));
			hilos[i].start();
		}
		
		for (int i = M; i < 2*M; ++i) {
			int j = i;
			hilos[i] = new Thread(()->funcionDec(j));
			hilos[i].start();
		}
		
		for (int i = 0; i < 2*M; ++i) {
			hilos[i].join();
		}
		
		System.out.println("Ya han acabado\n");
		System.out.println(n);
	}

	public static void funcionInc(int i){
		for (int j = 0;j < N; ++j) {
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			n++;
			mutex.release();
		}
	}

	public static void funcionDec(int i) {
		for (int j = 0;j < N; ++j) {
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			n--;
			mutex.release();
		}
	}
}
