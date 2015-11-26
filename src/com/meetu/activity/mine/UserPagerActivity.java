package com.meetu.activity.mine;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.R;
import com.meetu.cloud.object.ObjUser;
import com.meetu.fragment.UserInfoFragment;
import com.meetu.fragment.UserPhotoFragment;
import com.meetu.view.CustomViewPager;
import com.meetu.view.ScrollTabHolder;
import com.meetu.view.SlidingTabLayout;

public class UserPagerActivity extends FragmentActivity implements ScrollTabHolder{
	private ImageView backImv;
	private ImageView setImv;
	private ImageView userLikeImv;
	private ImageView userProfileImv;
	private ImageView userGenderImv;
	private ImageView userScripImv;
	private TextView userNameTv;
	private ImageView userVipImv;
	private ImageView userApproveImv;
	private CustomViewPager userPager;
	private SlidingTabLayout userTab;
	private LinearLayout headView;
	/**
	 * 相关操作为北京图片伸缩效果，暂时注释
	 * */
	//private LinearLayout imgView;

	private ViewPagerAdapter adapter=null;
	//网络 数据相关
	private BitmapUtils bitmapUtils; 
	private String headURl="";//头像的URL
	//拿本地的  user 
	private AVUser currentUser = AVUser.getCurrentUser();
	private String userId = "";

	private static final String IMAGE_TRANSLATION_Y = "image_translation_y";
	private static final String HEADER_TRANSLATION_Y = "header_translation_y";
	private int minHeaderHeight;
	//头部高度
	private int headerHeight;
	//头部划出屏幕高度
	private int minHeaderTranslation;
	private int numFragments;

	private int mScrollState;
	//头部是否滑动
	private boolean isTopScroll=true;
	//滑动距离
	private float tempA=285;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.getWindow();
		setContentView(R.layout.userpager_layout);
		initValues();
		initView();
		if (savedInstanceState != null) {
			//imgView.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
			headView.setTranslationY(savedInstanceState.getFloat(HEADER_TRANSLATION_Y));
		}
		userPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//触摸位于头部，禁止横向滑动
				if(px2dip(getApplicationContext(), event.getY())<tempA){
					userPager.setScanScroll(false);
					return true;
				}else{
					userPager.setScanScroll(true);
					return false;
				}

			}
		});
		setupAdapter();
	}
	private void initValues() {
		int tabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
		minHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
		headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
		//头部滑动距离
		minHeaderTranslation = -minHeaderHeight + tabHeight;//-335+50

		numFragments = 2;
	}
	private void initView() {
		// TODO Auto-generated method stub
		backImv = (ImageView) findViewById(R.id.user_back_imv);
		setImv = (ImageView) findViewById(R.id.user_set_imv);
		userLikeImv = (ImageView) findViewById(R.id.user_like_imv);
		userProfileImv = (ImageView) findViewById(R.id.user_profile_iv);
		userGenderImv = (ImageView) findViewById(R.id.user_gender_imv);
		userScripImv = (ImageView) findViewById(R.id.user_scrip_imv);
		userNameTv = (TextView) findViewById(R.id.user_name_tv);
		userVipImv = (ImageView) findViewById(R.id.user_vip_dis);
		userApproveImv = (ImageView) findViewById(R.id.user_approve_dis);
		userPager = (CustomViewPager) findViewById(R.id.user_pager);
		userTab = (SlidingTabLayout) findViewById(R.id.user_tab);
		headView = (LinearLayout) findViewById(R.id.head_view);
		//imgView = (LinearLayout) findViewById(R.id.img_view);

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
		userProfileImv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "hello", 1000).show();
			}
		});
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//outState.putFloat(IMAGE_TRANSLATION_Y, imgView.getTranslationY());
		outState.putFloat(HEADER_TRANSLATION_Y, headView.getTranslationY());
		super.onSaveInstanceState(outState);
	}
	private void setupAdapter() {
		if (adapter == null) {
			adapter = new ViewPagerAdapter(getSupportFragmentManager(), numFragments);
		}

		userPager.setAdapter(adapter);
		userPager.setOffscreenPageLimit(numFragments);
		userTab.setOnPageChangeListener(getViewPagerPageChangeListener());
		userTab.setViewPager(userPager);

	}
	//viewpager滑动时间监听
	private ViewPager.OnPageChangeListener getViewPagerPageChangeListener () {
		ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
			@SuppressLint("NewApi") 
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				int currentItem = userPager.getCurrentItem();
				if (positionOffsetPixels > 0) {
					SparseArrayCompat<ScrollTabHolder> scrollTabHolders = adapter.getScrollTabHolders();

					ScrollTabHolder fragmentContent;
					log.d("mytest", "pager"+position);
					log.d("mytest", "size"+scrollTabHolders.size());
					if (position < currentItem) {
						// Revealed the previous page
						fragmentContent = scrollTabHolders.valueAt(position);
					} else {
						// Revealed the next page
						fragmentContent = scrollTabHolders.valueAt(position + 1);
					}
					fragmentContent.adjustScroll((int) (headView.getHeight() + headView.getTranslationY()),
							headView.getHeight());
				}
			}

			@SuppressLint("NewApi")
			@Override
			public void onPageSelected(int position) {
				userPager.setScanScroll(true);
				SparseArrayCompat<ScrollTabHolder> scrollTabHolders = adapter.getScrollTabHolders();

				if (scrollTabHolders == null || scrollTabHolders.size() != numFragments) {
					return;
				}

				ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
				//记录滑动位置，切换后纵向仍停留在原位置
				currentHolder.adjustScroll(
						(int) (headView.getHeight() + headView.getTranslationY()),
						headView.getHeight());
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				mScrollState = state;
			}
		};

		return listener;
	}
	@SuppressLint("NewApi")
	private void scrollHeader(int scrollY) {
		float translationY = Math.max(-scrollY,minHeaderTranslation);
		headView.setTranslationY(translationY);
		//imgView.setTranslationY(-translationY / 3);
	}
	@Override
	public void adjustScroll(int scrollHeight, int headerHeight) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollViewScroll(ScrollView view, int x, int y, int oldX,
			int oldY, int pagePosition) {
		// TODO Auto-generated method stub
		tempA=285-px2dip(getApplicationContext(),y);
		if (userPager.getCurrentItem() == pagePosition){
			scrollHeader(view.getScrollY());
		}
	}

	@Override
	public void onRecyclerViewScroll(RecyclerView view, int scrollY,
			int pagePosition) {
		if (userPager.getCurrentItem() == pagePosition) {
			log.d("mytest", "zhixing2");
			scrollHeader(scrollY);
		}
	}  
	
	class ViewPagerAdapter extends FragmentPagerAdapter{
		private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
		private int nums;

		public ViewPagerAdapter(FragmentManager fm, int numFragments) {
			super(fm);
			// TODO Auto-generated constructor stub
			mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
			nums = numFragments;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			switch (position) {
			case 0:
				fragment = UserInfoFragment.newInstance(0);
				break;

			case 1:
				fragment = UserPhotoFragment.newInstance(1);
				break;


			default:
				throw new IllegalArgumentException("Wrong page given " + position);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return nums;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Object object = super.instantiateItem(container, position);

			mScrollTabHolders.put(position, (ScrollTabHolder) object);

			return object;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "个人信息";

			case 1:
				return "UU秀墙";

			default:
				throw new IllegalArgumentException("wrong position for the fragment in vehicle page");
			}
		}
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}
		//获取fragmentHolder列表
		public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
			return mScrollTabHolders;
		}
	}
	/** 
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 */  
	public static int px2dip(Context context, float pxValue) {  
		final float scale = context.getResources().getDisplayMetrics().density;  
		return (int) (pxValue / scale + 0.5f);  
	}
}
