package org.ringo.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import org.Main;
import org.ringo.Address;
import org.ringo.Entity;

public class Multicast extends Thread {

	private Entity entity;
	private MulticastSocket mso;
	private Address addr;
	
	public Multicast(Entity e) {
		this.entity = e;
		this.addr = e.multicastAddresses.get(this.entity.multicastAddresses.size() - 1);
	}

	public static void send(String msg, Address addr) {
		
		try {	
			DatagramSocket dso = new DatagramSocket();
			
			byte[] data;
			data = msg.getBytes();
			
			if (Main.DEBUG) System.out.println("[MULT]: send: " + msg);
			
			DatagramPacket dp = new DatagramPacket(data, data.length, addr.ip, addr.port);
			dso.send(dp);
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		if (Main.DEBUG) {
			System.out.println("[MULT]: Starting multicast service... Work in progress...");
		}
		
		try {
			this.mso = new MulticastSocket(this.addr.port);
			this.mso.joinGroup(this.addr.ip);
			byte[] data = new byte[512];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			while (true) {
				this.mso.receive(dp);
				String msg = new String(dp.getData(), 0, dp.getLength());
				if (Main.DEBUG) System.out.println("[MULT]: receive: " + msg);
				if(msg.startsWith("DOWN")){
					this.entity.close(this.entity.multicastAddresses.indexOf(this.addr));
				}
				
			}
		} catch (SocketException e) {
			System.out.println("[MULT]: close " + this.addr);
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println(e);
		}
	}
	
	public void interrupt(){
		super.interrupt();  
		this.mso.close();
	}

}
