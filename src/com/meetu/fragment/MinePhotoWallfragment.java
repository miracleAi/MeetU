package com.meetu.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.meetu.R;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.activity.mine.MinephotoActivity;
import com.meetu.activity.mine.UpdatepictureActivity;
import com.meetu.adapter.PhotoWallAdapter;
import com.meetu.adapter.UserPhotoWallAdapter;
import com.meetu.adapter.PhotoWallAdapter.GridViewHeightaListener;

import com.meetu.cloud.callback.ObjUserPhotoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.entity.Middle;
import com.meetu.entity.PhotoWall;
import com.meetu.tools.BitmapCut;
import com.meetu.view.MyRecyclerView.OnScrollListener;
import com.meetu.view.ScrollTabHolderMineupFragment;

import android.R.raw;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MinePhotoWallfragment extends ScrollTabHolderMineupFragment
		implements com.meetu.adapter.UserPhotoWallAdapter.OnItemClickCallBack {

	private View view;
	private LinearLayout newsList;

	private RecyclerView mRecyclerView;

	private UserPhotoWallAdapter mAdapter;
	private GridViewHeightaListener gridViewHeightaListener;

	// 网络数据 相关
	private AVUser currentUser = AVUser.getCurrentUser();
	// 当前用户
	private ObjUser user = new ObjUser();
	// 网络请求下来的 图片信息
	private List<ObjUserPhoto> objUserPhotos = new ArrayList<ObjUserPhoto>();

	private StaggeredGridLayoutManager mLayoutMgr;
	private int mScrollY;
	private int mPosition;
	private static final String ARG_POSITION = "position";

	public static Fragment newInstance(int position) {
		MinePhotoWallfragment fragment = new MinePhotoWallfragment();
		Bundle args = new Bundle();
		args.putInt(ARG_POSITION, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void adjustScroll(int scrollHeight, int headerHeight) {
		if (mRecyclerView == null)
			return;

		mScrollY = headerHeight - scrollHeight;
		// 滑块偏移某值之后为零点，滑块偏移量与view运动方向相反
		mLayoutMgr.scrollToPositionWithOffset(0, -mScrollY);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		// afinal=new FinalHttp();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			if (currentUser != null) {
				// 强制类型转换
				user = AVUser.cast(currentUser, ObjUser.class);
			}
			view = inflater.inflate(R.layout.fragment_mine_photo_wall, null);
			mPosition = getArguments().getInt(ARG_POSITION);
			initView();
			loaddata();
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initView() {
		mRecyclerView = (RecyclerView) view.findViewById(R.id.id_RecyclerView);
		mAdapter = new UserPhotoWallAdapter(getActivity(), objUserPhotos);
		// mAdapter.setOnItemClickLitener(this);
		mAdapter.setOnItemClickLitener(this);
		mLayoutMgr = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(mLayoutMgr);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				mScrollY += dy;
				if (mScrollTabHolder != null) {
					log.d("mytest", "zhixing");
					mScrollTabHolder.onRecyclerViewScroll(recyclerView,
							mScrollY, mPosition);
				}
			}
		});
	}

	public void setGridViewHeightaListener(
			GridViewHeightaListener gridViewHeightaListener) {
		this.gridViewHeightaListener = gridViewHeightaListener;
	}

	//
	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	}

	private void loaddata() {
		ObjUserPhotoWrap.queryUserPhoto(user, new ObjUserPhotoCallback() {

			@Override
			public void callback(List<ObjUserPhoto> objects, AVException e) {

				if (e != null) {
					return;
				} else if (objects != null) {
					objUserPhotos.addAll(objects);
					// mAdapter=new StaggeredHomeAdapter(getActivity(),
					// objUserPhotos);
					// mRecyclerView.setAdapter(mAdapter);
					log.e("lucifer", "我的照片数量" + objUserPhotos.size() + "url=="
							+ objUserPhotos.get(0).getPhoto().getUrl());
					log.e("zcq", "" + objUserPhotos.get(0).getPraiseCount());
					handler.sendEmptyMessage(1);
				}

			}
		});

	}

	@Override
	public void onItemClick(int id) {
		Intent intent = new Intent(super.getActivity(), MinephotoActivity.class);
		intent.putExtra("photolist", (Serializable) objUserPhotos);
		intent.putExtra("id", "" + id);
		log.e("lucifer", "id==" + id);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	@Override
	public void onItemLongClick(View view, int position) {
		// TODO Auto-generated method stub

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				mAdapter.notifyDataSetChanged();
				// refreshComplete();
				log.e("zcq", "刷新了");
				break;
			}
		}

	};

	private void refreshComplete() {
		mRecyclerView.postDelayed(new Runnable() {

			@Override
			public void run() {
				// mRecyclerView.onRefreshComplete();

			}
		}, 500);
	}

	/**
	 * 刷新数据列表重新加载
	 * 
	 * @author lucifer
	 * @date 2015-11-9
	 */
	public void reflesh() {
		mAdapter.notifyDataSetChanged();
	}
}
