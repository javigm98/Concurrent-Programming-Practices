package parte1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		if(args.length < 1) return;
		int port = Integer.parseInt(args[0]);
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(true) {
				Socket socket = serverSocket.accept();
				Runnable gestor = new GestionaClient(socket);
				new Thread(gestor).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
