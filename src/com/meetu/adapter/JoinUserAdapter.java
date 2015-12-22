package com.meetu.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.LogUtil.log;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.entity.Huodong;
import com.meetu.entity.User;
import com.meetu.myapplication.MyApplication;
import com.meetu.tools.DensityUtil;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JoinUserAdapter extends BaseAdapter {
	private Context mContext;
	private List<ObjUser> joinUsersList;
	// 网络数据相关
	private FinalBitmap finalBitmap;
	Bitmap loadBitmapIng=null;

	public JoinUserAdapter(Context context, List<ObjUser> joinUsersList) {
		this.mContext = context;
		this.joinUsersList = joinUsersList;
		MyApplication app = (MyApplication) context.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		log.e("zcq", "joinUsersList" + joinUsersList.size());
		loadBitmapIng = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.mine_likelist_profile_default);
	}

	@Override
	public int getCount() {
		log.e("zcq", "joinUsersList" + joinUsersList.size());
		return joinUsersList.size();
	}

	@Override
	public Object getItem(int position) {

		return joinUsersList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final ObjUser item = joinUsersList.get(position);

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_joinusers, null);
			holder.ivImgUrl = (ImageView) convertView
					.findViewById(R.id.userPhoto_joinUsers_img);
			holder.ivSex = (ImageView) convertView
					.findViewById(R.id.sex_joinUsers_img);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.userName_item_joinUsers_tv);
			holder.tvSchool = (TextView) convertView
					.findViewById(R.id.school_item_joinUsers_tv);
			holder.tvWeizhi = (TextView) convertView
					.findViewById(R.id.position_item_joinUser_tv);
			holder.all=(RelativeLayout) convertView.findViewById(R.id.center2_joinUsers_rl);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(item.getProfileClip()!=null){
			finalBitmap.display(holder.ivImgUrl, item.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(mContext, 40),  DensityUtil.dip2px(mContext, 40)),loadBitmapIng);
		}
		holder.all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(mContext,UserPagerActivity.class);
				intent.putExtra("userId", ""+item.getObjectId());
				mContext.startActivity(intent);
			}
		});
		
		holder.tvName.setText("" + item.getNameNick());
		holder.tvSchool.setText("" + item.getSchool());
		holder.tvWeizhi.setText("" + position);
		if (item.getGender() == 2) {
			holder.ivSex.setImageResource(R.drawable.acty_joinlist_img_female);
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView tvName;
		private TextView tvSchool;
		private TextView tvWeizhi;
		private ImageView ivImgUrl;
		private ImageView ivSex;
		private RelativeLayout all;

	}

}
