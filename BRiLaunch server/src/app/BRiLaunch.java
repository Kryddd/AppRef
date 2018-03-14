package app;

import java.util.ArrayList;
import serveurs.ServeurBRi;
import servicesBRi.ServiceBRiAmateur;
import servicesBRi.ServiceBRiProg;
import users.Amateur;
import users.Programmeur;

public class BRiLaunch {

	private final static int PORT_PROG = 2600;
	private final static int PORT_AMA = 2700;
	
	public static void main(String[] args) {
		
		// Comptes programmeur
		ArrayList<Programmeur> progs = new ArrayList<>(); 
		progs.add(new Programmeur("brette", "passe", "localhost"));
		progs.add(new Programmeur("couderc", "1234", "localhost"));
		progs.add(new Programmeur("Bob", "passe", "localhost"));
		
		// Comptes amateur
		ArrayList<Amateur> amateurs = new ArrayList<>();
		amateurs.add(new Amateur("Jean", "depasse", "localhost"));
		amateurs.add(new Amateur("Philippe", "abcd", "localhost"));
		
		// Passage des listes de comptes aux services associés
		ServiceBRiAmateur.initProgs(amateurs);
		ServiceBRiProg.initProgs(progs);
		
		
		// Lancements des serveurs programmeur et amateur
		new Thread(new ServeurBRi(PORT_PROG, ServiceBRiProg.class)).start();
		new Thread(new ServeurBRi(PORT_AMA, ServiceBRiAmateur.class)).start();
	}
}
