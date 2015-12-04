package com.meetu.cloud.object;

import java.io.Serializable;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjActivityOrder")
public class ObjActivityOrder extends AVObject {
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 * 提交订单用户（只读）
	 */
	public static final String USER = "user";
	/**
	 * 订单所属活动（只读）
	 */
	public static final String ACTIVITY = "activity";
	/**
	 * 订单的票（只读）
	 */
	public static final String TICKET = "ticket";
	/**
	 * 订单状态
	 */
	public static final String ORDERSTATUS = "orderStatus";
	/**
	 * 用户期望
	 */
	public static final String USEREXPECT = "userExpect";
	/**
	 * 用户性别
	 */
	public static final String USERGENDER = "userGender";
	/**
	 * 未知
	 */
	public static final String USERNO = "userNo";

	public ObjActivityOrder() {
		// TODO Auto-generated constructor stub
	}

	public ObjUser getUser() {
		return this.getAVUser(USER);
	}

	public ObjActivity getActivity() {
		return this.getAVObject(ACTIVITY);
	}

	public ObjActivityTicket getTicket() {
		return this.getAVObject(TICKET);
	}

	public int getOrderStatus() {
		return this.getInt(ORDERSTATUS);
	}

	public String getUserExpect() {
		return this.getString(USEREXPECT);
	}

	public int getUserGender() {
		return this.getInt(USERGENDER);
	}

	public int getUserNo() {
		return this.getInt(USERNO);
	}

	public void setUser(ObjUser user) {
		this.put(USER, user);
	}

	public void setActivity(ObjActivity activity) {
		this.put(ACTIVITY, activity);
	}

	public void setTicket(ObjActivityTicket ticket) {
		this.put(TICKET, ticket);
	}

	public void setOrderStatus(int status) {
		this.put(ORDERSTATUS, status);
	}

	public void setUserExpect(String expect) {
		this.put(USEREXPECT, expect);
	}

	public void setUserGender(int gender) {
		this.put(USERGENDER, gender);
	}

	public void setUserNo(int userNo) {
		this.put(USERNO, userNo);
	}
}
