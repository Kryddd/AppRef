package couderc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import services.Service;

public class InversionChaine implements Service {

	private final Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	public InversionChaine(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		System.out.println("Service Bonjour lancé");
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out.println("Service d'inversion de chaine de caractères lancé##Insèrer une chaine à inverser :");
		String chaine = "";
		try {
			chaine = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		StringBuilder result = new StringBuilder();
		
		for (int i = chaine.length() - 1; i >= 0; i--) {
            result.append(chaine.charAt(i));
        }
		
		out.println(result.toString() + "####Appuyez sur entrée pour continuer...");
	}

}
