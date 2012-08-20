package com.narrowtux.blueberry.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.narrowtux.blueberry.http.headers.HttpHeaders;

public class HttpExchange {
	InputStream in;
	OutputStream out;
	HttpHeaders requestHeaders, responseHeaders;
	
	public HttpExchange(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
		init();
	}
	
	private void init() {
		requestHeaders = new HttpHeaders();
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
	
	public void close() throws IOException {
		in.close();
		out.close();
	}
}
