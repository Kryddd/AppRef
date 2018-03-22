package servicesBRi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import services.ClasseInvalideException;
import services.Service;
import services.ServiceManager;
import users.Programmeur;

/**
 * Classe du service utilisateur BRi Programmeur
 * 
 * @author couderc1
 * @version 1.0
 */
public class ServiceBRiProg extends ServiceBRi {

	/**
	 * Liste des programmeurs
	 */
	private static ArrayList<Programmeur> lProgs;

	/**
	 * Initialise la liste des programmeurs
	 * 
	 * @param progs
	 */
	public static void initProgs(ArrayList<Programmeur> progs) {
		ServiceBRiProg.lProgs = progs;
	}

	
	private BufferedReader in;
	private PrintWriter out;
	private boolean quit;
	
	/**
	 * Constructeur du service
	 * 
	 * @param sock
	 */
	public ServiceBRiProg(Socket sock) {
		super(sock);
		quit = false;
	}

	@Override
	public void run() {
		System.out.println("Connexion programmeur " + getNumService() + " demarree");

		try {
			in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
			out = new PrintWriter(super.getSocket().getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String entree = "";
		
		// COMMUNICATION CLIENT-SERVEUR
		while (!quit) {

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
					addService();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "2":
				rmService();
			case "3":
				editService();
			default:
				out.println("Commande invalide");
			}
		}

		// FIN DE CONNEXION
		System.out.println("Connexion programmeur " + getNumService() + " terminee");
		try {
			super.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String menu() throws IOException {
		out.println("Tappez le chiffre correspondant à l'opération demandée : ##" 
				+ "0. Quitter ##"
				+ "1. Ajouter un service ##"
				+ "2. Supprimer un service ##"
				+ "3. Modifier un service");
		return in.readLine();
	}
	
	private void addService() throws IOException {
		// TODO Identification
		
		out.println("Chemin de la classe classe à charger :");
		String URLFileDir = "ftp://" + in.readLine(); 
		URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(URLFileDir)});
		
		out.println("Nom de la classe à charger :");
		String className = in.readLine();
		Class<?> classLoaded = null;
		
		try {
			classLoaded = urlcl.loadClass(className);
		} catch (ClassNotFoundException e) {
			out.println("Nom de classe invalide!");
		}
		
		try {
			ServiceManager.addService((Class<? extends Service>) classLoaded);
		} catch (InstantiationException | IllegalAccessException | ClasseInvalideException e) {
			e.printStackTrace();
		}
		
	}

	private void rmService() {
		// TODO Identification
		
	}

	private void editService() {
		// TODO Identification
		
	}
}
