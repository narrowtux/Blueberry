package com.narrowtux.blueberry.http.headers.factories;

import java.util.LinkedList;
import java.util.List;

import com.narrowtux.blueberry.http.headers.Cookie;

public class CookieFactory extends HeaderObjectFactory<List<Cookie>> {

	@Override
	public List<Cookie> createObject(String value) {
		List<Cookie> ret = new LinkedList<Cookie>();
		for (String v:value.split("; ")) {
			String name = "";
			String cookieValue = "";
			String sp[] = v.split("=", 2);
			name = sp[0];
			cookieValue = sp[1];
			ret.add(new Cookie(name, cookieValue, null, null, null, null));
		}
		return ret;
	}

	@Override
	public String getRequestHeaderKey() {
		return "Cookie";
	}
}
