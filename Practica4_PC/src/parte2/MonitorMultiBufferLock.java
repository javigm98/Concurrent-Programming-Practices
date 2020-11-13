package parte2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorMultiBufferLock {
	private static int MAX = 100;
	private int[] buff = new int [MAX];
	
	private int ini = 0, fin = 0, cuenta = 0;
	
	private final Lock lock = new ReentrantLock();
	private final Condition he_producido = lock.newCondition();
	private final Condition he_consumido = lock.newCondition();
	
	public void almacenar(int[] productos) throws InterruptedException {
		lock.lock();
		while (MAX-cuenta < productos.length) he_consumido.await();
		for(int i = 0; i < productos.length; ++i) {
			buff[(fin+i)%MAX] = productos[i];
			System.out.println("Se ha producido el producto " + productos[i] + " en la posicion " + (fin + i)%MAX);
		}
		fin = (fin + productos.length)%MAX;
		cuenta = cuenta + productos.length;
		he_producido.signal();
		lock.unlock();
	}
	
	public int[] extraer(int numExtraer) throws InterruptedException {
		lock.lock();
		int [] productos = new int[numExtraer];
		while(cuenta < numExtraer) he_producido.await();
		for(int i = 0; i < productos.length; ++i) {
			productos[i] = buff[(ini+i)%MAX];
			System.out.println("Se ha consumido el producto " + productos[i] + " en la posicion " + (ini + i)%MAX);
		}
		fin = (fin + productos.length)%MAX;
		cuenta = cuenta - numExtraer;
		he_consumido.signal();
		lock.unlock();
		return productos;
	}

}
