package com.meetu.cloud.object;

import java.io.Serializable;
import java.util.ArrayList;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

@AVClassName("ObjChat")
public class ObjChat extends AVObject implements Serializable {
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 * 觅聊创建用户
	 */
	public static final String USER = "user";
	/**
	 * 觅聊状态
	 */
	public static final String CHATSTATUS = "chatStatus";
	/**
	 * 觅聊标题
	 */
	public static final String CHATTITLE = "chatTitle";
	/**
	 * 觅聊封面（图片）
	 */
	public static final String CHATPICTURE = "chatPicture";
	/**
	 * 觅聊的对话ID（只读）
	 */
	public static final String CONVERSATIONID = "conversationId";
	/**
	 * 觅聊结束时间（前台计算所得）
	 */
	private static final String TIMECHATSTOP = "timeChatStop";
	/**
	 * 觅聊成员列表
	 * */
	private static final String MEMBER = "member";
	/**
	 * 觅聊成员数
	 * */
	private static final String MEMBERCOUNT = "memberCount";

	public ObjChat() {
		// TODO Auto-generated constructor stub
	}

	public ObjUser getUser() {
		return this.getAVUser(USER, ObjUser.class);
	}

	public void setUser(ObjUser user) {
		this.put(USER, user);
		;
	}

	public int getMember() {
		return this.getInt(MEMBER);
	}

	public void setMember(ArrayList<String> members) {
		this.put(MEMBER, members);
		;
	}

	public int getMemberCount() {
		return this.getInt(MEMBERCOUNT);
	}

	public void setMemberCount(int memberCount) {
		this.put(MEMBERCOUNT, memberCount);
	}

	public int getChatStatus() {
		return this.getInt(CHATSTATUS);
	}

	public void setChatStatus(int chatStatus) {
		this.put(CHATSTATUS, chatStatus);
		;
	}

	public String getChatTitle() {
		return this.getString(CHATTITLE);
	}

	public void setChatTitle(String chatTitle) {
		this.put(CHATTITLE, chatTitle);
		;
	}

	public AVFile getChatPicture() {
		return this.getAVFile(CHATPICTURE);
	}

	public void setChatPicture(AVFile chatPicture) {
		this.put(CHATPICTURE, chatPicture);
	}

	public String getConversationId() {
		return this.getString(CONVERSATIONID);
	}

	public void setConversationId(String conversationId) {
		this.put(CONVERSATIONID, conversationId);
	}

	public long getTimeChatStop() {
		return this.getLong(TIMECHATSTOP);
	}

	public void setTimeChatStop(long timeChatStop) {
		this.put(TIMECHATSTOP, timeChatStop);
	}

}
