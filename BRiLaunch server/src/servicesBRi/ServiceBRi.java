package servicesBRi;

import java.io.IOException;
import java.net.Socket;

/**
 * Classe abstraite des services utilisateurs BRi
 * @author couderc1
 * @version 1.0
 */
public abstract class ServiceBRi implements Runnable {

	private static int compteurNum = 0; // Compteur de service
	private int numService; // Numéro du service

	private Socket sockClient;
	
	/**
	 * Constructeur du service
	 * @param sock
	 */
	public ServiceBRi(Socket sock) {
		this.sockClient = sock;
		numService = compteurNum++;
	}

	
	/**
	 * Lancement du thread du service
	 */
	public void start() {
		(new Thread(this)).start();
	}
	
	/**
	 * Getter du socket
	 * @return socket
	 */
	public Socket getSocket() {
		return this.sockClient;
	}
	
	/**
	 * Getter du numéro du service
	 * @return Num du service
	 */
	public int getNumService() {
		return numService;
	}

	@Override
	public abstract void run();
	
	protected void finalize() throws IOException {
		sockClient.close();
	}
}
