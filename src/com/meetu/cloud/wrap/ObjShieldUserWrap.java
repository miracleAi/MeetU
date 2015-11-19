package com.meetu.cloud.wrap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjShieldUser;
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
	public static void unShieldUser(ObjShieldUser shieldUser,final ObjFunBooleanCallback callback){
		shieldUser.deleteInBackground(new DeleteCallback() {

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
}
