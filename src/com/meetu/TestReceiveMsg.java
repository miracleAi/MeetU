package com.meetu;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;
import com.meetu.entity.Messages;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.MessagesDao;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TestReceiveMsg extends Activity{
	TextView textIv,urlTv,memberTv;
	MessageHandler msgHandler;
	//当前用户
	ObjUser user = new ObjUser();
	ChatmsgsDao chatDao = null;
	//当前会话
	String conversationId = "5623af6560b2ce30d24a2c67";
	MessagesDao msgDao = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_receive_layout);
		msgHandler = new MessageHandler();
		chatDao = new ChatmsgsDao(getApplicationContext());
		msgDao = new MessagesDao(this);
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
		memberTv = (TextView) findViewById(R.id.member_tv);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVIMMessageManager.unregisterMessageHandler(AVIMTypedMessage.class, msgHandler);
	}
	//文本消息处理方法
	public void createChatMsg(AVIMConversation conversation,AVIMTypedMessage message){
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
		boolean b = (Boolean) msg.getAttrs().get("isShowTime");
		chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setContent(msg.getText());
		chatDao.insert(chatBean);
		if(conversation.getConversationId().equals(conversationId)){
			//测试显示
			textIv.setText(msg.getText());
		}else{
			//未读消息加1
			msgDao.updateUnread(user.getObjectId(), msg.getConversationId());
		}

	}
	//图片消息处理方法
	public void createChatPicMsg(AVIMConversation conversation,AVIMTypedMessage message){
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
		boolean b = (Boolean) msg.getAttrs().get("isShowTime");
		chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setImgMsgImageUrl(msg.getFileUrl());
		chatBean.setImgMsgImageHeight(msg.getHeight());
		chatBean.setImgMsgImageWidth(msg.getWidth());
		chatDao.insert(chatBean);
		if(conversation.getConversationId().equals(conversationId)){
			//测试显示
			urlTv.setText(msg.getFileUrl());
		}else{
			//未读消息加1
			msgDao.updateUnread(user.getObjectId(), msg.getConversationId());
		}
	}
	//成员加入消息处理
	public void handleMemberAdd(final AVIMClient client, final AVIMConversation conversation,
			List<String> array, String str){
		int msgType = (Integer) conversation.getAttribute("cType");
		if(msgType == Constants.ACTYSG){
			//活动群，判断是否参加
			String actyId = (String) conversation.getAttribute("appendId");
			try {
				ObjActivity acty = ObjActivity.createWithoutData(ObjActivity.class, actyId);
				ObjActivityWrap.queryUserJoin(acty, user, new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						if(e != null){
							return ;
						}
						if(result){
							//已参加，保存
							ObjUserWrap.getObjUser(client.getClientId(), new ObjUserInfoCallback() {
								
								@Override
								public void callback(ObjUser joinuser, AVException e) {
									// TODO Auto-generated method stub
									Chatmsgs chatBean = new Chatmsgs();
									chatBean.setChatMsgType(Constants.MEMBERADD_TYPE);
									chatBean.setNowJoinUserId(client.getClientId());
									chatBean.setUid(user.getObjectId());
									chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
									chatBean.setContent(joinuser.getNameNick()+"加入群聊");
									chatDao.insert(chatBean);
									if(conversation.getConversationId().equals(conversationId)){
										//显示
										memberTv.setText(client.getClientId());
									}else{
										//未读消息加1,保存未读
										msgDao.updateUnread(user.getObjectId(), conversation.getConversationId());
									}
								}
							});
						}else{
							//未参加  直接放弃
						}
					}
				});
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//普通群，直接保存
			Chatmsgs chatBean = new Chatmsgs();
			chatBean.setChatMsgType(Constants.MEMBERADD_TYPE);
			chatBean.setNowJoinUserId(client.getClientId());
			chatBean.setUid(user.getObjectId());
			chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
			chatDao.insert(chatBean);
			if(conversation.getConversationId().equals(conversationId)){
				//显示
				memberTv.setText(client.getClientId());
			}else{
				//未读消息加1,保存未读
				msgDao.updateUnread(user.getObjectId(), conversation.getConversationId());
			}
		}
	}
	//被踢出
	public void handleMemberRemove(AVIMClient client, AVIMConversation conversation,
			List<String> array, String str){
		if(conversation.getConversationId().equals(conversationId)){
			//显示
			memberTv.setText("您已被踢出");
			//删除会话缓存
			msgDao.deleteConv(user.getObjectId(), conversation.getConversationId());
			//删除消息缓存
			chatDao.deleteConversationId(user.getObjectId(), conversation.getConversationId());
		}else{
			//未读消息加1,保存未读
			msgDao.updateUnread(user.getObjectId(), conversation.getConversationId());
			Chatmsgs chatBean = new Chatmsgs();
			chatBean.setChatMsgType(Constants.MEMBERADD_TYPE);
			chatBean.setNowJoinUserId(client.getClientId());
			chatBean.setUid(user.getObjectId());
			chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
			chatBean.setContent("您被踢出群聊");
			chatDao.insert(chatBean);
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
				createChatMsg(conversation,message);
				break;
			case Constants.IMAGE_TYPE:
				createChatPicMsg(conversation,message);
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
			handleMemberAdd(client, conversation, array, str);
		}

		@Override
		public void onMemberLeft(AVIMClient client, AVIMConversation conversation,
				List<String> array, String str) {
			// 被踢出人，踢出人
			handleMemberRemove(client, conversation, array, str);
			
		}

	}
}
