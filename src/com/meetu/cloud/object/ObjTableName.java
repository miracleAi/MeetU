package com.meetu.cloud.object;

public class ObjTableName {
	//获取用户表名
	public static String objName(){
		return "_User";
	}
	//活动表名
	public static String getActivityTbName(){
		return "ObjActivity";
	}
	//活动报名表
	public static String getActivitySignUpTb(){
		return "ObjActivityOrder";
	}
	//活动点赞表
	public static String getActivityFavorTb(){
		return "ObjActivityPraise";
	}
	//A关注B的
	public static String getMyFollowTb(){
		return "_Followee";
	}
	//活动封面
	public static String getActivityCoverTb(){
		return "ObjActivityCover";
	}
	//活动点赞
	public static String getPhotoPraiseTb(){
		return "ObjActivityPhotoPraise";
	}
	//用户照片点赞
	public static String getUserPhotoPraiseTb(){
		return "ObjUserPhotoPraise";
	}
	//用户照片
	public static String getUserPhotoTb(){
		return "ObjUserPhoto";
	}
}
