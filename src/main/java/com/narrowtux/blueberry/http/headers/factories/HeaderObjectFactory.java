package com.narrowtux.blueberry.http.headers.factories;

public abstract class HeaderObjectFactory<T> {
	public abstract T createObject(String value);
	
	public abstract String getRequestHeaderKey();
}
