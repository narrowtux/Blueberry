package com.narrowtux.blueberry.handler;

import gnu.trove.map.hash.TIntObjectHashMap;

import com.narrowtux.blueberry.http.HttpExchange;
import com.narrowtux.blueberry.http.headers.CookieFactory;
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
	
	public void registerRequestHeader(HeaderObjectFactory<?> factory) {
		requestHeaderFactories.put(factory.getKey().hashCode(), factory);
	}
	
	public abstract void handle(HttpExchange exchange);
	
	
}
