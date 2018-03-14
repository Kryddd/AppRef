package servicesBRi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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

	/**
	 * Constructeur du service
	 * 
	 * @param sock
	 */
	public ServiceBRiProg(Socket sock) {
		super(sock);
	}

	@Override
	public void run() {
		System.out.println("Connexion programmeur " + getNumService() + " demarree");

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
			PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Connexion programmeur " + getNumService() + " terminee");
		try {
			super.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
