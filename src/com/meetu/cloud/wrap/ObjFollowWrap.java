package com.meetu.cloud.wrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunMapCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjTableName;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;

public class ObjFollowWrap {
	/**
	 * 查询我关注的并且参加活动的人列表
	 * 
	 * @param callback
	 */
	public static void myFollow(final ArrayList<ObjUser> orderUsers,
			ObjUser user, final ObjUserCallback callback) {
		AVQuery<ObjUser> query = ObjUser.followeeQuery(user.getObjectId(),
				ObjUser.class);
		query.include("followee");
		query.findInBackground(new FindCallback<ObjUser>() {

			@Override
			public void done(List<ObjUser> objects, AVException e) {
				// TODO Auto-generated method stub
				if (null != e) {
					callback.callback(null, e);
					return;
				}
				if (null == objects) {
					callback.callback(null, new AVException(0, "查询关注列表失败"));
					return;
				}
				ArrayList<ObjUser> followAndOrderUsers = new ArrayList<ObjUser>();
				for (ObjUser object : objects) {
					for (ObjUser objectOrder : orderUsers) {
						if (object.getObjectId() == objectOrder.getObjectId()) {
							followAndOrderUsers.add(object);
						}
					}
				}
				callback.callback(followAndOrderUsers, null);
			}
		});
	}

	/**
	 * 获取我关注的人的列表
	 * */
	public static void getFollowee(ObjUser user, final ObjUserCallback callback) {
		AVQuery<ObjUser> query = ObjUser.followeeQuery(user.getObjectId(),
				ObjUser.class);
		query.include("followee");
		query.findInBackground(new FindCallback<ObjUser>() {

			@Override
			public void done(List<ObjUser> objects, AVException e) {
				// TODO Auto-generated method stub
				if (null != e) {
					callback.callback(null, e);
					return;
				}
				if (null == objects) {
					callback.callback(null, new AVException(0, "查询关注列表失败"));
				} else {
					callback.callback(objects, null);
				}
			}
		});
	}

	/**
	 * 获取我的粉丝列表
	 * */
	public static void getFollower(ObjUser user, final ObjUserCallback callback) {
		AVQuery<ObjUser> query = ObjUser.followeeQuery(user.getObjectId(),
				ObjUser.class);
		query.include("follower");
		query.findInBackground(new FindCallback<ObjUser>() {

			@Override
			public void done(List<ObjUser> objects, AVException e) {
				// TODO Auto-generated method stub
				if (null != e) {
					callback.callback(null, e);
					return;
				}
				if (null == objects) {
					callback.callback(null, new AVException(0, "查询粉丝列表失败"));
				} else {
					callback.callback(objects, null);
				}
			}
		});
	}

	/**
	 * 关注
	 * */
	public static void followIn(ObjUser user, String userID,
			final ObjFunBooleanCallback callback) {
		user.followInBackground(userID, new FollowCallback<AVObject>() {

			@Override
			public void done(AVObject object, AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					callback.callback(true, null);
					return;
				}
				if (e.getCode() == AVException.DUPLICATE_VALUE) {
					callback.callback(false, new AVException(0, "您已关注过此用户"));
				} else {
					callback.callback(false, e);
				}
			}
		});
	}

	/**
	 * 取消关注
	 * */
	public static void unFollow(ObjUser user, String userID,
			final ObjFunBooleanCallback callback) {
		user.unfollowInBackground(userID, new FollowCallback<AVObject>() {

			@Override
			public void done(AVObject object, AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					callback.callback(true, null);
					return;
				} else {
					callback.callback(false, e);
				}
			}
		});
	}

	/**
	 * 我关注的 关注我的 相互关注的
	 * */
	public static void queryfollow(ObjUser user,
			final ObjFunMapCallback callback) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", user);
		AVCloud.callFunctionInBackground("followMembers", params,
				new FunctionCallback<Map<String, Object>>() {

					@Override
					public void done(Map<String, Object> result, AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							callback.callback(null, e);
							return;
						}
						if (result != null) {
							log.d("mytest", "follow result++" + result);
							callback.callback(result, null);
						} else {
							callback.callback(null, new AVException(0,
									"获取觅聊信息失败"));
						}
					}
				});
	}
}
