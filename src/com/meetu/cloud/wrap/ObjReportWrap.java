package com.meetu.cloud.wrap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjReportChat;
import com.meetu.cloud.object.ObjReportUser;

public class ObjReportWrap {
	/**
	 * 举报用户
	 * */
	public static void reportUser(ObjReportUser reportUser,final ObjFunBooleanCallback callback){
		reportUser.saveInBackground(new SaveCallback() {
			
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
	/**
	 * 举报觅聊
	 * */
	public static void reportChat(ObjReportChat chat,final ObjFunBooleanCallback callback){
		chat.saveInBackground(new SaveCallback() {
			
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
