package com.narrowtux.blueberry;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

public class BlueberryWebServer extends Thread {
	private List<InetAddress> boundAdresses = new LinkedList<InetAddress>();
	private boolean running = false;
	
	public BlueberryWebServer() {
		
	}
	
	public void bind(InetAddress to) {
		boundAdresses.add(to);
		if (isRunning()) {
			
		}
	}
	
	public boolean isRunning() {
		return running;
	}
}
