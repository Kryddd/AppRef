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
			// appel�e affichera l'erreur au lieu du menu
			if (!error) {
				menu();
			}
			else {
				error = false;
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
			case "5": // Cr�er un compte
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
			case "7":
				try {
					changeStateService();
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				out.println("Commande invalide");
				error = true;
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

	

	/**
	 * Change l'adresse du serveur FTP du d�veloppeur
	 * @throws IOException
	 */
	private void changeFTP() throws IOException {
		if (progLogged == null) {
			out.println("Vous devez etre connect� pour effectuer cette op�ration!##Saisissez 1 pour vous connecter");
			error = true;
		} else {

			out.println("Saissisez votre nouvelle adresse de serveur FTP :");

			progLogged.setURLFtp(in.readLine());
		}

	}

	/**
	 * Connexion du programmeur
	 * @throws IOException
	 */
	private void login() throws IOException {

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
			out.println("L'identifiant ou le mot de passe est incorrect!##" + "R�essayer en saisissant 1");
		}
	}

	/**
	 * Affiche le menu
	 */
	private void menu() {
		out.println(
				"> Tappez le chiffre correspondant � l'op�ration demand�e : ##" + "0. Quitter ##" + "1. S'identifier ##"
						+ "2. Ajouter un service ##" + "3. Supprimer un service ##" + "4. Modifier un service##"
						+ "5. Cr�er un compte##" + "6. Changer de serveur FTP##" + "7. Activer/d�sactiver un service");
	}

	/**
	 * Cr��e un compte
	 * 
	 * @throws IOException
	 */
	private void register() throws IOException {

		out.println("Cr�ation de compte####Login :");
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
					out.println("Le login existe d�j�!##" + "Reessayer en saisissant 5");
				}
			}

			if (!error) {
				// Ajoute le prog � la liste
				lProgs.add(new Programmeur(login, pwd, ftp));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addService() throws IOException {
		if (progLogged == null) {
			out.println("Vous devez etre connect� pour effectuer cette op�ration!##Saisissez 1 pour vous connecter");
			error = true;
		} else {

			// Connexion au serveur FTP
			out.println("Chemin de la classe � charger (Appuyer sur entr�e si elle se trouve � la racine):");
			String URLFileDir = "ftp://" + progLogged.getURLFtp() + in.readLine();
			URLClassLoader urlcl = new URLClassLoader(new URL[] { new URL(URLFileDir) });

			// Chargement de la classe
			out.println("Nom de la classe � charger (sans le nom de package):");
			String className = progLogged.getLogin() + "." + in.readLine();
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
	 * Supprimer un service du programmeur
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private void rmService() throws NumberFormatException, IOException {
		if (progLogged == null) {
			out.println("Vous devez etre connect� pour effectuer cette op�ration!##Saisissez 1 pour vous connecter");
			error = true;
		} else {

			out.println(ServiceManager.servicesList() + "## Numero du service � supprimer :");

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
	 * Met � jour un service du programmeur
	 * 
	 * @throws IOException
	 */
	private void editService() throws IOException {
		if (progLogged == null) {
			out.println("Vous devez etre connect� pour effectuer cette op�ration!##Saisissez 1 pour vous connecter");
			error = true;
		} else {
			out.println("Remplacement de service##" + ServiceManager.servicesList() + "##Num�ro du service � remplacer :");

			int numServRempl = Integer.valueOf(in.readLine());
			
			// V�rifie si le service appartient au programmeur avant de le remplacer
			if (ServiceManager.getServicePackage(numServRempl).equals(progLogged.getLogin())) {
	
				// Connexion au FTP
				out.println("Chemin de la classe � remplacer (Appuyer sur entr�e si elle se trouve � la racine) :");
				String URLFileDir = "ftp://" + progLogged.getURLFtp() + in.readLine();
				URLClassLoader urlcl = new URLClassLoader(new URL[] { new URL(URLFileDir) });
	
				// R�cuperation de la classe
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
					out.println(e.getMessage() + "##R�essayer en saisissant 4");
					error = true;
				}
	
				urlcl.close();
			}
			else {
				out.println("Vous ne pouvez pas remplacer de service dont vous n etes pas l auteur!##"
						+ "Reessayer en saisissant 4");
				error = true;
			}
		}

	}
	
	/**
	 * Change l'�tat(activ� ou d�sactiv�) des services du programmeur
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	private void changeStateService() throws NumberFormatException, IOException {
		if (progLogged == null) {
			out.println("Vous devez etre connect� pour effectuer cette op�ration!##Saisissez 1 pour vous connecter");
			error = true;
		} else {
			out.println(ServiceManager.servicesList() + "##Saisissez le num�ro du service dont vous voulez changer l'�tat :");
			
			int numService = Integer.valueOf(in.readLine());
			
			// V�rifie si le programmeur est propri�taire du service
			if(progLogged.getLogin().equals(ServiceManager.getServicePackage(numService))) {
				// Change l'�tat du service
				ServiceManager.changeState(numService);
			}
			else {
				error = true;
				out.println("Vous n'etes pas propri�taire du service!##R�essayer en saisissant 7");
			}
		}
	}
}
