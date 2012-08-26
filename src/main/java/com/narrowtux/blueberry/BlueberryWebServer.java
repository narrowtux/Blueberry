package com.narrowtux.blueberry;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.narrowtux.blueberry.handler.HttpRequestHandler;
import com.narrowtux.blueberry.http.headers.factories.CookieFactory;
import com.narrowtux.blueberry.http.headers.factories.DateTimeFactory;
import com.narrowtux.blueberry.http.headers.factories.HeaderObjectFactory;
import com.narrowtux.blueberry.http.headers.factories.LongFactory;

/**
 * <p>
 * Main class for a web server instance (you can have multiple if you want)
 * </p>
 * <h1>Getting started</h1>
 * <ol>
 * <li>First, you need to create the web server instance:
 * 
 * <pre>
 * BlueberryWebServer server = new BlueberryWebServer();
 * </pre>
 * 
 * </li>
 * <li>
 * After that, you have to bind the server to at least one
 * {@link com.narrowtux.blueberry.Address}.
 * 
 * <pre>
 * server.bind(new Address(null, 80));
 * </pre>
 * 
 * In this example, we bind to port 80 on any IP address.</li>
 * <li>
 * Before you can start the server, you have to add at least one handler that
 * handles HTTP Requests.<br/>
 * Said handler must extend
 * {@link com.narrowtux.blueberry.handler.HttpRequestHandler}.
 * 
 * <pre>
 * server.getHandlers().add(myHandler);
 * </pre>
 * 
 * Note that the handlers list is a {@link java.util.LinkedList}, the first
 * handler in that list will be asked if a HTTP request matches first, then the
 * seconod handler, etc...</li>
 * <li>
 * Now the instance is ready to start:
 * 
 * <pre>
 * server.start();
 * </pre>
 * 
 * </li>
 * <li>
 * When you shut down your server, you should stop it:
 * 
 * <pre>
 * server.stop();
 * </pre>
 * 
 * </li>
 * </ol>
 * 
 * @see com.narrowtux.blueberry.handler.HttpRequestHandler
 * @see com.narrowtux.blueberry.http.HttpExchange
 * 
 * @author tux
 * 
 */
public class BlueberryWebServer {
	private LinkedHashMap<Address, BlueberryServerThread> sockets = new LinkedHashMap<Address, BlueberryServerThread>();
	private boolean running = false;
	private LinkedList<HttpRequestHandler> handlers = new LinkedList<HttpRequestHandler>();
	private String applicationName = "Untitled application";
	private boolean debug = false;
	
	/**
	 * Constructs a new webserver
	 */
	public BlueberryWebServer() {
		
	}
	
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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
	
	public String getServerString() {
		return applicationName + " using Blueberry {$version} / " + System.getProperty("os.name") + " " + System.getProperty("os.version");
	}
	
	public String getApplicationName() {
		return applicationName;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	TIntObjectHashMap<HeaderObjectFactory<?>> requestHeaderFactories = new TIntObjectHashMap<HeaderObjectFactory<?>>();
	
	{
		registerRequestHeader(new CookieFactory());
		registerRequestHeader(new DateTimeFactory("Accept-Datetime"));
		registerRequestHeader(new DateTimeFactory("Date"));
		registerRequestHeader(new DateTimeFactory("If-Modified-Since"));
		registerRequestHeader(new DateTimeFactory("If-Unmodified-Since"));
		registerRequestHeader(new LongFactory("Content-Length"));
	}
	
	public void registerRequestHeader(HeaderObjectFactory<?> factory) {
		requestHeaderFactories.put(factory.getRequestHeaderKey().hashCode(), factory);
	}
	
	public HeaderObjectFactory<?> getRequestHeaderFilter(String key) {
		return requestHeaderFactories.get(key.hashCode());
	}
}
