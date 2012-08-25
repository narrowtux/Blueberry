package com.narrowtux.blueberry.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;

import com.narrowtux.blueberry.BlueberryWebServer;
import com.narrowtux.blueberry.handler.HttpRequestHandler;
import com.narrowtux.blueberry.http.headers.HttpHeaders;
import com.narrowtux.blueberry.http.headers.HttpStatusCode;
import com.narrowtux.blueberry.util.BlueberryOutputStream;

/**
 * <p>The HttpExchange class knows everything about a request and will use the
 * handler's input to reply to the request</p>
 * <p>By default, caching is enabled. Caching has the advantage that we know the
 * exact amount of bytes sent to the client when we send the headers, so this
 * can be sent to the client beforehand as the Content-Length-header value.<br/>
 * You can disable caching with {@link HttpExchange.disableCaching} if you want to send huge amounts of data</p>
 * <p>For replying to the client, you can either use {@link HttpExchange.echo}, {@link HttpExchange.getOutputStream} or {@link HttpExchange.getWriter}.</p>
 * @author narrowtux
 */
public class HttpExchange {
	private final InputStream in;
	private final OutputStream out;
	private BlueberryOutputStream cache = null;
	private OutputStream currentOut;
	private HttpHeaders requestHeaders, responseHeaders;
	private InetAddress from;
	private URI uri;
	private HttpVersion version;
	private HttpRequestMethod method;
	BufferedWriter writer;
	BufferedReader reader;
	HttpRequestHandler handler = null;
	boolean caching = true;
	HttpStatusCode status = null;
	private HashMap<String, Object> arguments = new HashMap<String, Object>();
	private BlueberryWebServer webServer = null;
	
	public HttpExchange(InputStream in, OutputStream out, InetAddress from, URI uri, HttpVersion version, HttpRequestMethod method, BufferedReader reader2, BlueberryWebServer server) {
		this.in = in;
		this.out = out;
		this.from = from;
		this.uri = uri;
		this.version = version;
		this.method = method;
		this.reader = reader2;
		this.webServer = server;
		init();
	}
	
	/**
	 * Sets the handler to the given handler
	 * @param handler the handler to use. If != null, will read the headers
	 */
	public void setHandler(HttpRequestHandler handler) {
		this.handler = handler;
		
		if (handler != null) {
			try {
				requestHeaders.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the handler
	 */
	public HttpRequestHandler getHandler() {
		return handler;
	}
	
	private void init() {
		currentOut = cache = new BlueberryOutputStream();
		requestHeaders = new HttpHeaders(this);
		responseHeaders = new HttpHeaders(this);
		
		responseHeaders.setHeader("Content-Type", "text/html");
		
		writer = new BufferedWriter(new OutputStreamWriter(getOutputStream()));
//		reader = new BufferedReader(new InputStreamReader(getInputStream()));
	}
	
	public void readArguments() {
		switch (getRequestMethod()) {
		case POST:
			// TODO check for the Content-Type and parse accordingly
			try {
				int length = (int) (long) (Long) getRequestHeaders().getHeader(
						"Content-Length").get(0);
				char data[] = new char[length];
				reader.read(data);
				String content = new String(data);
				String args[] = content.split("&");
				for (String arg : args) {
					String sp[] = arg.split("=", 2);
					if (sp.length == 2) {
						// TODO check for argument arrays
						arguments.put(sp[0], sp[1]);
					}
				}
			} catch (IOException e) {
			}
			break;
		default:
			break;
		}
	}

	public Object getArgument(String key) {
		return arguments.get(key);
	}
	
	/**
	 * @return the HTTP headers the client has sent
	 */
	public HttpHeaders getRequestHeaders() {
		return requestHeaders;
	}
	
	/**
	 * @return the HTTP headers we will send/have sent to the client
	 */
	public HttpHeaders getResponseHeaders() {
		return responseHeaders;
	}
	
	/**
	 * @return the input stream from the client
	 */
	public InputStream getInputStream() {
		return in;
	}
	
	/**
	 * @return the output stream to the client
	 */
	public OutputStream getOutputStream() {
		return currentOut;
	}
	
	/**
	 * @return the address of the requesting client
	 */
	public InetAddress getRequestingAddress() {
		return from;
	}
	
	/**
	 * @return the URI the client has requested
	 */
	public URI getRequestedUri() {
		return uri;
	}
	
	/**
	 * @return the HTTP Request method the client uses (GET/PUSH/...)
	 */
	public HttpRequestMethod getRequestMethod() {
		return method;
	}
	
	/**
	 * @return the HTTP Version the client uses (1.0 / 1.1)
	 */
	public HttpVersion getHttpVersion() {
		return version;
	}
	
	/**
	 * Sends the headers with HTTP status code
	 * When caching is enabled, this will just remember the status code until the exchange is closed and the cache is flushed.
	 * @param status the http status code
	 * @param contentSize the content size of the document. If you don't know, pass 0. This is not required when caching is enabled, because it will be calculated when you close the exchange
	 * @throws IOException
	 */
	public void sendResponseHeaders(HttpStatusCode status, long contentSize) throws IOException {
		if (isCaching()) {
			this.status = status;
			return;
		}
		if (getResponseHeaders().isSealed()) {
			return;
		}
		echo("HTTP/" + getHttpVersion().getVersionString() + " " + status.getCode() + " " + status.getTitle() + "\n");
		if (contentSize != 0) {
			getResponseHeaders().setHeader("Content-Length", contentSize);
		}
		getResponseHeaders().write();
	}
	
	/**
	 * @return a buffered writer that writes to the output stream
	 * @see com.narrowtux.blueberry.http.HttpExchange.echo
	 */
	public BufferedWriter getWriter() {
		return writer;
	}
	
	/**
	 * @return a buffered reader that reads form the input stream
	 */
	public BufferedReader getReader() {
		return reader;
	}
	
	/**
	 * @return if this exchange is caching the output
	 */
	public boolean isCaching() {
		return caching;
	}
	
	/**
	 * Shorthand for sending something to the client
	 * @param content
	 */
	public void echo(String content) {
		try {
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
		}
	}
	
	/**
	 * Closes the exchange.
	 * Will read any remaining data from the client to give it the illusion that we have understood everything it sent to us.
	 * When caching is enabled, this will send the status code and the headers, as well as the content you've written into the output stream (or the writer)
	 * @throws IOException
	 * @throws IllegalStateException when no status code has been given.
	 */
	public void close() throws IOException, IllegalStateException {
		writer.flush();
		if (caching) {
			if (status == null) {
				throw new IllegalStateException("No status code given when trying to send cache");
			}
			caching = false;
			currentOut = out;
			writer = new BufferedWriter(new OutputStreamWriter(out));
			sendResponseHeaders(status, cache.getSize());
			out.write(cache.getRawBuffer().array(), 0, (int) cache.getSize());
			out.flush();
		}
		int available;
		while ((available = in.available()) > 0) {
			in.read(new byte[Math.min(available, 512)]);
		}
		in.close();
		out.close();
	}

	/**
	 * Disables caching for this exchange. 
	 * Useful if you want to send huge amounts of data that should not fill up the RAM.
	 * Please note that you can't re-enable caching after you've done this
	 */
	public void disableCaching() {
		if (!caching) {
			return;
		}
		caching = false;
		currentOut = out;
		writer = new BufferedWriter(new OutputStreamWriter(out));
		cache = null;
	}
	
	/**
	 * @return the webserver instance this request was received on
	 */
	public BlueberryWebServer getWebServer() {
		return webServer;
	}
}
