package com.meetu.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.activity.SystemSettingsActivity;
import com.meetu.activity.mine.UpdatepictureActivity;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.adapter.PhotoPagerAdapter;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Middle;
import com.meetu.tools.BitmapCut;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;
import com.meetu.tools.UriToBitmap;
import com.meetu.view.CustomViewPager;
import com.meetu.view.MyScrollView;
import com.meetu.view.ScrollTabHolder;
import com.meetu.view.SlidingTabLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author lucifer
 * @date 2015-11-25
 * @return
 */
public class MineUpfragment extends Fragment implements ScrollTabHolder,
OnClickListener {

	private CustomViewPager userPager;
	private SlidingTabLayout userTab;
	private LinearLayout headView;

	private ViewPagerAdapter adapter = null;
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

	private View view;
	// 控件相关
	private RelativeLayout settingLayout;
	private ImageView updateImageView, ivTouxiang;
	private TextView userName;
	// 网络相关
	private BitmapUtils bitmapUtils;
	private String headURl = "";// 头像的URL
	// 拿本地的 user
	private AVUser currentUser = AVUser.getCurrentUser();
	private ObjUser user;
	// 上传 信息 头像相关
	private String fHeadPath = "";
	private String yHeadPath = "";
	private ImageView sexImg;
	
	MinePhotoWallfragment photoWallFragment=null;
	
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_mine_up, null);
			bitmapUtils = new BitmapUtils(getActivity());
			if (currentUser != null) {
				// 强制类型转换
				user = AVUser.cast(currentUser, ObjUser.class);

				if(user.getProfileClip()!=null){
					headURl = user.getProfileClip().getUrl();
				}

				log.e("lucifer", "url" + headURl);
			}
			initView();
			initValues();

			userPager.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// 触摸位于头部，禁止横向滑动
					if (px2dip(getActivity().getApplicationContext(),
							event.getY()) < tempA) {
						userPager.setScanScroll(false);
						return true;
					} else {
						userPager.setScanScroll(true);
						return false;
					}

				}
			});
			setupAdapter();

		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		userPager = (CustomViewPager) view
				.findViewById(R.id.user_pager_mine_up_fragment);
		userTab = (SlidingTabLayout) view
				.findViewById(R.id.user_tab_mine_up_fragment);
		headView = (LinearLayout) view
				.findViewById(R.id.head_view_mine_up_fragment);

		settingLayout = (RelativeLayout) view
				.findViewById(R.id.setting_mine_up_fragment_rl);
		settingLayout.setOnClickListener(this);
		ivTouxiang = (ImageView) view
				.findViewById(R.id.user_profile_iv_mine_up_fragment_img);

		if (headURl != null) {
			// ivTouxiang.setImageBitmap(head);
			// 加载网络图片
			bitmapUtils.display(ivTouxiang, headURl);
		}

		ivTouxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog();

			}
		});
		userName = (TextView) view
				.findViewById(R.id.user_name_fragment_mine_up_tv);
		userName.setText("" + user.getNameNick());
		updateImageView = (ImageView) view
				.findViewById(R.id.update_mine_up_fragment_img);
		updateImageView.setOnClickListener(this);
		sexImg=(ImageView) view.findViewById(R.id.user_gender_imv);
		if(user.getGender()==2){
		//	sexImg.setImageResource(R.drawable);
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

	private void setupAdapter() {
		if (adapter == null) {
			adapter = new ViewPagerAdapter(getActivity()
					.getSupportFragmentManager(), numFragments);
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
					updateImageView.setVisibility(View.VISIBLE);
				}
			}

			@SuppressLint("NewApi")
			@Override
			public void onPageSelected(int position) {
				log.e("zcq", "position==" + position);

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
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void adjustScroll(int scrollHeight, int headerHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollViewScroll(ScrollView view, int x, int y, int oldX,
			int oldY, int pagePosition) {
		// TODO Auto-generated method stub
		tempA = 285 - px2dip(getActivity().getApplicationContext(), y);
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
				// fragment = UserInfoFragment.newInstance(0);
				fragment = MinePersonalInformation.newInstance(0);

				break;

			case 1:
				// fragment = UserPhotoFragment.newInstance(1);
				fragment = MinePhotoWallfragment.newInstance(1);

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
			log.e("zcq", "position==" + position);
			mScrollTabHolders.put(position, (ScrollTabHolder) object);


			if(object instanceof MinePhotoWallfragment){
				photoWallFragment = (MinePhotoWallfragment) object;
			}


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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_mine_up_fragment_rl:
			Intent intent2 = new Intent(getActivity(),
					SystemSettingsActivity.class);
			startActivity(intent2);
			break;
		case R.id.update_mine_up_fragment_img:
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent, 00);
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
				ContentResolver resolver = getActivity().getContentResolver();
				Bundle extras = data.getExtras();
				Uri url = data.getData();
				try {
					//
					photoPortait = getThumbnail(url, 2000);
					//
					
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
				
	//			photoPortait=UriToBitmap.getBitmapFromUri(getActivity(), url);
				if (photoPortait != null) {
					Middle.bimaBitmap = photoPortait;
					// saveHeadImg(headerPortait);
					// updata.setImageBitmap(headerPortait);
				}
				Intent intent = new Intent(getActivity(),
						UpdatepictureActivity.class);
				startActivityForResult(intent, 44);
			}
			break;
		case 11:
			if (resultCode == getActivity().RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		case 22:
			if (resultCode == getActivity().RESULT_OK) {
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
			if (resultCode == getActivity().RESULT_OK) {
				// TODO 刷新 照片列表
				log.e("lucifer", "上传照片成功刷新照片列表");

			//	 ((MinePhotoWallfragment)MinePhotoWallfragment.newInstance(1));
			//	 MinePhotoWallfragment.newInstance(1);
				 photoWallFragment.reflesh();
				
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showDialog() {
		final AlertDialog portraidlg = new AlertDialog.Builder(getActivity())
		.create();
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
										Toast.makeText(getActivity(),
												"头像已上传", 1000)
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
										Toast.makeText(getActivity(),
												"上传头像失败", 1000)
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
		InputStream input = getActivity().getContentResolver().openInputStream(
				uri);
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
		input = getActivity().getContentResolver().openInputStream(uri);
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

}
