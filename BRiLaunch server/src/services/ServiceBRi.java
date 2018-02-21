package services;

import java.net.Socket;

public class ServiceBRi implements Runnable {

	public ServiceBRi(Socket sock) {
		// TODO Auto-generated constructor stub
	}

	// lancement du service
	public void start() {
		(new Thread(this)).start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
