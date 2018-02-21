package users;

import java.util.ArrayList;

public class ListProgs {
	private ArrayList<Programmeur> progs;
	
	public ListProgs() {
		progs = new ArrayList<>();
	}
	
	public ListProgs(ArrayList<Programmeur> progs) {
		this.progs = progs;
	}
	
	
	public ArrayList<Programmeur> getProgs() {
		return progs;
	}
}
