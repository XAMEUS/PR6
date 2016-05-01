package org.ringo.services;

import org.Main;
import org.ringo.Entity;

public class Receive implements Runnable {

	private Entity entity;

	public Receive(Entity e) {
		this.entity = e;
	}

	@Override
	public void run() {
		
		if (Main.DEBUG) {
			System.out.println("Starting receive service... but unimplemented yet. :p");
		}
		
		while (true) {
			// TODO: UDP PROTOCOL
		}
	}

}
