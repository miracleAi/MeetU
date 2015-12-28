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
	public  String mineId;

	/**
	 * 成员ID
	 */
	public  String memberSeekId ;
	/**
	 * 觅聊ID
	 */
	public  String conversationId;
	/**
	 * 成员状态
	 */
	public  String convStatus;
	public String getMineId() {
		return mineId;
	}
	public void setMineId(String mineId) {
		this.mineId = mineId;
	}
	public String getMemberSeekId() {
		return memberSeekId;
	}
	public void setMemberSeekId(String memberSeekId) {
		this.memberSeekId = memberSeekId;
	}
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public String getConvStatus() {
		return convStatus;
	}
	public void setConvStatus(String convStatus) {
		this.convStatus = convStatus;
	}
	

}
