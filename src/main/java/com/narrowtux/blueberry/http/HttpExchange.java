package com.narrowtux.blueberry.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.HashMap;

import com.narrowtux.blueberry.handler.HttpRequestHandler;
import com.narrowtux.blueberry.http.headers.HttpHeaders;
import com.narrowtux.blueberry.http.headers.HttpStatusCode;

public class HttpExchange {
	private InputStream in;
	private OutputStream out;
	private HttpHeaders requestHeaders, responseHeaders;
	private InetAddress from;
	private URI uri;
	private HttpVersion version;
	private HttpRequestMethod method;
	private BufferedWriter writer;
	private BufferedReader reader;
	private HttpRequestHandler handler = null;
	private HashMap<String, Object> arguments = new HashMap<String, Object>();
	
	public HttpExchange(InputStream in, OutputStream out, InetAddress from, URI uri, HttpVersion version, HttpRequestMethod method, BufferedReader reader2) {
		this.in = in;
		this.out = out;
		this.from = from;
		this.uri = uri;
		this.version = version;
		this.method = method;
		this.reader = reader2;
		init();
	}
	
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
	
	public HttpRequestHandler getHandler() {
		return handler;
	}
	
	private void init() {
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
				String content = "";
				CharBuffer buffer = CharBuffer.allocate(512);
				while(reader.read(buffer) != -1) {}
				content = buffer.toString();
				String args[] = content.split(";");
				for (String arg:args) {
					String sp[] = arg.split("=", 2);
					if (sp.length == 2) {
						// TODO check for argument arrays
						arguments.put(sp[0], sp[1]);
					}
				}
			} catch (IOException e) {}
			break;
		default:
			break;
		}
	}
	
	public Object getArgument(String key) {
		return arguments.get(key);
	}
	
	public HttpHeaders getRequestHeaders() {
		return requestHeaders;
	}
	
	public HttpHeaders getResponseHeaders() {
		return responseHeaders;
	}
	
	public InputStream getInputStream() {
		return in;
	}
	
	public OutputStream getOutputStream() {
		return out;
	}
	
	public InetAddress getRequestingAddress() {
		return from;
	}
	
	public URI getRequestedUri() {
		return uri;
	}
	
	public HttpRequestMethod getRequestMethod() {
		return method;
	}
	
	public HttpVersion getHttpVersion() {
		return version;
	}
	
	public void sendResponseHeaders(HttpStatusCode status, long contentSize) throws IOException {
		echo("HTTP/" + getHttpVersion().getVersionString() + " " + status.getCode() + " " + status.getTitle() + "\n");
		if (contentSize != 0) {
			getResponseHeaders().setHeader("Content-Length", contentSize);
		}
		getResponseHeaders().write();
	}
	
	public BufferedWriter getWriter() {
		return writer;
	}
	
	public BufferedReader getReader() {
		return reader;
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
	
	public void close() throws IOException {
		writer.flush();
		int available;
		while ((available = in.available()) > 0) {
			in.read(new byte[Math.min(available, 512)]);
		}
		in.close();
		out.close();
	}
}
