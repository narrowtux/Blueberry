package com.narrowtux.blueberry.handler;

import java.io.IOException;
import java.net.URI;

import gnu.trove.map.hash.TIntObjectHashMap;

import com.narrowtux.blueberry.HttpException;
import com.narrowtux.blueberry.http.HttpExchange;
import com.narrowtux.blueberry.http.HttpRequestMethod;
import com.narrowtux.blueberry.http.HttpVersion;
import com.narrowtux.blueberry.http.headers.HttpHeaders;
import com.narrowtux.blueberry.http.headers.factories.CookieFactory;
import com.narrowtux.blueberry.http.headers.factories.DateTimeFactory;
import com.narrowtux.blueberry.http.headers.factories.HeaderObjectFactory;
import com.narrowtux.blueberry.http.headers.factories.LongFactory;
import com.narrowtux.blueberry.util.HeaderUtils;

/**
 * <p>The base class that you have to extend to handle HTTP requests</p>
 * <h1>Handling a HTTP request 101</h1>
 * <p>All handling is done in the {@link HttpRequestHandler.handle} method. It will be called if your handler matches a HTTP request ({@link doesMatch}).</p>
 * <p>You can send content to the client by using the {@link com.narrowtux.blueberry.http.HttpExchange}.</p>
 * @author tux
 *
 */
public abstract class HttpRequestHandler {
	
	private String filter = "/";
	
	public abstract void handle(HttpExchange exchange) throws IOException, HttpException ;
	
	/**
	 * @param filter the basic filter.
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	/**
	 * @return the basic filter
	 */
	public String getFilter() {
		return filter;
	}
	
	/**
	 * <p>Called by the handler thread to determine if this handler qualifies for the given request</p>
	 * <p>The default implementation checks if the request method is one of {GET, POST} and the uri path starts with {@link getFilter}.</p>
	 * @param version the HTTP version of the request
	 * @param method the HTTP request method of the request
	 * @param uri the requested URI
	 * @return if this handler matches the given request attributes
	 * @deprecated use the instance with the HttpHeaders argument instead
	 */
	@Deprecated
	public boolean doesMatch(HttpVersion version, HttpRequestMethod method, URI uri){
		throw new UnsupportedOperationException(); // to catch for non-existent reimplementations of the deprecated method
	}

	/**
	 * <p>Called by the handler thread to determine if this handler qualifies for the given request</p>
	 * <p>The default implementation checks if the request method is one of {GET, POST} and the uri path starts with {@link getFilter}.</p>
	 * @param version the HTTP version of the request
	 * @param method the HTTP request method of the request
	 * @param uri the requested URI
	 * @param requestHeaders the headers the client has sent
	 * @return if this handler matches the given request attributes
	 */
	public boolean doesMatch(HttpVersion version, HttpRequestMethod method, URI uri, HttpHeaders requestHeaders) {
		try {
			return doesMatch(version, method, uri);
		} catch (UnsupportedOperationException e) {
			if (HeaderUtils.headerContains(requestHeaders, "Upgrade")) {
				return false; // Basic HTTP handler doesn't support connection upgrades
			}
			if (method == HttpRequestMethod.GET || method == HttpRequestMethod.POST) {
				return uri.getPath().startsWith(getFilter());
			} else {
				return false;
			}
		}
	}
	
	public boolean shouldClose() {
		return true;
	}
}
