package com.meetu.activity.miliao;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVUser;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.adapter.MiLiaoUsersListAdapter;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;
import com.meetu.entity.User;
import com.meetu.sqlite.UserAboutDao;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MiLiaoUsersListActivity extends Activity implements
		OnClickListener {
	private List<User> usersList = new ArrayList<User>();
	private MiLiaoUsersListAdapter adapter;
	private ListView mlistView;

	private List<UserAboutBean> userAboutBeansList=new ArrayList<UserAboutBean>();
	UserAboutDao userAboutDao;
	String conversationId="";
	// 控件相关
	private RelativeLayout backLayout;
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser userMy = new ObjUser();
	private TextView numberAllTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_mi_liao_users_list);
		
		conversationId=getIntent().getStringExtra("conversationId");
		if(currentUser!=null){
			userMy = AVUser.cast(currentUser, ObjUser.class);
		}
		
		userAboutDao=new UserAboutDao(this);
	
		
		initView();
		initAdapter();
		loadData();
	}

	private void initAdapter() {
		
		adapter = new MiLiaoUsersListAdapter(this, userAboutBeansList);
		mlistView.setAdapter(adapter);
		
		mlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MiLiaoUsersListActivity.this,UserPagerActivity.class);
				intent.putExtra("userId", userAboutBeansList.get(position).getAboutUserId());
				startActivity(intent);
			}
		});
		
	}

	private void initView() {
		mlistView = (ListView) super
				.findViewById(R.id.listview_all_miliao_userlist);
		
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_miliao_UsersList_rl);
		backLayout.setOnClickListener(this);
		numberAllTextView=(TextView) findViewById(R.id.number_miliao_UsersList_tv);

	}

	private void loadData() {
		List<UserAboutBean> list=userAboutDao.queryUserAbout(userMy.getObjectId(), Constants.CONVERSATION_TYPE, conversationId);
		if(list!=null){
			userAboutBeansList.addAll(list);
		}
	

		numberAllTextView.setText(""+userAboutBeansList.size());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_miliao_UsersList_rl:
			finish();

			break;

		default:
			break;
		}

	}

}
