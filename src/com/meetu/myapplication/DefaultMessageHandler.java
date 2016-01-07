package com.meetu.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.bean.MemberActivityBean;
import com.meetu.bean.MemberSeekBean;
import com.meetu.bean.MessageChatBean;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjChatWrap;
import com.meetu.common.Constants;
import com.meetu.common.DbConstents;
import com.meetu.common.Log;
import com.meetu.entity.Chatmsgs;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.ConversationUserDao;
import com.meetu.sqlite.MemberActivityDao;
import com.meetu.sqlite.MemberSeekDao;
import com.meetu.sqlite.MessageChatDao;
import com.meetu.sqlite.MessagesDao;
import com.meetu.tools.DensityUtil;
import com.meetu.view.ChatViewInterface;

public class DefaultMessageHandler extends AVIMMessageHandler {
	MessagesDao msgDao = null;
	ChatmsgsDao chatDao = null;
	MessageChatDao messageChatDao = null;
	ConversationUserDao convUserDao = null;
	MemberSeekDao memSeekDao = null;
	MemberActivityDao memActyDao = null;
	private Context context;
	ChatViewInterface updateBean;
	String conversationId = "";
	AVUser user ;

	public DefaultMessageHandler(Context context) {
		this.context = context;
		msgDao = new MessagesDao(context);
		chatDao = new ChatmsgsDao(context);
		messageChatDao = new MessageChatDao(context);
		convUserDao = new ConversationUserDao(context);
		memSeekDao = new MemberSeekDao(context);
		memActyDao = new MemberActivityDao(context);
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
			createChatMsg(conversation, message);
			return;
		}
		if (message instanceof AVIMImageMessage) {
			createChatPicMsg(conversation, message);
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
	public void createChatMsg(AVIMConversation conversation,AVIMMessage message) {
		AVIMTextMessage msg = ((AVIMTextMessage) message);
		Log.e("AVIMTextMessage", ""+msg.getAttrs().get(Constants.CHAT_MSG_TYPE));
		int msgType = (Integer) msg.getAttrs().get(Constants.CHAT_MSG_TYPE);
		if(msgType == Constants.TYPE_SCRIPT){
			saveScript(msg,conversation);
		}else{
			saveMsg(msgType,msg,conversation);
		}
	}
	//保存普通消息，修改群聊状态，update当前view
	private void saveMsg(int msgType, AVIMTextMessage msg, AVIMConversation conversation) {
		//用于标记是否发送信息给前台更新
		boolean isSnd = false;
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
			isSnd = true;
			if(direction == Constants.IOTYPE_IN){
				chatBean.setTypeMsg(Constants.SHOW_RECEIVE_TYPE_TEXT);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_SEND_TYPE_TEXT);
			}
			boolean b = (Boolean) msg.getAttrs().get(Constants.IS_SHOW_TIME);
			chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
			chatBean.setMsgText(msg.getText());
			break;
		case Constants.MEMBER_ADD:
			Log.e("MEMBER_ADD", "新成员"+appendUserId+"加入了");
			chatBean.setIdOperated(appendUserId);
			int convType = (Integer) msg.getAttrs().get("convType");
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				//插入成员
				String appendConvId = (String) msg.getAttrs().get("appendId");
				if(convType == Constants.CONV_TYPE_SEEK){
					isSnd = false;
					break;
				}else{
					isSnd = true;
					chatBean.setTypeMsg(Constants.SHOW_SELF_ADD);
					chatBean.setMsgText("欢迎加入群聊");
					MemberActivityBean actyBean = new MemberActivityBean();
					actyBean.setConvStatus(0);
					actyBean.setConversationId(conversation.getConversationId());
					actyBean.setMineId(user.getObjectId());
					actyBean.setMemberId(appendUserId);
					actyBean.setActivityId(appendConvId);
					memActyDao.saveUserActivity(actyBean);
				}
			}else{
				isSnd = true;
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_ADD);
				chatBean.setMsgText("新人加入了，打个招呼吧");
				//插入成员
				String appendConvId = (String) msg.getAttrs().get("appendId");
				if(convType == Constants.CONV_TYPE_SEEK){
					MemberSeekBean seekBean = new MemberSeekBean();
					seekBean.setConvStatus(0);
					seekBean.setConversationId(conversation.getConversationId());
					seekBean.setMineId(user.getObjectId());
					seekBean.setMemberSeekId(appendUserId);
					seekBean.setSeekId(appendConvId);
					memSeekDao.saveUserSeek(seekBean);
				}else{
					MemberActivityBean actyBean = new MemberActivityBean();
					actyBean.setConvStatus(0);
					actyBean.setConversationId(conversation.getConversationId());
					actyBean.setMineId(user.getObjectId());
					actyBean.setMemberId(appendUserId);
					actyBean.setActivityId(appendConvId);
					memActyDao.saveUserActivity(actyBean);
				}
				int convStatus = (Integer) msg.getAttrs().get("convStatus");
				convUserDao.updateConvStatus(user.getObjectId(), conversation.getConversationId(), convStatus);
			}
			break;
		case Constants.KICK_OUT:
			isSnd = true;
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				chatBean.setTypeMsg(Constants.SHOW_SELF_KICK);
				chatBean.setMsgText("您已被踢出群聊");
				//改变群聊状态
				String convId = (String) msg.getAttrs().get("convId");
				convUserDao.updateConvStatus(user.getObjectId(), convId, Constants.CONV_STATUS_KICK);
			}else{
				isSnd = false;
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_KICK);
				//成员减少
				memSeekDao.deleteUserTypeUserId(user.getObjectId(), conversation.getConversationId(), appendUserId);
			}
			break;
		case Constants.QUIT:
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				isSnd = false;
				//理论上不会接收到
				chatBean.setTypeMsg(Constants.SHOW_SELF_QUIT);
			}else{
				isSnd = true;
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_QUIT);
				//成员减少
				memSeekDao.deleteUserTypeUserId(user.getObjectId(), conversation.getConversationId(), appendUserId);
			}
			break;
		case Constants.CONV_DISSOLVE:
			isSnd = true;
			chatBean.setTypeMsg(Constants.SHOW_CONV_DISSOLVE);
			//聊天状态改变
			convUserDao.updateConvStatus(user.getObjectId(), conversation.getConversationId(), Constants.CONV_STATUS_DISSOLVE);
			break;
		case Constants.CONV_DISMISS:
			isSnd = true;
			chatBean.setTypeMsg(Constants.SHOW_CONV_DISMISS);
			//聊天状态改变
			convUserDao.updateConvStatus(user.getObjectId(), conversation.getConversationId(), Constants.CONV_STATUS_DISMISS);
			break;
		case Constants.GAG:
			isSnd = true;
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				chatBean.setTypeMsg(Constants.SHOW_SELF_GAG);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_GAG);
			}
			break;
		case Constants.UN_GAG:
			isSnd = true;
			if(appendUserId != null && appendUserId.equals(user.getObjectId())){
				chatBean.setTypeMsg(Constants.SHOW_SELF_UN_GAG);
			}else{
				chatBean.setTypeMsg(Constants.SHOW_MEMBER_UN_GAG);
			}
			break;
		default:
			break;
		}
		if(!isSnd){
			return;
		}
		chatBean.setIdMine(user.getObjectId());
		chatBean.setIdClient(msg.getFrom());
		chatBean.setIdMessage(msg.getMessageId());
		chatBean.setIdConversation(msg.getConversationId());
		chatBean.setDirectionMsg(direction);
		chatBean.setStatusMsg(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		chatBean.setSendTimeStamp(msg.getTimestamp());
		chatBean.setIdCacheMsg(System.currentTimeMillis()+"");
		// 消息插入数据库
		messageChatDao.insert(chatBean);
		convUserDao.updateTime(user.getObjectId(), conversationId);
		if(updateBean == null){
			return ;
		}
		if(conversationId != null && !"".equals(conversationId)){
			if(msg.getConversationId().equals(conversationId)){
				updateBean.updateView(chatBean);	
			}
		}else{
			// 未读消息加1
			convUserDao.updateUnread(AVUser.getCurrentUser().getObjectId(),
					conversation.getConversationId());
			updateBean.updateView(chatBean);
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
	public void createChatPicMsg(AVIMConversation conversation,AVIMMessage message) {
		AVIMImageMessage msg = ((AVIMImageMessage) message);
		log.e("msg", "收到图片消息");
		log.e("createChatPicMsg",""+ msg.getAttrs().get("chatType")+msg.getAttrs().get("userId")+msg.getAttrs().get("appendId")+msg.getAttrs().get("msgType"));
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
		chatBean.setIdMine(user.getObjectId());
		chatBean.setIdClient(msg.getFrom());
		chatBean.setIdMessage(msg.getMessageId());
		chatBean.setIdConversation(msg.getConversationId());
		chatBean.setDirectionMsg(direction);
		chatBean.setStatusMsg(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		chatBean.setSendTimeStamp(msg.getTimestamp());
		chatBean.setFileUrl(msg.getAVFile().getThumbnailUrl(true, DensityUtil.dip2px(context, 160), DensityUtil.dip2px(context, 160),100,"jpg"));
		chatBean.setImgWidth(msg.getWidth());
		chatBean.setImgHeight(msg.getHeight());
		chatBean.setIdCacheMsg(System.currentTimeMillis()+"");
		// 消息插入数据库
		messageChatDao.insert(chatBean);
		// 未读消息加1
		convUserDao.updateUnread(AVUser.getCurrentUser().getObjectId(),
				conversation.getConversationId());
		if(updateBean == null){
			return ;
		}
		if(conversationId != null && !"".equals(conversationId)){
			updateBean.updateView(chatBean);
		}else{
			if(msg.getConversationId().equals(conversationId)){
				updateBean.updateView(chatBean);	
			}
		}
	}
}
