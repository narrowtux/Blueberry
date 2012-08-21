package com.narrowtux.blueberry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BlueberryServerThread extends Thread {
	BlueberryWebServer server = null;
	Address bind;
	ServerSocket socket = null;
	
	public BlueberryServerThread(BlueberryWebServer server, Address bindTo) {
		this.server = server;
		this.bind = bindTo;
	}
	
	@Override
	public void run() {
		try {
			socket = new ServerSocket(bind.getPort(), 0, bind.getAddress());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while (!isInterrupted()) {
			try {
				Socket handle = socket.accept();
				if (handle == null) {
					continue;
				}
				RequestHandlerThread handler = new RequestHandlerThread(handle, server);
				handler.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
