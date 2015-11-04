package com.meetu.cloud.wrap;

import java.util.List;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunObjectCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;

public class ObjPraiseWrap {
	/**
	 * 查询我是否对活动点赞
	 * @param callback
	 */
	public static void queryActivityFavor(AVUser user,ObjActivity activity,final ObjFunBooleanCallback callback){
		AVQuery<AVObject> query = new AVQuery<AVObject>(ObjTableName.getActivityFavorTb());
		query.whereEqualTo("user", user);
		query.whereEqualTo("activity", activity);
		query.getFirstInBackground(new GetCallback<AVObject>() {
			
			@Override
			public void done(AVObject object, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(false, e);
					return ;
				}
				if(object != null){
					callback.callback(true, null);
				}else{
					callback.callback(false, null);
				}
			}
		});
	}
}
