package com.meetu.common;

import com.avos.avoscloud.LogUtil.log;

/** 
 * @author  lucifer 
 * @date 2015-12-4
 * @return  
 */
public class dismissData {
	
	/**
	 * 消失时间 封装类
	 * @param timeOver
	 * @return  
	 * @author lucifer
	 * @date 2015-12-4
	 */
	
	public static String getDismissData(long timeOver){
		long nowData=System.currentTimeMillis();
		long numbers=timeOver-nowData;
		log.e("zcq", "numbers"+numbers);
		if((timeOver-nowData)>=1000*60*60*24){
			//大于24小时  不显示
			
			return null;
		}else  if((timeOver-nowData)>=1000*60*60){
				String string=""+((timeOver-nowData)/(1000*60*60))+"h";
				return string;
			}else if((timeOver-nowData)>=1000*60){
				String string=""+((timeOver-nowData)/(1000*60))+"min";
				return string;
			}else if((timeOver-nowData)>=1000){
				String string=""+((timeOver-nowData)/1000)+"s";
				return string;
			}else{
				return "dismiss";
			}
	}

}
