package couderc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import services.Service;

/**
 * Classe d'un service suivant la norme BRi
 * @author couderc1
 * @version 1.0
 */
public class Bonjour implements Service {

	private final Socket client;
	private PrintWriter out;
	
	public Bonjour(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		System.out.println("Service Bonjour lancé");
		try {
			new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out.println("Service Bonjour lancé######Bonjour!");
	}

}
