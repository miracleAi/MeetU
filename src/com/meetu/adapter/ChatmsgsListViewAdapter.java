package com.meetu.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import android.R.string;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.bean.MessageChatBean;
import com.meetu.bean.UserBean;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;
import com.meetu.entity.Huodong;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserDao;
import com.meetu.tools.BitmapCut;
import com.meetu.tools.DateUtils;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

@SuppressLint("NewApi")
public class ChatmsgsListViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<MessageChatBean> chatmsgsList;
	private SimpleDateFormat sd;

	private int mMaxItemWidth;// item最大宽度
	private int mMinItemWidth;// item最小宽度

	// TODO 暂时只测试4种状态 ,但是要进一步判断消息发送的方向
	private final int TYPE_COUNT = 20;
	private UserDao userDao;
	// 拿本地的 user
	private AVUser currentUser = AVUser.getCurrentUser();
	Handler handler;
	// 网络相关
	private ObjUser user = null;
	FinalBitmap finalBitmap;
	BitmapUtils bitmapUtils=null;
	Bitmap loadBitmap;
	int photoW,photoH;

	public ChatmsgsListViewAdapter(Context context, List<MessageChatBean> chatmsgsList,Handler handler) {
		this.mContext = context;
		this.chatmsgsList = chatmsgsList;
		this.handler = handler;

		mMaxItemWidth = DisplayUtils.getWindowWidth(mContext)
				- DensityUtil.dip2px(mContext, 110);
		mMinItemWidth = DensityUtil.dip2px(mContext, 44);

		if (currentUser != null) {
			user = AVUser.cast(currentUser, ObjUser.class);
		}

		MyApplication app = (MyApplication) context.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		bitmapUtils=new BitmapUtils(context.getApplicationContext());
		userDao = new UserDao(context);
		loadBitmap=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mine_likelist_profile_default);
	}

	/**
	 * 消息状态 style 10 我发的文本消息 12接收的文本消息 11 我发的图片的消息 13接收的图片消息 3新人加入消息 4普通通知消息
	 * 活动咨询反馈通知消息  消息发送方向  14 自己加入和踢出提醒
	 * 
	 */
	@Override
	public int getItemViewType(int position) {

		// TODO Auto-generated method stub
		return chatmsgsList != null ? chatmsgsList.get(position)
				.getTypeMsg() : -1;
	}

	/**
	 * 
	 */
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_COUNT;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return newsList.size();
		return chatmsgsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return chatmsgsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		final MessageChatBean item = chatmsgsList.get(position);
		com.meetu.common.Log.e("chatListsize", "size"+chatmsgsList.size());
		com.meetu.common.Log.e("getTypeMsg", ""+item.getTypeMsg());
		if (convertView == null) {
			holder = new ViewHolder();
			switch (item.getTypeMsg()) {
			case Constants.SHOW_SEND_TYPE_TEXT:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.chat_item_text_right, null);
				holder.content = (TextView) convertView
						.findViewById(R.id.content_chat_item_right_tv);
				holder.time = (TextView) convertView
						.findViewById(R.id.time_chat_item_right_tv);
				holder.timeLayout = (RelativeLayout) convertView
						.findViewById(R.id.time_chat_item_right_rl);
				holder.userHeadPhoto = (ImageView) convertView
						.findViewById(R.id.userHead_chat_item_right_img);
				holder.userName = (TextView) convertView
						.findViewById(R.id.userName_chat_item_tv);
				holder.failPhoto = (ImageView) convertView
						.findViewById(R.id.fail_chat_item_text_img);
				holder.resentLayout = (RelativeLayout) convertView
						.findViewById(R.id.fail_chat_item_text_right_rl);
				holder.progressMsg = (ProgressBar) convertView.findViewById(R.id.progressBar_msg);
				break;
				//TODO 1 暂时没有此类型 未知原因出现此类型 待解决

			case Constants.SHOW_RECEIVE_TYPE_TEXT:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.chat_item_text_left, null);
				holder.content = (TextView) convertView
						.findViewById(R.id.content_chat_item_left_tv);
				holder.time = (TextView) convertView
						.findViewById(R.id.time_chat_item_left_tv);
				holder.timeLayout = (RelativeLayout) convertView
						.findViewById(R.id.time_chat_item_left_rl);

				holder.userHeadPhoto = (ImageView) convertView
						.findViewById(R.id.userHead_chat_item_left_img);
				holder.userName = (TextView) convertView
						.findViewById(R.id.userName_chat_item_left_tv);
				holder.failPhoto = (ImageView) convertView
						.findViewById(R.id.fail_chat_item_text_left);
				holder.resentLayout = (RelativeLayout) convertView
						.findViewById(R.id.fail_chat_item_text_right_rl);
				holder.progressMsg = (ProgressBar) convertView.findViewById(R.id.progressBar_msg);
				break;
			case Constants.SHOW_SEND_TYPE_IMG:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.chat_item_photo_right, null);
				holder.photo = (ImageView) convertView
						.findViewById(R.id.photo_chat_item_right_img);
				holder.time = (TextView) convertView
						.findViewById(R.id.time_photochat_item_right_tv);
				holder.timeLayout = (RelativeLayout) convertView
						.findViewById(R.id.time_photochat_item_right_rl);
				holder.userHeadPhoto = (ImageView) convertView
						.findViewById(R.id.userHead_chat_item_photo_right_img);
				holder.userName = (TextView) convertView
						.findViewById(R.id.userName_chat_item_photo_right_tv);
				holder.failPhoto = (ImageView) convertView
						.findViewById(R.id.fail_chat_item_text_photo__right_img);
				holder.resentLayout = (RelativeLayout) convertView
						.findViewById(R.id.fail_chat_item_photo_right_rl);
				holder.progressMsg = (ProgressBar) convertView.findViewById(R.id.progressBar_msg);
				break;
			case Constants.SHOW_RECEIVE_TYPE_IMG:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.chat_item_photo_left, null);
				holder.photo = (ImageView) convertView
						.findViewById(R.id.photo_chat_item_left_img);
				holder.time = (TextView) convertView
						.findViewById(R.id.time_photochat_item_left_tv);
				holder.timeLayout = (RelativeLayout) convertView
						.findViewById(R.id.time_photochat_item_left_rl);

				holder.userHeadPhoto = (ImageView) convertView
						.findViewById(R.id.userHead_chat_item_photo_left_img);
				holder.userName = (TextView) convertView
						.findViewById(R.id.userNamechat_item_photo_left_tv);
				holder.failPhoto = (ImageView) convertView
						.findViewById(R.id.fail_chat_item_text_photo__left_img);
				holder.resentLayout = (RelativeLayout) convertView
						.findViewById(R.id.fail_chat_item_photo_left_rl);
				holder.progressMsg = (ProgressBar) convertView.findViewById(R.id.progressBar_msg);
			//	holder.leftPhotoUp=(ImageView) convertView.findViewById(R.id.photo_up_chat_item_left_img);
				
				break;

			case Constants.SHOW_MEMBER_ADD:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.chat_item_newjoin_remind, null);
				holder.userHeadPhoto = (ImageView) convertView
						.findViewById(R.id.msg_photo_img);
				holder.userSchool = (TextView) convertView
						.findViewById(R.id.content_chat_item_newjoin_tv);
				holder.userName = (TextView) convertView
						.findViewById(R.id.name_chat_item_newjoin_tv);
				holder.sexImg = (ImageView) convertView
						.findViewById(R.id.sex_icon_imv);
				holder.time = (TextView) convertView
						.findViewById(R.id.time_chat_item_newjoin_tv);
				holder.allItem=(LinearLayout) convertView.findViewById(R.id.all_card_item_newjoin_ll);
				break;
			case Constants.SHOW_SELF_ADD:
			case Constants.SHOW_SELF_KICK:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.chat_my_join_or_exit, null);
				holder.time = (TextView) convertView
						.findViewById(R.id.time_my_join_or_exit_remind_tv);
				holder.content = (TextView) convertView
						.findViewById(R.id.content_my_join_or_exit_remind_tv);
				break;
			default:
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		switch (item.getTypeMsg()) {
		case Constants.SHOW_SEND_TYPE_TEXT:
		case Constants.SHOW_RECEIVE_TYPE_TEXT:
			if(item.getStatusMsg() == Constants.STATUES_FAILED){
				holder.failPhoto.setVisibility(View.VISIBLE);
				holder.progressMsg.setVisibility(View.GONE);
				holder.resentLayout.setVisibility(View.VISIBLE);
			}else{
				holder.resentLayout.setVisibility(View.GONE);
			}
			holder.resentLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					holder.failPhoto.setVisibility(View.GONE);
					holder.progressMsg.setVisibility(View.VISIBLE);
					Message msg = new Message();
					msg.what = 3;
					msg.obj = item;
					handler.sendMessage(msg);
				}
			});
			SpannableString spannableString = ChatGroupActivity
					.getExpressionString(mContext, item.getMsgText());
			holder.content.setMaxWidth(mMaxItemWidth);
			holder.content.setMinWidth(mMinItemWidth);
			holder.content.setText(spannableString);
			if (item.getIsShowTime() == 1) {
				Date date = new Date(item.getSendTimeStamp());
				sd = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
				String string = sd.format(date);
				holder.time.setText(string);
				holder.timeLayout.setVisibility(View.VISIBLE);
			}
			if (item.getIsShowTime() == 0) {
				holder.timeLayout.setVisibility(View.GONE);
			}
			holder.userHeadPhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext,
							UserPagerActivity.class);
					intent.putExtra("userId", item.getIdClient());
					mContext.startActivity(intent);
				}
			});

			if (item.getIdClient() != null && !("").equals(item.getIdClient())) {
				if (user.getObjectId().equals(item.getIdClient())) {
					holder.userName.setText("" + user.getNameNick());
					// log.e("zcq", "是我自己发的消息");
					if (user.getProfileClip() != null) {
						// log.e("zcq", "是我自己发的消息");
						finalBitmap.display(holder.userHeadPhoto, user
								.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(mContext, 40), DensityUtil.dip2px(mContext, 40)),loadBitmap);
					}
				} else {

					ArrayList<UserBean> list = userDao.queryUser(item
							.getIdClient());
					if (null != list && list.size() > 0) {

						if (!list.get(0).getProfileClip().equals("")) {
							finalBitmap.display(holder.userHeadPhoto,
									list.get(0).getProfileClip(),loadBitmap);
						}
						holder.userName.setText("" + list.get(0).getNameNick());

					} else {
						ObjUserWrap.getObjUser(item.getIdClient(),
								new ObjUserInfoCallback() {

							@Override
							public void callback(ObjUser objuser,
									AVException e) {
								// TODO Auto-generated method stub
								if (e == null) {
									userDao.insertOrReplaceUser(objuser);
									ArrayList<UserBean> list2 = userDao
											.queryUser(item
													.getIdClient());
									if (null != list2
											&& list2.size() > 0) {
										if (!list2.get(0)
												.getProfileClip()
												.equals("")) {
											finalBitmap
											.display(
													holder.userHeadPhoto,
													list2.get(0)
													.getProfileClip(),loadBitmap);
										}
										holder.userName.setText(""
												+ list2.get(0)
												.getNameNick());

									}
								}
							}
						});
					}
				}
			}
			break;

		case Constants.SHOW_SEND_TYPE_IMG:
			if(item.getStatusMsg() == Constants.STATUES_FAILED){
				holder.failPhoto.setVisibility(View.VISIBLE);
				holder.progressMsg.setVisibility(View.GONE);
				holder.resentLayout.setVisibility(View.VISIBLE);
			}else{
				holder.resentLayout.setVisibility(View.GONE);
			}
			holder.resentLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					holder.failPhoto.setVisibility(View.GONE);
					holder.progressMsg.setVisibility(View.VISIBLE);
					Message msg = new Message();
					msg.what = 4;
					msg.obj = item;
					handler.sendMessage(msg);
				}
			});
			if (item.getFileUrl() != null
					&& !item.getFileUrl().equals("")) {
			//	finalBitmap.display(holder.photo, item.getImgMsgImageUrl());
				bitmapUtils.display(holder.photo, item.getFileUrl(), new BitmapLoadCallBack<ImageView>() {

					@Override
					public void onLoadCompleted(ImageView imageView, String arg1,
							Bitmap bitmap, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						
						bitmap=BitmapCut.toRoundCorner(bitmap, DensityUtil.dip2px(mContext, 10));
						
						holder.photo.setImageBitmap(bitmap);
					}

					@Override
					public void onLoadFailed(ImageView arg0, String arg1,
							Drawable arg2) {
						// TODO Auto-generated method stub
						
					}
				});
			}

			if (item.getIsShowTime() == 1) {
				Date date = new Date(item.getSendTimeStamp());
				sd = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
				String string = sd.format(date);
				holder.time.setText(string);
				holder.timeLayout.setVisibility(View.VISIBLE);
			}
			if (item.getIsShowTime() == 0) {
				holder.timeLayout.setVisibility(View.GONE);
			}

			holder.userHeadPhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext,
							UserPagerActivity.class);
					intent.putExtra("userId", item.getIdClient());
					mContext.startActivity(intent);
				}
			});

			if (user.getObjectId().equals(item.getIdClient())) {
				holder.userName.setText("" + user.getNameNick());
				if (user.getProfileClip() != null) {
					finalBitmap.display(holder.userHeadPhoto, user
							.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(mContext, 40), DensityUtil.dip2px(mContext, 40)),loadBitmap);
				}


			}

			break;
		case Constants.SHOW_RECEIVE_TYPE_IMG:
			if(item.getStatusMsg() == Constants.STATUES_FAILED){
				holder.resentLayout.setVisibility(View.VISIBLE);
			}else{
				holder.resentLayout.setVisibility(View.GONE);
			}
			if (item.getFileUrl() != null
					&& !item.getFileUrl().equals("")) {
				bitmapUtils.display(holder.photo, item.getFileUrl(), new BitmapLoadCallBack<ImageView>() {

					@Override
					public void onLoadCompleted(ImageView imageView, String arg1,
							Bitmap bitmap, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						
						bitmap=BitmapCut.toRoundCorner(bitmap, DensityUtil.dip2px(mContext, 10));
						
						holder.photo.setImageBitmap(bitmap);
					}

					@Override
					public void onLoadFailed(ImageView arg0, String arg1,
							Drawable arg2) {
						// TODO Auto-generated method stub
						
					}
				});
	//			finalBitmap.display(holder.photo, item.getImgMsgImageUrl());
	
	//计算图片 比例 大小
	/*			if(item.getImgMsgImageWidth()>item.getImgMsgImageHeight()){
					if(item.getImgMsgImageWidth()<DensityUtil.dip2px(mContext, 160)){
						photoW=item.getImgMsgImageWidth();
						photoH=item.getImgMsgImageHeight();
					}else{
						photoW=DensityUtil.dip2px(mContext, 160);
						photoH=(int)((double)item.getImgMsgImageHeight()/((double)((double)item.getImgMsgImageWidth()/(double)photoW)));
					}
					
				}else{
					if(item.getImgMsgImageHeight()<DensityUtil.dip2px(mContext, 160)){
						photoW=item.getImgMsgImageWidth();
						photoH=item.getImgMsgImageHeight();
					}else{
						photoH=DensityUtil.dip2px(mContext, 160);
						photoW=(int)((double)item.getImgMsgImageWidth()/((double)((double)item.getImgMsgImageHeight()/(double)photoH)));
					}
					
				}*/
			}
			
//			RelativeLayout.LayoutParams params= (LayoutParams) holder.leftPhotoUp.getLayoutParams();
//			params.width=photoW;
//			params.height=photoH;
//			holder.leftPhotoUp.setLayoutParams(params);
//			log.e("image wh==", "photoW=="+photoW+"photoH=="+photoH+"w=="+item.getImgMsgImageWidth()+" h=="+item.getImgMsgImageHeight());
//			finalBitmap.display(holder.photo, item.getImgMsgImageUrl(), photoW, photoH);

			if (item.getIsShowTime() == 1) {
				Date date = new Date(item.getSendTimeStamp());
				sd = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
				String string = sd.format(date);
				// log.e(""+date+", "+sd.format(date)+", time=="+item.getSendTimeStamp());
				holder.time.setText(string);
				holder.timeLayout.setVisibility(View.VISIBLE);
			}
			if (item.getIsShowTime() == 0) {
				holder.timeLayout.setVisibility(View.GONE);
			}

			holder.userHeadPhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext,
							UserPagerActivity.class);
					intent.putExtra("userId", item.getIdClient());
					mContext.startActivity(intent);
				}
			});
			if (item.getIdClient() != null && !("").equals(item.getIdClient())) {
				ArrayList<UserBean> list = userDao
						.queryUser(item.getIdClient());
				if (null != list && list.size() > 0) {

					if (!list.get(0).getProfileClip().equals("")) {
						finalBitmap.display(holder.userHeadPhoto, list.get(0)
								.getProfileClip(),loadBitmap);
					}
					holder.userName.setText("" + list.get(0).getNameNick());

				} else {
					ObjUserWrap.getObjUser(item.getIdClient(),
							new ObjUserInfoCallback() {

						@Override
						public void callback(ObjUser objuser,
								AVException e) {
							// TODO Auto-generated method stub
							if (e == null) {
								userDao.insertOrReplaceUser(objuser);
								ArrayList<UserBean> list2 = userDao
										.queryUser(item.getIdClient());
								if (null != list2 && list2.size() > 0) {
									if (!list2.get(0).getProfileClip()
											.equals("")) {
										finalBitmap
										.display(
												holder.userHeadPhoto,
												list2.get(0)
												.getProfileClip(),loadBitmap);
									}
									holder.userName.setText(""
											+ list2.get(0)
											.getNameNick());

								}
							}
						}
					});
				}
			}

			break;

		case Constants.SHOW_MEMBER_ADD:
			// TODO 设置他人头像 需要封装个方法。多处使用
			try {

				holder.time.setText(com.meetu.cloud.utils.DateUtils
						.getFormattedTimeInterval(item.getSendTimeStamp()));

				if (item.getIdOperated() != null
						&& !("").equals(item.getIdOperated())) {
					
					holder.allItem.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(mContext,UserPagerActivity.class);
							intent.putExtra("userId", item.getIdOperated());
							mContext.startActivity(intent);
							
						}
					});
					
					
					ArrayList<UserBean> list = userDao.queryUser(item
							.getIdOperated());
					if (null != list && list.size() > 0) {

						if (!list.get(0).getProfileClip().equals("")) {
							finalBitmap.display(holder.userHeadPhoto,
									list.get(0).getProfileClip(),loadBitmap);
						}
						holder.userName.setText("" + list.get(0).getNameNick());
						holder.userSchool.setText("" + list.get(0).getSchool());
						if(list.get(0).getGender()==2){
							holder.sexImg.setImageResource(R.drawable.acty_joinlist_img_female);
						}
					} else {
						ObjUserWrap.getObjUser(item.getIdOperated(),
								new ObjUserInfoCallback() {

							@Override
							public void callback(ObjUser objuser,
									AVException e) {
								// TODO Auto-generated method stub
								if (e == null) {
									userDao.insertOrReplaceUser(objuser);
									ArrayList<UserBean> list2 = userDao.queryUser(item
											.getIdOperated());
									if (null != list2
											&& list2.size() > 0) {
										if (!list2.get(0)
												.getProfileClip()
												.equals("")) {
											finalBitmap
											.display(
													holder.userHeadPhoto,
													list2.get(0)
													.getProfileClip(),loadBitmap);
										}
										holder.userName.setText(""
												+ list2.get(0)
												.getNameNick());
										holder.userSchool.setText(""
												+ list2.get(0)
												.getSchool());
										if(list2.get(0).getGender()==2){
											holder.sexImg.setImageResource(R.drawable.acty_joinlist_img_female);
										}

									}
								}
							}
						});
					}
				}
			} catch (Exception e) {
				log.e("zcq 新人加入异常", e);
			}

			break;
		case Constants.SHOW_SELF_KICK:
		case Constants.SHOW_SELF_ADD:
			// 自己 加入和退出的状态提醒
			holder.content.setText("" + item.getMsgText());
			holder.time.setText(""
					+ com.meetu.cloud.utils.DateUtils
					.getFormattedTimeInterval(Long.valueOf(item
							.getSendTimeStamp())));
			break;

		}


		return convertView;
	}

	private class ViewHolder {
		private TextView content;
		private ImageView photo;
		private ImageView sexImg;
		private TextView time;
		private RelativeLayout timeLayout;
		private TextView userSchool;
		private ImageView userHeadPhoto;
		private TextView userName;
		private ImageView failPhoto;
		private ProgressBar progressMsg;
		private RelativeLayout resentLayout;
		private ImageView leftPhotoUp;
		private LinearLayout allItem;
	}

}
