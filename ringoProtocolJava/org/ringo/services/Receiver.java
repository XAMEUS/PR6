package org.ringo.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.Main;
import org.ringo.Entity;

public class Receiver implements Runnable {

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
		
		while (true) {
			
			try {
				this.dso = new DatagramSocket(this.entity.addr.port);
				if (Main.DEBUG) System.out.println("[RECV]: receiver server started on port " + this.entity.addr.port);
				byte[] data = new byte[512];
				DatagramPacket dp = new DatagramPacket(data, data.length);
				while(true){
					this.dso.receive(dp);
					String msg = new String(dp.getData(), 0, dp.getLength());
					if (Main.DEBUG) System.out.println("[RECV]: receive: " + msg);
					String uid = msg.substring(5,13);
					if(!entity.messagesIds.contains(uid)){
						entity.messagesIds.add(uid);
						
						//TODO Analyze
						
						entity.sender.send(msg); /* Je pense que le send devrait se faire dans les protocoles/applications, il ne vont pas tous forcément renvoyer le message*/
					}
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
