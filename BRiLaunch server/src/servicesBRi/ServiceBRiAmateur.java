package servicesBRi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;

import services.NotActivatedException;
import services.Service;
import services.ServiceManager;
import users.Amateur;

/**
 * Classe du service utilisateur BRi Amateur
 * 
 * @author couderc1
 * @version 1.0
 */
public class ServiceBRiAmateur extends ServiceBRi {

	/**
	 * Liste des amateurs
	 */
	private static ArrayList<Amateur> amateurs;

	/**
	 * Initialise la liste des amateurs
	 * 
	 * @param amateurs
	 */
	public static void initProgs(ArrayList<Amateur> amateurs) {
		ServiceBRiAmateur.amateurs = amateurs;
	}

	
	private BufferedReader in;
	private PrintWriter out;
	private boolean quit;
	
	/**
	 * Constructeur du service
	 * 
	 * @param sock
	 */
	public ServiceBRiAmateur(Socket sock) {
		super(sock);
		quit = false;
	}

	@Override
	public void run() {
		System.out.println("Connexion amateur " + getNumService() + " demarree");

		try {
			in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
			out = new PrintWriter(super.getSocket().getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		String entree = "";

		
		// COMMUNICATION CLIENT-SERVEUR
		while (!quit) {
			
			// menu
			try {
				entree = menu();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			switch (entree) {
				case "0":
					quit = true;
					break;
				case "1":
				try {
					services();
				} catch (IOException e) {
					e.printStackTrace();
				}
					break;
				default:
					out.println("Commande invalide");
			}
		}

		// FIN DE CONNEXION
		System.out.println("Connexion amateur " + getNumService() + " terminee");
		try {
			super.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private String menu() throws IOException {
		out.println("> Tappez le chiffre correspondant à l'opération demandée : ##" 
				+ "0. Quitter ##"
				+ "1. Choisir un Service");
		return in.readLine();
	}
	
	private void services() throws IOException {
		out.println(ServiceManager.servicesList()
				+ "Choisissez un service à lancer (0 si vous souhaitez quitter)");
		
		int entree = Integer.parseInt(in.readLine());
		
		if(entree == 0) {
			quit = true;
		}
		else {
			// TODO Identification
			
			Class<? extends Service> classService = null;
	
			try {
				// Récupère la classe du service demandé
				classService = ServiceManager.getService(entree);
				
				// Appel au constructeur
				Service service = classService.getConstructor(Socket.class).newInstance(getSocket());
				
				// Lancement du thread du service
				Thread serviceRunning = new Thread(service);
				serviceRunning.start();
				
				// Attend la fin du service
				serviceRunning.join();
				
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | InterruptedException e) {
				e.printStackTrace();
			} catch(NotActivatedException e1) {
				out.println("Le service n'est pas activé!");
			}
			
		}
	}
}
