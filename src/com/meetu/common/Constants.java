package com.meetu.common;

import com.avos.avoscloud.AVFile;

public class Constants {
	// 消息接收广播
	public static final String RECEIVE_MSG = "receive_msg";
	public static final String LOGIN_REGISTOR_FINISH = "loginOrRegistorFinish";
	public static final String MAIN_FINISH = "main_finish";
	/**
	 * 上传头像相关
	 */
	// 圆形头像文件名前缀
	public static final String HEAD_FILE_CIRCLE = "profileOrign_";
	// 方形头像文件名前缀
	public static final String HEAD_FILE_RECT = "profileClip_";
	// 头像图片类型
	public static final String IMG_TYPE = ".jpeg";

	/**
	 * 活动状态常量
	 */
	// 未知状态
	public static final int ActyStatusUnknown = 0;
	// 活动未发布
	public static final int ActyStatusNotRelease = 10;
	// 活动取消
	public static final int ActyStatusCancel = 20;
	// 活动移除
	public static final int ActyStatusRemove = 30;
	// 活动进行中
	public static final int ActyStatusProcess = 40;
	// 正在报名
	public static final int ActyStatusSignUp = 50;
	// 报名结束
	public static final int ActyStatusSignUPOver = 60;
	// 活动结束
	public static final int ActyStatusOver = 70;
	/**
	 * 订单状态
	 */
	// 锁定
	public static final int OrderStatusLock = 10;
	// 支付成功
	public static final int OrderStatusPaySuccess = 20;
	// 支付失败
	public static final int OrderStatusPayFail = 30;
	// 已签到
	public static final int OrderStatusArrive = 40;
	// 失效
	public static final int OrderStatusLoseEfficacy = 0;
	// 用户ID
	public static final String USERID = "id_mine";
	/**
	 * 活动信息缓存时间(待修改)
	 * */
	// sharePrefence名
	public static final String ACTIVITY_CACHE_SP = "activity_cache_sp";
	// 缓存时间标记
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
	// 文本
	public static final int SHOW_TEXT = 1;
	// 图片
	public static final int SHOW_IMG = 2;
	// 群成员变动
	public static final int SHOW_MEMBERCHANGE = 3;
	// 普通通知
	public static final int SHOW_NOMAL_NOTIFY = 4;
	// 活动反馈通知
	public static final int SHOW_ACTY_NOTY = 5;
	// 小纸条(保留，其他替换)
	public static final int SHOW_SCRIPT = 6;

	/**
	 * 文本 图片 区分发送接收方
	 * */
	// 发送文本
	public static final int SHOW_SEND_TEXT = 10;
	// 发送图片
	public static final int SHOW_SEND_IMG = 11;
	// 接收文本
	public static final int SHOW_RECV_TEXT = 12;
	// 接收图片
	public static final int SHOW_RECV_IMG = 13;
	// 自己加入提醒
	public static final int SHOW_SELF_CHANGE = 14;
	// 自己踢出提醒
	public static final int SHOW_SELF_DEL = 15;

	/**
	 * 消息状态常量
	 * */
	public static final int STATUES_NONE = 1; // 未知
	public static final int STATUES_SENDING = 2;// 发送中
	public static final int STATUES_SENT = 3;// 已发送
	public static final int STATUES_RECEIPT = 4;// 被接收
	public static final int STATUES_FAILED = 5;// 发送失败
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
	// 小纸条相关
	public static final String SCRIP_ID = "ScripId";
	public static final String SCRIP_X = "ScripX";
	public static final String SCRIP_Y = "ScripY";
	/**
	 * 用户集合类型
	 * */
	public static final int FOLLOW_TYPE = 1;// 我关注的人
	public static final int CONVERSATION_TYPE = 2;// 群聊聊天回话成员
	public static final int ACTIVITY_TYPE = 3;// 活动成员
	/**
	 * 系统消息类型
	 * */
	// 未知
	public static final int SysMsgTypeNone = 0;
	// 文本
	public static final int SysMsgTypeText = 1;
	// 关注
	public static final int SysMsgTypeFollow = 2;
	// 关注的人参加活动
	public static final int SysMsgTypeActy = 3;
	// 用户墙照片点赞
	public static final int SysMsgTypeUserPhoto = 4;
}
