package com.meetu.cloud.object;

import android.os.Parcelable.Creator;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVObject.AVObjectCreator;

@AVClassName("ObjActivityFeedback")
public class ObjActivityFeedback extends AVObject{
	public static final Creator CREATOR = AVObjectCreator.instance;
	public static final String ACTIVITY = "activity";
	public static final String USER = "user";
	public static final String CONTENT = "content";
	public ObjActivityFeedback() {
		// TODO Auto-generated constructor stub
	}
	public void setActivity(ObjActivity activity) {
		this.put(ACTIVITY, activity);
	}
	public void setUser(ObjUser user) {
		this.put(USER, user);
	}
	public void setContent(String content) {
		this.put(CONTENT, content);
	}
	
}
