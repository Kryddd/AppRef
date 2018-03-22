package couderc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.net.ftp.FTPClient;

import services.Service;

/**
 * Service d'analyse de XML suivant la norme BRi
 * @author Jacques
 * @version 1.0
 */
public class AnalyseXML implements Service {

	private final Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private FTPClient FtpCli;
	private String URLFtp;
	
	public AnalyseXML(Socket client, String URLFtp) {
		this.client = client;
		this.FtpCli = new FTPClient();
		this.URLFtp = URLFtp;
	}
	
	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			// Connection au serveur FTP
			FtpCli.connect(URLFtp);
			
			out.println("Fichier XML à analyser :");
			String fileName = in.readLine();
			InputStream streamXML = FtpCli.retrieveFileStream(fileName);
			InputStreamReader readerXML = new InputStreamReader(streamXML);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
