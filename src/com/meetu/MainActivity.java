package com.meetu;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.meetu.activity.LoginActivity;
import com.meetu.adapter.BoardPageFragmentAdapter;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjFollowWrap;
import com.meetu.common.ChatConnection;
import com.meetu.common.Constants;
import com.meetu.db.TabDb;
import com.meetu.entity.UserAbout;
import com.meetu.fragment.MineUpfragment;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.sqlite.UserDao;
import com.meetu.tools.SystemBarTintManager;
import com.meetu.view.ScrollTabHolder;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity implements
		OnTabChangeListener {

	private FragmentTabHost tabHost;
	// private ContentViewPager vpContent;
	private FrameLayout contentLayout;
	private ArrayList<Fragment> fragList = null;
	private BoardPageFragmentAdapter adapter;
	private String pageString;

	public static MineUpfragment fMineUpfragment;
	
	// 当前用户
		ObjUser user = new ObjUser();
		AVUser currentUser = AVUser.getCurrentUser();
	UserAboutDao userAboutDao;
	UserDao userDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.getWindow();
		// 状态栏相关
		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		 * setTranslucentStatus(true); SystemBarTintManager tintManager = new
		 * SystemBarTintManager(this);
		 * tintManager.setStatusBarTintEnabled(true);
		 * tintManager.setStatusBarTintResource(R.color.touming);//通知栏所需颜色 }
		 */
		setContentView(R.layout.activity_main);

		tabHost = (FragmentTabHost) super.findViewById(android.R.id.tabhost);
		contentLayout = (FrameLayout) super.findViewById(R.id.contentLayout);
		tabHost.setup(this, super.getSupportFragmentManager(),
				R.id.contentLayout);
		tabHost.getTabWidget().setDividerDrawable(null);
		tabHost.setOnTabChangedListener(this);
		initTab();
		pageString = super.getIntent().getStringExtra("page");

		// fMineUpfragment=(MineUpfragment)
		// MainActivity.this.getSupportFragmentManager().getFragments().get(3);

		// fMineUpfragment=new MineUpfragment();

		ChatConnection.isConnection();
		userAboutDao=new UserAboutDao(this);
		userDao=new UserDao(this);
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		
		getMyFollowUser();

	}

	// 状态栏相关
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		// 获得fragment实例
		log.e("zcq",
				"fragment.toString()==" + fragment.toString().subSequence(0, 6));
		super.onAttachFragment(fragment);
		if (fragment.toString().subSequence(0, 6).equals("MineUp")) {
			log.e("zcq", "实例化fragment成功");
			fMineUpfragment = (MineUpfragment) fragment;
			return;
		}
	}

	private void initTab() {
		String tabs[] = TabDb.getTabsTxt();
		for (int i = 0; i < tabs.length; i++) {
			TabSpec tabSpec = tabHost.newTabSpec(tabs[i]).setIndicator(
					getTabView(i));
			tabHost.addTab(tabSpec, TabDb.getFragments()[i], null);
			tabHost.setTag("" + i);
		}
	}

	private View getTabView(int idx) {
		View view = LayoutInflater.from(this).inflate(R.layout.footer_tabs,
				null);
		((TextView) view.findViewById(R.id.tvTab))
				.setText(TabDb.getTabsTxt()[idx]);
		if (idx == 0) {
			((TextView) view.findViewById(R.id.tvTab)).setTextColor(this
					.getResources().getColor(R.color.tablebar_check));
			((ImageView) view.findViewById(R.id.ivImg)).setImageResource(TabDb
					.getTabsImgLight()[idx]);
		} else {
			((ImageView) view.findViewById(R.id.ivImg)).setImageResource(TabDb
					.getTabsImg()[idx]);
		}
		return view;
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		updateTab();
	}

	private void updateTab() {
		TabWidget tabw = tabHost.getTabWidget();
		for (int i = 0; i < tabw.getChildCount(); i++) {
			View view = tabw.getChildAt(i);
			ImageView iv = (ImageView) view.findViewById(R.id.ivImg);
			if (i == tabHost.getCurrentTab()) {
				((TextView) view.findViewById(R.id.tvTab)).setTextColor(this
						.getResources().getColor(R.color.tablebar_check));
				iv.setImageResource(TabDb.getTabsImgLight()[i]);
			} else {
				((TextView) view.findViewById(R.id.tvTab))
						.setTextColor(getResources().getColor(
								R.color.foot_txt_gray));
				iv.setImageResource(TabDb.getTabsImg()[i]);
			}

		}
	}

	/**
	 * 按两次退出
	 */
	private long endTime = 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (System.currentTimeMillis() - endTime > 3000) {
			Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_LONG).show();
			endTime = System.currentTimeMillis();
		} else {
			System.exit(0);
		}
	}

	public static Fragment getFragment() {
		// List<Fragment>
		// list=MainActivity.this.getSupportFragmentManager().getFragments();
		// return list.get(3);

		return fMineUpfragment;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
			finish();
		}
	}
	
	/**
	 * 获得我关注过的用户的列表
	 *   
	 * @author lucifer
	 * @date 2015-12-7
	 */
	public void getMyFollowUser(){
		log.e("zcq", "正在加载我关注的人");
		ObjFollowWrap.getFollowee(user, new ObjUserCallback() {
			
			@Override
			public void callback(List<ObjUser> objects, AVException e) {
				if(e!=null){
					log.e("zcq", e);
					return;
				}
				if(objects==null){
					
				}else{
					
					log.e("zcq", "objects.size()=="+objects.size());
					ArrayList<UserAboutBean> userAboutBeanList;
					userAboutBeanList=new ArrayList<UserAboutBean>();
					for(int i=0;i<objects.size();i++){
						UserAboutBean userAboutBean=new UserAboutBean();
						userAboutBean.setUserId(user.getObjectId());
						userAboutBean.setAboutColetctionId("");
						userAboutBean.setAboutType(Constants.FOLLOW_TYPE);
						userAboutBean.setAboutUserId(objects.get(i).getObjectId());
						userAboutBeanList.add(userAboutBean);
						
						userDao.insertOrReplaceUser(objects.get(i));
					}
					
					userAboutDao.saveUserAboutList(userAboutBeanList);
					
				}
				
				
			}
		});
		
	}

}
