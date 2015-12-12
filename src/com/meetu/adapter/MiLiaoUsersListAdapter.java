package com.meetu.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;



import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.bean.UserAboutBean;
import com.meetu.bean.UserBean;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.entity.Huodong;
import com.meetu.entity.User;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserDao;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MiLiaoUsersListAdapter extends BaseAdapter {
	private Context mContext;
	private List<UserAboutBean> usersList;
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser userMy = new ObjUser();
	
	FinalBitmap finalBitmap;
	UserDao userDao;

	public MiLiaoUsersListAdapter(Context context, List<UserAboutBean> usersList) {
		this.mContext = context;
		this.usersList = usersList;
		if(currentUser!=null){
			userMy = AVUser.cast(currentUser, ObjUser.class);
		}
		MyApplication application=(MyApplication) context.getApplicationContext();
		finalBitmap=application.getFinalBitmap();
		userDao=new UserDao(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return usersList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return usersList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder ;
		final UserAboutBean item = usersList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_miliao_userslist, null);
			holder.ivImgUrl = (ImageView) convertView
					.findViewById(R.id.userPhoto_miliao_usersList_img);
			holder.ivSex = (ImageView) convertView
					.findViewById(R.id.sex_miliao_usersList_img);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.userName_item_miliao_usersList_tv);
			holder.tvSchool = (TextView) convertView
					.findViewById(R.id.school_item_miliao_usersList_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.ivImgUrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(mContext,UserPagerActivity.class);
				intent.putExtra("userId", item.getAboutUserId());
				mContext.startActivity(intent);
			}
		});
		if (item.getAboutUserId() != null && !("").equals(item.getAboutUserId())) {
			log.e("zcq id", "userId==" + userMy.getObjectId()
					+ " itemClientId==" + item.getAboutUserId());
			if (userMy.getObjectId().equals(item.getAboutUserId())) {
				holder.tvName.setText("" + userMy.getNameNick());
				holder.tvSchool.setText(userMy.getSchool());
				// log.e("zcq", "是我自己发的消息");
				if (userMy.getProfileClip() != null) {
					// log.e("zcq", "是我自己发的消息");
					finalBitmap.display(holder.ivImgUrl, userMy
							.getProfileClip().getUrl());
				}
				if(userMy.getGender()==2){
					holder.ivSex.setImageResource(R.drawable.acty_joinlist_img_female);
				}
			} else {

				log.e("zcq", "别人发的消息");

				ArrayList<UserBean> list = userDao.queryUser(item
						.getAboutUserId());
				if (null != list && list.size() > 0) {

					if (!list.get(0).getProfileClip().equals("")) {
						finalBitmap.display(holder.ivImgUrl,
								list.get(0).getProfileClip());
					}
					holder.tvName.setText("" + list.get(0).getNameNick());
					holder.tvSchool.setText(list.get(0).getSchool());
					if(list.get(0).getGender()==2){
						holder.ivSex.setImageResource(R.drawable.acty_joinlist_img_female);
					}
				} else {
					ObjUserWrap.getObjUser(item.getAboutUserId(),
							new ObjUserInfoCallback() {

						@Override
						public void callback(ObjUser objuser,
								AVException e) {
							// TODO Auto-generated method stub
							if (e == null) {
								userDao.insertOrReplaceUser(objuser);
								ArrayList<UserBean> list2 = userDao
										.queryUser(item
												.getAboutUserId());
								if (null != list2
										&& list2.size() > 0) {
									if (!list2.get(0)
											.getProfileClip()
											.equals("")) {
										finalBitmap
										.display(
												holder.ivImgUrl,
												list2.get(0)
												.getProfileClip());
									}
									holder.tvName.setText(""
											+ list2.get(0)
											.getNameNick());
									holder.tvSchool.setText(list2.get(0).getSchool());
									if(list2.get(0).getGender()==2){
										holder.ivSex.setImageResource(R.drawable.acty_joinlist_img_female);
									}

								}
							}
						}
					});
				}
			}
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView tvName;
		private TextView tvSchool;

		private ImageView ivImgUrl;
		private ImageView ivSex;

	}

}
