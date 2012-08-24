package com.narrowtux.blueberry.http.headers.factories;

public class LongFactory extends HeaderObjectFactory<Long> {
	String key;
	
	public LongFactory(String key) {
		this.key = key;
	}

	@Override
	public Long createObject(String value) {
		return Long.parseLong(value);
	}

	@Override
	public String getRequestHeaderKey() {
		return key;
	}

}
