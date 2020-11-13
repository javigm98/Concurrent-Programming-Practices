package parte2;

/**
 * Mensaje por el que un usuario solicita cerrar la conexión con el servidor
 * 
 * @author Javier Guzmán
 *
 */
public class MensajeCerrarConexion extends Mensaje {

	public MensajeCerrarConexion(String origenId, String destId) {
		super(TipoMensaje.MENSAJE_CERRAR_CONEXION, origenId, destId);
		// TODO Auto-generated constructor stub
	}

}
