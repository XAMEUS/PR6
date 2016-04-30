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
import org.ringo.Entity;

public class Welcome implements Runnable {

	private Entity entity;

	private int n = 0;

	private ServerSocket server;

	public Welcome(Entity e) {
		entity = e;
	}

	@Override
	public void run() {
		
		if (Main.DEBUG) {
			System.out.println("Starting welcome service...");
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
				
				int i = this.n % (this.entity.next.size());
				String welc = "WELC " + this.entity.next.get(i) + " " + this.entity.multicast.get(i) + "\n";
				if (Main.DEBUG) System.out.print("[WELC]: sending: " + welc);
				pw.print(welc);
				pw.flush();
				
				String newc = br.readLine();
				if (Main.DEBUG) System.out.println("[WELC]: waiting for NEWC message...");
				if (newc.substring(0, 4).equals("NEWC")) {
					if (Main.DEBUG) System.out.println("[WELC]: reading NEWC message: " + newc);
					InetAddress ip = InetAddress.getByName(newc.substring(5, 20)); // WARNING: why not a split()[1] ?
					int port = Integer.parseInt(newc.substring(21));
					this.entity.next.get(i).setAddress(ip, port);
					this.n++;
				} else {
					// TODO ERROR
					System.out.println("ERROR : Welcome, something went wrong... bad NEWC: " + newc);
				}
				
				pw.print("ACKC\n");
				pw.flush();
				
				br.close();
				pw.close();
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
