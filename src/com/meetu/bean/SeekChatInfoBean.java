package com.meetu.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeekChatInfoBean implements Serializable{
	//操作是否需要权限
	private boolean needAuthorise;
	//权限分类ID
	private String authoriseCategoryId;
	//用户有没有申请过权限
	private boolean isApply;
	//用户权限状态：0未查看 1拒绝 2通过申请
	private int applyResult;
	//权限申请ID
	private String applyId;
	//是否有新状态
	private boolean freshStatus;
	//申请回复
	private String applyReply;
	//提交时填写的问题
	private String argument;
	//觅聊数目
	private int seekChatCount;
	//觅聊列表
	private List<Map<String, Object>> chatList;
	
	public boolean getNeedAuthorise() {
		return needAuthorise;
	}
	public void setNeedAuthorise(boolean needAuthorise) {
		this.needAuthorise = needAuthorise;
	}
	public String getAuthoriseCategoryId() {
		return authoriseCategoryId;
	}
	public void setAuthoriseCategoryId(String authoriseCategoryId) {
		this.authoriseCategoryId = authoriseCategoryId;
	}
	public boolean getIsApply() {
		return isApply;
	}
	public void setIsApply(boolean isApply) {
		this.isApply = isApply;
	}
	public int getApplyResult() {
		return applyResult;
	}
	public void setApplyResult(int applyResult) {
		this.applyResult = applyResult;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public boolean getFreshStatus() {
		return freshStatus;
	}
	public void setFreshStatus(boolean freshStatus) {
		this.freshStatus = freshStatus;
	}
	public String getApplyReply() {
		return applyReply;
	}
	public void setApplyReply(String applyReply) {
		this.applyReply = applyReply;
	}
	public String getArgument() {
		return argument;
	}
	public void setArgument(String argument) {
		this.argument = argument;
	}
	public int getSeekChatCount() {
		return seekChatCount;
	}
	public void setSeekChatCount(int seekChatCount) {
		this.seekChatCount = seekChatCount;
	}
	public List<Map<String, Object>> getChatList() {
		return chatList;
	}
	public void setChatList(List<Map<String, Object>> chatList) {
		this.chatList = chatList;
	}
	
}
