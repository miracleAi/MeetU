 package com.meetu.sqlite;

import java.util.ArrayList;

import com.avos.avoscloud.AVCloud;
import com.meetu.bean.MemberActivityBean;
import com.meetu.bean.MemberSeekBean;
import com.meetu.common.DbConstents;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/** 
 * @author  lucifer 
 * @date 2015-12-28
 * @return  
 */
public class MemberSeekDao {
	private MySqliteDBHelper dbHelper;
	
	public void MemberSeekDao(Context context){
		dbHelper=new MySqliteDBHelper(context);
		
	};
	
	/**
	 * 插入一个活动成员
	 * 
	 * @param bean
	 * @author lucifer
	 * @date 2015-12-28
	 */
	public void saveUserActivity(MemberSeekBean bean) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.execSQL(
				"insert or replace into member_seek_tb values("
						+ "?,?,?,?,?)",
				new Object[] { bean.getMineId(), bean.getSeekId(),
						bean.getMemberSeekId(), bean.getConversationId(),
						bean.getConvStatus() });
		sdb.close();
	}

	/**
	 * 删除指定缓存
	 * 
	 * @param userId
	 * @param conversitionId
	 * @author lucifer
	 * @date 2015-12-28
	 */
	public void deleteByConv(String userId, String conversitionId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();

		sdb.execSQL("deleteb from member_activity_tb where "
				+ DbConstents.ID_MINE + "=? and"
				+ DbConstents.ID_SEEK_CONVERSATION + "=?", new String[] {
				userId, conversitionId });
		sdb.close();
	}

	/**
	 * 查询指定集合
	 * @param userId
	 * @param conversitionId
	 * @return  
	 * @author lucifer
	 * @date 2015-12-28
	 */
	public ArrayList<MemberSeekBean> queryUserAbout(String userId,
			String conversitionId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		ArrayList<MemberSeekBean> aboutList = new ArrayList<MemberSeekBean>();
		Cursor cursor = null;
		// 根据类型查询指定类型所有
		cursor = sdb.rawQuery("select * from " + DbConstents.MEMBER_ACTY_TB
				+ " where " + DbConstents.ID_MINE + "=? and "
				+ DbConstents.ID_ACTY_CONVERSATION + "=?", new String[] {
				userId, conversitionId });
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MemberSeekBean bean = new MemberSeekBean();
			
			bean.setMineId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_MINE)));
			bean.setSeekId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_SEEK)));
			bean.setMemberSeekId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_SEEK_MEMBER)));
			bean.setConversationId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_SEEK_CONVERSATION)));
			bean.setConvStatus(cursor.getString(cursor
					.getColumnIndex(DbConstents.STATUS_SEEK_CONV)));

			aboutList.add(bean);
			cursor.moveToNext();
		}
		sdb.close();

		return aboutList;
	}
	

}
