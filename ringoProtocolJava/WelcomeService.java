import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class WelcomeService implements Runnable{
	
	private Entity en;
	
	int n = 0;

	public WelcomeService(Entity en_p){
		en=en_p;
	}
	
	@Override
	public void run() {
		try{
    		ServerSocket server=new ServerSocket(en.port_tcp);
    		while(true){
	    		Socket socket=server.accept();
	    		BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    		PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	    		String welc = "WELC "+en.next.get(n%(en.next.size()))+" "+en.multicast.get(n%(en.next.size()))+"\n";
	    		pw.print(welc);
	    		pw.flush();
	    		String newc = br.readLine();
	    		if(newc.substring(0, 4).equals("NEWC")){
	    			en.next.get(n%(en.next.size())).setAddress(InetAddress.getByName(newc.substring(5,20)),Integer.parseInt(newc.substring(21)));
	    			n++;
	    		}else{
	    			//TODO ERREUR
	    		}
	    		pw.print("ACKC\n");
	    		pw.flush();
	    		br.close();
	    		pw.close();
	    		socket.close();
    		}    		
    	}catch(Exception e){
    		System.out.println(e);
    		e.printStackTrace();
    	}
		
	}

}
