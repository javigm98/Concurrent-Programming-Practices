package parte1_ProdConsN;


public class Main {
	private static final int M = 100, N = 100;
	private static MonitorProdConsNLock m= new MonitorProdConsNLock();

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
			m.almacenar(i);
			Thread.sleep(1000);
		}
	}
	public static void funcionCons(int i) throws InterruptedException{
		while(true){
			int producto = m.extraer();
			Thread.sleep(1000);
			System.out.println("---->El consumidor "+ i +" ha consumido el producto "+ producto);
		}
	}
}
