package org.ringo;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.Main;
import org.ringo.applications.ApplicationDIFF;
import org.ringo.applications.ApplicationTRANS;
import org.ringo.protocols.Protocols;

public class Commands {

	private static void appl(String cmd) {
		if (Main.app!=null){
			System.out.println("Can't launch more than one application at the same time, current application = "+Main.app);
		}
		if (cmd.substring(5).equals("DIFF")) {
			Main.app = new ApplicationDIFF();
			Main.app.start();
		}else if(cmd.substring(5).equals("TRANS")){
			Main.app = new ApplicationTRANS();
			Main.app.start();
		} else {
			System.out.println("Unkown application");
		}
	}

	private static void dupl(String cmd) {
		// TODO: implements
		System.out.println("DUPL: unimplemented");
	}

	private static void gbye(String cmd) {
		Protocols.gbye(Integer.parseInt(cmd.split(" ")[1]));
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

	private static void join(String cmd) {

		String[] s = cmd.split(" ");
		if (s.length != 3) {
			System.out.println("Bad syntax, use: JOIN <ip> <port>");
			return;
		}

		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(s[1]);
		} catch (UnknownHostException e) {
			System.out.println("JOIN ip port :: " + cmd + "\n\tbad ip");
			return;
		}
		int port = Integer.valueOf(s[2]);
		Main.entity.connect(ip, port);
	}

	public static void exec(String cmd) {
		if (cmd.length() < 4) {
			System.out.println("Bad command!");
			return;
		}
		if (cmd.equals("EXIT")) {
			Main.quit = true;
			return;
		}

		if (cmd.substring(0, 4).equals("APPL")) {
			Commands.appl(cmd);
		} else if (cmd.substring(0, 4).equals("DUPL")) {
			String[] s = cmd.split(" ");
			if (s.length != 5) {
				System.out.println("Bad syntax, use: DUPL <ip> <port> <ip-diff> <port-diff>");
				return;
			}

			InetAddress ip = null;
			try {
				ip = InetAddress.getByName(s[1]);
			} catch (UnknownHostException e) {
				System.out.println("DUPL <ip> <port> <ip-diff> <port-diff> :: " + cmd + "\n\tbad ip");
				return;
			}
			int port = Integer.valueOf(s[2]);
			
			InetAddress ip_diff = null;
			try {
				ip_diff = InetAddress.getByName(s[3]);
			} catch (UnknownHostException e) {
				System.out.println("DUPL <ip> <port> <ip-diff> <port-diff> :: " + cmd + "\n\tbad ip_diff");
				return;
			}
			int port_diff = Integer.valueOf(s[4]);
			Main.entity.connectDUPL(ip, port, ip_diff, port_diff);
		} else if (cmd.substring(0, 4).equals("GBYE")) {
			String[] s = cmd.split(" ");
			if (s.length < 2) {
				System.out.println("Bad syntax, use: GBYE <i>");
				return;
			}
			Commands.gbye(cmd);
		} else if (cmd.substring(0, 4).equals("HELP")) {
			System.out.println("\tAPPL app     :: start the application (app)");
			System.out.println("\t     DIFF    :: diff app");
			System.out.println("\t     TRANS   :: file transfert app");
			System.out.println("\tDUPL         :: duplicate a ring");
			System.out.println("\tNEXT         :: shows the nexts list");
			System.out.println("\tHELP         :: print commands list");
			System.out.println("\tRING ip port :: start a new ring, using (ip, port) for broadcast");
			System.out.println("\tSEND msg     :: sending a raw message (debug)");
			System.out.println("\tTEST         :: unimplemented");
			System.out.println("\tJOIN ip port :: join another ring through the node at (ip, port)");
			System.out.println("\tGBYE i       :: close connexion to ring i");
			System.out.println("\tWHOS         :: list all ring users");
			System.out.println();
			System.out.println("\tEXIT         :: quit (dangerous, hard mode)");
		} else if (cmd.substring(0, 4).equals("NEXT")) {
			System.out.println("Nexts list:" + ((Main.entity.nextAddresses.size() == 0) ? " None, you should create a ring" : ""));
			for (Address addr : Main.entity.nextAddresses)
				System.out.println("\t" + addr);
		} else if (cmd.substring(0, 4).equals("RING")) {
			Commands.ring(cmd);
		} else if (cmd.substring(0, 4).equals("SEND")) {
			Main.entity.sender.send("SEND " + Utils.uniqueId1() + " " + Main.entity.id + ":" + cmd.substring(5));
		} else if (cmd.substring(0, 4).equals("TEST")) {
			System.out.println("TEST: TODO");
		} else if (cmd.substring(0, 4).equals("JOIN")) {
			Commands.join(cmd);
		} else if (cmd.substring(0, 4).equals("WHOS")) {
			Protocols.whos();
		} else {
			System.out.println("Unkown command! :/");
		}
	}

}
