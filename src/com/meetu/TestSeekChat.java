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
			public void onClick(View arg0) {
				ObjFollowWrap.queryfollow(user, new ObjFunMapCallback() {

					@Override
					public void callback(Map<String, Object> map, AVException e) {
						// TODO Auto-generated method stub
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
