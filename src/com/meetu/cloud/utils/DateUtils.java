package com.meetu.cloud.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	// 常用的格式
	public static final String DateFormat_Only_Date = "MM-dd";
	public static final String DateFormat_Date = "yyyy-MM-dd";
	public static final String DateFormat_Time = "HH:mm:ss";
	public static final String DateFormat_DateTime = "MM-dd HH:mm:ss";
	public static final String DateFormat_YearTime = "yyyy-MM-dd HH:mm:ss";

	// 一分钟
	public static final long Time_Of_Minute = 60 * 1000;
	// 一小时
	public static final long Time_Of_Hour = 60 * Time_Of_Minute;
	// 一天
	public static final long Time_Of_Day = 24 * Time_Of_Hour;

	/**
	 * 格式化日期
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(long time, String format) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(date);
	}

	/**
	 * 获取目标显示
	 * 
	 * @param date
	 * @return
	 */
	public static String getFormattedTimeInterval(long time) {
		Date date = new Date(time);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int hour = cal.get(Calendar.HOUR);// 小时
		int minute = cal.get(Calendar.MINUTE);// 分
		int second = cal.get(Calendar.SECOND);// 秒
		Calendar calMsg = Calendar.getInstance();
		calMsg.setTime(date);
		int yearMsg = calMsg.get(Calendar.YEAR);
		long todayZero = System.currentTimeMillis() - hour * Time_Of_Hour
				- minute * Time_Of_Minute - second * 1000;
		if (todayZero - time < 0) {
			return format(time, DateFormat_Time);
		}
		if (todayZero - time < 24) {
			return "昨天 " + format(time, DateFormat_Time);
		}
		if (todayZero - time < 48) {
			return "前天 " + format(time, DateFormat_Time);
		}
		if (year == yearMsg) {
			return format(time, DateFormat_DateTime);
		}
		if ((year - yearMsg) == 1) {
			return "去年" + format(time, DateFormat_DateTime);
		}
		return format(time, DateFormat_YearTime);
	}
}
