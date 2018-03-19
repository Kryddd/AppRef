package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class App {
	public final static int PORT_AMA = 2700;
	public final static String adresse = "localhost";

	private static Socket socket;

	public static void main(String[] args) throws IOException {

		// new Thread(new Client(adresse, PORT_AMA)).start();

		socket = new Socket(adresse, PORT_AMA);

		BufferedReader sIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter sOut = new PrintWriter(socket.getOutputStream(), true);

		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println(sIn.readLine());
			String entree = clavier.readLine();
			sOut.println(entree);
			if(entree.equals("0")) {
				break;
			}
		}
		
		System.out.println("Fin de la communication");
	}

	protected void finalize() throws IOException {
		socket.close();
	}
}
