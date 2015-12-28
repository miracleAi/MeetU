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
	private  String mineId;
	/**
	 * 觅聊 id
	 */
	private String seekId;

	/**
	 * 成员ID
	 */
	private  String memberSeekId ;
	/**
	 * 觅聊ID
	 */
	private  String conversationId;
	/**
	 * 成员状态
	 */
	private  String convStatus;
	public String getMineId() {
		return mineId;
	}
	public void setMineId(String mineId) {
		this.mineId = mineId;
	}
	public String getSeekId() {
		return seekId;
	}
	public void setSeekId(String seekId) {
		this.seekId = seekId;
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
