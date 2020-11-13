package parte2;

/**
 * Mensaje que recibe el cliente cuando la conexi�n con el servidor ya se puede
 * cerrar
 * 
 * @author Javier Guzm�n
 *
 */
public class MensajeConfirmacionCerrarConexion extends Mensaje {

	public MensajeConfirmacionCerrarConexion(String origenId, String destId) {
		super(TipoMensaje.MENSAJE_CONFIRMACION_CERRAR_CONEXION, origenId, destId);
		// TODO Auto-generated constructor stub
	}

}
