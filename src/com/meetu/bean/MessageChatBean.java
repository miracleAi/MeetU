package com.meetu.bean;

public class MessageChatBean {
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
	public static String STATUS_MSG = "status_msg";
	//消息是否显示时间
	private int isShowTime;
}
