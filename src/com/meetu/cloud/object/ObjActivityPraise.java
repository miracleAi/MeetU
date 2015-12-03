package com.meetu.cloud.object;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("ObjActivityPraise")
public class ObjActivityPraise extends AVObject {
	public static final Creator CREATOR = AVObjectCreator.instance;

	public static final String ACTIVITY = "activity";
	public static final String USER = "user";

	public ObjActivityPraise() {
		// TODO Auto-generated constructor stub
	}

	public ObjActivity getActivity() {
		return this.getAVObject(ACTIVITY);
	}

	public void setActivity(ObjActivity activity) {
		this.put(ACTIVITY, activity);
	}

	public ObjUser getUser() {
		return this.getAVUser(USER);
	}

	public void setUser(ObjUser user) {
		this.put(USER, user);
		;
	}
}
