package org.ringo.services;

import org.Main;
import org.ringo.Entity;

public class Multicast implements Runnable {

	private Entity entity;
	
	public Multicast(Entity e) {
	}
	
	@Override
	public void run() {
		
		if (Main.DEBUG) {
			System.out.println("[MULT]: Starting multicast service... but unimplemented yet. :p");
		}
		
		while (true) {
			// TODO MULTICAST PROTOCOL
		}
	}

}
