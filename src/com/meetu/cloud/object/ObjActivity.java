package com.meetu.cloud.object;

import java.io.Serializable;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;
import com.avos.avoscloud.AVUser;
import com.meetu.common.Constants;

@AVClassName("ObjActivity")
public class ObjActivity extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 *  活动封面（图片）（只读）
	 */
	//AVFile activityCover;
	public static final String ACTYCOVER = "activityCover";
	/**
	 *  活动开始时间（UNIX时间戳，毫秒）（只读）
	 */
	//long timeStart;
	public static final String TIMESTART = "timeStart";
	/**
	 *  活动大概地址（只读）
	 */
	//String locationAddress;
	public static final String LOCATIONADDRESS = "locationAddress";
	/**
	 *  活动详情（网页）（只读）
	 */
	//AVFile activityContent;
	public static final String ACTYCONTENT = "activityContent";
	/**
	 *  点赞数量（只读）
	 */
	//int praiseCount;
	public static final String PRAISECOUNT = "praiseCount";
	/**
	 *  活动已报名的女孩数量（只读）
	 */
	//int orderCountGirl;
	public static final String ORDERCOUNGG = "orderCountGirl";
	/**
	 *  活动标题（只读）
	 */
	//String title;
	public static final String TITLE = "title";
	/**
	 *  活动已报名的男孩数量（只读）
	 */
	//int orderCountBoy;
	public static final String ORDERCOUNGB = "orderCountBoy";
	/**
	 *  活动地址标题（只读）
	 */
	//String locationTitle;
	public static final String LOCATIONTITLE = "locationTitle";
	/**
	 *  活动状态（只读）
	 */
	//int status;
	public static final String STATUS = "status";
	/**
	 *  活动详细地址（只读）
	 */
	//String locationPlace;
	public static final String LOCATIONPLACE = "locationPlace";
	/**
	 *  活动子标题（只读）
	 */
	//String titleSub;
	public static final String TITLESUB = "titleSub";
	/**
	 *  活动举办地的行政地址（只读）
	 */
	//String locationGovernment;
	public static final String LOCATIONGOV = "locationGovernment";
	/**
	 *  活动地址经度（只读）
	 */
	//int locationLongitude;
	public static final String LOCATIONLONGITUDE = "locationLongitude";
	/**
	 *  活动地址纬度（只读）
	 */
	//int locationLatitude;
	public static final String LOCATIONLATITUDE = "locationLatitude";
	/**
	 *  活动地址子标题（只读）
	 */
	//String locationSubtitle;
	public static final String LOCATIONSUBT = "locationSubtitle";
	/**
	 *  活动结束时间（UNIX时间戳，毫秒）（只读）
	 */
	//long timeStop;
	public static final String TIMESTOP = "orderCountBoy";
	/**
	 *  活动群聊的对话ID（只读）
	 */
	//String conversationId;
	public static final String CONVERSATIONID = "orderCountBoy";

	public ObjActivity() {
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return this.getInt(STATUS);
	}
	public String getTitle() {
		return this.getString(TITLE);
	}
	public String getTitleSub() {
		return this.getString(TITLESUB);
	}
	public String getLocationGovernment() {
		return this.getString(LOCATIONGOV);
	}
	public String getLocationAddress() {
		return this.getString(LOCATIONADDRESS);
	}
	public String getLocationPlace() {
		return this.getString(LOCATIONPLACE);
	}
	public int getLocationLongitude() {
		return this.getInt(LOCATIONLONGITUDE);
	}
	public int getLocationLatitude() {
		return this.getInt(LOCATIONLATITUDE);
	}
	public String getLocationTitle() {
		return this.getString(LOCATIONTITLE);
	}
	public String getLocationSubtitle() {
		return this.getString(LOCATIONSUBT);
	}
	public int getPraiseCount() {
		return this.getInt(PRAISECOUNT);
	}
	public long getTimeStart() {
		return this.getLong(TIMESTART);
	}
	public long getTimeStop() {
		return this.getLong(TIMESTOP);
	}
	public AVFile getActivityCover() {
		return this.getAVFile(ACTYCOVER);
	}
	public AVFile getActivityContent() {
		return this.getAVFile(ACTYCONTENT);
	}
	public int getOrderCountBoy() {
		return this.getInt(ORDERCOUNGB);
	}
	public int getOrderCountGirl() {
		return this.getInt(ORDERCOUNGG);
	}
	public String getConversationId() {
		return this.getString(CONVERSATIONID);
	}

	public static String getStatusStr(int status){
		String statusStr = "";
		switch (status) {
		case Constants.ActyStatusNotRelease:
			statusStr = "活动未发布";
			break;
		case Constants.ActyStatusSignUp: 
			statusStr = "火热报名中";
			break;
		case Constants.ActyStatusCancel:
			statusStr = "活动已取消";
			break;
		case Constants.ActyStatusSignUPOver:
			statusStr = "报名结束";
			break;
		case Constants.ActyStatusProcess: 
			statusStr = "活动进行中";
			break;
		case Constants.ActyStatusOver: 
			statusStr = "活动已结束";
			break;
		case Constants.ActyStatusRemove: 
			statusStr = "活动已移除";
			break;
		case Constants.ActyStatusUnknown:
			statusStr = "未知状态";
			break;
		default:
			break;
		}
		return statusStr;
	}
}
