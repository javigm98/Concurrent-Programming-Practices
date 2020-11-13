package ejercicio2;

public class AlmacenImp implements Almacen{

	private int buff = 0;
	
	
	

	@Override
	public void almacenar(int producto) {
		buff = producto;
		
	}

	@Override
	public int extraer() {
		int producto = buff;
		return producto;
	}

}
