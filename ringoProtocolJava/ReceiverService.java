import java.io.IOException;
import java.net.*;

public class ReceiverService implements Runnable {

	private Entity en;
	
	public ReceiverService(Entity en_p){
		en = en_p;
	}
	
	@Override
	public void run() {
		try {
			DatagramSocket dso = new DatagramSocket(en.me.port);
			byte[] data = new byte[512];
			DatagramPacket paquet = new DatagramPacket(data, data.length);
			while(true){
				dso.receive(paquet);
				String recu = new String(paquet.getData(), 0, paquet.getLength());
				System.out.println(recu);
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
