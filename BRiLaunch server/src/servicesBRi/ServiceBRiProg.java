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
	private boolean error;
	private Programmeur progLogged;

	/**
	 * Constructeur du service
	 * 
	 * @param sock
	 */
	public ServiceBRiProg(Socket sock) {
		super(sock);
		quit = false;
		error = false;
		progLogged = null;
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

			// En cas d'erreur de saisie, la fonction
			// appelée affichera l'erreur au lieu du menu
			if (!error) {
				menu();
			}
			try {
				entree = in.readLine();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			switch (entree) {
			case "0": // Quitter
				quit = true;
				break;
			case "1": // Se connecter
				try {
					login();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case "2": // Ajouter un service
				try {
					addService();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "3": // Supprimer un service
				try {
					rmService();
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}
				break;
			case "4": // Modifier un service
				try {
					editService();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case "5": // Créer un compte
				try {
					register();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "6": // Changer d'adresse FTP
				try {
					changeFTP();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
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

	private void changeFTP() throws IOException {
		if (progLogged == null) {
			out.println("Vous devez etre connecté pour effectuer cette opération!##Saisissez 1 pour vous connecter");
			error = true;
		} else {
			error = false;
			
			out.println("Saissisez votre nouvelle adresse de serveur FTP :");
			
			progLogged.setURLFtp(in.readLine());
		}
		
	}

	private void login() throws IOException {
		error = false;

		out.println("Login :");
		String login = in.readLine();
		out.println("Password :");
		String pwd = in.readLine();

		for (Programmeur p : lProgs) {
			if (p.getLogin().equals(login) && p.getPassword().equals(pwd)) {
				progLogged = p;
				break;
			}
		}

		if (progLogged == null) {
			error = true;
			out.println("L'identifiant ou le mot de passe est incorrect!##" + "Réessayer en saisissant 1");
		}
	}

	/**
	 * Affiche le menu
	 */
	private void menu() {
		out.println("> Tappez le chiffre correspondant à l'opération demandée : ##" + "0. Quitter ##"
				+ "1. S'identifier ##" + "2. Ajouter un service ##" + "3. Supprimer un service ##"
				+ "4. Modifier un service##" + "5. Créer un compte##" + "6.Changer de serveur FTP");
	}

	/**
	 * Créée un compte
	 * 
	 * @throws IOException
	 */
	private void register() throws IOException {
		error = false;

		out.println("Création de compte####Login :");
		String login = in.readLine();
		out.println("Password :");
		String pwd = in.readLine();
		out.println("Serveur FTP :");
		String ftp = in.readLine();

		synchronized (lProgs) {
			
			// Recherche les doublons
			for (Programmeur p : lProgs) {
				if (p.getLogin().equals(login)) {
					error = true;
					out.println("Le login existe déjà!##" + "Reessayer en saisissant 5");
				}
			}

			if (!error) {
				// Ajoute le prog à la liste
				lProgs.add(new Programmeur(login, pwd, ftp));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addService() throws IOException {
		if (progLogged == null) {
			out.println("Vous devez etre connecté pour effectuer cette opération!##Saisissez 1 pour vous connecter");
			error = true;
		} else {
			error = false;

			// Connexion au serveur FTP
			out.println("Chemin de la classe à charger :");
			String URLFileDir = "ftp://" + progLogged.getURLFtp() + in.readLine();
			URLClassLoader urlcl = new URLClassLoader(new URL[] { new URL(URLFileDir) });

			// Chargement de la classe
			out.println("Nom complet de la classe à charger :");
			String className = in.readLine();
			Class<?> classLoaded = null;
			try {
				classLoaded = urlcl.loadClass(className);

				// Ajout de la classe au Service Manager
				ServiceManager.addService((Class<? extends Service>) classLoaded);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				out.println("Nom de classe invalide!##Reessayer en saisissant 2");
				error = true;
			} catch (ClasseInvalideException e) {
				out.println(e.getMessage() + "##Reessayer en saisissant 2");
				in.readLine();
				error = true;
			}

			urlcl.close();

		}
	}

	/**
	 * Supprimer un service
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private void rmService() throws NumberFormatException, IOException {
		if (progLogged == null) {
			out.println("Vous devez etre connecté pour effectuer cette opération!##Saisissez 1 pour vous connecter");
			error = true;
		} else {
			error = false;

			out.println(ServiceManager.servicesList() + "## Numero du service à supprimer :");

			int numServ = Integer.valueOf(in.readLine());

			// Verifie que le service appartient au programmeur
			if (ServiceManager.getServicePackage(numServ).equals(progLogged.getLogin())) {

				// Retire le service
				ServiceManager.removeService(numServ);
			} else {
				out.println("Vous ne pouvez pas supprimer de service dont vous n etes pas l auteur!##"
						+ "Reessayer en saisissant 3");
				error = true;
			}
		}
	}

	/**
	 * Mettre à jour un service
	 * @throws IOException 
	 */
	private void editService() throws IOException {
		error = false;
		if (progLogged == null) {
			out.println("Vous devez etre connecté pour effectuer cette opération!##Saisissez 1 pour vous connecter");
			error = true;
		} else {
			out.println("Remplacement de service##" + ServiceManager.servicesList() + "##Numéro du service à remplacer :");
			
			int numServRempl = Integer.valueOf(in.readLine());
			
			// Connexion au FTP
			out.println("Chemin de la classe à remplacer :");
			String URLFileDir = "ftp://" + progLogged.getURLFtp() + in.readLine();
			URLClassLoader urlcl = new URLClassLoader(new URL[] { new URL(URLFileDir) });
			
			// Récuperation de la classe
			Class<?> classLoaded = null;
			try {
				classLoaded = urlcl.loadClass(ServiceManager.getServiceName(numServRempl));
			} catch (ClassNotFoundException e) {
				out.println("Nom de classe invalide!##Mettrer la classe sur le FTP et reessayer en saisissant 4");
				error = true;
			}
			
			
			// Remplacement du service dans le service manager
			try {
				ServiceManager.editService(classLoaded, numServRempl);
			} catch (ClasseInvalideException e) {
				out.println(e.getMessage() + "##Réessayer en saisissant 4");
				error = true;
			}
			
			urlcl.close();
		}

	}
}
