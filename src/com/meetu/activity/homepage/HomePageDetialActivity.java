package com.meetu.activity.homepage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;

import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.adapter.PhotoPagerAdapter;
import com.meetu.baidumapdemo.BaiduMapMainActivity;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjActivityCoverCallback;
import com.meetu.cloud.callback.ObjActivityPhotoCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.object.ObjUser;

import com.meetu.cloud.wrap.ObjActivityCoverWrap;
import com.meetu.cloud.wrap.ObjActivityOrderWrap;
import com.meetu.cloud.wrap.ObjActivityPhotoWrap;
import com.meetu.cloud.wrap.ObjPraiseWrap;
import com.meetu.common.PerfectInformation;
import com.meetu.entity.PhotoWall;
import com.meetu.entity.Photolunbo;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.ActivityDao;
import com.meetu.tools.DateUtils;
import com.meetu.tools.DensityUtil;
import com.meetu.view.MyScrollView;
import com.meetu.view.MyScrollView.OnScrollListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.bool;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

public class HomePageDetialActivity extends Activity implements
		OnPageChangeListener, OnClickListener, OnScrollListener {
	// 控件相关
	private ViewPager viewPager;
	private PhotoPagerAdapter adapter;
	// private List<Photolunbo> photolist = new ArrayList<Photolunbo>();
	private List<ImageView> dotViewsList = new ArrayList<ImageView>();
	private ScheduledExecutorService scheduledExecutorService;
	private int currentItem = 0;

	private LinearLayout mLinearLayout;
	private WebView webView;
	private RelativeLayout backLayout;
	private ImageView baiduMap, join, over, backFeed, memory, barrage;
	private TextView userNumber;
	private MyScrollView mScrollView;
	private int mhighty;// 滑动的高度y,改变标题栏背景透明度

	private RelativeLayout topLayout;
	private TextView titleTop;
	private String style;
	private ImageView favorImg;// 点赞图标
	private TextView favorNumber;

	private TextView address;
	private TextView titile, titleSub;
	private ImageView oneUser, twoUser, threeUser, fourUser, fiveUser, sixUser,
			sevenUser;
	// 网络数据相关
	private ActivityBean activityBean = new ActivityBean();
	private ObjActivity objActivity = null;

	// 轮播图
	private List<ObjActivityCover> objPhotosList = new ArrayList<ObjActivityCover>();

	// 当前用户
	AVUser currentUser = AVUser.getCurrentUser();
	private ObjUser user = new ObjUser();

	// 参加活动用户列表
	ArrayList<ObjUser> userList = new ArrayList<ObjUser>();

	private FinalBitmap finalBitmap;
	
	private RelativeLayout favorLayout;
	private ActivityDao activityDao;
	
	private boolean isFavor=false;//是否对该活动点赞
	private boolean isFavorNow=false;//判断是否在当前界面操作了点赞
	private boolean isCancleFavorNow=false;//判断是否在当前界面操作了取消点赞
	
	private TextView timeTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_home_page_detial);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		activityBean = (ActivityBean) bundle.getSerializable("activityBean");
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		activityDao=new ActivityDao(this);
		MyApplication app = (MyApplication) this.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		log.e("zcq", "activityBean" + activityBean.getLocationLongtitude()
				+ "   " + activityBean.getLocationLatitude());
		log.e("zcq", "TimeStart()==" + activityBean.getTimeStart() + "  =="
				+ activityBean.getTimeStop());
		initLoadActivity(activityBean.getActyId());
		initView();
		// startPlay();
		initLoad();
		mhighty = DensityUtil.dip2px(this, 250 - 44);
		// 点赞相关处理
		isFavorActivity(user, objActivity);
		queryOrderUsers(objActivity);
	}

	private void initLoadActivity(String activityId) {
		log.e("zcq", "activityId==" + activityId);
		try {
			objActivity = AVObject.createWithoutData(ObjActivity.class,
					activityId);

		} catch (AVException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据活动状态初始化界面
	 */
	private void initLoad() {

		if (activityBean.getStatus() == 70) {

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

		mLinearLayout = (LinearLayout) super
				.findViewById(R.id.dian_linearlayout_homepage);

		/**
		 * vebview相关 加载网页
		 */
		webView = (WebView) super.findViewById(R.id.contentWebView);
		webView.loadUrl("" + activityBean.getActivityContent());

		// 控件点击相关
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_homepage_detial_rl);
		backLayout.setOnClickListener(this);
		baiduMap = (ImageView) super
				.findViewById(R.id.baidumap_homepage_detial);
		baiduMap.setOnClickListener(this);

		over = (ImageView) super.findViewById(R.id.over_homepager_detial_img);
		// 报名
		join = (ImageView) super.findViewById(R.id.join_homepager_detial_img);
		join.setOnClickListener(this);
		// 弹幕
		barrage = (ImageView) super
				.findViewById(R.id.barrage_homepage_detial_img);
		barrage.setOnClickListener(this);
		// 反馈
		backFeed = (ImageView) super
				.findViewById(R.id.feedback_homepage_detial_img);
		backFeed.setOnClickListener(this);
		// 回忆
		memory = (ImageView) super
				.findViewById(R.id.memory_homepager_detial_img);
		memory.setOnClickListener(this);

		mScrollView = (MyScrollView) super
				.findViewById(R.id.scrollview_homepage);
		mScrollView.setOnScrollListener(this);
		topLayout = (RelativeLayout) super
				.findViewById(R.id.top_homepage_detial_rl);
		titleTop = (TextView) super
				.findViewById(R.id.title_top_homepager_detial_tv);
		
		titleTop.setText("" + activityBean.getTitle());

		address = (TextView) super
				.findViewById(R.id.address_home_page_detial_tv);
		address.setText("" + activityBean.getLocationAddress() + "  "
				+ activityBean.getLocationPlace());

		titile = (TextView) super
				.findViewById(R.id.title_lunbo_item_homepage_tv);
		titile.setText("" + activityBean.getTitle());
		titleSub = (TextView) super
				.findViewById(R.id.title_sub_lunbo_item_homepage_tv);
		titleSub.setText("" + activityBean.getTitleSub());

		favorImg = (ImageView) super
				.findViewById(R.id.favor_home_page_detial_img);

		favorNumber = (TextView) super
				.findViewById(R.id.favornumber_homepage_detail);
		favorNumber.setText("" + activityBean.getPraiseCount());
		// 参加活动的人
		oneUser = (ImageView) super
				.findViewById(R.id.userhead_one_heomePageDetial_img);
		twoUser = (ImageView) super
				.findViewById(R.id.userhead_two_heomePageDetial_img);
		threeUser = (ImageView) super
				.findViewById(R.id.userhead_three_heomePageDetial_img);
		fourUser = (ImageView) super
				.findViewById(R.id.userhead_four_heomePageDetial_img);
		fiveUser = (ImageView) super
				.findViewById(R.id.userhead_five_heomePageDetial_img);
		sixUser = (ImageView) super
				.findViewById(R.id.userhead_six_heomePageDetial_img);
		sevenUser = (ImageView) super
				.findViewById(R.id.userhead_seven_heomePageDetial_img);
		userNumber = (TextView) super
				.findViewById(R.id.userNumber_homePagedetial_tv);
		favorLayout=(RelativeLayout) findViewById(R.id.favor_home_page_detial_rl);
		favorLayout.setOnClickListener(this);
		timeTextView=(TextView) findViewById(R.id.time_homepage_detial_tv);
		
		
		timeTextView.setText(DateUtils.getActivityTimeStart(activityBean.getTimeStart())+"~"
				+DateUtils.getActivityTimeStop(activityBean.getTimeStop()));
	}

	/**
	 * 加载网络数据
	 * 
	 * @author lucifer
	 * @date 2015-11-11
	 */
	private void load() {

		objPhotosList = new ArrayList<ObjActivityCover>();

		ObjActivityCoverWrap.queryActivityCover(objActivity,
				new ObjActivityCoverCallback() {

					@Override
					public void callback(List<ObjActivityCover> objects,
							AVException e) {
						if (e != null) {
							log.e("zcq", e);
						} else {
							objPhotosList.addAll(objects);
							log.e("zcq",
									"objPhotosList==" + objPhotosList.size());
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
		case R.id.back_homepage_detial_rl:
			finish();

			break;
		case R.id.baidumap_homepage_detial:
			Intent intent = new Intent(this, BaiduMapMainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("activityBean", activityBean);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.join_homepager_detial_img:
			switch (activityBean.getStatus()) {
			case 40:
				Toast.makeText(this, "活动正在进行中，停止报名", Toast.LENGTH_SHORT).show();
				break;
			case 50:
				if(user.isCompleteUserInfo()){
					Intent intent2 = new Intent(this, JoinActivity.class);
					Bundle bundle2 = new Bundle();
					bundle2.putSerializable("activityBean", activityBean);
					intent2.putExtras(bundle2);
					startActivity(intent2);
				}else{
					PerfectInformation.showDiolagPerfertInformation(this, "亲爱的 完善个人信息后才能参加活动呢");
				}
				
				break;
			case 60:
				Toast.makeText(this, "报名已结束", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

			break;
		case R.id.memory_homepager_detial_img:
			Intent intent3 = new Intent(this, MemoryWallActivity.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("activityBean", activityBean);
			intent3.putExtras(bundle3);
			startActivity(intent3);
			break;
		case R.id.feedback_homepage_detial_img:
			
			if(user.isCompleteUserInfo()){
				Intent intent4 = new Intent(this, ActivityFeedbackActivity.class);
				Bundle bundle4 = new Bundle();
				bundle4.putSerializable("activityBean", activityBean);
				intent4.putExtras(bundle4);
				startActivity(intent4);
			}else{
				PerfectInformation.showDiolagPerfertInformation(this, "亲爱的 完善个人信息后才能参加活动呢");
			}
			
		
			break;
		case R.id.barrage_homepage_detial_img:
			Intent intent5 = new Intent(this, BarrageActivity.class);
			Bundle bundle5 = new Bundle();
			bundle5.putSerializable("activityBean", activityBean);
			intent5.putExtras(bundle5);
			startActivity(intent5);
			break;

		case R.id.userhead_one_heomePageDetial_img:
			Intent one = new Intent(this, UserPagerActivity.class);
			log.e("lucifer", "userList.get(0).getObjectId()=="
					+ userList.get(0).getObjectId());
			one.putExtra("userId", userList.get(0).getObjectId());
			startActivity(one);
			break;

		case R.id.userhead_two_heomePageDetial_img:
			Intent two = new Intent(this, UserPagerActivity.class);
			log.e("lucifer", "userList.get(1).getObjectId()=="
					+ userList.get(1).getObjectId());
			two.putExtra("userId", userList.get(1).getObjectId());
			startActivity(two);
			break;

		case R.id.userhead_three_heomePageDetial_img:
			Intent three = new Intent(this, UserPagerActivity.class);
			log.e("lucifer", "userList.get(2).getObjectId()=="
					+ userList.get(2).getObjectId());
			three.putExtra("userId", userList.get(2).getObjectId());
			startActivity(three);
			break;

		case R.id.userhead_four_heomePageDetial_img:
			Intent four = new Intent(this, UserPagerActivity.class);
			log.e("lucifer", "userList.get(3).getObjectId()=="
					+ userList.get(3).getObjectId());
			four.putExtra("userId", userList.get(3).getObjectId());
			startActivity(four);
			break;

		case R.id.userhead_five_heomePageDetial_img:
			Intent five = new Intent(this, UserPagerActivity.class);
			log.e("lucifer", "userList.get(4).getObjectId()=="
					+ userList.get(4).getObjectId());
			five.putExtra("userId", userList.get(4).getObjectId());
			startActivity(five);
			break;

		case R.id.userhead_six_heomePageDetial_img:
			Intent six = new Intent(this, UserPagerActivity.class);
			log.e("lucifer", "userList.get(5).getObjectId()=="
					+ userList.get(5).getObjectId());
			six.putExtra("userId", userList.get(5).getObjectId());
			startActivity(six);
			break;
		case R.id.userhead_seven_heomePageDetial_img:
			Intent seven = new Intent(this, UserPagerActivity.class);
			log.e("lucifer", "userList.get(6).getObjectId()=="
					+ userList.get(6).getObjectId());
			seven.putExtra("userId", userList.get(6).getObjectId());
			startActivity(seven);
			break;
		case R.id.favor_home_page_detial_rl:
			//点赞操作
			if(isFavor){
				cancleFavorActivity();
			}else{
				favorActivity();
			}
			break;

		default:
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
		setImageBackground(pos % objPhotosList.size());

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
				currentItem = (currentItem + 1) % objPhotosList.size();
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
	Handler myhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				for (int i = 0; i < objPhotosList.size(); i++) {
					ImageView viewDot = new ImageView(
							HomePageDetialActivity.this);
					if (i == 0) {
						viewDot.setBackgroundResource(R.drawable.main_dot_white);
					} else {
						viewDot.setBackgroundResource(R.drawable.main_dot_light);
					}
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					params.rightMargin = 10;
					viewDot.setLayoutParams(params);
					dotViewsList.add(viewDot);
					mLinearLayout.addView(viewDot);

					adapter = new PhotoPagerAdapter(
							HomePageDetialActivity.this, objPhotosList);
					viewPager
							.setOnPageChangeListener(HomePageDetialActivity.this);

					viewPager.setAdapter(adapter);
				}
				// adapter.notifyDataSetChanged();
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
		if (scrollY >= mhighty) {
			topLayout.setBackgroundColor(this.getResources().getColor(
					R.color.relativelayout_change));
			// ObjectAnimator animator = ObjectAnimator.ofFloat(topLayout,
			// "alpha", 0f, 0.5f);
			// animator.setDuration(1000);
			// animator.start();
			titleTop.setVisibility(View.VISIBLE);
			titleTop.setTextColor(this.getResources().getColor(
					R.color.barrage_acty_end_bg));
			favorNumber.setTextColor(this.getResources().getColor(R.color.tablebar_check));
//			if(isFavor==false){
//				favorImg.setImageResource(R.drawable.acty_show_navi_btn_like_line_nor);
//				
//			}
		} else {
			topLayout.setBackgroundColor(this.getResources().getColor(
					R.color.relativelayout_yuan));
			titleTop.setVisibility(View.INVISIBLE);
			favorNumber.setTextColor(this.getResources().getColor(R.color.white));
//			if(isFavor==false){
//				favorImg.setImageResource(R.drawable.acty_show_navi_btn_like_hl);
//				
//			}
		}

	}

	/**
	 * 点赞操作
	 * 查询我是否对活动点赞
	 * @param user
	 * @param objActivity
	 * @author lucifer
	 * @date 2015-11-16
	 */
	private void isFavorActivity(final ObjUser user, final ObjActivity objActivity) {
		ObjPraiseWrap.queryActivityFavor(user, objActivity,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						if (e != null) {
							log.e("zcq", e);
						} else if (result) {
							// 点赞过
							isFavor=true;
							favorImg.setImageResource(R.drawable.acty_cardimg_btn_like_hl);
//<<<<<<< HEAD
//							favorImg.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View arg0) {
//
//									// 修改云端
//									ObjPraiseWrap.cancelPraiseActivity(user,
//											objActivity,
//											new ObjFunBooleanCallback() {
//
//												@Override
//												public void callback(
//														boolean result,
//														AVException e) {
//													if (e != null
//															|| result == false) {
//														Toast.makeText(
//																getApplicationContext(),
//																"取消点赞失败",
//																1000).show();
//													} else {
//														// 插入到本地数据库 成功
//														// activityDao.updateIsFavor(user.getObjectId(),
//														// actyListCache.get(position).getActyId(),
//														// 1);
//														Toast.makeText(
//																getApplicationContext(),
//																"取消点赞", 1000)
//																.show();
//
//														favorImg.setImageResource(R.drawable.acty_cardimg_btn_like_nor);
//														isFavor(user,
//																objActivity);
//													}
//												}
//											});
//								}
//							});
//						} else {
//							// 没点赞
//							favorImg.setImageResource(R.drawable.acty_cardimg_btn_like_nor);
//							favorImg.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View arg0) {
//									// TODO Auto-generated method stub
//
//									// 修改云端
//									ObjPraiseWrap.praiseActivity(user,
//											objActivity,
//											new ObjFunBooleanCallback() {
//
//												@Override
//												public void callback(
//														boolean result,
//														AVException e) {
//													// TODO Auto-generated
//													// method stub
//													if (e != null
//															|| result == false) {
//														Toast.makeText(
//																getApplicationContext(),
//																"点赞失败",
//																1000).show();
//													} else {
//														// 插入到本地数据库 成功
//														// activityDao.updateIsFavor(user.getObjectId(),
//														// actyListCache.get(position).getActyId(),
//														// 1);
//														Toast.makeText(
//																getApplicationContext(),
//																"点赞成功", 1000)
//																.show();
//														favorImg.setImageResource(R.drawable.acty_cardimg_btn_like_hl);
//														isFavor(user,
//																objActivity);
//
//													}
//												}
//											});
//								}
//							});
//=======

						} else {
							// 没点赞
							favorImg.setImageResource(R.drawable.acty_cardimg_btn_like_nor);


						}

					}
				});
	}

	/**
	 * 活动报名的人
	 * 
	 * @author lucifer
	 * @date 2015-11-16
	 */
	public void queryOrderUsers(final ObjActivity activity) {
		ObjActivityOrderWrap.queryActivitySignUp(activity,
				new ObjUserCallback() {

					@Override
					public void callback(List<ObjUser> objects, AVException e) {
						if (e != null) {
							log.e("zcq", e);
							return;
						} else if (objects != null) {
							userList.addAll(objects);
							log.e("zcq", "userList==" + userList.size());
							userNumber.setText("" + userList.size());
							// TODO 点击进入个人主页 未完成
							if (userList.size() >= 7) {
								finalBitmap.display(sevenUser, userList.get(6)
										.getProfileClip().getUrl());
								sevenUser
										.setOnClickListener(HomePageDetialActivity.this);
							}
							if (userList.size() >= 6) {
								finalBitmap.display(sixUser, userList.get(5)
										.getProfileClip().getUrl());
								sixUser.setOnClickListener(HomePageDetialActivity.this);
							}
							if (userList.size() >= 5) {
								finalBitmap.display(fiveUser, userList.get(4)
										.getProfileClip().getUrl());
								fiveUser.setOnClickListener(HomePageDetialActivity.this);
							}
							if (userList.size() >= 4) {
								finalBitmap.display(fourUser, userList.get(3)
										.getProfileClip().getUrl());
								fourUser.setOnClickListener(HomePageDetialActivity.this);
							}
							if (userList.size() >= 3) {
								finalBitmap.display(threeUser, userList.get(2)
										.getProfileClip().getUrl());
								threeUser
										.setOnClickListener(HomePageDetialActivity.this);
							}
							if (userList.size() >= 2) {
								finalBitmap.display(twoUser, userList.get(1)
										.getProfileClip().getUrl());
								twoUser.setOnClickListener(HomePageDetialActivity.this);
							}
							if (userList.size() >= 1) {
								finalBitmap.display(oneUser, userList.get(0)
										.getProfileClip().getUrl());

								oneUser.setOnClickListener(HomePageDetialActivity.this);
							}

						}

					}
				});

	}
	/**
	 * 点赞
	 *   
	 * @author lucifer
	 * @date 2015-12-9
	 */
	public void favorActivity(){
		// 修改云端
		ObjPraiseWrap.praiseActivity(user,
				objActivity,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(
							boolean result,
							AVException e) {
						// TODO Auto-generated
						// method stub
						if (e != null
								|| result == false) {
							Toast.makeText(
									getApplicationContext(),
									"点赞失败，请检查网络",
									1000).show();
						} else {
							isFavor=true;
							isFavorNow=true;
							// 插入到本地数据库 成功
							// activityDao.updateIsFavor(user.getObjectId(),
							// actyListCache.get(position).getActyId(),
							// 1);
							Toast.makeText(
									getApplicationContext(),
									"点赞成功", 1000)
									.show();
							if(isCancleFavorNow){
								int number=activityBean.getPraiseCount();
								activityDao.updateFavourNumber(user.getObjectId(), activityBean.getActyId(), number);
								favorNumber.setText(""+number);
							}else{
								int number=activityBean.getPraiseCount()+1;
								activityDao.updateFavourNumber(user.getObjectId(), activityBean.getActyId(), number);
								favorNumber.setText(""+number);
							}
							
							favorImg.setImageResource(R.drawable.acty_cardimg_btn_like_hl);
							
//							isFavor(user,
//									objActivity);

						}
					}
				});
		
	}
	/**
	 * 取消点赞
	 *   
	 * @author lucifer
	 * @date 2015-12-9
	 */
	public void cancleFavorActivity(){
		// 修改云端
		ObjPraiseWrap.cancelPraiseActivity(user,
				objActivity,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(
							boolean result,
							AVException e) {
						if (e != null
								|| result == false) {
							Toast.makeText(
									getApplicationContext(),
									"取消失败，请检查网络",
									1000).show();
						} else {
							// 插入到本地数据库 成功
							// activityDao.updateIsFavor(user.getObjectId(),
							// actyListCache.get(position).getActyId(),
							// 1);
							isFavor=false;
							isCancleFavorNow=true;
							Toast.makeText(
									getApplicationContext(),
									"取消点赞成功", 1000)
									.show();
							//改变当前activity 缓存的点赞数量
							
							if(isFavorNow){
								int number=activityBean.getPraiseCount();
								
								activityDao.updateFavourNumber(user.getObjectId(), activityBean.getActyId(), number);
								favorNumber.setText(""+number);
							}else{
								int number=activityBean.getPraiseCount()-1;								
								activityDao.updateFavourNumber(user.getObjectId(), activityBean.getActyId(), number);
								favorNumber.setText(""+number);
							}
							

							favorImg.setImageResource(R.drawable.acty_cardimg_btn_like_nor);
							
//							isFavor(user,
//									objActivity);
						}
					}
				});
	}

}
