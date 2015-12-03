package com.meetu.cloud.object;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName("ObjShieldUser")
public class ObjShieldUser extends AVObject {
	public static final Creator CREATOR = AVObjectCreator.instance;
	public static String USER = "user";
	public static final String SHIELDUSER = "shieldUser";

	public ObjShieldUser() {
		// TODO Auto-generated constructor stub
	}

	public ObjUser getUser() {
		return this.getAVUser(USER, ObjUser.class);
	}

	public void setUser(ObjUser user) {
		this.put(USER, user);
	}

	public ObjUser getShieldUser() {
		return this.getAVUser(SHIELDUSER, ObjUser.class);
	}

	public void setShieldUser(ObjUser shieldUser) {
		this.put(SHIELDUSER, shieldUser);
	}

}
