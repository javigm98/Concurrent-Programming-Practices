package parte2;

public class MonitorMultiBuffer {
	private static int MAX = 100;
	
	private int[] buff = new int [MAX];
	
	private int ini = 0, fin = 0, cuenta = 0;
	
	
	synchronized int[] extraer(int numExtraer) throws InterruptedException {
		int[] productos = new int[numExtraer];
		while (cuenta < numExtraer) wait();
		for(int i = 0; i < numExtraer; ++i) {
			productos[i] = buff[(ini + i)%MAX];
			System.out.println("Se ha consumido el producto " + productos[i] + " de la posicion " + (ini + i)%MAX);
		}
		ini = (ini + numExtraer)%MAX;
		cuenta = cuenta - numExtraer;
		notifyAll();
		return productos;
		
	}

	synchronized void almacenar(int[] productos) throws InterruptedException {
		while(MAX - cuenta < productos.length) wait();
		for(int i = 0; i < productos.length; i++) {
			buff[(fin + i)%MAX] = productos[i];
			System.out.println("Se ha producido el producto " + productos[i] + " en la posicion " + (fin + i)%MAX);
		}
		fin = (fin + productos.length)%MAX;
		cuenta = cuenta + productos.length;
		notifyAll();
		
	}
}
