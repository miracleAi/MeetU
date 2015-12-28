package com.meetu.sqlite;

import java.util.ArrayList;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.bean.UserAboutBean;
import com.meetu.common.Constants;
import com.meetu.common.DbConstents;

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
			sdb.execSQL(
					"insert or replace into userabout_cache_tb values(" + "?,?,?,?)",
					new Object[] {bean.getUserId(), bean.getAboutType(),bean.getAboutUserId(),bean.getAboutColetctionId()});
		}
		sdb.close();
	}
	public void saveUserAboutBean(UserAboutBean bean) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.execSQL(
				"insert or replace into userabout_cache_tb values(" + "?,?,?,?)",
				new Object[] {bean.getUserId(), bean.getAboutType(),bean.getAboutUserId(),bean.getAboutColetctionId()});
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
					+ DbConstents.USERABOUT_CACHE_TB + " where "
					+ DbConstents.ID_MINE + "=? and " + DbConstents.ABOUTTYPE
					+ "=? and " + DbConstents.ABOUTCOLECTIONID + "=?",
					new String[] { userId, Integer.toString(aboutType),
							colectionId });
		} else {
			// 根据类型查询指定类型所有
			cursor = sdb.rawQuery(
					"select * from " + DbConstents.USERABOUT_CACHE_TB + " where "
							+ DbConstents.ID_MINE + "=? and "
							+ DbConstents.ABOUTTYPE + "=?", new String[] {
							userId, Integer.toString(aboutType) });
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserAboutBean bean = new UserAboutBean();
			bean.setUserId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_MINE)));
			bean.setAboutType(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ABOUTTYPE)));
			bean.setAboutUserId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ABOUTUSERID)));
			bean.setAboutColetctionId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ABOUTCOLECTIONID)));
			aboutList.add(bean);
			cursor.moveToNext();
		}
		sdb.close();
		
		return aboutList;
	}

	// 删除指定缓存
	public void deleteByType(String userId, int aboutType, String colectionId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		if (!"".equals(colectionId)) {
			sdb.delete(DbConstents.USERABOUT_CACHE_TB, DbConstents.ID_MINE
					+ "=? and " + DbConstents.ABOUTTYPE + "=? and "
					+ DbConstents.ABOUTCOLECTIONID + "=?", new String[] { userId,
					Integer.toString(aboutType), colectionId });
		} else {
			sdb.delete(DbConstents.USERABOUT_CACHE_TB, DbConstents.ID_MINE
					+ "=? and " + DbConstents.ABOUTTYPE + "=?", new String[] {
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
		sdb.delete(DbConstents.USERABOUT_CACHE_TB, DbConstents.ID_MINE
				+ "=? and " + DbConstents.ABOUTTYPE + "=? and " + DbConstents.ABOUTUSERID + "=?", new String[] {
				userId, Integer.toString(aboutType) ,deleteUserId});
		sdb.close();
		log.e("zcq", "删除数据库成员成功");
	}


	
}
