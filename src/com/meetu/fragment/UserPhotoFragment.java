package com.meetu.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.meetu.activity.mine.MinephotoActivity;
import com.meetu.adapter.PhotoWallAdapter.GridViewHeightaListener;
import com.meetu.adapter.UserPhotoWallAdapter;
import com.meetu.adapter.UserPhotoWallAdapter.OnItemClickCallBack;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.callback.ObjUserPhotoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.view.ScrollTabHolderFragment;
import com.meetu.view.ScrollTabHolderMineupFragment;

public class UserPhotoFragment extends ScrollTabHolderFragment implements
		OnItemClickCallBack {

	private View view;
	private LinearLayout newsList;

	private RecyclerView mRecyclerView;

	private UserPhotoWallAdapter mAdapter;
	private GridViewHeightaListener gridViewHeightaListener;

	// 网络数据 相关
	private static AVUser currentUser = AVUser.getCurrentUser();

	// 当前用户
	private ObjUser userObjUser = new ObjUser();
	private static ObjUser userMy;
	// 网络请求下来的 图片信息
	private List<ObjUserPhoto> objUserPhotos = new ArrayList<ObjUserPhoto>();

	private StaggeredGridLayoutManager mLayoutMgr;
	private int mScrollY;
	private int mPosition;
	private static final String ARG_POSITION = "position";
	private static String userId;
	private static Boolean isMyself = false;

	public UserPhotoFragment() {
		// TODO Auto-generated constructor stub
	}

	public static Fragment newInstance(int position, String userID) {
		UserPhotoFragment fragment = new UserPhotoFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_POSITION, position);
		fragment.setArguments(args);
		userId = userID;
		if (currentUser != null) {
			userMy = AVUser.cast(currentUser, ObjUser.class);
		}

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
			if (userMy.getObjectId().equals(userId)) {
				isMyself = true;
			}
			getUserInfo(userId);
			// if (currentUser != null) {
			// //强制类型转换
			// user = AVUser.cast(currentUser, ObjUser.class);
			// }
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
				if (mScrollY < 0) {
					mScrollY = 0;
				}

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
		ObjUserPhotoWrap.queryUserPhoto(userObjUser,
				new ObjUserPhotoCallback() {

					@Override
					public void callback(List<ObjUserPhoto> objects,
							AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							return;
						} else if (objects != null) {
							objUserPhotos.addAll(objects);
							// mAdapter=new StaggeredHomeAdapter(getActivity(),
							// objUserPhotos);
							// mRecyclerView.setAdapter(mAdapter);
							log.e("lucifer", "我的照片数量" + objUserPhotos.size()
									+ "url=="
									+ objUserPhotos.get(0).getPhoto().getUrl());
							log.e("zcq", ""
									+ objUserPhotos.get(0).getPraiseCount());
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
		intent.putExtra("userId", userId);
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
				if (e != null) {
					return;
				} else if (user != null) {
					userObjUser = user;

					loaddata();
				} else {
					return;
				}

			}
		});
	}
}
