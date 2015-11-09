package com.meetu.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;







import com.meetu.R;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.meetu.activity.mine.MinephotoActivity;
import com.meetu.activity.mine.UpdatepictureActivity;
import com.meetu.adapter.PhotoWallAdapter;
import com.meetu.adapter.PhotoWallAdapter.GridViewHeightaListener;
import com.meetu.adapter.StaggeredHomeAdapter;
import com.meetu.adapter.StaggeredHomeAdapter.OnItemClickCallBack;
import com.meetu.cloud.callback.ObjUserPhotoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.wrap.ObjUserPhotoWrap;
import com.meetu.entity.Middle;
import com.meetu.entity.PhotoWall;
import com.meetu.tools.BitmapCut;
import com.meetu.view.MyRecyclerView.OnScrollListener;






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

public class MinePhotoWallfragment extends Fragment implements OnItemClickCallBack{
	

	private PullToRefreshGridView pview;
//	private PhotoWallAdapter adapter;
//	private List<PhotoWall> data=new ArrayList<PhotoWall>();

	private View view;
	private LinearLayout newsList;
	
	private RecyclerView mRecyclerView;
	
	private StaggeredHomeAdapter mAdapter;
	private GridViewHeightaListener gridViewHeightaListener;
	
	//网络数据 相关
	private AVUser currentUser = AVUser.getCurrentUser();
	 //当前用户
	private ObjUser user = new ObjUser();
	//网络请求下来的 图片信息
	private List<ObjUserPhoto> objUserPhotos=new ArrayList<ObjUserPhoto>();
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
//		afinal=new FinalHttp(); 
	}

	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(view==null){
			
			if (currentUser != null) {
				//强制类型转换
				user = AVUser.cast(currentUser, ObjUser.class);
			}

			view=inflater.inflate(R.layout.fragment_mine_photo_wall, null);
			mRecyclerView=(RecyclerView) view.findViewById(R.id.id_RecyclerView);
			
			mAdapter=new StaggeredHomeAdapter(getActivity(), objUserPhotos);
			
			mAdapter.setOnItemClickLitener(this);
			
			mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager
					(2,StaggeredGridLayoutManager.VERTICAL));
			mRecyclerView.setAdapter(mAdapter);
			loaddata();
			
			//计算recycleview 的滑动距离

			
			
		}
		ViewGroup parent=(ViewGroup)view.getParent();
		if(parent!=null){
			parent.removeView(view);
		}
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
	
		
	}

	public void setGridViewHeightaListener(GridViewHeightaListener gridViewHeightaListener) {
		this.gridViewHeightaListener = gridViewHeightaListener;
	}
//	
	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	}
	private void loaddata(){
//		data=new ArrayList<PhotoWall>();
//		data.add(new PhotoWall(10,R.drawable.img1_ceshi));
//		data.add(new PhotoWall(2,R.drawable.img2_ceshi));
//		data.add(new PhotoWall(3,R.drawable.img3_ceshi));
//		data.add(new PhotoWall(4,R.drawable.img4_ceshi));
//		data.add(new PhotoWall(5,R.drawable.img5_ceshi));
//		data.add(new PhotoWall(5,R.drawable.img1_ceshi));
//		data.add(new PhotoWall(7,R.drawable.img2_ceshi));
//		data.add(new PhotoWall(10,R.drawable.img3_ceshi));
//		data.add(new PhotoWall(12,R.drawable.img4_ceshi));
//		data.add(new PhotoWall(10,R.drawable.img5_ceshi));
//		data.add(new PhotoWall(11,R.drawable.img1_ceshi));
//		data.add(new PhotoWall(12,R.drawable.img2_ceshi));
//		data.add(new PhotoWall(13,R.drawable.img3_ceshi));
//		data.add(new PhotoWall(14,R.drawable.img4_ceshi));
//		data.add(new PhotoWall(15,R.drawable.img5_ceshi));
		
		ObjUserPhotoWrap.queryUserPhoto(user, new ObjUserPhotoCallback() {
			
			@Override
			public void callback(List<ObjUserPhoto> objects, AVException e) {
				// TODO Auto-generated method stub
				
				objUserPhotos=objects;
				mAdapter=new StaggeredHomeAdapter(getActivity(), objUserPhotos);
				log.e("lucifer", "我的照片数量"+objUserPhotos.size()+"url=="+objUserPhotos.get(0).getPhoto().getUrl());
				handler.sendEmptyMessage(1);
			}
		});

	}

	@Override
	public void onItemClick(int id) {
		// TODO Auto-generated method stub
//		Toast.makeText(getActivity(), "点击了某个位置"+id, Toast.LENGTH_SHORT).show();
		Intent intent =new Intent(super.getActivity(),MinephotoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("PhotoWall",id);
		
		intent.putExtras(bundle);
		intent.putExtra("id", ""+id);
		
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}

	@Override
	public void onItemLongClick(View view, int position) {
		// TODO Auto-generated method stub
		
	}
	
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 1:
				mAdapter.notifyDataSetChanged();
		//		refreshComplete();
				log.e("zcq", "刷新了");
				break;
			}
		}
	
	};
	private void refreshComplete(){
		mRecyclerView.postDelayed(new Runnable() {
	
	            @Override
	            public void run() {
	      //      	mRecyclerView.onRefreshComplete();
	            	
	            }
	    }, 500);
	}

	

	
	
	


	

	

}
