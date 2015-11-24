package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;
@AVClassName("ObjAuthoriseApply")
public class ObjAuthoriseApply extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 *  请求认证用户
	 */
	public static final String USER = "user";
	/**
	 *  请求认证分类
	 */
	public static final String CATEGORY = "category";
	/**
	 *  请求内容
	 */
	public static final String ARGUMENT = "argument";
	/**
	 *  管理员是否已查看
	 */
	public static final String FRESHSTATUS = "freshStatus";
	/**
	 *  请求答复内容
	 */
	public static final String APPLYREPLY = "applyReply";
	/**
	 *  请求结果
	 */
	public static final String APPLYRESULT = "applyResult";
	
	public ObjAuthoriseApply() {
		// TODO Auto-generated constructor stub
	}
	public ObjUser getUser() {
		return this.getAVUser(USER, ObjUser.class);
	}
	public void setUser(ObjUser user) {
		this.put(USER, user);;
	}
	public ObjAuthoriseCategory getCategory() {
		return this.getAVObject(CATEGORY);
	}
	public void setCategory(ObjAuthoriseCategory category) {
		this.put(CATEGORY, category);
	}
	public String getArgument() {
		return this.getString(ARGUMENT);
	}
	public void setArgument(String argument) {
		this.put(ARGUMENT, argument);
	}
	public boolean getFreshStatus() {
		return this.getBoolean(FRESHSTATUS);
	}
	public void setFreshStatus(boolean freshStatus) {
		this.put(FRESHSTATUS, freshStatus);
	}
	public String getApplyReply() {
		return this.getString(APPLYREPLY);
	}
	public void setApplyReply(String applyReply) {
		this.put(applyReply, applyReply);
	}
	public int getApplyResult() {
		return this.getInt(APPLYRESULT);
	}
	public void setApplyResult(int applyResult) {
		this.put(APPLYRESULT, applyResult);
	}
	
}
