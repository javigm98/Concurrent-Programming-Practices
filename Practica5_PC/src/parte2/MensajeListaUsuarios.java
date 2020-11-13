package parte2;

import java.util.List;

/**
 * Mensaje que env�a un cliente para solicitar la lista de usuarios conectados
 * al servidor
 * 
 * @author Javier Guzm�n
 *
 */
public class MensajeListaUsuarios extends Mensaje {

	public MensajeListaUsuarios(String origenId, String destId) {
		super(TipoMensaje.MENSAJE_LISTA_USUARIOS, origenId, destId);

	}

}
