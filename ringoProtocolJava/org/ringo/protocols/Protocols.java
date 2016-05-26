package org.ringo.protocols;

import org.Main;
import org.ringo.Utils;

public class Protocols {

	private static boolean showMEMB = false;
	
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
	
	public static void analyzePROT(String msg) {
		if (msg.startsWith("WHOS")) {
			Protocols.WHOS(msg);
		}
		else if (msg.startsWith("MEMB")) {
			Protocols.MEMB(msg);
		}
		else {
			System.out.println("Unkown protocol! :/");
		}
	}
	
}
