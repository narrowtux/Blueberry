package com.narrowtux.blueberry.handler;

import gnu.trove.map.hash.TIntObjectHashMap;

import com.narrowtux.blueberry.http.headers.HttpStatusCode;

/**
 * <p>The error handler will automatically handle any exception catched by the {@link com.narrowtux.blueberry.RequestHandlerThread}.</p>
 * <p>You can register a custom handler for a code by using {@link ErrorHandler.registerErrorHandler}.</p>
 * @author tux
 *
 */
public abstract class ErrorHandler extends HttpRequestHandler {
	HttpStatusCode currentCode;
	static DefaultErrorHandler def = new DefaultErrorHandler();
	Throwable throwed = null;
	
	public static void setDefaultErrorHandler(DefaultErrorHandler def) {
		ErrorHandler.def = def;
	}
	
	protected HttpStatusCode getCurrentCode() {
		return currentCode;
	}
	
	protected Throwable getThrowed() {
		return throwed;
	}
	
	protected void setCurrentCode(HttpStatusCode currentCode) {
		this.currentCode = currentCode;
	}
	
	private static TIntObjectHashMap<ErrorHandler> factory = new TIntObjectHashMap<ErrorHandler>();
	
	public static void registerErrorHandler(HttpStatusCode forCode, ErrorHandler handler) {
		factory.put(forCode.getCode(), handler);
	}

	public static ErrorHandler withError(HttpStatusCode code) {
		ErrorHandler handler = factory.get(code.getCode());
		if (handler == null) {
			handler = def;
		}
		handler.setCurrentCode(code);
		return handler;
	}
	
	public ErrorHandler withException(Throwable t) {
		this.throwed = t;
		return this;
	}
}
