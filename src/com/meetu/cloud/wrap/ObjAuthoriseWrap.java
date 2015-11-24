package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.meetu.cloud.callback.ObjAuthoriseApplyCallback;
import com.meetu.cloud.callback.ObjAuthoriseCategoryCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjAuthoriseApply;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;

public class ObjAuthoriseWrap {
	/**
	 * 获取权限分类
	 * */
	public static void queryAuthoriseCatogory(int optionNum,final ObjAuthoriseCategoryCallback callback){
		AVQuery<ObjAuthoriseCategory> query = AVObject.getQuery(ObjAuthoriseCategory.class);
		query.whereEqualTo("operationNum", optionNum);
		query.findInBackground(new FindCallback<ObjAuthoriseCategory>() {

			@Override
			public void done(List<ObjAuthoriseCategory> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return;
				}
				if(objects != null){
					callback.callback(objects, null);
				}else{
					callback.callback(null, new AVException(0, "获取权限分类失败"));
				}
			}
		});
	}
	/**
	 * 查询用户是否有权限创建群聊
	 * */
	public static void queryUserAuthorise(ObjAuthoriseCategory category,ObjUser user,final ObjFunBooleanCallback callback){
		AVQuery<AVObject> query = new AVQuery<AVObject>(ObjTableName.getUserAuthoriseTb());
		query.whereEqualTo("user", user);
		query.whereEqualTo("category", category);
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
	 * 申请权限
	 * */
	public static void applyAuthorise(ObjUser user,ObjAuthoriseCategory category,String argument,final ObjFunBooleanCallback callback){
		ObjAuthoriseApply apply = new ObjAuthoriseApply();
		apply.setUser(user);
		apply.setCategory(category);
		apply.setArgument(argument);
		apply.setFreshStatus(false);
		apply.saveInBackground(new SaveCallback() {

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
	 * 重新申请权限
	 * */
	public static void updateApplyAuthorise(ObjAuthoriseApply apply,String argument,final ObjFunBooleanCallback callback){
		apply.setArgument(argument);
		apply.saveInBackground(new SaveCallback() {

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
	 * 查看权限申请状况
	 * */
	public static void queryApply(ObjUser user,ObjAuthoriseCategory category,final ObjAuthoriseApplyCallback callback){
		AVQuery<ObjAuthoriseApply> query = AVObject.getQuery(ObjAuthoriseApply.class);
		query.whereEqualTo("user", user);
		query.whereEqualTo("category", category);
		query.findInBackground(new FindCallback<ObjAuthoriseApply>() {

			@Override
			public void done(List<ObjAuthoriseApply> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return;
				}
				if(objects != null){
					callback.callback(objects, null);
				}else{
					callback.callback(null, new AVException(0, "获取申请权限状态失败"));
				}
			}
		});

	}
}
