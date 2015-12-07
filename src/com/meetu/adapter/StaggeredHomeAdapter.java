package com.meetu.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.meetu.adapter.PhotoWallAdapter.GridViewHeightaListener;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.entity.PhotoWall;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.BitmapChange;
import com.meetu.tools.DateUtils;
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

public class StaggeredHomeAdapter extends
		RecyclerView.Adapter<StaggeredHomeAdapter.MyViewHolder> {

	// private List<PhotoWall> mDatas;
	private List<ObjUserPhoto> mPhotos;
	private LayoutInflater mInflater;
	private List<Integer> mHeights;
	private int width;
	private Context mContext;

	// private List<Integer> mHeights;
	private GridViewHeightaListener gridViewHeightaListener;

	// 网络数据相关

	private BitmapUtils bitmapUtils;
	private String photoUrl;
	private FinalBitmap finalBitmap;

	public interface OnItemClickCallBack {
		void onItemClick(int id);

		void onItemLongClick(View view, int position);
	}

	private OnItemClickCallBack mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickCallBack mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public StaggeredHomeAdapter(Context context, List<ObjUserPhoto> datas) {

		mInflater = LayoutInflater.from(context);
		this.mPhotos = datas;
		log.d("mytest", "datas.size()==" + datas.size());
		width = DisplayUtils.getWindowWidth((Activity) context)
				- DensityUtil.dip2px(context, 28);
		mContext = context;
		bitmapUtils = new BitmapUtils(mContext);
		MyApplication app = (MyApplication) context.getApplicationContext();
		finalBitmap = app.getFinalBitmap();

	}

	@Override
	public int getItemCount() {
		log.d("mytest", "mPhotos.size()==" + mPhotos.size());
		return mPhotos.size();

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_photowall_fragment, parent, false));

		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		if (mPhotos != null && mPhotos.size() > 0) {

			ObjUserPhoto item = mPhotos.get(position);
			// 设置第一行的上magin
			if (position == 0 || position == 1) {
				RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) holder.rlAll
						.getLayoutParams();
				params.topMargin = DensityUtil.dip2px(mContext, 10);
				holder.rlAll.setLayoutParams(params);
			}
			photoUrl = item.getPhoto().getUrl();
			log.e("lucifer", "photoUrl==" + photoUrl);

			int photoWidth = item.getImageWidth();
			int photoHight = item.getImageHeight();
			log.e("lucifer", "photoWidth==" + photoWidth + " photoHight=="
					+ photoHight);
			int Hight = (int) ((double) photoWidth / (width / 2) * (photoHight));
			log.e("lucifer", "Hight==" + Hight);
			if (photoWidth >= (width / 2)) {
				finalBitmap.display(holder.ivImg, item.getPhoto().getUrl(),
						width / 2, Hight);
			} else {
				// 处理bitmap
				bitmapUtils.display(holder.ivImg, item.getPhoto().getUrl(),
						new BitmapLoadCallBack<ImageView>() {

							@Override
							public void onLoadCompleted(ImageView container,
									String uri, Bitmap bitmap,
									BitmapDisplayConfig config,
									BitmapLoadFrom from) {

								bitmap = BitmapChange
										.zoomImg(bitmap, width / 2);
								log.e("zcq", "www=== " + bitmap.getWidth()
										+ " hhh===" + bitmap.getHeight());
								holder.ivImg.setImageBitmap(bitmap);
							}

							@Override
							public void onLoadFailed(ImageView arg0,
									String arg1, Drawable arg2) {
								// TODO Auto-generated method stub

							}
						});

			}

			holder.ivFavorNumber.setText("" + item.getPraiseCount());
			holder.ivViewNumber.setText("" + item.getBrowseCount());
			holder.ivDesc.setText("" + item.getPhotoDescription());

			Date photoDate = item.getCreatedAt();

			String photoDateString = DateUtils.getDateToString(photoDate
					.getTime());
			holder.ivphotoDate.setText(photoDateString);

			// holder.ivImg.setImageResource(item.getImg());
			// holder.id = item.getId();

			// 如果设置了回调，则设置点击事件
			if (mOnItemClickLitener != null) {
				holder.itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mOnItemClickLitener.onItemClick(position);
					}
				});

				holder.itemView
						.setOnLongClickListener(new OnLongClickListener() {
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

	// public void addData(int position)
	// {
	// mDatas.add(position, "Insert One");
	// mHeights.add( (int) (100 + Math.random() * 300));
	// notifyItemInserted(position);
	// }
	//
	// public void removeData(int position)
	// {
	// mDatas.remove(position);
	// notifyItemRemoved(position);
	// }

	// public void setListViewHeightBasedOnChildren(GridView listView) {
	// ListAdapter listAdapter = listView.getAdapter();
	// if (listAdapter == null) {
	// // pre-condition
	// return;
	// }
	// int totalHeight = 0,count =
	// listAdapter.getCount()%2==0?listAdapter.getCount()/2:listAdapter.getCount()/2+1;
	// for (int i = 0; i < count; i++) {
	// View listItem = listAdapter.getView(i, null, listView);
	// listItem.measure(0, 0);
	// totalHeight += listItem.getMeasuredHeight();
	// }
	// ViewGroup.LayoutParams params = listView.getLayoutParams();
	// params.height = totalHeight;
	// this.gridViewHeightaListener.callBackHeight(totalHeight);
	// listView.setLayoutParams(params);
	// }

	class MyViewHolder extends ViewHolder {
		private RelativeLayout rlAll;
		private ImageView ivImg;
		private TextView ivFavorNumber, ivViewNumber, ivphotoDate, ivDesc;
		int id;

		public MyViewHolder(View view) {
			super(view);

			ivImg = (ImageView) view.findViewById(R.id.mine_img_loading);
			rlAll = (RelativeLayout) view.findViewById(R.id.rl_all);
			ivFavorNumber = (TextView) view
					.findViewById(R.id.mine_favourNumber);
			ivViewNumber = (TextView) view.findViewById(R.id.mine_viewNumber);
			ivphotoDate = (TextView) view
					.findViewById(R.id.mine_date_item_photoWall_tv);
			ivDesc = (TextView) view.findViewById(R.id.desc_item_photo_tv);
		}

		public void setData() {

		}
	}

}