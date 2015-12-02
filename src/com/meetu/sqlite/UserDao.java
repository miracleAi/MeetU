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

public class UserDao {
	private  MySqliteDBHelper helper;
	public UserDao(Context context){
		helper=new MySqliteDBHelper(context);
	}
	//插入或替换用户
	public  void insertOrReplaceUser(ObjUser user){
		SQLiteDatabase db=helper.getReadableDatabase();
		db.execSQL("insert or replace into userinfo_tb values(" +
				"?,?,?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?,?,?,?)", 
				new Object[]{user.getObjectId(),
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
				user.getProfileClip()!= null?user.getProfileClip().getUrl():"",
				user.getProfileOrign()!=null?user.getProfileOrign().getUrl():"",
				String.valueOf(System.currentTimeMillis())});
		db.close();
	}
	//查询某用户
	public ArrayList<UserBean> queryUser(String userId){
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor c=db.rawQuery("select * from userinfo_tb where "+Constants.USERID +"=?", new String[]{userId});
		ArrayList<UserBean> list = new ArrayList<UserBean>();
		while (c.moveToNext()) {
			UserBean user = new UserBean();
			user.setUserId(c.getString(c.getColumnIndex(Constants.USERID)));
			user.setUserType(c.getInt(c.getColumnIndex(Constants.USERTYPE)));
			user.setVipUser(intToBol(c.getInt(c.getColumnIndex(Constants.ISVIP))));
			user.setVerifyUser(intToBol(c.getInt(c.getColumnIndex(Constants.ISVERIFY))));
			user.setCompleteUserInfo(intToBol(c.getInt(c.getColumnIndex(Constants.ISCOMPLETE))));
			user.setNameNick(c.getString(c.getColumnIndex(Constants.NICKNAME)));
			user.setNameReal(c.getString(c.getColumnIndex(Constants.REALNAME)));
			user.setGender(c.getInt(c.getColumnIndex(Constants.GENDER)));
			user.setBirthday(Long.parseLong(c.getString(c.getColumnIndex(Constants.BIRTHDAY))));
			user.setConstellation(c.getString(c.getColumnIndex(Constants.CONSTELLATION)));
			user.setSchool(c.getString(c.getColumnIndex(Constants.SCHOOL)));
			user.setSchoolLocation(Long.parseLong(c.getString(c.getColumnIndex(Constants.SCHOOLLOCATION))));
			user.setSchoolNum(Integer.parseInt(c.getString(c.getColumnIndex(Constants.SCHOOLNUM))));
			user.setDepartment(c.getString(c.getColumnIndex(Constants.DEPARTMENT)));
			user.setDepartmentId(Integer.parseInt(c.getString(c.getColumnIndex(Constants.DEPARTMENTID))));
			user.setHometown(c.getString(c.getColumnIndex(Constants.HOMETOWN)));
			user.setProfileClip(c.getString(c.getColumnIndex(Constants.PROFILECLIP)));
			user.setProfileOrign(c.getString(c.getColumnIndex(Constants.PROFILEORIGN)));
			user.setCacheTime(c.getString(c.getColumnIndex(Constants.USER_CACHE_TIME)));
			list.add(user);
		}
		c.close();
		db.close();
		return list;
	}
	public ArrayList<UserBean> queryAll(){
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor c=db.rawQuery("select * from userinfo_tb",null);
		ArrayList<UserBean> list = new ArrayList<UserBean>();
		while (c.moveToNext()) {
			UserBean user = new UserBean();
			user.setUserId(c.getString(c.getColumnIndex(Constants.USERID)));
			user.setUserType(c.getInt(c.getColumnIndex(Constants.USERTYPE)));
			user.setVipUser(intToBol(c.getInt(c.getColumnIndex(Constants.ISVIP))));
			user.setVerifyUser(intToBol(c.getInt(c.getColumnIndex(Constants.ISVERIFY))));
			user.setCompleteUserInfo(intToBol(c.getInt(c.getColumnIndex(Constants.ISCOMPLETE))));
			user.setNameNick(c.getString(c.getColumnIndex(Constants.NICKNAME)));
			user.setNameReal(c.getString(c.getColumnIndex(Constants.REALNAME)));
			user.setGender(c.getInt(c.getColumnIndex(Constants.GENDER)));
			user.setBirthday(Long.parseLong(c.getString(c.getColumnIndex(Constants.BIRTHDAY))));
			user.setConstellation(c.getString(c.getColumnIndex(Constants.CONSTELLATION)));
			user.setSchool(c.getString(c.getColumnIndex(Constants.SCHOOL)));
			user.setSchoolLocation(Long.parseLong(c.getString(c.getColumnIndex(Constants.SCHOOLLOCATION))));
			user.setSchoolNum(Integer.parseInt(c.getString(c.getColumnIndex(Constants.SCHOOLNUM))));
			user.setDepartment(c.getString(c.getColumnIndex(Constants.DEPARTMENT)));
			user.setDepartmentId(Integer.parseInt(c.getString(c.getColumnIndex(Constants.DEPARTMENTID))));
			user.setHometown(c.getString(c.getColumnIndex(Constants.HOMETOWN)));
			user.setProfileClip(c.getString(c.getColumnIndex(Constants.PROFILECLIP)));
			user.setProfileOrign(c.getString(c.getColumnIndex(Constants.PROFILEORIGN)));
			list.add(user);
		}
		c.close();
		db.close();
		return list;
	}
	public boolean intToBol(int i){
		if( i == 0){
			return false;
		}else{
			return true;
		}
	}
	public int bolToInt(boolean b){
		if(b){
			return 1;
		}else{
			return 0;
		}
	}
}
