package com.meetu.bean;

public class CoversationUserBean {
	//我的ID
	private String idMine;
	//会话ID
	private String idConversation;
	//附加ID（活动ID，觅聊ID）
	private String idConvAppend;
	//会话创建者ID
	private String idConvCreator;
	//会话状态
	private int status;
	//会话类型
	private int type;
	//标题
	private String title;
	//静音标记
	private int mute;
	//是否拒绝接收消息
	private int refuseMsg;
	//会话未读条数
	private int unReadCount;
	//会话结束时间
	private long overTime;
	//最后更新时间
	private long updateTime;
	
	public int getRefuseMsg() {
		return refuseMsg;
	}
	public void setRefuseMsg(int refuseMsg) {
		this.refuseMsg = refuseMsg;
	}
	public int getUnReadCount() {
		return unReadCount;
	}
	public void setUnReadCount(int unReadCount) {
		this.unReadCount = unReadCount;
	}
	public String getIdMine() {
		return idMine;
	}
	public void setIdMine(String idMine) {
		this.idMine = idMine;
	}
	public String getIdConversation() {
		return idConversation;
	}
	public void setIdConversation(String idConversation) {
		this.idConversation = idConversation;
	}
	public String getIdConvAppend() {
		return idConvAppend;
	}
	public void setIdConvAppend(String idConvAppend) {
		this.idConvAppend = idConvAppend;
	}
	public String getIdConvCreator() {
		return idConvCreator;
	}
	public void setIdConvCreator(String idConvCreator) {
		this.idConvCreator = idConvCreator;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getMute() {
		return mute;
	}
	public void setMute(int mute) {
		this.mute = mute;
	}
	public long getOverTime() {
		return overTime;
	}
	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
}
