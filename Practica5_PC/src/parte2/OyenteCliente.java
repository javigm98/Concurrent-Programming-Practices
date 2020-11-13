package parte2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Hilo que lanza el servidor para cada usuario que se conecta a él y que será
 * el encargado de recibir y gestionar las peticiones de ese cliente.
 * 
 * Incluye el socket y los flujos de entrada y salida por los que se comunicará
 * con el cliente.
 * 
 * @author Javier Guzmán
 *
 */
public class OyenteCliente extends Thread {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public OyenteCliente(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			// Accedemos a los flujos de entrada y de salida del socket
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());

			// El booleano terminado indicará si hay que seguir esuchando al cliente o no
			boolean terminado = false;
			while (!terminado) {
				// Recibimos un mensaje del cliente
				Mensaje m = (Mensaje) input.readObject();

				// Vemos quien lo manda
				String usuario = m.getOrigenId();
				switch (m.getTipo()) {
				case MENSAJE_CONEXION:
					// Accedemos en exclusión mutua a la tabla información que contiene los usuarios
					// registrados en el fichero 'users.txt'
					Servidor.mutexInfo.acquire();

					// Si este usuario no estaba en 'users.txt' lo añadimos, poniendo como ficheros
					// asociados aquellos que nos han llegado como parate del mensaje
					if (!Servidor.informacion.containsKey(usuario)) {
						Servidor.informacion.put(usuario, ((MensajeConexion) m).getFicheros());
					}
					Servidor.mutexInfo.release();

					// Acedemos a la tabla usuarios del servidor en exclusión mutua
					Servidor.mutexUsers.acquire();

					// Añadimos los flujos correspondientes a este usuario que estamos conectando
					// con el servidor
					Servidor.usuarios.put(usuario, new InfoUsuarios(input, output));
					Servidor.mutexUsers.release();

					// Mandamos un mensaje al cliente indicandole que ya se ha establecido la
					// conexión con el servidor
					MensajeConfirmacionConexion mConfConex = new MensajeConfirmacionConexion(Servidor.server_name,
							usuario);
					output.writeObject(mConfConex);
					break;
				case MENSAJE_LISTA_USUARIOS:
					// Accedemos a la tabla de usuarios conectados del servidor en exclusión mutua
					Servidor.mutexUsers.acquire();

					// Creamos una lista con esos usuarios
					List<String> usuarios_activos = new ArrayList(Servidor.usuarios.keySet());
					Servidor.mutexUsers.release();

					// Mandamos un mensaje al cliente que incluye esa lista de usuarios
					MensajeConfirmacionListaUsuarios mConfLista = new MensajeConfirmacionListaUsuarios(
							Servidor.server_name, usuario, usuarios_activos);
					output.writeObject(mConfLista);
					break;
				case MENSAJE_CERRAR_CONEXION:
					// Accedemos a la tabla de usuarios conectados al servidor en exclusión mutua
					Servidor.mutexUsers.acquire();
					// Eliminamos la información relativa a este usuario
					Servidor.usuarios.remove(usuario);
					Servidor.mutexUsers.release();

					// Mandamos una confirmación de que ya se ha cerrado la conexión al cliente
					MensajeConfirmacionCerrarConexion mConfCerr = new MensajeConfirmacionCerrarConexion(
							Servidor.server_name, usuario);

					// Cerramos en este lado del canal la comunicación con el cliente
					output.writeObject(mConfCerr);
					output.close();
					input.close();
					socket.close();

					// Indicamos que ya no esperamos más mensajes del cliente
					terminado = true;
					break;
				case MENSAJE_PEDIR_FICHERO:
					// Obtenemos el nombre del fichero que pide el cliente
					String nombreFich = ((MensajePedirFichero) m).getNombreFich();
					// Accedemos en exclusión mutua a la tabla de informacion
					Servidor.mutexInfo.acquire();

					// Buscamos si pertenece a algún usuario registrado (podría no estar conectado)
					String propietario = null;
					for (Map.Entry<String, List<String>> mensajes : Servidor.informacion.entrySet()) {
						if (mensajes.getValue().indexOf(nombreFich) != -1) {
							propietario = mensajes.getKey();
							break;
						}
					}
					Servidor.mutexInfo.release();
					if (propietario != null) {
						// Si pertenece a algún usuario registrado vemos si ese usuario está conectado
						// Accedemos en exclusión mutua a la tabla de usuarios conectados
						Servidor.mutexUsers.acquire();
						if (Servidor.usuarios.get(propietario) == null)
							// Si el propietario no está conectado informamos al usuario
							output.writeObject(
									new MensajeFicheroInexistente(Servidor.server_name, usuario, nombreFich));
						else {
							// Si el propietario está conectado le mandamos un mensaje para que emita el
							// fichero indicándole que usuarió lo pide y qué fichero es este que pide
							ObjectOutputStream fout2 = Servidor.usuarios.get(propietario).getFout();
							MensajeEmitirFichero mEmitir = new MensajeEmitirFichero(Servidor.server_name, propietario,
									usuario, nombreFich);

							fout2.writeObject(mEmitir);
						}
						Servidor.mutexUsers.release();
					} else
						// Si el fichero no pertenece a ningún usuario registrado informamos al cliente
						output.writeObject(new MensajeFicheroInexistente(Servidor.server_name, usuario, nombreFich));
					break;
				case MENSAJE_PREPARADO_CLIENTESERVIDOR:
					// Vemos a qué cliente debemos indicarle que ya puede establecer un Receptor
					// para recibir el mensaje que pidió en una conexión peer to peer
					String destInfo = ((MensajePreparadoClienteServidor) m).getDestInfo();

					// Accedemos en exclusión mutua a la tabla de usuarios conectados
					Servidor.mutexUsers.acquire();
					// Obtenemos el flujo por el que podemos mandarle mensajes a ese cliente
					ObjectOutputStream fout1 = Servidor.usuarios.get(destInfo).getFout();
					Servidor.mutexUsers.release();

					// Le mandamos un mensaje al cliente que recibirá el fichero indicándole la ip y
					// el puerto por el que se debe conectar para recibirlo
					MensajePreparadoServidorCliente mPrepServClient = new MensajePreparadoServidorCliente(
							Servidor.server_name, destInfo, ((MensajePreparadoClienteServidor) m).getIp(),
							((MensajePreparadoClienteServidor) m).getPort());
					fout1.writeObject(mPrepServClient);
					break;

				default:
					break;

				}
			}

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
