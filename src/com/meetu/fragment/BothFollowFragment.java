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
import com.meetu.bean.UserBean;
import com.meetu.cloud.callback.ObjFunMapCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjFollowWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.sqlite.UserDao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BothFollowFragment extends Fragment implements
		OnRefreshListener2<ListView> {
	TextView bothCountTv;
	PullToRefreshListView bothLv;
	ArrayList<String> bothStrList = new ArrayList<String>();
	FollowAdapter bothAdapter;
	ObjUser user;

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
		View view = inflater
				.inflate(R.layout.fragment_both_follow_layout, null);
		bothCountTv = (TextView) view.findViewById(R.id.both_count_tv);
		bothLv = (PullToRefreshListView) view.findViewById(R.id.both_follow_lv);
		initView();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		ArrayList<String> list = (ArrayList<String>) getArguments()
				.getSerializable("bothFollowList");
		if (list != null) {
			bothStrList.clear();
			bothStrList.addAll(list);
			bothCountTv.setText(" " + bothStrList.size() + "人");
		}
		bothAdapter = new FollowAdapter(getActivity(), bothStrList);
		bothLv.setAdapter(bothAdapter);
		bothLv.setMode(Mode.PULL_FROM_START);
		bothLv.setOnRefreshListener(this);

		bothLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						UserPagerActivity.class);
				intent.putExtra("userId", bothStrList.get(position - 1));
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
				if (e != null) {
					Toast.makeText(getActivity(), "请求关注列表失败", 1000).show();
					return;
				}
				List<String> bothL = (List<String>) map.get("boths");
				if (bothL != null && bothL.size() > 0) {
					bothStrList.clear();
					bothStrList.addAll(bothL);
					bothCountTv.setText(" " + bothStrList.size() + "人");
					bothAdapter.notifyDataSetChanged();
				}
				bothLv.onRefreshComplete();
			}
		});
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}
}
