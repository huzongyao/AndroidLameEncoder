package com.hzy.lamedemo;

import java.util.Calendar;
import java.util.Date;

public class CommonUtils {
	
	public static String generateMp3FileName(){
		long backTime = new Date().getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(backTime));
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		String time = "rec_" + year + month + date + hour + minute + second + ".mp3";
		return time;
	}
}
