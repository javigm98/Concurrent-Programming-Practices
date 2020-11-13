package parte2;

import java.net.InetAddress;

/**
 * Mensaje que env�a un cliente al servidor indic�ndole que ya esta listo para
 * realizar una comunicaci�n peer to peer con otro cliente. Incluye la direcci�n
 * ip y el puerto por el que se tendr� que conectar el otro cliente y el nombre
 * de ese otro cliente
 * 
 * @author Javier Guzm�n
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
