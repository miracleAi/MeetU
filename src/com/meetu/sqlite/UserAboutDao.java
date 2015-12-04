package com.meetu.sqlite;

import java.util.ArrayList;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.bean.UserAboutBean;
import com.meetu.common.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserAboutDao {
	private MySqliteDBHelper dbHelper;

	public UserAboutDao(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new MySqliteDBHelper(context);
	}

	// 保存用户相关集合
	public void saveUserAboutList(ArrayList<UserAboutBean> list) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		for (int i = 0; i < list.size(); i++) {
			UserAboutBean bean = list.get(i);
			ContentValues cv = new ContentValues();
			cv.put(Constants.USERID, bean.getUserId());
			cv.put(Constants.ABOUTTYPE, bean.getAboutType());
			cv.put(Constants.ABOUTUSERID, bean.getAboutUserId());
			cv.put(Constants.ABOUTCOLECTIONID, bean.getAboutColetctionId());
			sdb.insert(Constants.USERABOUT_CACHE_TB, null, cv);
		}
		sdb.close();
	}

	// 查询指定集合
	public ArrayList<UserAboutBean> queryUserAbout(String userId,
			int aboutType, String colectionId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		ArrayList<UserAboutBean> aboutList = new ArrayList<UserAboutBean>();
		Cursor cursor = null;
		if (!"".equals(colectionId)) {
			// 查询指定成员
			cursor = sdb.rawQuery("select * from "
					+ Constants.USERABOUT_CACHE_TB + " where "
					+ Constants.USERID + "=? and " + Constants.ABOUTTYPE
					+ "=? and " + Constants.ABOUTCOLECTIONID + "=?",
					new String[] { userId, Integer.toString(aboutType),
							colectionId });
		} else {
			// 根据类型查询指定类型所有
			cursor = sdb.rawQuery(
					"select * from " + Constants.USERABOUT_CACHE_TB + " where "
							+ Constants.USERID + "=? and "
							+ Constants.ABOUTTYPE + "=?", new String[] {
							userId, Integer.toString(aboutType) });
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserAboutBean bean = new UserAboutBean();
			bean.setUserId(cursor.getString(cursor
					.getColumnIndex(Constants.USERID)));
			bean.setAboutType(cursor.getInt(cursor
					.getColumnIndex(Constants.ABOUTTYPE)));
			bean.setAboutUserId(cursor.getString(cursor
					.getColumnIndex(Constants.ABOUTUSERID)));
			bean.setAboutColetctionId(cursor.getString(cursor
					.getColumnIndex(Constants.ABOUTCOLECTIONID)));
			aboutList.add(bean);
			cursor.moveToNext();
		}
		return aboutList;
	}

	// 删除指定缓存
	public void deleteByType(String userId, int aboutType, String colectionId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		if (!"".equals(colectionId)) {
			sdb.delete(Constants.USERABOUT_CACHE_TB, Constants.USERID
					+ "=? and " + Constants.ABOUTTYPE + "=? and "
					+ Constants.ABOUTCOLECTIONID + "=?", new String[] { userId,
					Integer.toString(aboutType), colectionId });
		} else {
			sdb.delete(Constants.USERABOUT_CACHE_TB, Constants.USERID
					+ "=? and " + Constants.ABOUTTYPE + "=?", new String[] {
					userId, Integer.toString(aboutType) });
		}
		sdb.close();
	}
	/**
	 * 删除指定对话中的指定用户
	 * @param userId
	 * @param aboutType
	 * @param convercationId
	 * @param deleteUserId  
	 * @author lucifer
	 * @date 2015-12-4
	 */
	public void deleteUserTypeUserId(String userId,int aboutType,String convercationId,String deleteUserId){
		
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.delete(Constants.USERABOUT_CACHE_TB, Constants.USERID
				+ "=? and " + Constants.ABOUTTYPE + "=? and " + Constants.ABOUTUSERID + "=?", new String[] {
				userId, Integer.toString(aboutType) ,deleteUserId});
		sdb.close();
		log.e("zcq", "删除数据库成员成功");
	}
	
	
	// 查询参加活动并且我关注的人
	/*
	 * public ArrayList<UserAboutBean> queryOrderAndFollowUser(String userId,int
	 * type1,int type2){ ArrayList<UserAboutBean> list = new
	 * ArrayList<UserAboutBean>(); SQLiteDatabase
	 * sdb=dbHelper.getWritableDatabase(); Cursor
	 * cursor=sdb.rawQuery("select * from "+
	 * Constants.USERABOUT_CACHE_TB+" where "+Constants.USERID +"=? and "
	 * +Constants
	 * .ABOUTTYPE+"=? and "+Constants.ABOUTUSERID+" in select "+Constants
	 * .ABOUTUSERID+" from "+
	 * Constants.ACTIVITY_CACHE_TB+" where "+Constants.USERID +"=? and "
	 * +Constants.ABOUTTYPE+"=?",new
	 * String[]{userId,Integer.toString(type1),userId,Integer.toString(type2)});
	 * return list; }
	 */
}
