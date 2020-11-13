package ejercicio3;

import java.util.concurrent.Semaphore;

public class AlmacenImp implements Almacen{
	private static int MAX = 100;
	
	private volatile int[] buff = new int[MAX];
	private int ini = 0, fin = 0;
	private Semaphore empty = new Semaphore(MAX);
	private Semaphore full = new Semaphore(0);
	private Semaphore mutexP = new Semaphore(1);
	private Semaphore mutexC = new Semaphore(1);
	
	@Override
	public void almacenar(int producto) {
		try {
			empty.acquire(); //Me aseguro de que haya hueco
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mutexP.acquire(); //Soy el unico que toca FIN
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buff[fin] = producto;
		buff = buff;
		System.out.println("Se ha producido el producto " + producto + " en la posicion " + fin);
		fin = (fin+1)%MAX;
		mutexP.release();
		full.release();
		
		
	}
	@Override
	public int extraer() {
		try {
			full.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mutexC.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int producto = buff[ini];
		System.out.println("Se ha consumido el producto " + producto + " de la posicion " + ini);
		ini = (ini+1)%MAX;
		mutexC.release();
		empty.release();
		return producto;
	}


}
