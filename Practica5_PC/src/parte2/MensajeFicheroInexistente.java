package parte2;

/**
 * Mensaje que envía el servidor a un cliente cuando este último ha pedido
 * consultar un fichero que no pertence a ningún usuario conectado al servidor.
 * Evidentemente no se hace comunicación peer to peer.
 * 
 * OBSERVACIÓN: Este tipo de mensaje no se especificaba ni en el enuniado ni en
 * las explicaciones de la práctica pero se añade para tratar este posible error
 * 
 * @author Javier Guzmán
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
