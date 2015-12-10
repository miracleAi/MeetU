package com.meetu.common;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author  lucifer 
 * @date 2015-12-10
 * @return  
 */
public class GCJ_02ChangeBD_09 {
	public static  double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	public static List<Double> list=new ArrayList<Double>();
	/**
	 * GCJ_02ChangeBD_09
	 * @param gg_lat
	 * @param gg_lon
	 * @return  
	 * @author lucifer
	 * @date 2015-12-10
	 */
	public static List<Double> bd_encrypt(double gg_lat, double gg_lon)  
	{  
	    double x = gg_lon, y = gg_lat;  
	    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);  
	    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);  
	    double  bd_lon = z * Math.cos(theta) + 0.0065;  
	    double  bd_lat = z * Math.sin(theta) + 0.006;  
	    list=new ArrayList<Double>();
	    list.add(bd_lon);
	    list.add(bd_lat);
	    return list;
	}  
	  
/**
 * BD_09ChangeGCJ_02
 * @param bd_lat
 * @param bd_lon
 * @return  
 * @author lucifer
 * @date 2015-12-10
 */

	public static List<Double> bd_decrypt(double bd_lat, double bd_lon)  
	{  
	    double x = bd_lon - 0.0065, y = bd_lat - 0.006;  
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);  
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);  
	   double gg_lon = z * Math.cos(theta);  
	   double gg_lat = z * Math.sin(theta);  
	   list=new ArrayList<Double>();
	    list.add(gg_lon);
	    list.add(gg_lat);
	    return list;
	}  

}
