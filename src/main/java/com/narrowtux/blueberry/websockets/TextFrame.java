package com.narrowtux.blueberry.websockets;

import java.io.UnsupportedEncodingException;

public class TextFrame extends Frame {
	StringBuilder builder = new StringBuilder();

	public TextFrame() {
		opcode = OP_TEXT;
	}

	public TextFrame(String text) {
		this();
		builder.append(text);
	}

	@Override
	public void readPayload(byte[] payload) {
		try {
			builder.append(new String(payload, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String getText() {
		return builder.toString();
	}

	@Override
	public byte[] getPayload() {
		try {
			return builder.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

}
