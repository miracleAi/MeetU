package com.meetu.myapplication;

import java.util.Set;

import net.tsz.afinal.FinalBitmap;

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
import com.meetu.R;
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
import com.meetu.cloud.object.ObjScrip;
import com.meetu.cloud.object.ObjScripBox;
import com.meetu.cloud.object.ObjUserPhoto;
import com.meetu.cloud.object.ObjUserPhotoPraise;
import com.meetu.cloud.wrap.ObjChatMessage;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
	public static AVIMClient chatClient;
	public static boolean isChatLogin = false;
	
	
	public BitmapUtils bitmapUtils=null;
	private FinalBitmap finalBitmap=null;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//配置bitmapUtils
		finalBitmap=FinalBitmap.create(this);
		finalBitmap.configBitmapLoadThreadSize(3);//线程尺寸
		finalBitmap.configDiskCachePath(getFilesDir().toString());//
		finalBitmap.configDiskCacheSize(1024*1024*100);
		int memory=(int)Runtime.getRuntime().maxMemory()/8;
		finalBitmap.configMemoryCacheSize(memory);
		finalBitmap.configLoadingImage(R.drawable.mine_img_loading);
		finalBitmap.configLoadfailImage(R.drawable.mine_img_loading);
		//finalBitmap.configBitmapMaxHeight(bitmapHeight);
		
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
		AVObject.registerSubclass(ObjChat.class);
		AVObject.registerSubclass(ObjScrip.class);
		AVObject.registerSubclass(ObjScripBox.class);
		AVOSCloud.initialize(this,
				"tcd4rj3s3c54bdlkv1vfu5puvu9c2k96ur9kge3qvptqxp8p",
				"8fpp7j815746jg9x26f0d3c5p76xqkyqm586v2onvx3m2k7a");
		AVOSCloud.setDebugLogEnabled(true);

		if(null != AVUser.getCurrentUser()){
			chatClient = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
			ObjChatMessage.connectToChatServer(chatClient, new ObjAvimclientCallback() {

				@Override
				public void callback(AVIMClient client, AVException e) {
					// TODO Auto-generated method stub
					if(e != null){
						isChatLogin = false;
						return ;
					}
					chatClient = client;
					isChatLogin = true;
				}
			});
		} 
		AVIMMessageManager.registerDefaultMessageHandler(new DefaultMessageHandler());
	}
	public FinalBitmap getFinalBitmap() {
		// TODO Auto-generated method stub
		return finalBitmap;
	}
}
