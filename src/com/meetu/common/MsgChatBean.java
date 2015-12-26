package com.meetu.common;

/**
 * @author lucifer
 * @date 2015-12-26
 * @return
 */
public class MsgChatBean {
	/**
	 * 用户本人ID
	 */
	public static String mineId;

	/**
	 * 消息ID
	 */
	public static String messageId;
	/**
	 * 发送者ID
	 */
	public static String clientId;
	/**
	 * 消息发送时间标记
	 */
	public static String sendTimeStamp;
	/**
	 * 文本消息内容
	 */
	public static String msgText;
	/**
	 * 图片消息URL
	 */
	public static String fileUrl;
	/**
	 * 聊天图片宽度
	 */
	public static String msgImgHeight = "img_heigh";
	/**
	 * 聊天图片高度
	 */
	public static String msgImgWidth = "img_width";
	/**
	 * 消息属于的会话
	 */
	public static String conversationId;
	/**
	 * 被操作者的ID（如加入，踢出等操作消息）
	 */
	public static String operatedID;
	/**
	 * 消息的类型
	 */
	public static String msgType;
	/**
	 * 消息的方向(int 根据clientID判断)
	 */
	public static String directionMsg;
	/**
	 * 消息的状态
	 */
	public static String statusMsg;
	/**
	 * 消息是否显示时间
	 */
	public static String isShowTime;

	public static String getMineId() {
		return mineId;
	}

	public static void setMineId(String mineId) {
		MsgChatBean.mineId = mineId;
	}

	public static String getMessageId() {
		return messageId;
	}

	public static void setMessageId(String messageId) {
		MsgChatBean.messageId = messageId;
	}

	public static String getClientId() {
		return clientId;
	}

	public static void setClientId(String clientId) {
		MsgChatBean.clientId = clientId;
	}

	public static String getSendTimeStamp() {
		return sendTimeStamp;
	}

	public static void setSendTimeStamp(String sendTimeStamp) {
		MsgChatBean.sendTimeStamp = sendTimeStamp;
	}

	public static String getMsgText() {
		return msgText;
	}

	public static void setMsgText(String msgText) {
		MsgChatBean.msgText = msgText;
	}

	public static String getFileUrl() {
		return fileUrl;
	}

	public static void setFileUrl(String fileUrl) {
		MsgChatBean.fileUrl = fileUrl;
	}

	public static String getMsgImgHeight() {
		return msgImgHeight;
	}

	public static void setMsgImgHeight(String msgImgHeight) {
		MsgChatBean.msgImgHeight = msgImgHeight;
	}

	public static String getMsgImgWidth() {
		return msgImgWidth;
	}

	public static void setMsgImgWidth(String msgImgWidth) {
		MsgChatBean.msgImgWidth = msgImgWidth;
	}

	public static String getConversationId() {
		return conversationId;
	}

	public static void setConversationId(String conversationId) {
		MsgChatBean.conversationId = conversationId;
	}

	public static String getOperatedID() {
		return operatedID;
	}

	public static void setOperatedID(String operatedID) {
		MsgChatBean.operatedID = operatedID;
	}

	public static String getMsgType() {
		return msgType;
	}

	public static void setMsgType(String msgType) {
		MsgChatBean.msgType = msgType;
	}

	public static String getDirectionMsg() {
		return directionMsg;
	}

	public static void setDirectionMsg(String directionMsg) {
		MsgChatBean.directionMsg = directionMsg;
	}

	public static String getStatusMsg() {
		return statusMsg;
	}

	public static void setStatusMsg(String statusMsg) {
		MsgChatBean.statusMsg = statusMsg;
	}

	public static String getIsShowTime() {
		return isShowTime;
	}

	public static void setIsShowTime(String isShowTime) {
		MsgChatBean.isShowTime = isShowTime;
	}

}
