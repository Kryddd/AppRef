package serveurs;

import java.io.IOException;
import java.net.ServerSocket;

import services.ServiceBRi;

public class ServeurBRi implements Runnable {

	private ServerSocket listenSocket;
	
	public ServeurBRi(int port, Class<?> classeService) {
		try {
			listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void run() {
		try {
			while(true)
				new ServiceBRi(listenSocket.accept()).start();
		}
		catch (IOException e) { 
			try {this.listenSocket.close();} catch (IOException e1) {}
			System.err.println("Pb sur le port d'�coute :"+e);
		}
	}
	
	protected void finalize() throws Throwable {
		try {
			this.listenSocket.close();
		} catch (IOException e1) {}
	}

}
