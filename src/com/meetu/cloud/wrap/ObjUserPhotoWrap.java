package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserPhotoCallback;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.object.ObjUserPhotoPraise;

public class ObjUserPhotoWrap {
	/**
	 * 查询用户照片
	 * @param user
	 * @param callback
	 */
	public static void queryUserPhoto(ObjUser user,final ObjUserPhotoCallback callback){
		AVQuery<ObjUserPhoto> query = AVObject.getQuery(ObjUserPhoto.class);
		query.whereEqualTo("user", user);
		query.findInBackground(new FindCallback<ObjUserPhoto>() {

			@Override
			public void done(List<ObjUserPhoto> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return ;
				}
				if(objects != null){
					callback.callback(objects, null);
				}else{
					callback.callback(null, new AVException(0, "查询失败"));
				}
			}
		});
	}
	/**
	 * 查询是否对用户照片点赞
	 * @param photo
	 * @param user
	 * @param callback
	 */
	public static void queryUserPhotoPraise(ObjUserPhoto photo,ObjUser user,final ObjFunBooleanCallback callback){
		AVQuery<AVObject> query = new AVQuery<AVObject>(ObjTableName.getUserPhotoPraiseTb());
		query.whereEqualTo("userPhoto", photo);
		query.whereEqualTo("praiseUser", user);
		query.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> object, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(false, e);
					return ;
				}
				if(object != null && object.size()>0){
					callback.callback(true, null);
				}else{
					callback.callback(false, null);
				}
			}
		});
	}
	/**
	 * 对用户照片点赞
	 * @param photo
	 * @param user
	 * @param callback
	 */
	public static void praiseUserPhoto(ObjUserPhoto photo,ObjUser user,final ObjFunBooleanCallback callback){
		ObjUserPhotoPraise praise = new ObjUserPhotoPraise();
		praise.setPraiseUser(user);
		praise.setUserPhoto(photo);
		praise.saveInBackground(new SaveCallback() {
			
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
	 * 取消对用户照片点赞
	 * @param photo
	 * @param user
	 * @param callback
	 */
	public static void cancelPraiseUserPhoto(ObjUserPhoto photo,ObjUser user,final ObjFunBooleanCallback callback){
		AVQuery<AVObject> query = new AVQuery<AVObject>(ObjTableName.getUserPhotoPraiseTb());
		query.whereEqualTo("praiseUser", user);
		query.whereEqualTo("userPhoto", photo);
		query.deleteAllInBackground(new DeleteCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(false, e);
					return ;
				}else{
					callback.callback(true, null);
				}
			}
		});
	}
}
