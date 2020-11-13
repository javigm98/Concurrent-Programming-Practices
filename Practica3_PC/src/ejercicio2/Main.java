package ejercicio2;

import java.util.concurrent.Semaphore;

public class Main {
	
	private static final int M = 2, N = 3;
	private static Semaphore empty = new Semaphore(1);
	private static Semaphore full = new Semaphore(0);
	private static AlmacenImp almacen = new AlmacenImp();
	

	public static void main(String[] args) throws InterruptedException {
		Thread[] productores = new Thread[M];
		Thread[] consumidores = new Thread[N];
		
		
		for(int i = 0; i < M; ++i){
			int j = i;
			productores[i] = new Thread(()-> funcionProd(j));
			productores[i].start();
		}
		
		for (int i = 0; i <N; ++i){
			int j = i;
			consumidores[i] = new Thread(()-> funcionCons(j));
			consumidores[i].start();
		}
		
		for (int i = 0; i < M; ++i){
			productores[i].join();
		}
		
		for(int i = 0; i < N; ++i){
			productores[i].join();
		}

	}
	
	public static void funcionProd(int i){
		while (true){
			try {
				empty.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			almacen.almacenar(i);
			System.out.println("Se ha producido el producto " + i);
			full.release();
		}
	}
	public static void funcionCons(int i){
		while (true){
			try {
				full.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int prod = almacen.extraer();
			System.out.println(" El consumidor " + i + " ha consumido el producto " + prod);
			empty.release();
		}
	}

}
