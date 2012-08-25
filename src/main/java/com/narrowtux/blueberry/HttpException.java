package com.narrowtux.blueberry;

import com.narrowtux.blueberry.http.headers.HttpStatusCode;

public class HttpException extends Exception {
	private static final long serialVersionUID = 3090163447247112979L;

	private final HttpStatusCode code;
	private final Throwable t;
	
	public HttpException(HttpStatusCode code, Throwable t) {
		super();
		this.code = code;
		this.t = t;
	}
	
	public HttpException(HttpStatusCode code, String message) {
		this(code, new Exception(message));
	}

	public HttpStatusCode getCode() {
		return code;
	}

	public Throwable getT() {
		return t;
	}
}
