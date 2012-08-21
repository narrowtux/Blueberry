package com.narrowtux.blueberry.util;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class DateUtils {
	static DateTimeFormatter rfc1123 = (new DateTimeFormatterBuilder()).appendPattern("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'").toFormatter().withLocale(Locale.ENGLISH);
	
	public static String toRFC1123(DateTime dateTime) {
		dateTime = new DateTime(dateTime.getZone().convertLocalToUTC(dateTime.getMillis(), true));
		return dateTime.toString(rfc1123);
	}
}
