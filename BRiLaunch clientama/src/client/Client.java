import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
	private String adresse;
	private int port;

	public Client(String adresse, int portAma) {
		this.adresse = adresse;
		this.port = portAma;
	}

	@Override
	public void run() {
		int menu = 1;
		Socket socket = null;

		try {
			socket = connectServer();
			BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter sout = new PrintWriter(socket.getOutputStream(), true);

			while (menu != 0) {
				System.out.println("Tappez le chiffre correspondant à l'opération demandée : \n" 
						+ "0. Quitter \n"
						+ "1. Choisir un Service \n" 
						+ "2. Mettre à jour un service \n"
						+ "3. Changer l'adresse du serveur FTP \n");
				menu = Integer.parseInt(clavier.readLine());

				switch (menu) {
				case 0:
					break;
				case 1:
					choisirService(clavier, sin, sout);
					break;
				case 2:
					socket = changeAddress(clavier);
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

	private void choisirService(BufferedReader clavier, BufferedReader sin, PrintWriter sout) throws IOException {
		System.out.println(ListeService(clavier, sin, sout));
		System.out.println("Numéro du service");
		sout.print(Integer.parseInt(clavier.readLine()));
		
		///////////////////////////////////////////////////////////////////
		System.out.println("Pas implementé");
		///////////////////////////////////////////////////////////////////
	}

	private String ListeService(BufferedReader clavier, BufferedReader sin, PrintWriter sout) throws IOException {
		sout.print("Liste");
		return sin.readLine();
	}

	private Socket changeAddress(BufferedReader claier) throws IOException {
		String bufferAdresse = adresse;
		int bufferPort = port;

		System.out.println("Nouvelle adresse du serveur, \"default\" pour les parametres par défaut :");
		adresse = claier.readLine();

		if (adresse.toLowerCase().equals("default")) {
			adresse = App.adresse;
			port = App.PORT_AMA;
		}

		else {
			System.out.println("Nouveau port du serveur :");
			port = Integer.parseInt(claier.readLine());
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
