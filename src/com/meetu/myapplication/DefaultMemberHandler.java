package com.meetu.myapplication;

import java.util.List;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.MessagesDao;

public class DefaultMemberHandler extends AVIMConversationEventHandler {

	private Context context;
	MessagesDao msgDao = null;
	ChatmsgsDao chatDao = null;
	ObjUser user = null;

	public DefaultMemberHandler(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		msgDao = new MessagesDao(context);
		chatDao = new ChatmsgsDao(context);
	}

	@Override
	public void onInvited(AVIMClient arg0, AVIMConversation arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKicked(AVIMClient arg0, AVIMConversation arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMemberJoined(AVIMClient client,
			AVIMConversation conversation, List<String> array, String str) {
		// TODO Auto-generated method stub
		if (AVUser.getCurrentUser() == null) {
			return;
		}
		user = AVUser.cast(ObjUser.getCurrentUser(), ObjUser.class);
		handleMemberAdd(client, conversation, array, str, user);
	}

	@Override
	public void onMemberLeft(AVIMClient client, AVIMConversation conversation,
			List<String> array, String str) {
		// TODO Auto-generated method stub
		if (AVUser.getCurrentUser() == null) {
			return;
		}
		user = AVUser.cast(ObjUser.getCurrentUser(), ObjUser.class);
		if (!array.contains(user.getObjectId())) {
			// 被踢出者中不包含自己，不处理
			return;
		}
		handleMemberRemove(client, conversation, array, str, user);
	}

	// 成员加入消息处理
	public void handleMemberAdd(final AVIMClient client,
			final AVIMConversation conversation, List<String> array,
			String str, final ObjUser user) {
		log.d("mytest", "conversation" + conversation);
		for (String userId : array) {
			if (userId.equals(user.getObjectId())) {
				continue;
			}
			// 普通群，直接保存
			Chatmsgs chatBean = new Chatmsgs();
			chatBean.setChatMsgType(Constants.SHOW_MEMBERCHANGE);
			chatBean.setNowJoinUserId(client.getClientId());
			chatBean.setUid(user.getObjectId());
			chatBean.setNowJoinUserId(userId);
			chatBean.setMessageCacheId(String.valueOf(System
					.currentTimeMillis()));
			chatDao.insert(chatBean);
			// 未读消息加1,保存未读
			msgDao.updateUnread(user.getObjectId(),
					conversation.getConversationId());
		}
	}

	// 被踢出
	public void handleMemberRemove(AVIMClient client,
			AVIMConversation conversation, List<String> array, String str,
			ObjUser user) {
		// 未读消息加1,保存未读
		msgDao.updateUnread(user.getObjectId(),
				conversation.getConversationId());
		Chatmsgs chatBean = new Chatmsgs();
		chatBean.setChatMsgType(Constants.SHOW_MEMBERCHANGE);
		chatBean.setNowJoinUserId(client.getClientId());
		chatBean.setUid(user.getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setContent("您被踢出群聊");
		chatDao.insert(chatBean);
	}
}
