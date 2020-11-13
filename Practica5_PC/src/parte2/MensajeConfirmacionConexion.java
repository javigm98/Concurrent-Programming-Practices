package parte2;

/**
 * Mensaje que recibe el cliente como confirmaci�n de que ya est� conectado con
 * el servidor
 * 
 * @author Javier Guzm�n
 *
 */

public class MensajeConfirmacionConexion extends Mensaje {

	public MensajeConfirmacionConexion(String origenId, String destId) {
		super(TipoMensaje.MENSAJE_CONFIRMACION_CONEXION, origenId, destId);
		// TODO Auto-generated constructor stub
	}

}
