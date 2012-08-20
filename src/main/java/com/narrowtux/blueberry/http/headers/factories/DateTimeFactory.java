package com.narrowtux.blueberry.http.headers.factories;

import org.joda.time.DateTime;

public class DateTimeFactory extends HeaderObjectFactory<DateTime> {
	final String key;
	
	public DateTimeFactory(String forKey) {
		this.key = forKey;
	}
	
	@Override
	public DateTime createObject(String value) {
		return DateTime.parse(value);
	}

	@Override
	public String getKey() {
		return key;
	}

}
