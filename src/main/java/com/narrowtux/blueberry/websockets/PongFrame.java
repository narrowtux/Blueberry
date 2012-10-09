package com.narrowtux.blueberry.websockets;

public class PongFrame extends Frame {

	public PongFrame() {
		opcode = OP_PONG;
	}

	@Override
	public void readPayload(byte[] payload) {
	}

	@Override
	public byte[] getPayload() {
		return new byte[0];
	}
}
