package com.meetu.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;
import com.meetu.entity.Messages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MessagesDao {
	private MySqliteDBHelper helper;

	public MessagesDao(Context context) {
		helper = new MySqliteDBHelper(context);
	}

	/**
	 * 插入或替换数据
	 * 
	 * @param messages
	 *            消息表实体 Bean
	 */
	public void insert(Messages messages) {
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(
				"insert or replace into messages values(" + "?,?,?,?,?,"
						+ "?,?,?,?,?,?)",
				new Object[] { messages.getUserId(),
						messages.getConversationID(),
						messages.getConversationType(), messages.getTiStatus(),
						messages.getCreatorID(), messages.getTimeOver(),
						messages.getActyId(), messages.getActyName(),
						messages.getChatId(), messages.getChatName(),
						messages.getUnreadMsgCount() });
		db.close();
	}

	/**
	 * 插入列表,存在--修改 不存在--插入
	 * */
	public void insertList(ArrayList<Messages> list) {
		SQLiteDatabase db = helper.getReadableDatabase();
		for (int i = 0; i < list.size(); i++) {
			Messages messages = list.get(i);
			Cursor c = db.rawQuery(
					"select * from messages where " + Constants.USERID
							+ "=? and _conversation_id=?",
					new String[] { messages.getUserId(),
							messages.getConversationID() });
			if (c.moveToNext()) {
				Messages msg = new Messages();
				messages.setUnreadMsgCount(c.getInt(c
						.getColumnIndex("_unread_count")));
			}
			c.close();
			db.execSQL(
					"insert or replace into messages values(" + "?,?,?,?,?,"
							+ "?,?,?,?,?,?)",
					new Object[] { messages.getUserId(),
							messages.getConversationID(),
							messages.getConversationType(),
							messages.getTiStatus(), messages.getCreatorID(),
							messages.getTimeOver(), messages.getActyId(),
							messages.getActyName(), messages.getChatId(),
							messages.getChatName(),
							messages.getUnreadMsgCount() });
		}
		db.close();
	}

	/**
	 * 修改未读条数 +1
	 * */
	public void updateUnread(String userId, String convId) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from messages where "
				+ Constants.USERID + "=? and _conversation_id=?", new String[] {
				userId, convId });
		if (cursor.moveToNext()) {
			int count = cursor.getInt(cursor.getColumnIndex("_unread_count")) + 1;
			ContentValues values = new ContentValues();
			values.put("_unread_count", count);
			Log.d("mytest", "" + count);
			int s = sdb.update("messages", values, Constants.USERID
					+ "=? and _conversation_id=?", new String[] { userId,
					convId });
			Log.d("mytest", "s" + s);
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
		Cursor cursor = sdb.rawQuery("select * from messages where "
				+ Constants.USERID + "=? and _conversation_id=?", new String[] {
				userId, convId });
		if (cursor.moveToNext()) {
			int count = 0;
			ContentValues values = new ContentValues();
			values.put("_unread_count", count);
			Log.d("mytest", "" + count);
			int s = sdb.update("messages", values, Constants.USERID
					+ "=? and _conversation_id=?", new String[] { userId,
					convId });
			Log.d("mytest", "s" + s);
		}
		sdb.close();
	}

	/**
	 * 修改所有状态改为已剔除(通过替换，得出被踢出和失效的会话，查看一次后删除)
	 * */
	public void updeteStatus(String userId) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("_ti_status", 1);
		int s = sdb.update("messages", values, Constants.USERID + "=?",
				new String[] { userId });
		sdb.close();
	}

	/**
	 * 修改所有单个状态改为已剔除(修改会话为已踢出状态，查看一次后删除)
	 * */
	public void updeteSingleStatus(String userId, String convId) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("_ti_status", 1);
		int s = sdb.update("messages", values, Constants.USERID
				+ "=? and _conversation_id=?", new String[] { userId, convId });
		sdb.close();
	}

	/**
	 * 根据用户删除所有数据
	 * */
	public void deleteAll(String uid) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "delete from messages where " + Constants.USERID + "="
				+ uid;
		db.execSQL(sql);
		db.close();
	}

	/**
	 * 删除会话
	 * */
	public void deleteConv(String userId, String convId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		// String sql="delete from messages where "+Constants.USERID
		// +"="+userId+" and _conversation_id="+convId;
		String sql = "delete from messages where " + Constants.USERID + "=?"
				+ " and _conversation_id= ?";
		db.execSQL(sql, new Object[] { userId, convId });
		db.close();
	}

	/**
	 * *获取列表数据
	 * 
	 * @param uid
	 * @return
	 */
	public ArrayList<Messages> getMessages(String uid) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from messages where "
				+ Constants.USERID
				+ "=? and _conversation_type=1 or _conversation_type=2",
				new String[] { uid });
		ArrayList<Messages> list = new ArrayList<Messages>();
		while (c.moveToNext()) {
			Messages messages = new Messages();

			messages.setConversationID(c.getString(c
					.getColumnIndex("_conversation_id")));
			messages.setConversationType(c.getInt(c
					.getColumnIndex("_conversation_type")));
			messages.setCreatorID(c.getString(c.getColumnIndex("_creator_id")));
			messages.setTimeOver(c.getLong(c.getColumnIndex("_time_over")));
			messages.setActyId(c.getString(c.getColumnIndex("_acty_id")));
			messages.setActyName(c.getString(c.getColumnIndex("_acty_name")));
			messages.setChatId(c.getString(c.getColumnIndex("_chat_id")));
			messages.setChatName(c.getString(c.getColumnIndex("_chat_name")));
			messages.setUserId(c.getString(c.getColumnIndex(Constants.USERID)));
			messages.setTiStatus(c.getInt(c.getColumnIndex("_ti_status")));
			messages.setUnreadMsgCount(c.getInt(c
					.getColumnIndex("_unread_count")));
			list.add(messages);
		}
		c.close();
		db.close();

		return list;
	}

	/**
	 * *获取纸条箱列表数据
	 * 
	 * @param uid
	 * @return
	 */
	public ArrayList<Messages> getScripBox(String uid) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from messages where "
				+ Constants.USERID + "=? and _conversation_type=3",
				new String[] { uid });
		ArrayList<Messages> list = new ArrayList<Messages>();
		while (c.moveToNext()) {
			Messages messages = new Messages();

			messages.setConversationID(c.getString(c
					.getColumnIndex("_conversation_id")));
			messages.setConversationType(c.getInt(c
					.getColumnIndex("_conversation_type")));
			messages.setCreatorID(c.getString(c.getColumnIndex("_creator_id")));
			messages.setTimeOver(c.getLong(c.getColumnIndex("_time_over")));
			messages.setActyId(c.getString(c.getColumnIndex("_acty_id")));
			messages.setActyName(c.getString(c.getColumnIndex("_acty_name")));
			messages.setChatId(c.getString(c.getColumnIndex("_chat_id")));
			messages.setChatName(c.getString(c.getColumnIndex("_chat_name")));
			messages.setUserId(c.getString(c.getColumnIndex(Constants.USERID)));
			messages.setTiStatus(c.getInt(c.getColumnIndex("_ti_status")));
			messages.setUnreadMsgCount(c.getInt(c
					.getColumnIndex("_unread_count")));
			list.add(messages);
		}
		c.close();
		db.close();

		return list;
	}

	/**
	 * *查询单个会话是否存在
	 * 
	 * @param uid
	 * @param convid
	 * @return
	 */
	public ArrayList<Messages> getMessage(String uid, String convid) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from messages where "
				+ Constants.USERID + "=? and _conversation_id=?", new String[] {
				uid, convid });
		ArrayList<Messages> list = new ArrayList<Messages>();
		while (c != null && c.moveToNext()) {
			Messages messages = new Messages();
			messages.setConversationID(c.getString(c
					.getColumnIndex("_conversation_id")));
			messages.setConversationType(c.getInt(c
					.getColumnIndex("_conversation_type")));
			messages.setCreatorID(c.getString(c.getColumnIndex("_creator_id")));
			messages.setTimeOver(c.getLong(c.getColumnIndex("_time_over")));
			messages.setActyId(c.getString(c.getColumnIndex("_acty_id")));
			messages.setActyName(c.getString(c.getColumnIndex("_acty_name")));
			messages.setChatId(c.getString(c.getColumnIndex("_chat_id")));
			messages.setChatName(c.getString(c.getColumnIndex("_chat_name")));
			messages.setUserId(c.getString(c.getColumnIndex(Constants.USERID)));
			messages.setTiStatus(c.getInt(c.getColumnIndex("_ti_status")));
			messages.setUnreadMsgCount(c.getInt(c
					.getColumnIndex("_unread_count")));
			list.add(messages);
		}
		c.close();
		db.close();
		return list;
	}
}
