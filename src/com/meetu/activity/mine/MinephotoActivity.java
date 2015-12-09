package com.meetu.activity.mine;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.adapter.MinePhotoAdapter;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.entity.PhotoWall;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MinephotoActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	private String itemid;
	private String pid;
	private PhotoWall dataPhotoWall;
	private ImageView photo, back, delect, favor;
	private ViewPager viewPager;
	private MinePhotoAdapter adapter;
	int id;
	// private List<PhotoWall> photolist=new ArrayList<PhotoWall>();
	private RelativeLayout backLayout, delectLayout;
	// 网络数据相关
	private String photoUrl;
	private List<ObjUserPhoto> objUserPhotos = new ArrayList<ObjUserPhoto>();
	private String userId;// 用户的id
	private boolean isMyself = true;// 用来标记是否从我自己的页面跳过来的
	private int positionNow = -1;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_minephoto);
		userId = super.getIntent().getStringExtra("userId");
		itemid = super.getIntent().getStringExtra("id");
		pid = super.getIntent().getStringExtra("id");
		id = Integer.parseInt(itemid);
		positionNow = id;
		log.e("zcq", "id==" + itemid);
		objUserPhotos = (List<ObjUserPhoto>) this.getIntent()
				.getSerializableExtra("photolist");

		Intent intent = this.getIntent();

		photoUrl = intent.getStringExtra("url");

		if (userId != null) {
			isMyself = false;
		}
		initView();
	}

	/*
	 * 点击显示照片
	 */
	private void initView() {

		// photo = (ImageView) super.findViewById(R.id.photo_demail_mine);
		// photo.setImageResource(dataPhotoWall.getImg());
		back = (ImageView) super
				.findViewById(R.id.back_mine_photoview_fullscreen_img);
		back.setOnClickListener(this);
		delect = (ImageView) super
				.findViewById(R.id.deldect_mine_photoview_fullscreen_img);
		delect.setOnClickListener(this);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_mine_photoview_fullscreen_rl);
		backLayout.setOnClickListener(this);
		delectLayout = (RelativeLayout) super
				.findViewById(R.id.deldect_mine_photoview_fullscreen_rl);
		delectLayout.setOnClickListener(this);
		// favor = (ImageView) super.findViewById(R.id.favor_minephoto_mine);
		// favor.setOnClickListener(this);
		viewPager = (ViewPager) super.findViewById(R.id.viewpager_photo);
		adapter = new MinePhotoAdapter(this, objUserPhotos, userId);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		viewPager.setCurrentItem(id);

		if (isMyself == false) {
			delectLayout.setVisibility(View.GONE);
		} else {
			delectLayout.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_mine_photoview_fullscreen_rl:
			finish();
			break;
		case R.id.deldect_mine_photoview_fullscreen_rl:
			Toast.makeText(MinephotoActivity.this,"正在删除", 1000).show();
			ObjUserPhotoWrap.deleteUserPhoto(objUserPhotos.get(positionNow),new ObjFunBooleanCallback() {
				
				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if(result){
						Toast.makeText(MinephotoActivity.this, "已删除", 1000).show();
						objUserPhotos.remove(positionNow);
						adapter = new MinePhotoAdapter(MinephotoActivity.this, objUserPhotos, userId);
						viewPager.setAdapter(adapter);
						if(objUserPhotos.size() == 0){
							//回到主页需要刷新UU秀墙
							finish();
						}else if(positionNow > objUserPhotos.size()-1){
							viewPager.setCurrentItem(positionNow-1);
						}else{
							viewPager.setCurrentItem(positionNow);
						}
					}else{
						Toast.makeText(MinephotoActivity.this, "删除失败", 1000).show();
					}
				}
			});
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
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		positionNow = arg0;
	}

}
