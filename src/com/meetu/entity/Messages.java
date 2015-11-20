package com.meetu.entity;

public class Messages {
	/**
	 * 当前用户ID
	 * */
	private String userId;
	/**
	 * 对话ID
	 */
	private String conversationID;
	/**
	 * 对话类型
	 */
	private int conversationType;
	/**
	 * 对话创建者ID
	 */
	private String creatorID;
	/**
	 * 当前对话未读消息数
	 */
	private int unreadMsgCount;
	/**
	 * 结束时间，用于觅聊和活动群聊
	 */
	private long timeOver;
	/**
	 * 活动ID
	 */
	private String actyId;
	/**
	 * 活动名称，用于活动群聊
	 */
	private String actyName;
	/**
	 * 觅聊ID
	 */
	private String chatId;
	/**
	 * 觅聊名称，用于觅聊
	 */
	private String chatName;
	/**
	 * 标记用户是否被踢出群0未踢出，1已踢出
	 * */
	private int tiStatus;
	/**
	 * 对话成员数量
	 */
	private int personCount;
	/**
	 * 对话成员数量最后跟新时间
	 */
	private long personCountUpdate;
	/**
	 * 用户ID
	 */ 
	private String scripUserId;
	/**
	 * 用户昵称，用于单聊
	 */
	private String scripUserName;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getConversationID() {
		return conversationID;
	}
	public void setConversationID(String conversationID) {
		this.conversationID = conversationID;
	}
	public int getConversationType() {
		return conversationType;
	}
	public void setConversationType(int conversationType) {
		this.conversationType = conversationType;
	}
	public String getCreatorID() {
		return creatorID;
	}
	public void setCreatorID(String creatorID) {
		this.creatorID = creatorID;
	}
	public int getTiStatus() {
		return tiStatus;
	}
	public void setTiStatus(int tiStatus) {
		this.tiStatus = tiStatus;
	}
	public int getUnreadMsgCount() {
		return unreadMsgCount;
	}
	public void setUnreadMsgCount(int unreadMsgCount) {
		this.unreadMsgCount = unreadMsgCount;
	}
	public long getTimeOver() {
		return timeOver;
	}
	public void setTimeOver(long timeOver) {
		this.timeOver = timeOver;
	}
	public String getScripUserId() {
		return scripUserId;
	}
	public void setScripUserId(String scripUserId) {
		this.scripUserId = scripUserId;
	}
	public String getScripUserName() {
		return scripUserName;
	}
	public void setScripUserName(String scripUserName) {
		this.scripUserName = scripUserName;
	}
	public String getActyId() {
		return actyId;
	}
	public void setActyId(String actyId) {
		this.actyId = actyId;
	}
	public String getActyName() {
		return actyName;
	}
	public void setActyName(String actyName) {
		this.actyName = actyName;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public int getPersonCount() {
		return personCount;
	}
	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}
	public long getPersonCountUpdate() {
		return personCountUpdate;
	}
	public void setPersonCountUpdate(long personCountUpdate) {
		this.personCountUpdate = personCountUpdate;
	}
}
