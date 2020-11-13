package parte2;

/**
 * Mensaje que env�a el servidor a un cliente cuando este �ltimo ha pedido
 * consultar un fichero que no pertence a ning�n usuario conectado al servidor.
 * Evidentemente no se hace comunicaci�n peer to peer.
 * 
 * OBSERVACI�N: Este tipo de mensaje no se especificaba ni en el enuniado ni en
 * las explicaciones de la pr�ctica pero se a�ade para tratar este posible error
 * 
 * @author Javier Guzm�n
 *
 */
public class MensajeFicheroInexistente extends Mensaje {
	private String nombreFich;

	public MensajeFicheroInexistente(String origenId, String destId, String nombreFich) {
		super(TipoMensaje.MENSAJE_FICHERO_INEXISTENTE, origenId, destId);
		this.nombreFich = nombreFich;
	}

	public String getNombreFich() {
		return nombreFich;
	}

}
