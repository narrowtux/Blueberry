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

import com.narrowtux.blueberry.handler.ErrorHandler;
import com.narrowtux.blueberry.handler.HttpRequestHandler;
import com.narrowtux.blueberry.http.HttpExchange;
import com.narrowtux.blueberry.http.HttpRequestMethod;
import com.narrowtux.blueberry.http.HttpVersion;
import com.narrowtux.blueberry.http.headers.HttpStatusCode;

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
		HttpExchange exchange = null;
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
				
				exchange = new HttpExchange(in, out, from, uri, version, method, reader, server);
				boolean handled = false;
				for (HttpRequestHandler current : handlers) {
					if (current.doesMatch(version, method, uri)) {
						try {
							exchange.setHandler(current);
							exchange.readArguments();
							current.handle(exchange);
						} catch (Exception e) {
							if (e instanceof HttpException) {
								HttpException he = (HttpException) e;
								ErrorHandler.withError(he.getCode()).withException(he.getT()).handle(exchange);
							} else {
								ErrorHandler.withError(HttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR).withException(e).handle(exchange);
							}
						}
						handled = true;
						break;
					}
				}
				if (!handled) {
					throw new HttpException(HttpStatusCode.HTTP_404_NOT_FOUND, "No handler found for request: "+exchange.getRequestedUri());
				}
			} catch (Exception e) {
				if (exchange != null) {
					try {
						if (e instanceof HttpException) {
							HttpException he = (HttpException) e;
							ErrorHandler.withError(he.getCode()).withException(he.getT()).handle(exchange);
						} else {
							ErrorHandler.withError(HttpStatusCode.HTTP_500_INTERNAL_SERVER_ERROR).withException(e).handle(exchange);
						}
					} catch (HttpException e1) {
						e1.printStackTrace();
					}
				} else {
					e.printStackTrace();
				}
			} finally {
				in.close();
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
