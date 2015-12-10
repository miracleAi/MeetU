package com.meetu;

import java.util.ArrayList;
import java.util.List;

import cc.imeetu.R;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.meetu.cloud.callback.ObjConversationListCallback;
import com.meetu.cloud.callback.ObjSysMsgListCallback;
import com.meetu.cloud.object.ObjSysMsg;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.cloud.wrap.ObjSysMsgWrap;
import com.meetu.entity.Messages;
import com.meetu.myapplication.MyApplication;
import com.meetu.sqlite.MessagesDao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TestMsgTwoActivity extends Activity {
	private static final String LOADFAIL = "loadfail";
	private static final String LOADSUC = "loadsuc";
	private static final String LOADING = "loading";
	private static final String GETCOVERSATION = "getConversation";
	private static final String QUERYLOCALCOVERSATION = "queryLocalConversation";
	private static final String UPDATEUNREAD = "updateUnread";
	private static final String TESTQUERY = "testQuery";
	private static final String GETSYSMSG = "getSysMsg";
	Button clickBtn;
	TextView countTv, infoTv;
	MessagesDao messageDao = null;
	ObjUser user = null;
	String conversationId = "";
	ArrayList<Messages> msgList = new ArrayList<Messages>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_msg2_layout);
		messageDao = new MessagesDao(this);
		if (ObjUser.getCurrentUser() != null) {
			AVUser currentUser = ObjUser.getCurrentUser();
			user = AVUser.cast(currentUser, ObjUser.class);
		} else {
			Toast.makeText(getApplicationContext(), "please login", 1000)
					.show();
		}
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		countTv = (TextView) findViewById(R.id.count_tv);
		infoTv = (TextView) findViewById(R.id.info_tv);
		clickBtn = (Button) findViewById(R.id.click);
		clickBtn.setText(GETCOVERSATION);
		// clickBtn.setText(GETSYSMSG);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (clickBtn.getText().toString().equals(GETCOVERSATION)) {
					clickBtn.setText(LOADING);
					getConversation();
					return;
				}
				if (clickBtn.getText().toString().equals(QUERYLOCALCOVERSATION)) {
					clickBtn.setText(LOADING);
					queryLocalCinv();
					return;
				}
				if (clickBtn.getText().toString().equals(UPDATEUNREAD)) {
					clickBtn.setText(LOADING);
					updateUnread();
					return;
				}
				if (clickBtn.getText().toString().equals(TESTQUERY)) {
					clickBtn.setText(LOADING);
					testQuery();
					return;
				}
				if (clickBtn.getText().toString().equals(GETSYSMSG)) {
					clickBtn.setText(LOADING);
					getSysMsgs(user);
					return;
				}
			}
		});
	}

	// 获取系统消息
	public void getSysMsgs(ObjUser user) {
		ObjSysMsgWrap.querySysMsgs(user, new ObjSysMsgListCallback() {

			@Override
			public void callback(List<ObjSysMsg> objects, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					return;
				}
				if (objects.size() > 0) {
					countTv.setText(objects.get(0).getUser().getObjectId());
					clickBtn.setText(LOADSUC);
				} else {
					clickBtn.setText(LOADSUC);
				}
			}
		});
	}

	// 获取所有会话,保存至数据库
	public void getConversation() {
		ObjChatMessage.getConversation(user.getObjectId(),
				MyApplication.chatClient, new ObjConversationListCallback() {

					@Override
					public void callback(List<AVIMConversation> objects,
							AVException e) {
						// TODO Auto-generated method stub
						if (e != null) {
							clickBtn.setText(LOADFAIL);
							return;
						}
						log.d("mytest", "conversation:===" + objects);
						log.d("mytest", "conversation:===" + objects.size());
						clickBtn.setText(QUERYLOCALCOVERSATION);
						ArrayList<AVIMConversation> convList = new ArrayList<AVIMConversation>();
						for (AVIMConversation conversation : objects) {
							convList.add(conversation);
						}
						if (convList.size() > 0) {
							// 用第一条测试修改
							conversationId = convList.get(0)
									.getConversationId();
							// 保存到数据库
							ArrayList<Messages> list = new ArrayList<Messages>();
							for (int i = 0; i < convList.size(); i++) {
								AVIMConversation conversation = convList.get(i);
								Messages msg = new Messages();
								msg.setUserId(user.getObjectId());
								msg.setConversationID(conversation
										.getConversationId());
								int type = (Integer) conversation
										.getAttribute("cType");
								msg.setConversationType(type);
								String id = (String) conversation
										.getAttribute("appendId");
								String title = (String) conversation
										.getAttribute("title");
								long overTime = (Long) conversation
										.getAttribute("overTime");
								if (type == 1) {
									msg.setActyId(id);
									msg.setActyName(title);
								} else {
									msg.setChatId(id);
									msg.setChatName(title);
								}
								msg.setTimeOver(overTime);
								msg.setCreatorID(convList.get(i).getCreator());
								msg.setUpdateTime(System.currentTimeMillis());
								// 插入时所有标记为未踢出
								msg.setTiStatus(0);
								list.add(msg);
							}
							countTv.setText("eeee" + list.size());
							messageDao.insertList(list);
						}
					}
				});

	}

	// 查询数据库会话
	public void queryLocalCinv() {
		ArrayList<Messages> list = messageDao.getMessages(user.getObjectId());
		if (list != null && list.size() > 0) {
			msgList.addAll(list);
			infoTv.setText("count" + list.get(0).getConversationID());
			clickBtn.setText(UPDATEUNREAD);
		} else {
			clickBtn.setText(LOADFAIL);
		}

	}

	// 修改未读条数+1
	public void updateUnread() {
		messageDao.updateUnread(user.getObjectId(), conversationId);
		clickBtn.setText(TESTQUERY);
	}

	// 测试查询
	public void testQuery() {
		ArrayList<Messages> msgList = messageDao.getMessage(user.getObjectId(),
				conversationId);
		log.d("mytest", "msg" + msgList);
		infoTv.setText("count" + msgList.get(0).getUnreadMsgCount());
		clickBtn.setText(LOADSUC);
	}
}
