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
	 * 活动查询相关handler 标记
	 */
	//查询是否点赞
	public static final int QUER_FAVOR_OK = 0;
	//查询参加活动的人和参加活动并且我关注的人完成
	public static final int QUER_ORDERFOLLOW_OK = 1;
	//查询封面完成
	public static final int QUER_ACTIVITYCOVER_OK = 2;
}
