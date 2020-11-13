package parte2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Hilo que lanzar�n los clientes para recibir un fichero que pidi� directamente
 * de otro cliente mediante una comunicaci�n peer to peer.
 * 
 * @author Javier Guzm�n
 *
 */
public class Receptor extends Thread {
	private InetAddress ip;
	private int port;

	/**
	 * 
	 * @param ip   direcci�n ip en la que est� el emisor del fichero
	 * @param port puerto por el que debemos conectarnos con el emisor
	 */
	public Receptor(InetAddress ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			// Nos conectamos con el emisor por la ip y puerto indicados
			Socket socket = new Socket(ip, port);
			// Accedemos al flujo de entrada (solo quiero recibir datos)
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String contenido = "";
			String linea;

			// Recibimos y mostramos por pantalla el contenido del mensaje como lo hac�amos
			// en la parte1 de la pr�ctica
			while ((linea = reader.readLine()) != null) {
				contenido += linea + '\n';
			}
			System.out.println("Contenido del mensaje:\n" + contenido + '\n');

			// Cerramos este lado de la comuniaci�n
			reader.close();
			input.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
