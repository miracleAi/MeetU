package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjScripBoxCallback;
import com.meetu.cloud.callback.ObjScripCallback;
import com.meetu.cloud.callback.ObjScripInfoCallback;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjScrip;
import com.meetu.cloud.object.ObjScripBox;
import com.meetu.cloud.object.ObjUser;

public class ObjScriptWrap {

	/**
	 * 创建小纸条
	 * */
	public static void createScrip(final ObjScrip scrip,final ObjScripInfoCallback callback){
		scrip.setFetchWhenSave(true);
		scrip.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if(e == null){
					callback.callback(scrip, null);
				}else{
					callback.callback(null, e);
				}
			}
		});
	}
	/**
	 * 查询所有
	 * */
	public static void queryAllScrip(ObjScripBox scripBox,final ObjScripCallback callback){
		AVQuery<ObjScrip> query = AVObject.getQuery(ObjScrip.class);
		//query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
		//TimeUnit.DAYS.toMillis(1)
		//query.setMaxCacheAge(10*60*1000);
		query.whereEqualTo("scripBox", scripBox);
		query.findInBackground(new FindCallback<ObjScrip>() {

			@Override
			public void done(List<ObjScrip> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return ;
				}
				if(null != objects){
					callback.callback(objects, null);
				}else{
					callback.callback(null, new AVException(0, "获取纸条失败"));
				}
			}
		});
	}
	/**
	 * 查询所有纸条箱
	 * */
	public static void queryScripBox(ObjUser user,final ObjScripBoxCallback callback){
		AVQuery<ObjScripBox> query = AVObject.getQuery(ObjScripBox.class);
		query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
		//TimeUnit.DAYS.toMillis(1)
		query.setMaxCacheAge(10*60*1000);
		query.whereEqualTo("users", user);
		query.findInBackground(new FindCallback<ObjScripBox>() {
			
			@Override
			public void done(List<ObjScripBox> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return ;
				}
				if(null != objects){
					callback.callback(objects, null);
				}else{
					callback.callback(null, new AVException(0, "获取纸条箱失败"));
				}
			}
		});
	}
}
