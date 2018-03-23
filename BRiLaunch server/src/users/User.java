package users;

/**
 * Classe abstraite des utilisateurs
 * @author couderc1
 * @version 1.0
 */
public abstract class User {
	
	private String login;
	private String password;
	private String URLFtp;
	
	public User(String login, String password, String URLFtp) {
		this.login = login;
		this.password = password;
		this.URLFtp = URLFtp;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
	
	public String getURLFtp() {
		return this.URLFtp;
	}
	
	public void setURLFtp(String newFTP) {
		this.URLFtp = newFTP;
	}
}
