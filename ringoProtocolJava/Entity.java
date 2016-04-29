import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

class Entity {

	String id;
	Address me;
	int port_tcp;
	
	final static int MAX_RINGS = 2;

	ArrayList<Address> next;
	ArrayList<Address> multicast;
	
	Thread welcome;
	Thread recv;
	SenderService sender;

	public Entity(String id_p, int port_listen_p, int port_tcp_p) throws UnknownHostException {
		id = id_p;
		me = new Address(InetAddress.getLocalHost(),port_listen_p);
		port_tcp = port_tcp_p;
		next = new ArrayList<Address>();
		multicast = new ArrayList<Address>();
		sender = new SenderService(this);
	}
	


	public void connect(InetAddress ip, int port) {
		Socket socket;
		try {
			socket = new Socket(ip, port);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String welc = br.readLine();
			if (welc.substring(0, 4).equals("WELC")) {
				next.add(new Address(InetAddress.getByName(welc.substring(5, 20)), Integer.parseInt(welc.substring(21, 25))));
				multicast.add(new Address(InetAddress.getByName(welc.substring(26, 41)), Integer.parseInt(welc.substring(42))));
			} else {
				// TODO ERREUR
			}
			String newc = "NEWC " + me + "\n";
			pw.print(newc);
			pw.flush();
			String ackc = br.readLine();
			if (!ackc.substring(0, 4).equals("ACKC")) {
				// TODO ERREUR
			}
			pw.flush();
			pw.close();
			br.close();
			socket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		recv = new Thread(new ReceiverService(this));
		welcome = new Thread(new WelcomeService(this));
		recv.start();
		welcome.start();
		
	}

	
	
	public void start() throws UnknownHostException {
		
		next.add(new Address(me.ip,me.port));
		multicast.add(new Address(InetAddress.getByName("229.254.254.254"),9998));
		
		recv = new Thread(new ReceiverService(this));
		welcome = new Thread(new WelcomeService(this));
		recv.start();
		welcome.start();
	}

	public void read(){
		while(true){
			Scanner sc = new Scanner(System.in);
			String msg = sc.nextLine();
			sender.send(msg);
		}
	}
	
	public static void main(String[] args) {
		Entity en;
		Entity en2;
		try {
		
//			en = new Entity("test", 4242, 4243);
//			System.out.println(en.me + " " + en.port_tcp);
//			en.start();
			
			en2 = new Entity("test2", 4244, 4245);
			en2.connect(InetAddress.getByName("127.000.001.001"), 4243);
			en2.read();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
