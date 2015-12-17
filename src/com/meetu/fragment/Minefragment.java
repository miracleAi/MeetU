package com.meetu.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.MainActivity;
import com.meetu.activity.SetPersonalInformation2Activity;
import com.meetu.activity.SystemSettingsActivity;
import com.meetu.activity.mine.UpdatepictureActivity;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.adapter.PhotoWallAdapter.GridViewHeightaListener;
import com.meetu.adapter.ViewPagerAdapter;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Middle;
import com.meetu.tools.BitmapCut;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;
import com.meetu.view.MyScrollView;
import com.meetu.view.MyScrollView.OnScrollListener;

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
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Minefragment extends Fragment implements OnPageChangeListener,
		OnCheckedChangeListener, OnClickListener, OnScrollListener {

	private RadioGroup group = null;
	private ViewPager viewPager;
	private View view;
	private int viewpagerHight;
	private ImageView updateImageView, ivTouxiang;

	private MyScrollView myScrollView;
	private int mHightping;
	private int mscrollY;

	private float positionY;

	private LinearLayout ll;

	private int topHight;// 向上滑动的绝对高度。滑到此高度 悬浮
	// fragment 滑动
	private List<Fragment> list = new ArrayList<Fragment>();
	private ViewPagerAdapter adapter = null;

	// 控件相关
	private RelativeLayout setting;
	private boolean isMyserf = true;// 是否是我自己进入这个页面 默认是我自己true

	// 网络 数据相关
	private BitmapUtils bitmapUtils;
	private String headURl = "";// 头像的URL
	// 拿本地的 user
	private AVUser currentUser = AVUser.getCurrentUser();

	// 上传 信息 头像相关
	private String fHeadPath = "";
	private String yHeadPath = "";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_mine, null);

			bitmapUtils = new BitmapUtils(getActivity());
			if (currentUser != null) {
				// 强制类型转换
				ObjUser user = AVUser.cast(currentUser, ObjUser.class);

				headURl = user.getProfileClip().getUrl();
				log.e("lucifer", "url" + headURl);
			}

			group = (RadioGroup) view.findViewById(R.id.group_va_tab);
			group.setOnCheckedChangeListener(this);
			viewPager = (ViewPager) view.findViewById(R.id.mine_viewpager_va);
			viewPager.setOnPageChangeListener(this);
			initPageView();
			getBackHeight();
			group.check(1);
			updateImageView = (ImageView) view
					.findViewById(R.id.update_mine_img);
			updateImageView.setOnClickListener(this);
			myScrollView = (MyScrollView) view
					.findViewById(R.id.scroll_content);

			// //动态设置myScrollView高度
			// RelativeLayout.LayoutParams
			// params=(android.widget.RelativeLayout.LayoutParams)
			// myScrollView.getLayoutParams();
			// params.height=DensityUtil.dip2px(getActivity(),
			// 275)+DisplayUtils.getWindowHeight(getActivity());
			// myScrollView.setLayoutParams(params);

			mHightping = DisplayUtils.getWindowHeight(getActivity());
			ivTouxiang = (ImageView) view
					.findViewById(R.id.mine_btn_profile_iv);

			// Bitmap head=readHead();
			if (headURl != null&&!headURl.equals("")) {
				// ivTouxiang.setImageBitmap(head);
				// 加载网络图片
				bitmapUtils.display(ivTouxiang, headURl);
			}
			ivTouxiang.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showDialog();
					/*
					 * Intent intent = new
					 * Intent(getActivity(),UserPagerActivity.class);
					 * startActivity(intent);
					 */
				}
			});

			ll = (LinearLayout) view.findViewById(R.id.button_ll_mine);
			LinearLayout.LayoutParams params2 = (LayoutParams) ll
					.getLayoutParams();
			int h = params2.height;
			log.e("lucifer", "h=" + h);
			ll.setLayoutParams(params2);

			topHight = DensityUtil.dip2px(getActivity(), 285);

			setting = (RelativeLayout) view
					.findViewById(R.id.setting_mine_fragment_rl);
			setting.setOnClickListener(this);

		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initPageView() {
		// 个人信息fragment 并且把url传过去
		MinePersonalInformation personalInformation = new MinePersonalInformation();
		Bundle bundle1 = new Bundle();
		bundle1.putString("personalInformationUrl", "");
		personalInformation.setArguments(bundle1);

		// 照片墙fragment 并且把url传过去
		MinePhotoWallfragment photoWall = new MinePhotoWallfragment();
		// photoWall.setGridViewHeightaListener(this);

		Bundle bundle2 = new Bundle();
		bundle2.putString("photoWallUrl", "");
		photoWall.setArguments(bundle2);

		// list.add(personalInformation);
		// list.add(photoWall);

		adapter = new ViewPagerAdapter(super.getActivity()
				.getSupportFragmentManager(), list);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(1);
		// viewPager.setCurrentItem(0);

	}

	private void setTag(int idx) {

		RadioButton rb = (RadioButton) group.getChildAt(idx);
		rb.setChecked(true);
		if (idx == 0) {
			// 设置上传图片 图片隐藏
			updateImageView.setVisibility(View.INVISIBLE);

		}
		if (idx == 1) {
			// 设置上传图片 图片显示
			updateImageView.setVisibility(View.VISIBLE);

			myScrollView.setOnScrollListener(this);
		}
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	}

	private Intent intent;

	@Override
	public void onCheckedChanged(RadioGroup group, int position) {

		// Toast.makeText(getActivity(), ""+position,
		// Toast.LENGTH_SHORT).show();
		if (position == 1) {

			intent = new Intent();

		} else if (position == 2) {

		}
		viewPager.setCurrentItem(position - 1);
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

	// /**
	// * 照片墙当高度变化的时候就会响应这个监听
	// */
	// @Override
	// public void callBackHeight(int height) {
	// viewpagerHight = height;
	// LinearLayout.LayoutParams params = (LayoutParams)
	// viewPager.getLayoutParams();
	// params.height = viewpagerHight;
	// viewPager.setLayoutParams(params);
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 上传照片：
		case R.id.update_mine_img:
			// Toast.makeText(getActivity(), "s", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent, 00);
			break;
		case R.id.mine_btn_profile_iv:

			break;
		// 点击设置
		case R.id.setting_mine_fragment_rl:

			Intent intent2 = new Intent(getActivity(),
					SystemSettingsActivity.class);
			startActivity(intent2);
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

				((MinePhotoWallfragment) list.get(1)).reflesh(false);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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

	/**
	 * 动态设置viewpager的高度
	 */
	public void getBackHeight() {

		viewpagerHight = DisplayUtils.getWindowHeight(getActivity())
				- DensityUtil.dip2px(getActivity(), 50 + 79) - 1;
		LinearLayout.LayoutParams params = (LayoutParams) viewPager
				.getLayoutParams();
		params.height = viewpagerHight;
		viewPager.setLayoutParams(params);
		log.e("lucifer", "viewpagerHight=" + viewpagerHight);
	}

	/**
	 * 根据scrollview滑动进行判断
	 */
	@Override
	public void onScroll(final int scrollY) {

		Log.e("lucifer", "滑动距离scrollY " + scrollY);
		mscrollY = scrollY;
		myScrollView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				if (scrollY < topHight) {
					return false;
				} else {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// 按下了屏幕
						positionY = event.getY();
						break;
					case MotionEvent.ACTION_MOVE:
						// 移动了
						float y = event.getY();
						// 判断下滑
						if (y > positionY) {
							// Toast.makeText(getActivity(), "下移动了",
							// Toast.LENGTH_LONG).show();
							log.e("判断移动方向", "下移动了");
							positionY = y;
							return false;
						}
						Log.e("判断移动距离", "positionY " + positionY + " y " + y);
						break;
					default:
						break;
					}
					return true;
				}
			}
		});

	}

	/**
	 * 从内存卡读缓存
	 * 
	 * @return
	 */
	public Bitmap readHead() {
		String file = Environment.getExternalStorageDirectory()
				+ "/user_header.png";
		return BitmapFactory.decodeFile(file);
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
												Toast.makeText(getActivity(),
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

}
