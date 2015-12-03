package com.meetu.bean;

public class UserAboutBean {

	private String userId;// 用户id
	private int aboutType;// 类型 1我关注的人 2回话成员 3活动成员
	private String aboutUserId;// 所有人的id
	private String aboutColetctionId;// 对话id

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getAboutType() {
		return aboutType;
	}

	public void setAboutType(int aboutType) {
		this.aboutType = aboutType;
	}

	public String getAboutUserId() {
		return aboutUserId;
	}

	public void setAboutUserId(String aboutUserId) {
		this.aboutUserId = aboutUserId;
	}

	public String getAboutColetctionId() {
		return aboutColetctionId;
	}

	public void setAboutColetctionId(String aboutColetctionId) {
		this.aboutColetctionId = aboutColetctionId;
	}

}
