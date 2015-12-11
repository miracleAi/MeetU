package com.meetu.activity.mine;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil.log;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.meetu.adapter.LikesListviewAdapter;
import com.meetu.adapter.NewsListViewAdapter;
import com.meetu.bean.FavorBean;
import com.meetu.cloud.callback.FavorBeanCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.entity.Huodong;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class FavorListActivity extends Activity implements
OnRefreshListener2<ListView>, OnItemClickListener, OnClickListener {
	private PullToRefreshListView lvNewsList;
	private LikesListviewAdapter adapter;
	private List<FavorBean> userList = new ArrayList<FavorBean>();
	private ImageView back;
	private TextView countTv;
	private ObjUserPhoto userPhoto = new ObjUserPhoto();
	
	private RelativeLayout noneOrFailLayout;
	private TextView noneTextView;
	private TextView failTextView;
	SharedPreferences sp;
	long scanTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_favor_list);
		if(getIntent().getSerializableExtra("photo")!= null){
			userPhoto = (ObjUserPhoto) getIntent().getSerializableExtra("photo");
		}
		sp = getSharedPreferences("favorHisPos",Context.MODE_PRIVATE);
		scanTime = sp.getLong("scanTime", 0);
		sp.edit().putLong("scanTime", System.currentTimeMillis()).commit();
		log.d("mytest", "put"+sp.getLong("scanTime", 0));
		initView();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		ObjUserPhotoWrap.queryPhotoPraiseUsers(userPhoto, new FavorBeanCallback() {
			
			@Override
			public void callback(List<FavorBean> objects, AVException e) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				if(e != null){
					log.e("zcq", e);
					noneOrFailLayout.setVisibility(View.VISIBLE);
					noneTextView.setVisibility(View.GONE);
					failTextView.setVisibility(View.VISIBLE);
				}
				if(objects != null && objects.size()>0){
					noneOrFailLayout.setVisibility(View.GONE);
				
					List<FavorBean> list = objects;
					List<FavorBean> newList = new ArrayList<FavorBean>();
					List<FavorBean> hisList = new ArrayList<FavorBean>();
					log.d("mytest", "last===="+scanTime);
					for(int i=0;i<list.size();i++){
						FavorBean bean = list.get(i);
						if(bean.getPraiseTime()>scanTime){
							log.d("mytest", "new===="+bean.getPraiseTime());
							newList.add(bean);
						}else{
							log.d("mytest", "his===="+bean.getPraiseTime());
							hisList.add(bean);
						}
					}
					userList.clear();
					userList.addAll(newList);
					if(hisList.size()>0 && newList.size()>0){
						log.d("mytest", "zhixing");
						FavorBean hisItem = new FavorBean();
						hisItem.setUser(new ObjUser());
						hisItem.setPraiseTime(0);
						userList.add(hisItem);
					}
					userList.addAll(hisList);
					countTv.setText(""+userList.size());
					adapter.notifyDataSetChanged();
				}else{
					noneOrFailLayout.setVisibility(View.VISIBLE);
					noneTextView.setVisibility(View.VISIBLE);
					failTextView.setVisibility(View.GONE);
				}
				lvNewsList.onRefreshComplete();
			
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		noneOrFailLayout=(RelativeLayout) findViewById(R.id.none_or_favour_list_fragment_rl);
		noneTextView=(TextView) findViewById(R.id.none_favour_list_tv);
		failTextView=(TextView) findViewById(R.id.fail_favour_list_tv);
		back = (ImageView) findViewById(R.id.back_favorlist_mine_img);
		back.setOnClickListener(this);
		countTv = (TextView) findViewById(R.id.favornumber_likelist_mine);
		lvNewsList = (PullToRefreshListView) super
				.findViewById(R.id.newlikeslist);
		adapter = new LikesListviewAdapter(this, userList);
		lvNewsList.setAdapter(adapter);
		lvNewsList.setMode(Mode.PULL_FROM_START);
		lvNewsList.setOnRefreshListener(this);
		lvNewsList.setOnItemClickListener(this);
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(FavorListActivity.this,UserPagerActivity.class);
		intent.putExtra("userId", userList.get(position-1).getUser().getObjectId());
		startActivity(intent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		initData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_favorlist_mine_img:
			finish();
			break;

		default:
			break;
		}

	}

}
