package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;
@AVClassName("ObjUserPhotoPraise")
public class ObjUserPhotoPraise extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 *  点赞用户
	 */
	public static final String PRAISERUSER = "praiseUser";
	/**
	 *  被点赞照片
	 */
	public static final String USERPHORO = "userPhoto";
	
	public ObjUserPhotoPraise() {
		// TODO Auto-generated constructor stub
	}
	public ObjUser getPraiseUser() {
		return this.getAVUser(PRAISERUSER, ObjUser.class);
	}
	public void setPraiseUser(ObjUser praiseUser) {
		this.put(PRAISERUSER, praiseUser);;
	}
	public ObjUserPhoto getUserPhoto() {
		return this.getAVObject(USERPHORO);
	}
	public void setUserPhoto(ObjUserPhoto userPhoto) {
		this.put(USERPHORO, userPhoto);;
	}
	
}
