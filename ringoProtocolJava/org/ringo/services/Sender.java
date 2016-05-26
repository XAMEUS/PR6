package org.ringo.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.Main;
import org.ringo.Address;
import org.ringo.Entity;

public class Sender {
	
	private Entity entity;
	private DatagramSocket dso;
	
	public Sender(Entity e) {
		this.entity = e;
		this.dso = null;
	}
	
	public void send(String msg) {
		
		try {	
			if (this.dso == null)
				dso = new DatagramSocket();

			String uid = msg.split(" ")[1];
			//String uid = msg.substring(5,13);
			if(!Main.entity.messagesIds.contains(uid)){
				Main.entity.messagesIds.add(uid);
				byte[] data;
				data = msg.getBytes();
				
				if (Main.DEBUG) System.out.println("[SEND]: " + msg);
				
				for (Address addr : this.entity.nextAddresses) {
					DatagramPacket dp = new DatagramPacket(data, data.length, addr.ip, addr.port);
					dso.send(dp);
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

	public void send(String msg, Address addr) {
		
		try {	
			if (this.dso == null)
				dso = new DatagramSocket();

			String uid = msg.split(" ")[1];
			//String uid = msg.substring(5,13);
			if(!Main.entity.messagesIds.contains(uid)){
				Main.entity.messagesIds.add(uid);
				byte[] data;
				data = msg.getBytes();
				
				if (Main.DEBUG) System.out.println("[SEND]: " + msg);
				
				DatagramPacket dp = new DatagramPacket(data, data.length, addr.ip, addr.port);
				dso.send(dp);
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
