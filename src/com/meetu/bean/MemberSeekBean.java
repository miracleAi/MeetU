package com.meetu.bean;

/**
 * @author lucifer
 * @date 2015-12-26
 * @return
 */
public class MemberSeekBean {
	/**
	 * 用户本人ID
	 */
	public static String mineId;

	/**
	 * 成员ID
	 */
	public static String memberSeekId ;
	/**
	 * 觅聊ID
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
		MemberSeekBean.mineId = mineId;
	}
	public static String getMemberSeekId() {
		return memberSeekId;
	}
	public static void setMemberSeekId(String memberSeekId) {
		MemberSeekBean.memberSeekId = memberSeekId;
	}
	public static String getConversationId() {
		return conversationId;
	}
	public static void setConversationId(String conversationId) {
		MemberSeekBean.conversationId = conversationId;
	}
	public static String getConvStatus() {
		return convStatus;
	}
	public static void setConvStatus(String convStatus) {
		MemberSeekBean.convStatus = convStatus;
	}

}
