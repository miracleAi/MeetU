package com.meetu.adapter;

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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class UserPhotoWallAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int TYPE_HEADER = 1;
	private static final int TYPE_ITEM = 0;

	private List<ObjUserPhoto> mPhotos;
	private LayoutInflater mInflater;
	private List<Integer> mHeights;
	private int width;
	private Context mContext;
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

	public UserPhotoWallAdapter(Context context, List<ObjUserPhoto> datas) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.mPhotos = datas;
		width = DisplayUtils.getWindowWidth((Activity) context)
				- DensityUtil.dip2px(context, 28);
		mContext = context;
		bitmapUtils = new BitmapUtils(mContext.getApplicationContext());
		MyApplication app = (MyApplication) context.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mPhotos == null ? 1 : mPhotos.size() + 1;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
		// TODO Auto-generated method stub
		if (mPhotos != null && mPhotos.size() > 0) {
			log.d("mytest", "pos" + pos);
			if (getItemViewType(pos) == TYPE_HEADER) {
				RecyclerHeaderViewHolder holder = (RecyclerHeaderViewHolder) viewHolder;
				StaggeredGridLayoutManager.LayoutParams clp = (StaggeredGridLayoutManager.LayoutParams) holder.headView
						.getLayoutParams();
				if (clp != null) {
					clp.setFullSpan(true);
					clp.width = width;
					clp.height = DensityUtil.dip2px(mContext, 335);
					holder.headView.setLayoutParams(clp);
				}
			} else {
				final int position = pos - 1;
				final RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
				ObjUserPhoto item = mPhotos.get(position);

				log.e("zcq url==", item.getPhoto().getThumbnailUrl(true, item.getImageWidth(), item.getImageHeight(),100,"jpg"));

				if (item.getPhoto() != null) {
					photoUrl = item.getPhoto().getThumbnailUrl(true, item.getImageWidth(), item.getImageHeight(),100,"jpg");
				}

				int photoWidth = item.getImageWidth();
				int photoHight = item.getImageHeight();
				int Hight = (int) ((double) photoWidth / (width / 2) * (photoHight));
				if (photoWidth >= (width / 2)) {
					if (item.getPhoto() != null) {
						finalBitmap.display(holder.ivImg, item.getPhoto()
								.getThumbnailUrl(true, item.getImageWidth(), item.getImageHeight(),100,"jpg"), width / 2, Hight);
					}

				} else {
					// 处理bitmap
					if(item.getPhoto()!=null){
						bitmapUtils.display(holder.ivImg, item.getPhoto().getThumbnailUrl(true, item.getImageWidth(), item.getImageHeight(),100,"jpg"),
								new BitmapLoadCallBack<ImageView>() {

									@Override
									public void onLoadCompleted(
											ImageView container, String uri,
											Bitmap bitmap,
											BitmapDisplayConfig config,
											BitmapLoadFrom from) {

										bitmap = BitmapChange.zoomImg(bitmap,
												width / 2);
										holder.ivImg.setImageBitmap(bitmap);
									}

									@Override
									public void onLoadFailed(ImageView arg0,
											String arg1, Drawable arg2) {
										// TODO Auto-generated method stub

									}
								});

					}
					
				}

				holder.ivFavorNumber.setText("" + item.getPraiseCount());
				holder.ivViewNumber.setText("" + item.getBrowseCount());
				holder.ivDesc.setText("" + item.getPhotoDescription());

				Date photoDate = item.getCreatedAt();

				String photoDateString = DateUtils.getDateToString(photoDate
						.getTime());
				holder.ivphotoDate.setText(photoDateString);
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
									return false;
								}
							});
				}
			}
		}

	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return position == 0 ? TYPE_HEADER : TYPE_ITEM;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup,
			int viewType) {
		Context context = viewGroup.getContext();
		View view;
		if (viewType == TYPE_HEADER) {
			view = LayoutInflater.from(context).inflate(
					R.layout.recycler_header, viewGroup, false);
			return new RecyclerHeaderViewHolder(view);
		} else if (viewType == TYPE_ITEM) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_photowall_fragment, viewGroup, false);
			return new RecyclerItemViewHolder(view);
		}

		throw new RuntimeException("Invalid view type " + viewType);
	}

	private static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
		private RelativeLayout rlAll;
		private ImageView ivImg;
		private TextView ivFavorNumber, ivViewNumber, ivphotoDate, ivDesc;

		public RecyclerItemViewHolder(View view) {
			super(view);
			ivImg = (ImageView) view.findViewById(R.id.mine_img_loading);
			rlAll = (RelativeLayout) view.findViewById(R.id.rl_all_rl);
			ivFavorNumber = (TextView) view
					.findViewById(R.id.mine_favourNumber);
			ivViewNumber = (TextView) view.findViewById(R.id.mine_viewNumber);
			ivphotoDate = (TextView) view
					.findViewById(R.id.mine_date_item_photoWall_tv);
			ivDesc = (TextView) view.findViewById(R.id.desc_item_photo_tv);
		}
	}

	private static class RecyclerHeaderViewHolder extends
			RecyclerView.ViewHolder {
		View headView;

		public RecyclerHeaderViewHolder(View itemView) {
			super(itemView);
			headView = itemView.findViewById(R.id.cv);
		}
	}
}
