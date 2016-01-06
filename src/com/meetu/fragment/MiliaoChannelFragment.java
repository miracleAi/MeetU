package com.meetu.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.meetu.activity.miliao.MiLiaoUsersListActivity;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.bean.MemberSeekBean;
import com.meetu.bean.MessageChatBean;
import com.meetu.bean.SeekChatBean;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjListCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.common.Log;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.MemberSeekDao;
import com.meetu.sqlite.UserAboutDao;
import com.meetu.tools.DensityUtil;
import com.meetu.view.ChatViewInterface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MiliaoChannelFragment extends Fragment implements OnClickListener,ChatViewInterface{
	// 控件相关
	private TextView dismissData;
	private ImageView photoManager, backgroud;
	private RelativeLayout loadLayout;
	private TextView titile;
	private TextView numberAll, numberFavor, numberUserAll;
	private View view;
	private ImageView oneUser, twoUser, threeUser, fourUser, fiveUser;

	// 网络数据相关
	private ObjChat objChat = new ObjChat();
	private FinalBitmap finalBitmap=null;
	BitmapUtils bitmaputils;
	private AVIMConversation conv;
	// 会话成员列表
	ArrayList<ObjUser> memberUserList = new ArrayList<ObjUser>();
	//	private UserAboutDao userAboutDao;// 成员缓存
	private MemberSeekDao memberSeekDao;
	AVUser currentUser = ObjUser.getCurrentUser();
	ObjUser user = new ObjUser();
	private ArrayList<UserAboutBean> userAboutBeanList = new ArrayList<UserAboutBean>();

	private SeekChatBean seekChatBean = new SeekChatBean();

	private RelativeLayout numberLayout;

	List<String> list=new ArrayList<String>();//存放用户的id
	Bitmap loadBitmapIng=null;

	private RelativeLayout outmoseLayout;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.fragment_miliao_channel, null);
			if (currentUser != null) {
				user = AVUser.cast(currentUser, ObjUser.class);
			}

			loadBitmapIng=BitmapFactory.decodeResource(getResources(), R.drawable.mine_likelist_profile_default);
			//	userAboutDao = new UserAboutDao(getActivity());
			// objChat=(ObjChat) getArguments().get("ObjChat");
			// log.e("zcq","objChat=="+objChat.getObjectId()+"  objChat.getConversationId=="+objChat.getConversationId());

			seekChatBean = (SeekChatBean) getArguments().get("SeekChatBean");
			memberSeekDao=new MemberSeekDao(getActivity());
			log.e("seekChatBean", seekChatBean.toString());
			MyApplication app = (MyApplication) getActivity()
					.getApplicationContext();
			finalBitmap = app.getFinalBitmap();
			bitmaputils = new BitmapUtils(getActivity());
			bitmaputils.configDiskCacheEnabled(true);
			// conv =
			// MyApplication.chatClient.getConversation(""+seekChatBean.getCreator());
			initView();
			if(seekChatBean.getCreator()!=null){
				getUserInfo("" + seekChatBean.getCreator().getObjectId());
			}
			
			// getMembers(conv);
			setUserInfo();
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub

		dismissData = (TextView) view
				.findViewById(R.id.date_top_miliao_channel_tv);
		photoManager = (ImageView) view
				.findViewById(R.id.photoHead_manage_miliao_channel_img);
		titile = (TextView) view
				.findViewById(R.id.content_center_miliao_channel_tv);
		numberAll = (TextView) view.findViewById(R.id.numberAll_miliao_channel);
		numberFavor = (TextView) view
				.findViewById(R.id.numberFavor_miliao_channel);
		backgroud = (ImageView) view
				.findViewById(R.id.backgroud_miliao_channel_img);
		loadLayout = (RelativeLayout) view.findViewById(R.id.load_layout);
		backgroud.setTag(loadLayout);
		loadLayout.setVisibility(View.VISIBLE);

		// 参加觅聊的人
		oneUser = (ImageView) view.findViewById(R.id.one_photoHead_channel_img);
		twoUser = (ImageView) view.findViewById(R.id.two_photoHead_channel_img);
		threeUser = (ImageView) view
				.findViewById(R.id.three_photoHead_channel_img);
		fourUser = (ImageView) view
				.findViewById(R.id.four_photoHead_channel_img);
		fiveUser = (ImageView) view
				.findViewById(R.id.five_photoHead_channel_img);
		// 觅聊总人数头像
		numberUserAll = (TextView) view
				.findViewById(R.id.number_user_fragment_miliao_channel_tv);

		photoManager.setOnClickListener(this);

		if (seekChatBean.getCreator()!=null&&seekChatBean.getCreator().getProfileClip() != null) {
			log.e("zcq", "设置头像");
			finalBitmap.display(photoManager, seekChatBean.getCreator().getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(getActivity(), 40), DensityUtil.dip2px(getActivity(), 40)),loadBitmapIng);
			photoManager.setOnClickListener(this);
		}
		if (seekChatBean.getPictureUrl() != null) {
			log.e("zcq", "设置背景");
			//finalBitmap.display(backgroud, seekChatBean.getPictureUrl());
			bitmaputils.display(backgroud, seekChatBean.getPictureUrl(), new BitmapLoadCallBack<View>() {

				@Override
				public void onLoadCompleted(View view, String arg1,
						Bitmap bitmap, BitmapDisplayConfig arg3,
						BitmapLoadFrom arg4) {
					// TODO Auto-generated method stub
					((RelativeLayout)view.getTag()).setVisibility(View.GONE);
					((ImageView)view).setImageBitmap(bitmap);
				}

				@Override
				public void onLoadFailed(View arg0, String arg1, Drawable arg2) {
					// TODO Auto-generated method stub

				}
			});
		}
		numberLayout=(RelativeLayout) view.findViewById(R.id.number_user_fragment_miliao_channel_rl);
		numberLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),MiLiaoUsersListActivity.class);
				intent.putExtra("conversationId", seekChatBean.getConversationId());
				startActivity(intent);
			}
		});

		titile.setText("" + seekChatBean.getTitle());
		numberAll.setText("" + seekChatBean.getMembers().size());
		numberFavor.setText("" + seekChatBean.getFolloweeCount());
		numberUserAll.setText("" + seekChatBean.getMembers().size());

		updateTime();

	}
	/**
	 * 更新时间
	 *   
	 * @author lucifer
	 * @date 2016-1-6
	 */
	public void updateTime(){
		int disNumber = (int) ((seekChatBean.getTimeChatStop() - System
				.currentTimeMillis()) / 3600000);
		if (disNumber >= 1) {
			dismissData.setText("" + disNumber + "H");
		} else  if(disNumber>0){
			int minute = (int) ((seekChatBean.getTimeChatStop() - System
					.currentTimeMillis()) / 60000);
			dismissData.setText("" + minute + "M");
		}else{
			dismissData.setText("已");			
		}
	}

	/**
	 * 根据创建者用户id 获取用户相关信息
	 * 
	 * @param objId
	 * @author lucifer
	 * @date 2015-11-17
	 */
	private void getUserInfo(String objId) {
		ObjUserWrap.getObjUser(objId, new ObjUserInfoCallback() {

			@Override
			public void callback(ObjUser user, AVException e) {
				if(e == null){
					if (user.getProfileClip() != null) {
						finalBitmap.display(photoManager, user.getProfileClip().getThumbnailUrl(true
								, DensityUtil.dip2px(getActivity(), DensityUtil.dip2px(getActivity(), 40)), 
								DensityUtil.dip2px(getActivity(), DensityUtil.dip2px(getActivity(), 40))),loadBitmapIng);
					}
				}

			}
		});
	}
	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setUserInfo();	
		updateTime();
	}

	private void getUsersListInfo(final List<String> list) {

		memberUserList.clear();

		for (int i = 0; i < list.size(); i++) {
			ObjUserWrap.getObjUser(list.get(i), new ObjUserInfoCallback() {

				@Override
				public void callback(ObjUser user, AVException e) {
					if (e != null) {
						log.e("zcq", e);
						return;
					} else if (user != null) {
						log.e("zcq", "群员信息获取成功");
						memberUserList.add(user);

						if (memberUserList.size() == list.size()) {
							log.e("zcq", "设置头像");
							if (list.size() >= 5) {
								if (memberUserList.get(4).getProfileClip()!= null) {
									finalBitmap.display(fiveUser,
											memberUserList.get(4)
											.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(getActivity(), 40), DensityUtil.dip2px(getActivity(), 40)),loadBitmapIng);
									fiveUser.setOnClickListener(MiliaoChannelFragment.this);
								}else{
									fiveUser.setImageResource(R.drawable.acty_show_img_profiles);
								}

							}else{
								fiveUser.setImageResource(R.drawable.acty_show_img_profiles);
							}
							if (list.size() >= 4) {
								if (memberUserList.get(3).getProfileClip()
										.getUrl() != null) {
									finalBitmap.display(fourUser,
											memberUserList.get(3)
											.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(getActivity(), 40), DensityUtil.dip2px(getActivity(), 40)),loadBitmapIng);
									fourUser.setOnClickListener(MiliaoChannelFragment.this);
								}else{
									fourUser.setImageResource(R.drawable.acty_show_img_profiles);
								}

							}else{
								fourUser.setImageResource(R.drawable.acty_show_img_profiles);
							}
							if (list.size() >= 3) {
								if (memberUserList.get(2).getProfileClip()
										.getUrl() != null) {
									finalBitmap.display(threeUser,
											memberUserList.get(2)
											.getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(getActivity(), 40), DensityUtil.dip2px(getActivity(), 40)),loadBitmapIng);
									threeUser
									.setOnClickListener(MiliaoChannelFragment.this);
								}else{
									threeUser.setImageResource(R.drawable.acty_show_img_profiles);
								}

							}else{
								threeUser.setImageResource(R.drawable.acty_show_img_profiles);
							}
							if (list.size() >= 2) {
								if (memberUserList.get(1).getProfileClip()
										.getUrl() != null) {
									finalBitmap.display(twoUser, memberUserList
											.get(1).getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(getActivity(), 40), DensityUtil.dip2px(getActivity(), 40)),loadBitmapIng);
									twoUser.setOnClickListener(MiliaoChannelFragment.this);
								}else{
									twoUser.setImageResource(R.drawable.acty_show_img_profiles);
								}

							}else{
								twoUser.setImageResource(R.drawable.acty_show_img_profiles);
							}
							if (list.size() >= 1) {
								if (memberUserList.get(0).getProfileClip()!= null) {
									finalBitmap.display(oneUser, memberUserList
											.get(0).getProfileClip().getThumbnailUrl(true, DensityUtil.dip2px(getActivity(), 40), DensityUtil.dip2px(getActivity(), 40)),loadBitmapIng);
									oneUser.setOnClickListener(MiliaoChannelFragment.this);
								}else{
									oneUser.setImageResource(R.drawable.acty_show_img_profiles);
								}
							}else{
								oneUser.setImageResource(R.drawable.acty_show_img_profiles);
							}
						}

					}

				}
			});
		}

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:

				break;

			default:
				break;
			}
		}

	};

	/**
	 * 设置头像
	 *   
	 * @author lucifer
	 * @date 2015-12-28
	 */
	public void setUserInfo() {

		log.e("zcq", "设置头像");

		list = new ArrayList<String>();

		List<MemberSeekBean> list2=memberSeekDao.queryUserAbout(user.getObjectId(),seekChatBean.getConversationId());

		for(int i=0;i<list2.size();i++){
			list.add(list2.get(i).getMemberSeekId());
		}
		Log.e("list2==",""+list2.size()+ " list=="+list.size());
		getUsersListInfo(list);

		numberUserAll.setText("" + list.size());

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.one_photoHead_channel_img:
			Intent one = new Intent(getActivity(), UserPagerActivity.class);
			one.putExtra("userId", memberUserList.get(0).getObjectId());
			startActivity(one);
			break;
		case R.id.two_photoHead_channel_img:
			Intent two = new Intent(getActivity(), UserPagerActivity.class);
			two.putExtra("userId", memberUserList.get(1).getObjectId());
			startActivity(two);
			break;
		case R.id.three_photoHead_channel_img:
			Intent three = new Intent(getActivity(), UserPagerActivity.class);
			three.putExtra("userId", memberUserList.get(2).getObjectId());
			startActivity(three);
			break;
		case R.id.four_photoHead_channel_img:
			Intent four = new Intent(getActivity(), UserPagerActivity.class);
			four.putExtra("userId", memberUserList.get(3).getObjectId());
			startActivity(four);
			break;
		case R.id.five_photoHead_channel_img:
			Intent five = new Intent(getActivity(), UserPagerActivity.class);
			five.putExtra("userId", memberUserList.get(4).getObjectId());
			startActivity(five);
			break;
		case R.id.photoHead_manage_miliao_channel_img:
			Intent intent = new Intent(getActivity(), UserPagerActivity.class);
			intent.putExtra("userId", seekChatBean.getCreator().getObjectId());
			startActivity(intent);
			break;
		default:
			break;
		}

	}
	//接收到实时消息执行
	@Override
	public void updateView(MessageChatBean bean) {
		// TODO Auto-generated method stub
		int type = bean.getTypeMsg();
		switch (type) {
		case Constants.SHOW_MEMBER_ADD:
		case Constants.SHOW_SELF_ADD:
		case Constants.SHOW_MEMBER_KICK:
		case Constants.SHOW_SELF_KICK:
		case Constants.SHOW_MEMBER_QUIT:
			setUserInfo();
			break;

		default:
			break;
		}
	}
}
