package com.meetu.activity.homepage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.adapter.StaggeredHomeAdapter;
import com.meetu.adapter.StaggeredMemoryWallAdapter;
import com.meetu.adapter.StaggeredMemoryWallAdapter.OnItemClickCallBack;
import com.meetu.adapter.StaggeredMemoryWallAdapter.OnItemFavourMemory;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjActivityPhotoCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjActivityPhotoWrap;
import com.meetu.common.Images;
import com.meetu.entity.PhotoWall;
import com.meetu.entity.PhotoWallTest;
import com.meetu.tools.DensityUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class MemoryWallActivity extends Activity implements
		OnItemClickCallBack, OnClickListener {

	private RecyclerView mRecyclerView;
	private static StaggeredMemoryWallAdapter mAdapter;
	private List<PhotoWall> data = new ArrayList<PhotoWall>();
	private RelativeLayout backLayout;
	private ImageView barrage;
	// 网络相关信息
	private ActivityBean activityBean = new ActivityBean();
	private ObjActivity objActivity = null;
	private List<ObjActivityPhoto> photoList = new ArrayList<ObjActivityPhoto>();
	// 当前用户
	ObjUser user = new ObjUser();
	AVUser currentUser = AVUser.getCurrentUser();
	
	//空状态
	private RelativeLayout noneOrFailLayout;
	private TextView noneTextView;
	private TextView failtTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_memory_wall);
		activityBean = (ActivityBean) getIntent().getExtras().getSerializable(
				"activityBean");
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		}

		initLoadActivity(activityBean.getActyId());
		noneOrFailLayout=(RelativeLayout) findViewById(R.id.none_or_fail_memory_wall_rl);
		noneTextView=(TextView) findViewById(R.id.none_memory_wall_tv);
		failtTextView=(TextView) findViewById(R.id.fail_memory_wall_tv);
		initView();
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

	private void initView() {
		mRecyclerView = (RecyclerView) super
				.findViewById(R.id.id_RecyclerView_memoryWall);
		// loaddataUrl();
		loaddata();
		mAdapter = new StaggeredMemoryWallAdapter(this, photoList);
		// 对点赞 的回调进行处理
		mAdapter.setOnItemFavourMemory(new OnItemFavourMemory() {

			@Override
			public void onItemFavour(int position) {
				// TODO Auto-generated method stub
				ObjActivityPhotoWrap.praiseActivityPhoto(
						photoList.get(position), user,
						new ObjFunBooleanCallback() {

							@Override
							public void callback(boolean result, AVException e) {
								if (e != null) {
									log.e("zcq", e);
								} else if (result) {
									log.e("zcq", "点赞成功");
									handler.sendEmptyMessage(1);
								} else {
									log.e("zcq", "点赞失败");
									handler.sendEmptyMessage(1);
								}

							}
						});

			}

			@Override
			public void onItemCancleFavour(int position) {
				ObjActivityPhotoWrap.cancelPraiseActivityPhoto(user,
						photoList.get(position), new ObjFunBooleanCallback() {

							@Override
							public void callback(boolean result, AVException e) {
								if (e != null) {
									log.e("zcq", e);
								} else if (result) {
									log.e("zcq", "取消点赞成功");
									handler.sendEmptyMessage(1);
								} else {
									log.e("zcq", "取消点赞失败");
									handler.sendEmptyMessage(1);
								}

							}
						});

			}
		});
		mAdapter.setOnItemClickLitener(this);
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL));
		mRecyclerView.setAdapter(mAdapter);

		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_memorywall_homepager_rl);
		backLayout.setOnClickListener(this);
		barrage = (ImageView) super.findViewById(R.id.barrage_memorywall_img);
		barrage.setOnClickListener(this);
	}

	private void loaddata() {

		ObjActivityPhotoWrap.queryActivityPhotos(objActivity,
				new ObjActivityPhotoCallback() {

					@Override
					public void callback(List<ObjActivityPhoto> objects,
							AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							log.e("zcq 加载失败", e);
							noneOrFailLayout.setVisibility(View.VISIBLE);
							noneTextView.setVisibility(View.GONE);
							failtTextView.setVisibility(View.VISIBLE);
							noneOrFailLayout.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									loaddata();
								}
							});
						} else if (objects != null && objects.size() > 0) {
							
							
							noneOrFailLayout.setVisibility(View.GONE);
							photoList.addAll(objects);
							handler.sendEmptyMessage(1);
						} else {
							// Toast.makeText(getApplicationContext(), "数据加载失败",
							// 1000).show();
							noneOrFailLayout.setVisibility(View.VISIBLE);
							noneTextView.setVisibility(View.VISIBLE);
							failtTextView.setVisibility(View.GONE);
						}

						
					}
				});

	}

	@Override
	public void onItemClick(int id) {
		Intent intent = new Intent(this, MemoryPhotoActivity.class);
		intent.putExtra("id", "" + id);
		Bundle bundle = new Bundle();
		bundle.putSerializable("ObjActivityPhoto", (Serializable) photoList);
		intent.putExtras(bundle);
		startActivity(intent);

		this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

	}

	@Override
	public void onItemLongClick(View view, int position) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_memorywall_homepager_rl:
			finish();

			break;
		case R.id.barrage_memorywall_img:
			Intent intent = new Intent(this, BarrageActivity.class);
			startActivity(intent);

		default:
			break;
		}

	}

	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:

				mAdapter.notifyDataSetChanged();

				break;
			}
		}

	};

}
