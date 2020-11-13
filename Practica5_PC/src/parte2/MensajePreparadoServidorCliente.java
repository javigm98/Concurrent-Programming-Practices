package parte2;

import java.net.InetAddress;

/**
 * Mensaje que envía el servidor a un cliente que pidió consultar un fichero
 * indicándole que el propietario de ese fichero ya está listo para enviarlo y
 * que ya puede crear un Receptor para conectarse con él y recibir este mensaje.
 * Incluye la dirección ip y el puerto del otro cliente por el que nos debemos
 * conectar con él para que nos transmita el mensaje
 * 
 * @author Javier Guzmán
 *
 */

public class MensajePreparadoServidorCliente extends Mensaje {
	private InetAddress ip;
	private int port;

	public MensajePreparadoServidorCliente(String origenId, String destId, InetAddress ip, int port) {
		super(TipoMensaje.MENSAJE_PREPARADO_SERVIDORCLIENTE, origenId, destId);
		this.ip = ip;
		this.port = port;
	}

	public InetAddress getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

}
