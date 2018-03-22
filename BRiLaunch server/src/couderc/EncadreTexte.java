package couderc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import services.Service;

/**
 * Service d'encadrement de texte suivant la norme BRi
 * @author Jacques
 *
 */
public class EncadreTexte implements Service {

	private final Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	public EncadreTexte(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out.println("Service d'encadrement de chaine démarré##Chaine à encadrer :");
		String chaineEnc = "";
		try {
			chaineEnc = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("*** " + chaineEnc + " ***" + "####Appuyez sur Entrée pour continuer...");
	}

}
