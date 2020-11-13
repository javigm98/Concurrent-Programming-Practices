package parte1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public static void main (String[] args) {
		if(args.length < 1) return;
		int port = Integer.parseInt(args[0]);
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			System.out.println(localhost);
			System.out.println("De qué fichero quieres leer?: ");
			Scanner scan = new Scanner(System.in);
			String nombre = scan.nextLine();
			Socket socket = new Socket(localhost, port);
			
			OutputStream outputS = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(outputS, true);
			writer.println(nombre);
			
			InputStream inputC = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputC));
			String contenido = "";
			String linea;
			
			while((linea = reader.readLine())!= null) {
				contenido += linea + '\n';
			}
			System.out.println("Contenido del mensaje:\n" + contenido);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
