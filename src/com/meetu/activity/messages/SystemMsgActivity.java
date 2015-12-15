package com.meetu.activity.messages;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.R.layout;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.activity.homepage.HomePageDetialActivity;
import com.meetu.activity.mine.FavorListActivity;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.bean.ActivityBean;
import com.meetu.cloud.callback.ObjSysMsgListCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjSysMsg;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.utils.DateUtils;
import com.meetu.cloud.wrap.ObjSysMsgWrap;
import com.meetu.common.Constants;
import com.meetu.common.SharepreferencesUtils;
import com.meetu.sqlite.ActivityDao;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SystemMsgActivity extends Activity implements
		OnRefreshListener2<ListView> {
	private ImageView backImv;
	private PullToRefreshListView systemLv;
	ArrayList<ObjSysMsg> msgList = new ArrayList<ObjSysMsg>();
	SysAdapter sysAdapter;
	ObjUser user;
	BitmapUtils bitmapUtils;
	private ActivityDao activityDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.system_msg_layout);
		user = AVUser.cast(AVUser.getCurrentUser(), ObjUser.class);
		bitmapUtils = new BitmapUtils(getApplicationContext());
		activityDao = new ActivityDao(getApplicationContext());
		ArrayList<ObjSysMsg> list =  (ArrayList<ObjSysMsg>) getIntent().getSerializableExtra("sysmsg_list");
		initView();
		if(list!= null){
			msgList.clear();
			msgList.addAll(list);
			sysAdapter.notifyDataSetChanged();
		}
		PreferenceManager.getDefaultSharedPreferences(SystemMsgActivity.this).edit()
		.putLong(SharepreferencesUtils.SYS_MSG_SCAN, System.currentTimeMillis()).commit();
	}

	private void initView() {
		// TODO Auto-generated method stub
		backImv = (ImageView) findViewById(R.id.back_system_msg_img);
		systemLv = (PullToRefreshListView) findViewById(R.id.system_msg_lv);
		systemLv.setMode(Mode.PULL_FROM_START);
		systemLv.setOnRefreshListener(this);
		sysAdapter = new SysAdapter();
		systemLv.setAdapter(sysAdapter);

		backImv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		systemLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// position -1
				ObjSysMsg msg = msgList.get(position - 1);
				switch (msg.getMsgType()) {
				case Constants.SysMsgTypeText:

					break;
				case Constants.SysMsgTypeActy:
					Intent actyIntent = new Intent(SystemMsgActivity.this,
							HomePageDetialActivity.class);
					ArrayList<ActivityBean> list = activityDao.queryActyBean(
							user.getObjectId(), msgList.get(position - 1)
									.getActy().getObjectId());
					if (list != null && list.size() > 0) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("activityBean", list.get(0));
						actyIntent.putExtras(bundle);
						startActivity(actyIntent);
					}
					break;
				case Constants.SysMsgTypeFollow:
					Intent followIntent = new Intent(SystemMsgActivity.this,
							UserPagerActivity.class);
					followIntent.putExtra("userId", msg.getTowardsUser()
							.getObjectId());
					startActivity(followIntent);
					break;
				case Constants.SysMsgTypeUserPhoto:
					Intent photoIntent = new Intent(SystemMsgActivity.this,
							ShowSysMsgPhotoActivity.class);
					if (msg.getUserPhoto().getPhoto() != null) {
						photoIntent.putExtra("photoUrl", msg.getUserPhoto()
								.getPhoto().getUrl());
						startActivity(photoIntent);
					}
					break;
				default:
					break;
				}
			}
		});

	}

	private void loadSysMsg() {
		ObjSysMsgWrap.querySysMsgs(user, new ObjSysMsgListCallback() {

			@Override
			public void callback(List<ObjSysMsg> objects, AVException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					msgList.clear();
					msgList.addAll(objects);
					sysAdapter.notifyDataSetChanged();
				} else {
					// 查询出错
				}
				systemLv.onRefreshComplete();
			}
		});

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		loadSysMsg();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	private int mMaxItemWidth;// item最大宽度
	private int mMinItemWidth;// item最小宽度

	class SysAdapter extends BaseAdapter {

		public SysAdapter() {
			// TODO Auto-generated constructor stub
			mMaxItemWidth = DisplayUtils
					.getWindowWidth(getApplicationContext())
					- DensityUtil.dip2px(getApplicationContext(), 110);
			mMinItemWidth = DensityUtil.dip2px(getApplicationContext(), 44);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return msgList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return msgList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ObjSysMsg bean = msgList.get(position);
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.item_sys_msg_layout, null);
				holder.textContent = (TextView) convertView
						.findViewById(R.id.content_chat_item_left_tv);
				holder.textAvator = (ImageView) convertView
						.findViewById(R.id.userHead_chat_item_left_img);
				holder.textTime = (TextView) convertView
						.findViewById(R.id.time_chat_item_left_tv);
				holder.textName = (TextView) convertView
						.findViewById(R.id.userName_chat_item_left_tv);
				holder.timeTv = (TextView) convertView
						.findViewById(R.id.time_chat_item_newjoin_tv);
				holder.msgTitleTv = (TextView) convertView
						.findViewById(R.id.msg_title_tv);
				holder.nameTv = (TextView) convertView
						.findViewById(R.id.name_chat_item_newjoin_tv);
				holder.contentTv = (TextView) convertView
						.findViewById(R.id.content_chat_item_newjoin_tv);
				holder.msgImv = (ImageView) convertView
						.findViewById(R.id.msg_photo_img);
				holder.schoolIconImv = (ImageView) convertView
						.findViewById(R.id.school_icon_imv);
				holder.sexImv = (ImageView) convertView
						.findViewById(R.id.sex_icon_imv);
				holder.textLayout = (RelativeLayout) convertView
						.findViewById(R.id.text_layout);
				holder.cardLayout = (LinearLayout) convertView
						.findViewById(R.id.card_layout);
				if (bean.getMsgType() == Constants.SysMsgTypeText) {
					// 文本消息
					holder.textLayout.setVisibility(View.VISIBLE);
					holder.cardLayout.setVisibility(View.GONE);
				} else {
					// 卡片消息
					holder.textLayout.setVisibility(View.GONE);
					holder.cardLayout.setVisibility(View.VISIBLE);
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			switch (bean.getMsgType()) {
			case Constants.SysMsgTypeText:
				holder.textContent.setMaxWidth(mMaxItemWidth);
				holder.textContent.setMinWidth(mMinItemWidth);
				holder.textContent.setText(bean.getContent());
				holder.textName.setText("小U");
				holder.textAvator
						.setImageResource(R.drawable.acty_barrage_img_comment_profiles_default);
				holder.textTime.setText(DateUtils.format(bean.getCreatedAt()
						.getTime(), DateUtils.DateFormat_YearTime));
				break;
			case Constants.SysMsgTypeFollow:
				holder.schoolIconImv.setVisibility(View.VISIBLE);
				holder.sexImv.setVisibility(View.VISIBLE);
				ObjUser followUser = AVUser.cast(bean.getAVUser("towardsUser"),
						ObjUser.class);
				holder.nameTv.setText(followUser.getNameNick());
				holder.contentTv.setText(followUser.getSchool());
				holder.timeTv.setText(DateUtils.format(bean.getCreatedAt()
						.getTime(), DateUtils.DateFormat_YearTime));
				holder.msgTitleTv.setText("hi, " + user.getNameNick() + ", 你和"
						+ followUser.getNameNick() + "已相互关注");
				if (user.getProfileClip() != null) {
					bitmapUtils.display(holder.msgImv, followUser
							.getProfileClip().getUrl());
				} else {
					holder.msgImv
							.setImageResource(R.drawable.acty_barrage_img_comment_profiles_default);
				}
				if (followUser.getGender() == 2) {
					holder.sexImv
							.setImageResource(R.drawable.acty_joinlist_img_female);
				} else {
					holder.sexImv
							.setImageResource(R.drawable.acty_joinlist_img_male);
				}
				break;
			case Constants.SysMsgTypeActy:
				holder.schoolIconImv.setVisibility(View.GONE);
				holder.sexImv.setVisibility(View.GONE);
				try {
					ObjActivity acty = bean.getAVObject("acty",
							ObjActivity.class);
					ObjUser actyUser = AVUser.cast(
							bean.getAVUser("towardsUser"), ObjUser.class);
					holder.nameTv.setText(acty.getTitle());
					holder.contentTv.setText(acty.getTitleSub());
					holder.msgTitleTv.setText("hi, " + user.getNameNick()
							+ ", 你关注的" + actyUser.getNameNick() + "参加了"
							+ acty.getTitle() + "活动");
					holder.timeTv.setText(DateUtils.format(bean.getCreatedAt()
							.getTime(), DateUtils.DateFormat_YearTime));
					if (acty.getActivityCover() != null) {
						bitmapUtils.display(holder.msgImv, acty
								.getActivityCover().getUrl());
					} else {
						holder.msgImv
								.setImageResource(R.drawable.acty_barrage_img_comment_profiles_default);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Constants.SysMsgTypeUserPhoto:
				holder.schoolIconImv.setVisibility(View.GONE);
				holder.sexImv.setVisibility(View.GONE);
				try {
					ObjUserPhoto photo = bean.getAVObject("userPhoto",
							ObjUserPhoto.class);
					ObjUser favorUser = AVUser.cast(
							bean.getAVUser("towardsUser"), ObjUser.class);
					holder.msgTitleTv.setText("hi, " + user.getNameNick()
							+ ", " + favorUser.getNameNick() + "为你的照片送上一个赞");
					holder.timeTv.setText(DateUtils.format(bean.getCreatedAt()
							.getTime(), DateUtils.DateFormat_YearTime));
					if (photo.getPhoto() != null) {
						bitmapUtils.display(holder.msgImv, photo.getPhoto()
								.getUrl());
					} else {
						holder.msgImv
								.setImageResource(R.drawable.acty_barrage_img_comment_profiles_default);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			return convertView;
		}

		class ViewHolder {
			TextView textContent;
			TextView textTime;
			ImageView textAvator;
			TextView textName;
			TextView msgTitleTv;
			TextView contentTv;
			TextView nameTv;
			TextView timeTv;
			ImageView msgImv;
			ImageView schoolIconImv;
			ImageView sexImv;
			RelativeLayout textLayout;
			LinearLayout cardLayout;
		}
	}
}
