package com.meetu.cloud.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjScripBox")
public class ObjScripBox extends AVObject implements Serializable {
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 * 数组，包含小纸条发送双方
	 */
	public static final String USERS = "users";
	/**
	 * 第一次发起者
	 */
	public static final String SENDER = "sender";
	/**
	 * 纸条桶内纸条的数量
	 */
	public static final String SCRIPCOUNT = "scripCount";
	/**
	 * 纸筒发送者未读纸条数
	 */
	public static final String SENDERUNREADCOUNT = "senderUnreadCount";
	/**
	 * 纸筒接收者未读纸条数
	 */
	public static final String RECEIVERUNREADCOUNT = "receiverUnreadCount";
	/**
	 * 小纸条聊天状态
	 */
	public static final String CHATSTATUS = "chatStatus";
	/**
	 * 申请开启聊天者
	 */
	public static final String APPLYUSER = "applyUser";
	/**
	 * 对话ID
	 */
	public static final String CONVERSATIONID = "conversationId";

	public ObjScripBox() {
		// TODO Auto-generated constructor stub
	}

	public List<ObjUser> getUsers() {
		return this.getList(USERS, ObjUser.class);
	}

	public void setUsers(List<ObjUser> users) {
		this.put(USERS, users);
	}

	public ObjUser getSender() {
		return this.getAVUser(SENDER, ObjUser.class);
	}

	public void setSender(ObjUser sender) {
		this.put(SENDER, sender);
	}

	public int getScripCount() {
		return this.getInt(SCRIPCOUNT);
	}

	public void setScripCount(int scripCount) {
		this.put(SCRIPCOUNT, scripCount);
	}

	public int getSenderUnreadCount() {
		return this.getInt(SENDERUNREADCOUNT);
	}

	public void setSenderUnreadCount(int senderUnreadCount) {
		this.put(SENDERUNREADCOUNT, senderUnreadCount);
	}

	public int getReceiverUnreadCount() {
		return this.getInt(RECEIVERUNREADCOUNT);
	}

	public void setReceiverUnreadCount(int receiverUnreadCount) {
		this.put(RECEIVERUNREADCOUNT, receiverUnreadCount);
	}

	public int getChatStatus() {
		return this.getInt(CHATSTATUS);
	}

	public void setChatStatus(int chatStatus) {
		this.put(CHATSTATUS, chatStatus);
	}

	public ObjUser getApplyUser() {
		return this.getAVUser(APPLYUSER, ObjUser.class);
	}

	public void setApplyUser(ObjUser applyUser) {
		this.put(APPLYUSER, applyUser);
	}

	public String getConversationId() {
		return this.getString(CONVERSATIONID);
	}

	public void setConversationId(String conversationId) {
		this.put(CONVERSATIONID, conversationId);
	}

}
