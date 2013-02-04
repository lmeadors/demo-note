package com.elm.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {

	public String dateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return format.format(date);
	}

}
