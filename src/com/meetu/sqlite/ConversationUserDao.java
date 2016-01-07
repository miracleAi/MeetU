package com.meetu.sqlite;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.meetu.bean.CoversationUserBean;
import com.meetu.common.Constants;
import com.meetu.common.DbConstents;
import com.meetu.entity.Messages;

public class ConversationUserDao {
	private MySqliteDBHelper helper;
	public ConversationUserDao(Context context) {
		helper = new MySqliteDBHelper(context);
	}
	/**
	 * 修改未读条数 +1,更新时间
	 * */
	public void updateUnread(String userId, String convId) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from "+DbConstents.CONVERSATION_USER_TB+" where "
				+ DbConstents.ID_MINE + "=? and "+DbConstents.ID_CONVERSATION+"=?", new String[] {
						userId, convId });
		if (cursor.moveToNext()) {
			int count = cursor.getInt(cursor.getColumnIndex(DbConstents.UNREAD_COUNT)) + 1;
			ContentValues values = new ContentValues();
			values.put(DbConstents.UNREAD_COUNT, count);
			int s = sdb.update(DbConstents.CONVERSATION_USER_TB, values, DbConstents.ID_MINE
					+ "=? and "+DbConstents.ID_CONVERSATION+"=?", new String[] { userId,
					convId });
		}
		sdb.close();
	}
	/**
	 * 修改未读条数 清0
	 * 
	 * @param userId
	 * @param convId
	 * @author lucifer
	 * @date 2015-11-20
	 */
	public void updateUnreadClear(String userId, String convId) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from "+DbConstents.CONVERSATION_USER_TB+" where "
				+ DbConstents.ID_MINE + "=? and "+DbConstents.ID_CONVERSATION+"=?", new String[] {
						userId, convId });
		if (cursor.moveToNext()) {
			int count = 0;
			ContentValues values = new ContentValues();
			values.put(DbConstents.UNREAD_COUNT, count);
			int s = sdb.update(DbConstents.CONVERSATION_USER_TB, values, DbConstents.ID_MINE
					+ "=? and "+DbConstents.ID_CONVERSATION+"=?", new String[] { userId,
					convId });
		}
		sdb.close();
	}
	/**
	 * 修改未读条数 清0
	 * 
	 * @param userId
	 * @param convId
	 * @author lucifer
	 * @date 2015-11-20
	 */
	public void updateConvStatus(String userId, String convId,int status) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from "+DbConstents.CONVERSATION_USER_TB+" where "
				+ DbConstents.ID_MINE + "=? and "+DbConstents.ID_CONVERSATION+"=?", new String[] {
						userId, convId });
		if (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put(DbConstents.STATUS_CONV, status);
			int s = sdb.update(DbConstents.CONVERSATION_USER_TB, values, DbConstents.ID_MINE
					+ "=? and "+DbConstents.ID_CONVERSATION+"=?", new String[] { userId,
					convId });
		}
		sdb.close();
	}

	/**
	 * 更新时间
	 * */
	public void updateTime(String userId, String convId) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from "+DbConstents.CONVERSATION_USER_TB+" where "
				+ DbConstents.ID_MINE + "=? and "+DbConstents.ID_MSG_CONVERSATION+"=?", new String[] {
						userId, convId });
		if (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put(DbConstents.CONV_UPDATE_TIME, System.currentTimeMillis());
			int s = sdb.update(DbConstents.CONVERSATION_USER_TB, values, DbConstents.ID_MINE
					+ "=? and "+DbConstents.ID_MSG_CONVERSATION+"=?", new String[] { userId,
					convId });
		}
		sdb.close();
	}
	/**
	 * *获取列表数据
	 * 
	 * @param uid
	 * @return
	 */
	public ArrayList<CoversationUserBean> getMessages(String uid) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DbConstents.CONVERSATION_USER_TB+" where "
				+ DbConstents.ID_MINE + "=? order by "+DbConstents.CONV_UPDATE_TIME+" desc", new String[] {
						uid });
		ArrayList<CoversationUserBean> list = new ArrayList<CoversationUserBean>();
		while (c != null && c.moveToNext()) {
			CoversationUserBean messages = new CoversationUserBean();
			messages.setIdMine(c.getString(c.getColumnIndex(DbConstents.ID_MINE)));
			messages.setIdConversation(c.getString(c.getColumnIndex(DbConstents.ID_CONVERSATION)));
			messages.setIdConvCreator(c.getString(c.getColumnIndex(DbConstents.ID_CONV_CREATOR)));
			messages.setIdConvAppend(c.getString(c.getColumnIndex(DbConstents.ID_CONV_APPEND)));
			messages.setTitle(c.getString(c.getColumnIndex(DbConstents.TITLE_CONV)));
			messages.setStatus(c.getInt(c.getColumnIndex(DbConstents.STATUS_CONV)));
			messages.setType(c.getInt(c.getColumnIndex(DbConstents.TYPE_CONV)));
			messages.setUnReadCount(c.getInt(c.getColumnIndex(DbConstents.UNREAD_COUNT)));
			messages.setOverTime(c.getLong(c.getColumnIndex(DbConstents.CONV_OVER_TIME)));
			messages.setUpdateTime(c.getLong(c.getColumnIndex(DbConstents.CONV_UPDATE_TIME)));
			messages.setMute(c.getInt(c.getColumnIndex(DbConstents.CONV_MUTE)));
			list.add(messages);
		}
		c.close();
		db.close();

		return list;
	}
	/**
	 * *查询单个会话
	 * 
	 * @param uid
	 * @param convid
	 * @return
	 */
	public ArrayList<CoversationUserBean> getMessage(String uid, String convid) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DbConstents.CONVERSATION_USER_TB+" where "
				+ DbConstents.ID_MINE + "=? and "+DbConstents.ID_CONVERSATION+"=?", new String[] {
						uid, convid });
		ArrayList<CoversationUserBean> list = new ArrayList<CoversationUserBean>();
		while (c != null && c.moveToNext()) {
			CoversationUserBean messages = new CoversationUserBean();
			messages.setIdMine(c.getString(c.getColumnIndex(DbConstents.ID_MINE)));
			messages.setIdConversation(c.getString(c.getColumnIndex(DbConstents.ID_CONVERSATION)));
			messages.setIdConvCreator(c.getString(c.getColumnIndex(DbConstents.ID_CONV_CREATOR)));
			messages.setIdConvAppend(c.getString(c.getColumnIndex(DbConstents.ID_CONV_APPEND)));
			messages.setTitle(c.getString(c.getColumnIndex(DbConstents.TITLE_CONV)));
			messages.setStatus(c.getInt(c.getColumnIndex(DbConstents.STATUS_CONV)));
			messages.setType(c.getInt(c.getColumnIndex(DbConstents.TYPE_CONV)));
			messages.setUnReadCount(c.getInt(c.getColumnIndex(DbConstents.UNREAD_COUNT)));
			messages.setOverTime(c.getLong(c.getColumnIndex(DbConstents.CONV_OVER_TIME)));
			messages.setUpdateTime(c.getLong(c.getColumnIndex(DbConstents.CONV_UPDATE_TIME)));
			messages.setMute(c.getInt(c.getColumnIndex(DbConstents.CONV_MUTE)));
			list.add(messages);
		}
		c.close();
		db.close();
		return list;
	}
	/**
	 * 删除会话
	 * */
	public void deleteConv(String userId, String convId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "delete from "+DbConstents.CONVERSATION_USER_TB+" where " + DbConstents.ID_MINE + "=?"
				+ " and "+DbConstents.ID_CONVERSATION+"=?";
		db.execSQL(sql, new Object[] { userId, convId });
		db.close();
	}
	/**
	 * 删除会话
	 * */
	public void deleteAllConv(String userId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "delete from "+DbConstents.CONVERSATION_USER_TB+" where " + DbConstents.ID_MINE + "=?";
		db.execSQL(sql, new Object[] { userId});
		db.close();
	}
	/**
	 * 插入或替换数据
	 * 
	 * @param messages
	 *            消息表实体 Bean
	 */
	public void insert(CoversationUserBean messages) {
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(
				"insert or replace into "+DbConstents.CONVERSATION_USER_TB+" values("+"?,?,?,?,?,"
						+ "?,?,?,?,?,?)",
						new Object[] { messages.getIdMine(),
						messages.getIdConversation(),
						messages.getIdConvAppend(), messages.getIdConvCreator(),
						messages.getStatus(), messages.getType(),
						messages.getMute(), messages.getTitle(),
						messages.getOverTime(), messages.getUpdateTime(),
						messages.getUnReadCount()});
		db.close();
	}
	/**
	 * 插入列表,存在--修改 不存在--插入
	 * */
	public void insertList(ArrayList<CoversationUserBean> list) {
		SQLiteDatabase db = helper.getReadableDatabase();
		for (int i = 0; i < list.size(); i++) {
			CoversationUserBean messages = list.get(i);
			Cursor c = db.rawQuery(
					"select * from "+DbConstents.CONVERSATION_USER_TB+" where " + DbConstents.ID_MINE
					+ "=? and "+DbConstents.ID_CONVERSATION+"=?",
					new String[] { messages.getIdMine(),
							messages.getIdConversation() });
			if (c.moveToNext()) {
				CoversationUserBean msg = new CoversationUserBean();
				messages.setUnReadCount(c.getInt(c
						.getColumnIndex(DbConstents.UNREAD_COUNT)));
			}
			c.close();
			db.execSQL(
					"insert or replace into "+DbConstents.CONVERSATION_USER_TB+" values("+"?,?,?,?,?,"
							+ "?,?,?,?,?,?)",
							new Object[] {messages.getIdMine(),
							messages.getIdConversation(),
							messages.getIdConvAppend(), messages.getIdConvCreator(),
							messages.getStatus(), messages.getType(),
							messages.getMute(), messages.getTitle(),
							messages.getOverTime(), messages.getUpdateTime(),
							messages.getUnReadCount()});
		}
		db.close();
	}
}
