package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjActivityPhotoCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.object.ObjActivityPhotoPraise;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;

public class ObjActivityPhotoWrap {
	/**
	 * 获取活动图片
	 * @param activity
	 * @param callback
	 */
	public void queryActivityPhotos(ObjActivity activity,final ObjActivityPhotoCallback callback){
		AVQuery<ObjActivityPhoto> query = AVObject.getQuery(ObjActivityPhoto.class);
		query.whereEqualTo("activity", activity);
		query.findInBackground(new FindCallback<ObjActivityPhoto>() {
			
			@Override
			public void done(List<ObjActivityPhoto> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return ;
				}
				if(objects != null){
					callback.callback(objects, e);
				}
			}
		});
	}
	/**
	 * 查询是否对活动照片点赞
	 * @param photo
	 * @param user
	 * @param callback
	 */
	public void queryPhotoPraise(ObjActivityPhoto photo,ObjUser user,final ObjFunBooleanCallback callback){
		AVQuery<AVObject> query = new AVQuery<AVObject>(ObjTableName.getPhotoPraiseTb());
		query.whereEqualTo("activityPhoto", photo);
		query.whereEqualTo("user", user);
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
	 * 对活动照片点赞
	 * @param photo
	 * @param user
	 * @param callback
	 */
	public void praiseActivityPhoto(ObjActivityPhoto photo,ObjUser user,final ObjFunBooleanCallback callback){
		ObjActivityPhotoPraise praise = new ObjActivityPhotoPraise();
		praise.setActivityPhoto(photo);
		praise.setUser(user);
		praise.saveInBackground(new SaveCallback() {
			
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
	/**
	 * 取消对活动照片点赞
	 * @param user
	 * @param activity
	 * @param callback
	 */
	public static void cancelPraiseActivityPhoto(ObjUser user,ObjActivityPhoto activity,final ObjFunBooleanCallback callback){
		AVQuery<AVObject> query = new AVQuery<AVObject>(ObjTableName.getPhotoPraiseTb());
		query.whereEqualTo("user", user);
		query.whereEqualTo("activity", activity);
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
