import java.net.*;
import java.io.*;

class Entity{

    String id;
    InetAddress ip;
    int port_listen;
    String me_str;
    int port_tcp;
    
    InetAddress ip_next;
    int port_next;
    String next_str;
    
    InetAddress ip_multicast;
    int port_multicast;
    String multicast_str;
    
    public Entity(String id_p, int port_listen_p, int port_tcp_p) throws UnknownHostException{
        id = id_p;
        ip = InetAddress.getLocalHost();
        port_listen = port_listen_p;
        me_str = Entity.ipToStr(ip)+" "+String.format("%04d", port_listen);
        port_tcp = port_tcp_p;
        ip_next = InetAddress.getLocalHost();
        port_next = port_listen_p;
        next_str = Entity.ipToStr(ip_next)+" "+String.format("%04d", port_next);
        ip_multicast = InetAddress.getByName("229.254.254.254");
        port_multicast = 9998;
        multicast_str = Entity.ipToStr(ip_multicast)+" "+String.format("%04d", port_multicast);
    }
    
    public static String ipToStr(InetAddress ip_p){
    	byte[] ip_byte = ip_p.getAddress();
		int[] ip = new int[4];
		for(int i=0;i<4;i++){
			ip[i]=ip_byte[i] & 0xFF;
		}
		return String.format("%03d", ip[0])+"."+String.format("%03d", ip[1])+"."+String.format("%03d", ip[2])+"."+String.format("%03d", ip[3]);
    }
    
    public void setNext(InetAddress ip, int port){
    	ip_next = ip;
    	port_next = port;
    	next_str = Entity.ipToStr(ip_next)+" "+String.format("%04d", port_next);
    }
    
    public void setMulticast(InetAddress ip, int port){
    	ip_multicast = ip;
        port_multicast = port;
        multicast_str = Entity.ipToStr(ip_multicast)+" "+String.format("%04d", port_multicast);
    }

    public void connect(InetAddress ip,int port){
    	port_listen = port;
    	Socket socket;
		try {
			socket = new Socket(ip, port);
			BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
    		PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    		String welc = br.readLine();
    		if(welc.substring(0, 4).equals("WELC")){
    			setNext(InetAddress.getByName(welc.substring(6, 21)),Integer.parseInt(welc.substring(22,26)));
    			setMulticast(InetAddress.getByName(welc.substring(27, 42)),Integer.parseInt(welc.substring(43)));
    		}else{
    			//TODO ERREUR
    		}
    		String newc = "NEWC "+me_str+"\n";
    		pw.print(newc);
    		String ackc = br.readLine();
    		if(!ackc.substring(0,4).equals("ACKC")){
    			//TODO ERREUR
    		}
    		pw.flush();    
    		pw.close();
    		br.close();
    		socket.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
    }
    
    public void start(){
    	Thread loop = new Thread(new LoopService(this));
    	Thread welcome = new Thread(new WelcomeService(this));
    	//loop.start();
    	welcome.start();
    }

    
    public static void main(String[] args){
    	Entity en;
    	Entity en2;
    	try {
			//en = new Entity("test",4242,4243);
			en2 = new Entity("test2",4244,4245);
			en2.connect(InetAddress.getByName("127.000.001.001"),4343);
			//System.out.println(en.me_str+" "+en.port_tcp);
			//en.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
