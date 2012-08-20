package com.narrowtux.blueberry.http.headers;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class HttpHeaders {
	boolean sealed = false;
	LinkedHashMap<String, LinkedList<Object>> headers = new LinkedHashMap<String, LinkedList<Object>>(10);
	
	public void addHeader(String key, String value) {
		if (sealed) {
			throw new IllegalStateException("This header is readonly");
		}
		LinkedList<Object> contents = headers.get(key);
		if (contents == null) {
			contents = new LinkedList<Object>();
			headers.put(key, contents);
		}
		contents.add(value);
	}
	
	public List<Object> getHeader(String key) {
		return headers.get(key);
	}
	
	public void seal() {
		sealed = true;
	}
}
