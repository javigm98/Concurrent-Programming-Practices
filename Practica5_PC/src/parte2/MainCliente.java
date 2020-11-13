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
 * Clase que implementa el m�todo main de cada uno de los clientes de la
 * aplicaci�n. Comienza pidiendo el nombre de usuario al cliente y despues le
 * muestra un menu con las opciones que tiene disponibles hasta que el cliente
 * selecciona salir
 * 
 * @author Javier Guzm�n Mu�oz
 *
 */
public class MainCliente {
	/**
	 * Lleva la funcion main de los clientes que adem�s estar�n asociados a otro
	 * objeto cliente que guarde toda la informaci�n necesaria
	 * 
	 * @param args Cadena de strings con los siguientes valores - args[0] Puerto
	 *             base desde el que se establecer� la conexi�n para las
	 *             comunicaciones peer to peer con este cliente Hay que asegurarse
	 *             de que cada cliente tenga un rango distinto si todos se lanzan
	 *             desde la misma m�quina, esto es por ejemplo, podemos poner al
	 *             primer cliente el 600 (y emepzar� a establecer conexiones con
	 *             otros clientes a partir del 600, 601...), al segundo cliente el
	 *             700 y as� sucesivamente, teniendo siempre en cuenta que no se
	 *             solapen los puertos de distintos clientes - args[1] Puerto desde
	 *             el que se conectar�n los clientes con el servidor (es el puerto
	 *             desde el que el servidor crear� su ServerSocket) - Opcionalmente
	 *             detr�s de estos valores, args puede llevar m�s Strings con
	 *             nombres de archivos v�lidos, que ser�n los que se le asociar�n a
	 *             este usuario en caso de que no estuviese registrado en el fichero
	 *             users.txt cuando arrancamos el servidor. Es decir, si al arrancar
	 *             el servidor este cliente aparece en el fichero 'users.txt' se
	 *             cargar� de ah� su informaci�n y solo se cambiar� para que
	 *             aparezca como activo. Si por el contrario no aparece su id en
	 *             este fichero se incluir� en las tablas correspondientes y estos
	 *             String que hemos pasado como args ser�n los ficheros a los que
	 *             est� aociado. Si no hemos pasado ninguno, este user no tendr�
	 *             ning�n fichero asociado.
	 */
	public static void main(String[] args) {
		try {
			// M�nimo tenemos que pasar los dos numeros de puertos
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

			// Mostramos un cuadro de di�logo en el que introducimos el nombre de usuario
			// del cliente
			String nombreUsuario = JOptionPane.showInputDialog(null, "Introduzca nombre de usuario: ");

			// Creamos el socket con el servidor, que suponemos que estr� esperando en un
			// accept.
			Socket socket = new Socket(ip, portServidor);
			// Extraemos los flujos correspondientes al socket.
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

			// Creamos un objeto cliente con toda esta informaci�n
			Cliente cliente = new Cliente(nombreUsuario, ip, portBase, socket, input, output);

			// Arrancamos un nuevo hilo OyenteServidor para este cliente
			(new OyenteServidor(cliente)).start();

			// Mandamos un mensaje para establecer la conexion, por el que pasamos la lista
			// de ficheros por si tuviesen que a�adirse a la tabla
			MensajeConexion mConex = new MensajeConexion(nombreUsuario, Servidor.server_name, listaFicheros);
			output.writeObject(mConex);

			/*
			 * Para cada cliente creamos un JFrame que ser� su propia interfaz para pedir
			 * acciones con el servidor y sobre el que aparecer�n tres botones que nos
			 * indicar�n las acciones que el usuario puede realizar con el servidor.
			 * 
			 * La idea de llevar un JFrame por cliente es que se nos abra una pesta�a con
			 * cada uno y nos sea m�s f�cil tener abiertos a la vez varios clientes y
			 * cambiar entre ellos.
			 * 
			 * Se incluye tambi�n, comentado, el c�digo de la aplicaci�n sin esta peque�a
			 * interfaz gr�fica por si la interfaz diese problemas.
			 * 
			 * Todo el tiempo la salida ser� por consola
			 * 
			 * OBSERVACION: Si al ejecutar el cliente desde eclipse no se ven los botones en
			 * el JFrame, minimizar y volver a ampliar la pesta�a lo solucionar�a.
			 * 
			 */

			// Creamos el JFrame que ser� la interfaz
			JFrame frame = new JFrame("Interfaz del cliente " + nombreUsuario);
			frame.setSize(400, 200);
			frame.setVisible(true);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			// Creamos los botones con las diferentes acciones que puede realizar el usuario
			JButton op1 = new JButton("1.- Consultar la lista de usuarios conectados");
			JButton op2 = new JButton("2.- Pedir un fichero");
			JButton op3 = new JButton("3.- Salir");
			JLabel nombre = new JLabel("�Qu� desea realizar?");

			// A cada boton le a�adimos la acci�n que tiene que realizar cuando sea pulsado.
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

			// A�adimos los botones a la interfaz
			frame.getContentPane().setLayout(new GridLayout(4, 1));
			frame.getContentPane().add(nombre);
			frame.getContentPane().add(op1);
			frame.getContentPane().add(op2);
			frame.getContentPane().add(op3);

			// Codigo para E/S por consola (Descomentar si se quiere usar)

			/*
			 * int op = 0; Scanner scan = new Scanner(System.in); do {
			 * System.out.println("�Qu� desea realizar?");
			 * System.out.println("1.- Consultar la lista de usuarios conectados");
			 * System.out.println("2.- Pedir un fichero"); System.out.println("3.- Salir");
			 * System.out.println("Introduzca la opci�n deseada"); op =
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
