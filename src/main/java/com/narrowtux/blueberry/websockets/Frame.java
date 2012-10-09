package com.narrowtux.blueberry.websockets;

import gnu.trove.map.hash.TByteObjectHashMap;

public abstract class Frame {
	public static final byte OP_CONT = 0x0;
	public static final byte OP_TEXT = 0x1;
	public static final byte OP_BINARY = 0x2;
	// 0x3 - 0x7 are reserved
	public static final byte OP_CLOSE = 0x8;
	public static final byte OP_PING = 0x9;
	public static final byte OP_PONG = 0xA;
	// 0xB - 0xF are reserved
	
	private static final TByteObjectHashMap<Class<? extends Frame>> frameTypes = new TByteObjectHashMap<Class<? extends Frame>>();
	
	static {
		frameTypes.put(OP_TEXT, TextFrame.class);
		frameTypes.put(OP_BINARY, BinaryFrame.class);
		frameTypes.put(OP_CLOSE, CloseFrame.class);
		frameTypes.put(OP_PING, PingFrame.class);
		frameTypes.put(OP_PONG, PongFrame.class);
	}
	
	protected int opcode = 0;
	
	public Frame() {
	}
	
	public int getOpCode() {
		return opcode;
	}
	
	/**
	 * Reads payload and translates it into the data
	 * @param payload
	 */
	public abstract void readPayload(byte[] payload);
	
	/**
	 * Gets the payload data of this frame
	 * The length of the payload can exceed the defined maximum and is automatically split into multiple frames internally
	 * @return the payload data
	 */
	public abstract byte[] getPayload();
	
	public static Frame newFrame(byte opcode) {
		try {
			return frameTypes.get(opcode).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			System.out.println("No frame type found for opcode "+opcode);
			return null;
		}
	}
}
