package org.ringo.applications;

import java.util.Scanner;

public abstract class Application {
	
	protected String appName;
	protected Scanner sc;
		
	public void start(){
		boolean exit = false;
		String s = "";
		sc = new Scanner(System.in);
		while (!exit) {
			System.out.print("<"+appName+">");
			s = sc.nextLine().replaceAll("\n", "");
			if (s.equals("EXIT"))
				exit = true;
			else
				action(s);
		}
	}
	
	public abstract void action(String s);

	public static void analyzeAPPL(String msg){
		if(msg.substring(14,22).equals("DIFF####")){
			ApplicationDIFF.diffRead(msg);
		}
	}
	
}
