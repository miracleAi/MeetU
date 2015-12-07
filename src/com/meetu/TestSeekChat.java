package com.meetu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.bean.SeekChatBean;
import com.meetu.bean.SeekChatInfoBean;
import com.meetu.cloud.callback.ObjFunMapCallback;
import com.meetu.cloud.callback.ObjFunObjectCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjChatWrap;
import com.meetu.cloud.wrap.ObjFollowWrap;

import android.R.array;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestSeekChat extends Activity {
	Button clickBtn;
	TextView infoTv;
	ObjUser user = null;
	SeekChatInfoBean chatBean = null;
	ArrayList<SeekChatBean> seekList = new ArrayList<SeekChatBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_msg2_layout);
		if (ObjUser.getCurrentUser() != null) {
			AVUser currentUser = ObjUser.getCurrentUser();
			user = AVUser.cast(currentUser, ObjUser.class);
		} else {
			Toast.makeText(getApplicationContext(), "please login", 1000)
					.show();
		}
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		clickBtn = (Button) findViewById(R.id.click);
		infoTv = (TextView) findViewById(R.id.info_tv);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {/*
											 * // TODO Auto-generated method
											 * stub
											 * ObjChatWrap.queryChatInfo(user,
											 * System.currentTimeMillis(), 999,
											 * new ObjFunMapCallback() {
											 * 
											 * @Override public void
											 * callback(Map<String, Object>
											 * object, AVException e) { // TODO
											 * Auto-generated method stub
											 * 
											 * // TODO Auto-generated method
											 * stub if(e != null){ return; }
											 * chatBean = new
											 * SeekChatInfoBean();
											 * log.d("mytest",
											 * "result="+object);
											 * chatBean.setNeedAuthorise
											 * ((Boolean)
											 * object.get("needAuthorise"));
											 * log.d("mytest",
											 * "NeedAuthorise="+chatBean
											 * .getNeedAuthorise());
											 * if(chatBean.getNeedAuthorise()){
											 * //需要授权
											 * chatBean.setIsApply((Boolean)
											 * object.get("isApply"));
											 * log.d("mytest",
											 * "isApply="+chatBean
											 * .getIsApply());
											 * if(chatBean.getIsApply()){ //已经申请
											 * chatBean.setFreshStatus((Boolean)
											 * object.get("freshStatus"));
											 * log.d("mytest",
											 * "freshStatus="+chatBean
											 * .getFreshStatus());
											 * chatBean.setApplyResult((Integer)
											 * object.get("applyResult"));
											 * log.d("mytest",
											 * "applyResult="+chatBean
											 * .getApplyResult());
											 * chatBean.setApplyReply((String)
											 * object.get("applyReply"));
											 * log.d("mytest",
											 * "applyReply="+chatBean
											 * .getApplyReply());
											 * chatBean.setArgument((String)
											 * object.get("argument"));
											 * log.d("mytest",
											 * "argument="+chatBean
											 * .getArgument());
											 * chatBean.setApplyId((String)
											 * object.get("applyId"));
											 * log.d("mytest",
											 * "applyId="+chatBean
											 * .getApplyId()); }else{ //未申请 }
											 * }else{ //不需要授权，直接创建 } //有觅聊
											 * chatBean
											 * .setSeekChatCount((Integer)
											 * object.get("seekChatCount"));
											 * log.d("mytest",
											 * "seekChatCount"+chatBean
											 * .getSeekChatCount());
											 * if(chatBean.getSeekChatCount() ==
											 * 0){ return; }
											 * //createAt是Date类型，timeChatStop为long类型
											 * ，取值是注意
											 * chatBean.setChatList((List<
											 * Map<String,
											 * Object>>)object.get("seekChats"
											 * )); log.d("mytest",
											 * "seekChats="+chatBean
											 * .getChatList()); for(int
											 * i=0;i<chatBean
											 * .getSeekChatCount();i++){
											 * SeekChatBean bean = new
											 * SeekChatBean();
											 * List<Map<String,Object>> members
											 * = (List<Map<String, Object>>)
											 * chatBean
											 * .getChatList().get(i).get(
											 * "members");
											 * bean.setMembers(members);
											 * log.d("mytest",
											 * "members"+members);
											 * bean.setConversationId((String)
											 * object.get("conversationId"));
											 * log.d("mytest",
											 * "conversationId"+bean
											 * .getConversationId()); AVUser
											 * avUser = (AVUser)
											 * object.get("creator");
											 * bean.setCreator
											 * (AVUser.cast(avUser,
											 * ObjUser.class)); log.d("mytest",
											 * "creator"+bean.getCreator());
											 * bean.setFolloweeCount((Integer)
											 * object.get("followeeCount"));
											 * log.d("mytest",
											 * "followeeCount"+bean
											 * .getFolloweeCount()); Date date =
											 * (Date) object.get("createdAt");
											 * bean.setCreateAt(date.getTime());
											 * log.d("mytest",
											 * "createdAt"+bean.getCreateAt());
											 * bean.setObjectId((String)
											 * object.get("objectId"));
											 * log.d("mytest",
											 * "objectId"+bean.getObjectId());
											 * bean.setPictureUrl((String)
											 * object.get("pictureUrl"));
											 * log.d("mytest",
											 * "pictureUrl"+bean.
											 * getPictureUrl());
											 * bean.setTitle((String)
											 * object.get("title"));
											 * log.d("mytest",
											 * "title"+bean.getTitle());
											 * bean.setTimeChatStop((Long)
											 * object.get("timeChatStop"));
											 * log.d("mytest",
											 * "timeChatStop"+bean
											 * .getTimeChatStop()); }
											 * 
											 * } });
											 */
				ObjFollowWrap.queryfollow(user, new ObjFunMapCallback() {

					@Override
					public void callback(Map<String, Object> map, AVException e) {
						// TODO Auto-generated method stub
						log.d("mytest", "follow" + map);
						// 相互关注的人ID列表
						List<String> boths = (List<String>) map.get("boths");
						// 我关注的人ID列表
						List<String> follow = (List<String>) map
								.get("followees");
						// 关注我的人ID列表
						List<String> followers = (List<String>) map
								.get("followers");
					}
				});
			}
		});
	}

}
