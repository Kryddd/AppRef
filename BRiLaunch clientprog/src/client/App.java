import java.io.IOException;

public class App {
	public final static int PORT_AMA = 2700;
	public final static String adresse = "localhost";
	
	
	
	public static void main(String[] args) throws IOException{
		new Thread(new Client(adresse, PORT_AMA)).start();
	}
}
