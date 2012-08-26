package com.narrowtux.blueberry.http.headers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.narrowtux.blueberry.BlueberryWebServer;
import com.narrowtux.blueberry.http.HttpExchange;
import com.narrowtux.blueberry.http.headers.factories.HeaderObjectFactory;

public class HttpHeaders {
	boolean sealed = false;
	LinkedHashMap<String, LinkedList<Object>> headers = new LinkedHashMap<String, LinkedList<Object>>(10);
	BlueberryWebServer parent;
	static HashSet<String> multiHeaderLinesAllowed = new HashSet<String>();
	
	static {
		multiHeaderLinesAllowed.add("Set-Cookie");
	}
	
	public HttpHeaders(BlueberryWebServer parent) {
		super();
		this.parent = parent;
	}

	public void addHeader(String key, Object value) {
		if (sealed) {
			throw new IllegalStateException("This header is readonly");
		}
		LinkedList<Object> contents = headers.get(key);
		if (contents == null) {
			contents = new LinkedList<Object>();
			headers.put(key, contents);
		}
		if (value instanceof List<?>) {
			contents.addAll((List<?>) value);
		} else {
			contents.add(value);
		}
	}
	
	public void addHeader(HeaderObject object) {
		addHeader(object.getResponseHeaderKey(), object);
	}
	
	public void setHeader(HeaderObject object) {
		setHeader(object.getResponseHeaderKey(), object);
	}
	
	/**
	 * Sets a header value
	 * Useful against addHeader when the header is a singleton
	 * @param key the key to set
	 * @param value the value to set
	 */
	public void setHeader(String key, Object value) {
		LinkedList<Object> list = new LinkedList<Object>();
		list.add(value);
		headers.put(key, list);
	}
	
	public List<Object> getHeader(String key) {
		return headers.get(key);
	}
	
	public void seal() {
		sealed = true;
	}
	
	public void read(BufferedReader reader) throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null && !line.isEmpty()) {
			String sp[] = line.split(": ", 2);
			String key = sp[0];
			Object value = sp[1];
			
			HeaderObjectFactory<?> factory = parent.getRequestHeaderFilter(key);
			if (factory != null) {
				value = factory.createObject((String) value);
			}
			addHeader(key, value);
		}
	}
	
	public void write(BufferedWriter writer) throws IOException {
		for (Entry<String, LinkedList<Object>> e:headers.entrySet()) {
			String key = e.getKey();
			boolean multi = multiHeaderLinesAllowed.contains(key);
			String values = "";
			for (Object value:e.getValue()) {
				String toSend = value.toString();
				if (value instanceof HeaderObject) {
					HeaderObject ho = (HeaderObject) value;
					toSend = ho.getHeaderValue();
				}
				values += toSend;
				if (value != e.getValue().getLast()) {
					values += !multi? "; " : "\n"+key+": ";
				}
			}
			writer.write(key + ": " + values + "\n");
		}
		writer.write("\n");
		writer.flush();
		seal();
	}

	public boolean isSealed() {
		return sealed;
	}
}
