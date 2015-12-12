package com.meetu.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ChatmsgsDao {

	private MySqliteDBHelper helper;

	public ChatmsgsDao(Context context) {
		helper = new MySqliteDBHelper(context);
	}

	/**
	 * 插入数据数据
	 * 
	 * @param chatmsgs
	 *            消息实体
	 */
	public void insert(Chatmsgs chatmsgs) {
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(
				"insert into chatmsgs values(?," + "?,?,?,?,?,"
						+ "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?)",
				new Object[] { chatmsgs.getMessageCacheId(),chatmsgs.getUid(), chatmsgs.getMessageId(),
						chatmsgs.getClientId(), chatmsgs.getConversationId(),
						chatmsgs.getChatMsgType(),
						chatmsgs.getChatMsgDirection(),
						chatmsgs.getChatMsgStatus(), chatmsgs.getIsShowTime(),
						chatmsgs.getSendTimeStamp(),
						chatmsgs.getDeliveredTimeStamp(),
						chatmsgs.getContent(), chatmsgs.getImgMsgImageUrl(),
						chatmsgs.getImgMsgImageWidth(),
						chatmsgs.getImgMsgImageHeight(),
						chatmsgs.getNowJoinUserId(),
						chatmsgs.getNotificationBaseContent(),
						chatmsgs.getNotificationActyContent(),
						chatmsgs.getNotificationActyTitle(),
						chatmsgs.getNotificationActyTitleSub(),
						chatmsgs.getScriptId(), chatmsgs.getScripX(),
						chatmsgs.getScripY() });
		db.close();

	}
	/**
	 * 更新消息状态
	 * */
	public void updateStatus(String userId,String msgCacheId,int status) {
		log.d("mytest", "4------"+msgCacheId);
		SQLiteDatabase sdb = helper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from chatmsgs where "
				+ Constants.USERID + "=? and _message_cache_id=?", new String[] {
						userId, msgCacheId });
		if (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put("_chat_msg_status", status);
			int s = sdb.update("chatmsgs", values," _message_cache_id=?", new String[] {msgCacheId});
			Log.d("mytest", "s" + s);
		}
		sdb.close();
	}
	/**
	 * 删除操作
	 * 
	 * @param messageCacheId
	 *            消息的缓存id
	 */
	public void delete(String userId, String messageCacheId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "delete from chatmsgs where _message_cache_id=? and "
				+ Constants.USERID + "=?";
		db.execSQL(sql, new Object[] { messageCacheId, userId });
		db.close();

	}

	/**
	 * 
	 * 删除所有的消息信息缓存
	 */
	public void deleteAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "delete from chatmsgs ";
		db.execSQL(sql);
		db.close();

	}

	/**
	 * 删除单个对话缓存消息
	 * 
	 * @param conversationId
	 *            对话id
	 */
	public void deleteConversationId(String uid, String conversationId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "delete from chatmsgs where _conversation_id=? and "
				+ Constants.USERID + "=?";
		db.execSQL(sql, new Object[] { conversationId, uid });
		db.close();

	}

	/**
	 * 查询消息列表
	 * 
	 * @param conversationId
	 *            属于哪个对话的id,哪个用户uid
	 * @return
	 */
	public List<Chatmsgs> getChatmsgsList(String conversationId, String userId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from chatmsgs "
				+ "where _conversation_id=? and " + Constants.USERID + "=?",
				new String[] { conversationId, userId });
		List<Chatmsgs> list = new ArrayList<Chatmsgs>();
		while (c.moveToNext()) {
			Chatmsgs chatmsgs = new Chatmsgs();

			chatmsgs.setMessageCacheId(c.getString(c
					.getColumnIndex("_message_cache_id")));
			chatmsgs.setUid(c.getString(c.getColumnIndex(Constants.USERID)));
			chatmsgs.setMessageId(c.getString(c.getColumnIndex("_message_id")));
			chatmsgs.setClientId(c.getString(c.getColumnIndex("_client_id")));
			chatmsgs.setConversationId(c.getString(c
					.getColumnIndex("_conversation_id")));
			chatmsgs.setChatMsgType(c.getInt(c.getColumnIndex("_chat_msg_type")));
			chatmsgs.setChatMsgDirection(c.getInt(c
					.getColumnIndex("_chat_msg_direction")));
			chatmsgs.setChatMsgStatus(c.getInt(c
					.getColumnIndex("_chat_msg_status")));
			chatmsgs.setIsShowTime(c.getInt(c.getColumnIndex("_is_show_time")));
			chatmsgs.setSendTimeStamp(c.getString(c
					.getColumnIndex("_send_time_stamp")));
			chatmsgs.setDeliveredTimeStamp(c.getString(c
					.getColumnIndex("_delivered_time_stamp")));
			chatmsgs.setContent(c.getString(c.getColumnIndex("_content")));
			chatmsgs.setImgMsgImageUrl(c.getString(c
					.getColumnIndex("_img_msg_image_url")));
			chatmsgs.setImgMsgImageWidth(c.getInt(c
					.getColumnIndex("_img_msg_image_width")));
			chatmsgs.setImgMsgImageHeight(c.getInt(c
					.getColumnIndex("_img_msg_image_height")));
			chatmsgs.setNowJoinUserId(c.getString(c
					.getColumnIndex("_now_join_user_id")));
			chatmsgs.setNotificationBaseContent(c.getString(c
					.getColumnIndex("_notification_base_content")));
			chatmsgs.setNotificationActyContent(c.getString(c
					.getColumnIndex("_notification_acty_content")));
			chatmsgs.setNotificationActyTitle(c.getString(c
					.getColumnIndex("_notification_acty_title")));
			chatmsgs.setNotificationActyTitleSub(c.getString(c
					.getColumnIndex("_notification_acty_title_sub")));

			chatmsgs.setScriptId(c.getString(c.getColumnIndex("_scrip_id")));
			chatmsgs.setScripX(c.getInt(c.getColumnIndex("_scrip_x")));
			chatmsgs.setScripY(c.getInt(c.getColumnIndex("_scrip_y")));
			list.add(chatmsgs);
		}
		c.close();
		db.close();
		return list;

	}

	/**
	 * 查询小纸条消息列表
	 * 
	 * @param conversationId
	 *            属于哪个对话的id,哪个用户uid scriptId 小纸条id
	 * @return
	 */
	public List<Chatmsgs> getScriptChatmsgsList(String conversationId,
			String userId, String scriptId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from chatmsgs "
				+ "where _conversation_id=? and " + Constants.USERID
				+ "=? and _scrip_id=? ", new String[] { conversationId, userId,
				scriptId });
		List<Chatmsgs> list = new ArrayList<Chatmsgs>();
		while (c.moveToNext()) {
			Chatmsgs chatmsgs = new Chatmsgs();

			chatmsgs.setMessageCacheId(c.getString(c
					.getColumnIndex("_message_cache_id")));
			chatmsgs.setUid(c.getString(c.getColumnIndex(Constants.USERID)));
			chatmsgs.setMessageId(c.getString(c.getColumnIndex("_message_id")));
			chatmsgs.setClientId(c.getString(c.getColumnIndex("_client_id")));
			chatmsgs.setConversationId(c.getString(c
					.getColumnIndex("_conversation_id")));
			chatmsgs.setChatMsgType(c.getInt(c.getColumnIndex("_chat_msg_type")));
			chatmsgs.setChatMsgDirection(c.getInt(c
					.getColumnIndex("_chat_msg_direction")));
			chatmsgs.setChatMsgStatus(c.getInt(c
					.getColumnIndex("_chat_msg_status")));
			chatmsgs.setIsShowTime(c.getInt(c.getColumnIndex("_is_show_time")));
			chatmsgs.setSendTimeStamp(c.getString(c
					.getColumnIndex("_send_time_stamp")));
			chatmsgs.setDeliveredTimeStamp(c.getString(c
					.getColumnIndex("_delivered_time_stamp")));
			chatmsgs.setContent(c.getString(c.getColumnIndex("_content")));
			chatmsgs.setImgMsgImageUrl(c.getString(c
					.getColumnIndex("_img_msg_image_url")));
			chatmsgs.setImgMsgImageWidth(c.getInt(c
					.getColumnIndex("_img_msg_image_width")));
			chatmsgs.setImgMsgImageHeight(c.getInt(c
					.getColumnIndex("_img_msg_image_height")));
			chatmsgs.setNowJoinUserId(c.getString(c
					.getColumnIndex("_now_join_user_id")));
			chatmsgs.setNotificationBaseContent(c.getString(c
					.getColumnIndex("_notification_base_content")));
			chatmsgs.setNotificationActyContent(c.getString(c
					.getColumnIndex("_notification_acty_content")));
			chatmsgs.setNotificationActyTitle(c.getString(c
					.getColumnIndex("_notification_acty_title")));
			chatmsgs.setNotificationActyTitleSub(c.getString(c
					.getColumnIndex("_notification_acty_title_sub")));

			chatmsgs.setScriptId(c.getString(c.getColumnIndex("_scrip_id")));
			chatmsgs.setScripX(c.getInt(c.getColumnIndex("_scrip_x")));
			chatmsgs.setScripY(c.getInt(c.getColumnIndex("_scrip_y")));
			list.add(chatmsgs);
		}
		c.close();
		db.close();
		return list;

	}

}
