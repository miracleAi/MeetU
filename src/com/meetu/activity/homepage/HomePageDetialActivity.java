package com.meetu.activity.homepage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.adapter.PhotoPagerAdapter;
import com.meetu.baidumapdemo.BaiduMapMainActivity;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjActivityCoverCallback;
import com.meetu.cloud.callback.ObjActivityPhotoCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.wrap.ObjActivityCoverWrap;
import com.meetu.cloud.wrap.ObjActivityPhotoWrap;
import com.meetu.entity.PhotoWall;
import com.meetu.entity.Photolunbo;
import com.meetu.tools.DensityUtil;
import com.meetu.view.MyScrollView;
import com.meetu.view.MyScrollView.OnScrollListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomePageDetialActivity extends Activity
		implements
			OnPageChangeListener,
			OnClickListener ,OnScrollListener{
	//控件相关
	private ViewPager viewPager;
	private PhotoPagerAdapter adapter;
	private List<Photolunbo> photolist = new ArrayList<Photolunbo>();
	private List<ImageView> dotViewsList = new ArrayList<ImageView>();
	private ScheduledExecutorService scheduledExecutorService;
	private int currentItem = 0;

	private LinearLayout mLinearLayout;
	private WebView webView;
	private RelativeLayout backLayout;
	private ImageView baiduMap,join,over,backFeed,memory,barrage;
	
	private MyScrollView mScrollView;
	private int mhighty;//滑动的高度y,改变标题栏背景透明度
	
	private RelativeLayout topLayout;
	private TextView titleTop;
	private String style;
	
	private TextView address;
	//网络数据相关
	private ActivityBean activityBean=new ActivityBean();
	private ObjActivity objActivity= null;
	
	//轮播图
	private List<ObjActivityCover> objPhotosList=new ArrayList<ObjActivityCover>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_home_page_detial);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		activityBean= (ActivityBean) bundle.getSerializable("activityBean");
//		objActivity=(ObjActivity) bundle.getSerializable("objActivity");
		initLoadActivity(activityBean.getActyId());
		initView();
//		startPlay();
		initLoad();
		mhighty=DensityUtil.dip2px(this, 250-44);
		
	}
private void initLoadActivity(String activityId) {
	log.e("zcq", "activityId=="+activityId);
		try {
			 objActivity=AVObject.createWithoutData(ObjActivity.class, activityId);
//			ObjActivityCoverWrap.queryActivityCover(objActivity, new ObjActivityCoverCallback() {
//				
//				@Override
//				public void callback(List<ObjActivityCover> objects, AVException e) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
/**
 * 根据活动状态初始化界面
 */
	private void initLoad() {
		
		if(activityBean.getStatus()==70){
			
			over.setVisibility(View.VISIBLE);
			join.setFocusable(false);
			join.setVisibility(View.INVISIBLE);
			barrage.setFocusable(false);
			barrage.setVisibility(View.INVISIBLE);
			memory.setFocusable(true);
			memory.setVisibility(View.VISIBLE);
			backFeed.setFocusable(true);
			backFeed.setVisibility(View.VISIBLE);
		}
		
	}

	private void initView() {
		// 先加载数据在设置adapter
		viewPager = (ViewPager) super.findViewById(R.id.viewpager_photo);
		load();
		adapter = new PhotoPagerAdapter(this, objPhotosList);
//		viewPager.setOnPageChangeListener(this);

		viewPager.setAdapter(adapter);
		mLinearLayout = (LinearLayout) super
				.findViewById(R.id.dian_linearlayout_homepage);

		for(int i=0;i<photolist.size();i++){
			ImageView viewDot = new ImageView(this);
			if (i == 0) {
				viewDot.setBackgroundResource(R.drawable.main_dot_white);
			} else {
				viewDot.setBackgroundResource(R.drawable.main_dot_light);
			}
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			params.rightMargin=10;
			viewDot.setLayoutParams(params);
			dotViewsList.add(viewDot);
			mLinearLayout.addView(viewDot);
		}
		/**
		 * vebview相关 加载网页
		 */
		webView = (WebView) super.findViewById(R.id.contentWebView);
		webView.loadUrl(""+activityBean.getActivityContent());
		
		//控件点击相关
		backLayout=(RelativeLayout) super.findViewById(R.id.back_homepage_detial_rl);
		backLayout.setOnClickListener(this);
		baiduMap=(ImageView) super.findViewById(R.id.baidumap_homepage_detial);
		baiduMap.setOnClickListener(this);
		
		over=(ImageView) super.findViewById(R.id.over_homepager_detial_img);
		//报名
		join=(ImageView) super.findViewById(R.id.join_homepager_detial_img);
		join.setOnClickListener(this);
		//弹幕
		barrage=(ImageView) super.findViewById(R.id.barrage_homepage_detial_img);
		barrage.setOnClickListener(this);
		//反馈
		backFeed=(ImageView) super.findViewById(R.id.feedback_homepage_detial_img);
		backFeed.setOnClickListener(this);
		//回忆
		memory=(ImageView) super.findViewById(R.id.memory_homepager_detial_img);
		memory.setOnClickListener(this);
		
		mScrollView=(MyScrollView) super.findViewById(R.id.scrollview_homepage);
		mScrollView.setOnScrollListener(this);
		topLayout=(RelativeLayout) super.findViewById(R.id.top_homepage_detial_rl);
		titleTop=(TextView) super.findViewById(R.id.title_top_homepager_detial_tv);
		
		address=(TextView) super.findViewById(R.id.address_home_page_detial_tv);
		address.setText(""+activityBean.getLocationAddress()+"  "+activityBean.getLocationGovernment());
	}
/**
 * 加载网络数据
 *   
 * @author lucifer
 * @date 2015-11-11
 */
	private void load() {
//		photolist = new ArrayList<Photolunbo>();
//
//		photolist.add(new Photolunbo("1", R.drawable.acty_show_img_banner));
//		photolist.add(new Photolunbo("2", R.drawable.acty_show_img_banner));
//		photolist.add(new Photolunbo("3", R.drawable.acty_show_img_banner));
//		photolist.add(new Photolunbo("4", R.drawable.acty_show_img_banner));
//		photolist.add(new Photolunbo("5", R.drawable.acty_show_img_banner));
		
		objPhotosList=new ArrayList<ObjActivityCover>();
		
		ObjActivityCoverWrap.queryActivityCover(objActivity, new ObjActivityCoverCallback() {

			@Override
			public void callback(List<ObjActivityCover> objects, AVException e) {
				if(e!=null){
										log.e("zcq", e);
									}else{
										objPhotosList.addAll(objects);
									log.e("zcq", "objPhotosList=="+objPhotosList.size());
										myhandler.sendEmptyMessage(1);
									}
					 			}
					 		});
		

	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < dotViewsList.size(); i++) {
			if (i == selectItems) {
				dotViewsList.get(i).setBackgroundResource(
						R.drawable.main_dot_white);
			} else {
				dotViewsList.get(i).setBackgroundResource(
						R.drawable.main_dot_light);
			}
		}
	}

	@Override
	public void onClick(View v) {
switch (v.getId()) {
	case R.id.back_homepage_detial_rl :
		finish();
		
		break;
	case R.id.baidumap_homepage_detial:
		Intent intent=new Intent(this,BaiduMapMainActivity.class);
		startActivity(intent);
		break;
	case R.id.join_homepager_detial_img:
		switch (activityBean.getStatus()) {
		case 40:
			Toast.makeText(this, "活动报名已结束", Toast.LENGTH_SHORT).show();
			break;
		case 50:
			Intent intent2=new Intent(this,JoinActivity.class);
			startActivity(intent2);
			break;
		case 60:
			Toast.makeText(this, "活动报名已结束", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		
		break;
	case R.id.memory_homepager_detial_img:
		Intent intent3=new Intent(this,MemoryWallActivity.class);
		startActivity(intent3);
		break;
	case R.id.feedback_homepage_detial_img:
		Intent intent4=new Intent(this,ActivityFeedbackActivity.class);
		startActivity(intent4);
		break;
	case R.id.barrage_homepage_detial_img:
		Intent intent5=new Intent(this,BarrageActivity.class);
		startActivity(intent5);
		break;
	default :
		break;
}
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
	public void onPageSelected(int pos) {
		// TODO Auto-generated method stub
		setImageBackground(pos % photolist.size());

	}
/**
 * 开始播放轮播图
 *   
 * @author lucifer
 * @date 2015-11-12
 */
	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
	}

	@SuppressWarnings("unused")
	private void stopPlay() {
		scheduledExecutorService.shutdown();
	}
	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % photolist.size();
				handler.obtainMessage().sendToTarget();
			}
		}

	}
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}

	};
	/**
	 * 用来刷新adapter
	 */
	Handler myhandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:
				adapter.notifyDataSetChanged();	
				startPlay();
				break;
			}
		}
	
	};
/**
 * 滑动的一定高度，标题栏显示出来
 */
	
	@SuppressLint("NewApi")
	@Override
	public void onScroll(int scrollY) {
		if(scrollY>=mhighty){
			topLayout.setBackgroundColor(this.getResources().getColor(R.color.relativelayout_change));
//			ObjectAnimator animator = ObjectAnimator.ofFloat(topLayout, "alpha", 0f, 0.5f);  
//			animator.setDuration(1000);  
//			animator.start(); 
			titleTop.setVisibility(View.VISIBLE);
			titleTop.setTextColor(this.getResources().getColor(R.color.text_changehui));
		}else{
			topLayout.setBackgroundColor(this.getResources().getColor(R.color.relativelayout_yuan));
			titleTop.setVisibility(View.INVISIBLE);
//			ObjectAnimator animator = ObjectAnimator.ofFloat(topLayout, "alpha", 0.5f, 0f);  
//			animator.setDuration(1000);  
//			animator.start(); 
		}
		
	}

}
