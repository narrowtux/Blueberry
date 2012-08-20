package com.narrowtux.blueberry.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpExchange {
	InputStream in;
	OutputStream out;
	
	public void close() throws IOException {
		in.close();
		out.close();
	}
}
