package com.narrowtux.blueberry.websockets;

public class CloseFrame extends Frame {
	int closeReason;
	
	public CloseFrame() {
		opcode = OP_CLOSE;
	}

	@Override
	public void readPayload(byte[] payload) {
		// TODO Auto-generated method stub

	}
	
	public int getCloseReason() {
		return closeReason;
	}

	@Override
	public byte[] getPayload() {
		// TODO Auto-generated method stub
		return null;
	}
}
