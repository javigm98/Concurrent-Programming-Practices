package parte2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * Cliente de nuestro sistema de ficheros. Almacena toda la información
 * necesaria para cada cliente y cuenta con getters para todos sus atributos
 * 
 * @author Javier Guzmán Muñoz
 *
 */
public class Cliente {

	private String nombreUsuario;
	private InetAddress ip;

	/*
	 * puertoBase es el puerto a partir del cual se conectarán los clientes para las
	 * conexiones peer to peer. Su valor se irá incrementando módulo 100
	 * (puertoActual) según se vayan conectando clientes. Al llegar a las 100
	 * conexiones volverá al valor inicial, confiando en que ya se hayan liberado.
	 * Como no se especifica en el enunciado no lo hacemos más exhaustivo pero sí
	 * que garantizamos el acceso en exclusión mutua a puertoActual para evitar
	 * conectar dos emisores al mismo puerto.
	 */

	private static final int MAX_CONEXIONES = 100;
	private int puertoBase;

	// PuertoAct mide el incremento de puertoBase
	private int puertoAct = 0;
	private Socket s;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	// Mutex para el acceso en exclusion mutua a puertoAct
	private Semaphore mutexPort = new Semaphore(1);

	public Cliente(String nombreUsuario, InetAddress ip, int puertoBase, Socket s, ObjectInputStream input,
			ObjectOutputStream output) {
		super();
		this.nombreUsuario = nombreUsuario;
		this.ip = ip;
		this.puertoBase = puertoBase;
		this.s = s;
		this.input = input;
		this.output = output;

	}

	public InetAddress getIp() {
		return ip;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * Este método accede en exclusión mutua al puerto, toma su valor actual y lo
	 * devuelve. Además incrementa en uno (modulo MAX_CONEXIONES) el valor de este
	 * atributo para que la siguiente petición se realice por otro puerto
	 * 
	 * @return El puerto por el que podemos conectar al emisor
	 */
	public int getPuertoBase() {
		int puerto = 0;
		try {
			// Accedemos en exclusion mutua.
			mutexPort.acquire();
			puerto = puertoBase + puertoAct;
			puertoAct = (puertoAct + 1) % MAX_CONEXIONES;
			mutexPort.release();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return puerto;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public Socket getS() {
		return s;
	}

}
