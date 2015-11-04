package com.meetu.cloud.wrap;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityOrder;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;

public class ObjActivityOrderWrap {
	/**
	 * 查询活动报名成功表
	 * @param callback
	 */
	public static void queryActivitySignUp(ObjActivity activity,final ObjUserCallback callback){
		AVQuery<ObjActivityOrder> query = AVObject.getQuery(ObjActivityOrder.class);
		query.include("user");
		query.whereEqualTo("activity", activity);
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(Constants.OrderStatusPaySuccess);
		numbers.add(Constants.OrderStatusArrive);
		query.whereContainedIn("orderStatus", numbers);
		query.orderByAscending("userGender");
		query.orderByDescending("userNo");
		query.limit(1000);
		query.findInBackground(new FindCallback<ObjActivityOrder>() {

			@Override
			public void done(List<ObjActivityOrder> list, AVException e) {
				// TODO Auto-generated method stub
				if(null != e){
					callback.callback(null, new AVException(0, "查询活动报名表失败"));
					return ;
				}
				if(null == list){
					return ;
				}
				ArrayList<AVUser> orderUsers = new ArrayList<AVUser>();
				for(ObjActivityOrder object : list){
					if(object.getAVUser("user") != null){
						orderUsers.add(object.getAVUser("user"));
						AVUser avUser= object.getAVUser("user");
						//强制类型转换 用此方法 可获得子类属性值
						ObjUser user  = AVUser.cast(avUser, ObjUser.class);
						Log.d("mytest", "user"+user.getString("nameNick"));
					}else{
						Log.d("mytest", "user null");
					}
				}
				callback.callback(orderUsers, null);
			}
		});
	}
}
