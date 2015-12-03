package com.meetu.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.MessagesDao;

public class DefaultMessageHandler extends AVIMMessageHandler {
	MessagesDao msgDao = null;
	ChatmsgsDao chatDao = null;
	private Context context;

	public DefaultMessageHandler(Context context) {
		this.context = context;
		msgDao = new MessagesDao(context);
		chatDao = new ChatmsgsDao(context);
	}

	@Override
	public void onMessage(AVIMMessage message, AVIMConversation conversation,
			AVIMClient client) {
		// TODO Auto-generated method stub
		super.onMessage(message, conversation, client);
		if (AVUser.getCurrentUser() == null) {
			return;
		}
		if (message instanceof AVIMTextMessage) {
			createChatMsg(conversation, message, msgDao, chatDao);
			return;
		}
		if (message instanceof AVIMImageMessage) {
			createChatPicMsg(conversation, message, msgDao, chatDao);
			return;
		}
		Intent intent = new Intent(Constants.RECEIVE_MSG);
		context.sendBroadcast(intent);
	}

	@Override
	public void onMessageReceipt(AVIMMessage message,
			AVIMConversation conversation, AVIMClient client) {
		// TODO Auto-generated method stub
		super.onMessageReceipt(message, conversation, client);
	}

	// 文本消息处理方法
	public void createChatMsg(AVIMConversation conversation,
			AVIMMessage message, MessagesDao msgDao, ChatmsgsDao chatDao) {
		AVIMTextMessage msg = ((AVIMTextMessage) message);
		Chatmsgs chatBean = new Chatmsgs();
		int msgType = (Integer) msg.getAttrs().get(Constants.CHAT_MSG_TYPE);
		int derection = ChatMsgUtils.getDerection(msg.getMessageIOType());
		if (msgType == Constants.SHOW_SCRIPT) {
			chatBean.setChatMsgType(Constants.SHOW_SCRIPT);
			String script = (String) msg.getAttrs().get(Constants.SCRIP_ID);
			int scripx = (Integer) msg.getAttrs().get(Constants.SCRIP_X);
			int scripy = (Integer) msg.getAttrs().get(Constants.SCRIP_Y);
			chatBean.setScriptId(script);
			chatBean.setScripX(scripx);
			chatBean.setScripY(scripy);
		} else {
			if (msgType == Constants.SHOW_TEXT
					&& derection == Constants.IOTYPE_OUT) {
				chatBean.setChatMsgType(Constants.SHOW_SEND_TEXT);
			} else if (msgType == Constants.SHOW_TEXT
					&& derection == Constants.IOTYPE_IN) {
				chatBean.setChatMsgType(Constants.SHOW_RECV_TEXT);
			} else {
				chatBean.setChatMsgType(msgType);
			}
			boolean b = (Boolean) msg.getAttrs().get(Constants.IS_SHOW_TIME);
			chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		}
		chatBean.setUid(AVUser.getCurrentUser().getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setClientId(msg.getFrom());
		chatBean.setMessageId(msg.getMessageId());
		chatBean.setConversationId(msg.getConversationId());
		chatBean.setChatMsgDirection(derection);
		chatBean.setChatMsgStatus(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setContent(msg.getText());
		// 消息插入数据库库
		chatDao.insert(chatBean);
		// 未读消息加1
		msgDao.updateUnread(AVUser.getCurrentUser().getObjectId(),
				conversation.getConversationId());

	}

	// 图片消息处理方法
	public void createChatPicMsg(AVIMConversation conversation,
			AVIMMessage message, MessagesDao msgDao, ChatmsgsDao chatDao) {
		AVIMImageMessage msg = ((AVIMImageMessage) message);
		Chatmsgs chatBean = new Chatmsgs();
		int msgType = (Integer) msg.getAttrs().get(Constants.CHAT_MSG_TYPE);
		int derection = ChatMsgUtils.getDerection(msg.getMessageIOType());
		if (msgType == Constants.SHOW_IMG && derection == Constants.IOTYPE_OUT) {
			chatBean.setChatMsgType(Constants.SHOW_SEND_IMG);
		} else if (msgType == Constants.SHOW_IMG
				&& derection == Constants.IOTYPE_IN) {
			chatBean.setChatMsgType(Constants.SHOW_RECV_IMG);
		} else {
			chatBean.setChatMsgType(msgType);
		}
		chatBean.setUid(AVUser.getCurrentUser().getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setClientId(msg.getFrom());
		chatBean.setMessageId(msg.getMessageId());
		chatBean.setConversationId(msg.getConversationId());
		chatBean.setChatMsgDirection(derection);
		chatBean.setChatMsgStatus(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		boolean b = (Boolean) msg.getAttrs().get(Constants.IS_SHOW_TIME);
		chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setImgMsgImageUrl(msg.getFileUrl());
		chatBean.setImgMsgImageHeight(msg.getHeight());
		chatBean.setImgMsgImageWidth(msg.getWidth());
		// 消息插入数据库
		chatDao.insert(chatBean);
		// 未读消息加1
		msgDao.updateUnread(AVUser.getCurrentUser().getObjectId(),
				conversation.getConversationId());
	}
}
