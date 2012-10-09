package com.narrowtux.blueberry.websockets;

import java.nio.ByteBuffer;

public class BinaryFrame extends Frame {
	ByteBuffer buffer = ByteBuffer.allocate(256);

	public BinaryFrame() {
		opcode = OP_BINARY;
	}

	@Override
	public void readPayload(byte[] payload) {
		if (payload.length > buffer.remaining()) {
			ByteBuffer repl = ByteBuffer.allocate(buffer.capacity() * 2);
			repl.put(buffer);
			buffer = repl;
		}
		buffer.put(payload, 0, payload.length);
	}
	
	public void setData(byte [] data) {
		buffer = ByteBuffer.wrap(data);
	}
	
	public byte[] getData() {
		return buffer.array();
	}

	@Override
	public byte[] getPayload() {
		return buffer.array();
	}
}
