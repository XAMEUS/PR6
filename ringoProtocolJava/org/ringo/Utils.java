package org.ringo;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Utils {

	public static String uniqueId1() {
		String uniqueID = UUID.randomUUID().toString();
		//System.out.println(uniqueID);
		String id = "j8r";
		byte[] b = new byte[6];
		//System.out.println(id.getBytes().length);
		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		b[0] = (byte) cal.get(Calendar.MONTH);
		b[1] = (byte) cal.get(Calendar.DAY_OF_MONTH);
		b[2] = (byte) cal.get(Calendar.HOUR_OF_DAY);
		b[3] = (byte) cal.get(Calendar.MINUTE);
		b[4] = (byte) (cal.get(Calendar.MILLISECOND) % 64);
		b[5] = (byte) (Math.random() * 64);
		// System.out.print("bytes : ");
		// for (int i = 0; i < b.length; i++)
		// System.out.print(String.format("%02d", b[i]) + " ");
		// System.out.println();
		// System.out.println(new String(Base64.getEncoder().encode(b)));
		return new String(Base64.getEncoder().encode(b));
	}
	
	public static String toLittleEndian(int n){
		char[] tab = new char[8];
		for(int i=0;i<8;i++){
			tab[i]=(char)(n%256);
			n=n/256;
		}
		String s = new String(tab);
		return s;
		
	}
	
	public static int fromLittleEndian(String s){
		int n = 0;
		for(int i=0;i<8;i++){
			n+=(int)(s.charAt(i))*(Math.pow(256,i));
		}
		return n;
	}
	
	public static void main(String[] args){
		int n = Integer.parseInt("00000451");
		System.out.println(n);
		String s = toLittleEndian(n)+" "+"OK";
		
		System.out.println(s.split(" ")[1]);
		System.out.println(s.length());
		System.out.println(""+fromLittleEndian(s));
	}

}
