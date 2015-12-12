package com.meetu.activity.messages;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.activity.ReportActivity;
import com.meetu.activity.miliao.ChatGroupActivity;
import com.meetu.adapter.BoardPageFragmentAdapter;
import com.meetu.bean.UserAboutBean;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjReportUser;
import com.meetu.cloud.callback.ObjScripCallback;
import com.meetu.cloud.callback.ObjUserInfoCallback;
import com.meetu.cloud.object.ObjScrip;
import com.meetu.cloud.object.ObjScripBox;
import com.meetu.cloud.object.ObjShieldUser;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.utils.ChatMsgUtils;
import com.meetu.cloud.wrap.ObjReportWrap;
import com.meetu.cloud.wrap.ObjShieldUserWrap;
import com.meetu.cloud.wrap.ObjScriptWrap;
import com.meetu.cloud.wrap.ObjUserWrap;
import com.meetu.common.Constants;
import com.meetu.entity.Chatmsgs;
import com.meetu.fragment.MiliaoChannelFragment;
import com.meetu.fragment.NotesChannelFragment;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.ChatmsgsDao;
import com.meetu.sqlite.UserShieldDao;
import com.meetu.tools.DensityUtil;
import com.meetu.tools.DisplayUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NotesActivity extends FragmentActivity implements
OnPageChangeListener, OnClickListener {
	private ViewPager mViewPager;
	private BoardPageFragmentAdapter adapter = null;
	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	// 控件相关
	private RelativeLayout backLayout;
	private static RelativeLayout bottomLayout;
	private RelativeLayout disposeLayout;
	private RelativeLayout laheilayout, jubaolayout;
	private ImageView disposeImg;// 箭头
	TextView shieldTv, reportTv;
	private ImageView userHeadPhoto;
	private TextView userName;
	private RelativeLayout isShowLayout;
	private ImageView isShowImg;
	public static boolean isShow = true;// 是否显示小纸条消息
	// 表情键盘相关
	private LinearLayout sendLinearLayout;
	// private Boolean isShow=false;

	private int beforeID = 0;

	// 网络数据相关
	private ObjScripBox objScripBox = null;
	private List<ObjUser> objUsers = new ArrayList<ObjUser>();
	private AVUser currentUser = AVUser.getCurrentUser();
	// 当前用户
	private ObjUser user = new ObjUser();

	private String userId = "";// 对方id

	private FinalBitmap finalBitmap;
	// 标记是否屏蔽此用户
	private boolean isShield = false;

	List<ObjScrip> objScripsList = new ArrayList<ObjScrip>();

	ChatmsgsDao chatmsgsDao;

	MessageactivityHandler msgHandler;

	private ImageView favorImg;
	List<UserAboutBean> userAboutBeansList=new ArrayList<UserAboutBean>();
	private UserShieldDao shieldDao;
	
	private ImageView sendImageView;
	private int randomX=0,randomY=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除title
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		super.getWindow();
		setContentView(R.layout.activity_notes);
		objScripBox = (ObjScripBox) getIntent().getExtras().getSerializable(
				"ObjScripBox");
		shieldDao = new UserShieldDao(getApplicationContext());
		if (currentUser != null) {
			user = AVUser.cast(currentUser, ObjUser.class);
		} else {
			return;
		}
		initView();

		MyApplication app = (MyApplication) this.getApplicationContext();
		finalBitmap = app.getFinalBitmap();
		chatmsgsDao = new ChatmsgsDao(this);
		msgHandler = new MessageactivityHandler();
		objUsers = new ArrayList<ObjUser>();

		if (objScripBox != null) {

			objUsers.addAll(objScripBox.getUsers());

			for (ObjUser objUser : objUsers) {
				if (!user.getObjectId().equals(objUser.getObjectId())) {
					userId = objUser.getObjectId();
				}
			}
		}
		if (!userId.equals("")) {
			getUserInfo(userId);
		}

		mViewPager = (ViewPager) super.findViewById(R.id.mViewpager_notes);
		mViewPager.setOnPageChangeListener(this);
		initViewPager();

		// initViewPager();

		getScrips(objScripBox);



	}

	private void initViewPager() {

		for (int i = 0; i < objScripsList.size(); i++) {
			NotesChannelFragment frag = new NotesChannelFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("ObjScripBox", objScripBox);
			bundle.putSerializable("ObjScrip", objScripsList.get(i));
			frag.setArguments(bundle);
			fragmentList.add(frag);

		}
		adapter = new BoardPageFragmentAdapter(getSupportFragmentManager(),
				fragmentList);
		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setCurrentItem(0);

		mViewPager.setOnClickListener(this);

	}

	private void initView() {
		// TODO Auto-generated method stub
		sendImageView=(ImageView) findViewById(R.id.send_bottom_notes_img);
		sendImageView.setOnClickListener(this);
		sendLinearLayout = (LinearLayout) super
				.findViewById(R.id.bottom_notes_send_ll);
		backLayout = (RelativeLayout) super
				.findViewById(R.id.back_notes_top_rl);
		backLayout.setOnClickListener(this);

		bottomLayout = (RelativeLayout) super.findViewById(R.id.send_bottom_rl);
		disposeLayout = (RelativeLayout) super
				.findViewById(R.id.manage_notes_center_rl);
		disposeLayout.setOnClickListener(this);
		disposeImg = (ImageView) findViewById(R.id.manage_notes_center_img);
		userHeadPhoto = (ImageView) super
				.findViewById(R.id.photoHead_notes_top_img);
		userName = (TextView) findViewById(R.id.userName_notes_tv);
		isShowLayout = (RelativeLayout) findViewById(R.id.isShowScript_notes_rl);
		isShowLayout.setOnClickListener(this);
		isShowImg = (ImageView) findViewById(R.id.isShowScript_notes_img);
		favorImg=(ImageView) findViewById(R.id.favor_notes_top_img);


		if(userAboutBeansList==null||userAboutBeansList.size()==0){
			favorImg.setVisibility(View.GONE);
		}else{
			for(int i=0;i<userAboutBeansList.size();i++){
				if(userAboutBeansList.get(i).getAboutUserId().equals(userId)){
					favorImg.setVisibility(View.VISIBLE);
					break;
				}else{
					favorImg.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		visible();
		log.d("lucifer", "" + arg0);
		// NotesChannelFragment.dismissAll();

		// 获取系统软件盘的状态
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
		log.e("lucifer", "isOpen==" + isOpen);

		hideKeyBoard();

		// NotesChannelFragment.mEditText.clearFocus();

		// 暴力 拿到 fragment
		((NotesChannelFragment) fragmentList.get(beforeID)).isShowEditLayout();

		beforeID = arg0;

	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.mViewpager_notes:
			Toast.makeText(this, "shouye", Toast.LENGTH_SHORT).show();
			break;
		case R.id.back_notes_top_rl:
			finish();
			break;
		case R.id.manage_notes_center_rl:
			ObjUser otherUser;
			try {
				otherUser = AVUser.createWithoutData(ObjUser.class, userId);
				ObjShieldUserWrap.isShield(user, otherUser,
						new ObjFunBooleanCallback() {

							@Override
							public void callback(boolean result, AVException e) {
								// TODO Auto-generated method stub
								if (e != null) {
									Toast.makeText(getApplicationContext(),
											"操作失败", 1000).show();
									return;
								}
								if (result) {
									isShield = true;
								} else {
									isShield = false;
								}
								showPopWindow();
								popupWindow.showAsDropDown(v, 0, 0);
							}
						});
			} catch (AVException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;

		case R.id.isShowScript_notes_rl:
			// 显示or隐藏小纸条消息
			if (isShow == true) {
				isShow = false;
				isShowImg
				.setImageResource(R.drawable.massage_letters_show_btn_hide_hl);
				for (int i = 0; i < objScripsList.size(); i++) {
					// 暴力 拿到 fragment
					((NotesChannelFragment) fragmentList.get(i))
					.removeAllView();
				}

			} else {
				isShow = true;
				isShowImg
				.setImageResource(R.drawable.massage_letters_show_btn_hide_nor);
				for (int i = 0; i < objScripsList.size(); i++) {
					// 暴力 拿到 fragment
					((NotesChannelFragment) fragmentList.get(i)).showAllView();
				}

			}

			break;
			/**
			 * 点击发送随机发送按钮
			 */
		case R.id.send_bottom_notes_img:
			
			randomXY();
			log.e("zcq ", "randomX=="+randomX+" randomY=="+randomY);
			
			if(randomX<0){
				randomX=0;
			}
			if(randomY<=0){
				randomY=0;
			}
			
			((NotesChannelFragment) fragmentList.get(beforeID)).randowShowSendScript(randomX, randomY);
			
			break;
		default:
			break;
		}

	}

	private PopupWindow popupWindow;

	private void showPopWindow() {
		View view;
		if (popupWindow == null) {
			view = LayoutInflater.from(this).inflate(R.layout.item_laheijubao,
					null);
			popupWindow = new PopupWindow(view,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			// 设置外观
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			ColorDrawable colorDrawable = new ColorDrawable();
			popupWindow.setBackgroundDrawable(colorDrawable);
			shieldTv = (TextView) view.findViewById(R.id.lahei_item_tv);
			reportTv = (TextView) view.findViewById(R.id.jubao_item_tv);
			laheilayout = (RelativeLayout) view
					.findViewById(R.id.lahei_item_laheijubao_rl);
			jubaolayout = (RelativeLayout) view
					.findViewById(R.id.jubao_item_laheijubao_rl);
		}
		if (isShield) {
			shieldTv.setText("取消拉黑");
		} else {
			shieldTv.setText("拉黑Ta");
		}
		laheilayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.e("lucifer", "拉黑");
				if (isShield) {
					cancelShieldUser(userId);
				} else {
					shieldUser(userId);
				}
				popupWindow.dismiss();
			}
		});
		jubaolayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				log.e("lucifer", "举报");
				// 跳转到举报界面
				popupWindow.dismiss();
				Intent intent = new Intent(NotesActivity.this,
						ReportActivity.class);
				intent.putExtra("flag", "user");
				intent.putExtra("otherId", userId);
				startActivity(intent);
			}
		});
	}

	/**
	 * 拉黑用户
	 * */
	public void shieldUser(String userId) {
		ObjUser otherUser;
		try {
			otherUser = AVUser.createWithoutData(ObjUser.class, userId);
			ObjShieldUser shieldUser = new ObjShieldUser();
			shieldUser.setUser(user);
			shieldUser.setShieldUser(otherUser);
			ObjShieldUserWrap.shieldUser(shieldUser,
					new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if (e != null) {
						Toast.makeText(getApplicationContext(), "拉黑失败",
								1000).show();
						return;
					}
					if (result) {
						isShield = true;
						Toast.makeText(getApplicationContext(), "已拉黑",
								1000).show();
					} else {
						Toast.makeText(getApplicationContext(), "拉黑失败",
								1000).show();
					}
				}
			});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 取消拉黑
	 * */
	public void cancelShieldUser(String userId) {
		ObjUser otherUser;
		try {
			otherUser = AVUser.createWithoutData(ObjUser.class, userId);
			ObjShieldUserWrap.unShieldUser(user, otherUser,
					new ObjFunBooleanCallback() {

				@Override
				public void callback(boolean result, AVException e) {
					// TODO Auto-generated method stub
					if (e != null) {
						Toast.makeText(getApplicationContext(), "取消拉黑失败",
								1000).show();
						return;
					}
					if (result) {
						isShield = false;
						Toast.makeText(getApplicationContext(),
								"已取消拉黑", 1000).show();
					} else {
						Toast.makeText(getApplicationContext(), "取消拉黑失败",
								1000).show();
					}
				}
			});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 控制 底部布局 显示和隐藏
	 */
	public static void dismiss() {

		bottomLayout.setVisibility(View.GONE);

	}

	/**
	 * 控制 底部布局 显示
	 */
	public static void visible() {
		bottomLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 隐藏键盘
	 */
	protected void hideKeyBoard() {
		View curFocusView = getCurrentFocus();
		if (curFocusView != null) {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
			.hideSoftInputFromWindow(curFocusView.getWindowToken(), 0);
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

				if (user.getProfileClip() != null) {
					finalBitmap.display(userHeadPhoto, user.getProfileClip()
							.getUrl());
				}
				userName.setText(user.getNameNick());
				if (user.getGender() == 1) {

					// 根据性别设置图片
					Drawable nav_up = getResources().getDrawable(
							R.drawable.acty_joinlist_img_male);
					nav_up.setBounds(0, 0, nav_up.getMinimumWidth(),
							nav_up.getMinimumHeight());
					userName.setCompoundDrawables(null, null, nav_up, null);
					// holder.sexViewImg.setImageResource(R.drawable.massage_letters_img_line_girl);
				}

			}
		});
	}

	// 获取纸条箱内所有小纸条
	public void getScrips(ObjScripBox box) {
		objScripsList = new ArrayList<ObjScrip>();
		ObjScriptWrap.queryAllScrip(box, new ObjScripCallback() {

			@Override
			public void callback(List<ObjScrip> objects, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					log.e("zcq", e);
					return;
				}
				if (objects.size() > 0) {
					// 成功
					objScripsList.addAll(objects);
					log.e("zcq 纸条数量", "" + objects.size());
					handler.sendEmptyMessage(1);

				} else {

				}
			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:

				initViewPager();
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AVIMMessageManager.unregisterMessageHandler(AVIMTypedMessage.class,
				msgHandler);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,
				msgHandler);
	}

	/**
	 * 用来接收消息的handle
	 * 
	 * @author lucifer
	 * 
	 */
	public class MessageactivityHandler extends
	AVIMTypedMessageHandler<AVIMTypedMessage> {

		@Override
		public void onMessage(AVIMTypedMessage message,
				AVIMConversation conversation, AVIMClient client) {
			super.onMessage(message, conversation, client);
			// 请按自己需求改写
			switch (message.getMessageType()) {
			case Constants.TEXT_TYPE:
				log.e("zcq",
						"接收到一条文本消息" + " MessageId()==" + message.getMessageId());

				createChatMsg(conversation, message);

				break;
			case Constants.IMAGE_TYPE:
				// createChatPicMsg(conversation,message);
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

	public void createChatMsg(AVIMConversation conversation,
			AVIMTypedMessage message) {
		// TODO Auto-generated method stub
		AVIMTextMessage msg = ((AVIMTextMessage) message);
		Chatmsgs chatBean = new Chatmsgs();

		chatBean.setUid(user.getObjectId());
		chatBean.setMessageCacheId(String.valueOf(System.currentTimeMillis()));
		chatBean.setClientId(msg.getFrom());
		chatBean.setMessageId(msg.getMessageId());
		chatBean.setConversationId(msg.getConversationId());
		chatBean.setChatMsgDirection(ChatMsgUtils.getDerection(msg
				.getMessageIOType()));
		chatBean.setChatMsgStatus(ChatMsgUtils.getStatus(msg.getMessageStatus()));
		// boolean b = (Boolean) msg.getAttrs().get(Constants.IS_SHOW_TIME);
		// chatBean.setIsShowTime(ChatMsgUtils.geRecvTimeIsShow(b));
		String scriptId = (String) msg.getAttrs().get(Constants.SCRIP_ID);
		int scriptX = (Integer) msg.getAttrs().get(Constants.SCRIP_X);
		int scriptY = (Integer) msg.getAttrs().get(Constants.SCRIP_Y);
		log.e("zcq 接受", "scriptId==" + scriptId + " x==" + scriptX + " y=="
				+ scriptY);
		chatBean.setScriptId(scriptId);
		chatBean.setScripX(scriptX);
		chatBean.setScripY(scriptY);
		chatBean.setSendTimeStamp(String.valueOf(msg.getTimestamp()));
		chatBean.setDeliveredTimeStamp(String.valueOf(msg.getReceiptTimestamp()));
		chatBean.setContent(msg.getText());
		int style = (Integer) msg.getAttrs().get(Constants.CHAT_MSG_TYPE);
		// //我接受别人的消息
		// if(style==0&&ChatMsgUtils.getDerection(msg.getMessageIOType())==Constants.IOTYPE_IN){
		//
		// chatBean.setChatMsgType(12);
		// }else{
		// //接收到自己发的消息
		// chatBean.setChatMsgType(10);
		// }
		// 接收到本人id 发送的消息 不插入到本地消息数据库

		if (!user.getObjectId().equals(msg.getFrom())) {
			chatmsgsDao.insert(chatBean);
			log.e("lucifer", "插入成功");
		}

		if (conversation.getConversationId().equals(
				objScripBox.getConversationId())) {
			// 测试显示 刷新adapter
			((NotesChannelFragment) fragmentList.get(beforeID)).showAllView();
			// handler.sendEmptyMessage(1);

		} else {
			// 未读消息加1
			// messagesDao.updateUnread(user.getObjectId(),
			// msg.getConversationId());
		}

	}
	/**
	 * 
	 *   
	 * @author lucifer
	 * @date 2015-12-12
	 */
	public void randomXY(){
	int noteHight = DisplayUtils.getWindowHeight(this)
				- DensityUtil.dip2px(this, 190);		
	    randomX = 0+ (int)(Math.random()*(DisplayUtils.getWindowWidth(this)-DensityUtil.dip2px(this, 69)) );         
	    
		randomY=0+ (int)(Math.random()*(noteHight-DensityUtil.dip2px(this, 69)) );  
		
	}

}
