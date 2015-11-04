package com.meetu.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.meetu.cloud.callback.ObjActivityCoverCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjFunObjectsCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityCoverWrap;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjActivityWrap;
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
	//活动封面
	private ArrayList<ObjActivityCover> actyCovers;
	//参加活动已完成支付的用户列表
	private ArrayList<ObjUser> orderUsers;

	public void setActivity(ObjActivity activity) {
		this.activity = activity;
	}

	public void setFavor(boolean isFavor) {
		this.isFavor = isFavor;
	}

	public void setOrderAndFollow(int orderAndFollow) {
		this.orderAndFollow = orderAndFollow;
	}

	public void setActyCovers(ArrayList<ObjActivityCover> actyCovers) {
		this.actyCovers = actyCovers;
	}

	public void setOrderUsers(ArrayList<ObjUser> orderUsers) {
		this.orderUsers = orderUsers;
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
	public ArrayList<ObjActivityCover> getActyCovers() {
		return actyCovers;
	}

	public ArrayList<ObjUser> getOrderUsers() {
		return orderUsers;
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
	//查询参加活动列表  setOrderUsers
	public void queryOrderUsers(final ObjActivity activity,final Handler handler){
		ObjActivityOrderWrap.queryActivitySignUp(activity, new ObjUserCallback() {

			@Override
			public void callback(List<ObjUser> objects, AVException e) {
				// TODO Auto-generated method stub
				ArrayList<ObjUser> users = new ArrayList<ObjUser>();
				if(e != null){
					return ;
				}
				if(objects != null){
					users.addAll(objects);
					setOrderUsers(users);
					if(getOrderUsers().size()>0){
						queryFollowAndOrder(activity, handler);
					}else{
						setOrderAndFollow(0);
						handler.sendEmptyMessage(Constants.QUER_ORDERFOLLOW_OK);
					}
				}
			}
		});

	}
	//获取参加活动并且我关注的人  setOrderAndFollow
	public void queryFollowAndOrder(ObjActivity activity,final Handler handler){
		ArrayList<ObjUser> followUsers = new ArrayList<ObjUser>();
		if(user == null){
			return ;
		}
		ObjFollowWrap.myFollow(getOrderUsers(), user, new ObjFunObjectsCallback() {
			
			@Override
			public void callback(List<AVObject> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					return ;
				}
				if(objects == null){
					Log.d("mytest", "obj null");
					setOrderAndFollow(0);
				}else{
					setOrderAndFollow(objects.size());
				}
				handler.sendEmptyMessage(Constants.QUER_ORDERFOLLOW_OK);
			}
		});
	}
	//查询活动封面图列表 setActyCovers
	public void queryActyCovers(ObjActivity activity,final Handler handler){
		ObjActivityCoverWrap.queryActivityCover(activity, new ObjActivityCoverCallback() {

			@Override
			public void callback(List<ObjActivityCover> objects, AVException e) {
				// TODO Auto-generated method stub
				ArrayList<ObjActivityCover> list = new ArrayList<ObjActivityCover>();
				if(e != null){
					return ;
				}
				if(objects != null && objects.size()>0){
					list.addAll(objects);
					setActyCovers(list);
					handler.sendEmptyMessage(Constants.QUER_ACTIVITYCOVER_OK);
				}
			}
		});
	}
}
