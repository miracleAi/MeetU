package com.meetu;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestReceiveMsg extends Activity{
	TextView textIv,urlTv;
	MessageHandler msgHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_receive_layout);
		msgHandler = new MessageHandler();
		initView();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, msgHandler);
	}
	private void initView() {
		// TODO Auto-generated method stub
		textIv = (TextView) findViewById(R.id.text_tv);
		urlTv = (TextView) findViewById(R.id.url_tv);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVIMMessageManager.unregisterMessageHandler(AVIMTypedMessage.class, msgHandler);
	}
	//消息处理handler
	public class MessageHandler extends AVIMMessageHandler{
		@Override
		public void onMessage(AVIMMessage message,
				AVIMConversation conversation, AVIMClient client) {
			// TODO Auto-generated method stub
			super.onMessage(message, conversation, client);
			//接收消息界面更新，插入数据库
			textIv.setText(message.getContent());
		}
		@Override
		public void onMessageReceipt(AVIMMessage message,
				AVIMConversation conversation, AVIMClient client) {
			// TODO Auto-generated method stub
			super.onMessageReceipt(message, conversation, client);
		}
	}
}
