package com.meetu.bean;

/**
 * @author lucifer
 * @date 2015-12-26
 * @return
 */
public class MemberActivityBean {

	/**
	 * 用户本人ID
	 */
	public  String mineId;
	/**
	 * 成员ID
	 */
	public  String memberId;
	/**
	 * 活动ID
	 */
	public  String activityId;
	/**
	 * 活动群聊ID
	 */
	public  String conversationId;
	/**
	 * 成员状态
	 */
	public  int convStatus;
	public String getMineId() {
		return mineId;
	}
	public void setMineId(String mineId) {
		this.mineId = mineId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public int getConvStatus() {
		return convStatus;
	}
	public void setConvStatus(int convStatus) {
		this.convStatus = convStatus;
	}

	
	

}
