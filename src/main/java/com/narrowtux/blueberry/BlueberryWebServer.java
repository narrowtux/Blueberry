package com.narrowtux.blueberry;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.narrowtux.blueberry.handler.HttpRequestHandler;

public class BlueberryWebServer {
	private LinkedHashMap<Address, BlueberryServerThread> sockets = new LinkedHashMap<Address, BlueberryServerThread>();
	private boolean running = false;
	private LinkedList<HttpRequestHandler> handlers = new LinkedList<HttpRequestHandler>();
	
	/**
	 * Constructs a new webserver
	 */
	public BlueberryWebServer() {
		
	}
	
	/**
	 * Binds to the given address
	 * If the server is running, it will create a new thread for this
	 * @param to
	 */
	public void bind(Address to) {
		BlueberryServerThread thread = new BlueberryServerThread(this, to);
		sockets.put(to, thread);
		if (isRunning()) {
			thread.start();
		}
	}
	
	/**
	 * Unbinds from the given address
	 * If the server is running, it will interrupt the corresponding thread for this address
	 * @param from
	 */
	public void unbind(Address from) {
		BlueberryServerThread socket = sockets.get(from);
		if (socket != null) {
			if (isRunning()) {
				socket.interrupt();
			}
			sockets.remove(from);
		}
	}
	
	/**
	 * Starts the server
	 */
	public void start() {
		if (!isRunning()) {
			running = true;
			for (BlueberryServerThread socket:sockets.values()) {
				socket.start();
			}
		}
	}
	
	/**
	 * Stops the server
	 */
	public void stop() {
		if (isRunning()) {
			running = false;
			for (BlueberryServerThread socket:sockets.values()) {
				socket.interrupt();
			}
		}
	}
	
	/**
	 * @return if the server is running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * @return the list of handlers, you can use that to add handlers too
	 */
	public LinkedList<HttpRequestHandler> getHandlers() {
		return handlers;
	}
	
	/**
	 * Sets a new list of handlers, use this to rearrange the handlers
	 * @param handlers the new list of handlers
	 */
	public void setHandlers(LinkedList<HttpRequestHandler> handlers) {
		this.handlers = handlers;
	}
}
