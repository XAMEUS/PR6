package org;

import java.net.UnknownHostException;
import java.util.Scanner;

import org.ringo.Commands;
import org.ringo.Entity;
import org.ringo.applications.Application;

public class Main {

	public static Entity entity;
	public static Application app;
	public static boolean quit = false;
	public static final Scanner sc = new Scanner(System.in);
	
	public static boolean DEBUG = false;
	
	public static void main(String[] args) throws NumberFormatException, UnknownHostException {
		if (args.length < 2) {
			System.out.println("Error, missing args\n\t0: id (string)\n\t1: udp_port (int)\n\t2: tcp_port (int)");
			return;
		}
		if (args.length >= 4)
			Main.DEBUG = Integer.parseInt(args[3]) == 1;
		System.out.println("Hello, type 'HELP' to show commands.");
		Main.entity = new Entity(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		String r = "";
		while (!quit) {
			r = Main.sc.nextLine().replaceAll("\n", "");
			Commands.exec(r);
		}
		System.exit(0);
	}
}
