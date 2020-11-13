package parte2;

import java.util.List;

/**
 * Mensaje que envía un cliente para solicitar la lista de usuarios conectados
 * al servidor
 * 
 * @author Javier Guzmán
 *
 */
public class MensajeListaUsuarios extends Mensaje {

	public MensajeListaUsuarios(String origenId, String destId) {
		super(TipoMensaje.MENSAJE_LISTA_USUARIOS, origenId, destId);

	}

}
