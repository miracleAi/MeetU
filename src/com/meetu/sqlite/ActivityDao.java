package com.meetu.sqlite;

import java.util.ArrayList;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.common.Constants;
import com.meetu.common.DbConstents;

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

	// 添加活动列表
	public void saveActyList(ArrayList<ActivityBean> list) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		for (int i = 0; i < list.size(); i++) {
			ActivityBean bean = list.get(i);
			ContentValues cv = new ContentValues();
			cv.put(DbConstents.ID_MINE, bean.getUserId());
			cv.put(DbConstents.ACTIVITYID, bean.getActyId());
			cv.put(DbConstents.ACTIVITYCONTENT, bean.getActivityContent());
			cv.put(DbConstents.ACTIVITYCOVER, bean.getActivityCover());
			cv.put(DbConstents.ACTIVITYFOLLOWCOUNT, bean.getOrderAndFollow());
			cv.put(DbConstents.ACTIVITYINDEX, bean.getIndex());
			cv.put(DbConstents.ISACTIVITYPRAISE, bean.getIsFavor());
			cv.put(DbConstents.TIMESTART, bean.getTimeStart());
			cv.put(DbConstents.TIMESTOP, bean.getTimeStop());
			cv.put(DbConstents.TIMECHATSTOP, bean.getTimeChatStop());
			cv.put(DbConstents.TITLE, bean.getTitle());
			cv.put(DbConstents.TITLESUB, bean.getTitleSub());
			cv.put(DbConstents.STATUS, bean.getStatus());
			cv.put(DbConstents.PRAISECOUNT, bean.getPraiseCount());
			cv.put(DbConstents.ORDERCOUNTBOY, bean.getOrderCountBoy());
			cv.put(DbConstents.ORDERCOUNTGIRL, bean.getOrderCountGirl());
			cv.put(DbConstents.LOCATIONADDRESS, bean.getLocationAddress());
			cv.put(DbConstents.LOCATIONPLACE, bean.getLocationPlace());
			cv.put(DbConstents.LOCATIONGOVERNMENT, bean.getLocationGovernment());
			cv.put(DbConstents.LOCATIONLATITUDE, bean.getLocationLatitude());
			cv.put(DbConstents.LOCATIONLONGTITUDE, bean.getLocationLongtitude());
			cv.put(DbConstents.CONVERSATIONID, bean.getConversationId());
			sdb.insert(DbConstents.ACTIVITY_CACHE_TB, null, cv);
		}
		sdb.close();
	}

	// 修改活动列表是否点赞项
	public void updateIsFavor(String userId, String activityId, int flag) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbConstents.ISACTIVITYPRAISE, flag);
		sdb.update(DbConstents.ACTIVITY_CACHE_TB, values, DbConstents.ID_MINE
				+ "=? and " + DbConstents.ACTIVITYID + "=?", new String[] {
				userId, activityId });
		sdb.close();
	}
	/**
	 * 更新本地数据库点赞数量
	 * @param userId
	 * @param activityId
	 * @param flag  
	 * @author lucifer
	 * @date 2015-12-9
	 */
	public void updateFavourNumber(String userId, String activityId, int flag){
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbConstents.PRAISECOUNT, flag);
		sdb.update(DbConstents.ACTIVITY_CACHE_TB, values, DbConstents.ID_MINE
				+ "=? and " + DbConstents.ACTIVITYID + "=?", new String[] {
				userId, activityId });
		sdb.close();
		
	}
	/**
	 * 更新活动中我关注的人数量
	 * @param userId
	 * @param activityId
	 * @param flag  
	 * @author lucifer
	 * @date 2015-12-10
	 */
	public void updateFavorUserNumber(String userId, String activityId, int flag){
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbConstents.ACTIVITYFOLLOWCOUNT, flag);
		sdb.update(DbConstents.ACTIVITY_CACHE_TB, values, DbConstents.ID_MINE
				+ "=? and " + DbConstents.ACTIVITYID + "=?", new String[] {
				userId, activityId });
		sdb.close();
		
	}
	

	// 修改活动列表关注人数项
	public void updateOrderFollow(String userId, int index, int count) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbConstents.ACTIVITYFOLLOWCOUNT, count);
		sdb.update(DbConstents.ACTIVITY_CACHE_TB, values, DbConstents.ID_MINE
				+ "=? and " + DbConstents.ACTIVITYINDEX + "=?", new String[] {
				userId, String.valueOf(index) });
		sdb.close();
	}

	// 查询活动列表
	public ArrayList<ActivityBean> queryActys(String userId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from "
				+ DbConstents.ACTIVITY_CACHE_TB + " where " + DbConstents.ID_MINE
				+ "=? order by " + DbConstents.STATUS + "," + DbConstents.TIMESTART
				+ " desc", new String[] { userId });
		cursor.moveToFirst();
		ArrayList<ActivityBean> list = new ArrayList<ActivityBean>();
		while (!cursor.isAfterLast()) {
			ActivityBean bean = new ActivityBean();
			bean.setActyId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ACTIVITYID)));
			bean.setUserId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_MINE)));
			bean.setActivityContent(cursor.getString(cursor
					.getColumnIndex(DbConstents.ACTIVITYCONTENT)));
			bean.setActivityCover(cursor.getString(cursor
					.getColumnIndex(DbConstents.ACTIVITYCOVER)));
			bean.setLocationAddress(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONADDRESS)));
			bean.setLocationPlace(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONPLACE)));
			bean.setOrderCountBoy(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ORDERCOUNTBOY)));
			bean.setOrderCountGirl(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ORDERCOUNTGIRL)));
			bean.setPraiseCount(cursor.getInt(cursor
					.getColumnIndex(DbConstents.PRAISECOUNT)));
			bean.setStatus(cursor.getInt(cursor
					.getColumnIndex(DbConstents.STATUS)));
			bean.setTimeStart(cursor.getLong(cursor
					.getColumnIndex(DbConstents.TIMESTART)));
			bean.setTitle(cursor.getString(cursor
					.getColumnIndex(DbConstents.TITLE)));
			bean.setTitleSub(cursor.getString(cursor
					.getColumnIndex(DbConstents.TITLESUB)));
			bean.setTimeStop(cursor.getLong(cursor
					.getColumnIndex(DbConstents.TIMESTOP)));
			bean.setTimeChatStop(cursor.getLong(cursor
					.getColumnIndex(DbConstents.TIMECHATSTOP)));
			bean.setLocationGovernment(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONGOVERNMENT)));
			bean.setLocationLatitude(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONLATITUDE)));
			bean.setLocationLongtitude(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONLONGTITUDE)));
			bean.setConversationId(cursor.getString(cursor
					.getColumnIndex(DbConstents.CONVERSATIONID)));
			bean.setIndex(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ACTIVITYINDEX)));
			bean.setIsFavor(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ISACTIVITYPRAISE)));
			bean.setOrderAndFollow(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ACTIVITYFOLLOWCOUNT)));
			list.add(bean);
			cursor.moveToNext();
		}
		cursor.close();
		sdb.close();
		return list;
	}

	// 查询活动
	public ArrayList<ActivityBean> queryActyBean(String userId, String actyId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from "
				+ DbConstents.ACTIVITY_CACHE_TB + " where " + DbConstents.ID_MINE
				+ "=? and " + DbConstents.ACTIVITYID + "=?", new String[] {
				userId, actyId });
		ArrayList<ActivityBean> list = new ArrayList<ActivityBean>();
		while (cursor.moveToNext()) {
			ActivityBean bean = new ActivityBean();
			bean.setActyId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ACTIVITYID)));
			bean.setUserId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_MINE)));
			bean.setActivityContent(cursor.getString(cursor
					.getColumnIndex(DbConstents.ACTIVITYCONTENT)));
			bean.setActivityCover(cursor.getString(cursor
					.getColumnIndex(DbConstents.ACTIVITYCOVER)));
			bean.setLocationAddress(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONADDRESS)));
			bean.setLocationPlace(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONPLACE)));
			bean.setOrderCountBoy(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ORDERCOUNTBOY)));
			bean.setOrderCountGirl(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ORDERCOUNTGIRL)));
			bean.setPraiseCount(cursor.getInt(cursor
					.getColumnIndex(DbConstents.PRAISECOUNT)));
			bean.setStatus(cursor.getInt(cursor
					.getColumnIndex(DbConstents.STATUS)));
			bean.setTimeStart(cursor.getLong(cursor
					.getColumnIndex(DbConstents.TIMESTART)));
			bean.setTitle(cursor.getString(cursor
					.getColumnIndex(DbConstents.TITLE)));
			bean.setTitleSub(cursor.getString(cursor
					.getColumnIndex(DbConstents.TITLESUB)));
			bean.setTimeStop(cursor.getLong(cursor
					.getColumnIndex(DbConstents.TIMESTOP)));
			bean.setTimeChatStop(cursor.getLong(cursor
					.getColumnIndex(DbConstents.TIMECHATSTOP)));
			bean.setLocationGovernment(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONGOVERNMENT)));
			bean.setLocationLatitude(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONLATITUDE)));
			bean.setLocationLongtitude(cursor.getString(cursor
					.getColumnIndex(DbConstents.LOCATIONLONGTITUDE)));
			bean.setConversationId(cursor.getString(cursor
					.getColumnIndex(DbConstents.CONVERSATIONID)));
			bean.setIndex(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ACTIVITYINDEX)));
			bean.setIsFavor(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ISACTIVITYPRAISE)));
			bean.setOrderAndFollow(cursor.getInt(cursor
					.getColumnIndex(DbConstents.ACTIVITYFOLLOWCOUNT)));
			list.add(bean);
		}
		cursor.close();
		sdb.close();
		return list;
	}

	// 根据用户清除活动缓存
	public void deleteByUser(String userId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.delete(DbConstents.ACTIVITY_CACHE_TB, DbConstents.ID_MINE + "=?",
				new String[] { userId });
		sdb.close();
	}

}
