package com.meetu;

import java.util.ArrayList;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.meetu.bean.BarrageMsgBean;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.myapplication.DefaultMessageHandler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestDanmu extends Activity{
	private TextView showTv;
	private Button clickBtn;
	MyRunnable myRunnable = null;
	Handler handler = null;
	AVIMClient uClient  = AVIMClient.getInstance("5630843900b0ec3f9c9d2e03");	
	ArrayList<BarrageMsgBean> defList = new ArrayList<BarrageMsgBean>();
	ArrayList<BarrageMsgBean> hisList = new ArrayList<BarrageMsgBean>();
	ArrayList<BarrageMsgBean> realTimeList = new ArrayList<BarrageMsgBean>();
	BarrageMsgBean msgBean = new BarrageMsgBean();
	//默认列表内容
	String[] defContents = {"弹幕，是一场盛大的文字直播","活动未开始，弹幕便是活动群聊广场","活动进行时，弹幕化身现场直播解说员",
			"你在看弹幕，发弹幕的人也在看你","让我们的欢乐，感染你的生活"};
	//默认列表额外内容
	String[] defExtras = {"","","","",""};
	int defIndex = 0;
	int hisDex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_danmu_layout);
		myRunnable = new MyRunnable();
		handler = new Handler();
		uLigin();
		initDefDate();
		initView();
	}
	//初始化默认消息列表
	private void initDefDate() {
		for(int i=0;i<defContents.length;i++){
			BarrageMsgBean bean = new BarrageMsgBean();
			bean.setNickname("小U");
			bean.setContent(defContents[i]);
			bean.setTime(defExtras[i]);
			defList.add(bean);
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		showTv = (TextView) findViewById(R.id.show_tv);
		clickBtn = (Button) findViewById(R.id.click);
		clickBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(clickBtn.getText().toString().equals("start")){
					clickBtn.setText("stop");
					handler.post(myRunnable);
					return;
				}
				if(clickBtn.getText().toString().equals("stop")){
					clickBtn.setText("start");
					handler.removeCallbacks(myRunnable);
					return;
				}
			}
		});
	}
	class MyRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			showTv.setText(getMsg().getContent());
			handler.postDelayed(myRunnable, 2000);
		}
	}
	public BarrageMsgBean getMsg(){

		return msgBean;
	}
	private void uLigin() {
		ObjChatMessage.connectToChatServer(uClient, new ObjAvimclientCallback() {

			@Override
			public void callback(AVIMClient client, AVException e) {
				// TODO Auto-generated method stub
				if(e != null){
					return ;
				}
				uClient = client;
			}
		});
	}
}
