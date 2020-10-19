package kr.hyperlinkpool.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String getToday(String format) {
		Date date = new Date(); 
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String today = simpleDateFormat.format(date);
		return today;
	}
	
}
