package com.narrowtux.blueberry.util;

import java.util.List;

import com.narrowtux.blueberry.http.headers.HttpHeaders;

public class HeaderUtils {
	public static boolean headerEquals(HttpHeaders headers, String key, Object match) {
		List<Object> header = headers.getHeader(key);
		if (header != null && header.size() > 0) {
			for (Object o : header) {
				if (o.equals(match)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean headerContains(HttpHeaders headers, String key) {
		return headers.getHeader(key) != null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getHeaderValueAs(HttpHeaders headers, String key, Class<T> type) {
		List<Object> header = headers.getHeader(key);
		if (header != null && header.size() > 0) {
			Object o = header.get(0);
			if (type.isInstance(o)) {
				return (T) header.get(0);
			}
		}
		return null;
	}
}
