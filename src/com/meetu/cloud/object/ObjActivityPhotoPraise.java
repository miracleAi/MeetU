package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjActivityPhotoPraise")
public class ObjActivityPhotoPraise extends AVObject {
	public static final Creator CREATOR = AVObjectCreator.instance;
	/**
	 * 点赞用户（只读）
	 */
	public static final String USER = "user";
	/**
	 * 被点赞的活动照片（只读）
	 */
	public static final String ACTIVITYPHOTO = "activityPhoto";

	public ObjActivityPhotoPraise() {
		// TODO Auto-generated constructor stub
	}

	public ObjUser getUser() {
		return this.getAVUser(USER);
	}

	public void setUser(ObjUser user) {
		this.put(USER, user);
		;
	}

	public ObjActivityPhoto getActivityPhoto() {
		return this.getAVObject(ACTIVITYPHOTO);
	}

	public void setActivityPhoto(ObjActivityPhoto activityPhoto) {
		this.put(ACTIVITYPHOTO, activityPhoto);
	}
}
