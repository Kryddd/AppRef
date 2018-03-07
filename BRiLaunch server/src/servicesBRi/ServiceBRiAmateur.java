package servicesBRi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import users.Amateur;


/**
 * Classe du service utilisateur BRi Amateur
 * @author couderc1
 * @version 1.0
 */
public class ServiceBRiAmateur extends ServiceBRi{

	/**
	 * Liste des amateurs
	 */
	private static ArrayList<Amateur> amateurs;
	
	/**
	 * Initialise la liste des amateurs
	 * @param amateurs
	 */
	public static void initProgs(ArrayList<Amateur> amateurs) {
		ServiceBRiAmateur.amateurs = amateurs;
	}

	/**
	 * Constructeur du service
	 * @param sock
	 */
	public ServiceBRiAmateur(Socket sock) {
		super(sock);
	}
	
	@Override
	public void run() {
		System.out.println("Connexion amateur " + getNumService() + " demarree");

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
			PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Connexion amateur " + getNumService() + " terminee");
		try {
			super.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
