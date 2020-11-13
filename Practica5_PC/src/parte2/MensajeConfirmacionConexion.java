package parte2;

/**
 * Mensaje que recibe el cliente como confirmación de que ya está conectado con
 * el servidor
 * 
 * @author Javier Guzmán
 *
 */

public class MensajeConfirmacionConexion extends Mensaje {

	public MensajeConfirmacionConexion(String origenId, String destId) {
		super(TipoMensaje.MENSAJE_CONFIRMACION_CONEXION, origenId, destId);
		// TODO Auto-generated constructor stub
	}

}
