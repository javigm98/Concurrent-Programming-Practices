package parte2;

import java.net.InetAddress;

/**
 * Mensaje que envía un cliente al servidor indicándole que ya esta listo para
 * realizar una comunicación peer to peer con otro cliente. Incluye la dirección
 * ip y el puerto por el que se tendrá que conectar el otro cliente y el nombre
 * de ese otro cliente
 * 
 * @author Javier Guzmán
 *
 */
public class MensajePreparadoClienteServidor extends Mensaje {
	private String destInfo;
	private InetAddress ip;
	private int port;

	public MensajePreparadoClienteServidor(String origenId, String destId, String destInfo, InetAddress ip, int port) {
		super(TipoMensaje.MENSAJE_PREPARADO_CLIENTESERVIDOR, origenId, destId);
		this.destInfo = destInfo;
		this.ip = ip;
		this.port = port;
	}

	public String getDestInfo() {
		return destInfo;
	}

	public InetAddress getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

}
