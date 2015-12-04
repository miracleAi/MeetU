package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjReportChat")
public class ObjReportChat extends AVObject {
	public static final Creator CREATOR = AVObjectCreator.instance;
	public static String USER = "user";
	public static final String CHAT = "chat";
	public static final String APPEND = "append";
	public static final String REPORTCODE = "reportCode";

	public ObjReportChat() {
		// TODO Auto-generated constructor stub
	}

	public ObjUser getUser() {
		return this.getAVUser(USER, ObjUser.class);
	}

	public void setUser(ObjUser user) {
		this.put(USER, user);
	}

	public ObjChat getChat() {
		return this.getAVObject(CHAT);
	}

	public void setChat(ObjChat chat) {
		this.put(CHAT, chat);
	}

	public String getAppend() {
		return this.getString(APPEND);
	}

	public void setAppend(String append) {
		this.put(APPEND, append);
		;
	}

	public int getReportCode() {
		return this.getInt(REPORTCODE);
	}

	public void setReportCode(int reportCode) {
		this.put(REPORTCODE, reportCode);
	}

}
