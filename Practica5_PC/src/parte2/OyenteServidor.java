package parte2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * Hilo que lanzará el cliente para recibir los datos que solicite al servidor.
 * 
 * Incluye como atributo una referencia al cliente al que está asociado, el cual
 * contiene toda la información necsaria para llevar a acabo estas conexiones
 * 
 * @author Javier Guzmán
 *
 */

public class OyenteServidor extends Thread {
	private Cliente cliente;

	public OyenteServidor(Cliente cliente) {
		super();
		this.cliente = cliente;
	}

	@Override
	public void run() {
		try {
			// Accedemos a los flujos de entrada y de salida del cliente con el servidor
			ObjectInputStream input = cliente.getInput();
			ObjectOutputStream output = cliente.getOutput();

			// Accedemos al nombre del cliente al que estamos asociados
			String nombreUsuario = cliente.getNombreUsuario();

			// Escucharemos peticiones del servidor hasta que no queramos cerrar la conexión
			// con este.

			// Este booleano indicará cuando hemos terminado esta conexión
			boolean terminado = false;
			while (!terminado) {
				Mensaje m = (Mensaje) input.readObject();
				switch (m.getTipo()) {
				case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
					// Imprimimos un mensaje informando al cliente del cierre de la conexión
					System.out.println("Conexión con el servidor cerrada. Hasta pronto!");

					// Cerramos la conexión a este lado del socket
					cliente.getInput().close();
					cliente.getOutput().close();
					cliente.getS().close();

					// Indicamos que ya no queremos recibir más mensajes del servidor
					terminado = true;
					break;
				case MENSAJE_CONFIRMACION_CONEXION:
					// Indicamos al cliente que la conexión con el servidor se estableció
					// correctamente
					System.out.println("Establecida Conexión de " + nombreUsuario + " con el servidor");
					break;
				case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
					// Obtenemos la lista de usuarios que nos manda el servidor y la imprimimos por
					// la consola
					List<String> listaUsers = ((MensajeConfirmacionListaUsuarios) m).getUsuarios();
					System.out.println("Lista de usuarios conectados");
					for (String user : listaUsers) {
						System.out.println("\t" + user);
					}
					break;
				case MENSAJE_EMITIR_FICHERO:
					// Obtenemos información sobre que mensaje y a quién se lo debemos emitir
					MensajeEmitirFichero mEmitir = (MensajeEmitirFichero) m;
					String nombreDest = mEmitir.getNombreCliente();
					String nombreFich = mEmitir.getNombreFich();

					// Acceso (en exclusión mutua) al puerto correspondiente por el que este cliente
					// puede crear conexiones con otros. Además este método incrementa en uno el
					// número de puerto para que nadie más intente crear una conexión por él
					// mientras esta comunicación peer to peer esté activa
					int puertoCliente = cliente.getPuertoBase();

					// Preparamos el mensaje que debemos envir de vuelta al servidor para indicarle
					// que ya hemos creado el emisor para la comuniación peer to peer
					MensajePreparadoClienteServidor mPrepCS = new MensajePreparadoClienteServidor(nombreUsuario,
							Servidor.server_name, nombreDest, cliente.getIp(), puertoCliente);

					output.writeObject(mPrepCS);
					// Arrancamos el nuevo hilo emisor
					(new Emisor(puertoCliente, nombreFich)).start();
					break;
				case MENSAJE_PREPARADO_SERVIDORCLIENTE:
					// Creamos un nuevo receptor que se conectará a la dir ip y el puerto indicados
					MensajePreparadoServidorCliente mPrepSC = (MensajePreparadoServidorCliente) m;
					(new Receptor(mPrepSC.getIp(), mPrepSC.getPort())).start();
					break;
				case MENSAJE_FICHERO_INEXISTENTE:
					// En caso de que hayamos solicitado un mensaje que no pertenece a ningún
					// usuario conectado, recibiremos este menaje e informamos por la consola de
					// ello
					MensajeFicheroInexistente mFichInex = (MensajeFicheroInexistente) m;
					System.out.println(
							"El fichero " + mFichInex.getNombreFich() + " no pertenece a ningún usuario conectado");
					break;
				default:
					break;

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
