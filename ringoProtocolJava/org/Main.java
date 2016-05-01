package org;

import java.net.UnknownHostException;
import java.util.Scanner;

import org.ringo.Commands;
import org.ringo.Entity;

public class Main {

	public static Entity entity;
	public static boolean quit = false;
	private static Scanner sc;
	
	public static boolean DEBUG = true;
	
	public static void main(String[] args) throws NumberFormatException, UnknownHostException {
		if (args.length < 2) {
			System.out.println("Error, missing args\n\t0: id (string)\n\t1: udp_port (int)\n\t2: tcp_port (int)");
			return;
		}
		if (args.length >= 4)
			DEBUG = Integer.parseInt(args[2]) == 1;
		System.out.println("Hello, type 'HELP' to show commands.");
		Main.entity = new Entity(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		String r = "";
		sc = new Scanner(System.in);
		while (!quit) {
			r = sc.nextLine().replaceAll("\n", "");
			Commands.exec(r);
			if (r.equals("EXIT"))
				quit = true;
		}
	}
}
