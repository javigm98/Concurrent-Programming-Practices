package parte2;

import java.util.List;

/**
 * Mensaje que recibe un cliente con la lista de usuarios conectados con el
 * servidor que previamente solicitó. Incluye, además de como todos los mensajes
 * el origen y el destino, esta lista de usuarios conectados.
 * 
 * @author Javier Guzmán
 *
 */

public class MensajeConfirmacionListaUsuarios extends Mensaje {
	private List<String> usuarios;

	public MensajeConfirmacionListaUsuarios(String origenId, String destId, List<String> usuarios) {
		super(TipoMensaje.MENSAJE_CONFIRMACION_LISTA_USUARIOS, origenId, destId);
		this.usuarios = usuarios;
	}

	public List<String> getUsuarios() {
		return usuarios;
	}

}
