package com.narrowtux.blueberry.handler;

import java.io.IOException;
import java.net.URI;

import gnu.trove.map.hash.TIntObjectHashMap;

import com.narrowtux.blueberry.http.HttpExchange;
import com.narrowtux.blueberry.http.HttpRequestMethod;
import com.narrowtux.blueberry.http.HttpVersion;
import com.narrowtux.blueberry.http.headers.factories.CookieFactory;
import com.narrowtux.blueberry.http.headers.factories.DateTimeFactory;
import com.narrowtux.blueberry.http.headers.factories.HeaderObjectFactory;

public abstract class HttpRequestHandler {
	TIntObjectHashMap<HeaderObjectFactory<?>> requestHeaderFactories = new TIntObjectHashMap<HeaderObjectFactory<?>>();
	
	{
		registerRequestHeader(new CookieFactory());
		registerRequestHeader(new DateTimeFactory("Accept-Datetime"));
		registerRequestHeader(new DateTimeFactory("Date"));
		registerRequestHeader(new DateTimeFactory("If-Modified-Since"));
		registerRequestHeader(new DateTimeFactory("If-Unmodified-Since"));
	}
	
	private String filter = "/";
	
	public void registerRequestHeader(HeaderObjectFactory<?> factory) {
		requestHeaderFactories.put(factory.getRequestHeaderKey().hashCode(), factory);
	}
	
	public abstract void handle(HttpExchange exchange) throws IOException ;
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public boolean doesMatch(HttpVersion version, HttpRequestMethod method, URI uri){
		if (method == HttpRequestMethod.GET || method == HttpRequestMethod.POST) {
			return uri.getPath().startsWith(filter);
		} else {
			return false;
		}
	}

	public HeaderObjectFactory<?> getRequestHeaderFilter(String key) {
		return requestHeaderFactories.get(key.hashCode());
	}
}
