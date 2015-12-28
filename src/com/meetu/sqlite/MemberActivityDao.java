package com.meetu.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.meetu.bean.MemberActivityBean;
import com.meetu.bean.UserAboutBean;
import com.meetu.common.DbConstents;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author lucifer
 * @date 2015-12-28
 * @return
 */
public class MemberActivityDao {
	private MySqliteDBHelper dbHelper;

	public MemberActivityDao(Context context) {
		dbHelper = new MySqliteDBHelper(context);
	}

	/**
	 * 插入一个活动成员
	 * 
	 * @param bean
	 * @author lucifer
	 * @date 2015-12-28
	 */
	public void saveUserActivity(MemberActivityBean bean) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.execSQL(
				"insert or replace into member_activity_tb values("
						+ "?,?,?,?,?)",
				new Object[] { bean.getMineId(), bean.getActivityId(),
						bean.getMemberId(), bean.getConversationId(),
						bean.getConvStatus() });
		sdb.close();
	}
	
/**
 * 插入一列成员
 * @param list  
 * @author lucifer
 * @date 2015-12-28
 */
	public void saveAllUserActivity(List<MemberActivityBean> list) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		
		for (int i = 0; i < list.size(); i++) {			
			MemberActivityBean bean = list.get(i);
			sdb.execSQL(
					"insert or replace into member_activity_tb values("
							+ "?,?,?,?,?)",
					new Object[] { bean.getMineId(), bean.getActivityId(),bean.getMemberId(), bean.getConversationId(),
							bean.getConvStatus() });
		}
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

		sdb.execSQL("delete from member_activity_tb where "
				+ DbConstents.ID_MINE + "=? and "
				+ DbConstents.ID_ACTY_CONVERSATION + "=?", new String[] {
				userId, conversitionId });
		sdb.close();
	}

	/**
	 * 查询指定集合
	 * 
	 * @param userId
	 * @param conversitionId
	 * @return
	 * @author lucifer
	 * @date 2015-12-28
	 */
	public ArrayList<MemberActivityBean> queryUserAbout(String userId,
			String conversitionId) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		ArrayList<MemberActivityBean> aboutList = new ArrayList<MemberActivityBean>();
		Cursor cursor = null;
		// 根据类型查询指定类型所有
		cursor = sdb.rawQuery("select * from " + DbConstents.MEMBER_ACTY_TB
				+ " where " + DbConstents.ID_MINE + "=? and "
				+ DbConstents.ID_ACTY_CONVERSATION + "=?", new String[] {
				userId, conversitionId });
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MemberActivityBean bean = new MemberActivityBean();
			bean.setMineId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_MINE)));
			bean.setActivityId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_MINE_MEMBER_ACTY)));
			bean.setMemberId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_ACTY_MEMBER)));
			bean.setConversationId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_ACTY_CONVERSATION)));
			bean.setConvStatus(cursor.getString(cursor
					.getColumnIndex(DbConstents.STATUS_ACTY_CONV)));

			aboutList.add(bean);
			cursor.moveToNext();
		}
		sdb.close();

		return aboutList;
	}

}
