package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjShieldUser;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.city.ShengshiquActivity;

public class ObjShieldUserWrap {

	//屏蔽用户
	public static void shieldUser(ObjShieldUser shieldUser,final ObjFunBooleanCallback callback){
		shieldUser.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					callback.callback(true, null);
				}else{
					callback.callback(false, e);
				}
			}
		});
	}
	//取消屏蔽
	public static void unShieldUser(ObjUser user,ObjUser otherUser,final ObjFunBooleanCallback callback){
		AVQuery<ObjShieldUser> query = AVObject.getQuery(ObjShieldUser.class);
		query.whereEqualTo("user", user);
		query.whereEqualTo("shieldUser", otherUser);
		query.deleteAllInBackground(new DeleteCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					callback.callback(true, null);
				}else{
					callback.callback(false, e);
				}
			}
		});
	}
	//判断是否已拉黑
	public static void isShield(ObjUser user,ObjUser otherUser,final ObjFunBooleanCallback callback){
		AVQuery<ObjShieldUser> query = AVObject.getQuery(ObjShieldUser.class);
		query.whereEqualTo("user", user);
		query.whereEqualTo("shieldUser", otherUser);
		query.findInBackground(new FindCallback<ObjShieldUser>() {
			
			@Override
			public void done(List<ObjShieldUser> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(false, e);
					return;
				}
				if(objects != null && objects.size()>0){
					callback.callback(true, null);
				}else{
					callback.callback(false, null);
				}
			}
		});
	}
}
