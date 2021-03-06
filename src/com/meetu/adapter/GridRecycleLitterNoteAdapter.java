package com.meetu.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.baidu.a.a.a.c;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjScripBox;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.entity.LitterNotes;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.tools.DensityUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

public class GridRecycleLitterNoteAdapter extends
		RecyclerView.Adapter<GridRecycleLitterNoteAdapter.MyViewHolder> {

	private List<ObjScripBox> list;
	private LayoutInflater mInflater;
	private Context mContext;

	private FinalBitmap finalBitmap;

	private List<ObjUser> objUsers = new ArrayList<ObjUser>();
	private AVUser currentUser = AVUser.getCurrentUser();
	// 当前用户
	private ObjUser user = new ObjUser();
	String userId = "";// 对方的id
	UserAboutDao userAboutDao;
	List<UserAboutBean> userAboutBeansList=new ArrayList<UserAboutBean>();
	Bitmap loadBitmap;
	/**
	 * 单击 和长按接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnLitterNotesItemClickCallBack {
		void onItemClick(int id);

		void onItemLongClick(View view, int position);
	}

	private OnLitterNotesItemClickCallBack mOnItemClickLitener;

	public void setOnItemClickLitenerBack(
			OnLitterNotesItemClickCallBack mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public GridRecycleLitterNoteAdapter(Context context, List<ObjScripBox> list) {

		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.mContext = context;
		MyApplication app = (MyApplication) context.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		if (currentUser != null) {
			user = AVUser.cast(currentUser, ObjUser.class);
		} else {
			return;
		}
		userAboutDao=new UserAboutDao(context);
		userAboutBeansList=userAboutDao.queryUserAbout(user.getObjectId(), Constants.FOLLOW_TYPE, "");
		loadBitmap=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mine_likelist_profile_default);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, final int position) {
		if (list != null && list.size() > 0) {

			final ObjScripBox item = list.get(position);
			// holder.tvName.setText(item.getName());

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
			objUsers = new ArrayList<ObjUser>();

			if (item.getUsers() != null) {

				objUsers.addAll(item.getUsers());

				for (ObjUser objUser : objUsers) {
					if (!user.getObjectId().equals(objUser.getObjectId())) {
						userId = objUser.getObjectId();
					}
				}
			}
			if (!userId.equals("")) {
				getUserInfo(userId, holder);
			}

			if (item.getSender().getObjectId().equals(user.getObjectId())) {
				// 我是创建者
				holder.ivNoReadNumber.setText("" + item.getSenderUnreadCount());
				
				
				holder.ivImg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						objUsers = new ArrayList<ObjUser>();
						String	otheruserId="";
						if (item.getUsers() != null) {

							objUsers.addAll(item.getUsers());

							for (ObjUser objUser : objUsers) {
								if (!user.getObjectId().equals(objUser.getObjectId())) {
								otheruserId = objUser.getObjectId();
								}
							}
						}
						Intent intent=new Intent(mContext,UserPagerActivity.class);
						intent.putExtra("userId", otheruserId);
						mContext.startActivity(intent);
					}
				});
			} else {
				holder.ivNoReadNumber.setText(""
						+ item.getReceiverUnreadCount());
				holder.ivImg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						objUsers = new ArrayList<ObjUser>();
						String	otheruserId="";
						if (item.getUsers() != null) {

							objUsers.addAll(item.getUsers());

							for (ObjUser objUser : objUsers) {
								if (!user.getObjectId().equals(objUser.getObjectId())) {
								otheruserId = objUser.getObjectId();
								}
							}
						}
						
						Intent intent=new Intent(mContext,UserPagerActivity.class);
						intent.putExtra("userId", item.getSender().getObjectId());
						mContext.startActivity(intent);
					}
				});
			}
			
			
			
			if(userAboutBeansList==null||userAboutBeansList.size()==0){
				holder.ivFavor.setVisibility(View.GONE);
			}else{
				for(int i=0;i<userAboutBeansList.size();i++){
					if(userAboutBeansList.get(i).getAboutUserId().equals(userId)){
						holder.ivFavor.setVisibility(View.VISIBLE);
						break;
					}else{
						holder.ivFavor.setVisibility(View.GONE);
					}
				}
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
				R.layout.item_litter_note, parent, false));

		return holder;
	}

	class MyViewHolder extends ViewHolder {
		private RelativeLayout rlAll;
		private ImageView ivImg;
		private ImageView ivDetele;
		private ImageView ivFavor;
		private TextView tvName, tvSchool;
		private ImageView sexViewImg;
		private TextView ivNoReadNumber;

		int id;

		public MyViewHolder(View view) {
			super(view);
			ivImg = (ImageView) view
					.findViewById(R.id.photo_item_note_litter_img);
			tvName = (TextView) view.findViewById(R.id.user_name_item_note_tv);
			rlAll = (RelativeLayout) view
					.findViewById(R.id.all_item_litter_note_rl);
			// ivDetele=(ImageView)
			// view.findViewById(R.id.delete_item_miliao_info_img);
			ivFavor = (ImageView) view
					.findViewById(R.id.favor_item_note_info_img);
			tvSchool = (TextView) view.findViewById(R.id.school_item_note_tv);
			sexViewImg = (ImageView) view
					.findViewById(R.id.sex_view_color_img_item_note);
			ivNoReadNumber = (TextView) view
					.findViewById(R.id.number_noread_item_note_tv);
		}

	}

	/**
	 * 根据创建者用户id 获取用户相关信息
	 * 
	 * @param objId
	 * @author lucifer
	 * @date 2015-11-17
	 */
	private void getUserInfo(String objId, final MyViewHolder holder) {
		ObjUserWrap.getObjUser(objId, new ObjUserInfoCallback() {

			@Override
			public void callback(ObjUser user, AVException e) {
				
				if(e!=null){
					log.e("zcq", e);
					return;
				}

				if (user.getProfileClip() != null) {
					finalBitmap.display(holder.ivImg, user.getProfileClip()
							.getThumbnailUrl(true, DensityUtil.dip2px(mContext, 40), DensityUtil.dip2px(mContext, 40)),loadBitmap);
				}
				holder.tvName.setText(user.getNameNick());
				if (user.getGender() == 2) {

					// // 根据性别设置图片
					// Drawable nav_up = getResources().getDrawable(
					// R.drawable.acty_joinlist_img_male);
					// nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
					// nav_up.getMinimumHeight());
					// userName.setCompoundDrawables(null, null, nav_up, null);
					holder.sexViewImg
							.setImageResource(R.drawable.massage_letters_img_line_girl);
				}
				holder.tvSchool.setText("" + user.getSchool());

			}
		});
	}

}
