package com.meetu.activity.homepage;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.R;
import com.meetu.R.drawable;
import com.meetu.R.id;
import com.meetu.R.layout;
import com.meetu.adapter.MemoryPhotoAdapter;
import com.meetu.adapter.MinePhotoAdapter;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityPhotoWrap;
import com.meetu.entity.PhotoWall;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class MemoryPhotoActivity extends Activity implements
		OnPageChangeListener {
	private PhotoWall dataPhotoWall;
	private String itemid;
	private ViewPager viewPager;
	int id;
	// private List<PhotoWall> photolist=new ArrayList<PhotoWall>();
	private MemoryPhotoAdapter adapter;
	private ImageView favor;
	private TextView favorNumber;
	private int favourWight;// 点赞布局的宽度
	private int windowWidth;
	// 控件相关
	private RelativeLayout favorLayout;

	// 网络相关
	// 网络相关信息
	private ActivityBean activityBean = new ActivityBean();
	private ObjActivity objActivity = null;
	private List<ObjActivityPhoto> objphotoList = new ArrayList<ObjActivityPhoto>();
	// 当前用户
	ObjUser user = new ObjUser();
	AVUser currentUser = AVUser.getCurrentUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_memory_photo);
		itemid = super.getIntent().getStringExtra("id");
		objphotoList = (List<ObjActivityPhoto>) getIntent().getExtras()
				.getSerializable("ObjActivityPhoto");
		log.e("lucifer", "objphotoList" + objphotoList.size());
		id = Integer.parseInt(itemid);
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}
		initView();
	}

	private void initView() {
		viewPager = (ViewPager) super
				.findViewById(R.id.viewpager_photo_memory_detial);
		load();
		adapter = new MemoryPhotoAdapter(this, objphotoList);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);

		viewPager.setCurrentItem(id);
		favorNumber = (TextView) super
				.findViewById(R.id.number_favor_memory_detial);
		favorNumber.setText("" + objphotoList.get(id).getPraiseCount());
		favor = (ImageView) super.findViewById(R.id.favor_memory_detial);

		// 检查是否点赞
		isFavor(objphotoList.get(id), user);

		favorLayout = (RelativeLayout) super
				.findViewById(R.id.favor_photo_memory_detial_rl);
		// 按机型获取宽度，单位 像素
		favourWight = DensityUtil.dip2px(this, 80);
		windowWidth = DisplayUtils.getWindowWidth(this);
	}

	private void load() {
	}

	@Override
	public void onPageScrollStateChanged(int position) {
		// TODO Auto-generated method stub
		log.e("zcq" + "position=" + position);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (arg2 != 0) {
			// TODO Auto-generated method stub
			// arg1左滑是从【0,1】，右滑【1,0】 ，arg2是滑动的像素,左滑。增大一个屏幕宽度的像素
			// log.e("lucifer"+"arg0="+arg0+"    arg1="+arg1+"    arg2="+arg2);
			RelativeLayout.LayoutParams params = (LayoutParams) favorLayout
					.getLayoutParams();
			params.leftMargin = (favourWight * Math.abs(arg2 - windowWidth / 2)
					/ (windowWidth / 2) - favourWight);
			favorLayout.setLayoutParams(params);
			log.e("lucifer", "favourWight=" + favourWight
					+ "  params.leftMargin =" + params.leftMargin);
		}
		isFavor(objphotoList.get(arg0), user);
		favorNumber.setText("" + objphotoList.get(id).getPraiseCount());
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		log.e("lucifer" + "position=" + position);
	}

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.favor_memory_detial :
	// favor.setImageResource(R.drawable.mine_photoview_fullscreen_btn_collect_hi_2x);
	//
	//
	// break;
	//
	// default :
	// break;
	// }
	//
	// }
	/**
	 * 判断是否点赞过
	 * 
	 * @author lucifer
	 * @date 2015-11-14
	 */
	public void isFavor(final ObjActivityPhoto photo, final ObjUser user) {
		ObjActivityPhotoWrap.queryPhotoPraise(photo, user,
				new ObjFunBooleanCallback() {

					@Override
					public void callback(boolean result, AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							log.e("zcq", e);
						} else if (result) {
							favor.setImageResource(R.drawable.mine_photoview_fullscreen_btn_collect_hi_2x);
							favor.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									favor.setImageResource(R.drawable.acty_memorieswall_fullscreen_btn_like_nor);
									ObjActivityPhotoWrap
											.cancelPraiseActivityPhoto(
													user,
													photo,
													new ObjFunBooleanCallback() {

														@Override
														public void callback(
																boolean result,
																AVException e) {
															// TODO
															// Auto-generated
															// method stub
															if (e != null) {
																log.e("zcq", e);
															} else if (result) {
																log.e("zcq",
																		"取消点赞成功");
																MemoryWallActivity.handler
																		.sendEmptyMessage(1);

															} else {
																log.e("zcq",
																		"取消点赞失败");
																MemoryWallActivity.handler
																		.sendEmptyMessage(1);
															}

														}
													});

								}
							});
						} else {
							favor.setImageResource(R.drawable.acty_memorieswall_fullscreen_btn_like_nor);
							favor.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									favor.setImageResource(R.drawable.mine_photoview_fullscreen_btn_collect_hi_2x);
									ObjActivityPhotoWrap.praiseActivityPhoto(
											photo, user,
											new ObjFunBooleanCallback() {

												@Override
												public void callback(
														boolean result,
														AVException e) {
													if (e != null) {
														log.e("zcq", e);
													} else if (result) {
														log.e("zcq", "点赞成功");

														MemoryWallActivity.handler
																.sendEmptyMessage(1);
													} else {
														log.e("zcq", "点赞失败");
														MemoryWallActivity.handler
																.sendEmptyMessage(1);
													}

												}
											});

								}
							});
						}

					}
				});
	}

}
