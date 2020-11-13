package parte2;

import java.util.Arrays;
import java.util.Random;

public class Main {
	private static final int M = 100, N = 100, MAX = 100;
	//private static MonitorMultiBuffer m = new MonitorMultiBuffer();
	private static MonitorMultiBufferLock m = new MonitorMultiBufferLock();
	private static Random random = new Random();

	public static void main(String[] args) throws InterruptedException {
		Thread[] productores = new Thread[M];
		Thread[] consumidores = new Thread[N];
		
		
		for(int i = 0; i < M; ++i){
			int j = i;
			productores[i] = new Thread(()-> {
				try {
					funcionProd(j);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			productores[i].start();
		}
		
		for (int i = 0; i <N; ++i){
			int j = i;
			consumidores[i] = new Thread(()-> {
				try {
					funcionCons(j);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			consumidores[i].start();
		}
		
		for (int i = 0; i < M; ++i){
			productores[i].join();
		}
		
		for(int i = 0; i < N; ++i){
			productores[i].join();
		}

	}
	public static void funcionProd(int i) throws InterruptedException{
		while(true){
			int longitud = random.nextInt(MAX);
			int [] productos = new int[longitud];
			Arrays.fill(productos, i);
			System.out.println("---> El productor " +  i + " quiere producir " + longitud + " productos");
			m.almacenar(productos);
		
			Thread.sleep(1000);
		}
	}
	public static void funcionCons(int i) throws InterruptedException{
		while(true) {
			int numExtraer = random.nextInt(MAX);
			System.out.println("---> El consumidor " +  i + " quiere extraer " + numExtraer + " productos");
			int [] productos = m.extraer(numExtraer);
			Thread.sleep(1000);
		}
	}

}
