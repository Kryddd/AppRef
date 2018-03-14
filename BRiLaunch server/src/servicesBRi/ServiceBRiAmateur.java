package servicesBRi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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

	
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	/**
	 * Constructeur du service
	 * 
	 * @param sock
	 */
	public ServiceBRiAmateur(Socket sock) {
		super(sock);
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

		boolean quit = false;
		String entree = "";

		
		// COMMUNICATION CLIENT-SERVEUR
		while (!quit) {
			try {
				entree = in.readLine();
			} catch (IOException e) {
				entree = "";
			}
			
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
		out.println("Tappez le chiffre correspondant à l'opération demandée : \n" 
				+ "0. Quitter \n"
				+ "1. Choisir un Service \n");
		return in.readLine();
	}
	
	private void services() throws IOException {
		out.println(ServiceManager.servicesList()
				+ "Choisissez un service à lancer (0 si aucun)\n");

		// TODO Lancer le service demandé
	}
}
