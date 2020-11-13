package parte1_ProdCons;

public class MonitorProdCons {
	private int buff = 0;
	
	private boolean lleno = false;
	
	synchronized void almacenar(int prod) throws InterruptedException {
		while(lleno) wait();
		buff = prod;
		System.out.println("Se ha producido el producto " + prod);
		lleno = true;
		notifyAll();
	}
	
	synchronized int extraer() throws InterruptedException {
		while(!lleno) wait();
		int prod = buff;
		System.out.println("Se ha consumido el producto " + prod);
		lleno = false;
		notifyAll();
		return prod;
	}
}
