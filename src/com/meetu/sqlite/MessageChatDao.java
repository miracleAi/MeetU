package com.meetu.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.meetu.bean.MessageChatBean;
import com.meetu.common.Constants;
import com.meetu.common.DbConstents;
import com.meetu.entity.Chatmsgs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MessageChatDao {
	private MySqliteDBHelper helper;
	public MessageChatDao(Context context) {
		helper = new MySqliteDBHelper(context);
	}
	/**
	 * 插入数据数据
	 * 
	 * @param chatmsgs
	 *            消息实体
	 */
	public void insert(MessageChatBean msgBean) {
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(
				"insert into "+DbConstents.MSG_CHAT_TB +" values(" + "?,?,?,?,?,?,"
						+ "?,?,?,?,?," + "?,?,?,?)",
						new Object[] { msgBean.getIdMine()+msgBean.getIdMessage(),msgBean.getIdMine(),msgBean.getIdMessage(), msgBean.getIdClient(),
						msgBean.getSendTimeStamp(), msgBean.getMsgText(),
						msgBean.getFileUrl(),
						msgBean.getImgHeight(),
						msgBean.getImgWidth(),
						msgBean.getIdConversation(),
						msgBean.getIdOperated(),
						msgBean.getTypeMsg(),
						msgBean.getStatusMsg(), 
						msgBean.getDirectionMsg(),
						msgBean.getIsShowTime()});
		db.close();

	}
	/**
	 * 查询消息列表
	 * 
	 * @param conversationId
	 *            属于哪个对话的id,哪个用户uid
	 * @return
	 */
	public List<MessageChatBean> getChatmsgsList(String conversationId, String userId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DbConstents.MSG_CHAT_TB
				+ " where "+DbConstents.ID_MSG_CONVERSATION+"=? and " + DbConstents.ID_MINE + "=?",
				new String[] { conversationId, userId });
		List<MessageChatBean> list = new ArrayList<MessageChatBean>();
		while (c.moveToNext()) {
			MessageChatBean chatmsgs = new MessageChatBean();

			chatmsgs.setIdMine(c.getString(c.getColumnIndex(DbConstents.ID_MINE)));
			chatmsgs.setIdMessage(c.getString(c.getColumnIndex(DbConstents.ID_MESSAGE)));
			chatmsgs.setIdClient(c.getString(c.getColumnIndex(DbConstents.ID_CLIENT)));
			chatmsgs.setIdConversation(c.getString(c
					.getColumnIndex(DbConstents.ID_MSG_CONVERSATION)));
			chatmsgs.setTypeMsg(c.getInt(c.getColumnIndex(DbConstents.TYPE_MSG)));
			chatmsgs.setDirectionMsg(c.getInt(c
					.getColumnIndex(DbConstents.DIRECTION_MSG)));
			chatmsgs.setStatusMsg(c.getInt(c
					.getColumnIndex(DbConstents.STATUS_MSG)));
			chatmsgs.setIsShowTime(c.getInt(c.getColumnIndex(DbConstents.IS_SHOW_TIME)));
			chatmsgs.setSendTimeStamp(c.getLong(c
					.getColumnIndex(DbConstents.SEND_TIME_STAMP)));
			chatmsgs.setMsgText(c.getString(c.getColumnIndex(DbConstents.MSG_TEXT)));
			chatmsgs.setFileUrl(c.getString(c
					.getColumnIndex(DbConstents.MSG_FILE_URL)));
			chatmsgs.setImgHeight(c.getInt(c
					.getColumnIndex(DbConstents.MSG_IMG_HEIGH)));
			chatmsgs.setImgWidth(c.getInt(c
					.getColumnIndex(DbConstents.MSG_IMG_WIDTH)));
			chatmsgs.setIdOperated(c.getString(c
					.getColumnIndex(DbConstents.ID_OPERATED)));
			chatmsgs.setIdCacheMsg(c.getString(c
					.getColumnIndex(DbConstents.ID_CACHE_MSG)));
			list.add(chatmsgs);
		}
		c.close();
		db.close();
		return list;
	}
	/**
	 * 删除操作
	 * 
	 * @param messageCacheId
	 *            消息的缓存id
	 */
	public void delete(String userId, String messageCacheId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "delete from "+DbConstents.MSG_CHAT_TB+" where "+DbConstents.ID_CACHE_MSG+"=? and "
				+ DbConstents.ID_MINE + "=?";
		db.execSQL(sql, new Object[] { messageCacheId, userId });
		db.close();

	}
	/**
	 * 更新消息状态
	 * */
	public void updateStatus(String userId,String msgCacheId,int status) {
		SQLiteDatabase sdb = helper.getWritableDatabase();
		Cursor cursor = sdb.rawQuery("select * from "+DbConstents.MSG_CHAT_TB+" where "
				+ DbConstents.ID_MINE + "=? and "+DbConstents.ID_CACHE_MSG+"=?", new String[] {
						userId, msgCacheId });
		if (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put(DbConstents.STATUS_MSG, status);
			int s = sdb.update(DbConstents.MSG_CHAT_TB, values,DbConstents.ID_CACHE_MSG+"=?", new String[] {msgCacheId});
		}
		sdb.close();
	}
}
