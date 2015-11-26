package com.meetu.bean;

import java.util.ArrayList;
import java.util.HashMap;

import com.meetu.cloud.object.ObjUser;

public class SeekChatBean {

	//会话ID
	private String conversationId;
	//创建日期
	private String createAt;
	//创建者
	private ObjUser creator;
	//我关注的人数
	private int followeeCount;
	//觅聊成员
	private ArrayList<HashMap<String, Object>> members;
	//觅聊ID
	private String objectId;
	//图片URL
	private String pictureUrl;
	//结束日期
	private long timeChatStop;
	//觅聊标题
	private String title;
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public ObjUser getCreator() {
		return creator;
	}
	public void setCreator(ObjUser creator) {
		this.creator = creator;
	}
	public int getFolloweeCount() {
		return followeeCount;
	}
	public void setFolloweeCount(int followeeCount) {
		this.followeeCount = followeeCount;
	}
	public ArrayList<HashMap<String, Object>> getMembers() {
		return members;
	}
	public void setMembers(ArrayList<HashMap<String, Object>> members) {
		this.members = members;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public long getTimeChatStop() {
		return timeChatStop;
	}
	public void setTimeChatStop(long timeChatStop) {
		this.timeChatStop = timeChatStop;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
