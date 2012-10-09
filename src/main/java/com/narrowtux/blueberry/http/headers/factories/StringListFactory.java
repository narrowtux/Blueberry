package com.narrowtux.blueberry.http.headers.factories;

import java.util.Arrays;
import java.util.List;

public class StringListFactory extends HeaderObjectFactory<List<String>> {
	private String key, splitBy;
	
	public StringListFactory(String key, String splitBy) {
		this.key = key;
		this.splitBy = splitBy;
	}

	@Override
	public List<String> createObject(String value) {
		return Arrays.asList(value.split(splitBy));
	}

	@Override
	public String getRequestHeaderKey() {
		return key;
	}

}
