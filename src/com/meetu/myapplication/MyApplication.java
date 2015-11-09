package com.meetu.myapplication;

import java.util.Set;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.meetu.cloud.object.ObjActivity;
import com.meetu.cloud.object.ObjActivityCover;
import com.meetu.cloud.object.ObjActivityFeedback;
import com.meetu.cloud.object.ObjActivityOrder;
import com.meetu.cloud.object.ObjActivityPhoto;
import com.meetu.cloud.object.ObjActivityPhotoPraise;
import com.meetu.cloud.object.ObjActivityPraise;
import com.meetu.cloud.object.ObjActivityTicket;
import com.meetu.cloud.object.ObjAuthoriseApply;
import com.meetu.cloud.object.ObjAuthoriseCategory;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.object.ObjUserPhotoPraise;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		log.e("AVOSCloud", "3254");
		//leancloud子类注册
		AVObject.registerSubclass(ObjActivity.class);
		AVObject.registerSubclass(ObjActivityPraise.class);
		AVObject.registerSubclass(ObjActivityPhoto.class);
		AVObject.registerSubclass(ObjActivityPhotoPraise.class);
		AVObject.registerSubclass(ObjActivityCover.class);
		AVObject.registerSubclass(ObjActivityOrder.class);
		AVObject.registerSubclass(ObjActivityTicket.class);
		AVObject.registerSubclass(ObjActivityFeedback.class);
		AVObject.registerSubclass(ObjUserPhoto.class);
		AVObject.registerSubclass(ObjUserPhotoPraise.class);
		AVObject.registerSubclass(ObjAuthoriseCategory.class);
		AVObject.registerSubclass(ObjAuthoriseApply.class);
		AVOSCloud.initialize(this,
				"tcd4rj3s3c54bdlkv1vfu5puvu9c2k96ur9kge3qvptqxp8p",
				"8fpp7j815746jg9x26f0d3c5p76xqkyqm586v2onvx3m2k7a");
		AVOSCloud.setDebugLogEnabled(true);
		// 注册默认的消息处理逻辑
		AVIMMessageManager
		.registerDefaultMessageHandler(new CustomMessageHandler());
	}


	public static class CustomMessageHandler extends AVIMMessageHandler {
		// 接收到消息后的处理逻辑
		@Override
		public void onMessage(AVIMMessage message,
				AVIMConversation conversation, AVIMClient client) {
			if (message instanceof AVIMTextMessage) {
				Log.d("Tom & Jerry", ((AVIMTextMessage) message).getText());
			}
		}

		public void onMessageReceipt(AVIMMessage message,
				AVIMConversation conversation, AVIMClient client) {

		}
	}

	public void jerryReceiveMsgFromTom() {
		// Jerry登录
		AVIMClient jerry = AVIMClient.getInstance("Jerry");
		jerry.open(new AVIMClientCallback() {

			@Override
			public void done(AVIMClient client, AVIMException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					// ...//登录成功后的逻辑
				}
			}
		});
	}

}
