package com.meetu.cloud.wrap;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;

public class ObjFollowWrap {
	/**
	 * 查询我关注的并且参加活动的人列表
	 * @param callback
	 */
	public static void myFollow(final ArrayList<ObjUser> orderUsers,AVUser user,final ObjUserCallback callback){
		AVQuery<AVObject> query = new AVQuery<AVObject>(ObjTableName.getMyFollowTb());
		query.include("followee");
		query.whereEqualTo("user", user);
		query.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> list, AVException e) {
				// TODO Auto-generated method stub
				if(null != e){
					callback.callback(null, new AVException(0, "查询关注列表失败"));
					return ;
				}
				if(null == list){
					return ;
				}
				ArrayList<ObjUser> followAndOrderUsers = new ArrayList<ObjUser>();
				for(AVObject objects :list){
					for(AVUser objectOrder : orderUsers){
						AVUser followee = objects.getAVUser("followee");
						if(followee.getObjectId() == objectOrder.getObjectId()){
							ObjUser fUser = AVUser.cast(followee, ObjUser.class);
							followAndOrderUsers.add(fUser);
						}
					}
				}
				callback.callback(followAndOrderUsers, null);
			}
		});
	}
}
