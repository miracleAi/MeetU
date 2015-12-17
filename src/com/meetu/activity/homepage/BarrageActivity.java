package com.meetu.activity.homepage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.activity.mine.UserPagerActivity;
import com.meetu.bean.ActivityBean;
import com.meetu.bean.BarrageMsgBean;
import com.meetu.bean.UserBean;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjCoversationCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.utils.DateUtils;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.common.PerfectInformation;
import com.meetu.entity.Barrage;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.UserDao;
import com.meetu.tools.DenUtil;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class BarrageActivity extends Activity {
	// 放置弹幕的父布局
	private AbsoluteLayout barrarylayout;
	private int windowWidth;
	private float x;
	private Barrage myBarrage;
	int number = 0, topHight;
	MyRunnable myRunnable = null;
	Handler handler = null;
	AVIMClient uClient = AVIMClient.getInstance("5630843900b0ec3f9c9d2e03");
	// 弹幕显示用列表，默认，历史，实时
	ArrayList<BarrageMsgBean> defList = new ArrayList<BarrageMsgBean>();
	ArrayList<BarrageMsgBean> hisList = new ArrayList<BarrageMsgBean>();
	ArrayList<BarrageMsgBean> realTimeList = new ArrayList<BarrageMsgBean>();
	// 默认列表内容
	String[] defContents = { "弹幕，是一场盛大的文字直播", "活动未开始，弹幕便是活动群聊广场",
			"活动进行时，弹幕化身现场直播解说员", "你在看弹幕，发弹幕的人也在看你", "弹幕直播将在3s后开启","活动群聊的互动内容会呈现在弹幕里呢",
			"点击弹幕，就可以查看弹幕详情","点击头像，就可以逮到弹幕的人","弹幕直播开启啦  开启啦","让我们的欢乐，感染你的生活" };
	// 默认列表额外内容
	String[] defExtras = { "2015.11.05", "2015.05.21", "2016.01.01","2015.12.15", "2016.02.14",
			"2015.12.15","2016.01.01","2015.05.21","2015.11.05", "2016.02.14" };
	int defIndex = 0;
	int hisIndex = 0;
	// 5623af6560b2ce30d24a2c67 测试用
	String conversationId = "";
	// 55e2b23a60b291d784a12ccb 测试用
	String activityId = "";
	ObjUser user = null;
	long lastMsgTime = 0;
	MessageHandler msgHandler = null;
	// 父组件的高度
	private int validHeightSpace;
	// 控件相关
	private ImageView headPhoto,barrageBack;
	private TextView uName, uContent, topTitle, timeTv;
	private RelativeLayout back, userJoinList;
	// 底部布局
	RelativeLayout bottomRl;
	TextView bottomTv;
	ImageView bottomImv;
	private int viewHight;// 动态生成的view的高度 。考虑到上下间距。要稍大点
	private BarrageMsgBean bottomBean = null;
	private BottomRunnable bottomRunnable = null;
	// 网络 数据相关
	private BitmapUtils bitmapUtils;
	private UserDao userDao;
	// 标记是否参加活动
	private boolean isJoined = false;
	private ActivityBean actyBean = new ActivityBean();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_barrage);
		actyBean = (ActivityBean) getIntent().getExtras().getSerializable(
				"activityBean");
		activityId = actyBean.getActyId();
		conversationId = actyBean.getConversationId();
		user = AVUser.cast(AVUser.getCurrentUser(), ObjUser.class);
		myRunnable = new MyRunnable();
		handler = new Handler();
		msgHandler = new MessageHandler();
		bottomRunnable = new BottomRunnable();
		windowWidth = DisplayUtils.getWindowWidth(BarrageActivity.this);
		validHeightSpace = DensityUtil.dip2px(this, 350);
		bitmapUtils = new BitmapUtils(getApplicationContext());
		userDao = new UserDao(getApplicationContext());
		initView();
		initChange();
		isOrder();
		initDefData();
		// 启动弹幕
		handler.post(myRunnable);
		handler.post(bottomRunnable);
	}

	private void initView() {
		barrarylayout = (AbsoluteLayout) super
				.findViewById(R.id.top_barrage_rl);
		headPhoto = (ImageView) super
				.findViewById(R.id.userHeadPhone_barrage_img);
		uName = (TextView) super.findViewById(R.id.userName_barrage_tv);
		uContent = (TextView) super
				.findViewById(R.id.content_bottom_barrage_tv);
		timeTv = (TextView) findViewById(R.id.time_tv);

		topTitle = (TextView) super.findViewById(R.id.title_top_barrary_tv);
		back = (RelativeLayout) super.findViewById(R.id.back_barrage_rl);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		userJoinList = (RelativeLayout) super
				.findViewById(R.id.userjoin_barrary_rl);

		bottomRl = (RelativeLayout) findViewById(R.id.bottom_bottom_barrage_rl);
		bottomTv = (TextView) findViewById(R.id.bottom_tv);
		bottomImv = (ImageView) findViewById(R.id.bottom_arrow);
		barrageBack = (ImageView) findViewById(R.id.back_barrage_img);

		topTitle.setText(actyBean.getTitle());
		barrageBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initChange() {
		viewHight = DensityUtil.dip2px(this, 70);// 动态生成的view的高度 。考虑到上下间距。稍大点
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,
				msgHandler);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,
				msgHandler);
		ObjChatMessage.logOutChat(uClient, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					return;
				}
				if (result) {
					// 小U退出聊天登录成功
				} else {
					// 小U退出聊天登录成功失败
				}
			}
		});
		// 停止弹幕
		handler.removeCallbacks(myRunnable);
		handler.removeCallbacks(bottomRunnable);
		super.onDestroy();
	}

	public void initBottom() {
		if (actyBean.getStatus() == Constants.ActyStatusOver) {
			bottomRl.setBackgroundColor(getResources().getColor(
					R.color.barrage_acty_end_bg));
			bottomImv.setVisibility(View.GONE);
			bottomTv.setText("活动已结束");
		} else {
			if (isJoined) {
				bottomRl.setBackgroundColor(getResources().getColor(
						R.color.barrage_conv_join_bg));
				bottomImv.setVisibility(View.VISIBLE);
				bottomTv.setText("进入群聊");
			} else {
				bottomRl.setBackgroundColor(getResources().getColor(
						R.color.barrage_acty_join_bg));
				bottomImv.setVisibility(View.VISIBLE);
				bottomTv.setText("报名活动");
			}
		}
		bottomRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.d("mytest", "click");
				if (actyBean.getStatus() == Constants.ActyStatusOver) {
					return;
				}
				if (!isJoined) {
					switch (actyBean.getStatus()) {
					case Constants.ActyStatusProcess:
						Toast.makeText(getApplicationContext(), "活动进行中，报名已结束",
								1000).show();
						break;
					case Constants.ActyStatusSignUPOver:
						Toast.makeText(getApplicationContext(), "报名已结束", 1000)
						.show();
						break;
					case Constants.ActyStatusSignUp:

						if(user.isCompleteUserInfo()){
							Intent intent = new Intent(BarrageActivity.this,
									JoinActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("activityBean", actyBean);
							intent.putExtras(bundle);
							startActivity(intent);
						}else{
							PerfectInformation.showDiolagPerfertInformation(BarrageActivity.this, "亲爱的 只有完善个人信息后才能报名");
						}

						break;
					default:
						break;
					}
				} else {
					Intent intent = new Intent(BarrageActivity.this,
							ChatGroupActivity.class);
					intent.putExtra("ConversationStyle", "1");
					intent.putExtra("ConversationId", actyBean.getConversationId());
					intent.putExtra("title", actyBean.getTitle());
					log.d("mytest", "time over ==== "+actyBean.getTimeChatStop());
					intent.putExtra("TimeOver",String.valueOf(actyBean.getTimeChatStop()));
					startActivity(intent);
				}
			}
		});
	}

	// 判断用户是否报名
	private void isOrder() {
		// TODO Auto-generated method stub
		ObjActivity activity = null;
		try {
			activity = ObjActivity.createWithoutData(ObjActivity.class,
					activityId);
			ObjActivityWrap.queryUserJoin(activity, user,
					new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if (e != null) {
						return;
					}
					if (result) {
						isJoined = true;
						isChatLogin();
					} else {
						isJoined = false;
						uLigin();
					}
					initBottom();
				}
			});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 检查本人客户端是否登录
	private void isChatLogin() {
		// TODO Auto-generated method stub
		ObjChatMessage.getClientStatus(MyApplication.chatClient,
				new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					return;
				}
				if (result) {
					// 本人聊天登录
					getHisList(MyApplication.chatClient);
				} else {
					// 本人聊天未登录
					chatLogin();
				}
			}
		});
	}

	// 本人客户端登录
	public void chatLogin() {
		MyApplication.chatClient = AVIMClient.getInstance(AVUser
				.getCurrentUser().getObjectId());
		ObjChatMessage.connectToChatServer(MyApplication.chatClient,
				new ObjAvimclientCallback() {

			@Override
			public void callback(AVIMClient client, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					return;
				}
				MyApplication.chatClient = client;
				// 本人聊天登录成功
			}
		});
	}

	/**
	 * 创建一个新的view布局
	 */
	@SuppressLint("ResourceAsColor")
	private void initAdd(final BarrageMsgBean barrage) {
		// 发送者用户信息判断及查询
		if (barrage.getUserId() != null && !("").equals(barrage.getUserId())) {
			ArrayList<UserBean> list = userDao.queryUser(barrage.getUserId());
			if (null != list && list.size() > 0) {
				barrage.setNickName(list.get(0).getNameNick());
				barrage.setUserAvator(list.get(0).getProfileClip());
			} else {
				if (null != barrage.getNickName()
						&& ("小U").equals(barrage.getNickName())) {

				} else {
					barrage.setNickName("");
				}
				barrage.setUserAvator("");
				ObjUserWrap.getObjUser(barrage.getUserId(),
						new ObjUserInfoCallback() {

					@Override
					public void callback(ObjUser objuser, AVException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							userDao.insertOrReplaceUser(objuser);
						}
					}
				});
			}
		}
		setBottomBean(barrage);
		topHight = (number % (validHeightSpace / viewHight)) * viewHight
				+ viewHight;
		number++;
		LinearLayout ll = new LinearLayout(this);
		// 设置RelativeLayout布局的宽高
		@SuppressWarnings("deprecation")
		AbsoluteLayout.LayoutParams relLayoutParams = new AbsoluteLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, windowWidth, topHight);
		// 弹幕头像
		ImageView imageView = new ImageView(this);
		LinearLayout.LayoutParams imageLp = new LinearLayout.LayoutParams(
				DensityUtil.dip2px(getApplicationContext(), 44),
				DensityUtil.dip2px(getApplicationContext(), 44));
		imageView.setId(1);
		if(barrage.getNickName().equals("小U")){
			imageView.setImageResource(R.drawable.img_profiles_u);
		}else if (null != barrage.getUserAvator()
				&& !("").equals(barrage.getUserAvator())) {
			bitmapUtils.display(imageView, barrage.getUserAvator());
		} else {
			imageView
			.setImageResource(R.drawable.mine_likelist_profile_default);
		}
		// 唯一标记,与tv一致
		imageView.setTag(barrage.getId());
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(barrage.getUserId() != null && !("").equals(barrage.getUserId())){
					Intent intent = new Intent(BarrageActivity.this, UserPagerActivity.class);
					intent.putExtra("userId",barrage.getUserId());
					startActivity(intent);
				}
			}
		});
		ll.addView(imageView, imageLp);
		LinearLayout.LayoutParams textLp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		textLp.setMargins(DensityUtil.dip2px(this, 8),
				DensityUtil.dip2px(this, 5), 0, DensityUtil.dip2px(this, 5));
		TextView tv = new TextView(this);
		tv.setText(barrage.getContent());
		tv.setGravity(Gravity.CENTER);
		tv.setBackgroundResource(R.drawable.barrage_text_bg);
		// 动态设置的padding一定要放在设置背景后面才能起作用
		tv.setPadding(24, 10, 24, 10);
		tv.setClickable(true);
		tv.setTextSize(13);
		tv.setTag(barrage.getId());
		if (null != barrage.getNickName()
				&& !("").equals(barrage.getNickName())) {
			tv.setText(barrage.getNickName() + ":" + barrage.getContent());
		} else {
			tv.setText(barrage.getContent());
		}
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.removeCallbacks(bottomRunnable);
				initBottom(barrage, true);
			}
		});
		tv.setId(2);
		ll.addView(tv, textLp);
		ll.setLayoutParams(relLayoutParams);
		barrarylayout.addView(ll);
		MyDanMu(tv.getText().toString(), tv, imageView, ll);
	}

	/**
	 * 给生成的view设置属性位移动画
	 * 
	 * @param title
	 * @param textview
	 * @param imageView
	 * @param relativeLayout
	 */
	@SuppressLint("NewApi")
	public void MyDanMu(String title, final TextView textview,
			final ImageView imageView, final LinearLayout relativeLayout) {
		int width = DisplayUtils.getWindowWidth(BarrageActivity.this);
		int strwidth = DenUtil.dp2px(BarrageActivity.this,
				DenUtil.GetTextWidth(title, 40));
		x = (float) width + strwidth;
		ValueAnimator animator = ValueAnimator.ofFloat(0, -strwidth - width);
		animator.setTarget(textview);
		animator.setDuration(8000).start();
		animator.setInterpolator(new LinearInterpolator());
		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {

				barrarylayout.removeView(relativeLayout);
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});

		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				relativeLayout.setTranslationX((Float) animation
						.getAnimatedValue());
			}

		});

	}

	class BottomRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			initBottom(getBottomBean(), false);
		}

	}

	public void setBottomBean(BarrageMsgBean bean) {
		this.bottomBean = bean;
	}

	public BarrageMsgBean getBottomBean() {
		return bottomBean;
	}

	public void initBottom(final BarrageMsgBean bean, boolean isClick) {
		if(bean.getNickName().equals("小U")){
			headPhoto
			.setImageResource(R.drawable.img_profiles_u);
		}if (null != bean.getUserAvator() && !("").equals(bean.getUserAvator())) {
			bitmapUtils.display(headPhoto, bean.getUserAvator());
		} else {
			headPhoto
			.setImageResource(R.drawable.img_profiles_u);
		}
		if (null != bean.getNickName() && !("").equals(bean.getNickName())) {
			uName.setText(bean.getNickName());
		} else {
			uName.setText("");
		}
		timeTv.setText(bean.getTime());
		uContent.setText(bean.getContent());
		headPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(bean.getUserId() != null && !("").equals(bean.getUserId())){
					Intent intent = new Intent(BarrageActivity.this, UserPagerActivity.class);
					intent.putExtra("userId",bean.getUserId());
					startActivity(intent);
				}
			}
		});
		if (isClick) {
			handler.postDelayed(bottomRunnable, 8000);
		} else {
			handler.postDelayed(bottomRunnable, 4000);
		}
	}

	class MyRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			initAdd(getMsg());
			handler.postDelayed(myRunnable, 1000);
		}
	}

	// 取得弹幕显示信息
	public BarrageMsgBean getMsg() {
		BarrageMsgBean msgBean = new BarrageMsgBean();
		// 优先显示实时消息
		if (realTimeList.size() > 0) {
			msgBean = realTimeList.get(0);
			lastMsgTime = System.currentTimeMillis();
			msgBean.setId(String.valueOf(lastMsgTime));
			realTimeList.remove(msgBean);
			hisList.add(msgBean);
			return msgBean;
		}
		// 没有实时消息，默认消息播放超过15秒，播放历史消息
		if ((System.currentTimeMillis() - lastMsgTime) > (15 * 1000)
				&& hisList.size() > 0) {
			msgBean = hisList.get(getHisIndex());
			msgBean.setId(String.valueOf(System.currentTimeMillis()));
			hisIndex++;
			return msgBean;
		}
		// 没有实时消息，穿插默认消息
		msgBean = defList.get(getDefIndex());
		msgBean.setId(String.valueOf(System.currentTimeMillis()));
		defIndex++;
		return msgBean;
	}

	public int getHisIndex() {
		if (hisIndex < 0 || hisIndex > (hisList.size() - 1)) {
			hisIndex = 0;
		}
		return hisIndex;
	}

	public int getDefIndex() {
		if (defIndex < 0 || defIndex > (defList.size() - 1)) {
			defIndex = 0;
		}
		return defIndex;
	}

	// 初始化默认消息列表
	private void initDefData() {
		for (int i = 0; i < defContents.length; i++) {
			BarrageMsgBean bean = new BarrageMsgBean();
			bean.setUserId("");
			bean.setNickName("小U");
			bean.setContent(defContents[i]);
			bean.setTime(defExtras[i]);
			defList.add(bean);
		}
	}

	// 小U账户登录
	private void uLigin() {
		ObjChatMessage.connectToChatServer(uClient,
				new ObjAvimclientCallback() {

			@Override
			public void callback(AVIMClient client, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					return;
				}
				uClient = client;
				// 小U聊天登录成功
				getHisList(uClient);
			}
		});
	}

	// 获取历史记录
	public void getHisList(AVIMClient client) {
		ObjChatMessage.getConversationById(client, conversationId,
				new ObjCoversationCallback() {

			@Override
			public void callback(AVIMConversation conversation,
					AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					return;
				}
				conversation.queryMessages(500,
						new AVIMMessagesQueryCallback() {

					@Override
					public void done(List<AVIMMessage> msgs,
							AVIMException e) {
						// TODO Auto-generated method stub

						if (e != null) {
							return;
						}
						// 获取历史消息成功
						for (AVIMMessage message : msgs) {
							if (message instanceof AVIMTextMessage) {
								AVIMTextMessage msg = ((AVIMTextMessage) message);
								BarrageMsgBean bean = new BarrageMsgBean();
								bean.setContent(msg.getText());
								bean.setUserId(msg.getFrom());
								bean.setTime(DateUtils
										.getFormattedTimeInterval(msg
												.getTimestamp()));
								hisList.add(bean);
							}
						}
					}
				});
			}
		});
	}

	// 消息处理handler
	public class MessageHandler extends
	AVIMTypedMessageHandler<AVIMTypedMessage> {
		@Override
		public void onMessage(AVIMTypedMessage message,
				AVIMConversation conversation, AVIMClient client) {
			// TODO Auto-generated method stub
			super.onMessage(message, conversation, client);
			switch (message.getMessageType()) {
			case Constants.TEXT_TYPE:
				// 接收到实时消息
				if (conversation.getConversationId().equals(conversationId)) {
					AVIMTextMessage msg = ((AVIMTextMessage) message);
					BarrageMsgBean bean = new BarrageMsgBean();
					bean.setContent(msg.getText());
					bean.setUserId(msg.getFrom());
					bean.setTime(DateUtils.getFormattedTimeInterval(msg
							.getTimestamp()));
					realTimeList.add(bean);
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onMessageReceipt(AVIMTypedMessage message,
				AVIMConversation conversation, AVIMClient client) {
			// TODO Auto-generated method stub
			super.onMessageReceipt(message, conversation, client);
		}
	}


}
