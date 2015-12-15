package com.meetu.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVUser;
import com.meetu.activity.miliao.MiLiaoInfoActivity;
import com.meetu.adapter.StaggeredMemoryWallAdapter.MyViewHolder;
import com.meetu.adapter.StaggeredMemoryWallAdapter.OnItemClickCallBack;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.object.ObjUser;
import com.meetu.common.Constants;
import com.meetu.entity.PhotoWall;
import com.meetu.entity.User;
import com.meetu.entity.UserAbout;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.tools.DisplayUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridRecycleMiLiaoInfoAdapter extends
		RecyclerView.Adapter<GridRecycleMiLiaoInfoAdapter.MyViewHolder> {

	private List<UserAbout> list;
	private LayoutInflater mInflater;
	private Context mContext;
	// 网络数据处理
	private FinalBitmap finalBitmap;
	Bitmap loadBitmap;
	
	// 当前用户
		private ObjUser user = new ObjUser();
		AVUser currentUser = AVUser.getCurrentUser();
		List<UserAboutBean> userAboutBeansList=new ArrayList<UserAboutBean>();
		private UserAboutDao userAboutDao;
	/**
	 * 单击 和长按接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnMiLiaoInfoItemClickCallBack {
		void onItemClick(int id);

		void onItemLongClick(View view, int position);
	}

	private OnMiLiaoInfoItemClickCallBack mOnItemClickLitener;

	public void setOnItemClickLitenerBack(
			OnMiLiaoInfoItemClickCallBack mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public GridRecycleMiLiaoInfoAdapter(Context context, List<UserAbout> list) {

		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.mContext = context;
		MyApplication app = (MyApplication) context.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		if (currentUser != null) {
			// 强制类型转换
			user = AVUser.cast(currentUser, ObjUser.class);
		} else {
			return;
		}
		userAboutDao=new UserAboutDao(context);
		loadBitmap=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mine_likelist_profile_default);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, final int position) {
		if (list != null && list.size() > 0) {

			UserAbout item = list.get(position);
			holder.tvName.setText(item.getUserName());
			// holder.ivImg.setImageResource(item.getHeadPhoto());
			if (position == list.size() - 1
					&& item.getUserHeadPhotoUrl() == null) {
				holder.ivImg.setImageResource(item.getDeleteImg());
			} else {
				if (item.getUserHeadPhotoUrl() != null
						|| item.getUserHeadPhotoUrl() != "") {
					finalBitmap.display(holder.ivImg,
							item.getUserHeadPhotoUrl(),loadBitmap);
				}
			}

			if (item.getIsDetele() == false||item.getUserId().equals(user.getObjectId())) {
				holder.ivDetele.setVisibility(View.INVISIBLE);
			} else {
				holder.ivDetele.setVisibility(View.VISIBLE);
			}
			
			
			userAboutBeansList=userAboutDao.queryUserAbout(user.getObjectId(), Constants.FOLLOW_TYPE, "");
			if(userAboutBeansList==null||userAboutBeansList.size()==0){
				holder.ivFavor.setVisibility(View.GONE);
			}else{
				holder.ivFavor.setVisibility(View.GONE);
				for(int i=0;i<userAboutBeansList.size();i++){
					if(userAboutBeansList.get(i).getAboutUserId().equals(item.getUserId())){
						holder.ivFavor.setVisibility(View.VISIBLE);
						break;
					}
				}
			}

			if (mOnItemClickLitener != null) {
				holder.rlAll.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mOnItemClickLitener.onItemClick(position);
					}
				});

				holder.rlAll.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {

						return false;
					}
				});
			}

		}

	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(
				R.layout.item_miliao_info, parent, false));

		return holder;
	}

	class MyViewHolder extends ViewHolder {
		private RelativeLayout rlAll;
		private ImageView ivImg;
		private ImageView ivDetele;
		private ImageView ivFavor;
		private TextView tvName;

		int id;

		public MyViewHolder(View view) {
			super(view);
			ivImg = (ImageView) view
					.findViewById(R.id.photo_item_miliao_info_img);
			tvName = (TextView) view
					.findViewById(R.id.name_item_miliao_info_tv);
			rlAll = (RelativeLayout) view
					.findViewById(R.id.all_item_miliao_info_rl);
			ivDetele = (ImageView) view
					.findViewById(R.id.delete_item_miliao_info_img);
			ivFavor = (ImageView) view
					.findViewById(R.id.favor_item_miliao_info_img);
		}

	}

}
