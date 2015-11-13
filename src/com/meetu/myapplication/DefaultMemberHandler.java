package com.meetu.myapplication;

import java.util.List;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;

public class DefaultMemberHandler extends AVIMConversationEventHandler{

	@Override
	public void onInvited(AVIMClient arg0, AVIMConversation arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKicked(AVIMClient arg0, AVIMConversation arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMemberJoined(AVIMClient arg0, AVIMConversation arg1,
			List<String> arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMemberLeft(AVIMClient arg0, AVIMConversation arg1,
			List<String> arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}

}
