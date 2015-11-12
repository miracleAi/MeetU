package com.meetu;

import java.util.List;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;
import com.meetu.sqlite.ChatmsgsDao;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TestReceiveMsg extends Activity{
	TextView textIv,urlTv;
	MessageHandler msgHandler;
	//当前用户
	ObjUser user = new ObjUser();
	ChatmsgsDao chatDao = null;
	//当前会话
	String conversationId = "5623af6560b2ce30d24a2c67";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_receive_layout);
		msgHandler = new MessageHandler();
		chatDao = new ChatmsgsDao(getApplicationContext());
		initView();
		if(AVUser.getCurrentUser() != null){
			user = AVUser.cast(AVUser.getCurrentUser(), ObjUser.class);
		}else{
			Toast.makeText(getApplicationContext(), "请先登录", 1000).show();
		}
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
	//文本消息处理方法
	public void createChatMsg(AVIMTypedMessage message){
		AVIMTextMessage msg = ((AVIMTextMessage)message);
		Chatmsgs chatBean = new Chatmsgs();
		chatBean.setChatMsgType(Constants.TEXT_TYPE);
		chatBean.setUid(user.getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setClientId(msg.getFrom());
		chatBean.setMessageId(msg.getMessageId());
		chatBean.setConversationId(msg.getConversationId());
		chatBean.setChatMsgDirection(ChatMsgUtils.getDerection(msg.getMessageIOType()));
		chatBean.setChatMsgStatus(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		//此处为测试数据，实际为获取最新一条消息时间
		long t = System.currentTimeMillis() - 10000;
		chatBean.setIsShowTime(ChatMsgUtils.isShowChatTime(t));
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setContent(msg.getText());
		chatDao.insert(chatBean);
		//测试显示
		textIv.setText(msg.getText());
	}
	//图片消息处理方法
	public void createChatPicMsg(AVIMTypedMessage message){
		AVIMImageMessage msg = ((AVIMImageMessage)message);
		Chatmsgs chatBean = new Chatmsgs();
		chatBean.setChatMsgType(Constants.IMAGE_TYPE);
		chatBean.setUid(user.getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setClientId(msg.getFrom());
		chatBean.setMessageId(msg.getMessageId());
		chatBean.setConversationId(msg.getConversationId());
		chatBean.setChatMsgDirection(ChatMsgUtils.getDerection(msg.getMessageIOType()));
		chatBean.setChatMsgStatus(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		//此处为测试数据，实际为获取最新一条消息时间
		long t = System.currentTimeMillis() - 10000;
		chatBean.setIsShowTime(ChatMsgUtils.isShowChatTime(t));
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setImgMsgImageUrl(msg.getFileUrl());
		chatBean.setImgMsgImageHeight(msg.getHeight());
		chatBean.setImgMsgImageWidth(msg.getWidth());
		chatDao.insert(chatBean);
		//测试显示
		urlTv.setText(msg.getFileUrl());
	}
	//成员加入消息处理
	public void handleMember(AVIMClient client, AVIMConversation conversation,
			List<String> array, String str){
		if(conversation.getConversationId().equals(conversationId)){
			//位于当前页面,显示  保存
			textIv.setText(array.get(0));
		}else{
			//直接保存
		}
	}
	//消息处理handler
	public class MessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage>{
		@Override
		public void onMessage(AVIMTypedMessage message,
				AVIMConversation conversation, AVIMClient client) {
			// TODO Auto-generated method stub
			super.onMessage(message, conversation, client);
			switch (message.getMessageType()) {
			case Constants.TEXT_TYPE:
				createChatMsg(message);
				break;
			case Constants.IMAGE_TYPE:
				break;

			default:
				break;
			}
		}
		@Override
		public void onMessageReceipt(AVIMTypedMessage message,
				AVIMConversation conversation, AVIMClient client) {
			// TODO Auto-generated method stub
			super.onMessageReceipt(message, conversation, client);
		}
	}
	//群成员变动消息处理
	public class MemberChangeHandler extends AVIMConversationEventHandler{

		@Override
		public void onInvited(AVIMClient client, AVIMConversation conversation,
				String str) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onKicked(AVIMClient client, AVIMConversation conversation,
				String str) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMemberJoined(AVIMClient client, AVIMConversation conversation,
				List<String> array, String str) {
			// 参与者  ，邀请人
			//在当前聊天--》1.活动群，判断参与者是否参加活动，不参加不提示。2.普通群：提示
			//不在当前聊天--》1.参加，插入数据库。2.插入数据库
		}

		@Override
		public void onMemberLeft(AVIMClient client, AVIMConversation conversation,
				List<String> array, String str) {
			// 被踢出人，踢出人

		}

	}
}
