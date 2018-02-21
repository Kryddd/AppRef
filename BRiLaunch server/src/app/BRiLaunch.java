package app;

import java.util.ArrayList;

import serveurs.ServeurBRi;
import services.ServiceBRiAmateur;
import services.ServiceBRiProg;
import users.Amateur;
import users.Programmeur;

public class BRiLaunch {

	private final static int PORT_PROG = 2600;
	private final static int PORT_AMA = 2700;
	
	public static void main(String[] args) {
		ArrayList<Programmeur> progs = new ArrayList<>(); 
		progs.add(new Programmeur("brette", "passe", "localhost"));
		progs.add(new Programmeur("couderc", "passe", "localhost"));
		progs.add(new Programmeur("Bob", "passe", "localhost"));
		
		try {
			Class.forName("ServiceBRiProg.class");
			Class.forName("ServiceBRiAmateur.class");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		new Thread(new ServeurBRi(PORT_PROG, ServiceBRiProg.class)).start();
		new Thread(new ServeurBRi(PORT_AMA, ServiceBRiAmateur.class)).start();
	}
}
