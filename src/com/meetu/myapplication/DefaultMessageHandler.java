package com.meetu.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.bean.MessageChatBean;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.MessagesDao;
import com.meetu.tools.DensityUtil;
import com.meetu.view.ChatViewInterface;

public class DefaultMessageHandler extends AVIMMessageHandler {
	MessagesDao msgDao = null;
	ChatmsgsDao chatDao = null;
	private Context context;
	ChatViewInterface updateBean;
	String conversationId = "";
	AVUser user ;

	public DefaultMessageHandler(Context context) {
		this.context = context;
		msgDao = new MessagesDao(context);
		chatDao = new ChatmsgsDao(context);
	}
	public void setUpdateBean(ChatViewInterface updateBean) {
		this.updateBean = null;
		this.updateBean = updateBean;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	@Override
	public void onMessage(AVIMMessage message, AVIMConversation conversation,
			AVIMClient client) {
		// TODO Auto-generated method stub
		super.onMessage(message, conversation, client);
		if (AVUser.getCurrentUser() == null) {
			return;
		}else{
			user = AVUser.getCurrentUser();
		}
		if (message instanceof AVIMTextMessage) {
			createChatMsg(conversation, message, msgDao, chatDao);
			return;
		}
		if (message instanceof AVIMImageMessage) {
			createChatPicMsg(conversation, message, msgDao, chatDao);
			return;
		}
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
		int msgType = (Integer) msg.getAttrs().get(Constants.CHAT_MSG_TYPE);
		if(msgType == Constants.TYPE_SCRIPT){
			saveScript(msg,conversation);
		}else{
			saveMsg(msgType,msg,conversation);
		}
	}
	//保存普通消息
	private void saveMsg(int msgType, AVIMTextMessage msg, AVIMConversation conversation) {
		String appendUserId = (String) msg.getAttrs().get(Constants.APPEND_USER_ID);
		int direction = 0;
		if(msg.getFrom().equals(user.getObjectId())){
			direction = Constants.IOTYPE_OUT;
		}else{
			direction = Constants.IOTYPE_IN;
		}
		MessageChatBean chatBean = new MessageChatBean();
		switch (msgType) {
		case Constants.TYPE_TEXT:
			if(direction == Constants.IOTYPE_IN){
				chatBean.setTypeMsg(Constants.SHOW_RECEIVE_TYPE_TEXT);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_SEND_TYPE_TEXT);
			}
			break;
		case Constants.MEMBER_ADD:
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				chatBean.setTypeMsg(Constants.SHOW_SELF_ADD);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_ADD);
			}
			break;
		case Constants.KICK_OUT:
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				chatBean.setTypeMsg(Constants.SHOW_SELF_KICK);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_KICK);
			}
			break;
		case Constants.QUIT:
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				chatBean.setTypeMsg(Constants.SHOW_SELF_QUIT);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_QUIT);
			}
			break;
		case Constants.CONV_DISSOLVE:
			chatBean.setTypeMsg(Constants.SHOW_CONV_DISSOLVE);
			break;
		case Constants.CONV_DISMISS:
			chatBean.setTypeMsg(Constants.SHOW_CONV_DISMISS);
			break;
		case Constants.GAG:
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				chatBean.setTypeMsg(Constants.SHOW_SELF_GAG);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_GAG);
			}
			break;
		case Constants.UN_GAG:
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				chatBean.setTypeMsg(Constants.SHOW_SELF_UN_GAG);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_UN_GAG);
			}
			break;
		default:
			break;
		}
		boolean b = (Boolean) msg.getAttrs().get(Constants.IS_SHOW_TIME);
		chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		if(appendUserId != null && !"".equals(appendUserId)){
			chatBean.setIdOperated(appendUserId);
		}
		if(msg.getText() != null){
			chatBean.setMsgText(msg.getText());
		}
		chatBean.setIdClient(msg.getFrom());
		chatBean.setIdMessage(msg.getMessageId());
		chatBean.setIdConversation(msg.getConversationId());
		chatBean.setDirectionMsg(direction);
		chatBean.setStatusMsg(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		chatBean.setSendTimeStamp(msg.getTimestamp());
		// 消息插入数据库库
		//chatDao.insert(chatBean);
		// 未读消息加1
		//msgDao.updateUnread(AVUser.getCurrentUser().getObjectId(),
				//conversation.getConversationId());
		if(conversationId != null && !"".equals(conversationId)){
			updateBean.updateView(chatBean);
		}else{
			if(msg.getConversationId().equals(conversationId)){
				updateBean.updateView(chatBean);	
			}
		}
	}
	//保存纸条消息
	private void saveScript(AVIMTextMessage msg, AVIMConversation conversation) {
		String script = (String) msg.getAttrs().get(Constants.SCRIP_ID);
		int scripx = (Integer) msg.getAttrs().get(Constants.SCRIP_X);
		int scripy = (Integer) msg.getAttrs().get(Constants.SCRIP_Y);
		int direction = 0;
		if(msg.getFrom().equals(user.getObjectId())){
			direction = Constants.IOTYPE_OUT;
		}else{
			direction = Constants.IOTYPE_IN;
		}
		Chatmsgs scriptBean = new Chatmsgs();
		scriptBean.setChatMsgType(Constants.SHOW_SCRIPT_MSG);
		scriptBean.setScriptId(script);
		scriptBean.setScripX(scripx);
		scriptBean.setScripY(scripy);
		scriptBean.setUid(AVUser.getCurrentUser().getObjectId());
		scriptBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		scriptBean.setClientId(msg.getFrom());
		scriptBean.setMessageId(msg.getMessageId());
		scriptBean.setConversationId(msg.getConversationId());
		scriptBean.setChatMsgDirection(direction);
		scriptBean.setChatMsgStatus(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		scriptBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		scriptBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		scriptBean.setContent(msg.getText());
		// 消息插入数据库库
		chatDao.insert(scriptBean);
		// 未读消息加1
		msgDao.updateUnread(AVUser.getCurrentUser().getObjectId(),
				msg.getConversationId());
	}
	// 图片消息处理方法
	public void createChatPicMsg(AVIMConversation conversation,
			AVIMMessage message, MessagesDao msgDao, ChatmsgsDao chatDao) {
		AVIMImageMessage msg = ((AVIMImageMessage) message);
		MessageChatBean chatBean = new MessageChatBean();
		int msgType = (Integer) msg.getAttrs().get(Constants.CHAT_MSG_TYPE);
		int direction = 0;
		if(msg.getFrom().equals(user.getObjectId())){
			direction = Constants.IOTYPE_OUT;
		}else{
			direction = Constants.IOTYPE_IN;
		}
		if (msgType == Constants.TYPE_IMG && direction == Constants.IOTYPE_OUT) {
			if(direction == Constants.IOTYPE_OUT){
				chatBean.setTypeMsg(Constants.SHOW_SEND_TYPE_IMG);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_RECEIVE_TYPE_IMG);
			}
		} 
		boolean b = (Boolean) msg.getAttrs().get(Constants.IS_SHOW_TIME);
		chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		chatBean.setIdClient(msg.getFrom());
		chatBean.setIdMessage(msg.getMessageId());
		chatBean.setIdConversation(msg.getConversationId());
		chatBean.setDirectionMsg(direction);
		chatBean.setStatusMsg(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		chatBean.setSendTimeStamp(msg.getTimestamp());
		chatBean.setFileUrl(msg.getAVFile().getThumbnailUrl(true, DensityUtil.dip2px(context, 160), DensityUtil.dip2px(context, 160),100,"jpg"));
		chatBean.setImgWidth(msg.getWidth());
		chatBean.setImgHeight(msg.getHeight());
		// 消息插入数据库
		//chatDao.insert(chatBean);
		// 未读消息加1
		//msgDao.updateUnread(AVUser.getCurrentUser().getObjectId(),
				//conversation.getConversationId());
	}
}
