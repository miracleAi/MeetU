package com.meetu.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.meetu.R;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.adapter.FollowAdapter;
import com.meetu.cloud.callback.ObjFunMapCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjFollowWrap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyFollowFragment extends Fragment implements OnRefreshListener2<ListView>{
	TextView followCountTv,followerCountTv;
	PullToRefreshListView followLv;
	ArrayList<String> followStrList = new ArrayList<String>();
	FollowAdapter myFollowAdapter ;
	ObjUser user ;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		user = AVUser.cast(AVUser.getCurrentUser(), ObjUser.class);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_myfollow_layout, null);
		followCountTv = (TextView) view.findViewById(R.id.myfollow_count_tv);
		followerCountTv = (TextView) view.findViewById(R.id.follower_count_tv);
		followLv = (PullToRefreshListView) view.findViewById(R.id.follow_lv);
		initView();
		return view;
	}
	private void initView() {
		// TODO Auto-generated method stub
		ArrayList<String> list = (ArrayList<String>) getArguments().getSerializable("myFollowList");
		int count = getArguments().getInt("followerCount");
		if(list != null){
			followStrList.clear();
			followStrList.addAll(list);
			followCountTv.setText(" "+followStrList.size()+"人");
			followerCountTv.setText(" "+count+"人");
		}else{
			followCountTv.setText(" 0人");
			followerCountTv.setText(" 0人");
		}
		myFollowAdapter = new FollowAdapter(getActivity(), followStrList);
		followLv.setAdapter(myFollowAdapter);
		followLv.setMode(Mode.PULL_FROM_START);
		followLv.setOnRefreshListener(this);
		
		followLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),UserPagerActivity.class);
				intent.putExtra("userId", followStrList.get(position-1));
				startActivity(intent);
			}
		});
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		ObjFollowWrap.queryfollow(user, new ObjFunMapCallback() {

			@Override
			public void callback(Map<String, Object> map, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					Toast.makeText(getActivity(), "请求关注列表失败", 1000).show();
					return;
				}
				List<String> followL = (List<String>) map.get("followees");
				if(followL != null && followL.size()>0){
					followStrList.clear();
					followStrList.addAll(followL);
					followCountTv.setText(" "+followStrList.size()+"人");
					myFollowAdapter.notifyDataSetChanged();
				}
				List<String> followerL = (List<String>) map.get("followers");
				if(followerL != null && followerL.size()>0){
					followerCountTv.setText(" "+followerL.size()+"人");
				}
				followLv.onRefreshComplete();
			}
		});
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

}
