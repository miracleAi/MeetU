package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.meetu.cloud.callback.ObjGlobalCallback;
import com.meetu.cloud.object.ObjGlobalAndroid;

public class ObjGlobalAndroidWrap {
	//获取信息
	public static void checkVersion(final ObjGlobalCallback callback){
		AVQuery<ObjGlobalAndroid> query = AVObject.getQuery(ObjGlobalAndroid.class);
		query.findInBackground(new FindCallback<ObjGlobalAndroid>() {
			
			@Override
			public void done(List<ObjGlobalAndroid> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return;
				}
				if(objects!= null && objects.size()>0){
					callback.callback(objects.get(0), null);
				}else{
					callback.callback(null, new AVException(0, "获取版本信息失败"));
				}
			}
		});
	}
}
