package com.elm.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {

	public static final String DISPLAY_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	public static final String SQL_LITE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public String dateString(Date date) {
		return new SimpleDateFormat(DISPLAY_DATE_FORMAT).format(date);
	}

	public String toSqlLiteDateString(Date date) {
		return new SimpleDateFormat(SQL_LITE_DATE_FORMAT).format(date);
	}

}
