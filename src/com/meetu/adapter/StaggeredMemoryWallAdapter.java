package com.meetu.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.meetu.R;
import com.meetu.adapter.PhotoWallAdapter.GridViewHeightaListener;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.common.ImageLoader;
import com.meetu.entity.PhotoWall;
import com.meetu.entity.PhotoWallTest;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.BitmapChange;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StaggeredMemoryWallAdapter extends
			RecyclerView.Adapter<StaggeredMemoryWallAdapter.MyViewHolder> {
	private Context mContext;

	private List<ObjActivityPhoto> mDatas;
	private LayoutInflater mInflater;
	private List<Integer> mHeights;
	private int width;
	private int leftHight=0,rightHight=0;
	
	private ImageLoader mImageLoader;

	private Bitmap bitmap;

	 private FinalBitmap finalBitmap;
	 private BitmapUtils bitmapUtils; 

	public interface OnItemClickCallBack {
		void onItemClick(int id);

		void onItemLongClick(View view, int position);
	}

	private OnItemClickCallBack mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickCallBack mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public StaggeredMemoryWallAdapter(Context context, List<ObjActivityPhoto> datas) {
		mContext=context;
		
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		width = DisplayUtils.getWindowWidth((Activity)context)-DensityUtil.dip2px(context, 28);
		log.e("lucifer", "width=="+width);
		
		MyApplication app=(MyApplication) context.getApplicationContext();
		finalBitmap=app.getFinalBitmap();

		bitmapUtils=new BitmapUtils(context);
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_memorywall, parent, false));
		
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		if (mDatas!=null && mDatas.size()>0){

					
			ObjActivityPhoto item = mDatas.get(position);
			if(position==0||position==1){
				LinearLayout.LayoutParams params=(android.widget.LinearLayout.LayoutParams) holder.rlAll.getLayoutParams();
				params.topMargin=DensityUtil.dip2px(mContext, 10);
				holder.rlAll.setLayoutParams(params);
			}
			
			int photoWidth=item.getPhotoWidth();
			int photoHight=item.getPhotoHeight();
			log.e("lucifer", "photoWidth=="+photoWidth+" photoHight=="+photoHight);
			int Hight=(int) ((double)photoWidth/(width/2)*(photoHight));
			log.e("lucifer", "Hight=="+Hight);
			if(photoWidth>=(width/2)){
				finalBitmap.display(holder.ivImg, item.getPhoto().getUrl(), width/2, Hight);
			}else{
				//处理bitmap 
				bitmapUtils.display(holder.ivImg, item.getPhoto().getUrl(),new BitmapLoadCallBack<ImageView>() {

					@Override
					public void onLoadCompleted(ImageView container, String uri,
							Bitmap bitmap, BitmapDisplayConfig config,
							BitmapLoadFrom from) {
						
						bitmap=BitmapChange.zoomImg(bitmap, width/2);
						log.e("zcq", "www=== "+bitmap.getWidth()+" hhh==="+bitmap.getHeight());
						holder.ivImg.setImageBitmap(bitmap);
					}

					@Override
					public void onLoadFailed(ImageView arg0, String arg1,
							Drawable arg2) {
						// TODO Auto-generated method stub
						
					}
				} );
				
			}
			
			
					
//					holder.ivImg.setTag(item.getImageURL());
//					
////					mImageLoader.showImageByAsyncTask(holder.ivImg, item.getImageURL());
//					mImageLoader.showImageByThread(holder.ivImg, item.getImageURL());
//					
					
					// 如果设置了回调，则设置点击事件
					if (mOnItemClickLitener != null) {
						holder.itemView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								mOnItemClickLitener.onItemClick(position);
							}
						});
			
						holder.itemView.setOnLongClickListener(new OnLongClickListener() {
							@Override
							public boolean onLongClick(View v) {
								// int pos = holder.getLayoutPosition();
								// mOnItemClickLitener.onItemLongClick(holder.itemView,
								// pos);
								// removeData(pos);
								return false;
							}
						});
		}       
	}
	}




	class MyViewHolder extends ViewHolder {
		private RelativeLayout rlAll;
		ImageView ivImg;

		int id;
		public MyViewHolder(View view) {
			super(view);
			
			ivImg = (ImageView) view.findViewById(R.id.img_item_memorywall);
			rlAll=(RelativeLayout) view.findViewById(R.id.item_memorywall_rl);
		}

		public void setData() {

		}
	}

}