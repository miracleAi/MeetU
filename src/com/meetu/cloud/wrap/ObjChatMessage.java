package com.meetu.cloud.wrap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationMemberCountCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.cloud.callback.ObjAuthoriseCategoryCallback;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjCoversationCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunCountCallback;
import com.meetu.cloud.callback.ObjListCallback;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;
import com.meetu.entity.Chatmsgs;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;

public class ObjChatMessage {
	/**
	 * 创建与服务器连接,登录
	 * @param client
	 */
	public static void connectToChatServer(AVIMClient client,final ObjAvimclientCallback callback){
		client.open(new AVIMClientCallback() {

			@Override
			public void done(AVIMClient client, AVIMException e) {
				if(e != null){
					callback.callback(null, e);
					return ;
				}
				if(client != null){
					callback.callback(client, null);
				}else{
					callback.callback(client, new AVException(0, "登录失败"));
				}
			}

		});
	}
	/**
	 * 创建或者查询一个已有的对话
	 * @param members 对话的成员
	 * @param name 对话的名字
	 * @param attributes 对话的额外属性
	 * @param isTransient 是否是暂态对话
	 * @param isUnique 如果已经存在符合条件的对话，是否返回已有对话
	 *                 为 false 时，则一直为创建新的对话
	 *                 为 true 时，则先查询，如果已有符合条件的对话，则返回已有的，否则，创建新的并返回
	 *                 为 true 时，仅 members 为有效查询条件
	 * @param callback
	 * public void createConversation(final List<String> members, final String name,
      final Map<String, Object> attributes, final boolean isTransient, final boolean isUnique,
      final AVIMConversationCreatedCallback callback)
	 */
	public static void createChat(AVIMClient client,List<String> members,String name,final ObjCoversationCallback callback){
		client.createConversation(members, name, null,
				new AVIMConversationCreatedCallback() {

			@Override
			public void done(AVIMConversation conversation, AVIMException e) {
				if(e != null){
					callback.callback(null, e);
					return ;
				}
				if(null != conversation){
					callback.callback(conversation, null);
				}else{
					callback.callback(null, new AVException(0, "创建会话失败"));
				}
			}});
	}
	/**
	 * 加入对话
	 * @param ConversationId 对话的 id
	 */
	public static void joinChat(AVIMClient client,AVIMConversation conv,final ObjFunBooleanCallback callback){
		conv.join(new AVIMConversationCallback(){

			@Override
			public void done(AVIMException e) {
				if(e != null){
					callback.callback(false, e);
					return ;
				}else{
					//加入成功
					callback.callback(true, null);
				}
			}

		});

	}
	/**
	 * 查询会话成员数量
	 * */
	public static void getChatCount(AVIMConversation conv,final ObjFunCountCallback callback){
		conv.getMemberCount(new AVIMConversationMemberCountCallback() {
			
			@Override
			public void done(Integer count, AVIMException e) {
				// TODO Auto-generated method stub
				if( e != null){
					callback.callback(0, e);
					return ;
				}
				if(count != null){
					callback.callback(count, null);
				}else{
					callback.callback(0, new AVException(0, "获取成员数量失败"));
				}
			}
		});
	}
	/**
	 *获取会话成员
	 * */
	public static void getChatMembers(final AVIMConversation conv,final ObjListCallback callback){
		final ArrayList<String> mList = new ArrayList<String>();
		conv.fetchInfoInBackground(new AVIMConversationCallback() {
			
			@Override
			public void done(AVIMException e) {
				// TODO Auto-generated method stub
				if(e == null){
					List<String> list = conv.getMembers();
					for(String s:list){
						mList.add(s);
					}
					callback.callback(mList, null);
				}else{
					callback.callback(null, e);
				}
			}
		});
	}
	/**
	 * 发送文本消息
	 * @param conversation
	 * @param message
	 */
	public static void sendChatMsg(AVIMConversation conversation,String text,final ObjFunBooleanCallback callback){
		AVIMTextMessage msg = new AVIMTextMessage();
		msg.setText(text);
		// 发送消息
		conversation.sendMessage(msg, new AVIMConversationCallback() {

			@Override
			public void done(AVIMException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					//发送成功
					callback.callback(true, null);
				}else{
					callback.callback(false, e);
				}
			}

		});

	}
	/**
	 * 发送图片消息
	 * @param conversation
	 * @param message
	 */
	public void sendPicMsg(AVIMConversation conversation,String path,final ObjFunBooleanCallback callback){
		try {
			AVIMImageMessage picture = new AVIMImageMessage(path);
			conversation.sendMessage(picture, new AVIMConversationCallback() {
				
				@Override
				public void done(AVIMException e) {
					// TODO Auto-generated method stub
					if (e == null) {
						//发送成功
						callback.callback(true, null);
					}else{
						callback.callback(false, e);
					}
				}
			});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取聊天记录
	 */
	public static void getHistoryMsg(){
		
	}
	/**注销聊天
	 * 
	 * @param client
	 */
	public static void logOutChat(AVIMClient client,final ObjFunBooleanCallback callback){
		client.close(new AVIMClientCallback() {

			@Override
			public void done(AVIMClient client, AVIMException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(false, e);
					return ;
				}
				if(client == null){
					callback.callback(false, new AVException(0, "退出失败"));
				}else{
					callback.callback(true, null);
				}
			}
		});
	}
}
