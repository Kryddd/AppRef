package serveurs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

import servicesBRi.ServiceBRi;

/**
 * @author couderc1
 *
 */
public class ServeurBRi implements Runnable {

	private ServerSocket listenSocket;
	private Class<? extends ServiceBRi> classeService;

	/**
	 * @param port
	 * @param classeService
	 */
	public ServeurBRi(int port, Class<? extends ServiceBRi> classeService) {
		try {
			listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.classeService = classeService;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				// Créée une instance du service avec le constructeur lorsque le port est solicité
				classeService.getConstructor(Socket.class)
				.newInstance(listenSocket.accept()).start();
			}
		} catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			try {
				this.listenSocket.close();
			} catch (IOException e1) {
			}
			System.err.println("Pb sur le port d'écoute :" + e);
		}
	}

	protected void finalize() throws Throwable {
		
		// Ferme le socket à la destruction de l'instance
		try {
			this.listenSocket.close();
		} catch (IOException e1) {
		}
	}

}
