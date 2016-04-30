package org.ringo.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

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
			
			byte[] data;
			data = msg.getBytes();
			
			System.out.println("[SEND]: " + msg);
			
			for (Address addr : this.entity.next) {
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
