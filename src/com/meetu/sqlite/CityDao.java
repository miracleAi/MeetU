package com.meetu.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.meetu.entity.City;





import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityDao {
	private SQLiteDatabase database;
	
	public List<City> getPrivance(){
		database = SQLiteDatabase.openOrCreateDatabase(DBManagerCity.DB_PATH + "/" + DBManagerCity.DB_NAME, null);
		String sql="select *from city";
		List<City> list=new ArrayList<City>();
		Cursor c=database.rawQuery(sql, null);
		while(c.moveToNext()){
			City item=new City();
			item.setId(c.getString(c.getColumnIndex("id")));
			item.setPrivance(c.getString(c.getColumnIndex("province")));
			item.setPrivance_num(c.getString(c.getColumnIndex("province_num")));
			item.setCity(c.getString(c.getColumnIndex("city")));
			item.setCity_num(c.getString(c.getColumnIndex("city_num")));
			item.setTown(c.getString(c.getColumnIndex("town")));
			item.setTown_num(c.getString(c.getColumnIndex("town_num")));
			
			System.out.println("id="+item.getId()+" 省= "+item.getPrivance()+" 市="+item.getCity()+"区="+item.getTown());

			list.add(item);			
		}
		return list;
		
		
	}
	
	/**
	 * 查询所有的省信息
	 * @return  
	 * @author lucifer
	 * @date 2015-11-4
	 */
	public List<City> getAllPrivance(){
		database = SQLiteDatabase.openOrCreateDatabase(DBManagerCity.DB_PATH + "/" + DBManagerCity.DB_NAME, null);
		String sql="select *from city group by province_num";
		List<City> list=new ArrayList<City>();
		Cursor c=database.rawQuery(sql, null);
		while(c.moveToNext()){
			City item=new City();
			item.setId(c.getString(c.getColumnIndex("id")));
			item.setPrivance(c.getString(c.getColumnIndex("province")));
			item.setPrivance_num(c.getString(c.getColumnIndex("province_num")));
			item.setCity(c.getString(c.getColumnIndex("city")));
			item.setCity_num(c.getString(c.getColumnIndex("city_num")));
			item.setTown(c.getString(c.getColumnIndex("town")));
			item.setTown_num(c.getString(c.getColumnIndex("town_num")));
			
			System.out.println("id="+item.getId()+" 省= "+item.getPrivance()+" 市="+item.getCity()+"区="+item.getTown());

			list.add(item);			
		}
		return list;
		
	}
	/**
	 * 根据传进来的省 查 该省的所有市
	 * @param provinceNum
	 * @return  
	 * @author lucifer
	 * @date 2015-11-4
	 */
	public List<City>getAllCity(String provinceNum){
		
		database = SQLiteDatabase.openOrCreateDatabase(DBManagerCity.DB_PATH + "/" + DBManagerCity.DB_NAME, null);
		String sql="select *from city where province= ? group by city_num";
		List<City> list=new ArrayList<City>();
		Cursor c=database.rawQuery(sql, new String[]{provinceNum});
		while(c.moveToNext()){
			City item=new City();
			item.setId(c.getString(c.getColumnIndex("id")));
			item.setPrivance(c.getString(c.getColumnIndex("province")));
			item.setPrivance_num(c.getString(c.getColumnIndex("province_num")));
			item.setCity(c.getString(c.getColumnIndex("city")));
			item.setCity_num(c.getString(c.getColumnIndex("city_num")));
			item.setTown(c.getString(c.getColumnIndex("town")));
			item.setTown_num(c.getString(c.getColumnIndex("town_num")));
			
			System.out.println("id="+item.getId()+" 省= "+item.getPrivance()+" 市="+item.getCity()+"区="+item.getTown());

			list.add(item);			
		}
		return list;
		
	}
	/**
	 * 根据传捡来的省和市 查询所有的区
	 * @param provinceNum
	 * @param cityNum
	 * @return  
	 * @author lucifer
	 * @date 2015-11-4
	 */
	
	public List<City>getAllTown(String provinceNum,String cityNum){
		database = SQLiteDatabase.openOrCreateDatabase(DBManagerCity.DB_PATH + "/" + DBManagerCity.DB_NAME, null);
		String sql="select *from city where province=? and city=?";
		List<City> list=new ArrayList<City>();
		Cursor c=database.rawQuery(sql, new String[]{provinceNum,cityNum});
		while(c.moveToNext()){
			City item=new City();
			item.setId(c.getString(c.getColumnIndex("id")));
			item.setPrivance(c.getString(c.getColumnIndex("province")));
			item.setPrivance_num(c.getString(c.getColumnIndex("province_num")));
			item.setCity(c.getString(c.getColumnIndex("city")));
			item.setCity_num(c.getString(c.getColumnIndex("city_num")));
			item.setTown(c.getString(c.getColumnIndex("town")));
			item.setTown_num(c.getString(c.getColumnIndex("town_num")));
			
			System.out.println("id="+item.getId()+" 省= "+item.getPrivance()+" 市="+item.getCity()+"区="+item.getTown());

			list.add(item);			
		}
		return list;
	}
	
	/**
	 * 根据省市区的编号 查到 唯一的id信息
	 * @param provinceNum
	 * @param cityNum
	 * @param twonNum
	 * @return  
	 * @author lucifer
	 * @date 2015-11-4
	 */
	public List<City> getID(String provinceNum,String cityNum,String twonNum){
		
		database = SQLiteDatabase.openOrCreateDatabase(DBManagerCity.DB_PATH + "/" + DBManagerCity.DB_NAME, null);
		String sql="select *from city where province=? and city=? and town=?";
		List<City> list=new ArrayList<City>();
		Cursor c=database.rawQuery(sql, new String[]{provinceNum,cityNum,twonNum});
		while(c.moveToNext()){
			City item=new City();
			item.setId(c.getString(c.getColumnIndex("id")));
			item.setPrivance(c.getString(c.getColumnIndex("province")));
			item.setPrivance_num(c.getString(c.getColumnIndex("province_num")));
			item.setCity(c.getString(c.getColumnIndex("city")));
			item.setCity_num(c.getString(c.getColumnIndex("city_num")));
			item.setTown(c.getString(c.getColumnIndex("town")));
			item.setTown_num(c.getString(c.getColumnIndex("town_num")));
			
			System.out.println("id="+item.getId()+" 省= "+item.getPrivance()+" 市="+item.getCity()+"区="+item.getTown());

			list.add(item);			
		}
		return list;
		
	}
	

	

}
