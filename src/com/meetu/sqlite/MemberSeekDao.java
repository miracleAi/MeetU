 package com.meetu.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.bean.MemberActivityBean;
import com.meetu.bean.MemberSeekBean;
import com.meetu.common.DbConstents;
import com.meetu.common.Log;

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
	
	public MemberSeekDao(Context context){
		dbHelper=new MySqliteDBHelper(context);
		
	};
	
	/**
	 * 插入一个活动成员
	 * 
	 * @param bean
	 * @author lucifer
	 * @date 2015-12-28
	 */
	public void saveUserSeek(MemberSeekBean bean) {
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.execSQL(
				"insert or replace into member_seek_tb values("
						+ "?,?,?,?,?)",
				new Object[] { bean.getMineId(),bean.getMemberSeekId(), bean.getSeekId(),
						 bean.getConversationId(),
						bean.getConvStatus() });
		sdb.close();
	}
	/**
	 * 插入一列成员
	 * @param list  
	 * @author lucifer
	 * @date 2015-12-28
	 */
	public void saveAllUserSeek(List<MemberSeekBean> list){
		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		Log.e("list===", ""+list.size());
		for(int i=0;i<list.size();i++){
			MemberSeekBean bean=list.get(i);
			sdb.execSQL(
					"insert or replace into member_seek_tb values("
							+ "?,?,?,?,?)",
					new Object[] { bean.getMineId(), bean.getMemberSeekId(),bean.getSeekId(),
							 bean.getConversationId(),
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

		sdb.execSQL("delete from member_seek_tb where "
				+ DbConstents.ID_MINE + "=? and "
				+ DbConstents.ID_SEEK_CONVERSATION + "=?", new String[] {
				userId, conversitionId });
		sdb.close();
	}
	
	/**
	 * 删除指定对话成员
	 * @param userId
	 * @param conversitionId
	 * @param deleteUserId  
	 * @author lucifer
	 * @date 2015-12-29
	 */
	public void deleteUserTypeUserId(String userId,String conversitionId,String deleteUserId){

		SQLiteDatabase sdb = dbHelper.getWritableDatabase();
		sdb.delete(DbConstents.USERABOUT_CACHE_TB, DbConstents.ID_MINE
				+ "=?  and "+ DbConstents.ABOUTUSERID + "=?", new String[] {
				userId, deleteUserId});
		
		sdb.close();
		log.e("zcq", "删除数据库成员成功");
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
		cursor = sdb.rawQuery("select * from " + DbConstents.MEMBER_SEEK_TB
				+ " where " + DbConstents.ID_MINE + "=? and "
				+ DbConstents.ID_SEEK_CONVERSATION + "=?", new String[] {
				userId, conversitionId });
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			
			MemberSeekBean bean = new MemberSeekBean();
			
			bean.setMineId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_MINE)));
			bean.setMemberSeekId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_SEEK_MEMBER)));
			bean.setSeekId(cursor.getString(cursor
					.getColumnIndex(DbConstents.ID_SEEK)));			
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
