package org.ringo.applications;

import org.Main;
import org.ringo.Utils;

public class ApplicationDIFF extends Application {

	public ApplicationDIFF() {
		this.appName = "DIFF";
	}

	@Override
	public void action(String s) {
		ApplicationDIFF.diffWrite(s);
	}

	public static void diffWrite(String msg) {
		if (msg.length() > 485) {
			msg = msg.substring(0, 485);
		}
		String uid = Utils.uniqueId1();
		Main.entity.messagesIds.add(uid);
		String send = "APPL " + uid + " DIFF#### " + String.format("%03d", msg.length()) + " " + msg;
		Main.entity.sender.send(send);
	}

	public static void diffRead(String msg) {
		String recv = msg.substring(27);
		System.out.println(recv);
		Main.entity.sender.send(msg);
	}

}
