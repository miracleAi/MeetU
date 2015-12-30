package com.meetu.cloud.wrap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVQuery.CachePolicy;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.cloud.callback.ObjActivityCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjTicketCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityFeedback;
import com.meetu.cloud.object.ObjActivityOrder;
import com.meetu.cloud.object.ObjActivityTicket;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;

public class ObjActivityWrap {
	/**
	 * 网络活动查询
	 */
	public static void queryAllActivitys(final ObjActivityCallback callback) {
		AVQuery<ObjActivity> query = AVObject.getQuery(ObjActivity.class);
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		numbers.add(Constants.ActyStatusProcess);
		numbers.add(Constants.ActyStatusSignUp);
		numbers.add(Constants.ActyStatusSignUPOver);
		numbers.add(Constants.ActyStatusOver);
		
//		query.whereContainedIn("status", numbers);
//		query.orderByAscending("status");
//		query.orderByDescending("timeStart");
	
		query.limit(1000);
		query.findInBackground(new FindCallback<ObjActivity>() {

			@Override
			public void done(List<ObjActivity> list, AVException e) {
				// TODO Auto-generated method stub
				if (null != e) {
					callback.callback(null, new AVException(0, "查询活动列表失败"));
					return;
				}
				if (null == list) {
					return;
				}
				ObjActivity activity = list.get(0);
				callback.callback(list, null);
			}
		});
	}

	/**
	 * 检查用户是否参加过此活动
	 * 
	 * @param activity
	 * @param user
	 * @param callback
	 */
	public static void queryUserJoin(ObjActivity activity, ObjUser user,
			final ObjFunBooleanCallback callback) {
		AVQuery<AVObject> query = new AVQuery<AVObject>(
				ObjTableName.getActivitySignUpTb());
		query.whereEqualTo("activity", activity);
		query.whereEqualTo("user", user);
		query.whereEqualTo("orderStatus", Constants.OrderStatusPaySuccess);
		query.findInBackground(new FindCallback<AVObject>() {

			@Override
			public void done(List<AVObject> object, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					callback.callback(false, e);
					return;
				}
				if (object != null && object.size() > 0) {
					callback.callback(true, null);
				} else {
					callback.callback(false, null);
				}
			}
		});
	}

	/**
	 * 获取活动票种信息
	 * 
	 * @param activity
	 * @param callback
	 */
	public static void queryTicket(ObjActivity activity,
			final ObjTicketCallback callback) {
		AVQuery<ObjActivityTicket> query = AVObject
				.getQuery(ObjActivityTicket.class);
		query.whereEqualTo("activity", activity);
		query.findInBackground(new FindCallback<ObjActivityTicket>() {

			@Override
			public void done(List<ObjActivityTicket> objects, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					callback.callback(null, e);
					return;
				}
				if (null != objects && objects.size() > 0) {
					callback.callback(objects, null);
				} else {
					callback.callback(null, new AVException(0, "未获取票种信息"));
				}
			}
		});
	}

	/**
	 * 活动反馈
	 * 
	 * @param activity
	 * @param user
	 * @param callback
	 */
	public static void activityFeedBack(ObjActivity activity, ObjUser user,
			String content, final ObjFunBooleanCallback callback) {
		ObjActivityFeedback feed = new ObjActivityFeedback();
		feed.setActivity(activity);
		feed.setUser(user);
		feed.setContent(content);
		feed.saveInBackground(new SaveCallback() {

			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					callback.callback(true, null);
				} else {
					callback.callback(false, e);
				}
			}
		});
	}

	/**
	 * 根据ID生成activity对象
	 * */
	public static ObjActivity getActivityById(String id) {
		ObjActivity activity = null;
		try {
			activity = AVObject.createWithoutData(ObjActivity.class, id);
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return activity;
	}
}
