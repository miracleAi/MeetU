package com.meetu.cloud.wrap;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVQuery.CachePolicy;
import com.avos.avoscloud.FindCallback;
import com.meetu.cloud.callback.ObjActivityCallback;
import com.meetu.cloud.callback.ObjActivityCoverCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjTableName;

public class ObjActivityCoverWrap {
	/**
	 * 查询活动封面 轮播图
	 * 
	 * @param callback
	 */
	public static void queryActivityCover(ObjActivity activity,
			final ObjActivityCoverCallback callback) {
		AVQuery<ObjActivityCover> query = AVObject
				.getQuery(ObjActivityCover.class);
		query.whereEqualTo("activity", activity);
		query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
		// TimeUnit.DAYS.toMillis(1)
		query.setMaxCacheAge(10 * 60 * 1000);
		query.findInBackground(new FindCallback<ObjActivityCover>() {

			@Override
			public void done(List<ObjActivityCover> list, AVException e) {
				// TODO Auto-generated method stub
				if (null != e) {
					callback.callback(null, new AVException(0, "查询封面列表失败"));
					return;
				}
				if (null == list) {
					return;
				}
				callback.callback(list, null);
			}
		});
	}
}
