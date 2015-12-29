package com.meetu.cloud.wrap;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.AVQuery.CachePolicy;
import com.meetu.cloud.callback.ObjActivityOrderCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityOrder;
import com.meetu.cloud.object.ObjActivityTicket;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;

public class ObjActivityOrderWrap {
	/**
	 * 查询活动报名成功表
	 * 
	 * @param callback
	 */
	public static void queryActivitySignUp(ObjActivity activity,
			final ObjUserCallback callback) {
		AVQuery<ObjActivityOrder> query = AVObject
				.getQuery(ObjActivityOrder.class);
		query.include("user");
		query.whereEqualTo("activity", activity);
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(Constants.OrderStatusPaySuccess);
		numbers.add(Constants.OrderStatusArrive);
		query.whereContainedIn("orderStatus", numbers);
		query.orderByAscending("userGender");
		query.orderByDescending("userNo");
//		query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
//		query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
		query.limit(1000);
		query.findInBackground(new FindCallback<ObjActivityOrder>() {

			@Override
			public void done(List<ObjActivityOrder> list, AVException e) {
				// TODO Auto-generated method stub
				if (null != e) {
					callback.callback(null, new AVException(0, "查询活动报名表失败"));
					return;
				}
				if (null == list) {
					return;
				}
				ArrayList<ObjUser> orderUsers = new ArrayList<ObjUser>();
				if (list.size() == 0) {
					callback.callback(orderUsers, null);
					return;
				}
				for (ObjActivityOrder object : list) {
					if (object.getAVUser("user") != null) {
						// 强制类型转换 用此方法 可获得子类属性值
						AVUser avUser = object.getAVUser("user");
						ObjUser user = AVUser.cast(avUser, ObjUser.class);
						orderUsers.add(user);
					} else {
					}
				}
				callback.callback(orderUsers, null);
			}
		});
	}

	// 报名
	public static void signUpActivity(ObjActivity activity, ObjUser user,
			ObjActivityTicket ticket, int status, String expect,
			final ObjActivityOrderCallback callback) {
		final ObjActivityOrder order = new ObjActivityOrder();
		order.setActivity(activity);
		order.setUser(user);
		order.setOrderStatus(status);
		order.setTicket(ticket);
		order.setUserExpect(expect);
		order.setUserGender(user.getGender());
		order.setFetchWhenSave(true);
		order.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					callback.callback(order, null);
				} else {
					callback.callback(null, e);
				}
			}
		});
	}
	
	//查询我是否报名，若报名返回订单
	public static void queryIsOrder(ObjActivity activity,ObjUser user,final ObjActivityOrderCallback callback){
		AVQuery<ObjActivityOrder> query = AVObject
				.getQuery(ObjActivityOrder.class);
		query.whereEqualTo("activity", activity);
		query.whereEqualTo("user", user);
		query.whereEqualTo("orderStatus", Constants.OrderStatusPaySuccess);
		query.findInBackground(new FindCallback<ObjActivityOrder>() {
			
			@Override
			public void done(List<ObjActivityOrder> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					callback.callback(null, e);
					return;
				}
				if(objects!= null && objects.size()>0){
					callback.callback(objects.get(0), null);
				}else{
					callback.callback(null, null);
				}
			}
		});
	}
	//修改订单（修改订单中属性，重新保存即可）
	public static void updateOrder(final ObjActivityOrder order,final ObjActivityOrderCallback callback){
		order.setFetchWhenSave(true);
		order.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					callback.callback(order, null);
				} else {
					callback.callback(null, e);
				}
			}
		});
		
	}
}
