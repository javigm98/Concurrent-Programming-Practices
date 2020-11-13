package parte2;

/**
 * Mensaje que recibe un cliente para que cree un emisor que transmitirá un
 * fochero a otro cliente Incluye el nombre del fichero a transmitir y el id del
 * cliente que lo recibirá
 * 
 * @author Javier Guzmán
 *
 */

public class MensajeEmitirFichero extends Mensaje {
	private String nombreCliente;
	private String nombreFich;

	public MensajeEmitirFichero(String origenId, String destId, String nombreCliente, String nombreFich) {
		super(TipoMensaje.MENSAJE_EMITIR_FICHERO, origenId, destId);
		this.nombreCliente = nombreCliente;
		this.nombreFich = nombreFich;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getNombreFich() {
		return nombreFich;
	}

}
