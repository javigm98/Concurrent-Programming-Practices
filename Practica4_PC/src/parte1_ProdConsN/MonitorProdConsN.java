package parte1_ProdConsN;

public class MonitorProdConsN {
	private static int MAX = 100;
	
	private int[] buff = new int[MAX];
	
	private int ini = 0, fin = 0, cuenta = 0;
	
	synchronized void almacenar (int producto) throws InterruptedException {
		while(cuenta == MAX) wait();
		buff[fin] = producto;
		System.out.println("Se ha producido el producto " + producto + " en la posicion " + fin);
		fin = (fin+1)%MAX;
		cuenta++;
		notifyAll();
	}
	
	synchronized int extraer() throws InterruptedException {
		while (cuenta == 0) wait();
		int prod = buff[ini];
		System.out.println("Se ha consumido el producto " + prod + " de la posicion " + ini);
		ini = (ini+1)%MAX;
		cuenta--;
		notifyAll();
		return prod;
	}
	
	

}
