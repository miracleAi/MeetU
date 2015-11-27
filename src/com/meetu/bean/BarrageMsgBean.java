package com.meetu.bean;

import com.meetu.cloud.object.ObjUser;

public class BarrageMsgBean {
	String id;
	String content;
	String userId;
	ObjUser user;
	String time;
	long sendTime;
	String userAvator;
	String nickName;
	
	public String getUserAvator() {
		return userAvator;
	}
	public void setUserAvator(String userAvator) {
		this.userAvator = userAvator;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public ObjUser getUser() {
		return user;
	}
	public void setUser(ObjUser user) {
		this.user = user;
	}
	
}
