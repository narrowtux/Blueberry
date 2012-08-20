package com.narrowtux.blueberry.http.headers;

import org.joda.time.DateTime;


public class Cookie implements HeaderObject {
	private String name = null, content = null, path = null, domain = null;
	private Security security;
	private DateTime expires;
	
	public Cookie(String name, String content, String path, String domain,
			DateTime expires, Security security) {
		this.name = name;
		this.content = content;
		this.path = path;
		this.domain = domain;
		this.expires = expires;
		this.security = security;
	}

	public String getHeaderValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static enum Security {
		HttpOnly,
		Secure,
		Both
	}
}
