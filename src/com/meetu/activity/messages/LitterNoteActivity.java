package com.meetu.activity.messages;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.baidu.location.e.v;
import com.meetu.adapter.GridRecycleLitterNoteAdapter;
import com.meetu.adapter.GridRecycleLitterNoteAdapter.OnLitterNotesItemClickCallBack;
import com.meetu.adapter.GridRecycleMiLiaoInfoAdapter;
import com.meetu.cloud.callback.ObjScripBoxCallback;
import com.meetu.cloud.object.ObjScripBox;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjScriptWrap;
import com.meetu.entity.LitterNotes;
import com.meetu.sqlite.UserShieldDao;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LitterNoteActivity extends Activity implements
		OnLitterNotesItemClickCallBack, OnClickListener {

	private List<LitterNotes> mdata = new ArrayList<LitterNotes>();
	private GridRecycleLitterNoteAdapter mAdapter;
	private RecyclerView mRecyclerView;

	// 控件相关
	private RelativeLayout backLayout;

	// 网络数据相关
	private AVUser currentUser = AVUser.getCurrentUser();
	// 当前用户
	private ObjUser user = new ObjUser();
	private List<ObjScripBox> objScripBoxList = new ArrayList<ObjScripBox>();

	private List<ObjUser> objUsersList = new ArrayList<ObjUser>();// 数组，包含小纸条发送双方
	private UserShieldDao shieldDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_litter_note);
		if (currentUser != null) {
			user = AVUser.cast(currentUser, ObjUser.class);
		} else {
			return;
		}

		// loadData();
		shieldDao = new UserShieldDao(this);
		getScripBoxs();
		initView();
		initAdapter();

	}

	private void initAdapter() {
		// TODO Auto-generated method stub
		mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		mAdapter = new GridRecycleLitterNoteAdapter(this, objScripBoxList);

		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemClickLitenerBack(this);
	}

	private void initView() {
		mRecyclerView = (RecyclerView) super
				.findViewById(R.id.recycleview_litter_notes);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_litter_notes_rl);
		backLayout.setOnClickListener(this);

	}


	/**
	 * item 点击
	 */
	@Override
	public void onItemClick(int id) {
		log.e("lucifer", "id=" + id);
		boolean isShield = false;
		ObjScripBox box = objScripBoxList.get(id);
		if(user.getObjectId().equals(box.getUsers().get(0))){
			isShield = shieldDao.queryIsShield(box.getUsers().get(1).getObjectId(), user.getObjectId());
		}else{
			isShield = shieldDao.queryIsShield(objScripBoxList.get(id).getUsers().get(0).getObjectId(), user.getObjectId());
		}
		if(isShield){
			Toast.makeText(LitterNoteActivity.this, "您已被对方用户屏蔽", 1000).show();
		}else{
			Intent intent = new Intent(this, NotesActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ObjScripBox", box);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public void onItemLongClick(View view, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_litter_notes_rl:

			finish();

			break;

		default:
			break;
		}

	}

	// 获取所有纸条箱
	public void getScripBoxs() {
		ObjScriptWrap.queryScripBox(user, new ObjScripBoxCallback() {

			@Override
			public void callback(List<ObjScripBox> objects, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					log.e("zcq", "纸条箱获取失败");
					return;
				}
				if (objects.size() > 0) {

					// log.e("zcq 对话id", ""+objects.get(0).getConversationId());
					objScripBoxList = new ArrayList<ObjScripBox>();

					objScripBoxList.addAll(objects);

					log.e("objects.size()", "objects.size()==" + objects.size()
							+ "  objScripBoxList==" + objScripBoxList.size());
					handler.sendEmptyMessage(1);

				} else {
					log.e("zcq", "小纸条箱为0");
				}
			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				log.e("zcq", "刷新了");
				// mAdapter.notifyDataSetChanged();

				initAdapter();

				break;
			}
		}

	};

}
