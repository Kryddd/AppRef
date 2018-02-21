package services;

import java.util.ArrayList;

import users.ListProgs;

public class ServiceBRiProg implements Runnable{
	
	private static ListProgs lProgs;
	
	public static void initProgs(ListProgs progs) {
		ServiceBRiProg.lProgs = progs;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
