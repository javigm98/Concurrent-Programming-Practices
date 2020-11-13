package parte2;

import java.util.List;

/**
 * Mensaje por el que un usuario solicita establecer una conexi�n con el
 * servidor. El mensaje incluye una lista de ficheros para que se a�adan a la
 * tabla informacion del servidor por si este usuario no figuraba en el fichero
 * inicial 'users.txt'.
 * 
 * @author Javier Guzm�n
 *
 */
public class MensajeConexion extends Mensaje {
	private List<String> ficheros;

	public MensajeConexion(String origenId, String destId, List<String> ficheros) {
		super(TipoMensaje.MENSAJE_CONEXION, origenId, destId);
		this.ficheros = ficheros;
	}

	public List<String> getFicheros() {
		return ficheros;
	}

}