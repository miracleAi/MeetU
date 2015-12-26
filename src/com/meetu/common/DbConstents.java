package com.meetu.common;

public class DbConstents {
	//用户本人ID
	public static String ID_MINE = "id_mine";
	/***
	 * 活动成员列表
	 */
	public static String MEMBER_ACTY_TB = "member_activity_tb";
	//成员ID
	public static String ID_ACTY_MEMBER = "id_member";
	//活动ID
	public static String ID_ACTY = "id_activity";
	//活动群聊ID
	public static String ID_ACTY_CONVERSATION = "id_conversation";
	//成员状态
	public static String STATUS_ACTY_CONV = "status_conv";

	/***
	 * 觅聊成员表
	 */
	public static String MEMBER_SEEK_TB = "member_seek_tb";
	//成员ID
	public static String ID_SEEK_MEMBER = "id_member";
	//觅聊ID
	public static String ID_SEEK_CONVERSATION = "id_conversation";
	//成员状态
	public static String STATUS_SEEK_CONV = "status_conv";
	
	/***
	 * 聊天消息表
	 * */
	public static String MSG_CHAT_TB = "msg_chat_tb";
	//消息ID
	public static String ID_MESSAGE = "id_message";
	//发送者ID
	public static String ID_CLIENT = "id_client";
	//消息发送时间标记
	public static String SEND_TIME_STAMP = "send_time_stamp";
	//文本消息内容
	public static String MSG_TEXT = "msg_text";
	//图片消息URL
	public static String MSG_FILE_URL = "file_url";
	//聊天图片宽度
	public static String MSG_IMG_HEIGH = "img_heigh";
	//聊天图片高度
	public static String MSG_IMG_WIDTH = "img_width";
	//消息属于的会话
	public static String ID_MSG_CONVERSATION = "id_conversation";
	//被操作者的ID（如加入，踢出等操作消息）
	public static String ID_OPERATED = "id_operated";
	//消息的类型
	public static String TYPE_MSG = "type_msg";
	//消息的方向(int  根据clientID判断)
	public static String DIRECTION_MSG = "direction_msg";
	//消息的状态
	public static String STATUS_MSG = "status_msg";
	//消息是否显示时间
	public static String IS_SHOW_TIME = "is_show_time";
	
	/**
	 * 用户-会话表
	 * */
	public static String CONVERSATION_USER_TB = "conversation_user_tb";
	//会话ID
	public static String ID_CONVERSATION = "id_conversation";
	//附加ID（活动ID，觅聊ID）
	public static String ID_CONV_APPEND = "id_append";
	//会话创建者ID
	public static String ID_CONV_CREATOR = "id_conv_creator";
	//会话状态
	public static String STATUS_CONV = "status";
	//会话类型
	public static String TYPE_CONV = "type";
	//标题
	public static String TITLE_CONV = "title";
	//静音标记
	public static String CONV_MUTE = "mute";
	//会话结束时间
	public static String CONV_OVER_TIME = "over_time";
	//最后更新时间
	public static String CONV_UPDATE_TIME = "update_time";
}
