package com.meetu.cloud.object;

import java.io.Serializable;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjSysMsg")
public class ObjSysMsg extends AVObject implements Serializable{
	public static final Creator CREATOR = AVObjectCreator.instance;
	// 对方用户
	public static final String TOWARDSUSER = "towardsUser";
	// 文本内容
	public static final String CONTENT = "content";
	// 消息类型
	public static final String MSGTYPE = "msgType";
	// 被点赞用户照片
	public static final String USERPHOTO = "userPhoto";
	// 用户
	public static final String USER = "user";
	// 活动
	public static final String ACTY = "acty";

	public ObjUser getTowardsUser() {
		return this.getAVUser(TOWARDSUSER, ObjUser.class);
	}

	public void setTowardsUser(ObjUser towardsUser) {
		this.put(TOWARDSUSER, towardsUser);
	}

	public String getContent() {
		return this.getString(CONTENT);
	}

	public void setContent(String content) {
		this.put(CONTENT, content);
	}

	public int getMsgType() {
		return this.getInt(MSGTYPE);
	}

	public void setMsgType(int msgType) {
		this.put(MSGTYPE, msgType);
	}

	public ObjUserPhoto getUserPhoto() {
		return this.getAVObject(USERPHOTO);
	}

	public void setUserPhoto(ObjUserPhoto userPhoto) {
		this.put(USERPHOTO, userPhoto);
		;
	}

	public ObjUser getUser() {
		return this.getAVUser(USER, ObjUser.class);
	}

	public void setUser(ObjUser user) {
		this.put(USER, user);
		;
	}

	public ObjActivity getActy() {
		return this.getAVObject(ACTY);
	}

	public void setActy(ObjActivity acty) {
		this.put(ACTY, acty);
		;
	}

}
