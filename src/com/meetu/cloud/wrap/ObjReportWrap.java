package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjReportChat;
import com.meetu.cloud.object.ObjReportUser;
import com.meetu.cloud.object.ObjShieldUser;
import com.meetu.cloud.object.ObjUser;

public class ObjReportWrap {
	/**
	 * 举报用户
	 * */
	public static void reportUser(ObjReportUser reportUser,
			final ObjFunBooleanCallback callback) {
		reportUser.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					callback.callback(true, null);
				} else {
					callback.callback(false, e);
				}
			}
		});
	}

	/**
	 * 判断是否已举报用户
	 * */
	public static void isReport(ObjUser user, ObjUser otherUser,
			final ObjFunBooleanCallback callback) {
		AVQuery<ObjReportUser> query = AVObject.getQuery(ObjReportUser.class);
		query.whereEqualTo("user", user);
		query.whereEqualTo("reportUser", otherUser);
		query.findInBackground(new FindCallback<ObjReportUser>() {

			@Override
			public void done(List<ObjReportUser> objects, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					callback.callback(false, e);
					return;
				}
				if (objects != null && objects.size() > 0) {
					callback.callback(true, null);
				} else {
					callback.callback(false, null);
				}
			}
		});
	}

	/**
	 * 举报觅聊
	 * */
	public static void reportChat(ObjReportChat chat,
			final ObjFunBooleanCallback callback) {
		chat.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					callback.callback(true, null);
				} else {
					callback.callback(false, e);
				}
			}
		});
	}
}
