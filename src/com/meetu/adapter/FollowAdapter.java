package com.meetu.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.bean.UserBean;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.sqlite.UserDao;
import com.meetu.tools.DensityUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FollowAdapter extends BaseAdapter {
	ArrayList<String> list;
	Context context;
	UserDao userDao;
	// 网络数据相关
	private BitmapUtils bitmapUtils;

	public FollowAdapter(Context context, ArrayList<String> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
		userDao = new UserDao(context);
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		String userId = list.get(position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_joinusers, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.nameTv = (TextView) convertView
				.findViewById(R.id.userName_item_joinUsers_tv);
		holder.avatorImv = (ImageView) convertView
				.findViewById(R.id.userPhoto_joinUsers_img);
		holder.sexImv = (ImageView) convertView
				.findViewById(R.id.sex_joinUsers_img);
		holder.schoolTv = (TextView) convertView
				.findViewById(R.id.school_item_joinUsers_tv);
		holder.positionTv = (TextView) convertView
				.findViewById(R.id.position_item_joinUser_tv);
		holder.positionTagTv = (TextView) convertView
				.findViewById(R.id.item_join_user_tag);
		holder.arrowImv = (ImageView) convertView
				.findViewById(R.id.item_right_arrow);

		holder.positionTv.setVisibility(View.GONE);
		holder.positionTagTv.setVisibility(View.GONE);
		holder.arrowImv.setVisibility(View.VISIBLE);
		ArrayList<UserBean> beanList = userDao.queryUser(userId);
		if (beanList != null && beanList.size() > 0) {
			UserBean bean = beanList.get(0);
			bitmapUtils.display(holder.avatorImv, bean.getProfileClip());
			holder.nameTv.setText(bean.getNameNick());
			holder.schoolTv.setText(bean.getSchool());
			if (bean.getGender() == 2) {
				holder.sexImv
						.setImageResource(R.drawable.acty_joinlist_img_female);
			} else {
				holder.sexImv
						.setImageResource(R.drawable.acty_joinlist_img_male);
			}
		} else {
			ObjUserWrap.getObjUser(userId, new ObjUserInfoCallback() {

				@Override
				public void callback(ObjUser user, AVException e) {
					// TODO Auto-generated method stub
					if (e == null) {
						userDao.insertOrReplaceUser(user);
						if (user.getProfileClip() != null) {
							bitmapUtils.display(holder.avatorImv, user
									.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(context, 40), DensityUtil.dip2px(context, 40)));							
						}
						holder.nameTv.setText(user.getNameNick());
						holder.schoolTv.setText(user.getSchool());
						if (user.getGender() == 2) {
							holder.sexImv
									.setImageResource(R.drawable.acty_joinlist_img_female);
						} else {
							holder.sexImv
									.setImageResource(R.drawable.acty_joinlist_img_male);
						}
					}
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		private TextView nameTv;
		private TextView schoolTv;
		private TextView positionTv;
		private TextView positionTagTv;
		private ImageView avatorImv;
		private ImageView sexImv;
		private ImageView arrowImv;
	}
}
