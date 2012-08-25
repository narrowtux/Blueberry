package com.narrowtux.blueberry.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BlueberryOutputStream extends OutputStream {
	boolean closing = false;
	private ByteBuffer buffer = ByteBuffer.allocate(512);
	
	public BlueberryOutputStream() {
	}
	
	@Override
	public void write(byte[] b) {
		while (buffer.remaining() < b.length) {
			expand();
		}
		buffer.put(b);
	}

	@Override
	public void write(byte[] b, int len, int off) {
		while (buffer.remaining() < b.length) {
			expand();
		}
		buffer.put(b, len, off);
	}

	@Override
	public void write(int b) {
		if (buffer.remaining() < 1) {
			expand();
		}
		buffer.put((byte) b);
	}
	
	public long getSize() {
		return buffer.position();
	}
	
	public ByteBuffer getRawBuffer() {
		return buffer;
	}
	
	private void expand() {
		ByteBuffer replacement = ByteBuffer.allocate(buffer.capacity() * 2);
		replacement.put(buffer.array());
		replacement.position(buffer.position());
		buffer = replacement;
	}
}
