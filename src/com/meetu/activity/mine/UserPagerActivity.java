package com.meetu.activity.mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.activity.ReportActivity;
import com.meetu.activity.SystemSettingsActivity;
import com.meetu.activity.messages.CreateLitterNoteActivity;
import com.meetu.activity.messages.NotesActivity;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjShieldUser;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjFollowWrap;
import com.meetu.cloud.wrap.ObjShieldUserWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Middle;
import com.meetu.fragment.UserInfoFragment;
import com.meetu.fragment.UserPhotoFragment;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.tools.BitmapCut;
import com.meetu.view.CustomViewPager;
import com.meetu.view.ScrollTabHolder;
import com.meetu.view.SlidingTabLayout;

public class UserPagerActivity extends FragmentActivity implements
		ScrollTabHolder, OnClickListener {
	// 控件相关
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
	private RelativeLayout backLayout, settingLayout, Morelayout;
	private TextView addMore;
	/**
	 * 相关操作为北京图片伸缩效果，暂时注释
	 * */
	// private LinearLayout imgView;

	private ViewPagerAdapter adapter = null;
	// 网络 数据相关
	private BitmapUtils bitmapUtils;
	private String headURl = "";// 头像的URL
	// 拿本地的 user
	private AVUser currentUser = AVUser.getCurrentUser();
	private String userId = "";

	private static final String IMAGE_TRANSLATION_Y = "image_translation_y";
	private static final String HEADER_TRANSLATION_Y = "header_translation_y";
	private int minHeaderHeight;
	// 头部高度
	private int headerHeight;
	// 头部划出屏幕高度
	private int minHeaderTranslation;
	private int numFragments;

	private int mScrollState;
	// 头部是否滑动
	private boolean isTopScroll = true;
	// 滑动距离
	private float tempA = 285;
	private FinalBitmap finalBitmap;

	private ObjUser userMy;
	private Boolean isMyself = false;

	private ImageView updateImageView, ivTouxiang;
	private TextView userName;

	// 上传 信息 头像相关
	private String fHeadPath = "";
	private String yHeadPath = "";
	// 标记是否屏蔽
	private boolean isShield = false;
	// popwindow相关
	TextView laheiTv;
	TextView jubaoTv;
	RelativeLayout laheilayout;
	RelativeLayout jubaolayout;
	boolean isFollow=false;//对该用户是否关注、点赞
	
	List<UserAboutBean> userAboutBeansList=new ArrayList<UserAboutBean>();
	UserAboutDao userAboutDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.getWindow();
		setContentView(R.layout.userpager_layout);
		if (currentUser != null) {
			// 强制类型转换
			userMy = AVUser.cast(currentUser, ObjUser.class);
		} else {
			return;
		}
		userAboutDao=new UserAboutDao(getApplicationContext());
		Intent intent = getIntent();
		// 取到用户的id
		userId = intent.getStringExtra("userId");

		if (userMy.getObjectId().equals(userId)) {
			isMyself = true;
		}
		
		userAboutBeansList=userAboutDao.queryUserAbout(userMy.getObjectId(), Constants.FOLLOW_TYPE, "");
		if(userAboutBeansList==null||userAboutBeansList.size()==0){
			isFollow=false;
		}else{
			for(int i=0;i<userAboutBeansList.size();i++){
				if(userAboutBeansList.get(i).getAboutUserId().equals(userId)){
					isFollow=true;
					break;
				}
			}
		}
		
		

		MyApplication app = (MyApplication) this.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		initValues();
		initView();
		getUserInfo(userId);
		if (savedInstanceState != null) {
			// imgView.setTranslationY(savedInstanceState.getFloat(IMAGE_TRANSLATION_Y));
			headView.setTranslationY(savedInstanceState
					.getFloat(HEADER_TRANSLATION_Y));
		}
		userPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// 触摸位于头部，禁止横向滑动
				if (px2dip(getApplicationContext(), event.getY()) < tempA) {
					userPager.setScanScroll(false);
					return true;
				} else {
					userPager.setScanScroll(true);
					return false;
				}

			}
		});
		setupAdapter();
		
		if(isFollow==true){
			
			userLikeImv.setImageResource(R.drawable.mine_photoview_btn_like_hl);
		}
	}

	private void initValues() {
		int tabHeight = getResources()
				.getDimensionPixelSize(R.dimen.tab_height);
		minHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.min_header_height);
		headerHeight = getResources().getDimensionPixelSize(
				R.dimen.header_height);
		// 头部滑动距离
		minHeaderTranslation = -minHeaderHeight + tabHeight;// -335+50

		numFragments = 2;
	}

	private void initView() {

		backImv = (ImageView) findViewById(R.id.user_back_imv);
		setImv = (ImageView) findViewById(R.id.user_set_imv);
		userLikeImv = (ImageView) findViewById(R.id.user_like_imv);
		userLikeImv.setOnClickListener(this);
		userProfileImv = (ImageView) findViewById(R.id.user_profile_iv);
		userGenderImv = (ImageView) findViewById(R.id.user_gender_imv);
		userScripImv = (ImageView) findViewById(R.id.user_scrip_imv);
		userScripImv.setOnClickListener(this);
		userNameTv = (TextView) findViewById(R.id.user_name_tv);
		// userVipImv = (ImageView) findViewById(R.id.user_vip_dis);
		// userApproveImv = (ImageView) findViewById(R.id.user_approve_dis);
		userPager = (CustomViewPager) findViewById(R.id.user_pager);
		userTab = (SlidingTabLayout) findViewById(R.id.user_tab);
		headView = (LinearLayout) findViewById(R.id.head_view);
		// imgView = (LinearLayout) findViewById(R.id.img_view);

		bitmapUtils = new BitmapUtils(getApplicationContext());
		// if(currentUser!=null){
		// //强制类型转换
		// ObjUser user = AVUser.cast(currentUser, ObjUser.class);
		// //此处为测试，应为对应用户头像
		// headURl=user.getProfileClip().getUrl();
		// }
		settingLayout = (RelativeLayout) findViewById(R.id.setting_userpager_layout_rl);
		settingLayout.setOnClickListener(this);
		addMore = (TextView) findViewById(R.id.addmore_userpager_layout_tv);

		backLayout = (RelativeLayout) findViewById(R.id.back_userpager_layout_rl);
		backLayout.setOnClickListener(this);
		Morelayout = (RelativeLayout) findViewById(R.id.addmore_userpager_layout_rl);
		Morelayout.setOnClickListener(this);
		if (isMyself == true) {
			backLayout.setVisibility(View.GONE);
			settingLayout.setVisibility(View.VISIBLE);
			userLikeImv.setVisibility(View.GONE);
			userScripImv.setVisibility(View.GONE);
			Morelayout.setVisibility(View.GONE);
		} else {
			backLayout.setVisibility(View.VISIBLE);
			settingLayout.setVisibility(View.GONE);
			setImv.setVisibility(View.GONE);
		}

		userProfileImv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isMyself == true) {
					showDialog();
				} else {
					log.e("zcq", "此处应放大图片");
				}

			}
		});

		updateImageView = (ImageView) findViewById(R.id.update_userpager_img);
		updateImageView.setOnClickListener(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// outState.putFloat(IMAGE_TRANSLATION_Y, imgView.getTranslationY());
		outState.putFloat(HEADER_TRANSLATION_Y, headView.getTranslationY());
		super.onSaveInstanceState(outState);
	}

	private void setupAdapter() {
		if (adapter == null) {
			adapter = new ViewPagerAdapter(getSupportFragmentManager(),
					numFragments);
		}

		userPager.setAdapter(adapter);
		userPager.setOffscreenPageLimit(numFragments);
		userTab.setOnPageChangeListener(getViewPagerPageChangeListener());
		userTab.setViewPager(userPager);

	}

	// viewpager滑动时间监听
	private ViewPager.OnPageChangeListener getViewPagerPageChangeListener() {
		ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
			@SuppressLint("NewApi")
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int currentItem = userPager.getCurrentItem();
				if (positionOffsetPixels > 0) {
					SparseArrayCompat<ScrollTabHolder> scrollTabHolders = adapter
							.getScrollTabHolders();

					ScrollTabHolder fragmentContent;
					log.d("mytest", "pager" + position);
					log.d("mytest", "size" + scrollTabHolders.size());
					if (position < currentItem) {
						// Revealed the previous page
						fragmentContent = scrollTabHolders.valueAt(position);
					} else {
						// Revealed the next page
						fragmentContent = scrollTabHolders
								.valueAt(position + 1);
					}
					fragmentContent.adjustScroll(
							(int) (headView.getHeight() + headView
									.getTranslationY()), headView.getHeight());
				}

				if (position == 0) {
					updateImageView.setVisibility(View.GONE);
				} else if (position == 1) {
					if (isMyself) {
						updateImageView.setVisibility(View.VISIBLE);
					}

				}
			}

			@SuppressLint("NewApi")
			@Override
			public void onPageSelected(int position) {
				userPager.setScanScroll(true);
				SparseArrayCompat<ScrollTabHolder> scrollTabHolders = adapter
						.getScrollTabHolders();

				if (scrollTabHolders == null
						|| scrollTabHolders.size() != numFragments) {
					return;
				}

				ScrollTabHolder currentHolder = scrollTabHolders
						.valueAt(position);
				// 记录滑动位置，切换后纵向仍停留在原位置
				currentHolder.adjustScroll(
						(int) (headView.getHeight() + headView
								.getTranslationY()), headView.getHeight());
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
		float translationY = Math.max(-scrollY, minHeaderTranslation);

		headView.setTranslationY(translationY);
		// imgView.setTranslationY(-translationY / 3);
	}

	@Override
	public void adjustScroll(int scrollHeight, int headerHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollViewScroll(ScrollView view, int x, int y, int oldX,
			int oldY, int pagePosition) {
		// TODO Auto-generated method stub
		tempA = 285 - px2dip(getApplicationContext(), y);
		if (userPager.getCurrentItem() == pagePosition) {
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

	class ViewPagerAdapter extends FragmentPagerAdapter {
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
				fragment = UserInfoFragment.newInstance(0, userId);
				break;

			case 1:
				fragment = UserPhotoFragment.newInstance(1, userId);
				break;

			default:
				throw new IllegalArgumentException("Wrong page given "
						+ position);
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
				throw new IllegalArgumentException(
						"wrong position for the fragment in vehicle page");
			}
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		// 获取fragmentHolder列表
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

	/**
	 * 根据创建者用户id 获取用户相关信息
	 * 
	 * @param objId
	 * @author lucifer
	 * @date 2015-11-17
	 */
	private void getUserInfo(String objId) {
		ObjUserWrap.getObjUser(objId, new ObjUserInfoCallback() {

			@Override
			public void callback(ObjUser user, AVException e) {

				if (user.getProfileClip() != null) {
					finalBitmap.display(userProfileImv, user.getProfileClip()
							.getUrl());
				}
				userNameTv.setText(user.getNameNick());

			}
		});
	}

	private PopupWindow popupWindow;

	private void showPopWindow() {
		if (popupWindow == null) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.item_laheijubao, null);
			popupWindow = new PopupWindow(view,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			// 设置外观
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			ColorDrawable colorDrawable = new ColorDrawable();
			popupWindow.setBackgroundDrawable(colorDrawable);
			laheiTv = (TextView) view.findViewById(R.id.lahei_item_tv);
			jubaoTv = (TextView) view.findViewById(R.id.jubao_item_tv);
			laheilayout = (RelativeLayout) view
					.findViewById(R.id.lahei_item_laheijubao_rl);
			jubaolayout = (RelativeLayout) view
					.findViewById(R.id.jubao_item_laheijubao_rl);
		}
		if (isShield) {
			laheiTv.setText("取消拉黑");
		} else {
			laheiTv.setText("拉黑Ta");
		}
		laheilayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.e("lucifer", "拉黑");
				if (isShield) {
					cancelShieldUser(userId);
				} else {
					shieldUser(userId);
				}
				popupWindow.dismiss();
			}
		});
		jubaolayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.e("lucifer", "举报");
				// 跳转到举报界面
				popupWindow.dismiss();
				Intent intent = new Intent(UserPagerActivity.this,
						ReportActivity.class);
				intent.putExtra("flag", "user");
				intent.putExtra("otherId", userId);
				startActivity(intent);
			}
		});
	}

	/**
	 * 拉黑用户
	 * */
	public void shieldUser(String userId) {
		ObjUser otherUser;
		try {
			otherUser = AVUser.createWithoutData(ObjUser.class, userId);
			ObjShieldUser shieldUser = new ObjShieldUser();
			shieldUser.setUser(userMy);
			shieldUser.setShieldUser(otherUser);
			ObjShieldUserWrap.shieldUser(shieldUser,
					new ObjFunBooleanCallback() {

						@Override
						public void callback(boolean result, AVException e) {
							// TODO Auto-generated method stub
							if (e != null) {
								Toast.makeText(getApplicationContext(), "操作失败",
										1000).show();
								return;
							}
							if (result) {
								isShield = true;
								Toast.makeText(getApplicationContext(), "已拉黑",
										1000).show();
							} else {
								Toast.makeText(getApplicationContext(), "操作失败",
										1000).show();
							}
						}
					});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 取消拉黑
	 * */
	public void cancelShieldUser(String userId) {
		ObjUser otherUser;
		try {
			otherUser = AVUser.createWithoutData(ObjUser.class, userId);
			ObjShieldUserWrap.unShieldUser(userMy, otherUser,
					new ObjFunBooleanCallback() {

						@Override
						public void callback(boolean result, AVException e) {
							// TODO Auto-generated method stub
							if (e != null) {
								Toast.makeText(getApplicationContext(), "操作失败",
										1000).show();
								return;
							}
							if (result) {
								isShield = false;
								Toast.makeText(getApplicationContext(),
										"已取消拉黑", 1000).show();
							} else {
								Toast.makeText(getApplicationContext(), "取消失败",
										1000).show();
							}
						}
					});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void onClick(final View v) {

		switch (v.getId()) {

		case R.id.back_userpager_layout_rl:

			finish();
			break;
		case R.id.setting_userpager_layout_rl:
			Intent intent2 = new Intent(this, SystemSettingsActivity.class);
			startActivity(intent2);
			break;
		case R.id.addmore_userpager_layout_rl:
			ObjUser otherUser;
			try {
				otherUser = AVUser.createWithoutData(ObjUser.class, userId);
				ObjShieldUserWrap.isShield(userMy, otherUser,
						new ObjFunBooleanCallback() {

							@Override
							public void callback(boolean result, AVException e) {
								// TODO Auto-generated method stub
								if (e != null) {
									Toast.makeText(getApplicationContext(),
											"操作失败", 1000).show();
									return;
								}
								if (result) {
									isShield = true;
								} else {
									isShield = false;
								}
								showPopWindow();
								popupWindow.showAsDropDown(v, 0, 0);
							}
						});
			} catch (AVException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case R.id.update_userpager_img:
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent, 00);
			break;
		/**
		 * 发送小纸条
		 */
		case R.id.user_scrip_imv:

			Intent intent3 = new Intent(this, CreateLitterNoteActivity.class);
			intent3.putExtra("userId", userId);
			startActivity(intent3);
			break;
		/**
		 * 点赞
		 */
		case R.id.user_like_imv:
			if(!isFollow){
				followInUser();
			}else{
				dismissFollowUser();
			}
			
			break;
		default:
			break;
		}

	}

	/**
	 * 接收相册返回的图片并处理
	 */

	private Bitmap photoPortait;
	private Bitmap headerPortait;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {

		case 00:
			if (data != null) {
				ContentResolver resolver = this.getContentResolver();
				Bundle extras = data.getExtras();
				Uri url = data.getData();
				try {
					//
					photoPortait = getThumbnail(url, 2000);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Bitmap output = Bitmap.createBitmap(headerPortait.getWidth(),
				// headerPortait.getHeight(), Config.ARGB_8888);
				// headerPortait=BitmapCut.toRectBitmap(headerPortait);
				if (photoPortait != null) {
					Middle.bimaBitmap = photoPortait;
					// saveHeadImg(headerPortait);
					// updata.setImageBitmap(headerPortait);
				}
				Intent intent = new Intent(this, UpdatepictureActivity.class);
				startActivityForResult(intent, 44);
			}
			break;
		case 11:
			if (resultCode == this.RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		case 22:
			if (resultCode == this.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/user_header.png");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片

			}

			break;
		case 33:
			if (data != null) {
				Bundle extras = data.getExtras();
				headerPortait = extras.getParcelable("data");
				if (headerPortait != null) {
					fHeadPath = saveHeadImg(headerPortait, false);
				}
				headerPortait = BitmapCut.toRoundBitmap(headerPortait);

				if (headerPortait != null) {
					yHeadPath = saveHeadImg(headerPortait, true);
					// 上传头像到服务器
					if (currentUser != null) {
						// 强制类型转换
						ObjUser user = AVUser.cast(currentUser, ObjUser.class);
						completeInfo(user);
					}
				}
			}
			break;
		case 44:
			if (resultCode == this.RESULT_OK) {
				// TODO 刷新 照片列表
				log.e("lucifer", "上传照片成功刷新照片列表");

				// ((MinePhotoWallfragment)list.get(1)).reflesh();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showDialog() {
		final AlertDialog portraidlg = new AlertDialog.Builder(this).create();
		portraidlg.show();
		Window win = portraidlg.getWindow();
		win.setContentView(R.layout.dialog_show_photo);
		RadioButton portrait_native = (RadioButton) win
				.findViewById(R.id.Portrait_native);
		portrait_native.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 11);
				portraidlg.dismiss();
			}
		});
		RadioButton portrait_take = (RadioButton) win
				.findViewById(R.id.Portrait_take);
		portrait_take.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 调用摄像头
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								"/user_header.png")));
				startActivityForResult(intent2, 22);
				portraidlg.dismiss();
			}
		});
		View viewTop = win.findViewById(R.id.view_top_dialog_sethead);
		View viewBottom = win.findViewById(R.id.view_bottom_dialog_sethead);
		// 点击dialog外部，关闭dialog
		viewTop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				portraidlg.dismiss();
			}
		});
		viewBottom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				portraidlg.dismiss();
			}
		});

	}

	/**
	 * 调用系统的裁剪功能
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		// 调用拍照的裁剪功能
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽和搞的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 33);
	}

	public String saveHeadImg(Bitmap head, boolean isY) {
		FileOutputStream fos = null;
		String path = "";
		if (isY) {
			path = Environment.getExternalStorageDirectory()
					+ "/user_header.png";
		} else {
			path = Environment.getExternalStorageDirectory()
					+ "/f_user_header.png";
		}
		try {
			fos = new FileOutputStream(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		head.compress(CompressFormat.PNG, 100, fos);
		try {
			fos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 完善个人信息 上传信息 和头像
	 * 
	 * @param user
	 * @author lucifer
	 * @date 2015-11-6
	 */
	AVFile fFile = null; // 切圆后的图
	AVFile yFile = null;// 原始图

	public void completeInfo(final ObjUser user) {
		File upF1 = new File(yHeadPath);
		File upF2 = new File(fHeadPath);
		try {
			yFile = AVFile.withAbsoluteLocalPath(Constants.HEAD_FILE_RECT
					+ user.getObjectId() + Constants.IMG_TYPE, fHeadPath);
			fFile = AVFile.withAbsoluteLocalPath(Constants.HEAD_FILE_CIRCLE
					+ user.getObjectId() + Constants.IMG_TYPE, yHeadPath);
			// 上传头像 和 个人信息
			fFile.saveInBackground(new SaveCallback() {
				@Override
				public void done(AVException e) {
					// TODO Auto-generated method stub
					if (e != null) {
						return;
					}
					user.setProfileClip(fFile);
					yFile.saveInBackground(new SaveCallback() {
						@Override
						public void done(AVException e) {
							// TODO Auto-generated method stub
							if (e != null) {
								return;
							}
							user.setProfileOrign(yFile);
							ObjUserWrap.completeUserInfo(user,
									new ObjFunBooleanCallback() {

										@Override
										public void callback(boolean result,
												AVException e) {
											// TODO Auto-generated method stub
											if (result) {
												// clickBtn.setText(LOAD_SUC);
												Toast.makeText(
														getApplicationContext(),
														"save 上传成功", 1000)
														.show();
												// 更新头像
												headURl = user.getProfileClip()
														.getUrl();
												log.e("lucifer", "url"
														+ headURl);
												bitmapUtils.display(ivTouxiang,
														headURl);

											} else {
												// clickBtn.setText(LOAD_FAIL);
												Toast.makeText(
														getApplicationContext(),
														"save 上传失败", 1000)
														.show();
											}
										}
									});
						}
					});
				}
			});

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public Bitmap getThumbnail(Uri uri, int size) throws FileNotFoundException,
			IOException {
		InputStream input = this.getContentResolver().openInputStream(uri);
		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1)
				|| (onlyBoundsOptions.outHeight == -1))
			return null;
		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;
		double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = this.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	
	/**
	 * 关注用户
	 *   
	 * @author lucifer
	 * @date 2015-12-7
	 */
	public void followInUser(){
		ObjFollowWrap.followIn(userMy, userId, new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if(e!=null){
					log.e("zcq  关注", e);
					return;
				}
				if(result){
					log.e("zcq", "关注成功");
					Toast.makeText(getApplicationContext(), "关注成功", Toast.LENGTH_SHORT).show();
					isFollow=true;
					userLikeImv.setImageResource(R.drawable.mine_photoview_btn_like_hl);
				}else{
					log.e("zcq", "关注失败");
					Toast.makeText(getApplicationContext(), "关注失败", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	
	/**
	 * 取消关注用户
	 *   
	 * @author lucifer
	 * @date 2015-12-7
	 */
	public void dismissFollowUser(){
		
		ObjFollowWrap.unFollow(userMy, userId, new ObjFunBooleanCallback() {
			
			@Override
			public void callback(boolean result, AVException e) {
				if(e!=null){
					log.e("zcq  取消关注", e);
					return;
				}
				if(result){
					//取消关注后   需要  换图片 改变状态  从缓存的关注列表中删除该用户
					log.e("zcq", "取消关注成功");
					Toast.makeText(getApplicationContext(), "取消关注成功", Toast.LENGTH_SHORT).show();
					isFollow=false;
					userLikeImv.setImageResource(R.drawable.mine_photoview_btn_like_nor2x);
					
					userAboutDao.deleteUserTypeUserId(userMy.getObjectId(), Constants.FOLLOW_TYPE, "", userId);
				}else{
					log.e("zcq", "取消关注失败");
					Toast.makeText(getApplicationContext(), "取消关注失败", Toast.LENGTH_SHORT).show();
				}				
			}
		});
	}

}
