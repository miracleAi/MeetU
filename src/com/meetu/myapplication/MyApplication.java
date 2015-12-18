package com.meetu.myapplication;

import java.util.Set;

import net.tsz.afinal.FinalBitmap;
import cc.imeetu.R;
import cn.beecloud.BCPay;
import cn.beecloud.BeeCloud;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.lidroid.xutils.BitmapUtils;
import com.meetu.cloud.callback.ObjAvimclientCallback;
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
import com.meetu.cloud.object.ObjChat;
import com.meetu.cloud.object.ObjGlobalAndroid;
import com.meetu.cloud.object.ObjReportChat;
import com.meetu.cloud.object.ObjReportUser;
import com.meetu.cloud.object.ObjScrip;
import com.meetu.cloud.object.ObjScripBox;
import com.meetu.cloud.object.ObjShieldUser;
import com.meetu.cloud.object.ObjSysMsg;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.object.ObjUserPhotoPraise;
import com.meetu.cloud.wrap.ObjChatMessage;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	public static AVIMClient chatClient;
	public static boolean isChatLogin = false;

	public BitmapUtils bitmapUtils = null;
	private FinalBitmap finalBitmap = null;
	private FinalBitmap finalBitmapHead=null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 配置bitmapUtils
		finalBitmap = FinalBitmap.create(this);
		finalBitmap.configBitmapLoadThreadSize(3);// 线程尺寸
		finalBitmap.configDiskCachePath(getFilesDir().toString());//
		finalBitmap.configDiskCacheSize(1024 * 1024 * 100);
		int memory = (int) Runtime.getRuntime().maxMemory() / 8;
		finalBitmap.configMemoryCacheSize(memory);
		finalBitmap.configLoadingImage(R.drawable.mine_img_loading);
	//	finalBitmap.configLoadfailImage(R.drawable.mine_img_loading);	
		// finalBitmap.configBitmapMaxHeight(bitmapHeight);
		/**
		 * 第三方支付相关
		 * */
		// 推荐在主Activity里的onCreate函数中初始化BeeCloud.
		BeeCloud.setAppIdAndSecret("3adc89a6-617f-4445-8f23-2b805df90fe4",
				"2f5add66-01cf-4024-9efe-e4c183f79205");

		// 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
		// 第二个参数需要换成你自己的微信AppID.
		String initInfo = BCPay.initWechatPay(getApplicationContext(), "wxc38cdfe5049cb17e");
		/**
		 * 第三方云服务相关
		 * */
		// leancloud子类注册
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
		AVObject.registerSubclass(ObjChat.class);
		AVObject.registerSubclass(ObjScrip.class);
		AVObject.registerSubclass(ObjScripBox.class);
		AVObject.registerSubclass(ObjSysMsg.class);
		AVObject.registerSubclass(ObjReportChat.class);
		AVObject.registerSubclass(ObjReportUser.class);
		AVObject.registerSubclass(ObjShieldUser.class);
		AVObject.registerSubclass(ObjGlobalAndroid.class);
		AVOSCloud.initialize(this,
				"tcd4rj3s3c54bdlkv1vfu5puvu9c2k96ur9kge3qvptqxp8p",
				"8fpp7j815746jg9x26f0d3c5p76xqkyqm586v2onvx3m2k7a");
		//调试开关
		//AVOSCloud.setDebugLogEnabled(true);
		AVIMMessageManager
		.registerDefaultMessageHandler(new DefaultMessageHandler(
				getApplicationContext()));
		 AVIMMessageManager.setConversationEventHandler(new
		 DefaultMemberHandler(getApplicationContext()));

		if (null != AVUser.getCurrentUser()) {
			chatClient = AVIMClient.getInstance(AVUser.getCurrentUser()
					.getObjectId());
			ObjChatMessage.connectToChatServer(chatClient,
					new ObjAvimclientCallback() {

				@Override
				public void callback(AVIMClient client, AVException e) {
					// TODO Auto-generated method stub
					if (e != null) {
						isChatLogin = false;
						log.e("zcq", "长连接登录失败");
						return;
					}
					chatClient = client;
					isChatLogin = true;
					log.e("zcq", "长连接登录成功");
				}
			});
		}
	}
/**
 * 取普通图片bitmap
 * @return  
 * @author lucifer
 * @date 2015-12-15
 */

	public FinalBitmap getFinalBitmap() {
		// TODO Auto-generated method stub
		return finalBitmap;
	}

}
