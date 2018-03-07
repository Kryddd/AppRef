import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
	private String adresse;
	private int port;

	public Client(String adresse, int portProg) {
		this.adresse = adresse;
		this.port = portProg;
	}

	@Override
	public void run() {
		int menu = 1;
		Scanner sc = new Scanner(System.in);
		Socket socket = null;

		try {
			socket = connectServer();

			while (menu != 0) {
				System.out.println("Tappez le chiffre correspondant à l'opération demandée : \n" + "0. Quitter \n"
						+ "1. Ajouter un Service \n" + "2. Mettre à jour un service \n"
						+ "3. Changer l'adresse du serveur FTP \n");
				menu = Integer.parseInt(sc.nextLine());

				switch (menu) {
				case 0:
					break;
				case 1:
					addService();
					break;
				case 2:
					updateService();
					break;
				case 3:
					socket = changeAddress(sc);
					break;
				default:
					System.out.println("Entrée invalide");
					break;
				}
			}

			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addService() {
		///////////////////////////////////////////////////////////////////
		System.out.println("Pas implementé");
		///////////////////////////////////////////////////////////////////
	}

	private void updateService() {
		///////////////////////////////////////////////////////////////////
		System.out.println("Pas implementé");
		///////////////////////////////////////////////////////////////////
	}

	private Socket changeAddress(Scanner sc) throws IOException {
		String bufferAdresse = adresse;
		int bufferPort = port;
		
		System.out.println("Nouvelle adresse du serveur, \"default\" pour les parametres par défaut :");
		adresse = sc.nextLine();

		if (adresse.toLowerCase().equals("default")) {
			adresse = App.adresse;
			port = App.PORT_PROG;
		}

		else {
			System.out.println("Nouveau port du serveur :");
			port = Integer.parseInt(sc.nextLine());
		}

		try {
			return connectServer();
		} catch (IOException e) {
			System.out.println("Erreur de connexion, retour au parametres précédants");
			adresse = bufferAdresse;
			port = bufferPort;
			return connectServer();
		}
	}

	private Socket connectServer() throws IOException {
		Socket socket = new Socket(adresse, port);
		System.out.println("Connexion au serveur " + adresse + " sur le port " + port);
		return socket;
	}
}
