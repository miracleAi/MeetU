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

public class MessagesDao {
	private  MySqliteDBHelper helper;

	private ChatmsgsDao chatmsgsDao;

	public MessagesDao(Context context){
		helper=new MySqliteDBHelper(context);
	}
	/**
	 * 插入或替换数据
	 * @param messages 消息表实体 Bean
	 */
	public void insert(Messages messages){

		SQLiteDatabase db=helper.getReadableDatabase();
		db.execSQL("insert or replace into messages values(null," +
				"?,?,?,?,?," +
				"?,?,?,?,?,?)", 
				new Object[]{messages.getUserId(),messages.getConversationID(),messages.getConversationType(),messages.getTiStatus(),messages.getCreatorID(),messages.getTimeOver(),
				messages.getActyId(),messages.getActyName(),messages.getChatId(),messages.getChatName(),messages.getUnreadMsgCount()});
		db.close();
	}
	/**
	 * 插入列表
	 * */
	public void insertList(ArrayList<Messages> list){
		SQLiteDatabase db=helper.getReadableDatabase();
		for(int i=0;i<list.size();i++){
			insert(list.get(i));
		}
		db.close();
	}
	/**
	 * 修改未读条数
	 * */
	public void updateUnread(String userId,String convId){
		SQLiteDatabase sdb=helper.getWritableDatabase();
		Cursor cursor=sdb.rawQuery("select  from messages where "+Constants.USERID +"=? and _conversation_id=?",new  String[]{userId,convId});
		cursor.moveToFirst();
		if(!cursor.moveToLast()){
			int count = cursor.getInt(cursor.getColumnIndex("_unread_count"));
			ContentValues values = new ContentValues();
			values.put("_unread_count", count+1);
			sdb.update("messages", values,Constants.USERID+"=? and _conversation_id=?"
					,new String[]{userId,convId});
		}
		sdb.close();
	}
	/**
	 * 根据用户删除所有数据
	 * */
	public void deleteAll(String uid){
		SQLiteDatabase db=helper.getReadableDatabase();
		String sql="delete * from messages where "+Constants.USERID +"="+uid;
		db.execSQL(sql);
		db.close();
	}
	/**
	 * 删除会话
	 * */
	public void deleteConv(String userId,String convId){
		SQLiteDatabase db=helper.getReadableDatabase();
		String sql="delete * from messages where "+Constants.USERID +"="+userId+" and _conversation_id="+convId;
		db.execSQL(sql);
		db.close();
	}
	//获取列表数据
	public ArrayList<Messages> getMessages(String uid){
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor c=db.rawQuery("select * from messages where "+Constants.USERID +"=?", new String[]{uid});

		ArrayList<Messages> list=new ArrayList<Messages>();
		while(c.moveToNext()){
			Messages messages=new Messages();

			messages.setConversationID(c.getString(c.getColumnIndex("_conversation_id")));
			messages.setConversationType(c.getInt(c.getColumnIndex("_conversation_type")));
			messages.setCreatorID(c.getString(c.getColumnIndex("_creator_id")));
			messages.setTimeOver(c.getLong(c.getColumnIndex("_time_over")));
			messages.setActyId(c.getString(c.getColumnIndex("_acty_id")));
			messages.setActyName(c.getString(c.getColumnIndex("_acty_name")));
			messages.setChatId(c.getString(c.getColumnIndex("_chat_id")));
			messages.setChatName(c.getString(c.getColumnIndex("_chat_name")));
			messages.setUserId(c.getString(c.getColumnIndex(Constants.USERID)));
			messages.setTiStatus(c.getInt(c.getColumnIndex("_ti_status")));
			messages.setUnreadMsgCount(c.getInt(c.getColumnIndex("_unread_count")));
			list.add(messages);			
		}
		c.close();
		db.close();

		return list;
	}






}
