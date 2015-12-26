package com.meetu.common;

public class DbConstents {
	/**
	 * 数据库名
	 * */
	public static final String DBNAME = "meetu.db";
	//用户本人ID
	public static String ID_MINE = "id_mine";
	/***
	 * 活动成员列表
	 */
	public static String MEMBER_ACTY_TB = "member_activity_tb";
	//主键
	public static String ID_MINE_MEMBER_ACTY = "id_mine_member_acty";
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
	//主键
	public static String ID_MINE_MEMBER_SEEK = "id_mine_member_seek";
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
	//主键
	public static String ID_CACHE_MSG = "id_cache_msg";
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
	//主键
	public static String ID_MINE_CONVERSATION = "id_mine_conversation";
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

	/**
	 * 用户表
	 * */ 
	public static final String USERINFO_TB = "userinfo_tb";
	// 用户类型
	public static final String USERTYPE = "user_type";
	// 用户是否是Vip用户
	public static final String ISVIP = "is_vip";
	// 用户是否已完成验证
	public static final String ISVERIFY = "is_verify";
	// 用户是否已完善个人信息
	public static final String ISCOMPLETE = "is_complete";
	// 昵称
	public static final String NICKNAME = "nick_name";
	// 真实名称
	public static final String REALNAME = "real_name";
	// 性别
	public static final String GENDER = "gender";
	// 生日
	public static final String BIRTHDAY = "birthday";
	// 星座
	public static final String CONSTELLATION = "constellation";
	// 学校
	public static final String SCHOOL = "school";
	// 学校所在地编码
	public static final String SCHOOLLOCATION = "shool_location";
	// 学校编码
	public static final String SCHOOLNUM = "school_num";
	// 专业
	public static final String DEPARTMENT = "department";
	// 专业编码
	public static final String DEPARTMENTID = "department_id";
	// 家乡
	public static final String HOMETOWN = "hometown";
	// 切圆后的头像
	public static final String PROFILECLIP = "profile_clip";
	// 原始头像
	public static final String PROFILEORIGN = "profile_orign";
	// 用户信息缓存时间
	public static final String USER_CACHE_TIME = "user_cache_time";
	/**
	 * 用户拉黑表，存储拉黑我的人
	 * */
	public static final String USER_SHIELD_TB = "user_shield_tb";
	//屏蔽我的人的ID
	public static final String SHIELD_USER_ID = "shield_user_id";
	/**
	 * 用户集合相关(优化后，活动成员和觅聊成员剥离，此表只用于存储关注)
	 * */
	public static final String USERABOUT_CACHE_TB = "userabout_cache_tb";
	//主键
	public static final String ABOUTCACHEID="about_cache_id";
	// 类型ID
	public static final String ABOUTTYPE = "about_type";
	// 相关用户ID
	public static final String ABOUTUSERID = "about_user_id";
	// 集合ID,用于存储活动ID--活动参与者 会话ID--会话成员
	public static final String ABOUTCOLECTIONID = "about_conversation_id";
	/**
	 * 活动缓存表表名
	 * */
	public static final String ACTIVITY_CACHE_TB = "activity_cache_tb";
	// 活动ID
	public static final String ACTIVITYID = "activity_id";
	// 是否对活动点赞
	public static final String ISACTIVITYPRAISE = "is_activity_praise";
	// 参加活动人中我关注的人
	public static final String ACTIVITYFOLLOWCOUNT = "activity_follow_count";
	// 活动封面
	public static final String ACTIVITYCOVER = "activity_cover";
	// 活动开始时间
	public static final String TIMESTART = "time_start";
	// 活动内容网页
	public static final String ACTIVITYCONTENT = "activity_content";
	// 活动点赞数
	public static final String PRAISECOUNT = "praise_count";
	// 参加活动的女生
	public static final String ORDERCOUNTGIRL = "order_count_girl";
	// 参加活动的男生
	public static final String ORDERCOUNTBOY = "order_count_boy";
	// 活动标题
	public static final String TITLE = "activity_title";
	// 活动子标题
	public static final String TITLESUB = "title_sub";
	// 活动状态
	public static final String STATUS = "activity_status";
	// 活动大概地址
	public static final String LOCATIONADDRESS = "location_address";
	// 活动详细地址
	public static final String LOCATIONPLACE = "location_place";
	// 活动行政地址
	public static final String LOCATIONGOVERNMENT = "location_government";
	// 活动经度
	public static final String LOCATIONLONGTITUDE = "location_longtitude";
	// 活动纬度
	public static final String LOCATIONLATITUDE = "location_latitude";
	// 活动结束时间
	public static final String TIMESTOP = "time_stop";
	//活动群聊结束时间
	public static final String TIMECHATSTOP = "time_chat_stop";
	// 活动群聊ID
	public static final String CONVERSATIONID = "conversation_id";
	// 活动index
	public static final String ACTIVITYINDEX = "activity_index";
	// 是否对点赞 和参加活动关注的人 缓存了
	public static final String ISCACHEFLAG = "is_cache_flag";
}
