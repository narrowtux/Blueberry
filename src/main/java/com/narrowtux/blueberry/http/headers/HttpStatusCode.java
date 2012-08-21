package com.narrowtux.blueberry.http.headers;

import gnu.trove.map.hash.TIntObjectHashMap;

import com.narrowtux.blueberry.http.HttpVersion;

public enum HttpStatusCode {
	// 1xx Informatinonal
	HTTP_100_CONTINUE(100, "Continue", HttpVersion.HTTP11),
	HTTP_101_SWITCHING_PROTOCOLS(101, "Switching Protocols", HttpVersion.HTTP11),
	HTTP_102_PROCESSING(102, "Processing", HttpVersion.HTTP11),
	
	// 2xx Success
	HTTP_200_OK(200, "OK"),
	HTTP_201_CREATED(201, "Created"),
	HTTP_202_ACCEPTED(202, "Accepted"),
	HTTP_203_NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
	HTTP_204_NO_CONTENT(204, "No Content"),
	HTTP_205_RESET_CONTENT(205, "Reset Content"),
	HTTP_206_PARTIAL_CONTENT(206, "Partial Content"),
	HTTP_207_MULTI_STATUS(207, "Multi-Status"),
	HTTP_208_ALREADY_REPORTED(208, "Already Reported"),
	HTTP_226_IM_USED(226, "IM Used"),
	
	// 3xx Redirection
	HTTP_300_MULTIPLE_CHOICES(300, "Multiple Choices"),
	HTTP_301_MOVED_PERMANENTLY(301, "Moved Permanently"),
	HTTP_302_FOUND(302, "Found"),
	HTTP_303_SEE_OTHER(303, "See Other"),
	HTTP_304_NOT_MODIFIED(304, "Not Modified"),
	HTTP_305_USE_PROXY(305, "Use Proxy"),
	HTTP_306_RESERVED(306, "Reserved"),
	HTTP_307_TEMPORARY_REDIRECT(307, "Temporary Redirect"),
	HTTP_308_PERMANENT_REDIRECT(308, "Permanent Redirect"),
	
	// 4xx Client Error
	HTTP_400_BAD_REQUEST(400, "Bad Request"),
	HTTP_401_UNAUTHORIZED(401, "Unauthorized"),
	HTTP_402_PAYMENT_REQUIRED(402, "Payment Required"),
	HTTP_403_FORBIDDEN(403, "Forbidden"),
	HTTP_404_NOT_FOUND(404, "Not Found"),
	HTTP_405_METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
	HTTP_406_NOT_ACCEPTABLE(406, "Not Acceptable"),
	HTTP_407_PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
	HTTP_408_REQUEST_TIMEOUT(408, "Request Timeout"),
	HTTP_409_CONFLICT(409, "Conflict"),
	HTTP_410_GONE(410, "Gone"),
	HTTP_411_LENGTH_REQUIRED(411, "Length Required"),
	HTTP_412_PRECONDITION_FAILED(412, "Precondition Failed"),
	HTTP_413_REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
	HTTP_414_REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
	HTTP_415_UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
	HTTP_416_REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
	HTTP_417_EXPECTATION_FAILED(417, "Expectation Failed"),
	HTTP_422_UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
	HTTP_423_LOCKED(423, "Locked"),
	HTTP_424_FAILED_DEPENDENCY(424, "Failed Dependency"),
	HTTP_425_RESERVED_FOR_WEBDAV_ADVANCED_COLLECTIONS_EXPIRED_PROPOSAL(425, "Reserved for WebDAV advanced collections expired proposal"),
	HTTP_426_UPGRADE_REQUIRED(426, "Upgrade Required"),
	HTTP_427_UNASSIGNED(427, "Unassigned"),
	HTTP_428_PRECONDITION_REQUIRED(428, "Precondition Required"),
	HTTP_429_TOO_MANY_REQUESTS(429, "Too Many Requests"),
	HTTP_430_UNASSIGNED(430, "Unassigned"),
	HTTP_431_REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
	
	// 5xx Server error
	HTTP_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	HTTP_501_NOT_IMPLEMENTED(501, "Not Implemented"),
	HTTP_502_BAD_GATEWAY(502, "Bad Gateway"),
	HTTP_503_SERVICE_UNAVAILABLE(503, "Service Unavailable"),
	HTTP_504_GATEWAY_TIMEOUT(504, "Gateway Timeout"),
	HTTP_505_HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
	HTTP_506_VARIANT_ALSO_NEGOTIATES_EXPERIMENTAL(506, "Variant Also Negotiates (Experimental)"),
	HTTP_507_INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
	HTTP_508_LOOP_DETECTED(508, "Loop Detected"),
	HTTP_509_UNASSIGNED(509, "Unassigned"),
	HTTP_510_NOT_EXTENDED(510, "Not Extended"),
	HTTP_511_NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required"),
	;
	
	private final int code;
	private final String title;
	private final HttpVersion minVersion;
	
	private static TIntObjectHashMap<HttpStatusCode> byCode = new TIntObjectHashMap<HttpStatusCode>();
	
	static {
		for (HttpStatusCode element:values()) {
			byCode.put(element.getCode(), element);
		}
	}

	private HttpStatusCode(int code, String title) {
		this(code, title, HttpVersion.HTTP10);
	}

	private HttpStatusCode(int code, String title, HttpVersion minVersion) {
		this.code = code;
		this.title = title;
		this.minVersion = minVersion;
	}

	public int getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public HttpVersion getMinVersion() {
		return minVersion;
	}
	
	public HttpStatusCode byCode(int code) {
		return byCode.get(code);
	}
}
