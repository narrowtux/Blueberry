package com.narrowtux.blueberry.http.headers;

import org.joda.time.DateTime;

import com.narrowtux.blueberry.util.DateUtils;


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
		StringBuilder builder = new StringBuilder();
		builder.append(name).append('=').append(content);
		
		if (expires != null) {
			builder.append("; Expires=");
			builder.append(DateUtils.toRFC1123(expires));
		}
		
		if (domain != null) {
			builder.append("; Domain=");
			builder.append(domain);
		}
		
		if (path != null) {
			builder.append("; Path=");
			builder.append(path);
		}
		
		if (security != null) {
			switch (security) {
			case Both:
				builder.append("; ").append(Security.HttpOnly).append("; ").append(Security.Secure);
				break;
			default:
				builder.append("; ").append(security);
				break;
			}
		}
		
		return builder.toString();
	}
	
	public static enum Security {
		HttpOnly,
		Secure,
		Both
	}

	public String getResponseHeaderKey() {
		return "Set-Cookie";
	}
}
