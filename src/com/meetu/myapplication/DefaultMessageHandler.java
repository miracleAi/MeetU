package com.meetu.myapplication;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;

public class DefaultMessageHandler extends AVIMMessageHandler{

	@Override
	public void onMessage(AVIMMessage message, AVIMConversation conversation,
			AVIMClient client) {
		// TODO Auto-generated method stub
		super.onMessage(message, conversation, client);
	}
	@Override
	public void onMessageReceipt(AVIMMessage message,
			AVIMConversation conversation, AVIMClient client) {
		// TODO Auto-generated method stub
		super.onMessageReceipt(message, conversation, client);
	}
}
