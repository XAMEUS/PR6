package org.ringo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.Main;

public class Commands {
	
	private static void appl(String cmd) {
		// TODO: implements
		System.out.println("APPL: unimplemented");
	}
	
	private static void dupl(String cmd) {
		// TODO: implements
		System.out.println("DUPL: unimplemented");
	}
	
	private static void gbye(String cmd) {
		// TODO: implements
		System.out.println("GBYE: unimplemented");
	}
	
	private static void ring(String cmd) {
		
		String[] s = cmd.split(" ");
		if (s.length != 3) {
			System.out.println("Bad syntax, use: RING <broadcast ip> <broadcast port>");
			return;
		}
		
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(s[1]);
		} catch (UnknownHostException e) {
			System.out.println("WELC ip port :: " + cmd + "\n\tbad ip");
			return;
		}
		int port = Integer.valueOf(s[2]);
		Main.entity.start(ip, port);
	}
	
	private static void test(String cmd) {
		// TODO: implements
		System.out.println("TEST: unimplemented");
	}
	
	private static void welc(String cmd) {
		
		String[] s = cmd.split(" ");
		if (s.length != 3) {
			System.out.println("Bad syntax, use: WELC <ip> <port>");
			return;
		}
		
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(s[1]);
		} catch (UnknownHostException e) {
			System.out.println("WELC ip port :: " + cmd + "\n\tbad ip");
			return;
		}
		int port = Integer.valueOf(s[2]);
		Main.entity.connect(ip, port);
	}
	
	public static void exec(String cmd) {
		if (cmd.length() < 4) {
			System.out.println("Bad command! ABCD");
			return;
		}
		if (cmd.equals("EXIT"))
			return;
		
		if (cmd.substring(0, 4).equals("APPL")) {
			System.out.println("APPL: TODO :)");
		}
		else if (cmd.substring(0, 4).equals("DUPL")) {
			System.out.println("DUPL: TODO");
		}
		else if (cmd.substring(0, 4).equals("GBYE")) {
			System.out.println("GBYE: TODO");
		}
		else if (cmd.substring(0, 4).equals("HELP")) {
			System.out.println("\tAPPL         :: unimplemented");
			System.out.println("\tDUPL         :: unimplemented");
			System.out.println("\tNEXT         :: shows the nexts list");
			System.out.println("\tHELP         :: print commands list");
			System.out.println("\tRING ip port :: start a new ring, using (ip, port) for broadcast");
			System.out.println("\tTEST         :: unimplemented");
			System.out.println("\tWELC ip port :: connect to an other node (ip, port)");
			System.out.println();
			System.out.println("\tEXIT         :: quit (dangerous, hard mode)");
		}
		else if (cmd.substring(0, 4).equals("NEXT")) {
			System.out.println("Nexts list:" + ((Main.entity.next.size() == 0)? " None, you should create a ring":""));
			for (Address addr: Main.entity.next)
				System.out.println("\t" + addr);
		}
		else if (cmd.substring(0, 4).equals("RING")) {
			Commands.ring(cmd);
		}
		else if (cmd.substring(0, 4).equals("TEST")) {
			System.out.println("TEST: TODO");
		}
		else if (cmd.substring(0, 4).equals("WELC")) {
			Commands.welc(cmd);
		}
		else {
			System.out.println("Unkown command! :/");
		}
	}
	
}
