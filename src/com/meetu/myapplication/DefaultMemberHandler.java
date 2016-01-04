package com.meetu.myapplication;

import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.common.Log;
import com.meetu.entity.Chatmsgs;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.MessagesDao;
import com.meetu.sqlite.UserAboutDao;

public class DefaultMemberHandler extends AVIMConversationEventHandler {

	private Context context;
	MessagesDao msgDao = null;
	ChatmsgsDao chatDao = null;
	ObjUser user = null;
	UserAboutDao aboutDao = null;

	public DefaultMemberHandler(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		msgDao = new MessagesDao(context);
		chatDao = new ChatmsgsDao(context);
		aboutDao = new UserAboutDao(context);
	}

	@Override
	public void onInvited(AVIMClient arg0, AVIMConversation arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKicked(AVIMClient client, AVIMConversation conversation, String str) {
		log.e("zcq", "进入全局监听被踢出");
		if (AVUser.getCurrentUser() == null) {
			return;
		}
		user = AVUser.cast(ObjUser.getCurrentUser(), ObjUser.class);
		Intent intent = new Intent(Constants.RECEIVE_MSG);
		context.sendBroadcast(intent);
		handleMemberRemove(client, conversation, str, user);
	}

	@Override
	public void onMemberJoined(AVIMClient client,
			AVIMConversation conversation, List<String> array, String str) {
		// TODO Auto-generated method stub
		if (AVUser.getCurrentUser() == null) {
			return;
		}
		Intent intent = new Intent(Constants.RECEIVE_MSG);
		context.sendBroadcast(intent);
		user = AVUser.cast(ObjUser.getCurrentUser(), ObjUser.class);
		handleMemberAdd(client, conversation, array, str, user);
	}

	@Override
	public void onMemberLeft(AVIMClient client, AVIMConversation conversation,
			List<String> array, String str) {
		// TODO Auto-generated method stub
		log.e("zcq", "进入全局监听其他成员被踢出");
	}

	// 成员加入消息处理
	public void handleMemberAdd(final AVIMClient client,
			final AVIMConversation conversation, List<String> array,
			String str, final ObjUser user) {
		Log.e("成员加入消息","新成员加入了"+user.getObjectId());
		for (String userId : array) {
			if (userId.equals(user.getObjectId())) {
				continue;
			}
			// 普通群，直接保存
			Chatmsgs chatBean = new Chatmsgs();
			//chatBean.setChatMsgType(Constants.SHOW_MEMBERCHANGE);
			chatBean.setClientId(client.getClientId());
			chatBean.setNowJoinUserId(client.getClientId());
			chatBean.setUid(user.getObjectId());
			chatBean.setNowJoinUserId(userId);
			chatBean.setMessageCacheId(String.valueOf(System
					.currentTimeMillis()));
			chatBean.setConversationId(conversation.getConversationId());
			chatBean.setSendTimeStamp(String.valueOf(System
					.currentTimeMillis()));
			chatDao.insert(chatBean);
			// 未读消息加1,保存未读
			msgDao.updateUnread(user.getObjectId(),
					conversation.getConversationId());
			//插入成员
			UserAboutBean aboutBean = new UserAboutBean();
			aboutBean.setUserId(user.getObjectId());
			aboutBean.setAboutType(Constants.CONVERSATION_TYPE);
			aboutBean.setAboutUserId(client.getClientId());
			aboutBean.setAboutColetctionId(conversation.getConversationId());
			aboutDao.saveUserAboutBean(aboutBean);
		}
	}

	// 被踢出
	public void handleMemberRemove(AVIMClient client,
			AVIMConversation conversation, String str,
			ObjUser user) {
		// 未读消息加1,保存未读
		msgDao.updateUnread(user.getObjectId(),
				conversation.getConversationId());
		Chatmsgs chatBean = new Chatmsgs();
		//chatBean.setChatMsgType(Constants.SHOW_SELF_DEL);
		chatBean.setClientId(client.getClientId());
		chatBean.setNowJoinUserId(client.getClientId());
		chatBean.setUid(user.getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setContent("您已被踢出觅聊");
		chatBean.setMessageCacheId(String.valueOf(System
				.currentTimeMillis()));
		chatBean.setConversationId(conversation.getConversationId());
		chatDao.insert(chatBean);
		//删除成员
		aboutDao.deleteUserTypeUserId(user.getObjectId(), Constants.CONVERSATION_TYPE, conversation.getConversationId(), client.getClientId());
	}
}
