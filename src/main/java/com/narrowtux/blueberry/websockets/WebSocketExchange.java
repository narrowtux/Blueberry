package com.narrowtux.blueberry.websockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import com.narrowtux.blueberry.http.HttpExchange;

public class WebSocketExchange {
	private InputStream in;
	private OutputStream out;
	private HttpExchange httpExchange;
	private boolean open = true;
	
	public WebSocketExchange(InputStream in, OutputStream out, HttpExchange exchange) {
		super();
		this.in = in;
		this.out = out;
		this.httpExchange = exchange;
	}
	
	public boolean isOpen() {
		return open;
	}

	public void close() throws IOException {
		if (open) {
			open = false;
			try {
				sendFrame(new CloseFrame());
			} catch (SocketException e) {}
		}
		getInputStream().close();
		getOutputStream().close();
	}

	public InputStream getInputStream() {
		return in;
	}

	public OutputStream getOutputStream() {
		return out;
	}
	
	public HttpExchange getHttpExchange() {
		return httpExchange;
	}
	
	public void sendFrame(Frame frame) throws IOException {
		if (!open) {
			throw new SocketException("Socket is closed");
		}
		boolean fin = true;
		byte opcode = (byte) frame.getOpCode();
		boolean maskEnabled = false;
		byte data[] = frame.getPayload();
		long len = data != null ? data.length : 0;
		
		byte firstlen = (byte) len;
		if (len > 125) {
			firstlen = 126;
		}
		if (len > 65535) {
			firstlen = 127;
		}
		
		out.write((fin ? 0x80 : 0x00)| opcode);
		out.write((maskEnabled ? 0x80 : 0x00) << 7 | firstlen);
		
		if (firstlen == 126) {
			out.write((int) (len >> 8));
			out.write((int) len);
		} else if (firstlen == 127) {
			out.write((int) (len >> 56));
			out.write((int) (len >> 48));
			out.write((int) (len >> 40));
			out.write((int) (len >> 32));
			out.write((int) (len >> 24));
			out.write((int) (len >> 16));
			out.write((int) (len >> 8));
			out.write((int) len);
		}
		
		if (data != null) {
			out.write(data);
		}
	}
}
