package org.ringo.applications;

import org.Main;

public abstract class Application {

	protected String appName;
	public void start() {
		boolean exit = false;
		String s = "";
		while (!exit) {
			// System.out.print("<" + appName + ">"); WARNING: destroying output/input with threads...
			s = Main.sc.nextLine().replaceAll("\n", "");
			if (s.equals("EXIT"))
				exit = true;
			else
				action(s);
		}
	}

	public abstract void action(String s);

	public static void analyzeAPPL(String msg) {
		if (msg.substring(14, 22).equals("DIFF####")) {
			ApplicationDIFF.diffRead(msg);
		}
	}

}
