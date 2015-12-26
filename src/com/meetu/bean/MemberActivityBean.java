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
	public static String mineId;
	/**
	 * 成员ID
	 */
	public static String memberId;
	/**
	 * 活动ID
	 */
	public static String activityId;
	/**
	 * 活动群聊ID
	 */
	public static String conversationId;
	/**
	 * 成员状态
	 */
	public static String convStatus;
	public static String getMineId() {
		return mineId;
	}
	public static void setMineId(String mineId) {
		MemberActivityBean.mineId = mineId;
	}
	public static String getMemberId() {
		return memberId;
	}
	public static void setMemberId(String memberId) {
		MemberActivityBean.memberId = memberId;
	}
	public static String getActivityId() {
		return activityId;
	}
	public static void setActivityId(String activityId) {
		MemberActivityBean.activityId = activityId;
	}
	public static String getConversationId() {
		return conversationId;
	}
	public static void setConversationId(String conversationId) {
		MemberActivityBean.conversationId = conversationId;
	}
	public static String getConvStatus() {
		return convStatus;
	}
	public static void setConvStatus(String convStatus) {
		MemberActivityBean.convStatus = convStatus;
	}
	
	

}
