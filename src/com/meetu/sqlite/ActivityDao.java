package com.meetu.sqlite;

import java.util.ArrayList;

import com.meetu.bean.ActivityBean;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.common.Constants;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ActivityDao {
	private MySqliteDBHelper dbHelper;
	public ActivityDao(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new MySqliteDBHelper(context);
	}
	//添加活动列表
	public void saveActyList(ArrayList<ActivityBean> list){
		SQLiteDatabase sdb=dbHelper.getWritableDatabase();
		for(int i=0;i<list.size();i++){
			ActivityBean bean = list.get(i);
			ContentValues cv=new ContentValues();
			cv.put(Constants.USERID, bean.getUserId());
			cv.put(Constants.ACTIVITYID, bean.getActyId());
			cv.put(Constants.ACTIVITYCONTENT, bean.getActivityContent());
			cv.put(Constants.ACTIVITYCOVER, bean.getActivityCover());
			cv.put(Constants.ACTIVITYFOLLOWCOUNT, bean.getOrderAndFollow());
			cv.put(Constants.ACTIVITYINDEX, bean.getIndex());
			cv.put(Constants.ISACTIVITYPRAISE, bean.getIsFavor());
			cv.put(Constants.TIMESTART, bean.getTimeStart());
			cv.put(Constants.TIMESTOP, bean.getTimeStop());
			cv.put(Constants.TITLE, bean.getTitle());
			cv.put(Constants.TITLESUB, bean.getTitleSub());
			cv.put(Constants.STATUS, bean.getStatus());
			cv.put(Constants.PRAISECOUNT, bean.getPraiseCount());
			cv.put(Constants.ORDERCOUNTBOY, bean.getOrderCountBoy());
			cv.put(Constants.ORDERCOUNTGIRL, bean.getOrderCountGirl());
			cv.put(Constants.LOCATIONADDRESS, bean.getLocationAddress());
			cv.put(Constants.LOCATIONPLACE, bean.getLocationPlace());
			cv.put(Constants.LOCATIONGOVERNMENT, bean.getLocationGovernment());
			cv.put(Constants.CONVERSATIONID, bean.getConversationId());
			sdb.insert(Constants.ACTIVITY_CACHE_TB, null, cv);
		}
		sdb.close();
	}
	//修改活动列表是否点赞项
	public void updateIsFavor(String userId,int index,int flag){
		SQLiteDatabase sdb=dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constants.ISACTIVITYPRAISE, flag);
		sdb.update(Constants.ACTIVITY_CACHE_TB, values,Constants.USERID+"=? and "+Constants.ACTIVITYINDEX+"=?"
				,new String[]{userId,Integer.toString(index)});
		sdb.close();
	}
	//修改活动列表关注人数项
	public void updateOrderFollow(String userId,int index,int count){
		SQLiteDatabase sdb=dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Constants.ACTIVITYFOLLOWCOUNT, count);
		sdb.update(Constants.ACTIVITY_CACHE_TB, values,Constants.USERID+"=? and "+Constants.ACTIVITYINDEX+"=?"
				,new String[]{userId,String.valueOf(index)});
		sdb.close();
	}
	//查询活动列表
	public ArrayList<ActivityBean> queryActys(String userId){
		SQLiteDatabase sdb=dbHelper.getWritableDatabase();
		Cursor cursor=sdb.rawQuery("select * from "+ Constants.ACTIVITY_CACHE_TB+" where "+Constants.USERID +"=? order by "
		+Constants.STATUS+","+Constants.TIMESTART+" desc",new  String[]{userId});
		cursor.moveToFirst();
		ArrayList<ActivityBean> list = new ArrayList<ActivityBean>();
		while(!cursor.isAfterLast()){
			ActivityBean bean = new ActivityBean();
			bean.setActyId(cursor.getString(cursor.getColumnIndex(Constants.ACTIVITYID)));
			bean.setUserId(cursor.getString(cursor.getColumnIndex(Constants.USERID)));
			bean.setActivityContent(cursor.getString(cursor.getColumnIndex(Constants.ACTIVITYCONTENT)));
			bean.setActivityCover(cursor.getString(cursor.getColumnIndex(Constants.ACTIVITYCOVER)));
			bean.setLocationAddress(cursor.getString(cursor.getColumnIndex(Constants.LOCATIONADDRESS)));
			bean.setLocationPlace(cursor.getString(cursor.getColumnIndex(Constants.LOCATIONPLACE)));
			bean.setOrderCountBoy(cursor.getInt(cursor.getColumnIndex(Constants.ORDERCOUNTBOY)));
			bean.setOrderCountGirl(cursor.getInt(cursor.getColumnIndex(Constants.ORDERCOUNTGIRL)));
			bean.setPraiseCount(cursor.getInt(cursor.getColumnIndex(Constants.PRAISECOUNT)));
			bean.setStatus(cursor.getInt(cursor.getColumnIndex(Constants.STATUS)));
			bean.setTimeStart(cursor.getLong(cursor.getColumnIndex(Constants.TIMESTART)));
			bean.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE)));
			bean.setTitleSub(cursor.getString(cursor.getColumnIndex(Constants.TITLESUB)));
			bean.setTimeStop(cursor.getLong(cursor.getColumnIndex(Constants.TIMESTOP)));
			bean.setLocationGovernment(cursor.getString(cursor.getColumnIndex(Constants.LOCATIONGOVERNMENT)));
			bean.setConversationId(cursor.getString(cursor.getColumnIndex(Constants.CONVERSATIONID)));
			bean.setIndex(cursor.getInt(cursor.getColumnIndex(Constants.ACTIVITYINDEX)));
			bean.setIsFavor(cursor.getInt(cursor.getColumnIndex(Constants.ISACTIVITYPRAISE)));
			bean.setOrderAndFollow(cursor.getInt(cursor.getColumnIndex(Constants.ACTIVITYFOLLOWCOUNT)));
			list.add(bean);
			cursor.moveToNext();
		}
		cursor.close();
		sdb.close();
		return list;
	}
	//根据用户清除活动缓存
	public void deleteByUser(String userId){
		SQLiteDatabase sdb=dbHelper.getWritableDatabase();
		sdb.delete(Constants.ACTIVITY_CACHE_TB, Constants.USERID+"=?", new String[]{userId});
		sdb.close();
	}
	
}
