package com.meetu.bean;

import com.meetu.cloud.object.ObjUser;

public class BarrageMsgBean {
	String id;
	String content;
	// 发送者ID
	String userId;
	// 消息发送时间（或者默认消息附加信息）
	String time;
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
}
