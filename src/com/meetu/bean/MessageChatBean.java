package com.meetu.bean;

public class MessageChatBean {
	//消息ID
	private String idMessage;
	//发送者ID
	private String idClient;
	//消息发送时间标记
	private long sendTimeStamp;
	//文本消息内容
	private String msgText;
	//图片消息URL
	private String fileUrl;
	//聊天图片宽度
	private int imgHeight;
	//聊天图片高度
	private int imgWidth;
	//消息属于的会话
	private String idConversation;
	//被操作者的ID（如加入，踢出等操作消息）
	private String idOperated;
	//消息的类型
	private int typeMsg;
	//消息的方向(int  根据clientID判断)
	private int directionMsg;
	//消息的状态
	private int statusMsg;
	//消息是否显示时间
	private int isShowTime;
	
	public String getIdMessage() {
		return idMessage;
	}
	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
	}
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public long getSendTimeStamp() {
		return sendTimeStamp;
	}
	public void setSendTimeStamp(long sendTimeStamp) {
		this.sendTimeStamp = sendTimeStamp;
	}
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public int getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}
	public int getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}
	public String getIdConversation() {
		return idConversation;
	}
	public void setIdConversation(String idConversation) {
		this.idConversation = idConversation;
	}
	public String getIdOperated() {
		return idOperated;
	}
	public void setIdOperated(String idOperated) {
		this.idOperated = idOperated;
	}
	public int getTypeMsg() {
		return typeMsg;
	}
	public void setTypeMsg(int typeMsg) {
		this.typeMsg = typeMsg;
	}
	public int getDirectionMsg() {
		return directionMsg;
	}
	public void setDirectionMsg(int directionMsg) {
		this.directionMsg = directionMsg;
	}
	public int getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(int statusMsg) {
		this.statusMsg = statusMsg;
	}
	public int getIsShowTime() {
		return isShowTime;
	}
	public void setIsShowTime(int isShowTime) {
		this.isShowTime = isShowTime;
	}
	
}
