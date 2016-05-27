package org.ringo.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.Main;
import org.ringo.Entity;
import org.ringo.applications.Application;
import org.ringo.protocols.Protocols;

public class Receiver extends Thread {

	private Entity entity;
	private DatagramSocket dso;

	public Receiver(Entity e) {
		this.entity = e;
	}

	@Override
	public void run() {

		if (Main.DEBUG) {
			System.out.println("[RECV]: Starting receiver service...");
		}

		try {
			this.dso = new DatagramSocket(this.entity.addr.port);
			if (Main.DEBUG)
				System.out.println("[RECV]: receiver server started on port " + this.entity.addr.port);
			byte[] data = new byte[512];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			while (true) {
				this.dso.receive(dp);
				String msg = new String(dp.getData(), 0, dp.getLength());
				msg=msg.trim();
				if (Main.DEBUG)
					System.out.println("[RECV]: receive: " + msg);
				if (Protocols.analyzePROT(msg))
					continue;
				else if (Application.analyzeAPPL(msg))
					continue;
				else 
					entity.sender.send(msg);
			}
		} catch (SocketException e) {
			System.out.println("[RECV]: close");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void interrupt() {
		super.interrupt();
		this.dso.close();
	}

}
