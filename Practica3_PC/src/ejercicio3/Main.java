package ejercicio3;

public class Main {
	
	private static final int M = 100, N = 100;
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
		while(true){
			almacen.almacenar(i);
		}
	}
	public static void funcionCons(int i){
		while(true){
			int producto = almacen.extraer();
			System.out.println("El consumidor "+ i +" ha consumido el producto "+ producto);
		}
	}
}

