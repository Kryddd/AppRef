package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class App {
	public final static int PORT_PROG = 2600;
	public final static String adresse = "localhost";

	private static Socket socket;

	public static void main(String[] args) throws IOException {

		// new Thread(new Client(adresse, PORT_AMA)).start();

		socket = new Socket(adresse, PORT_PROG);

		BufferedReader sIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter sOut = new PrintWriter(socket.getOutputStream(), true);

		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println(sIn.readLine());
			sOut.println(clavier.readLine());
		}
	}

	protected void finalize() throws IOException {
		socket.close();
	}
}
