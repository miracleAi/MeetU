package com.meetu.common;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil.log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.callback.ObjFunBooleanCallback;
import com.meetu.cloud.wrap.ObjChatMessage;
import com.meetu.myapplication.MyApplication;

/**
 * 建立聊天长连接
 * 
 * @author lucifer
 * @date 2015-11-24
 * @return
 */
public class ChatConnection {
	public static AVIMClient chatClient;

	/**
	 * 
	 * 建立聊天长连接
	 * 
	 * @author lucifer
	 * @date 2015-11-24
	 */
	public static void isConnection() {
		chatClient = AVIMClient.getInstance(AVUser.getCurrentUser()
				.getObjectId());
		ObjChatMessage.getClientStatus(chatClient, new ObjFunBooleanCallback() {

			@Override
			public void callback(boolean result, AVException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					log.e("zcq", e);
					return;
				} else if (result) {
					log.e("zcq", "已经建立过长连接");
				} else {
					ObjChatMessage.connectToChatServer(chatClient,
							new ObjAvimclientCallback() {

								@Override
								public void callback(AVIMClient client,
										AVException e) {
									if (e != null) {
										log.e("zcq", e);
										return;
									}
									if (client != null) {
										MyApplication.chatClient = client;
										log.e("zcq", "连接聊天长连接成功");

									} else {
										log.e("zcq", "连接聊天长连接失败");

									}
								}
							});

				}
			}
		});
	}
}
