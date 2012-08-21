package com.narrowtux.blueberry.http;

public enum HttpVersion {
	HTTP10(0),
	HTTP11(1),
	;
	
	private final int code;

	private HttpVersion(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
