package com.meetu.cloud.wrap;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.cloud.callback.ObjActivityCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;

public class ObjActivityWrap {
	/**
	 * 网络活动查询
	 */
	public static void queryAllActivitys(final ObjActivityCallback callback){
		AVQuery<ObjActivity> query = AVObject.getQuery(ObjActivity.class);
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(Constants.ActyStatusProcess);
		numbers.add(Constants.ActyStatusSignUp);
		numbers.add(Constants.ActyStatusSignUPOver);
		numbers.add(Constants.ActyStatusOver);
		query.whereContainedIn("status", numbers);
		query.orderByAscending("status");
		query.orderByDescending("timeStart");
		query.limit(1000);
		query.findInBackground(new FindCallback<ObjActivity>() {
			
			@Override
			public void done(List<ObjActivity> list, AVException e) {
				// TODO Auto-generated method stub
				if(null != e){
					callback.callback(null, new AVException(0, "查询活动列表失败"));
					return ;
				}
				if(null == list){
					return ; 
				}
				ObjActivity activity = list.get(0);
				callback.callback(list, null);
			}
		});
	}
}
