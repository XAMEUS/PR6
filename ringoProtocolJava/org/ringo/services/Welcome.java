package org.ringo.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.Main;
import org.ringo.Address;
import org.ringo.Entity;

public class Welcome extends Thread {

	private Entity entity;

	private int n = 0;

	private ServerSocket server;

	public Welcome(Entity e) {
		entity = e;
	}

	@Override
	public void run() {
		
		if (Main.DEBUG) {
			System.out.println("[WELC]: Starting welcome service...");
		}
		
		try {
			
			this.server = new ServerSocket(this.entity.tcpPort);
			if (Main.DEBUG) System.out.println("[WELC]: TCP server running on port: " + this.entity.tcpPort);
			
			while (true) {
			
				if (Main.DEBUG) System.out.println("[WELC]: waiting for someone to connect...");
				Socket socket = this.server.accept();
				if (Main.DEBUG) System.out.println("[WELC]: new connection!");
	
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				int i = this.n % (this.entity.nextAddresses.size());
				String welc = "WELC " + this.entity.nextAddresses.get(i) + " " + this.entity.multicastAddresses.get(i) + "\n";
				if (Main.DEBUG) System.out.print("[WELC]: sending: " + welc);
				pw.print(welc);
				pw.flush();
				
				
				String recu = br.readLine();
				String[] s = recu.split(" ");
				if (Main.DEBUG) System.out.println("[WELC]: waiting for NEWC message...");
				if (s[0].equals("NEWC")) {
					
					if (Main.DEBUG) System.out.println("[WELC]: reading NEWC message: " + recu);
					InetAddress ip = InetAddress.getByName(recu.substring(5, 20));
					int port = Integer.parseInt(recu.substring(21));
					this.entity.nextAddresses.get(i).setAddress(ip, port);
					
					pw.print("ACKC\n");
					pw.flush();
					
				} else if(s[0].equals("DUPL")){
					
					if (Main.DEBUG) System.out.println("[WELC]: reading DUPL message: " + recu);
					entity.nextAddresses.add(new Address(InetAddress.getByName(s[1]),
							Integer.parseInt(s[2])));
					entity.multicastAddresses.add(new Address(InetAddress.getByName(s[3]),
							Integer.parseInt(s[4])));
					this.n++;
					
					pw.print("ACKC "+entity.addr.port+"\n");
					pw.flush();
					
					Thread t = new Thread(new Multicast(this.entity));
					entity.addMulticast(t);
					t.start();

					
				} else {
					// TODO ERROR
					System.out.println("ERROR : Welcome, something went wrong... bad NEWC: " + recu);
				}
								
				br.close();
				pw.close();
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void interrupt(){
		super.interrupt();  
		try {
			this.server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
