package com.narrowtux.blueberry.http.headers;

import org.joda.time.DateTime;

import com.narrowtux.blueberry.http.headers.factories.HeaderObjectFactory;

public class CookieFactory extends HeaderObjectFactory<Cookie> {

	@Override
	public Cookie createObject(String value) {
		String name = "";
		String cookieValue = "";
		String path = "/";
		String domain = "*";
		DateTime expires = new DateTime(0);
		Cookie.Security security = Cookie.Security.Both;
		
		// TODO actual parsing
		
		return new Cookie(name, cookieValue, path, domain, expires, security);
	}

	@Override
	public String getKey() {
		return "Set-Cookie";
	}
}
