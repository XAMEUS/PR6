import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SenderService {

	private Entity en;

	public SenderService(Entity en_p) {
		en = en_p;
	}

	public void send(String msg) {
		try {
			DatagramSocket dso = new DatagramSocket();
			byte[] data;
			data = msg.getBytes();
			for (Address a : en.next) {
				DatagramPacket paquet = new DatagramPacket(data, data.length, a.ip, a.port);
				dso.send(paquet);
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
