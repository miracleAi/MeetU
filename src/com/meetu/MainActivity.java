package com.meetu;

import java.util.ArrayList;



import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.meetu.R;
import com.meetu.activity.LoginActivity;
import com.meetu.adapter.BoardPageFragmentAdapter;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.common.ChatConnection;
import com.meetu.db.TabDb;
import com.meetu.myapplication.MyApplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity implements OnTabChangeListener{

	private FragmentTabHost tabHost;
	//private ContentViewPager vpContent;
	private FrameLayout contentLayout;
	private ArrayList<Fragment> fragList=null;
	private BoardPageFragmentAdapter adapter;
	private String pageString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.getWindow();
		setContentView(R.layout.activity_main);
		
		tabHost=(FragmentTabHost)super.findViewById(android.R.id.tabhost);
		contentLayout=(FrameLayout)super.findViewById(R.id.contentLayout);
		tabHost.setup(this,super.getSupportFragmentManager(),R.id.contentLayout);
		tabHost.getTabWidget().setDividerDrawable(null);
		tabHost.setOnTabChangedListener(this);
		initTab();	
		pageString=super.getIntent().getStringExtra("page");
		
	//	isConnection();
		ChatConnection.isConnection();
		
	}
	
	private void initTab(){
		String tabs[]=TabDb.getTabsTxt();
		for(int i=0;i<tabs.length;i++){
			TabSpec tabSpec=tabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i));
			tabHost.addTab(tabSpec,TabDb.getFragments()[i],null);
			tabHost.setTag(i);		
		}
	}
	private View getTabView(int idx){
		View view=LayoutInflater.from(this).inflate(R.layout.footer_tabs,null);
		((TextView)view.findViewById(R.id.tvTab)).setText(TabDb.getTabsTxt()[idx]);
		if(idx==0){
			((TextView)view.findViewById(R.id.tvTab)).setTextColor(this.getResources().getColor(R.color.tablebar_check));
			((ImageView)view.findViewById(R.id.ivImg)).setImageResource(TabDb.getTabsImgLight()[idx]);
		}else{
			((ImageView)view.findViewById(R.id.ivImg)).setImageResource(TabDb.getTabsImg()[idx]);
		}
		return view;
	}

	

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		updateTab();
	}
	
	private void updateTab(){
		TabWidget tabw=tabHost.getTabWidget();
		for(int i=0;i<tabw.getChildCount();i++){
			View view=tabw.getChildAt(i);
			ImageView iv=(ImageView)view.findViewById(R.id.ivImg);
			if(i==tabHost.getCurrentTab()){
				((TextView)view.findViewById(R.id.tvTab)).setTextColor(this.getResources().getColor(R.color.tablebar_check));
				iv.setImageResource(TabDb.getTabsImgLight()[i]);
			}else{
				((TextView)view.findViewById(R.id.tvTab)).setTextColor(getResources().getColor(R.color.foot_txt_gray));
				iv.setImageResource(TabDb.getTabsImg()[i]);
			}
			
		}
	}
	/**
	 * 按两次退出
	 */
	private long endTime=0;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(System.currentTimeMillis()-endTime>3000){
			Toast.makeText(this, "再按一次返回键退出程序",Toast.LENGTH_LONG).show();
			endTime=System.currentTimeMillis();
		}else{
			System.exit(0);
		}
	}
	
	/**
	 * 
	 * 判断是否连接了长连接  
	 * @author lucifer
	 * @date 2015-11-24
	 */
//	private void isConnection(){
//		chatClient = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
//		ObjChatMessage.getClientStatus(MyApplication.chatClient, new ObjFunBooleanCallback() {
//			
//			@Override
//			public void callback(boolean result, AVException e) {
//				// TODO Auto-generated method stub
//				if(e!=null){
//					log.e("zcq", e);
//					return;
//				}else if(result){
//					log.e("zcq", "已经建立过长连接");
//				}else{
//					ObjChatMessage.connectToChatServer(MyApplication.chatClient, new ObjAvimclientCallback() {
//						
//						@Override
//						public void callback(AVIMClient client, AVException e) {							
//							if(e != null){
//								log.e("zcq", e);
//								return ;
//							}
//							if(client != null){
//								MyApplication.chatClient = client;
//								log.e("zcq", "连接聊天长连接成功");
//								
//							}else{
//								log.e("zcq", "连接聊天长连接失败");
//								
//							}
//						}
//					});
//					
//				}
//			}
//		});
//	}

}
