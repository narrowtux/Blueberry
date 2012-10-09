package com.narrowtux.blueberry.websockets;

public class PingFrame extends Frame {

	public PingFrame() {
		opcode = OP_PING;
	}

	@Override
	public void readPayload(byte[] payload) {
	}

	@Override
	public byte[] getPayload() {
		return new byte[0];
	}

}
