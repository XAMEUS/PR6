package org.ringo.services;

import org.Main;
import org.ringo.Entity;

public class Broadcast implements Runnable {

	private Entity entity;
	
	public Broadcast(Entity e) {
	}
	
	@Override
	public void run() {
		
		if (Main.DEBUG) {
			System.out.println("Starting broadcast service... but unimplemented yet. :p");
		}
		
		while (true) {
			// TODO MULTICAST PROTOCOL
		}
	}

}
