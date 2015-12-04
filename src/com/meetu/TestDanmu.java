package com.meetu;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.bean.BarrageMsgBean;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjCoversationCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.utils.DateUtils;
import com.meetu.cloud.wrap.ObjActivityWrap;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.common.Constants;
import com.meetu.myapplication.MyApplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestDanmu extends Activity {
	private TextView showTv;
	private Button clickBtn;
	MyRunnable myRunnable = null;
	Handler handler = null;
	AVIMClient uClient = AVIMClient.getInstance("5630843900b0ec3f9c9d2e03");
	// 弹幕显示用列表，默认，历史，实时
	ArrayList<BarrageMsgBean> defList = new ArrayList<BarrageMsgBean>();
	ArrayList<BarrageMsgBean> hisList = new ArrayList<BarrageMsgBean>();
	ArrayList<BarrageMsgBean> realTimeList = new ArrayList<BarrageMsgBean>();
	// 默认列表内容
	String[] defContents = { "弹幕，是一场盛大的文字直播", "活动未开始，弹幕便是活动群聊广场",
			"活动进行时，弹幕化身现场直播解说员", "你在看弹幕，发弹幕的人也在看你", "让我们的欢乐，感染你的生活" };
	// 默认列表额外内容
	String[] defExtras = { "", "", "", "", "" };
	int defIndex = 0;
	int hisIndex = 0;
	String conversationId = "5623af6560b2ce30d24a2c67";
	String activityId = "55e2b23a60b291d784a12ccb";
	ObjUser user = null;
	long lastMsgTime = 0;
	MessageHandler msgHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_danmu_layout);
		myRunnable = new MyRunnable();
		handler = new Handler();
		msgHandler = new MessageHandler();
		initView();
		if (AVUser.getCurrentUser() == null) {
			// 未登录
			return;
		} else {
			user = AVUser.cast(ObjUser.getCurrentUser(), ObjUser.class);
		}
		isChatLogin();
		initDefData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class,
				msgHandler);
	}

	@Override
	protected void onPause() {
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
					Log.d("mytest", "小U退出聊天登录成功");
				} else {
					Log.d("mytest", "小U退出聊天登录成功失败");
				}
			}
		});
		super.onPause();
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
							Log.d("mytest", "本人聊天登录");
							isOrder();
						} else {
							Log.d("mytest", "本人聊天未登录");
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
						Log.d("mytest", "本人聊天登录成功");
						isOrder();
					}
				});
	}

	private void initView() {
		// TODO Auto-generated method stub
		showTv = (TextView) findViewById(R.id.show_tv);
		clickBtn = (Button) findViewById(R.id.click);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (clickBtn.getText().toString().equals("start")) {
					clickBtn.setText("stop");
					handler.post(myRunnable);
					return;
				}
				if (clickBtn.getText().toString().equals("stop")) {
					clickBtn.setText("start");
					handler.removeCallbacks(myRunnable);
					return;
				}
			}
		});
	}

	class MyRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			showTv.setText(getMsg().getContent());
			handler.postDelayed(myRunnable, 1000);
		}
	}

	// 取得弹幕显示信息
	public BarrageMsgBean getMsg() {
		BarrageMsgBean msgBean = new BarrageMsgBean();
		// 优先显示实时消息
		if (realTimeList.size() > 0) {
			msgBean = realTimeList.get(0);
			realTimeList.remove(msgBean);
			hisList.add(msgBean);
			lastMsgTime = System.currentTimeMillis();
			return msgBean;
		}
		// 没有实时消息，默认消息播放超过15秒，播放历史消息
		if ((System.currentTimeMillis() - lastMsgTime) > (15 * 1000)
				&& hisList.size() > 0) {
			msgBean = hisList.get(getHisIndex());
			hisIndex++;
			return msgBean;
		}
		// 没有实时消息，穿插默认消息
		msgBean = defList.get(getDefIndex());
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
			bean.setContent(defContents[i]);
			bean.setTime(defExtras[i]);
			defList.add(bean);
		}
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
								Log.d("mytest", "本人聊天登录成功且已报名");
								getHisList(MyApplication.chatClient);
							} else {
								uLigin();
							}
						}
					});
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
						Log.d("mytest", "小U聊天登录成功");
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
										Log.d("mytest",
												"获取历史消息成功" + msgs.size());
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
										Log.d("mytest",
												"获取历史消息成功" + hisList.size());
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
				Log.d("mytest", "接收到实时消息");
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
