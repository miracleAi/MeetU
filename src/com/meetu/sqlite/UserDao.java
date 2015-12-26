package com.meetu.sqlite;

import java.util.ArrayList;

import android.R.string;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.bean.UserBean;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;
import com.meetu.common.DbConstents;
import com.meetu.tools.DensityUtil;

public class UserDao {
	private MySqliteDBHelper helper;
	private int photoWidth=0;

	public UserDao(Context context) {
		helper = new MySqliteDBHelper(context);
		photoWidth=DensityUtil.dip2px(context, 40);
	}

	// 插入或替换用户
	public void insertOrReplaceUser(ObjUser user) {
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(
				"insert or replace into userinfo_tb values("
						+ "?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?)",
				new Object[] {
						user.getObjectId(),
						user.getUserType(),
						bolToInt(user.isVipUser()),
						bolToInt(user.isCompleteUserInfo()),
						bolToInt(user.isVerifyUser()),
						user.getGender(),
						user.getNameNick(),
						user.getNameReal(),
						user.getConstellation(),
						user.getBirthday(),
						user.getSchool(),
						user.getSchoolLocation(),
						user.getSchoolNum(),
						user.getDepartment(),
						user.getDepartmentId(),
						user.getHometown(),
						user.getProfileClip() != null ? user.getProfileClip().getThumbnailUrl(true, photoWidth, photoWidth) : "",
						user.getProfileOrign() != null ? user.getProfileOrign()
								.getThumbnailUrl(true, photoWidth, photoWidth) : "",
						String.valueOf(System.currentTimeMillis()) });
		db.close();
	}

	// 查询某用户
	public ArrayList<UserBean> queryUser(String userId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from userinfo_tb where "
				+ Constants.USERID + "=?", new String[] { userId });
		ArrayList<UserBean> list = new ArrayList<UserBean>();
		while (c.moveToNext()) {
			UserBean user = new UserBean();
			user.setUserId(c.getString(c.getColumnIndex(DbConstents.ID_MINE)));
			user.setUserType(c.getInt(c.getColumnIndex(DbConstents.USERTYPE)));
			user.setVipUser(intToBol(c.getInt(c.getColumnIndex(DbConstents.ISVIP))));
			user.setVerifyUser(intToBol(c.getInt(c
					.getColumnIndex(DbConstents.ISVERIFY))));
			user.setCompleteUserInfo(intToBol(c.getInt(c
					.getColumnIndex(DbConstents.ISCOMPLETE))));
			user.setNameNick(c.getString(c.getColumnIndex(DbConstents.NICKNAME)));
			user.setNameReal(c.getString(c.getColumnIndex(DbConstents.REALNAME)));
			user.setGender(c.getInt(c.getColumnIndex(DbConstents.GENDER)));
			user.setBirthday(Long.parseLong(c.getString(c
					.getColumnIndex(DbConstents.BIRTHDAY))));
			user.setConstellation(c.getString(c
					.getColumnIndex(DbConstents.CONSTELLATION)));
			user.setSchool(c.getString(c.getColumnIndex(DbConstents.SCHOOL)));
			user.setSchoolLocation(Long.parseLong(c.getString(c
					.getColumnIndex(DbConstents.SCHOOLLOCATION))));
			user.setSchoolNum(Integer.parseInt(c.getString(c
					.getColumnIndex(DbConstents.SCHOOLNUM))));
			user.setDepartment(c.getString(c
					.getColumnIndex(DbConstents.DEPARTMENT)));
			user.setDepartmentId(Integer.parseInt(c.getString(c
					.getColumnIndex(DbConstents.DEPARTMENTID))));
			user.setHometown(c.getString(c.getColumnIndex(DbConstents.HOMETOWN)));
			user.setProfileClip(c.getString(c
					.getColumnIndex(DbConstents.PROFILECLIP)));
			user.setProfileOrign(c.getString(c
					.getColumnIndex(DbConstents.PROFILEORIGN)));
			user.setCacheTime(c.getString(c
					.getColumnIndex(DbConstents.USER_CACHE_TIME)));
			list.add(user);
		}
		c.close();
		db.close();
		return list;
	}

	public ArrayList<UserBean> queryAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from userinfo_tb", null);
		ArrayList<UserBean> list = new ArrayList<UserBean>();
		while (c.moveToNext()) {
			UserBean user = new UserBean();
			user.setUserId(c.getString(c.getColumnIndex(DbConstents.ID_MINE)));
			user.setUserType(c.getInt(c.getColumnIndex(DbConstents.USERTYPE)));
			user.setVipUser(intToBol(c.getInt(c.getColumnIndex(DbConstents.ISVIP))));
			user.setVerifyUser(intToBol(c.getInt(c
					.getColumnIndex(DbConstents.ISVERIFY))));
			user.setCompleteUserInfo(intToBol(c.getInt(c
					.getColumnIndex(DbConstents.ISCOMPLETE))));
			user.setNameNick(c.getString(c.getColumnIndex(DbConstents.NICKNAME)));
			user.setNameReal(c.getString(c.getColumnIndex(DbConstents.REALNAME)));
			user.setGender(c.getInt(c.getColumnIndex(DbConstents.GENDER)));
			user.setBirthday(Long.parseLong(c.getString(c
					.getColumnIndex(DbConstents.BIRTHDAY))));
			user.setConstellation(c.getString(c
					.getColumnIndex(DbConstents.CONSTELLATION)));
			user.setSchool(c.getString(c.getColumnIndex(DbConstents.SCHOOL)));
			user.setSchoolLocation(Long.parseLong(c.getString(c
					.getColumnIndex(DbConstents.SCHOOLLOCATION))));
			user.setSchoolNum(Integer.parseInt(c.getString(c
					.getColumnIndex(DbConstents.SCHOOLNUM))));
			user.setDepartment(c.getString(c
					.getColumnIndex(DbConstents.DEPARTMENT)));
			user.setDepartmentId(Integer.parseInt(c.getString(c
					.getColumnIndex(DbConstents.DEPARTMENTID))));
			user.setHometown(c.getString(c.getColumnIndex(DbConstents.HOMETOWN)));
			user.setProfileClip(c.getString(c
					.getColumnIndex(DbConstents.PROFILECLIP)));
			user.setProfileOrign(c.getString(c
					.getColumnIndex(DbConstents.PROFILEORIGN)));
			list.add(user);
		}
		c.close();
		db.close();
		return list;
	}

	public boolean intToBol(int i) {
		if (i == 0) {
			return false;
		} else {
			return true;
		}
	}

	public int bolToInt(boolean b) {
		if (b) {
			return 1;
		} else {
			return 0;
		}
	}
}
