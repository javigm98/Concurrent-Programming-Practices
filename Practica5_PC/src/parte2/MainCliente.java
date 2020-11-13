package parte2;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//import java.util.Scanner; (Para E/S por consola)

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Clase que implementa el método main de cada uno de los clientes de la
 * aplicación. Comienza pidiendo el nombre de usuario al cliente y despues le
 * muestra un menu con las opciones que tiene disponibles hasta que el cliente
 * selecciona salir
 * 
 * @author Javier Guzmán Muñoz
 *
 */
public class MainCliente {
	/**
	 * Lleva la funcion main de los clientes que además estarán asociados a otro
	 * objeto cliente que guarde toda la información necesaria
	 * 
	 * @param args Cadena de strings con los siguientes valores - args[0] Puerto
	 *             base desde el que se establecerá la conexión para las
	 *             comunicaciones peer to peer con este cliente Hay que asegurarse
	 *             de que cada cliente tenga un rango distinto si todos se lanzan
	 *             desde la misma máquina, esto es por ejemplo, podemos poner al
	 *             primer cliente el 600 (y emepzará a establecer conexiones con
	 *             otros clientes a partir del 600, 601...), al segundo cliente el
	 *             700 y así sucesivamente, teniendo siempre en cuenta que no se
	 *             solapen los puertos de distintos clientes - args[1] Puerto desde
	 *             el que se conectarán los clientes con el servidor (es el puerto
	 *             desde el que el servidor creará su ServerSocket) - Opcionalmente
	 *             detrás de estos valores, args puede llevar más Strings con
	 *             nombres de archivos válidos, que serán los que se le asociarán a
	 *             este usuario en caso de que no estuviese registrado en el fichero
	 *             users.txt cuando arrancamos el servidor. Es decir, si al arrancar
	 *             el servidor este cliente aparece en el fichero 'users.txt' se
	 *             cargará de ahí su información y solo se cambiará para que
	 *             aparezca como activo. Si por el contrario no aparece su id en
	 *             este fichero se incluirá en las tablas correspondientes y estos
	 *             String que hemos pasado como args serán los ficheros a los que
	 *             esté aociado. Si no hemos pasado ninguno, este user no tendrá
	 *             ningún fichero asociado.
	 */
	public static void main(String[] args) {
		try {
			// Mínimo tenemos que pasar los dos numeros de puertos
			if (args.length < 2)
				return;
			int portBase = Integer.parseInt(args[0]);
			int portServidor = Integer.parseInt(args[1]);
			// Consideramos la dir ip de la maquina actual
			InetAddress ip = InetAddress.getLocalHost();

			// Si hubiesemos pasado algun nombre de fichero hacemos con ellos una lista.
			List<String> listaFicheros = new ArrayList<>();
			for (int i = 2; i < args.length; ++i) {
				listaFicheros.add(args[i]);
			}
			// Scanner scan = new Scanner(System.in);
			// System.out.println("Introduzca nombre de usuario: ");
			// String nombreUsuario = scan.nextLine();

			// Mostramos un cuadro de diálogo en el que introducimos el nombre de usuario
			// del cliente
			String nombreUsuario = JOptionPane.showInputDialog(null, "Introduzca nombre de usuario: ");

			// Creamos el socket con el servidor, que suponemos que estrá esperando en un
			// accept.
			Socket socket = new Socket(ip, portServidor);
			// Extraemos los flujos correspondientes al socket.
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

			// Creamos un objeto cliente con toda esta información
			Cliente cliente = new Cliente(nombreUsuario, ip, portBase, socket, input, output);

			// Arrancamos un nuevo hilo OyenteServidor para este cliente
			(new OyenteServidor(cliente)).start();

			// Mandamos un mensaje para establecer la conexion, por el que pasamos la lista
			// de ficheros por si tuviesen que añadirse a la tabla
			MensajeConexion mConex = new MensajeConexion(nombreUsuario, Servidor.server_name, listaFicheros);
			output.writeObject(mConex);

			/*
			 * Para cada cliente creamos un JFrame que será su propia interfaz para pedir
			 * acciones con el servidor y sobre el que aparecerán tres botones que nos
			 * indicarán las acciones que el usuario puede realizar con el servidor.
			 * 
			 * La idea de llevar un JFrame por cliente es que se nos abra una pestaña con
			 * cada uno y nos sea más fácil tener abiertos a la vez varios clientes y
			 * cambiar entre ellos.
			 * 
			 * Se incluye también, comentado, el código de la aplicación sin esta pequeña
			 * interfaz gráfica por si la interfaz diese problemas.
			 * 
			 * Todo el tiempo la salida será por consola
			 * 
			 * OBSERVACION: Si al ejecutar el cliente desde eclipse no se ven los botones en
			 * el JFrame, minimizar y volver a ampliar la pestaña lo solucionaría.
			 * 
			 */

			// Creamos el JFrame que será la interfaz
			JFrame frame = new JFrame("Interfaz del cliente " + nombreUsuario);
			frame.setSize(400, 200);
			frame.setVisible(true);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			// Creamos los botones con las diferentes acciones que puede realizar el usuario
			JButton op1 = new JButton("1.- Consultar la lista de usuarios conectados");
			JButton op2 = new JButton("2.- Pedir un fichero");
			JButton op3 = new JButton("3.- Salir");
			JLabel nombre = new JLabel("¿Qué desea realizar?");

			// A cada boton le añadimos la acción que tiene que realizar cuando sea pulsado.
			op1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						// Creamos y mandamos un nuevo MensajeListaUsuarios.
						MensajeListaUsuarios mLista = new MensajeListaUsuarios(nombreUsuario, Servidor.server_name);
						output.writeObject(mLista);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			});

			op2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						// Introducimos el nombre del fichero que queremos buscar
						String nombreFich = JOptionPane.showInputDialog(frame,
								"Introduzca el nombre del fichero que quieres consultar ");

						// Creamos y mandamos el correspondiente MensajePedirFichero con el nombre que
						// hayan introducido
						MensajePedirFichero mPedirFich = new MensajePedirFichero(nombreUsuario, Servidor.server_name,
								nombreFich);
						output.writeObject(mPedirFich);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			});

			op3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						// Creamos y mandamos el correspondiente MensajeCerrarConexion de este usuario
						MensajeCerrarConexion mCerrConex = new MensajeCerrarConexion(nombreUsuario,
								Servidor.server_name);
						output.writeObject(mCerrConex);

						// Cerramos el JFrame (interfaz)
						frame.dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			});

			// Añadimos los botones a la interfaz
			frame.getContentPane().setLayout(new GridLayout(4, 1));
			frame.getContentPane().add(nombre);
			frame.getContentPane().add(op1);
			frame.getContentPane().add(op2);
			frame.getContentPane().add(op3);

			// Codigo para E/S por consola (Descomentar si se quiere usar)

			/*
			 * int op = 0; Scanner scan = new Scanner(System.in); do {
			 * System.out.println("¿Qué desea realizar?");
			 * System.out.println("1.- Consultar la lista de usuarios conectados");
			 * System.out.println("2.- Pedir un fichero"); System.out.println("3.- Salir");
			 * System.out.println("Introduzca la opción deseada"); op =
			 * Integer.parseInt(scan.nextLine());
			 * 
			 * switch (op) { case 0: // Creamos y mandamos al servidor un mensaje
			 * ListaUsuarios para pedirle la lista // de los usuarios conectados
			 * MensajeListaUsuarios mLista = new MensajeListaUsuarios(nombreUsuario,
			 * Servidor.server_name); output.writeObject(mLista); break; case 1: // E/S por
			 * consola //
			 * System.out.println("Introduzca el nombre del fichero que quieres consultar");
			 * // String nombreFich = scan.nextLine();
			 * 
			 * // Creamos y mandamos el correspondiente MensajePedirFichero con el nombre
			 * que // hayan introducido MensajePedirFichero mPedirFich = new
			 * MensajePedirFichero(nombreUsuario, Servidor.server_name, nombreFich);
			 * output.writeObject(mPedirFich); break; case 2:
			 * 
			 * // Creamos y mandamos el correspondiente MensajeCerrarConexion de este
			 * usuario MensajeCerrarConexion mCerrConex = new
			 * MensajeCerrarConexion(nombreUsuario, Servidor.server_name);
			 * output.writeObject(mCerrConex); break; }
			 * 
			 * } while (op != 2); // Repetimos el menu mientras no queramos salir
			 */

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
