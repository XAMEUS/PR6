package org.ringo.protocols;

import java.net.UnknownHostException;

import org.Main;
import org.ringo.Address;
import org.ringo.Utils;

public class Protocols {

	private static boolean showMEMB = false;
	private static boolean gbye = false;
	private static int eybg = -1;
	
	public static void whos() {
		String uid = Utils.uniqueId1();
		String send = "WHOS " + uid;
		Main.entity.sender.send(send);
	}

	private static void WHOS(String msg) {
		String uid = msg.substring(5,13);
		if(!Main.entity.messagesIds.contains(uid)) {
			Main.entity.sender.send(msg);
			uid = Utils.uniqueId1();
			String send = "MEMB " + uid + " " + Main.entity.id
							+ " " + Main.entity.addr.ip + " " + Main.entity.tcpPort;
			Main.entity.sender.send(send);
		} else {
			Protocols.showMEMB = true;
			System.out.println("[WHOS]: receiving MEMB...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						System.err.println(e);
					}
					Protocols.showMEMB = false;
				}
			}).start();
		}
	}
	

	private static void MEMB(String msg) {
		if (Protocols.showMEMB) {
			System.out.println("[WHOS]: " + msg.substring(13));
		} else {
			Main.entity.sender.send(msg);
		}
	}
	
	public static void gbye(int n) {
		Protocols.gbye = true;
		Protocols.eybg = n;
		String uid = Utils.uniqueId1();
		String send = "GBYE " + uid + " " + Main.entity.addr.toString() + " " + Main.entity.nextAddresses.get(n).toString();
		Main.entity.sender.send(send);
	}
	
	public static void analyzePROT(String msg) {
		if (msg.startsWith("WHOS")) {
			Protocols.WHOS(msg);
		}
		else if (msg.startsWith("MEMB")) {
			Protocols.MEMB(msg);
		}
		else if (msg.startsWith("GBYE")) {
			Protocols.BGYE(msg);
		}
		else if (msg.startsWith("EYBG")) {
			Protocols.EYBG(msg);
		}
		else {
			System.out.println("Unkown protocol! :/");
		}
	}

	private static void BGYE(String msg) {
		String[] s = msg.split(" ");
		for (int i = 0; i < Main.entity.nextAddresses.size(); i++) {
			Address addr = Main.entity.nextAddresses.get(0);
			String ip = s[2];
			int port = Integer.parseInt(s[3]);
			if (Address.ipToStr(addr.ip).equals(ip) && addr.port == port) {
				if (Main.DEBUG) System.out.println("[EYBG]: goodbye " + ip + ":" + port);
				try {
					Main.entity.nextAddresses.set(i, new Address(s[4], Integer.parseInt(s[5])));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				Main.entity.sender.send("EYBG " + Utils.uniqueId1(), addr);
			}
		}
		Main.entity.sender.send(msg);
	}

	private static void EYBG(String msg) {
		if (Protocols.gbye) {
			Main.entity.close(Protocols.eybg);
			Protocols.gbye = false;
			Protocols.eybg = -1;
		}
	}
}
