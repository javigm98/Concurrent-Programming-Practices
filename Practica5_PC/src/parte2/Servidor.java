package parte2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Servidor de la aplicación. Posee un main y varios atributos estáticos ya que
 * solo lanzaremos un servidor en nuestra aplicación
 * 
 * @author Javier Guzmán
 *
 */
public class Servidor {
	// Nombre del servidor para poner como origen o destino en los mensajes
	protected static String server_name = "Server";

	// Tabla hash con información de los usuarios registrados en el sistema. Cada
	// entrada se corresponde con un nombre de usuario y los ficheros que lleva
	// asociados. Esta tabla se rellena con la información que leeremos de
	// 'users.txt'
	protected static Map<String, List<String>> informacion = new HashMap<>();

	// Tabla hash con información de los usuarios activos en el sistema. Para cada
	// usuario contiene el flujo de entrda y el de salida por el que se comunican
	// con el servidor.
	protected static Map<String, InfoUsuarios> usuarios = new HashMap<>();

	// Dirección IP del servidor
	protected static InetAddress localhost;

	// Puerto por el que recibe conexiones el servidor
	protected static int port;

	// Socket del servidor
	private static ServerSocket serverSocket;

	// Mutex para el acceso en exclusión mutua a la tabla información
	protected static Semaphore mutexInfo = new Semaphore(1);

	// Mutex para el acceso en exclusión mutua a la tabla usuarios
	protected static Semaphore mutexUsers = new Semaphore(1);

	public static void main(String[] args) {
		if (args.length < 1)
			return;
		// Nos tienen que pasar el puerto del servidor como parámetro
		port = Integer.parseInt(args[0]);
		try {
			// Leemos la información inicial del fichero 'users.txt'
			File usersFile = new File("users.txt");
			if (!usersFile.exists()) {
				// Apagamos el servidor en caso de error
				System.out.println("No se ha podido cargar el fichero 'users.txt'");
				return;
			}
			leeUsuarios(usersFile);
			localhost = InetAddress.getLocalHost();
			serverSocket = new ServerSocket(port);

			// Esperamos nuevas conexiones en un accept continuo y creamos un nuevo oyente
			// para cada petición de cliente que nos llegue
			while (true) {
				Socket socket = serverSocket.accept();
				(new OyenteCliente(socket)).start();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Lee el fichero 'users.txt' y rellena la tabla informacion con su contenido
	private static void leeUsuarios(File usersFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(usersFile));
		String linea;
		while ((linea = br.readLine()) != null) {
			String[] palabras = linea.split(" ");
			String nombre = palabras[0];
			List<String> ficheros = new ArrayList<>();
			for (int i = 1; i < palabras.length; ++i) {
				ficheros.add(palabras[i]);
			}
			informacion.put(nombre, ficheros);
		}
	}

}
