package com.meetu;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.meetu.cloud.callback.ObjConversationListCallback;
import com.meetu.cloud.object.ObjUser;
import com.meetu.cloud.wrap.ObjChatMessage;
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

public class TestMsgTwoActivity extends Activity{
	private static final String LOADFAIL = "loadfail";
	private static final String LOADSUC = "loadsuc";
	private static final String LOADING = "loading";
	private static final String GETCOVERSATION = "getConversation";
	private static final String QUERYLOCALCOVERSATION = "queryLocalConversation";
	private static final String UPDATEUNREAD = "updateUnread";
	Button clickBtn;
	TextView countTv,infoTv;
	MessagesDao messageDao = null;
	ObjUser user = null;
	String conversationId = "";
	ArrayList<Messages> msgList = new ArrayList<Messages>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_msg2_layout);
		messageDao = new MessagesDao(getApplicationContext());
		if(ObjUser.getCurrentUser() != null){
			AVUser currentUser = ObjUser.getCurrentUser();
			user = AVUser.cast(currentUser, ObjUser.class);
		}else{
			Toast.makeText(getApplicationContext(), "please login", 1000).show();
		}
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		countTv = (TextView) findViewById(R.id.count_tv);
		infoTv = (TextView) findViewById(R.id.info_tv);
		clickBtn = (Button) findViewById(R.id.click);
		clickBtn.setText(GETCOVERSATION);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(clickBtn.getText().toString().equals(GETCOVERSATION)){
					//查询权限分类，判断是否需要权限
					clickBtn.setText(LOADING);
					getConversation();
					return;
				}
				if(clickBtn.getText().toString().equals(QUERYLOCALCOVERSATION)){
					//查询权限分类，判断是否需要权限
					clickBtn.setText(LOADING);
					queryLocalCinv();
					return;
				}
				if(clickBtn.getText().toString().equals(UPDATEUNREAD)){
					//查询权限分类，判断是否需要权限
					clickBtn.setText(LOADING);
					updateUnread();
					return;
				}
			}
		});
	}
	//获取所有会话,保存至数据库
	public void getConversation(){
		ObjChatMessage.getConversation(MyApplication.chatClient, new ObjConversationListCallback() {

			@Override
			public void callback(List<AVIMConversation> objects, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					clickBtn.setText(LOADFAIL);
					return;
				}
				clickBtn.setText(QUERYLOCALCOVERSATION);
				countTv.setText(objects.size());
				if(objects.size()>0){/*
					//用第一条测试修改
					conversationId = objects.get(0).getConversationId();
					//保存到数据库
					ArrayList<Messages> list = new ArrayList<Messages>();
					for(int i=0;i<objects.size();i++){
						Messages msg = new Messages();
						msg.setUserId(user.getObjectId());
						msg.setConversationID(objects.get(i).getConversationId());
						int type = (Integer) objects.get(i).getAttribute("cType");
						msg.setConversationType(type);
						String id = (String) objects.get(i).getAttribute("appendId");
						String title = (String) objects.get(i).getAttribute("title");
						long overTime = (Long) objects.get(i).getAttribute("overTime");
						if(type == 1){
							msg.setActyId(id);
							msg.setActyName(title);
						}else{
							msg.setChatId(id);
							msg.setChatName(title);
						}
						msg.setTimeOver(overTime);
						msg.setCreatorID(objects.get(i).getCreator());
						msg.setTiStatus(0);
						list.add(msg);
					}
					messageDao.insertList(list);
				*/}
			}
		});
	}
	//查询数据库会话
	public void queryLocalCinv(){
		ArrayList<Messages> list = messageDao.getMessages(user.getObjectId());
		if(list != null && list.size()>0){
			msgList.addAll(list);
			infoTv.setText("count"+list.get(0).getUnreadMsgCount());
			clickBtn.setText(UPDATEUNREAD);
		}else{
			clickBtn.setText(LOADFAIL);
		}
		
	}
	//修改未读条数
	public void updateUnread(){
		messageDao.updateUnread(user.getObjectId(), conversationId);
		clickBtn.setText(QUERYLOCALCOVERSATION);
	}
}
