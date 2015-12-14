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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.activity.mine.UserPagerActivity;
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
	private List<Chatmsgs> chatmsgsList;
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

	public ChatmsgsListViewAdapter(Context context, List<Chatmsgs> chatmsgsList,Handler handler) {
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
		userDao = new UserDao(context);
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
				.getChatMsgType() : -1;
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
		Log.d("lucifer", "getCount()");
		// return newsList.size();
		return chatmsgsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Log.d("lucifer", "getItem()");
		return chatmsgsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Log.d("lucifer", "getItemId()");
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		final Chatmsgs item = chatmsgsList.get(position);

		if (convertView == null) {
			holder = new ViewHolder();
			log.e("zcq", "item.getChatMsgType()==" + item.getChatMsgType());
			log.e("zcq", "item.getClientId()==" + item.getClientId());
			log.e("zcq", "item.getContent()==" + item.getContent());
			switch (item.getChatMsgType()) {


			case Constants.SHOW_SEND_TEXT:
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
				break;
				//TODO 1 暂时没有此类型 未知原因出现此类型 待解决

			case Constants.SHOW_RECV_TEXT:
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
				break;
			case Constants.SHOW_SEND_IMG:
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
				holder.resentLayout = (RelativeLayout) convertView
						.findViewById(R.id.fail_chat_item_photo_right_rl);
				break;
			case Constants.SHOW_RECV_IMG:
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
				holder.resentLayout = (RelativeLayout) convertView
						.findViewById(R.id.fail_chat_item_photo_left_rl);
				break;

			case Constants.SHOW_MEMBERCHANGE:
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
				break;
			case Constants.SHOW_SELF_DEL:
			case Constants.SHOW_SELF_CHANGE:
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.chat_my_join_or_exit, null);
				holder.time = (TextView) convertView
						.findViewById(R.id.time_my_join_or_exit_remind_tv);
				holder.content = (TextView) convertView
						.findViewById(R.id.content_my_join_or_exit_remind_tv);

			default:
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		switch (item.getChatMsgType()) {

		case Constants.SHOW_SEND_TEXT:
		case Constants.SHOW_RECV_TEXT:
			if(item.getChatMsgStatus() == Constants.STATUES_FAILED){
				holder.resentLayout.setVisibility(View.VISIBLE);
			}else{
				holder.resentLayout.setVisibility(View.GONE);
			}
			holder.resentLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = 3;
					msg.obj = item;
					handler.sendMessage(msg);
				}
			});
			SpannableString spannableString = ChatGroupActivity
					.getExpressionString(mContext, item.getContent());
			holder.content.setMaxWidth(mMaxItemWidth);
			holder.content.setMinWidth(mMinItemWidth);
			holder.content.setText(spannableString);
			if (item.getIsShowTime() == 1) {
				long time = Long.parseLong(item.getSendTimeStamp());
				Date date = new Date(time);
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
					intent.putExtra("userId", item.getClientId());
					mContext.startActivity(intent);
				}
			});

			if (item.getClientId() != null && !("").equals(item.getClientId())) {
				log.e("zcq id", "userId==" + user.getObjectId()
						+ " itemClientId==" + item.getClientId());
				if (user.getObjectId().equals(item.getClientId())) {
					holder.userName.setText("" + user.getNameNick());
					// log.e("zcq", "是我自己发的消息");
					if (user.getProfileClip() != null) {
						// log.e("zcq", "是我自己发的消息");
						finalBitmap.display(holder.userHeadPhoto, user
								.getProfileClip().getUrl());
					}
				} else {

					log.e("zcq", "别人发的消息");

					ArrayList<UserBean> list = userDao.queryUser(item
							.getClientId());
					if (null != list && list.size() > 0) {

						if (!list.get(0).getProfileClip().equals("")) {
							finalBitmap.display(holder.userHeadPhoto,
									list.get(0).getProfileClip());
						}
						holder.userName.setText("" + list.get(0).getNameNick());

					} else {
						ObjUserWrap.getObjUser(item.getClientId(),
								new ObjUserInfoCallback() {

							@Override
							public void callback(ObjUser objuser,
									AVException e) {
								// TODO Auto-generated method stub
								if (e == null) {
									userDao.insertOrReplaceUser(objuser);
									ArrayList<UserBean> list2 = userDao
											.queryUser(item
													.getClientId());
									if (null != list2
											&& list2.size() > 0) {
										if (!list2.get(0)
												.getProfileClip()
												.equals("")) {
											finalBitmap
											.display(
													holder.userHeadPhoto,
													list2.get(0)
													.getProfileClip());
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

		case Constants.SHOW_SEND_IMG:
			if(item.getChatMsgStatus() == Constants.STATUES_FAILED){
				holder.resentLayout.setVisibility(View.VISIBLE);
			}else{
				holder.resentLayout.setVisibility(View.GONE);
			}
			holder.resentLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = 4;
					msg.obj = item;
					handler.sendMessage(msg);
				}
			});
			if (item.getImgMsgImageUrl() != null
					&& !item.getImgMsgImageUrl().equals("")) {
				finalBitmap.display(holder.photo, item.getImgMsgImageUrl());
			}

			if (item.getIsShowTime() == 1) {
				long time = Long.parseLong(item.getSendTimeStamp());
				Date date = new Date(time);
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
					intent.putExtra("userId", item.getClientId());
					mContext.startActivity(intent);
				}
			});

			if (user.getObjectId().equals(item.getClientId())) {
				holder.userName.setText("" + user.getNameNick());
				if (user.getProfileClip() != null) {
					finalBitmap.display(holder.userHeadPhoto, user
							.getProfileClip().getUrl());
				}


			}

			break;
		case Constants.SHOW_RECV_IMG:
			if(item.getChatMsgStatus() == Constants.STATUES_FAILED){
				holder.resentLayout.setVisibility(View.VISIBLE);
			}else{
				holder.resentLayout.setVisibility(View.GONE);
			}
			if (item.getImgMsgImageUrl() != null
					&& !item.getImgMsgImageUrl().equals("")) {
				finalBitmap.display(holder.photo, item.getImgMsgImageUrl());
			}

			if (item.getIsShowTime() == 1) {
				long time = Long.parseLong(item.getSendTimeStamp());
				Date date = new Date(time);
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
					intent.putExtra("userId", item.getClientId());
					mContext.startActivity(intent);
				}
			});
			if (item.getClientId() != null && !("").equals(item.getClientId())) {
				ArrayList<UserBean> list = userDao
						.queryUser(item.getClientId());
				if (null != list && list.size() > 0) {

					if (!list.get(0).getProfileClip().equals("")) {
						finalBitmap.display(holder.userHeadPhoto, list.get(0)
								.getProfileClip());
					}
					holder.userName.setText("" + list.get(0).getNameNick());

				} else {
					ObjUserWrap.getObjUser(item.getNowJoinUserId(),
							new ObjUserInfoCallback() {

						@Override
						public void callback(ObjUser objuser,
								AVException e) {
							// TODO Auto-generated method stub
							if (e == null) {
								userDao.insertOrReplaceUser(objuser);
								ArrayList<UserBean> list2 = userDao
										.queryUser(item.getClientId());
								if (null != list2 && list2.size() > 0) {
									if (!list2.get(0).getProfileClip()
											.equals("")) {
										finalBitmap
										.display(
												holder.userHeadPhoto,
												list2.get(0)
												.getProfileClip());
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

		case Constants.SHOW_MEMBERCHANGE:
			// TODO 设置他人头像 需要封装个方法。多处使用
			try {

				long time = Long.parseLong(item.getSendTimeStamp());
				holder.time.setText(com.meetu.cloud.utils.DateUtils
						.getFormattedTimeInterval(time));

				if (item.getNowJoinUserId() != null
						&& !("").equals(item.getNowJoinUserId())) {
					
					holder.userHeadPhoto.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(mContext,UserPagerActivity.class);
							intent.putExtra("userId", item.getNowJoinUserId());
							mContext.startActivity(intent);
							
						}
					});
					ArrayList<UserBean> list = userDao.queryUser(item
							.getNowJoinUserId());
					if (null != list && list.size() > 0) {

						if (!list.get(0).getProfileClip().equals("")) {
							finalBitmap.display(holder.userHeadPhoto,
									list.get(0).getProfileClip());
						}
						holder.userName.setText("" + list.get(0).getNameNick());
						holder.userSchool.setText("" + list.get(0).getSchool());
					} else {
						ObjUserWrap.getObjUser(item.getNowJoinUserId(),
								new ObjUserInfoCallback() {

							@Override
							public void callback(ObjUser objuser,
									AVException e) {
								// TODO Auto-generated method stub
								if (e == null) {
									userDao.insertOrReplaceUser(objuser);
									ArrayList<UserBean> list2 = userDao.queryUser(item
											.getNowJoinUserId());
									if (null != list2
											&& list2.size() > 0) {
										if (!list2.get(0)
												.getProfileClip()
												.equals("")) {
											finalBitmap
											.display(
													holder.userHeadPhoto,
													list2.get(0)
													.getProfileClip());
										}
										holder.userName.setText(""
												+ list2.get(0)
												.getNameNick());
										holder.userSchool.setText(""
												+ list2.get(0)
												.getSchool());

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
		case Constants.SHOW_SELF_CHANGE:
			// 自己 加入和退出的状态提醒
			log.e("zcq 14", "自己加入消息显示");
			holder.content.setText("" + item.getContent());
			holder.time.setText(""
					+ com.meetu.cloud.utils.DateUtils
					.getFormattedTimeInterval(Long.valueOf(item
							.getSendTimeStamp())));
			break;

		}

		Log.d("lucifer", "getView()");

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
		private RelativeLayout resentLayout;
	}

}
