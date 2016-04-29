import java.net.*;
import java.io.*;

public class Address {
	
	InetAddress ip;
	int port;
	
	public Address(String ip_p, int port_p) throws UnknownHostException{
		ip = InetAddress.getByName(ip_p);
		port=port_p;
	}
	
	public Address(InetAddress ip_p, int port_p){
		ip=ip_p;
		port=port_p;
	}
	
	public String toString(){
		return Address.ipToStr(ip)+" "+String.format("%04d",port);
		
	}
	
	public void setAddress(InetAddress ip_p, int port_p) {
		ip = ip_p;
		port = port_p;
	}
	
	public void setAddress(String ip_p, int port_p) throws UnknownHostException{
		ip = InetAddress.getByName(ip_p);
		port=port_p;
	}
	
	public static String ipToStr(InetAddress ip_p) {
		byte[] ip_byte = ip_p.getAddress();
		int[] ip = new int[4];
		for (int i = 0; i < 4; i++) {
			ip[i] = ip_byte[i] & 0xFF;
		}
		return String.format("%03d", ip[0]) + "." + String.format("%03d", ip[1]) + "." + String.format("%03d", ip[2])
				+ "." + String.format("%03d", ip[3]);
	}
	
}
