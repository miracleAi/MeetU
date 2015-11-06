package com.meetu.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjFollowWrap;
import com.meetu.cloud.wrap.ObjPraiseWrap;
import com.meetu.common.Constants;

public class ActivityBean {

	//userid 缓存标记用户需要
	private String userId;
	//活动ID
	private String actyId;
	//是否点赞
	private boolean isFavor;
	//我关注并且参加活动的人数
	private int orderAndFollow;
	//  活动封面（图片）（只读）
	private String activityCover;
	//  活动开始时间（UNIX时间戳，毫秒）（只读）
	private long timeStart;
	//  活动详情（网页）（只读）
	private String activityContent;
	//  点赞数量（只读）
	private int praiseCount;
	//  活动已报名的女孩数量（只读）
	private int orderCountGirl;
	//  活动已报名的男孩数量（只读）
	private int orderCountBoy;
	//  活动标题（只读）
	private String title;
	//  活动子标题（只读）
	private String titleSub;
	//  活动状态（只读）
	private int status;
	//  活动大概地址（只读）
	private String locationAddress;
	//  活动详细地址（只读）
	private String locationPlace;
	//  活动举办地的行政地址（只读）
	private String locationGovernment;
	//  活动结束时间（UNIX时间戳，毫秒）（只读）
	private long timeStop;
	//  活动群聊的对话ID（只读）
	String conversationId;
	public void setFavor(boolean isFavor) {
		this.isFavor = isFavor;
	}

	public void setOrderAndFollow(int orderAndFollow) {
		this.orderAndFollow = orderAndFollow;
	}

	public boolean isFavor() {
		return isFavor;
	}
	public int getOrderAndFollow() {
		return orderAndFollow;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getActyId() {
		return actyId;
	}

	public void setActyId(String actyId) {
		this.actyId = actyId;
	}

	public String getActivityCover() {
		return activityCover;
	}

	public void setActivityCover(String activityCover) {
		this.activityCover = activityCover;
	}

	public long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public String getActivityContent() {
		return activityContent;
	}

	public void setActivityContent(String activityContent) {
		this.activityContent = activityContent;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public int getOrderCountGirl() {
		return orderCountGirl;
	}

	public void setOrderCountGirl(int orderCountGirl) {
		this.orderCountGirl = orderCountGirl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getOrderCountBoy() {
		return orderCountBoy;
	}

	public void setOrderCountBoy(int orderCountBoy) {
		this.orderCountBoy = orderCountBoy;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLocationPlace() {
		return locationPlace;
	}

	public void setLocationPlace(String locationPlace) {
		this.locationPlace = locationPlace;
	}

	public String getTitleSub() {
		return titleSub;
	}

	public void setTitleSub(String titleSub) {
		this.titleSub = titleSub;
	}

	public String getLocationGovernment() {
		return locationGovernment;
	}

	public void setLocationGovernment(String locationGovernment) {
		this.locationGovernment = locationGovernment;
	}

	public long getTimeStop() {
		return timeStop;
	}

	public void setTimeStop(long timeStop) {
		this.timeStop = timeStop;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
}
