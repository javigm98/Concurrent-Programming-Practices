package parte2;

import java.net.InetAddress;

/**
 * Mensaje que env�a el servidor a un cliente que pidi� consultar un fichero
 * indic�ndole que el propietario de ese fichero ya est� listo para enviarlo y
 * que ya puede crear un Receptor para conectarse con �l y recibir este mensaje.
 * Incluye la direcci�n ip y el puerto del otro cliente por el que nos debemos
 * conectar con �l para que nos transmita el mensaje
 * 
 * @author Javier Guzm�n
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
