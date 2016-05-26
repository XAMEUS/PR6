package org.ringo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Address {

	public InetAddress ip;
	public int port;

	public Address(String ip, int port) throws UnknownHostException {
		this.ip = InetAddress.getByName(ip);
		this.port = port;
	}

	public Address(InetAddress ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String toString() {
		return Address.ipToStr(this.ip) + " " + String.format("%04d", this.port);

	}

	public void setAddress(InetAddress ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public void setAddress(String ip, int port) throws UnknownHostException {
		this.ip = InetAddress.getByName(ip);
		this.port = port;
	}
	
	public void setPort(int port){
		this.port = port;
	}

	public static String ipToStr(InetAddress ip) {
		byte[] ipByte = ip.getAddress();
		int[] ipInt = new int[4];
		for (int i = 0; i < 4; i++) {
			ipInt[i] = ipByte[i] & 0xFF;
		}
		return String.format("%03d", ipInt[0]) + "." + String.format("%03d", ipInt[1]) + "." + String.format("%03d", ipInt[2])
				+ "." + String.format("%03d", ipInt[3]);
	}

}
