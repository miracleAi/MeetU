package com.meetu.common;

public class Constants {
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
	//用户ID
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
}
