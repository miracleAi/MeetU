package com.meetu.myapplication;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.avos.avoscloud.im.v2.AVIMClient.AVIMClientStatus;
import com.avos.avoscloud.im.v2.callback.AVIMClientStatusCallback;
import com.meetu.cloud.callback.ObjAvimclientCallback;
import com.meetu.cloud.wrap.ObjChatMessage;

public class ClientEventHandler extends AVIMClientEventHandler{

	@Override
	public void onConnectionPaused(AVIMClient arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnectionResume(AVIMClient arg0) {
		// TODO Auto-generated method stub

	}

}
