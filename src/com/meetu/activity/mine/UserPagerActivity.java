package com.meetu.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.R;
import com.meetu.adapter.ViewPagerAdapter;
import com.meetu.cloud.object.ObjUser;
import com.meetu.fragment.MinePersonalInformation;
import com.meetu.fragment.MinePhotoWallfragment;
import com.meetu.view.MyScrollView;
import com.meetu.view.MyScrollView.OnScrollListener;

public class UserPagerActivity extends FragmentActivity implements OnPageChangeListener,OnCheckedChangeListener,OnClickListener,OnScrollListener{
	private MyScrollView myScrollView;
	private ImageView backImv;
	private ImageView setImv;
	private ImageView userLikeImv;
	private ImageView userProfileImv;
	private ImageView userGenderImv;
	private ImageView userScripImv;
	private TextView userNameTv;
	private ImageView userVipImv;
	private ImageView userApproveImv;
	private RadioGroup userGroup;
	private ViewPager userPager;
	
	//fragment 滑动
	private List<Fragment> list = new ArrayList<Fragment>();
	private ViewPagerAdapter adapter=null;
	//网络 数据相关
	private BitmapUtils bitmapUtils; 
	private String headURl="";//头像的URL
	//拿本地的  user 
	private AVUser currentUser = AVUser.getCurrentUser();
	private String userId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userpager_layout);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		myScrollView = (MyScrollView) findViewById(R.id.scroll_content);
		backImv = (ImageView) findViewById(R.id.user_back_imv);
		setImv = (ImageView) findViewById(R.id.user_set_imv);
		userLikeImv = (ImageView) findViewById(R.id.user_like_imv);
		userProfileImv = (ImageView) findViewById(R.id.user_profile_iv);
		userGenderImv = (ImageView) findViewById(R.id.user_gender_imv);
		userScripImv = (ImageView) findViewById(R.id.user_scrip_imv);
		userNameTv = (TextView) findViewById(R.id.user_name_tv);
		userVipImv = (ImageView) findViewById(R.id.user_vip_dis);
		userApproveImv = (ImageView) findViewById(R.id.user_approve_dis);
		userGroup = (RadioGroup) findViewById(R.id.user_group_tab);
		userPager = (ViewPager) findViewById(R.id.user_viewpager);

		userGroup.setOnCheckedChangeListener(this);
		userPager.setOnPageChangeListener(this);
		initPageView();
		bitmapUtils=new BitmapUtils(getApplicationContext());
		if(currentUser!=null){
			//强制类型转换
			ObjUser user = AVUser.cast(currentUser, ObjUser.class);
			//此处为测试，应为对应用户头像
			headURl=user.getProfileClip().getUrl();
		}
		if(headURl!=null){
			// 加载网络图片
			bitmapUtils.display(userProfileImv, headURl);
		}

	}
	private void initPageView(){
		//个人信息fragment 并且把url传过去
		MinePersonalInformation personalInformation=new MinePersonalInformation();
		Bundle bundle1=new Bundle();
		bundle1.putString("personalInformationUrl", "");
		personalInformation.setArguments(bundle1);

		//照片墙fragment 并且把url传过去
		MinePhotoWallfragment photoWall=new MinePhotoWallfragment();
		Bundle bundle2=new Bundle();
		bundle2.putString("photoWallUrl", "");
		photoWall.setArguments(bundle2);

		list.add(personalInformation);
		list.add(photoWall);

		adapter = new ViewPagerAdapter(getSupportFragmentManager(), list);
		userPager.setAdapter(adapter);
		userPager.setOffscreenPageLimit(1);

	}
	private void setTag(int idx){
		RadioButton rb=(RadioButton)userGroup.getChildAt(idx);
		rb.setChecked(true);
		if(idx == 1) {
			myScrollView.setOnScrollListener(this);
		}
	}
	@Override
	public void onScroll(int scrollY) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onCheckedChanged(RadioGroup group, int position) {
		// TODO Auto-generated method stub
		userPager.setCurrentItem(position-1);
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
