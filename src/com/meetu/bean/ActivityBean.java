package com.meetu.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjFollowWrap;
import com.meetu.cloud.wrap.ObjPraiseWrap;
import com.meetu.common.Constants;

public class ActivityBean implements Serializable{

	//活动
	private ObjActivity activity;
	//是否点赞
	private boolean isFavor;
	//我关注并且参加活动的人数
	private int orderAndFollow;

	public void setActivity(ObjActivity activity) {
		this.activity = activity;
	}

	public void setFavor(boolean isFavor) {
		this.isFavor = isFavor;
	}

	public void setOrderAndFollow(int orderAndFollow) {
		this.orderAndFollow = orderAndFollow;
	}



	public ObjActivity getActivity() {
		return activity;
	}
	public boolean isFavor() {
		return isFavor;
	}
	public int getOrderAndFollow() {
		return orderAndFollow;
	}

	AVUser user = ObjUser.getCurrentUser();
	//查询是否点赞  setFavor
	public void queryFavor(ObjActivity activity,final Handler handler){
		if(user == null){
			return ;
		}
		ObjPraiseWrap.queryActivityFavor(user, activity, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(result){
					setFavor(true);
				}else{
					setFavor(false);
				}
				handler.sendEmptyMessage(Constants.QUER_FAVOR_OK);
			}
		});
	}
}
