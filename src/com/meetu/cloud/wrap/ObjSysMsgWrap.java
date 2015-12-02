package com.meetu.cloud.wrap;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.meetu.cloud.callback.ObjSysMsgListCallback;
import com.meetu.cloud.object.ObjSysMsg;
import com.meetu.cloud.object.ObjUser;

public class ObjSysMsgWrap {
	//查询系统消息 
	public static void querySysMsgs(ObjUser user,final ObjSysMsgListCallback callback){
		AVQuery<ObjSysMsg> query = AVObject.getQuery(ObjSysMsg.class);
		query.whereEqualTo("user", user);
		query.include("towardsUser");
		query.include("acty");
		query.include("userPhoto");
		query.findInBackground(new FindCallback<ObjSysMsg>() {

			@Override
			public void done(List<ObjSysMsg> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return ;
				}
				if(objects != null){
					callback.callback(objects, null);
				}else{
					callback.callback(null, new AVException(0, "获取系统消息失败"));
				}
			}
		});
	}
}
