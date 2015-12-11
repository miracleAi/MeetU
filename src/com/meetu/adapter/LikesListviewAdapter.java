package com.meetu.adapter;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.imeetu.R;

import com.lidroid.xutils.BitmapUtils;
import com.meetu.bean.FavorBean;
import com.meetu.cloud.object.ObjUser;
import com.meetu.entity.Huodong;

@SuppressLint("NewApi")
public class LikesListviewAdapter extends BaseAdapter{

	private Context mContext;
	private List<FavorBean> newsList;
	// 网络数据相关
	private BitmapUtils bitmapUtils;

	public LikesListviewAdapter(Context context, List<FavorBean> newsList) {
		this.mContext = context;
		this.newsList = newsList;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.d("lucifer", "getCount()");
		// return newsList.size();
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Log.d("lucifer", "getItem()");
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Log.d("lucifer", "getItemId()");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		FavorBean favor = newsList.get(position);
		ObjUser bean = favor.getUser();
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_likeslist, null);

			holder.avatorImv = (ImageView) convertView
					.findViewById(R.id.userhead_likelist_mine_img);
			holder.nameTv = (TextView) convertView.findViewById(R.id.name_item_tv);
			holder.schoolTv = (TextView) convertView.findViewById(R.id.school_item_tv);
			holder.hisItemLayout = (LinearLayout) convertView.findViewById(R.id.his_layout);
			holder.dataLayout = (RelativeLayout) convertView.findViewById(R.id.data_layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(favor.getPraiseTime() == 0){
			holder.hisItemLayout.setVisibility(View.VISIBLE);
			holder.dataLayout.setVisibility(View.GONE);
		}else{
			holder.hisItemLayout.setVisibility(View.GONE);
			holder.dataLayout.setVisibility(View.VISIBLE);
			bitmapUtils.display(holder.avatorImv, bean.getProfileClip().getUrl());
			holder.nameTv.setText(bean.getNameNick());
			holder.schoolTv.setText(bean.getSchool());
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView avatorImv;
		private TextView nameTv;
		private TextView schoolTv;
		private LinearLayout hisItemLayout;
		private RelativeLayout dataLayout;
	}
}
