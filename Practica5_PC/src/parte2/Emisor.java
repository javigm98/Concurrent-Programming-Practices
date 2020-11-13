package parte2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hilo que lanza un cliente para que establezca un serverSocket con el que se
 * comunicará con un receptor de otro cliente al que mandará un fichero concreto
 * 
 * @author Javier Guzmán Muñoz
 *
 */
public class Emisor extends Thread {
	private int port;
	private String nombreFich;

	/**
	 * 
	 * @param port       Puerto por el que esperaremos a que un receptor se conecte
	 * @param nombreFich Nombre del fichero que debemos enviar al receptor
	 */
	public Emisor(int port, String nombreFich) {
		super();
		this.port = port;
		this.nombreFich = nombreFich;
	}

	@Override
	public void run() {
		try {
			// Establecemos un ServerSocket
			ServerSocket serverSocket = new ServerSocket(port);
			// Esperamos a que el receptor se nos conecte
			Socket s = serverSocket.accept();
			// Accedemos al flujo de salida (vamos a enviar cosas solo)
			OutputStream output = s.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);

			// Abrimos el archivo
			File archivo = new File(nombreFich);
			// Si no se ha podido abrir mandamos un mensaje informando
			if (!archivo.exists())
				writer.println("No se ha podido abrir el archivo " + nombreFich);
			else {
				// Mandamos el contendio del fichero por el socket
				BufferedReader lectArchivo = new BufferedReader(new FileReader(archivo));
				lectArchivo.transferTo(writer);
				writer.flush();
				lectArchivo.close();
			}
			// Cerramos la conexion
			writer.close();
			output.close();
			s.close();
			serverSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
