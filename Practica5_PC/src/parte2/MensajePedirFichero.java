package parte2;

/**
 * Mensaje que env�a un cliente al servidor para pedir un fichero de otro
 * cliente. Incluye el nombre del fichero que estamos pidiendo
 * 
 * @author Javier Guzm�n
 *
 */
public class MensajePedirFichero extends Mensaje {
	private String nombreFich;

	public MensajePedirFichero(String origenId, String destId, String nombreFich) {
		super(TipoMensaje.MENSAJE_PEDIR_FICHERO, origenId, destId);
		this.nombreFich = nombreFich;
	}

	public String getNombreFich() {
		return nombreFich;
	}

}
