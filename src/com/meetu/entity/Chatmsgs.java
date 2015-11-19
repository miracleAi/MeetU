package com.meetu.entity;
/**
 * 聊天消息实体
 * @author Administrator
 *
 */

public class Chatmsgs {
	/**
	 * 消息缓存id
	 */
	private String messageCacheId;
	/**
	 * 用户id
	 * */
	private String uid;
	/**
	 * 消息id
	 */
	private String messageId;
	/**
	 * 消息发送者id
	 */
	private String clientId;
	/**
	 * 消息对话id
	 */
	private String conversationId;
	/**
	 * 消息类型id
	 */
	private int chatMsgType;
	/**
	 * 消息的方向
	 */
	private int chatMsgDirection;
	/**
	 * 消息的状态
	 */
	private int chatMsgStatus;
	/**
	 * 是否显示时间
	 */
	private int isShowTime ;//是否显示时间
	/**
	 * 消息发送时间
	 */
	private String sendTimeStamp;
	/**
	 * 消息接收时间
	 */
	private String deliveredTimeStamp;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 图片消息的图片url
	 */
	private String imgMsgImageUrl;
	/**
	 *图片消息中图片的宽
	 */
	private int imgMsgImageWidth;
	/**
	 *图片消息中图片的高
	 */
	private int imgMsgImageHeight;
	/**
	 * 新加入成员的id
	 */
	private String nowJoinUserId;
	/**
	 * 纸条ID
	 * */
	private String scriptId;
	/**
	 * 纸条坐标X
	 * */
	private int scripX;
	/**
	 * 纸条坐标Y
	 * */
	private int scripY;
	//以下没有用
	/**
	 * 文本通知消息的内容
	 */
	private String notificationBaseContent;
	/**
	 * 活动反馈通知消息的内容
	 */
	private String notificationActyContent;
	/**
	 * 活动反馈通知消息的标题
	 */
	private String notificationActyTitle;
	/**
	 * 活动反馈通知消息的子标题
	 */
	private String notificationActyTitleSub;
	
	public String getMessageCacheId() {
		return messageCacheId;
	}
	public void setMessageCacheId(String messageCacheId) {
		this.messageCacheId = messageCacheId;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public int getChatMsgType() {
		return chatMsgType;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public void setChatMsgType(int chatMsgType) {
		this.chatMsgType = chatMsgType;
	}
	public int getChatMsgDirection() {
		return chatMsgDirection;
	}
	public void setChatMsgDirection(int chatMsgDirection) {
		this.chatMsgDirection = chatMsgDirection;
	}
	public int getChatMsgStatus() {
		return chatMsgStatus;
	}
	public void setChatMsgStatus(int chatMsgStatus) {
		this.chatMsgStatus = chatMsgStatus;
	}
	public int getIsShowTime() {
		return isShowTime;
	}
	public void setIsShowTime(int isShowTime) {
		this.isShowTime = isShowTime;
	}
	public String getSendTimeStamp() {
		return sendTimeStamp;
	}
	public void setSendTimeStamp(String sendTimeStamp) {
		this.sendTimeStamp = sendTimeStamp;
	}
	public String getDeliveredTimeStamp() {
		return deliveredTimeStamp;
	}
	public void setDeliveredTimeStamp(String deliveredTimeStamp) {
		this.deliveredTimeStamp = deliveredTimeStamp;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImgMsgImageUrl() {
		return imgMsgImageUrl;
	}
	public void setImgMsgImageUrl(String imgMsgImageUrl) {
		this.imgMsgImageUrl = imgMsgImageUrl;
	}
	public int getImgMsgImageWidth() {
		return imgMsgImageWidth;
	}
	public void setImgMsgImageWidth(int imgMsgImageWidth) {
		this.imgMsgImageWidth = imgMsgImageWidth;
	}
	public int getImgMsgImageHeight() {
		return imgMsgImageHeight;
	}
	public void setImgMsgImageHeight(int imgMsgImageHeight) {
		this.imgMsgImageHeight = imgMsgImageHeight;
	}
	public String getNowJoinUserId() {
		return nowJoinUserId;
	}
	public void setNowJoinUserId(String nowJoinUserId) {
		this.nowJoinUserId = nowJoinUserId;
	}
	public String getNotificationBaseContent() {
		return notificationBaseContent;
	}
	public void setNotificationBaseContent(String notificationBaseContent) {
		this.notificationBaseContent = notificationBaseContent;
	}
	public String getNotificationActyContent() {
		return notificationActyContent;
	}
	public void setNotificationActyContent(String notificationActyContent) {
		this.notificationActyContent = notificationActyContent;
	}
	public String getNotificationActyTitle() {
		return notificationActyTitle;
	}
	public void setNotificationActyTitle(String notificationActyTitle) {
		this.notificationActyTitle = notificationActyTitle;
	}
	public String getNotificationActyTitleSub() {
		return notificationActyTitleSub;
	}
	public void setNotificationActyTitleSub(String notificationActyTitleSub) {
		this.notificationActyTitleSub = notificationActyTitleSub;
	}
	
	public String getScriptId() {
		return scriptId;
	}
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}
	public int getScripX() {
		return scripX;
	}
	public void setScripX(int scripX) {
		this.scripX = scripX;
	}
	public int getScripY() {
		return scripY;
	}
	public void setScripY(int scripY) {
		this.scripY = scripY;
	}
	public Chatmsgs(String messageCacheId, String messageId, String clientId,
			String conversationId, int chatMsgType, int chatMsgDirection,
			int chatMsgStatus, int isShowTime, String sendTimeStamp,
			String deliveredTimeStamp, String content, String imgMsgImageUrl,
			int imgMsgImageWidth, int imgMsgImageHeight, String nowJoinUserId,
			String notificationBaseContent, String notificationActyContent,
			String notificationActyTitle, String notificationActyTitleSub) {
		super();
		this.messageCacheId = messageCacheId;
		this.messageId = messageId;
		this.clientId = clientId;
		this.conversationId = conversationId;
		this.chatMsgType = chatMsgType;
		this.chatMsgDirection = chatMsgDirection;
		this.chatMsgStatus = chatMsgStatus;
		this.isShowTime = isShowTime;
		this.sendTimeStamp = sendTimeStamp;
		this.deliveredTimeStamp = deliveredTimeStamp;
		this.content = content;
		this.imgMsgImageUrl = imgMsgImageUrl;
		this.imgMsgImageWidth = imgMsgImageWidth;
		this.imgMsgImageHeight = imgMsgImageHeight;
		this.nowJoinUserId = nowJoinUserId;
		this.notificationBaseContent = notificationBaseContent;
		this.notificationActyContent = notificationActyContent;
		this.notificationActyTitle = notificationActyTitle;
		this.notificationActyTitleSub = notificationActyTitleSub;
	}
	public Chatmsgs() {
		super();
	}
	


}
