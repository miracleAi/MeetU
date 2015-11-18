package com.meetu.bean;

import com.meetu.cloud.object.ObjUser;

public class BarrageMsgBean {
	String content;
	String userId;
	ObjUser user;
	String time;
	long sendTime;
	
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
