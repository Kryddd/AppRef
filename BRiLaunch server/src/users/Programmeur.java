package users;

public class Programmeur extends User {
	
	private String URLFtp;
	
	public Programmeur(String login, String password, String URLFtp) {
		super(login, password);
		
		this.URLFtp = URLFtp;
	}

	public String getURLFtp() {
		return URLFtp;
	}
	
}
