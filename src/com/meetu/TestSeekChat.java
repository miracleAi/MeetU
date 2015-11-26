package com.meetu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.bean.SeekChatBean;
import com.meetu.bean.SeekChatInfoBean;
import com.meetu.cloud.callback.ObjFunMapCallback;
import com.meetu.cloud.callback.ObjFunObjectCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatWrap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestSeekChat extends Activity{
	Button clickBtn;
	TextView infoTv;
	ObjUser user = null;
	SeekChatInfoBean chatBean = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_msg2_layout);
		if(ObjUser.getCurrentUser() != null){
			AVUser currentUser = ObjUser.getCurrentUser();
			user = AVUser.cast(currentUser, ObjUser.class);
		}else{
			Toast.makeText(getApplicationContext(), "please login", 1000).show();
		}
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		clickBtn = (Button) findViewById(R.id.click);
		infoTv = (TextView) findViewById(R.id.info_tv);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ObjChatWrap.queryChatInfo(user, System.currentTimeMillis(), 999, new ObjFunMapCallback() {
					
					@Override
					public void callback(Map<String, Object> object, AVException e) {
						// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						if(e != null){
							return;
						}
						chatBean = new SeekChatInfoBean();
						log.d("mytest", "result="+object);
						chatBean.setNeedAuthorise((Boolean) object.get("needAuthorise"));
						log.d("mytest", "NeedAuthorise="+chatBean.getNeedAuthorise());
						if(chatBean.getNeedAuthorise()){
							//需要授权
							chatBean.setIsApply((Boolean) object.get("isApply"));
							log.d("mytest", "isApply="+chatBean.getIsApply());
							if(chatBean.getIsApply()){
								//已经申请
								chatBean.setFreshStatus((Boolean) object.get("freshStatus"));
								log.d("mytest", "freshStatus="+chatBean.getFreshStatus());
								chatBean.setApplyResult((Integer) object.get("applyResult"));
								log.d("mytest", "applyResult="+chatBean.getApplyResult());
								chatBean.setApplyReply((String) object.get("applyReply"));
								log.d("mytest", "applyReply="+chatBean.getApplyReply());
								chatBean.setArgument((String) object.get("argument"));
								log.d("mytest", "argument="+chatBean.getArgument());
								chatBean.setApplyId((String) object.get("applyId"));
								log.d("mytest", "applyId="+chatBean.getApplyId());
							}else{
								//未申请
							}
						}else{
							//不需要授权，直接创建
						}
						//有觅聊 
						chatBean.setSeekChatCount((Integer) object.get("seekChatCount"));
						log.d("mytest", "seekChatCount"+chatBean.getSeekChatCount());
						if(chatBean.getSeekChatCount() >0){
							chatBean.setChatList((List<Map<String, Object>>)object.get("seekChats"));
							log.d("mytest", "seekChats="+chatBean.getChatList());
							List<Map<String, Object>> members = (List<Map<String, Object>>) chatBean.getChatList().get(0).get("members");
							//log.d("mytest", "members"+members);
						}
					
					}
				});
			}
		});
	}

}
