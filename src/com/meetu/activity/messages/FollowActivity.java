package com.meetu.activity.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.meetu.adapter.ViewPagerAdapter;
import com.meetu.cloud.callback.ObjFunMapCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjFollowWrap;
import com.meetu.fragment.BothFollowFragment;
import com.meetu.fragment.MyFollowFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class FollowActivity extends FragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener {
	private ImageView backImvl;
	private RadioGroup titleGroup;
	private ViewPager followPager;

	private ViewPagerAdapter pagerAdapter;
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	private ArrayList<String> followList = new ArrayList<String>();
	private ArrayList<String> bothList = new ArrayList<String>();
	private ArrayList<String> followerList = new ArrayList<String>();

	private ObjUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_follow_layout);
		user = AVUser.cast(AVUser.getCurrentUser(), ObjUser.class);
		initView();
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backImvl = (ImageView) findViewById(R.id.back_follow_img);
		titleGroup = (RadioGroup) findViewById(R.id.group_follow_tab);
		followPager = (ViewPager) findViewById(R.id.follow_viewpager);
		backImvl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		titleGroup.setOnCheckedChangeListener(this);
		followPager.setOnPageChangeListener(this);
		titleGroup.check(1);
	}

	private void initViewpager() {
		// TODO Auto-generated method stub
		MyFollowFragment myFollowFragment = new MyFollowFragment();
		Bundle bundle1 = new Bundle();
		// bundle1.putString("myFollowList", followList);
		bundle1.putSerializable("myFollowList", followList);
		bundle1.putInt("followerCount", followerList.size());
		myFollowFragment.setArguments(bundle1);

		BothFollowFragment bothFollowFragment = new BothFollowFragment();
		Bundle bundle2 = new Bundle();
		// bundle2.putString("bothFollowList", "");
		bundle2.putSerializable("bothFollowList", bothList);
		bothFollowFragment.setArguments(bundle2);

		fragmentList.add(myFollowFragment);
		fragmentList.add(bothFollowFragment);

		pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
				fragmentList);
		followPager.setAdapter(pagerAdapter);
		followPager.setOffscreenPageLimit(1);
	}

	public void setTag(int idx) {
		RadioButton rb = (RadioButton) titleGroup.getChildAt(idx);
		rb.setChecked(true);
	}

	private void loadData() {
		// TODO Auto-generated method stub
		ObjFollowWrap.queryfollow(user, new ObjFunMapCallback() {

			@Override
			public void callback(Map<String, Object> map, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					Toast.makeText(getApplicationContext(), "请求关注列表失败", 1000)
							.show();
					return;
				}
				List<String> followL = (List<String>) map.get("followees");
				if (followL != null && followL.size() > 0) {
					followList.addAll(followL);
				}
				List<String> followerL = (List<String>) map.get("followers");
				if (followerL != null && followerL.size() > 0) {
					followerList.addAll(followerL);
				}
				List<String> bothL = (List<String>) map.get("boths");
				if (bothL != null && bothL.size() > 0) {
					bothList.addAll(bothL);
				}
				initViewpager();
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int position) {
		// TODO Auto-generated method stub
		followPager.setCurrentItem(position - 1);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		setTag(position);
	}

}
