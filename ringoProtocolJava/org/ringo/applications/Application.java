package org.ringo.applications;

import org.Main;

public abstract class Application {

	protected String appName;

	
	public void start() {
		String s = "";
		prompt();
	}

	public void prompt(){
		boolean exit = false;
		while(!exit){
			String s = "";
			System.out.print("<" + appName + ">");
			s = Main.sc.nextLine().replaceAll("\n", "");
			if (s.equals("EXIT")){
				Main.app=null;
				exit = true;
			}else
				action(s);
		}
	}
	
	public abstract void action(String s);

	public static void analyzeAPPL(String msg) {
		if (msg.substring(14, 22).equals("DIFF####")) {
			ApplicationDIFF.diffRead(msg);
		}else if(msg.substring(14, 22).equals("TRANS###")){
			ApplicationTRANS.transAnalyze(msg);
		}
	}
	
	public String toString(){
		return appName;
	}
	
	public String getAppName(){
		return appName;
	}

}
