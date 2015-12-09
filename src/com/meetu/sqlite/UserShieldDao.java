package com.meetu.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.meetu.bean.UserShieldBean;
import com.meetu.cloud.object.ObjShieldUser;
import com.meetu.common.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 用户屏蔽表操作
 * */
public class UserShieldDao {
	private MySqliteDBHelper helper;

	public UserShieldDao(Context context) {
		helper = new MySqliteDBHelper(context);
	}

	// 插入列表
	public void saveShieldList(List<ObjShieldUser> list) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		if(list.size() == 0){
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			ObjShieldUser objShieldUser = list.get(i);
			UserShieldBean bean = new UserShieldBean();
			// 被屏蔽者为用户自己
			bean.setUserId(objShieldUser.getShieldUser().getObjectId());
			bean.setShieldUserId(objShieldUser.getUser().getObjectId());
			ContentValues cv = new ContentValues();
			cv.put(Constants.USERID, bean.getUserId());
			cv.put(Constants.SHIELD_USER_ID, bean.getShieldUserId());
			sdb.insert(Constants.USER_SHIELD_TB, null, cv);
		}
		sdb.close();
	}
	// 查询是否被某用户屏蔽
	public boolean queryIsShield(String userId, String shieldUserId) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from "
				+ Constants.USER_SHIELD_TB + " where " + Constants.USERID
				+ "=? and " + Constants.SHIELD_USER_ID + "=?", new String[] {
				userId, shieldUserId });
		if (cursor.moveToNext()) {
			// 存在，即被shieldUserId屏蔽
			return true;
		} else {
			return false;
		}
	}

	// 根据用户清除屏蔽列表缓存
	public void deleteByUser(String userId) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		sdb.delete(Constants.USER_SHIELD_TB, Constants.SHIELD_USER_ID + "=?",
				new String[] { userId });
		sdb.close();
	}
}
