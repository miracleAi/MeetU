package com.meetu.bean;

import com.meetu.cloud.object.ObjUser;

public class FavorBean {

	ObjUser user;
	long praiseTime;
	public ObjUser getUser() {
		return user;
	}
	public void setUser(ObjUser user) {
		this.user = user;
	}
	public long getPraiseTime() {
		return praiseTime;
	}
	public void setPraiseTime(long praiseTime) {
		this.praiseTime = praiseTime;
	}
	
}
