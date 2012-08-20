package com.narrowtux.blueberry.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import java.lang.annotation.Retention;

import com.narrowtux.blueberry.http.HttpRequestType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface HttpRequestHandlerMethod {
	String filter = "";
	HttpRequestType requestType = HttpRequestType.ANY;
}
