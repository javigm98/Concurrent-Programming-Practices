package parte1;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class GestionaClient implements Runnable {
	private Socket socket;

	public GestionaClient(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			InputStream inputS = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputS));
			String nombre = reader.readLine();
			System.out.println("Quiero leer del fichero " + nombre);
			

			OutputStream outputS = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(outputS, true);

			File archivo = new File(nombre);
			if (!archivo.exists()) {
				writer.println("ERROR: no existe el archivo");
			} else {
				BufferedReader lectArchivo = new BufferedReader(new FileReader(archivo));
				lectArchivo.transferTo(writer);
				writer.flush();
				lectArchivo.close();
			}
			writer.close();
			outputS.close();
			inputS.close();
			reader.close();
			
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
