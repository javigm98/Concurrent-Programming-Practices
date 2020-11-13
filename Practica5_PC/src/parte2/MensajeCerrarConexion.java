package parte2;

/**
 * Mensaje por el que un usuario solicita cerrar la conexi�n con el servidor
 * 
 * @author Javier Guzm�n
 *
 */
public class MensajeCerrarConexion extends Mensaje {

	public MensajeCerrarConexion(String origenId, String destId) {
		super(TipoMensaje.MENSAJE_CERRAR_CONEXION, origenId, destId);
		// TODO Auto-generated constructor stub
	}

}
