package com.meetu.tools;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/** 
 * @author  lucifer 
 * @date 2015-11-5
 * @return  
 */
public class StringToDate {
	// 将字符串转为时间戳
	 
	 public static String getTimea(String user_time) {
	  String re_time = null;
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	  Date d;
	  try {
	   d = sdf.parse(user_time);
	   long l = d.getTime();
	   String str = String.valueOf(l);
	   re_time = str.substring(0, 10);
	  } catch (ParseException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	  }
	  return re_time;
	 }
	 
	

}
