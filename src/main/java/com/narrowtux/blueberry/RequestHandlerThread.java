package com.narrowtux.blueberry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.LinkedList;

import com.narrowtux.blueberry.handler.HttpRequestHandler;
import com.narrowtux.blueberry.http.HttpExchange;
import com.narrowtux.blueberry.http.HttpRequestMethod;
import com.narrowtux.blueberry.http.HttpVersion;

public class RequestHandlerThread extends Thread {
	Socket handle;
	BlueberryWebServer server;

	public RequestHandlerThread(Socket handle, BlueberryWebServer server) {
		super();
		this.server = server;
		this.handle = handle;
	}

	@Override
	public void run() {
		try {
			InputStream in = handle.getInputStream();
			OutputStream out = handle.getOutputStream();
	
			try {
				InetAddress from = handle.getInetAddress();
	
				HttpVersion version;
				HttpRequestMethod method;
				URI uri;
	
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String firstLine = reader.readLine();
				
				if (firstLine == null) {
					throw new IllegalStateException("No data");
				}
				String[] sp1 = firstLine.split(" ");
				method = HttpRequestMethod.valueOf(sp1[0]);
				String[] sp2 = sp1[2].split("/");
				if (!sp2[0].equals("HTTP")) {
					throw new IllegalStateException("Invalid protocol '"+sp1[2]+"'");
				}
				version = HttpVersion.byString(sp2[1]);
	
				uri = new URI(sp1[1]);
	
				LinkedList<HttpRequestHandler> handlers = server.getHandlers();
				
				HttpExchange exchange = new HttpExchange(in, out, from, uri, version, method, reader);
				boolean handled = false;
				for (HttpRequestHandler current : handlers) {
					if (current.doesMatch(version, method, uri)) {
						try {
							exchange.setHandler(current);
							current.handle(exchange);
						} catch (Exception e) {
							e.printStackTrace();
							// TODO send code 500 with exception trace
						}
						handled = true;
						break;
					}
				}
				if (!handled) {
					throw new IllegalStateException("Could not handle request");
					// TODO send code 500 with error message
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO send code 500 with error message
			} finally {
				in.close();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
