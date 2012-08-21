package com.narrowtux.blueberry.http;

import java.util.HashMap;

public enum HttpVersion {
	HTTP10(0, "1.0"),
	HTTP11(1, "1.1"),
	;
	
	private final int code;
	private final String versionString;
	private final static HashMap<String, HttpVersion> versions = new HashMap<String, HttpVersion>(2);
	
	static {
		for (HttpVersion version:values()) {
			versions.put(version.getVersionString(), version);
		}
	}

	private HttpVersion(int code, String version) {
		this.code = code;
		this.versionString = version;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getVersionString() {
		return versionString;
	}
	
	public static HttpVersion byString(String version) {
		return versions.get(version);
	}
}
