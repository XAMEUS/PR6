package org.ringo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import org.Main;
import org.ringo.services.Multicast;
import org.ringo.services.Receiver;
import org.ringo.services.Sender;
import org.ringo.services.Welcome;

public class Entity {

	public final String id;
	public final Address addr;
	public final int tcpPort;

	public static final int MAX_RINGS = 2;

	public final ArrayList<Address> nextAddresses;
	public final ArrayList<Address> multicastAddresses;

	private Thread welcome;
	private Thread receive;
	private ArrayList<Thread> multicast;
	
	public final Sender sender;
	
	public HashSet<String> messagesIds;

	/**
	 * Main constructor.
	 * @param id_p the user id
	 * @param udpPort the udp port (listen)
	 * @param tcpPort the tcp port (listen)
	 * @throws UnknownHostException
	 */
	public Entity(String id_p, int udpPort, int tcpPort) {
		this.id = id_p;
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("Unknow host! :x");
			System.exit(0);
		}
		this.addr = new Address(ip, udpPort);
		this.tcpPort = tcpPort;
		this.nextAddresses = new ArrayList<Address>();
		this.multicastAddresses = new ArrayList<Address>();
		this.sender = new Sender(this);
		this.messagesIds = new HashSet<String>();
		
	}

	public void connect(InetAddress ip, int port) {
		
		if (Main.DEBUG) System.out.println("Trying to connect to (" + ip + ", " + port + ")");
		Socket socket;
		
		try {
			socket = new Socket(ip, port);
			if (Main.DEBUG) System.out.println("Connected to (" + ip + ", " + port + "); now trying WELC protocol...");
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String welc = br.readLine();
			if (Main.DEBUG) System.out.println("Trying to read WELC message...");
			if (Main.DEBUG) System.out.println("Read : " + welc);
			if (welc.substring(0, 4).equals("WELC")) {
				nextAddresses.add(new Address(InetAddress.getByName(welc.substring(5, 20)),
						Integer.parseInt(welc.substring(21, 25))));
				multicastAddresses.add(new Address(InetAddress.getByName(welc.substring(26, 41)),
						Integer.parseInt(welc.substring(42))));
			} else {
				// TODO ERROR
				System.out.println("ERROR : connect, bad WELC:" + welc);
			}
			
			if (Main.DEBUG) System.out.println("Sending NEWC message...");
			String newc = "NEWC " + addr + "\n";
			pw.print(newc);
			pw.flush();
			
			String ackc = br.readLine();
			if (Main.DEBUG) System.out.println("Read : " + ackc);
			if (!ackc.substring(0, 4).equals("ACKC")) {
				// TODO ERROR
				System.out.println("ERROR : connect, bad ACKC: " + ackc);
			}
			
			pw.close();
			br.close();
			socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Connection refused! :o");
			return;
		}
		
		System.out.println("Successfully connected to (" + ip + ", " + port + "). ^^");

		this.receive = new Thread(new Receiver(this));
		this.welcome = new Thread(new Welcome(this));
		Thread t = new Thread(new Multicast(this));
		this.multicast.add(t);
		
		
		this.receive.start();
		this.welcome.start();
		t.start();

	}
	
	public void connectDUPL(InetAddress ip, int port, InetAddress ip_mult, int port_mult) {
		
		if (Main.DEBUG) System.out.println("Trying to connect to (" + ip + ", " + port + ")");
		Socket socket;
		
		try {
			socket = new Socket(ip, port);
			if (Main.DEBUG) System.out.println("Connected to (" + ip + ", " + port + "); now trying WELC protocol...");
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String welc = br.readLine();
			if (Main.DEBUG) System.out.println("Read : " + welc);
			if (Main.DEBUG) System.out.println("Trying to read WELC message...");
			if (!welc.substring(0, 4).equals("WELC")) {
				// TODO ERROR
				System.out.println("ERROR : connect, bad WELC:" + welc);
			}
			
			if (Main.DEBUG) System.out.println("Sending DUPL message...");
			String dupl = "DUPL " + addr + " " + multicastAddresses.get(0) +"\n";
			pw.print(dupl);
			pw.flush();
			
			String ackd = br.readLine();
			if (Main.DEBUG) System.out.println("Read : " + ackd);
			if (ackd.substring(0, 4).equals("ACKC")) {
				nextAddresses.add(new Address(InetAddress.getByName(welc.substring(5, 20)),
						Integer.parseInt(ackd.substring(5))));
				multicastAddresses.add(new Address(ip_mult,port_mult));
			}else{
				// TODO ERROR
				System.out.println("ERROR : connect, bad ACKD: " + ackd);
			}
			
			pw.close();
			br.close();
			socket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("Connection refused! :o");
			return;
		}
		
		System.out.println("Successfully connected to (" + ip + ", " + port + "). ^^");

		this.receive = new Thread(new Receiver(this));
		this.welcome = new Thread(new Welcome(this));
		Thread t = new Thread(new Multicast(this));
		this.multicast.add(t);
		
		
		this.receive.start();
		this.welcome.start();
		t.start();

	}

	/**
	 * Starts a new Ring
	 * @param ip multicast-ip
	 * @param port multicast-port (should be less than 9999)
	 */
	public void start(InetAddress ip, int port) {

		if (Main.DEBUG) System.out.println("STARTING a new Ring with multicast (" + ip + ", " + port + ")");
		this.nextAddresses.add(new Address(addr.ip, addr.port));
		this.multicastAddresses.add(new Address(ip, port));

		this.receive = new Thread(new Receiver(this));
		this.welcome = new Thread(new Welcome(this));
		Thread t = new Thread(new Multicast(this));
		this.multicast.add(t);
		
		
		this.receive.start();
		this.welcome.start();
		t.start();
	}
	
	public void addMulticast(Thread t){
		multicast.add(t);
	}

}
