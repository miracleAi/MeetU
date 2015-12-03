package com.meetu.common;

import com.avos.avoscloud.AVFile;

public class Constants {
	//消息接收广播
	public static final String RECEIVE_MSG = "receive_msg";
	/**
	 * 上传头像相关
	 */
	//圆形头像文件名前缀
	public static final String HEAD_FILE_CIRCLE = "profileOrign_";
	//方形头像文件名前缀
	public static final String HEAD_FILE_RECT = "profileClip_";
	//头像图片类型
	public static final String IMG_TYPE = ".jpeg";

	/**
	 *  活动状态常量
	 */
	//未知状态
	public static final int ActyStatusUnknown = 0;
	// 活动未发布
	public static final int ActyStatusNotRelease = 10;
	//  活动取消
	public static final int ActyStatusCancel = 20;
	//  活动移除
	public static final int ActyStatusRemove = 30;
	//  活动进行中
	public static final int ActyStatusProcess = 40;
	//  正在报名
	public static final int ActyStatusSignUp = 50;
	//  报名结束
	public static final int ActyStatusSignUPOver = 60;
	//  活动结束
	public static final int ActyStatusOver = 70;
	/**
	 * 订单状态
	 */
	//  锁定
	public static final int intOrderStatusLock = 10;
	//  支付成功
	public static final int OrderStatusPaySuccess = 20;
	//  支付失败
	public static final int OrderStatusPayFail = 30;
	//  已签到
	public static final int OrderStatusArrive = 40;
	//  失效
	public static final int OrderStatusLoseEfficacy = 0;
	/**
	 * 数据库名
	 * */
	public  static final String DBNAME="meetu.db";
	/**
	 * 活动缓存表表名
	 * */
	public static final String ACTIVITY_CACHE_TB = "activity_cache_tb";
	/**
	 * 表字段常量名
	 * */
	//用户ID
	public static final String USERID = "user_id";
	//活动ID
	public static final String ACTIVITYID = "activity_id";
	//是否对活动点赞
	public static final String ISACTIVITYPRAISE = "is_activity_praise";
	//参加活动人中我关注的人
	public static final String ACTIVITYFOLLOWCOUNT = "activity_follow_count";
	//活动封面
	public static final String ACTIVITYCOVER = "activity_cover";
	//活动开始时间
	public static final String TIMESTART = "time_start";
	//活动内容网页
	public static final String ACTIVITYCONTENT = "activity_content";
	//活动点赞数
	public static final String PRAISECOUNT = "praise_count";
	//参加活动的女生
	public static final String ORDERCOUNTGIRL = "order_count_girl";
	//参加活动的男生
	public static final String ORDERCOUNTBOY = "order_count_boy";
	//活动标题
	public static final String TITLE = "activity_title";
	//活动子标题
	public static final String TITLESUB = "title_sub";
	//活动状态
	public static final String STATUS = "activity_status";
	//活动大概地址
	public static final String LOCATIONADDRESS = "location_address";
	//活动详细地址
	public static final String LOCATIONPLACE = "location_place";
	//活动行政地址
	public static final String LOCATIONGOVERNMENT = "location_government";
	//活动经度
	public static final String LOCATIONLONGTITUDE = "location_longtitude";
	//活动纬度
	public static final String LOCATIONLATITUDE = "location_latitude";
	//活动结束时间
	public static final String TIMESTOP = "time_stop";
	//活动群聊ID
	public static final String CONVERSATIONID = "conversation_id";
	//活动index
	public static final String ACTIVITYINDEX = "activity_index";
	//是否对点赞 和参加活动关注的人 缓存了
	public static final String ISCACHEFLAG="is_cache_flag";
	/**
	 * 活动信息缓存时间
	 * */
	//sharePrefence名
	public static final String ACTIVITY_CACHE_SP = "activity_cache_sp";
	//缓存时间标记
	public static final String ACTIVITY_CACHE_TIME = "activity_cache_time";
	/**
	 * 用户权限分类
	 * */
	public static final int GROUP_CREATE = 100;
	/**
	 * 消息类型常量与leancloud对应
	 * */
	public static final int TEXT_TYPE = -1;
	public static final int IMAGE_TYPE = -2;
	public static final int AUDIO_TYPE = -3;
	public static final int VEDIO_TYPE = -4;
	public static final int LOCATION_TYPE = -5;
	public static final int FILE_TYPE = -6;
	public static final int UNSUPPORT_TYPE = -7;

	/**
	 * *传到云端，必须与iOS保持一直,标记消息展现类型
	 */
	//文本
	public static final int SHOW_TEXT = 0;
	//图片
	public static final int SHOW_IMG = 1;
	//群成员变动
	public static final int SHOW_MEMBERCHANGE = 2;
	//普通通知
	public static final int SHOW_NOMAL_NOTIFY= 3;
	//活动反馈通知
	public static final int SHOW_ACTY_NOTY = 4;
	//小纸条
	public static final int SHOW_SCRIPT = 5;

	/**
	 * 文本 图片 区分发送接收方
	 * */
	//发送文本
	public static final int SHOW_SEND_TEXT = 10;
	//发送图片
	public static final int SHOW_SEND_IMG = 11;
	//接收文本
	public static final int SHOW_RECV_TEXT = 12;
	//接收图片
	public static final int SHOW_RECV_IMG = 13;
	/**
	 * 消息状态常量
	 * */
	public static final int STATUES_NONE = 1; //未知
	public static final int STATUES_SENDING = 2;//发送中
	public static final int STATUES_SENT = 3;//已发送
	public static final int STATUES_RECEIPT = 4;//被接收
	public static final int STATUES_FAILED = 5;//发送失败
	/**
	 * 消息方向常量
	 * */
	public static final int IOTYPE_IN = 1;
	public static final int IOTYPE_OUT = 2;
	/**
	 * 消息时间是否显示
	 * */
	public static final int TIMESHOW = 1;
	public static final int TIMESHOWNOT = 0;
	/**
	 * 对话类型
	 * */
	public static final int ACTYSG = 1;
	public static final int SEEKMSG = 2;
	public static final int SRIPTMSG = 3;
	/**
	 * 上传map key值常量
	 * */
	public static final String IS_SHOW_TIME = "IsShowTime";
	public static final String CHAT_MSG_TYPE = "ChatMsgType";
	//小纸条相关
	public static final String SCRIP_ID = "ScripId";
	public static final String SCRIP_X = "ScripX";
	public static final String SCRIP_Y = "ScripY";
	/**
	 * 用户集合类型
	 * */
	public static final int FOLLOW_TYPE =1;//我关注的人
	public static final int CONVERSATION_TYPE =2;//觅聊回话成员
	public static final int ACTIVITY_TYPE =3;//活动成员
	/**
	 * 用户集合相关
	 * */
	public static final String USERABOUT_CACHE_TB = "userabout_cache_tb";
	/**
	 * 表字段常量名
	 * */
	//类型ID
	public static final String ABOUTTYPE = "about_type";
	//相关用户ID
	public static final String ABOUTUSERID = "about_user_id";
	//集合ID,用于存储活动ID--活动参与者  会话ID--会话成员
	public static final String ABOUTCOLECTIONID = "about_conversation_id";

	/**
	 * 系统消息类型
	 * */
	//未知
	public static final int SysMsgTypeNone = 0;
	//文本
	public static final int SysMsgTypeText = 1;
	//关注
	public static final int SysMsgTypeFollow = 2;
	//关注的人参加活动
	public static final int SysMsgTypeActy = 3;
	//用户墙照片点赞
	public static final int SysMsgTypeUserPhoto = 4;
	/**
	 * 表字段常量名
	 * */
	//用户表名
	public static final String USERINFO_TB = "userinfo_tb";
	//用户类型
	public static final String USERTYPE = "user_type";
	//用户是否是Vip用户
	public static final String ISVIP = "is_vip";
	//用户是否已完成验证
	public static final String ISVERIFY = "is_verify";
	//用户是否已完善个人信息
	public static final String ISCOMPLETE = "is_complete";
	//昵称
	public static final String NICKNAME = "nick_name";
	//真实名称
	public static final String REALNAME = "real_name";
	//性别
	public static final String GENDER = "gender";
	//生日
	public static final String BIRTHDAY = "birthday";
	//星座
	public static final String CONSTELLATION = "constellation";
	//学校
	public static final String SCHOOL = "school";
	//学校所在地编码
	public static final String SCHOOLLOCATION = "shool_location";
	//学校编码
	public static final String SCHOOLNUM = "school_num";
	//专业
	public static final String DEPARTMENT = "department";
	//专业编码
	public static final String DEPARTMENTID = "department_id";
	//家乡
	public static final String HOMETOWN = "hometown";
	//切圆后的头像
	public static final String PROFILECLIP = "profile_clip";
	//原始头像
	public static final String PROFILEORIGN = "profile_orign";
	//用户信息缓存时间
	public static final String USER_CACHE_TIME = "user_cache_time";
	/**
	 * 用户拉黑表，存储拉黑我的人
	 * */
	//表名
	public static final String USER_SHIELD_TB = "user_shield_tb";
	//屏蔽我的人的ID
	public static final String SHIELD_USER_ID = "shield_user_id";
}
